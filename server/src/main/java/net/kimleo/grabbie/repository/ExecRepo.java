package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Client;
import net.kimleo.grabbie.model.Execution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExecRepo extends CrudRepository<Execution, Long> {
    public List<Execution> findByClientId(Long id);

    public List<Execution> findByTaskId(Long id);

    public List<Execution> findByTaskIdAndExecuted(Long id, Boolean executed);

    public List<Execution> findByClientIdAndExecuted(Long id, Boolean executed);

}
