package ca.brocku.cosc.geneticprogramming.repositories;

import ca.brocku.cosc.geneticprogramming.models.LogEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<LogEntry, Long> {

    LogEntry findByParamsIdAndRunAndGeneration(long paramsId,int run, int generation);
    List<LogEntry> findAllByParamsId(long paramsId);
}
