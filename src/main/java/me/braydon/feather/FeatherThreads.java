package me.braydon.feather;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Feather thread pool. This is used by default
 * when executing tasks in asynchronous pipelines.
 *
 * @author Braydon
 */
public final class FeatherThreads {
    private static final AtomicInteger ID = new AtomicInteger(0); // The thread id
    public static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(FeatherSettings.getThreadCount(), new ThreadFactoryBuilder()
                                                                                           .setNameFormat("Feather Thread #" + ID.incrementAndGet())
                                                                                           .build()); // The thread pool to execute on
}