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
import java.util.concurrent.*;

public class TaskScheduler {
    private static final Logger logger = LogManager.getLogger(TaskScheduler.class);
    private final ExecutorService executor;
    private final TaskStore<ScheduledTask> taskStore;
    private final CountDownLatch tasksCompletedLatch;

    public TaskScheduler(int numThreads) {
        executor = Executors.newFixedThreadPool(numThreads);
        taskStore = new PriorityBlockingQueueTaskStore();
        tasksCompletedLatch = new CountDownLatch(numThreads);
    }

    public void scheduleTask(ScheduledTask task) {
        taskStore.add(task);
    }

    public void start() {
        for (int i = 0; i < ((ThreadPoolExecutor) executor).getCorePoolSize(); i++) {
            executor.submit(() -> {
                try {
                    while (true) {
                        ScheduledTask task = taskStore.poll();
                        if (task == null && taskStore.isEmpty()) {
                            logger.info("All tasks completed.");
                            break;  // Завершить поток
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
                } finally {
                    tasksCompletedLatch.countDown();  // Уменьшить счетчик при завершении потока
                }
            });
        }
        logger.info("Task Scheduler started.");
    }

    public void stop() {
        try {
            tasksCompletedLatch.await();  // Ждать завершения всех потоков
            executor.shutdown();
            logger.info("Task Scheduler stopped.");
        } catch (InterruptedException e) {
            logger.error("Shutdown interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws IOException {
        TaskScheduler scheduler = new TaskScheduler(4);

        List<ScheduledTask> tasks = TaskLoader.loadTasksFromFile("./src/main/resources/tasks.txt");
        tasks.forEach(scheduler::scheduleTask);

        scheduler.start();
        scheduler.stop();  // Ожидать завершения всех потоков перед остановкой
    }
}
