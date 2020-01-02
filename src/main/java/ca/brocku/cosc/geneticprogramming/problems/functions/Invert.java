package ca.brocku.cosc.geneticprogramming.problems.functions;

import ca.brocku.cosc.geneticprogramming.problems.DoubleData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Invert extends GPNode {

	public int expectedChildren() {
		return 1;
	}

	@Override
	public String toString() {
		return "invert";
	}

	@Override
	public void eval(EvolutionState evolutionState, int thread, GPData gpData,
                     ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

		DoubleData rd = ((DoubleData) (gpData));
		children[0].eval(evolutionState, thread, gpData, adfStack, gpIndividual, problem);

		rd.x = -rd.x;

	}

}
