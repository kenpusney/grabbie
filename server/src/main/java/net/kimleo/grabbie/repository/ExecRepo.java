package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Execution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExecRepo extends CrudRepository<Execution, Long> {
    List<Execution> findByAgentId(Long id);

    List<Execution> findByTaskId(Long id);

    List<Execution> findByTaskIdAndExecuted(Long id, Boolean executed);

    List<Execution> findByAgentIdAndExecuted(Long id, Boolean executed);

    Page<Execution> findAll(Pageable pageable);
}
