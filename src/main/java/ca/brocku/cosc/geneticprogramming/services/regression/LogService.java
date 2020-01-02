package ca.brocku.cosc.geneticprogramming.services.regression;

import ca.brocku.cosc.geneticprogramming.models.LogEntry;
import ca.brocku.cosc.geneticprogramming.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    public void saveLog( int generation, int hits, double adjFitness, double avgFitness, double stdFitness){
        logRepository.save(new LogEntry(RegressionProblemResolverServiceImpl.paramsID,RegressionProblemResolverServiceImpl.runCounter,generation,hits,adjFitness,avgFitness,stdFitness));
    }
}
