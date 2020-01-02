package ca.brocku.cosc.geneticprogramming.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class FinalResults {

   @Id @GeneratedValue
   long id;

   int generation;
   int avgHits;
   double avgAdjFitness;
   double avgFitnessOverAll;
   double avgStdFitness;
   double overallBest;
   long parameterId;
   int overallBestRun;

   public FinalResults(int generation, int avgHits, double avgAdjFitness, double avgStdFitness,
                       double avgFitnessOverAll, double overallBest, long parameterId){
       this.generation = generation;
       this.avgHits = avgHits;
       this.avgAdjFitness = avgAdjFitness;
       this.avgStdFitness = avgStdFitness;
       this.avgFitnessOverAll = avgFitnessOverAll;
       this.overallBest = overallBest;
       this.parameterId = parameterId;
   }

}
