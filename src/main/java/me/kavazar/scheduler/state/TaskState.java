package me.kavazar.scheduler.state;


import me.kavazar.scheduler.tasks.ScheduledTask;

public interface TaskState {
    void execute(ScheduledTask task);
}

