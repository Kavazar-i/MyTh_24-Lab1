package me.kavazar.scheduler;

import me.kavazar.scheduler.tasks.OneTimeTask;
import me.kavazar.scheduler.tasks.RecurringTask;
import me.kavazar.scheduler.tasks.ScheduledTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskLoader {

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
                        System.out.println("Executing " + type + " task.");
                        return null;
                    }, System.currentTimeMillis() + executionTime));
                } else if (type.startsWith("recurring")) {
                    long interval = Long.parseLong(parts[2]);
                    tasks.add(new RecurringTask(() -> {
                        System.out.println("Executing " + type + " task.");
                        return null;
                    }, System.currentTimeMillis() + executionTime, interval));
                }
            }
        }

        return tasks;
    }
}
