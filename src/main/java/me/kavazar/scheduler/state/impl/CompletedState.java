package me.kavazar.scheduler.state.impl;

import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.tasks.ScheduledTask;

public class CompletedState implements TaskState {
    @Override
    public void execute(ScheduledTask task) {
        System.out.println("Task has been completed.");
    }
}
