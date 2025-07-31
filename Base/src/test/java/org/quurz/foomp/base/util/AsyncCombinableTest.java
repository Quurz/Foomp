package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.async.AsyncExecutionException;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.quurz.foomp.base.util.async.AsyncCombinable.asyncCombinable;
import static org.slf4j.LoggerFactory.getLogger;

class AsyncCombinableTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(AsyncCombinableTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAsyncCombinable() {
        LOGGER.info("Test Async.async");

        assertThatThrownBy(() -> asyncCombinable(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> asyncCombinable(SOME_STRING_VALUE));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test async.map");

        final var executor
            = Executors.newVirtualThreadPerTaskExecutor();
        final var timeout
            = Duration.ofMillis(500);
        final var negativeTimeout
            = Duration.ofSeconds(-1);

        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).map(s -> null).execute(executor, negativeTimeout))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).map(s -> null).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).map(s -> { throw new AsyncExecutionException(); }).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).map(s -> { throw new AsyncExecutionException("<OUTER>", new AsyncExecutionException("<INNER>")); }).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class)
            .hasMessage("<INNER>")
            .hasNoCause();
        assertThatNoException().isThrownBy(() -> {
            final var result
                = asyncCombinable(SOME_STRING_VALUE).map(String::length).execute(executor, timeout);
            assertThat(result)
                .isEqualTo(SOME_STRING_VALUE.length());
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test async.lift");

        final var executor
            = Executors.newVirtualThreadPerTaskExecutor();
        final var timeout
            = Duration.ofMillis(500);

        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).lift(asyncCombinable(_$ -> null)).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class)
            .hasCauseInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var result
                = asyncCombinable(SOME_STRING_VALUE).lift(asyncCombinable(String::length)).execute(executor, timeout);
            assertThat(result)
                .isEqualTo(SOME_STRING_VALUE.length());
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testBind() {
        LOGGER.info("Test async.bind");

        final var executor
            = Executors.newVirtualThreadPerTaskExecutor();
        final var timeout
            = Duration.ofMillis(500);

        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).bind(_$ -> null).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class)
            .hasCauseInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(SOME_STRING_VALUE).bind(s -> asyncCombinable(null)).execute(executor, timeout))
            .isInstanceOf(AsyncExecutionException.class)
            .hasCauseInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var result
                = asyncCombinable(SOME_STRING_VALUE).bind(s -> asyncCombinable(s.length())).execute(executor, timeout);
            assertThat(result)
                .isEqualTo(SOME_STRING_VALUE.length());
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused", "LambdaBodyCanBeCodeBlock"})
    @Test
    void testCombine() {
        LOGGER.info("Test async.combine");

        final var executor
            = Executors.newVirtualThreadPerTaskExecutor();
        final var timeout
            = Duration.ofMillis(100);

        assertThatThrownBy(() -> asyncCombinable(0).combine().with(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(0).combine().with(asyncCombinable(0), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> asyncCombinable(0).combine().with(null, (_1, _2) -> 5))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> asyncCombinable(5).combine().with(asyncCombinable(5), Integer::sum));
    }

    @SuppressWarnings("unused")
    @Test
    void testExecute() {
        LOGGER.info("Test async.execute");

        final var executor
            = Executors.newVirtualThreadPerTaskExecutor();
        final var timeout
            = Duration.ofMillis(500);

        assertThatThrownBy(() -> asyncCombinable(0).execute(executor, Duration.ofSeconds(0)))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> asyncCombinable(0).execute(executor, Duration.ofSeconds(-1)))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
            asyncCombinable(5)
                .combine()
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), (_1, _2) -> null)
                .finish()
                .execute(executor, timeout)
        )
        .isInstanceOf(AsyncExecutionException.class)
        .hasCauseInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
            asyncCombinable(5)
                .combine()
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), waitingFun(Integer::sum, Duration.ofSeconds(10)))
                .finish()
                .execute(executor, timeout)
        )
        .isInstanceOf(AsyncExecutionException.class)
        .hasCauseInstanceOf(TimeoutException.class)
        .extracting(throwable -> ((AsyncExecutionException) throwable).getThreadName())
        .isEqualTo("main");

        assertThatThrownBy(() ->
            asyncCombinable(5)
                .combine()
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5), Integer::sum)
                    .with(asyncCombinable(5).map(waitingFun(Fun.identity(), Duration.ofMillis(800))), Integer::sum)
                .finish()
                .execute(executor, timeout)
            )
            .isInstanceOf(AsyncExecutionException.class)
            .hasCauseInstanceOf(TimeoutException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = asyncCombinable(5)
                        .combine()
                        .finish()
                    .execute(executor, timeout);
                assertThat(result)
                    .isEqualTo(5);
            });

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = asyncCombinable(5)
                        .combine()
                            .with(asyncCombinable(5), Integer::sum)
                            .with(asyncCombinable(5), Integer::sum)
                        .finish()
                        .execute(executor, timeout);
                assertThat(result)
                    .isEqualTo(15);
            });

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = asyncCombinable("a")
                        .combine()
                            .with(asyncCombinable("b").map(waitingFun(Fun.identity(), Duration.ofMillis(400))), String::concat)
                            .with(asyncCombinable("c").map(waitingFun(Fun.identity(), Duration.ofMillis(300))), String::concat)
                            .with(asyncCombinable("d").map(waitingFun(Fun.identity(), Duration.ofMillis(200))), String::concat)
                            .with(asyncCombinable("e").map(waitingFun(Fun.identity(), Duration.ofMillis(100))), String::concat)
                        .finish()
                        .execute(executor, timeout);
                assertThat(result)
                    .isEqualTo("abcde");
            });
    }

}
