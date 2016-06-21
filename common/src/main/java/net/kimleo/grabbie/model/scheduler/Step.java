package net.kimleo.grabbie.model.scheduler;

import net.kimleo.grabbie.model.Task;

import javax.persistence.*;

@Entity
public class Step {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Task task;

    @ManyToOne
    Step onSuccess;

    @ManyToOne
    Step onFail;
}
