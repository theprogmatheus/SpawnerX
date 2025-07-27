package com.github.theprogmatheus.mc.plugin.spawnerx.util;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutorTimeLogger {

    public static void executeAndLogTime(Logger logger, String taskName, Runnable task) {
        long start = System.nanoTime();
        try {
            task.run();
        } finally {
            long end = System.nanoTime();
            long durationMs = (end - start) / 1_000_000;
            logger.log(Level.INFO, "[{0}] executed in {1} ms", new Object[]{taskName, durationMs});
        }
    }

    public static <T> T executeAndLogTime(Logger logger, String taskName, Callable<T> task) throws Exception {
        long start = System.nanoTime();
        try {
            return task.call();
        } finally {
            long end = System.nanoTime();
            long durationMs = (end - start) / 1_000_000;
            logger.log(Level.INFO, "[{0}] executed in {1} ms", new Object[]{taskName, durationMs});
        }
    }
}
