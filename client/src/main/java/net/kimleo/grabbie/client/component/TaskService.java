package net.kimleo.grabbie.client.component;

import net.kimleo.grabbie.client.agent.AgentInfo;
import net.kimleo.grabbie.model.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class TaskService {

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
            System.out.println(exec);
            String ExecUrl = navigator.execution(exec.getId());
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
