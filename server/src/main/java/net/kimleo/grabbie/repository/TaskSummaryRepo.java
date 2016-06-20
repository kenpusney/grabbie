package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Task;
import net.kimleo.grabbie.model.summary.TaskSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TaskSummaryRepo extends CrudRepository<TaskSummary, Long> {
    TaskSummary findByTask(Task task);
    Page<TaskSummary> findAll(Pageable pageable);
}
