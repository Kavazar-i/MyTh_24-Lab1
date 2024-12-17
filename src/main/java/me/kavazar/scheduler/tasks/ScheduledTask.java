package me.kavazar.scheduler.tasks;

import me.kavazar.scheduler.context.ExecutionContext;

import java.util.Optional;

// Abstract Scheduled Task
abstract public class ScheduledTask {
    public final ExecutionContext context;
    protected final long executionTime;

    public ScheduledTask(ExecutionContext context, long executionTime) {
        this.context = context;
        this.executionTime = executionTime;
    }

    public abstract boolean isRecurring();

    public long getExecutionTime() {
        return executionTime;
    }

    public abstract Optional<ScheduledTask> nextScheduledTask();
}
