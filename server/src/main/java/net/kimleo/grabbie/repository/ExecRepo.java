package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Execution;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExecRepo extends CrudRepository<Execution, Long> {
    public List<Execution> findByAgentId(Long id);

    public List<Execution> findByTaskId(Long id);

    public List<Execution> findByTaskIdAndExecuted(Long id, Boolean executed);

    public List<Execution> findByAgentIdAndExecuted(Long id, Boolean executed);

}
