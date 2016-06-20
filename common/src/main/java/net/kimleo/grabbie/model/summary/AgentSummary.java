package net.kimleo.grabbie.model.summary;

import net.kimleo.grabbie.model.Agent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class AgentSummary {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    Agent agent;

    Integer taskExecuted;

    Long upTime;
}
