package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;
import static org.quurz.foomp.base.functions.Fun.fun;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("DataFlowIssue")
class FunTest
        extends TestHelper {

    private static final Logger LOGGER
            = getLogger(FunTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testFunWithNullFunction() {
        LOGGER.info("Test Fun.fun with null function");

        final Function<String, String> nullReturningFunction
                = _$ -> null;
        final Function<String, String> function
                = Function.identity();

        assertThatThrownBy(() -> fun(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun(nullReturningFunction).apply(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fun(nullReturningFunction).apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);

        assertThat(fun(function).apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("unused")
    @Test
    void testFunWithNullReturningFunction() {
        LOGGER.info("Test Fun.fun with null returning function");

        assertThatThrownBy(() -> fun(_$ -> null).apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testFunWithValidFunction() {
        LOGGER.info("Test Fun.fun with valid function");

        final Function<Integer, Integer> function
                = i -> i + 5;
        assertThatNoException()
                .isThrownBy(() -> {
                    final var fun
                            = fun(function);

                    assertThat(fun.apply(5))
                            .isEqualTo(10);
                });
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testComposeWithNullFunction() {
        LOGGER.info("Test fun.compose with null-function");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(null);
                }).isInstanceOf(NullPointerException.class);

    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testComposeWithNullReturningAfter() {
        LOGGER.info("Test fun.compose with null-returning after");

        final Fun<Integer, Integer> fun
                = i -> null;
        final Fun<Integer, Integer> before
                = i -> i + 5;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(before).apply(0);
                })
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testComposeWithNullReturningBefore() {
        LOGGER.info("Test fun.compose with null-returning before");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        final Fun<String, Integer> before
                = i -> null;
        assertThatException()
                .isThrownBy(() -> {
                    final var _$
                            = fun.compose(before).apply(SOME_STRING_VALUE);
                })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testComposeWithValidBefore() {
        LOGGER.info("Test fun.compose with valid funs");

        Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = fun.compose(fun).apply(0);
                    assertThat(result)
                            .isEqualTo(10);
                });
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testAndThenWithNullAfter() {
        LOGGER.info("Test fun.andThen with null-fun");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.andThen(null);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testAndThenWithNullReturningAfter() {
        LOGGER.info("Test fun.andThen with null-returning after");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        final Fun<Integer, Integer> after
                = i -> null;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.andThen(after).apply(0);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAsyncReturnsFunction() {
        LOGGER.info("Test fun.async() returns a Fun");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        assertThat(asyncFun).isNotNull();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testAsyncWithNullSupplier() {
        LOGGER.info("Test fun.async() with null supplier throws NPE");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        assertThatThrownBy(() -> asyncFun.apply(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAsyncWithNullReturningSupplier() {
        LOGGER.info("Test fun.async() with null-returning supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        final CompletableFuture<Integer> future = asyncFun.apply(() -> null);

        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testAsyncWithNullReturningFunction() {
        LOGGER.info("Test fun.async() with null-returning function");

        final Fun<Integer, Integer> fun = Fun.fun(_$ -> null);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        final CompletableFuture<Integer> future = asyncFun.apply(() -> 42);

        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    void testAsyncWithValidInputs() throws Exception {
        LOGGER.info("Test fun.async() with valid inputs");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        final CompletableFuture<Integer> future = asyncFun.apply(() -> 21);

        assertThat(future.get()).isEqualTo(42);
    }

    @Test
    void testAsyncCanBeReused() throws Exception {
        LOGGER.info("Test fun.async() can be reused multiple times");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async();

        final CompletableFuture<Integer> future1 = asyncFun.apply(() -> 10);
        final CompletableFuture<Integer> future2 = asyncFun.apply(() -> 20);
        final CompletableFuture<Integer> future3 = asyncFun.apply(() -> 30);

        assertThat(future1.get()).isEqualTo(20);
        assertThat(future2.get()).isEqualTo(40);
        assertThat(future3.get()).isEqualTo(60);
    }

    @Test
    void testAsyncWithComposition() throws Exception {
        LOGGER.info("Test fun.async() with composed functions");

        final Fun<Integer, Integer> doubler = Fun.fun(x -> x * 2);
        final Fun<Integer, Integer> incrementer = Fun.fun(x -> x + 1);
        final Fun<Integer, Integer> composed = doubler.andThen(incrementer);

        final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = composed.async();
        final CompletableFuture<Integer> future = asyncFun.apply(() -> 20);

        assertThat(future.get()).isEqualTo(41); // (20 * 2) + 1
    }

    // ============================================
    // Tests für async(ExecutorService)
    // ============================================

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testAsyncWithExecutorAndNullExecutor() {
        LOGGER.info("Test fun.async(executor) with null executor");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);

        assertThatThrownBy(() -> fun.async(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testAsyncWithExecutorReturnsFunction() {
        LOGGER.info("Test fun.async(executor) returns a Fun");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async(executor);
            assertThat(asyncFun).isNotNull();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testAsyncWithExecutorAndNullSupplier() {
        LOGGER.info("Test fun.async(executor) with null supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async(executor);

            assertThatThrownBy(() -> asyncFun.apply(null))
                    .isInstanceOf(NullPointerException.class);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testAsyncWithExecutorAndNullReturningSupplier() {
        LOGGER.info("Test fun.async(executor) with null-returning supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async(executor);
            final CompletableFuture<Integer> future = asyncFun.apply(() -> null);

            assertThatThrownBy(future::get)
                    .isInstanceOf(ExecutionException.class)
                    .hasCauseInstanceOf(NullPointerException.class);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testAsyncWithExecutorAndValidInputs() throws Exception {
        LOGGER.info("Test fun.async(executor) with valid inputs");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async(executor);
            final CompletableFuture<Integer> future = asyncFun.apply(() -> 21);

            assertThat(future.get()).isEqualTo(42);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testAsyncWithExecutorUsesProvidedExecutor() throws Exception {
        LOGGER.info("Test fun.async(executor) uses the provided executor");

        final AtomicInteger executionCount = new AtomicInteger(0);
        final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            executionCount.incrementAndGet();
            return new Thread(r);
        });

        try {
            final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
            final Fun<Supplier<Integer>, CompletableFuture<Integer>> asyncFun = fun.async(executor);

            final CompletableFuture<Integer> future = asyncFun.apply(() -> 21);
            future.get();

            // Der custom executor sollte verwendet worden sein
            assertThat(executionCount.get()).isGreaterThan(0);
        } finally {
            executor.shutdown();
        }
    }

    // ============================================
    // Tests für deferredAsync()
    // ============================================

    @Test
    void testDeferredAsyncReturnsFunction() {
        LOGGER.info("Test fun.deferredAsync() returns a Fun");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();

        assertThat(deferredAsyncFun).isNotNull();
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testDeferredAsyncWithNullSupplier() {
        LOGGER.info("Test fun.deferredAsync() with null supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();

        assertThatThrownBy(() -> deferredAsyncFun.apply(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testDeferredAsyncReturnsProvider() {
        LOGGER.info("Test fun.deferredAsync() returns a Provider");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();

        final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> 21);

        assertThat(provider).isNotNull();
    }

    @Test
    void testDeferredAsyncDoesNotExecuteImmediately() {
        LOGGER.info("Test fun.deferredAsync() does not execute immediately");

        final AtomicBoolean executed = new AtomicBoolean(false);
        final Fun<Integer, Integer> fun = Fun.fun(x -> {
            executed.set(true);
            return x * 2;
        });

        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();
        final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> 21);

        // Provider wurde erstellt, aber noch nicht ausgeführt
        assertThat(executed.get()).isFalse();

        // Erst beim Aufruf von get() wird der Task gestartet
        provider.get();

        // Kurz warten, damit der async Task starten kann
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(executed.get()).isTrue();
    }

    @Test
    void testDeferredAsyncWithValidInputs() throws Exception {
        LOGGER.info("Test fun.deferredAsync() with valid inputs");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();

        final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> 21);
        final CompletableFuture<Integer> future = provider.get();

        assertThat(future.get()).isEqualTo(42);
    }

    @Test
    void testDeferredAsyncWithNullReturningSupplier() {
        LOGGER.info("Test fun.deferredAsync() with null-returning supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync();

        final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> null);
        final CompletableFuture<Integer> future = provider.get();

        assertThatThrownBy(future::get)
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(NullPointerException.class);
    }

    // ============================================
    // Tests für deferredAsync(ExecutorService)
    // ============================================

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testDeferredAsyncWithExecutorAndNullExecutor() {
        LOGGER.info("Test fun.deferredAsync(executor) with null executor");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);

        assertThatThrownBy(() -> fun.deferredAsync(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testDeferredAsyncWithExecutorReturnsFunction() {
        LOGGER.info("Test fun.deferredAsync(executor) returns a Fun");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync(executor);
            assertThat(deferredAsyncFun).isNotNull();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void testDeferredAsyncWithExecutorAndNullSupplier() {
        LOGGER.info("Test fun.deferredAsync(executor) with null supplier");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync(executor);

            assertThatThrownBy(() -> deferredAsyncFun.apply(null))
                    .isInstanceOf(NullPointerException.class);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testDeferredAsyncWithExecutorDoesNotExecuteImmediately() {
        LOGGER.info("Test fun.deferredAsync(executor) does not execute immediately");

        final AtomicBoolean executed = new AtomicBoolean(false);
        final Fun<Integer, Integer> fun = Fun.fun(x -> {
            executed.set(true);
            return x * 2;
        });

        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync(executor);
            final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> 21);

            // Provider wurde erstellt, aber noch nicht ausgeführt
            assertThat(executed.get()).isFalse();

            // Erst beim Aufruf von get() wird der Task gestartet
            provider.get();

            // Kurz warten, damit der async Task starten kann
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            assertThat(executed.get()).isTrue();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void testDeferredAsyncWithExecutorAndValidInputs() throws Exception {
        LOGGER.info("Test fun.deferredAsync(executor) with valid inputs");

        final Fun<Integer, Integer> fun = Fun.fun(x -> x * 2);
        final ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            final Fun<Supplier<Integer>, Provider<CompletableFuture<Integer>>> deferredAsyncFun = fun.deferredAsync(executor);
            final Provider<CompletableFuture<Integer>> provider = deferredAsyncFun.apply(() -> 21);
            final CompletableFuture<Integer> future = provider.get();

            assertThat(future.get()).isEqualTo(42);
        } finally {
            executor.shutdown();
        }
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testDeferWithNullSupplier() {
        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.defer(null);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testDeferWithNullReturningSupplier() {
        LOGGER.info("Test fun.defer with null-returning supplier");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatThrownBy(() -> {
            final var _$
                    = fun.defer(() -> null).call();
        })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testDeferWithValidSupplier() {
        LOGGER.info("Test fun.defer with valid supplier");

        final Fun<Integer, Integer> fun
                = i -> i + 5;
        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = fun.defer(() -> 0).call();
                    assertThat(result)
                            .isEqualTo(5);
                });
    }

    // ============================================
    // Tests für identity, memoise, nullSafe, etc.
    // ============================================

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testIdentityWithNullArgument() {
        LOGGER.info("Test Fun.identity with null-argument");

        assertThatThrownBy(() -> {
            final var _$
                    = Fun.identity().apply(null);
        })
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testIdentityWithValidArgument() {
        LOGGER.info("Test Fun.identity with valid argument");

        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = Fun.identity().apply(SOME_STRING_VALUE);
                    assertThat(result)
                            .isEqualTo(SOME_STRING_VALUE);
                });
    }

    @Test
    void testMemoise() {
        LOGGER.info("Test fun.memoising");

        final Fun<String, String> fun
                = s -> s;
        assertThatNoException()
                .isThrownBy(fun::memoise);
        assertThat(fun.memoise())
                .isNotNull();
        assertThat(fun.memoise().apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings({"LambdaBodyCanBeCodeBlock", "unused"})
    @Test
    void testNullSafe() {
        LOGGER.info("Test fun.nullChecked");

        final Fun<String, String> nullReturningFun
                = _$ -> null;
        final Fun<String, String> fun
                = Fun.identity();

        assertThatThrownBy(() -> nullReturningFun.nullSafe().apply(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> nullReturningFun.nullSafe().apply(SOME_STRING_VALUE))
                .isInstanceOf(NullPointerException.class);
        assertThat(fun.nullSafe().apply(SOME_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testApplicable() {
        LOGGER.info("Test fun.applicable");

        final Fun<String, String> fun
                = s -> s;
        assertThatNoException()
                .isThrownBy(fun::applicable);
        assertThat(fun.applicable())
                .isNotNull();
        assertThatNoException()
                .isThrownBy(() -> {
                    final var result
                            = fun.applicable().apply(SOME_STRING_VALUE);
                    assertThat(result)
                            .isEqualTo(SOME_STRING_VALUE);
                });
    }

}