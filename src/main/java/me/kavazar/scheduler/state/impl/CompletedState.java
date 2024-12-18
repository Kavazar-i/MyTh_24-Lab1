package me.kavazar.scheduler.state.impl;

import me.kavazar.scheduler.state.TaskState;
import me.kavazar.scheduler.task.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompletedState implements TaskState {

    private static final Logger logger = LogManager.getLogger(TaskState.class);

    @Override
    public void execute(ScheduledTask task) {
        logger.info("Task has been completed.");
    }
}
