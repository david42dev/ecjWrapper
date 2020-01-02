package ca.brocku.cosc.geneticprogramming.problems.functions;

import ca.brocku.cosc.geneticprogramming.problems.DoubleData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class EulerExponential extends GPNode {

	@Override
	public String toString() {
		return "EulerExponential";
	}

	public int expectedChildren() {
		return 1;
	}


	@Override
	public void eval(EvolutionState evolutionState, int thread, GPData gpData,
                     ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
		DoubleData rd = ((DoubleData) (gpData));
		children[0].eval(evolutionState, thread, gpData, adfStack, gpIndividual, problem);

		rd.x = Math.exp(rd.x);
		if (Double.isInfinite(rd.x)) {
			rd.x = 9001;
		}
	}
}
