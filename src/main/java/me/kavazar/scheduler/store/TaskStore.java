package me.kavazar.scheduler.store;

import me.kavazar.scheduler.task.ScheduledTask;

public interface TaskStore<T extends ScheduledTask> {
    T poll();

    void add(T task);

    boolean isEmpty();
}
