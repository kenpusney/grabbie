package net.kimleo.grabbie.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Agent {
    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    String url;

    Date lastActiveTime;

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

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
