package net.kimleo.grabbie.client.agent;

import net.kimleo.grabbie.model.Agent;
import org.springframework.http.ResponseEntity;

public class AgentInfo {
    private Agent agent;


    public AgentInfo(Agent agent) {
        this.agent = agent;
    }

    public Long id() {
        return agent.getId();
    }
}
