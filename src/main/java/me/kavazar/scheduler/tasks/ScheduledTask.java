package me.kavazar.scheduler.tasks;


import me.kavazar.scheduler.state.impl.CompletedState;
import me.kavazar.scheduler.state.impl.ExecutingState;
import me.kavazar.scheduler.state.impl.PendingState;
import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.context.ExecutionContext;

import java.util.Optional;

public abstract class ScheduledTask {
    public final ExecutionContext context;
    protected final long executionTime;
    private TaskState state;

    public ScheduledTask(ExecutionContext context, long executionTime) {
        this.context = context;
        this.executionTime = executionTime;
        this.state = new PendingState();
    }

    public abstract boolean isRecurring();

    public long getExecutionTime() {
        return executionTime;
    }

    public abstract Optional<ScheduledTask> nextScheduledTask();

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void execute() {
        setState(new ExecutingState());
        state.execute(this);

        if (!isRecurring()) {
            setState(new CompletedState());
        }
    }
}