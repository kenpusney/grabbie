package net.kimleo.grabbie.model.summary;

import net.kimleo.grabbie.model.Task;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TaskSummary {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    Task task;

    Integer totalExecutedCount = 0;

    Long averageExecutionTime = 0L;

    Integer failedCount = 0;

    public TaskSummary() {
    }

    public TaskSummary(Task task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTotalExecutedCount() {
        return totalExecutedCount;
    }

    public void setTotalExecutedCount(int totalExecutedCount) {
        this.totalExecutedCount = totalExecutedCount;
    }

    public Long getAverageExecutionTime() {
        return averageExecutionTime;
    }

    public void setAverageExecutionTime(Long averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }
}
