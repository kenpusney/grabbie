package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.Task;
import net.kimleo.grabbie.model.summary.TaskSummary;
import net.kimleo.grabbie.repository.ExecRepo;
import net.kimleo.grabbie.repository.TaskRepo;
import net.kimleo.grabbie.repository.TaskSummaryRepo;
import net.kimleo.grabbie.service.ExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskSummaryRepo taskSummaries;

    @Autowired
    private ExecService execService;

    @Autowired
    @Qualifier("internalNavigator")
    Navigator navigator;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task = taskRepo.save(task);
        taskSummaries.save(new TaskSummary(task));
        return ResponseEntity.created(URI.create(navigator.task(task.getId())))
                .body(task);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskRepo.findOne(id));
    }

    @RequestMapping("/{id}/execution")
    public ResponseEntity<List<Execution>> getTaskExecution(
            @PathVariable Long id,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        return ResponseEntity.ok(execService.getTaskExecution(id, executed));
    }
}
