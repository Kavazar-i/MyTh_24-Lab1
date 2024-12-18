package me.kavazar.scheduler.state;


import me.kavazar.scheduler.task.ScheduledTask;

public interface TaskState {
    void execute(ScheduledTask task);
}

