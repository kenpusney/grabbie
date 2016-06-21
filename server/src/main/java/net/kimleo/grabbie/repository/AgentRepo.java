package net.kimleo.grabbie.repository;

import net.kimleo.grabbie.model.Agent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AgentRepo extends CrudRepository<Agent, Long> {
    Agent findByUrl(String url);
}
