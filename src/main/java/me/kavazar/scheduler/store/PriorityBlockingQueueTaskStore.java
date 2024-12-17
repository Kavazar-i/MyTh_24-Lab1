package me.kavazar.scheduler.store;

import me.kavazar.scheduler.tasks.ScheduledTask;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

// Priority Blocking Queue Task Store Implementation
public class PriorityBlockingQueueTaskStore implements TaskStore<ScheduledTask> {
    private final PriorityBlockingQueue<ScheduledTask> taskQueue;

    public PriorityBlockingQueueTaskStore() {
        taskQueue = new PriorityBlockingQueue<>(11, Comparator.comparingLong(ScheduledTask::getExecutionTime));
    }

    @Override
    public void add(ScheduledTask task) {
        taskQueue.offer(task);
    }

    @Override
    public ScheduledTask poll() {
        return taskQueue.poll();
    }

    @Override
    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }
}
