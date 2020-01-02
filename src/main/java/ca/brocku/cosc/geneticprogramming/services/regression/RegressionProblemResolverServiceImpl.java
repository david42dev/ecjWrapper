package ca.brocku.cosc.geneticprogramming.services.regression;

import ca.brocku.cosc.geneticprogramming.models.*;
import ca.brocku.cosc.geneticprogramming.repositories.FinalResultRepository;
import ca.brocku.cosc.geneticprogramming.repositories.LogRepository;
import ca.brocku.cosc.geneticprogramming.repositories.ParametersRepository;
import ca.brocku.cosc.geneticprogramming.util.MySecurityManager;
import ec.Evolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegressionProblemResolverServiceImpl implements RegressionProblemEvolverService {

    private ParamsFileWriter paramsFileWriter;
    private ParametersRepository parametersRepository;
    private LogRepository logRepository;
    private FinalResultRepository finalResultRepository;
    private Parameters params;

    // the rest of the program (especially ecj) needs to know on which run we are currently on and which parameter
    // id we are using. If this backend should be updated to be multi agent friendly, this has to be changed.
    // For Example JMS or other message systems (MQTT etc.) could be used.
    public static int runCounter;
    public static long paramsID;


    @Autowired
    public RegressionProblemResolverServiceImpl(final ParamsFileWriter paramsFileWriter,
                                                final ParametersRepository parameterRepo,
                                                final LogRepository logRepository,
                                                final FinalResultRepository finalResultRepository
    ){
        this.paramsFileWriter = paramsFileWriter;
        this.parametersRepository = parameterRepo;
        this.logRepository = logRepository;
        this.finalResultRepository = finalResultRepository;
    }

    public List<String> getAllFunctions(){
        ArrayList<String> functions = new ArrayList<>();
        for (Functions f: Functions.values()){
            functions.add(f.getClassname());
        }
        return functions;
    }

    public List<String> getAllTerminals(){
        ArrayList<String> terminals = new ArrayList<>();
        for (Terminals t: Terminals.values()){
            terminals.add(t.getClassname());
        }
        return terminals;
    }

    @Override
    public List<FinalResults> runProblem(long id) {

        // since we want to find only one result to one paramfile to have them distinguishable we save an already
        // used paramsfile again to have a new id
        if(logRepository.findAllByParamsId(id).size()>0){
            params.setId(0);
            id = parametersRepository.save(params).getId();
        }


        params = parametersRepository.findById(id).get();
        paramsFileWriter.writeFile(params);
        MySecurityManager secManager = new MySecurityManager();
        System.setSecurityManager(secManager);

        //we need this to know which run we are on. E.G. to save our statistics with the run number
        runCounter = 0;
        paramsID = params.getId();




        // calling the ecj library

        while(runCounter < params.getNumberOfRuns()) {
            try {
                System.out.println("------------------------------------Regression ------------------------------------------");
                int numberOfJobs = 1;
                //String pathToLogs = ClassLoader.getSystemClassLoader().getResource("treeResults/").getPath();
                String time = "" + LocalDateTime.now();
                String statisticType = "ca.brocku.cosc.geneticprogramming.services.regression.CustomStatistics";
                String[] arguments1b = {
                        Evolve.A_FILE,
                        new File("temporaryParamsFile.params").getPath(),
                        "-p", ("stat=" + statisticType),
                        // "-p", ("stat.file=$" + pathToLogs + "assignment1output" + time.replace(":", ".") + ".stat"),
                        "-p", ("stat.file=$results/paramId" + paramsID+ ".run" + runCounter + ".stat"),
                        "-p", ("jobs=" + numberOfJobs)
                };
                Evolve.main(arguments1b);
                System.out.println("Problem Resolver called");
            } catch (SecurityException e) {
                // The ECJ Library calls "System.exit" Which has to be cached, if the java environment should not be closed
                System.out.println("system.exit() has been chatched");
            }
            ++runCounter;
        }

        //From here on we calculate the final results

        List<FinalResults> finalResultsList = new ArrayList<>();
        long idOfOverAllBestSolution = 0;
        double overAllBestFitness = 0;
        int bestRun = 0;

        for (int i = 0; i< params.getGenerationSize(); i++){
            FinalResults finalResult = new FinalResults(i,0,0,0,0,0,paramsID);

            for (int j = 0; j< params.getNumberOfRuns(); j++){
                LogEntry logEntry = logRepository.findByParamsIdAndRunAndGeneration(paramsID,j,i);
                /*
                 it can happen that our GP systems exited before reaching maximum generations, if it finds the best solution.
                 If so we copy the last optimal solution for the rest of the generations for the statistics
                 */
                if(logEntry == null){
                    LogEntry tmp = logRepository.findByParamsIdAndRunAndGeneration(paramsID,j,i-1);
                    logEntry = new LogEntry(tmp.getParamsId(),j,i,tmp.getHits(),tmp.getAdjFitness(),tmp.getAvgFitness(),tmp.getStdFitness());
                    logEntry = logRepository.save(logEntry);
                }


                finalResult.setAvgHits(finalResult.getAvgHits() + logEntry.getHits());
                finalResult.setAvgAdjFitness(finalResult.getAvgAdjFitness() + logEntry.getAdjFitness());
                finalResult.setAvgStdFitness(finalResult.getAvgStdFitness() + logEntry.getStdFitness());
                finalResult.setAvgFitnessOverAll(finalResult.getAvgFitnessOverAll() + logEntry.getAvgFitness());
                if(overAllBestFitness < logEntry.getAdjFitness()){
                    idOfOverAllBestSolution = logEntry.getId();
                    overAllBestFitness = logEntry.getAdjFitness();
                    bestRun = logEntry.getRun();
                }
            }

            finalResult.setAvgHits(finalResult.getAvgHits() / params.getNumberOfRuns());
            finalResult.setAvgAdjFitness(finalResult.getAvgAdjFitness() / params.getNumberOfRuns());
            finalResult.setAvgStdFitness(finalResult.getAvgStdFitness() / params.getNumberOfRuns());
            finalResult.setAvgFitnessOverAll(finalResult.getAvgFitnessOverAll() / params.getNumberOfRuns());
            finalResult.setOverallBestRun(bestRun);
            finalResultsList.add(finalResult);
        }

        if(idOfOverAllBestSolution != 0){
            LogEntry overAllBestLogEntry = logRepository.findById(idOfOverAllBestSolution).get();
            for (int i = 0; i< finalResultsList.size(); i++){
                finalResultsList.get(i).setOverallBest(
                        logRepository.findByParamsIdAndRunAndGeneration(
                                overAllBestLogEntry.getParamsId(),
                                overAllBestLogEntry.getRun(),
                                i).getAdjFitness()
                );
                finalResultsList.get(i).setOverallBestRun(overAllBestLogEntry.getRun());
                finalResultsList.set(i,finalResultRepository.save(finalResultsList.get(i)));
            }
        }

        return finalResultsList;
    }

}
