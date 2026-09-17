package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.MemoisingApplicable.memoisingApplicable;
import static org.slf4j.LoggerFactory.getLogger;

class MemoisingApplicableTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(MemoisingApplicableTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMemoisingApplicableWithNullApplicable() {
        LOGGER.info("Test memoisingApplicable with null applicable");

        assertThatThrownBy(() -> memoisingApplicable(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMemoisingApplicableWithNullInput() {
        LOGGER.info("Test memoisingApplicable with null input");

        final var memoising = memoisingApplicable(Applicable.identity());

        assertThatThrownBy(() -> memoising.apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testMemoisingApplicableCachesSuccessfulResults() {
        LOGGER.info("Test memoisingApplicable caches successful results");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            // First call - should invoke the function
            final var result1 = memoising.apply(5);
            assertThat(result1).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(1);

            // Second call with same input - should use cache
            final var result2 = memoising.apply(5);
            assertThat(result2).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(1); // Still 1, not invoked again

            // Third call with different input - should invoke the function
            final var result3 = memoising.apply(7);
            assertThat(result3).isEqualTo(14);
            assertThat(invocationCounter.get()).isEqualTo(2);

            // Fourth call with first input again - should use cache
            final var result4 = memoising.apply(5);
            assertThat(result4).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(2); // Still 2
        });
    }

    @Test
    void testMemoisingApplicableDoesNotCacheExceptions() {
        LOGGER.info("Test memoisingApplicable does not cache exceptions");

        final var invocationCounter = new AtomicInteger(0);
        final var testException = new IllegalArgumentException("Test exception");
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            if (x == 0) {
                throw testException;
            }
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);

        // First call with x=0 - should throw exception
        assertThatThrownBy(() -> memoising.apply(0))
            .isSameAs(testException);
        assertThat(invocationCounter.get()).isEqualTo(1);

        // Second call with x=0 - should re-execute and throw exception again (not cached)
        assertThatThrownBy(() -> memoising.apply(0))
            .isSameAs(testException);
        assertThat(invocationCounter.get()).isEqualTo(2); // Invoked again!

        // Call with different value should work normally and be cached
        assertThatNoException().isThrownBy(() -> {
            final var result = memoising.apply(5);
            assertThat(result).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(3);

            // Repeated call with x=5 uses cache
            final var cachedResult = memoising.apply(5);
            assertThat(cachedResult).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(3);
        });
    }

    @Test
    void testMemoisingApplicableUsesEqualsForCacheKeys() {
        LOGGER.info("Test memoisingApplicable uses equals/hashCode for cache keys");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<String, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            return x.length();
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            // Two different String objects with same content
            final var str1 = "test";
            final var str2 = "test";

            final var result1 = memoising.apply(str1);
            assertThat(result1).isEqualTo(4);
            assertThat(invocationCounter.get()).isEqualTo(1);

            // Should use cache because str2.equals(str1)
            final var result2 = memoising.apply(str2);
            assertThat(result2).isEqualTo(4);
            assertThat(invocationCounter.get()).isEqualTo(1); // Still 1
        });
    }

    @Test
    void testClearRemovesCachedResults() {
        LOGGER.info("Test clear removes cached results");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            // First call
            final var result1 = memoising.apply(5);
            assertThat(result1).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(1);

            // Second call - uses cache
            final var result2 = memoising.apply(5);
            assertThat(result2).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(1);

            // Clear cache
            final var returnValue = memoising.clear();
            assertThat(returnValue).isSameAs(memoising); // Check chaining

            // Third call - should recompute
            final var result3 = memoising.apply(5);
            assertThat(result3).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(2); // Invoked again
        });
    }

    @Test
    void testRetryAfterFailureSucceedsAndCachesResult() {
        LOGGER.info("Test retry after failure succeeds and caches result");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            final var count = invocationCounter.incrementAndGet();
            if (count == 1) {
                throw new IllegalStateException("First call fails");
            }
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);

        // First call throws exception
        assertThatThrownBy(() -> memoising.apply(5))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("First call fails");
        assertThat(invocationCounter.get()).isEqualTo(1);

        // Second call without calling clear() should retry and succeed (because failure was not cached)
        assertThatNoException().isThrownBy(() -> {
            final var result = memoising.apply(5);
            assertThat(result).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(2);

            // Third call should now use the cached successful result
            final var cachedResult = memoising.apply(5);
            assertThat(cachedResult).isEqualTo(10);
            assertThat(invocationCounter.get()).isEqualTo(2); // Still 2, cached!
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testThreadSafety() throws InterruptedException {
        LOGGER.info("Test thread-safety of memoisingApplicable");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            // Simulate some work
            Thread.sleep(10);
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);
        final int threadCount = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);

        // Submit multiple threads that all try to compute the same value
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    final var result = memoising.apply(42);
                    assertThat(result).isEqualTo(84);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // The function should have been invoked only once despite multiple concurrent calls
        // (computeIfAbsent guarantees atomic insertion)
        assertThat(invocationCounter.get()).isEqualTo(1);
    }

    @Test
    void testMemoisingApplicableWithDifferentInputTypes() {
        LOGGER.info("Test memoisingApplicable with different input types");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<String, Integer> applicable = s -> {
            invocationCounter.incrementAndGet();
            return s.length();
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            assertThat(memoising.apply("hello")).isEqualTo(5);
            assertThat(invocationCounter.get()).isEqualTo(1);

            assertThat(memoising.apply("hello")).isEqualTo(5);
            assertThat(invocationCounter.get()).isEqualTo(1); // Cached

            assertThat(memoising.apply("world")).isEqualTo(5);
            assertThat(invocationCounter.get()).isEqualTo(2);

            assertThat(memoising.apply("hi")).isEqualTo(2);
            assertThat(invocationCounter.get()).isEqualTo(3);

            assertThat(memoising.apply("hello")).isEqualTo(5);
            assertThat(invocationCounter.get()).isEqualTo(3); // Still cached
        });
    }

    @SuppressWarnings("unused")
    @Test
    void testMemoisingApplicablePreservesExceptionStackTrace() {
        LOGGER.info("Test memoisingApplicable preserves exception stack trace");

        final var testException = new RuntimeException("Test");
        final Applicable<Integer, Integer> applicable = x -> {
            throw testException;
        };

        final var memoising = memoisingApplicable(applicable);

        // First call
        assertThatThrownBy(() -> memoising.apply(1))
            .isSameAs(testException); // Same instance

        // Second call - should get same exception instance with same stack trace
        assertThatThrownBy(() -> memoising.apply(1))
            .isSameAs(testException)
            .hasStackTraceContaining("testMemoisingApplicablePreservesExceptionStackTrace"); // Verify stack trace is preserved
    }

    @Test
    void testMemoisingApplicableWithIdentityFunction() {
        LOGGER.info("Test memoisingApplicable with identity function");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            return x;
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            assertThat(memoising.apply(42)).isEqualTo(42);
            assertThat(invocationCounter.get()).isEqualTo(1);

            assertThat(memoising.apply(42)).isEqualTo(42);
            assertThat(invocationCounter.get()).isEqualTo(1); // Cached
        });
    }

    @Test
    void testMemoisingApplicableWithMultipleClearCalls() {
        LOGGER.info("Test memoisingApplicable with multiple clear calls");

        final var invocationCounter = new AtomicInteger(0);
        final Applicable<Integer, Integer> applicable = x -> {
            invocationCounter.incrementAndGet();
            return x * 2;
        };

        final var memoising = memoisingApplicable(applicable);

        assertThatNoException().isThrownBy(() -> {
            memoising.apply(5);
            assertThat(invocationCounter.get()).isEqualTo(1);

            // Multiple clear calls should not cause issues
            memoising.clear();
            memoising.clear();
            memoising.clear();

            memoising.apply(5);
            assertThat(invocationCounter.get()).isEqualTo(2); // Recomputed
        });
    }

}
