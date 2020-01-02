package ca.brocku.cosc.geneticprogramming.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Entity
@Data
public class Parameters {
    @Id @GeneratedValue
    long id;

    int populationSize;
    int generationSize;
    int tournamentSize;
    int maxTreeSizeCrossover;
    double crossoverProbability;
    double mutationProbability;
    double reproductionProbability;
    int initialTreeDepthMin;
    int initialTreeDepthMax;
    double initialGrowProbability;
    double selectionNonTerminals;
    double selectionTerminals;
    int elitism;

    int numberOfRuns;

    @ElementCollection
    List<String> functions;
    @ElementCollection
    List<String> terminals;



}

