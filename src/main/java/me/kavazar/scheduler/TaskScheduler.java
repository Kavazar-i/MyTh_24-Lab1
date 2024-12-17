package me.kavazar.scheduler;


import me.kavazar.scheduler.store.PriorityBlockingQueueTaskStore;
import me.kavazar.scheduler.store.TaskStore;
import me.kavazar.scheduler.tasks.OneTimeTask;
import me.kavazar.scheduler.tasks.RecurringTask;
import me.kavazar.scheduler.tasks.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.*;

// Task Scheduler
public class TaskScheduler {
    private static final Logger logger = LogManager.getLogger(TaskScheduler.class);
    private final ExecutorService executor;
    private final TaskStore<ScheduledTask> taskStore;

    public TaskScheduler(int numThreads) {
        executor = Executors.newFixedThreadPool(numThreads);
        taskStore = new PriorityBlockingQueueTaskStore();
    }

    public void scheduleTask(ScheduledTask task) {
        taskStore.add(task);
    }

    public void start() {
        for (int i = 0; i < ((ThreadPoolExecutor) executor).getCorePoolSize(); i++) {
            executor.submit(new TaskRunner(taskStore));
        }
        logger.info("Task Scheduler started.");
    }

    public void stop() {
        executor.shutdownNow();
        logger.info("Task Scheduler stopped.");
    }

    public static void main(String[] args) throws IOException {
        TaskScheduler scheduler = new TaskScheduler(4);
        scheduler.scheduleTask(new OneTimeTask(() -> {
            System.out.println("Executing one-time task.");
            return null;
        }, System.currentTimeMillis() + 5000));

        scheduler.scheduleTask(new RecurringTask(() -> {
            System.out.println("Executing recurring task.");
            return null;
        }, System.currentTimeMillis() + 3000, 3000));

        scheduler.start();
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stop));
    }
}
