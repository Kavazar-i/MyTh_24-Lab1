package me.kavazar.scheduler;

import me.kavazar.scheduler.store.TaskStore;
import me.kavazar.scheduler.task.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class TaskRunner implements Runnable {
    private static final Logger logger = LogManager.getLogger(TaskRunner.class);
    private final TaskStore<ScheduledTask> taskStore;
    private final CountDownLatch tasksCompletedLatch;
    private volatile boolean running = true;

    public TaskRunner(TaskStore<ScheduledTask> taskStore, CountDownLatch tasksCompletedLatch) {
        this.taskStore = taskStore;
        this.tasksCompletedLatch = tasksCompletedLatch;
    }

    @Override
    public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                ScheduledTask task = taskStore.poll();

                if (task == null && taskStore.isEmpty()) {
                    logger.info("All tasks completed.");
                    tasksCompletedLatch.countDown();
                    break;
                }

                if (task == null) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    continue;
                }

                long delay = task.getExecutionTime() - System.currentTimeMillis();
                if (delay > 0) {
                    taskStore.add(task);
                    TimeUnit.MILLISECONDS.sleep(Math.min(delay, 100));
                } else {
                    task.execute();
                    task.nextScheduledTask().ifPresent(taskStore::add);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Task execution interrupted", e);
        } catch (Exception e) {
            logger.error("Task execution error: ", e);
        }
    }

    public void stop() {
        running = false;
    }
}
