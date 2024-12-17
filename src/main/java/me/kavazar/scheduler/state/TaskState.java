package me.kavazar.scheduler.state;


import me.kavazar.scheduler.tasks.ScheduledTask;

// Task State Interface
public interface TaskState {
    void execute(ScheduledTask task);
}

