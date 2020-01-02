package ca.brocku.cosc.geneticprogramming.controller;

import ca.brocku.cosc.geneticprogramming.models.Parameters;
import ca.brocku.cosc.geneticprogramming.services.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ParametersController.BASE_URL)
@CrossOrigin("*")
public class ParametersController {
    static final String BASE_URL = "/parameters";

    private ParamsService paramsService;

    @Autowired
    public ParametersController(final ParamsService paramsService){
        this.paramsService = paramsService;
    }

    @GetMapping()
    List<Parameters> all() {
        return paramsService.getAllParameters();
    }

    @PostMapping()
    Parameters createParams(@RequestBody Parameters newParameters) {
        return paramsService.saveNewParameters(newParameters);
    }

    // Single item
    @GetMapping("/{id}")
    Parameters getParameter(@PathVariable Long id) {
        return paramsService.getParameters(id);
    }

    @PutMapping("/{id}")
    Parameters updateParameters(@RequestBody Parameters newParameters, @PathVariable Long id) {
        return paramsService.updateParameters(newParameters,id);
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id) {
        paramsService.deleteParameters(id);
    }

}


