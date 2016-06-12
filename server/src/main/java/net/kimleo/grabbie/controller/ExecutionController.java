package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.repository.ExecRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/execution")
public class ExecutionController {

    @Autowired
    private ExecRepo execRepo;

    @Autowired
    ClientController clientController;

    @Autowired
    TaskController taskController;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Execution> createExecution(@RequestBody Execution execution) {
        execution = execRepo.save(execution);
        return ResponseEntity.created(URI.create("/execution/" + execution.getId()))
                .body(execution);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Execution> getExecution(@PathVariable("id") Long id) {
        return ResponseEntity.ok(execRepo.findOne(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Execution>> getExecutionByClientId(
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        if (clientId != null && taskId != null) {
            return ResponseEntity.badRequest().body(null);
        }
        if (clientId != null)
            return clientController.getClientExecution(clientId, executed);
        else if (taskId != null)
            return taskController.getTaskExecution(taskId, executed);
        else
            return ResponseEntity.badRequest().body(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Execution> updateExecution(
            @PathVariable("id") Long id,
            @RequestBody Execution execution) {
        Execution original = execRepo.findOne(id);
        original.setExecuted(execution.getExecuted());
        if (execution.getDate() != null) {
            original.setDate(execution.getDate());
        } else {
            original.setDate(new Date());
        }
        original.setResult(execution.getResult());
        return ResponseEntity.ok(execRepo.save(original));
    }

}
