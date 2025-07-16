package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

// TODO: Ordentlich machen
class AsyncExecutionExceptionTest {

    private static final Logger LOGGER
            = getLogger(AsyncExecutionExceptionTest.class);

    @Test
    void dummy() {
        try {
            throw new AsyncExecutionException("Test exception");
        } catch (AsyncExecutionException e) {
            System.out.println("Thread Name (main): " + e.getThreadName());
        }

        // Test in einem ExecutorService-Thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                throw new AsyncExecutionException("Test exception in executor");
            } catch (AsyncExecutionException e) {
                System.out.println("Thread Name (executor): " + e.getThreadName());
            }
        });
        executorService.shutdown();
    }

}
