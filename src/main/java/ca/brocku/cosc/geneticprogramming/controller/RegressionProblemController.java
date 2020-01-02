package ca.brocku.cosc.geneticprogramming.controller;

import ca.brocku.cosc.geneticprogramming.models.FinalResults;
import ca.brocku.cosc.geneticprogramming.services.regression.RegressionProblemEvolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RegressionProblemController.BASE_URL)
@CrossOrigin("*")
public class RegressionProblemController {
    static final String BASE_URL = "/regression";

    private RegressionProblemEvolverService regressionProblemEvolverService;

    @Autowired
    public RegressionProblemController(final RegressionProblemEvolverService regressionProblemEvolverService){
        this.regressionProblemEvolverService = regressionProblemEvolverService;
    }

    @GetMapping("/run/{id}")
    public List<FinalResults> runRegression(@PathVariable long id){
        return regressionProblemEvolverService.runProblem(id);
    }

    @GetMapping("/terminals")
    public List<String> getTerminals(){
        return regressionProblemEvolverService.getAllTerminals();
    }

    @GetMapping("/functions")
    public List<String> getFunctions(){
        return regressionProblemEvolverService.getAllFunctions();
    }
}

