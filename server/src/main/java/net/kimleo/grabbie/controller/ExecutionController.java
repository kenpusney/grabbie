package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.model.Agent;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.repository.AgentRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/execution")
public class ExecutionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
    @Autowired
    @Qualifier("internalNavigator")
    private Navigator navigator;

    @Autowired
    private AgentController agentController;

    @Autowired
    private TaskController taskController;

    @Autowired
    private ExecRepo execRepo;

    @Autowired
    private AgentRepo agentRepo;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Execution> createExecution(@RequestBody Execution execution) {
        execution = execRepo.save(execution);
        LOGGER.info("Execution {} created for task {} and agent {}",
                execution, execution.getTask(), execution.getAgent());
        return ResponseEntity.created(URI.create(navigator.execution(execution.getId())))
                .body(execution);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Execution> getExecution(@PathVariable("id") Long id) {
        return ResponseEntity.ok(execRepo.findOne(id));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Execution>> getExecutionByClientId(
            @RequestParam(value = "agentId", required = false) Long clientId,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        if (clientId != null && taskId != null) {
            return ResponseEntity.badRequest().body(null);
        }
        if (clientId != null)
            return agentController.getClientExecution(clientId, executed);
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
        Agent agent = original.getAgent();
        agent.setLastActiveTime(new Date());
        if (execution.getDate() != null) {
            original.setDate(execution.getDate());
        } else {
            original.setDate(new Date());
        }
        original.setResult(execution.getResult());
        LOGGER.info("Agent {} finished execution {} with result:\n {}",
                agent, execution, execution.getResult());
        agentRepo.save(agent);
        return ResponseEntity.ok(execRepo.save(original));
    }

}
