package ca.brocku.cosc.geneticprogramming.services;

import ca.brocku.cosc.geneticprogramming.models.Parameters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParamsService {

    List<Parameters> getAllParameters();
    Parameters saveNewParameters(Parameters parameters);
    Parameters getParameters(Long id);
    void deleteParameters(Long id);
    Parameters updateParameters(Parameters newParameters, Long id);
}
