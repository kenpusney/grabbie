package net.kimleo.grabbie.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Task {

    @Id
    @GeneratedValue
    Long id;

    String[] command;

    public Task() {
    }

    public Task(String... command) {
        this.command = command;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                '}';
    }
}
