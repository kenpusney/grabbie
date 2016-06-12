package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepo extends CrudRepository<Task, Long> {
    List<Task> findByCommand(String command);
}
