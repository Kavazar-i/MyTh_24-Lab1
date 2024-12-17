package me.kavazar.scheduler.state.impl;

import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.tasks.ScheduledTask;

// State for Executing Task
public class ExecutingState implements TaskState {
    @Override
    public void execute(ScheduledTask task) {
        try {
            task.context.call();
        } catch (Exception e) {
            System.err.println("Error during task execution: " + e.getMessage());
        }
    }
}
