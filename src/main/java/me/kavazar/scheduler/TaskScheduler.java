package me.kavazar.scheduler;


import me.kavazar.scheduler.store.impl.PriorityBlockingQueueTaskStore;
import me.kavazar.scheduler.store.TaskStore;
import me.kavazar.scheduler.tasks.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
            executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        ScheduledTask task = taskStore.poll();
                        if (task == null) {
                            TimeUnit.MILLISECONDS.sleep(100);
                            continue;
                        }

                        long currentTime = System.currentTimeMillis();
                        long delay = task.getExecutionTime() - currentTime;

                        if (delay > 0) {
                            taskStore.add(task);
                            TimeUnit.MILLISECONDS.sleep(Math.min(delay, 100));
                        } else {
                            task.execute();

                            if (task.isRecurring() && task.nextScheduledTask().isPresent()) {
                                ScheduledTask nextTask = task.nextScheduledTask().get();
                                taskStore.add(nextTask);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.error("Task execution interrupted", e);
                    } catch (Exception e) {
                        logger.error("Task execution error", e);
                    }
                }
            });
        }
        logger.info("Task Scheduler started.");
    }

    public void stop() {
        executor.shutdownNow();
        logger.info("Task Scheduler stopped.");
    }

    public static void main(String[] args) throws IOException {
        logger.info("Log system initialized");
        TaskScheduler scheduler = new TaskScheduler(4);

        List<ScheduledTask> tasks = TaskLoader.loadTasksFromFile("./src/main/resources/tasks.txt");
        tasks.forEach(scheduler::scheduleTask);

        scheduler.start();
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::stop));
    }
}
