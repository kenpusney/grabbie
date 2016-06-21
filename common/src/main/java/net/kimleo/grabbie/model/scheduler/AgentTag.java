package net.kimleo.grabbie.model.scheduler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AgentTag {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    String name;

    public AgentTag() {
    }

    public AgentTag(String name) {
        this.name = name;
    }
}
