package ca.brocku.cosc.geneticprogramming.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
@Data
@AllArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue
    long id;
    long paramsId;
    int generation;
    int hits;
    double adjFitness;
    double avgFitness;
    double stdFitness;
    int run;

    public LogEntry(long paramsId, int run, int generation, int hits, double adjFitness, double avgFitness, double stdFitness){
        this.paramsId = paramsId;
        this.run = run;
        this.generation = generation;
        this.hits = hits;
        this.adjFitness = adjFitness;
        this.avgFitness = avgFitness;
        this.stdFitness = stdFitness;
    }
}
