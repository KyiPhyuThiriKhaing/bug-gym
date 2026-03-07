package dev.philixtheexplorer.buggym.application;

import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Centralized runner for app-level background tasks.
 */
public class BackgroundTaskRunner {

    private final ExecutorService executor;

    public BackgroundTaskRunner(String threadPrefix, int poolSize) {
        AtomicInteger sequence = new AtomicInteger(1);
        this.executor = Executors.newFixedThreadPool(poolSize, runnable -> {
            Thread thread = new Thread(runnable, threadPrefix + "-" + sequence.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
    }

    public void run(Task<?> task) {
        executor.execute(task);
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }
}
