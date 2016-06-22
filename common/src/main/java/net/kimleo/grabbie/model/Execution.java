package net.kimleo.grabbie.model;

import javax.persistence.*;
import java.util.Date;

import static net.kimleo.grabbie.model.ExecutionStatus.NOT_EXECUTED;

@Entity
public class Execution {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Agent agent;

    @ManyToOne
    Task task;

    @Lob
    String result;

    Boolean executed = false;

    ExecutionStatus status = NOT_EXECUTED;

    Date timeStarted;

    Long duration;

    Date date;

    public Execution() {
    }

    public Execution(Agent agent, Task task) {
        this.agent = agent;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Boolean getExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Execution{" +
                "id=" + id +
                ", agent=" + agent.getId() +
                ", task=" + task.getId() +
                ", executed=" + executed +
                '}';
    }
}
