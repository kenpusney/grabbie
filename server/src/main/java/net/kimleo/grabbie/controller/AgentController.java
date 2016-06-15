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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    @Qualifier("internalNavigator")
    Navigator navigator;

    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private ExecRepo execRepo;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Agent> createClient(@RequestBody Agent agent) {
        Agent existedAgent = agentRepo.findByUrl(agent.getUrl());
        if (existedAgent != null) {
            if (existedAgent.getLastActiveTime() != null
                    && System.currentTimeMillis() - existedAgent.getLastActiveTime().getTime() < 5000) {
                LOGGER.warn("Conflict connection found for agent {}", existedAgent);
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .location(URI.create(navigator.agent(existedAgent.getId())))
                        .body(existedAgent);
            } else {
                LOGGER.warn("Previous connection found for agent {}", existedAgent);
                agent = existedAgent;
            }
        }
        agent.setLastActiveTime(new Date());
        agent = agentRepo.save(agent);
        return ResponseEntity.created(URI.create(navigator.agent(agent.getId())))
                .body(agent);
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Agent> getClient(@PathVariable("id") Long id) {
        return ResponseEntity.ok(agentRepo.findOne(id));
    }

    @RequestMapping("/{id}/execution")
    public ResponseEntity<List<Execution>> getClientExecution(
            @PathVariable("id") Long id,
            @RequestParam(value = "executed", required = false) Boolean executed) {
        if (executed != null) {
            return ResponseEntity.ok(execRepo.findByAgentIdAndExecuted(id, executed));
        }
        return ResponseEntity.ok(execRepo.findByAgentId(id));
    }
}
