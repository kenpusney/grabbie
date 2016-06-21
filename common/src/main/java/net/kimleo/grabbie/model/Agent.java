package net.kimleo.grabbie.model;

import net.kimleo.grabbie.model.scheduler.AgentTag;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Agent {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    String url;

    Date lastActiveTime;

    @ManyToMany
    List<AgentTag> agentTags;

    public Agent() {
    }

    public Agent(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public List<AgentTag> getAgentTags() {
        return agentTags;
    }

    public void setAgentTags(List<AgentTag> agentTags) {
        this.agentTags = agentTags;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
