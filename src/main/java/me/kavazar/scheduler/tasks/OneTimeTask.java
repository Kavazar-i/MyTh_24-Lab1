package me.kavazar.scheduler.tasks;

import me.kavazar.scheduler.context.ExecutionContext;

import java.util.Optional;

// One-Time Task Implementation
public class OneTimeTask extends ScheduledTask {
    public OneTimeTask(ExecutionContext context, long executionTime) {
        super(context, executionTime);
    }

    @Override
    public boolean isRecurring() {
        return false;
    }

    @Override
    public Optional<ScheduledTask> nextScheduledTask() {
        return Optional.empty();
    }
}