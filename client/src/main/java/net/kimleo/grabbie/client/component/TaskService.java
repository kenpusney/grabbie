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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    public static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    Navigator navigator;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AgentInfo agentInfo;

    @Scheduled(fixedDelay = 1000)
    public void taskExecutionLoop() {
        Execution[] execs = getNotExecutedTasks();
        for (Execution exec : execs) {
            LOGGER.info("Execution found: {}", exec);
            String ExecUrl = navigator.execution(exec.getId());
            ArrayList<String> command = new ArrayList<>();
            command.add(exec.getTask().getCommand());
            command.addAll(Arrays.asList(exec.getTask().getArgs()));
            try {
                Process process = new ProcessBuilder(command).start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String result = reader.lines().collect(Collectors.joining("\n"));
                exec.setResult(result);
                LOGGER.info("Executed result: {}", result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            exec.setExecuted(true);
            RequestEntity<Execution> requestEntity = RequestEntity
                    .put(URI.create(ExecUrl))
                    .body(exec);
            restTemplate.exchange(ExecUrl, HttpMethod.PUT, requestEntity, Execution.class);
        }
    }

    private Execution[] getNotExecutedTasks() {
        Execution[] executions = restTemplate.getForObject(
                navigator.agentExecutions(agentInfo.id()) + "?executed={executed}",
                Execution[].class, false);

        return executions;
    }
}
