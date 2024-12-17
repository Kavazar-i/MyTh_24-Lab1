package me.kavazar.scheduler.state.impl;

import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.tasks.ScheduledTask;

// State for Pending Task
public class PendingState implements TaskState {
    @Override
    public void execute(ScheduledTask task) {
        System.out.println("Task is pending execution.");
    }
}
