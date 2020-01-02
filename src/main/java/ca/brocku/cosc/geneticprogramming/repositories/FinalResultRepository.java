package ca.brocku.cosc.geneticprogramming.repositories;

import ca.brocku.cosc.geneticprogramming.models.FinalResults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalResultRepository extends CrudRepository<FinalResults, Long>{
}
