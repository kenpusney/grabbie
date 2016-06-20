package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.model.Agent;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.summary.TaskSummary;
import net.kimleo.grabbie.repository.AgentRepo;
import net.kimleo.grabbie.repository.ExecRepo;
import net.kimleo.grabbie.repository.TaskSummaryRepo;
import net.kimleo.grabbie.service.ExecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

import static net.kimleo.grabbie.model.ExecutionStatus.STARTED;

@RestController
@RequestMapping("/execution")
public class ExecutionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
    @Autowired
    @Qualifier("internalNavigator")
    private Navigator navigator;

    @Autowired
    private ExecRepo execRepo;

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private TaskSummaryRepo taskSummaries;

    @Autowired
    private ExecService execService;

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
            @RequestParam(value = "agentId", required = false) Long agentId,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        if (isInvalidFilterRequest(agentId, taskId)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (agentId != null)
            return ResponseEntity.ok(execService.getAgentExecution(agentId, executed));
        else return ResponseEntity.ok(execService.getTaskExecution(taskId, executed));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Execution> updateExecution(
            @PathVariable("id") Long id,
            @RequestBody Execution execution) {
        Execution updatedExecution = updateOriginalExecution(id, execution);
        Agent agent = updateAgentStatus(updatedExecution);
        LOGGER.info("Agent {} finished execution {} with result:\n {}",
                agent, execution, execution.getResult());
        agentRepo.save(agent);
        return ResponseEntity.ok(execRepo.save(updatedExecution));
    }

    private Agent updateAgentStatus(Execution updatedExecution) {
        Agent agent = updatedExecution.getAgent();
        agent.setLastActiveTime(new Date());
        return agent;
    }

    private Execution updateOriginalExecution(Long id, Execution execution) {
        Execution original = execRepo.findOne(id);
        original.setExecuted(execution.getExecuted());
        if (execution.getExecuted()) {
            original.setDate(new Date());
            long time = original.getDate().getTime();
            original.setDuration(time - original.getTimeStarted().getTime());
            updateTaskSummary(original);
        } else if (execution.getStatus() == STARTED) {
            original.setTimeStarted(new Date());
        }
        original.setStatus(execution.getStatus());
        original.setResult(execution.getResult());
        return original;
    }

    private void updateTaskSummary(Execution original) {
        TaskSummary taskSummary = taskSummaries.findByTask(original.getTask());
        taskSummary.setTotalExecutedCount(taskSummary.getTotalExecutedCount() + 1);
        taskSummary.setAverageExecutionTime(
                reCalculateAverageTime(
                        taskSummary.getAverageExecutionTime(),
                        original.getDuration(),
                        taskSummary.getTotalExecutedCount()));
    }

    private long reCalculateAverageTime(Long average, Long duration, int count) {
        return average + (duration - average) / count;
    }

    private boolean isInvalidFilterRequest(Long agentId, Long taskId) {
        return (agentId != null && taskId != null) || (agentId == null && taskId == null);
    }
}
