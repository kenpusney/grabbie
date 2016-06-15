package net.kimleo.grabbie.controller;

import net.kimleo.grabbie.model.Agent;
import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
public class AgentControllerIntegrationTest extends AbstractWebControllerIntegrationTestBase {

    public static final String LOCALHOST = "localhost";
    public static final String EXISTED_AGENT = "existedAgent";
    public static final String AGENT_WITH_TASKS = "agentWithTasks";

    @Test
    public void createAgent() throws Exception {
        mockMvc.perform(post(navigator.agent())
                .contentType(contentType)
                .content(json(new Agent(LOCALHOST))).with(auth))
                .andExpect(status().isCreated());
    }

    @Test
    public void createWithExistedAgentAndTimeOut() throws Exception {
        Agent agent = new Agent(EXISTED_AGENT);
        agent.setLastActiveTime(new Date(new Date().getTime() - 10000));
        Agent existedAgent = agentRepo.save(agent);

        mockMvc.perform(post(navigator.agent())
                .contentType(contentType)
                .content(json(new Agent(EXISTED_AGENT))).with(auth))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",
                        is(existedAgent.getId().intValue())));
    }


    @Test
    public void createWithExistedAgent() throws Exception {
        Agent agent = new Agent(EXISTED_AGENT);
        agent.setLastActiveTime(new Date());
        Agent existedAgent = agentRepo.save(agent);

        mockMvc.perform(post(navigator.agent())
                .contentType(contentType)
                .content(json(new Agent(EXISTED_AGENT))).with(auth))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.id",
                        is(existedAgent.getId().intValue())));
    }


    @Test
    public void retrieveExecutionsOfTasks() throws Exception {
        Agent agentWithTasks = agentRepo.save(new Agent(AGENT_WITH_TASKS));

        execRepo.save(new Execution(agentWithTasks, taskRepo.save(new Task())));
        execRepo.save(new Execution(agentWithTasks, taskRepo.save(new Task())));
        execRepo.save(new Execution(agentWithTasks, taskRepo.save(new Task())));

        mockMvc.perform(get(navigator.agentExecutions(agentWithTasks.getId()))
                .with(auth))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].agent.id",
                        is(agentWithTasks.getId().intValue())));
    }

}