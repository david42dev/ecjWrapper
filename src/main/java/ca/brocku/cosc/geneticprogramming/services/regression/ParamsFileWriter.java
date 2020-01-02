package ca.brocku.cosc.geneticprogramming.services.regression;

import ca.brocku.cosc.geneticprogramming.models.Functions;
import ca.brocku.cosc.geneticprogramming.models.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ParamsFileWriter {

    String licence = "# Copyright 2006 by Sean Luke and George Mason University\n" +
            "# Licensed under the Academic Free License version 3.0\n" +
            "# See the file \"LICENSE\" for more information\n"
            +"# BASED ON KOZA PARAMS";
    @Autowired
    ResourceLoader resourceLoader;

    public void writeFile(Parameters params) {
        try {

            File file = new File("temporaryParamsFile.params");

            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(licence);
            printWriter.println(this.ecParams);
            printWriter.println(this.simpleParams);
            printWriter.println(this.kozaParams);

            printWriter.println("#additional params");
            printWriter.println("pop.subpop.0.size = " + params.getPopulationSize());
            printWriter.println("generations = " + params.getGenerationSize());
            printWriter.println("select.tournament.size = " + params.getTournamentSize());
            printWriter.println("gp.koza.xover.maxdepth = " + params.getMaxTreeSizeCrossover());
            printWriter.println("breed.elite.0 = " + params.getElitism());

            printWriter.println("# breeding pipelines, override from Koza.params:");
            printWriter.println("pop.subpop.0.species.pipe = ec.breed.MultiBreedingPipeline");
            printWriter.println("pop.subpop.0.species.pipe.generate-max = false");
            printWriter.println("gp.koza.half.min-depth = " + params.getInitialTreeDepthMin());
            printWriter.println("gp.koza.half.max-depth = " + params.getInitialTreeDepthMax());
            printWriter.println("gp.koza.half.growp = " + params.getInitialGrowProbability());

            printWriter.println("# Subsidiary pipelines:");
            printWriter.println("pop.subpop.0.species.pipe.num-sources = 3");
            printWriter.println("pop.subpop.0.species.pipe.source.0 = ec.gp.koza.CrossoverPipeline");
            printWriter.println("pop.subpop.0.species.pipe.source.0.prob = " + params.getCrossoverProbability());
            printWriter.println("pop.subpop.0.species.pipe.source.1 = ec.breed.ReproductionPipeline");
            printWriter.println("pop.subpop.0.species.pipe.source.1.prob =" + params.getReproductionProbability());
            printWriter.println("pop.subpop.0.species.pipe.source.2 =  ec.gp.koza.MutationPipeline");
            printWriter.println("pop.subpop.0.species.pipe.source.2.prob = " + params.getMutationProbability());

            printWriter.println("gp.koza.ns.terminals = " + params.getSelectionTerminals());
            printWriter.println("gp.koza.ns.nonterminals = " + params.getSelectionNonTerminals());

            printWriter.println("# We have one function set, of class GPFunctionSet\n");
            printWriter.println("gp.fs.size = 1");
            printWriter.println("gp.fs.0 = ec.gp.GPFunctionSet");

            printWriter.println("# We'll call the function set \"f0\".");
            printWriter.println("gp.fs.0.name = f0");

            printWriter.println("# We have five functions in the function set.  They are:");
            int functionSize = 0;

            for(String terminal: params.getTerminals()){
                printWriter.println("gp.fs.0.func." + functionSize + " = ca.brocku.cosc.geneticprogramming.problems.terminals." + terminal);
                printWriter.println("gp.fs.0.func." + functionSize + ".nc = nc0");
                ++functionSize;
            }

            for(String function: params.getFunctions()){
                printWriter.println("gp.fs.0.func." + functionSize + " = ca.brocku.cosc.geneticprogramming.problems.functions." + function);
                printWriter.println("gp.fs.0.func." + functionSize + ".nc = nc" + Functions.valueOfLabel(function).getNumberOfChildren());
                ++functionSize;
            }

            printWriter.println("gp.fs.0.size = " + functionSize);
            printWriter.println("eval.problem = ca.brocku.cosc.geneticprogramming.problems.MultiValuedRegression\n" +
                    "eval.problem.data = ca.brocku.cosc.geneticprogramming.problems.DoubleData");

            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final String ecParams =
            "evalthreads = \t\t\t\t1\n" +
            "breedthreads = \t\t\t\t1\n" +
            "seed.0 =\t\t\t\ttime\n" +
            "checkpoint =\t\t\t\tfalse\n" +
            "checkpoint-modulo =                     1\n" +
            "checkpoint-prefix = \t\t\tec";
    final String simpleParams =
            "state = \t\t\t\tec.simple.SimpleEvolutionState\n" +
            "init = \t\t\t\t\tec.simple.SimpleInitializer\n" +
            "finish = \t        \t\tec.simple.SimpleFinisher\n" +
            "exch = \t\t\t\t\tec.simple.SimpleExchanger\n" +
            "breed =\t\t\t\t\tec.simple.SimpleBreeder\n" +
            "eval =\t\t\t\t\tec.simple.SimpleEvaluator\n" +
            "stat =\t\t\t\t\tec.simple.SimpleStatistics\n" +
            "quit-on-run-complete =\t\t\ttrue\n" +
            "pop = \t\t\t\t\tec.Population\n" +
            "pop.subpops =\t\t\t\t1\n" +
            "pop.subpop.0 = \t\t\t\tec.Subpopulation\n" +
            "pop.subpop.0.duplicate-retries =\t0\n" +
            "stat.file\t\t\t\t$out.stat\n";
    final String kozaParams =
            "pop.subpop.0.species.fitness = ec.gp.koza.KozaFitness\n" +
            "init = ec.gp.GPInitializer\n" +
            "pop.subpop.0.species = ec.gp.GPSpecies\n" +
            "pop.subpop.0.species.ind = ec.gp.GPIndividual\n" +
            "pop.subpop.0.duplicate-retries = 100\n" +
            "pop.subpop.0.species.ind.numtrees = 1\n" +
            "pop.subpop.0.species.ind.tree.0 = ec.gp.GPTree\n" +
            "pop.subpop.0.species.ind.tree.0.tc = tc0\n" +
            "breed.reproduce.source.0 = ec.select.TournamentSelection\n" +
            "gp.koza.xover.source.0 = ec.select.TournamentSelection\n" +
            "gp.koza.xover.source.1 = same\n" +
            "gp.koza.xover.ns.0 = ec.gp.koza.KozaNodeSelector\n" +
            "gp.koza.xover.ns.1 = same\n" +
            "gp.koza.xover.tries = 1\n" +
            "gp.koza.mutate.source.0 = ec.select.TournamentSelection\n" +
            "gp.koza.mutate.ns.0 = ec.gp.koza.KozaNodeSelector\n" +
            "gp.koza.mutate.build.0 = ec.gp.koza.GrowBuilder\n" +
            "gp.koza.mutate.maxdepth = 17\n" +
            "gp.koza.mutate.tries = 1\n" +
            "gp.koza.grow.min-depth = 5\n" +
            "gp.koza.grow.max-depth = 5\n" +
            "gp.problem.stack = ec.gp.ADFStack\n" +
            "gp.adf-stack.context = ec.gp.ADFContext\n" +
            "gp.koza.ns.root = 0.0\n" +
            "gp.type.a.size = 1\n" +
            "gp.type.a.0.name = nil\n" +
            "gp.type.s.size = 0\n" +
            "gp.tc.size = 1\n" +
            "gp.tc.0 = ec.gp.GPTreeConstraints\n" +
            "gp.tc.0.name = tc0\n" +
            "gp.tc.0.fset = f0\n" +
            "gp.tc.0.returns = nil\n" +
            "gp.tc.0.init = ec.gp.koza.HalfBuilder\n" +
            "gp.nc.size = 7\n" +
            "gp.nc.0 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.0.name = nc0\n" +
            "gp.nc.0.returns = nil\n" +
            "gp.nc.0.size = 0\n" +
            "gp.nc.1 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.1.name = nc1\n" +
            "gp.nc.1.returns = nil\n" +
            "gp.nc.1.size = 1\n" +
            "gp.nc.1.child.0 = nil\n" +
            "gp.nc.2 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.2.name = nc2\n" +
            "gp.nc.2.returns = nil\n" +
            "gp.nc.2.size = 2\n" +
            "gp.nc.2.child.0 = nil\n" +
            "gp.nc.2.child.1 = nil\n" +
            "gp.nc.3 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.3.name = nc3\n" +
            "gp.nc.3.returns = nil\n" +
            "gp.nc.3.size = 3\n" +
            "gp.nc.3.child.0 = nil\n" +
            "gp.nc.3.child.1 = nil\n" +
            "gp.nc.3.child.2 = nil\n" +
            "gp.nc.4 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.4.name = nc4\n" +
            "gp.nc.4.returns = nil\n" +
            "gp.nc.4.size = 4\n" +
            "gp.nc.4.child.0 = nil\n" +
            "gp.nc.4.child.1 = nil\n" +
            "gp.nc.4.child.2 = nil\n" +
            "gp.nc.4.child.3 = nil\n" +
            "gp.nc.5 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.5.name = nc5\n" +
            "gp.nc.5.returns = nil\n" +
            "gp.nc.5.size = 5\n" +
            "gp.nc.5.child.0 = nil\n" +
            "gp.nc.5.child.1 = nil\n" +
            "gp.nc.5.child.2 = nil\n" +
            "gp.nc.5.child.3 = nil\n" +
            "gp.nc.5.child.4 = nil\n" +
            "gp.nc.6 = ec.gp.GPNodeConstraints\n" +
            "gp.nc.6.name = nc6\n" +
            "gp.nc.6.returns = nil\n" +
            "gp.nc.6.size = 6\n" +
            "gp.nc.6.child.0 = nil\n" +
            "gp.nc.6.child.1 = nil\n" +
            "gp.nc.6.child.2 = nil\n" +
            "gp.nc.6.child.3 = nil\n" +
            "gp.nc.6.child.4 = nil\n" +
            "gp.nc.6.child.5 = nil\n";
}
