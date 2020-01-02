package ca.brocku.cosc.geneticprogramming.repositories;

import ca.brocku.cosc.geneticprogramming.models.Parameters;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersRepository extends CrudRepository<Parameters, Long> {

    @Override
    Iterable<Parameters> findAll();
}
