package me.kavazar.scheduler;

import me.kavazar.scheduler.task.OneTimeTask;
import me.kavazar.scheduler.task.RecurringTask;
import me.kavazar.scheduler.task.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskLoader {

    private static final Logger logger = LogManager.getLogger(TaskLoader.class);

    public static List<ScheduledTask> loadTasksFromFile(String fileName) throws IOException {
        List<ScheduledTask> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                long executionTime = Long.parseLong(parts[1]);

                if (type.startsWith("one-time")) {
                    tasks.add(new OneTimeTask(() -> {

                        logger.info("Executing " + type + " task.");
                        return null;
                    }, System.currentTimeMillis() + executionTime));
                } else if (type.startsWith("recurring")) {
                    long interval = Long.parseLong(parts[2]);
                    tasks.add(new RecurringTask(() -> {
                        logger.info("Executing " + type + " task.");
                        return null;
                    }, System.currentTimeMillis() + executionTime, interval));
                }
            }
        }

        return tasks;
    }
}
