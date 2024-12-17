package me.kavazar.scheduler.store;

import me.kavazar.scheduler.tasks.ScheduledTask;

public interface TaskStore<T extends ScheduledTask> {
    T poll();

    void add(T task);

    boolean isEmpty();
}
