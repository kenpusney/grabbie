package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.model.Agent;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExecutionControllerIntegrationTest extends AbstractWebControllerIntegrationTestBase {
    @Test
    public void testExecution() throws Exception {
        Agent agentToExecuteTask = agentRepo.save(new Agent("agentToExecuteTask"));
        Task taskToExecute = taskRepo.save(new Task());

        Execution execution = execRepo.save(new Execution(agentToExecuteTask, taskToExecute));
        execRepo.save(new Execution(agentToExecuteTask, taskToExecute));
        execRepo.save(new Execution(agentToExecuteTask, taskToExecute));


        mockMvc.perform(get(String.format("%s?agentId=%d", navigator.execution(), agentToExecuteTask.getId()))
                .with(auth))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].agent.id",
                        is(agentToExecuteTask.getId().intValue())));

        execution.setExecuted(true);

        mockMvc.perform(put(navigator.execution(execution.getId()))
                .content(json(execution))
                .contentType(contentType)
                .with(auth))
                .andExpect(jsonPath("$.executed", is(true)))
                .andExpect(jsonPath("$.date", notNullValue()));

        mockMvc.perform(get(String.format("%s?agentId=%d&executed=false", navigator.execution(), agentToExecuteTask.getId()))
                .with(auth))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].executed", is(false)));

        mockMvc.perform(get(String.format("%s?taskId=%d&executed=true", navigator.execution(), taskToExecute.getId()))
                .with(auth))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].agent.url", is(agentToExecuteTask.getUrl())));
    }
}