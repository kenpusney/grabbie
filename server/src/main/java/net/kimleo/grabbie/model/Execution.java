package net.kimleo.grabbie.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Execution {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Client client;

    @ManyToOne
    Task task;

    @Lob
    String result;

    Boolean executed = false;

    Date date;

    public Execution() {
    }

    public Execution(Client client, Task task) {
        this.client = client;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
}
