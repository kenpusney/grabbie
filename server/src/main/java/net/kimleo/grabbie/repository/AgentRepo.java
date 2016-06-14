package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Agent;
import org.springframework.data.repository.CrudRepository;

public interface AgentRepo extends CrudRepository<Agent, Long> {
    public Agent findByUrl(String url);
}
