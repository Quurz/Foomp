package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.MemoisingPred.memoisingPred;
import static org.slf4j.LoggerFactory.getLogger;

class MemoisingPredTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(MemoisingPredTest.class);

    @Test
    @SuppressWarnings({"DataFlowIssue", "unused"})
    void testMemoisingPred() {
        LOGGER.info("Test MemoisingPred.memoisingPred");

        assertThatThrownBy(() -> memoisingPred(null))
            .isInstanceOf(NullPointerException.class);

        final Pred<Integer> isEven = x -> x % 2 == 0;
        final var memoisingPred = memoisingPred(isEven);

        assertThat(memoisingPred.test(2)).isTrue();
        assertThat(memoisingPred.test(3)).isFalse();
    }

    @Test
    void testTest() {
        LOGGER.info("Test memoisingPred.test");

        final AtomicInteger counter = new AtomicInteger(0);
        final Pred<Integer> countingPred = x -> {
            counter.incrementAndGet();
            return x % 2 == 0;
        };

        final var memoisingPred = memoisingPred(countingPred);

        // First call
        assertThat(memoisingPred.test(2)).isTrue();
        assertThat(counter.get()).isEqualTo(1);

        // Second call with same input -> should be memoized
        assertThat(memoisingPred.test(2)).isTrue();
        assertThat(counter.get()).isEqualTo(1);

        // Call with different input
        assertThat(memoisingPred.test(3)).isFalse();
        assertThat(counter.get()).isEqualTo(2);

        // Clear cache and call again
        memoisingPred.clear();
        assertThat(memoisingPred.test(2)).isTrue();
        assertThat(counter.get()).isEqualTo(3);
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testNullInput() {
        LOGGER.info("Test memoisingPred.test with null input");

        final var memoisingPred = memoisingPred(x -> true);
        assertThatThrownBy(() -> memoisingPred.test(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testConcurrency() throws InterruptedException {
        LOGGER.info("Test memoisingPred concurrency");

        final AtomicInteger counter = new AtomicInteger(0);
        final Pred<Integer> countingPred = x -> {
            // Simuliere etwas Arbeit
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            counter.incrementAndGet();
            return x % 2 == 0;
        };

        final var memoisingPred = memoisingPred(countingPred);
        final int threadCount = 10;
        final int input = 42;
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);
        final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    memoisingPred.test(input);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // Trotz mehrerer Threads sollte das Prädikat nur einmal ausgeführt worden sein
        assertThat(counter.get()).isEqualTo(1);
    }
}
