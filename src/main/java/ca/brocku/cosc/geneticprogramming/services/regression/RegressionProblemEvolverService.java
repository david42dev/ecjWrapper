package ca.brocku.cosc.geneticprogramming.services.regression;

import ca.brocku.cosc.geneticprogramming.models.FinalResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegressionProblemEvolverService {

    List<FinalResults> runProblem(long id);
    List<String> getAllTerminals();
    List<String> getAllFunctions();

}
