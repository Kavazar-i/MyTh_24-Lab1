package me.kavazar.scheduler;

import me.kavazar.scheduler.store.TaskStore;
import me.kavazar.scheduler.tasks.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

class TaskRunner implements Runnable {
    private static final Logger logger = LogManager.getLogger(TaskRunner.class);
    private final TaskStore<ScheduledTask> taskStore;
    private volatile boolean running = true;

    public TaskRunner(TaskStore<ScheduledTask> taskStore) {
        this.taskStore = taskStore;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                ScheduledTask task = taskStore.poll();
                if (task == null) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    continue;
                }
                long delay = task.getExecutionTime() - System.currentTimeMillis();
                if (delay > 0) {
                    taskStore.add(task);
                    TimeUnit.MILLISECONDS.sleep(delay);
                } else {
                    task.context.call();
                    if (task.isRecurring()) {
                        taskStore.add(task.nextScheduledTask().get());
                    }
                }
            } catch (Exception e) {
                logger.error("Task execution error: ", e);
            }
        }
    }

    public void stop() {
        running = false;
    }
}
