package me.kavazar.scheduler.tasks;

import me.kavazar.scheduler.context.ExecutionContext;

import java.util.Optional;

// Recurring Task Implementation
public class RecurringTask extends ScheduledTask {
    private final long interval;

    public RecurringTask(ExecutionContext context, long executionTime, long interval) {
        super(context, executionTime);
        this.interval = interval;
    }

    @Override
    public boolean isRecurring() {
        return true;
    }

    @Override
    public Optional<ScheduledTask> nextScheduledTask() {
        return Optional.of(new RecurringTask(context, executionTime + interval, interval));
    }
}
