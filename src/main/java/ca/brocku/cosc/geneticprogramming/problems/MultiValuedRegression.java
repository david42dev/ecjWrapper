/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ca.brocku.cosc.geneticprogramming.problems;

import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;


public class MultiValuedRegression extends GPProblem implements SimpleProblemForm {

	private static final long serialVersionUID = 1;

	public double currentX;
	public double currentY;

	private ArrayList<Double> inputX = new ArrayList();
	private ArrayList<Double> inputY = new ArrayList();
	private ArrayList<Double> inputExpected = new ArrayList();

	public void setup(final EvolutionState state,
					  final Parameter base) {
		super.setup(state, base);

		// verify our input is the right class (or subclasses from it)
		if (!(input instanceof DoubleData))
			state.output.fatal("GPData class must subclass from " + DoubleData.class,
					base.push(P_DATA), null);
		readFromFile();
	}

	public void evaluate(final EvolutionState state,
						 final Individual ind,
						 final int subpopulation,
						 final int threadnum) {
		if (!ind.evaluated)  // don't bother reevaluating
		{
			DoubleData input = (DoubleData) (this.input);

			int hits = 0;
			double sum = 0.0;
			double expectedResult;
			double result;
			for (int y = 0; y < inputX.size(); y++) {
				currentX = inputX.get(y);
				currentY = inputY.get(y);
				expectedResult = inputExpected.get(y);
				((GPIndividual) ind).trees[0].child.eval(
						state, threadnum, input, stack, ((GPIndividual) ind), this);

				result = Math.abs(expectedResult - input.x);
				if (result <= 0.01) hits++;
				sum += result;
			}

			// the fitness better be KozaFitness!
			KozaFitness f = ((KozaFitness) ind.fitness);
			f.setStandardizedFitness(state, sum);
			f.hits = hits;
			ind.evaluated = true;
		}
	}

	private void readFromFile() {
		File file = new File("trainingdata.txt");

		try {
			Scanner scan = new Scanner(file);
			//The first element is the X Value, the second Element the Y value and the third element the expected value
			scan.useLocale(Locale.US);

			while (scan.hasNextDouble()) {
				inputX.add(scan.nextDouble());
				inputY.add(scan.nextDouble());
				inputExpected.add(scan.nextDouble());
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}
}

