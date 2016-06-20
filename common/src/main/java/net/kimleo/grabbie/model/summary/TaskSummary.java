package net.kimleo.grabbie.model.summary;

import net.kimleo.grabbie.model.Task;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class TaskSummary {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    Task task;

    int totalExecutedCount;

    int averageExecutionTime;

    int failedCount;
}
