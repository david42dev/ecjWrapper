package ca.brocku.cosc.geneticprogramming.services.regression;

import ca.brocku.cosc.geneticprogramming.util.SpringContext;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPNode;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleStatistics;
import ec.util.Parameter;


public class CustomStatistics extends SimpleStatistics {
// based on Tyler Cowan guide to make custom statistics
	boolean warned = false;

	private LogService getLogService() {
		return SpringContext.getBean(LogService.class);
	}

	@Override
	public void setup(EvolutionState state, Parameter base) {
		super.setup(state,base);
		state.output.println("Generation"
						+ "\t" + "best_individuals_adjusted_fitness"
						+ "\t" + "average_fitness_of_all_individuals"
						+ "\t" + "best_individuals_hits"
						+ "\t" + "best_individuals_std_fitness"
				, statisticslog);
	}
	@Override
	public void postEvaluationStatistics(final EvolutionState state) {
		// for now we just print the best fitness per subpopulation.
		Individual[] best_i = new Individual[state.population.subpops.length]; // quiets compiler complaints
		for (int x = 0; x < state.population.subpops.length; x++) {
			best_i[x] = state.population.subpops[x].individuals[0];
			for (int y = 1; y < state.population.subpops[x].individuals.length; y++) {
				if (state.population.subpops[x].individuals[y] == null) {
					if (!warned) {
						state.output.warnOnce("Null individuals found in subpopulation");
						warned = true; // we do this rather than relying on warnOnce because it is much faster in a tight loop
					}
				} else if (best_i[x] == null || state.population.subpops[x].individuals[y].fitness.betterThan(best_i[x].fitness)) {
					best_i[x] = state.population.subpops[x].individuals[y];
				}
				if (best_i[x] == null) {
					if (!warned) {
						state.output.warnOnce("Null individuals found in subpopulation");
						warned = true; // we do this rather than relying on warnOnce because it is much faster in a tight loop
					}
				}
			}
			// now test to see if it's the new best_of_run
			if (best_of_run[x] == null || best_i[x].fitness.betterThan(best_of_run[x].fitness)) {
				best_of_run[x] = (Individual) (best_i[x].clone());
			}
		}


		// main loop, prints once per generation
		for (int x = 0; x < state.population.subpops.length; x++) {
			// initializing average values
			float avg_fit = 0;
			// calculated average values for this generation
			for (int i = 0; i < state.population.subpops[x].individuals.length; i++) {
				avg_fit += ((KozaFitness) state.population.subpops[x].individuals[i].fitness).adjustedFitness();
			}
			avg_fit /= state.population.subpops[x].individuals.length;
			// printing information
			if (doGeneration) {
				state.output.println(state.generation
						+ "\t" +((KozaFitness) best_i[x].fitness).adjustedFitness()
						+ "\t" + avg_fit
						+ "\t" + (((KozaFitness) best_i[x].fitness).hits)
						+ "\t" + (((KozaFitness) best_i[x].fitness).standardizedFitness())
						, statisticslog);

				getLogService().saveLog(state.generation,
						(((KozaFitness) best_i[x].fitness).hits),
						((KozaFitness) best_i[x].fitness).adjustedFitness(), ((double)avg_fit),
						(((KozaFitness) best_i[x].fitness).standardizedFitness()));


			}

			if (doMessage && !silentPrint) {
				state.output.message("Subpop \t" + x + " best fitness of generation \t"
						+ (best_i[x].evaluated ? " " : " (evaluated flag not set): ")
						+ best_i[x].fitness.fitnessToStringForHumans());
			}
		}
	}

	// additional method added to calculate tree size. should be called with the tree's root as node.
	public int calc_tree_size(GPNode node) {
		int c = 1;
		for (GPNode n : node.children) {
			c += calc_tree_size(n);
		}
		return c;
	}
}
