package net.kimleo.grabbie.client.component;

import net.kimleo.grabbie.client.agent.AgentInfo;
import net.kimleo.grabbie.component.Navigator;
import net.kimleo.grabbie.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import static net.kimleo.grabbie.model.ExecutionStatus.EXECUTED_FAILURE;
import static net.kimleo.grabbie.model.ExecutionStatus.EXECUTED_SUCCESS;
import static net.kimleo.grabbie.model.ExecutionStatus.STARTED;

@Service
public class TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    Navigator navigator;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AgentInfo agentInfo;

    @Autowired
    ProcService procService;

    @Scheduled(fixedDelay = 1000)
    public void taskExecutionLoop() {
        Execution[] execs = getNonExecutedExecutions();
        for (Execution exec : execs) {
            LOGGER.info("Execution found: {}", exec);
            String[] command = exec.getTask().getCommand();
            String execUrl = navigator.execution(exec.getId());
            exec.setStatus(STARTED);
            sendExecutionUpdate(exec);
            try {
                String result = procService.executeProcess(command);
                exec.setResult(result);
                exec.setStatus(EXECUTED_SUCCESS);
                LOGGER.info("Executed result: {}", result);
            } catch (IOException e) {
                exec.setResult(e.toString());
                exec.setStatus(EXECUTED_FAILURE);
                LOGGER.error("Executed failed with exception", e);
            }
            exec.setExecuted(true);
            sendExecutionUpdate(exec);
        }
    }

    private void sendExecutionUpdate(Execution exec) {
        String execUrl = navigator.execution(exec.getId());
        RequestEntity<Execution> requestEntity = RequestEntity
                .put(URI.create(execUrl))
                .body(exec);
        restTemplate.exchange(execUrl, HttpMethod.PUT, requestEntity, Execution.class);
    }

    private Execution[] getNonExecutedExecutions() {

        return restTemplate.getForObject(
                navigator.agentExecutions(agentInfo.id()) + "?executed={executed}",
                Execution[].class, false);
    }
}
