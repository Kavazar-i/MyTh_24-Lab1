package me.kavazar.scheduler.state.impl;

import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.tasks.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExecutingState implements TaskState {

    private static final Logger logger = LogManager.getLogger(TaskState.class);

    @Override
    public void execute(ScheduledTask task) {
        try {
            task.context.call();
        } catch (Exception e) {
            logger.info("Error during task execution: " + e.getMessage());
        }
    }
}
