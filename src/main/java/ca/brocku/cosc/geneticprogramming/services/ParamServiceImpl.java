package ca.brocku.cosc.geneticprogramming.services;

import ca.brocku.cosc.geneticprogramming.models.Parameters;
import ca.brocku.cosc.geneticprogramming.repositories.ParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParamServiceImpl implements ParamsService {

    private ParametersRepository parametersRepository;

    @Autowired
    public ParamServiceImpl(final ParametersRepository parametersRepository){
        this.parametersRepository = parametersRepository;
    }

    @Override
    public List<Parameters> getAllParameters() {
        return (List<Parameters>) parametersRepository.findAll();
    }

    @Override
    public Parameters saveNewParameters(Parameters parameters) {
        parameters.setId(0);
        return parametersRepository.save(parameters);
    }

    @Override
    public Parameters getParameters(Long id) {
        return parametersRepository.findById(id).get();
    }

    @Override
    public void deleteParameters(Long id) {
        parametersRepository.deleteById(id);
    }

    @Override
    public Parameters updateParameters(Parameters newParameters, Long id) {
        newParameters.setId(id);
        return parametersRepository.save(newParameters);
    }
}
