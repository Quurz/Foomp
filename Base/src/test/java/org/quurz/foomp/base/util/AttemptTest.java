package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Attempt.attempt;
import static org.quurz.foomp.base.util.Result.success;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
class AttemptTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(AttemptTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAttempt() {
        LOGGER.info("Test Attempt.attempt");

        assertThatThrownBy(() -> attempt(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> attempt(5));
    }

    @Test
    void testTryIt() {
        LOGGER.info("Test attempt.tryIt");

        assertThat(attempt(5).tryIt().isSuccess())
            .isTrue();
        assertThat(attempt(5).tryIt())
            .isEqualTo(success(5));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testOnFailureRecoverWithSupplier() {
        LOGGER.info("Test attempt.onFailureRecover with supplier");

        final Function<Integer, Integer> throwingFunction
            = i -> { throw new IllegalStateException(); };
        final var invocationCountingSupplier
            = super.invocationCountingSupplier(() -> 23);

        final var attempt
            = attempt(5);

        assertThatThrownBy(() -> attempt.onFailureRecover((Supplier<Integer>) null))
            .isInstanceOf(NullPointerException.class);

        assertThat(attempt.map(Function.identity()).onFailureRecover(invocationCountingSupplier).tryIt())
            .isEqualTo(success(5));
        assertThat(invocationCountingSupplier.getInvocationCount())
            .isZero();

        assertThat(attempt.map(throwingFunction).onFailureRecover(invocationCountingSupplier).tryIt())
            .isEqualTo(success(23));
        assertThat(invocationCountingSupplier.getInvocationCount())
            .isEqualTo(1);

        assertThat(attempt.map(throwingFunction).onFailureRecover(() -> null).tryIt().getException())
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testOnFailureRecoverWithFunction() {
        LOGGER.info("Test attempt.onFailureRecover with function");

        final Function<Integer, Integer> throwingFunction
            = i -> { throw new IllegalStateException(); };
        final var invocationCountingFun
            = super.invocationCountingFun((Exception exception) -> 23);

        final var attempt
            = attempt(5);

        assertThatThrownBy(() -> attempt.onFailureRecover((Function<? super Exception, Integer>) null))
            .isInstanceOf(NullPointerException.class);

        assertThat(attempt.map(Function.identity()).onFailureRecover(invocationCountingFun).tryIt())
            .isEqualTo(success(5));
        assertThat(invocationCountingFun.getInvocationCount())
            .isZero();

        assertThat(attempt.map(throwingFunction).onFailureRecover(invocationCountingFun).tryIt())
            .isEqualTo(success(23));
        assertThat(invocationCountingFun.getInvocationCount())
            .isEqualTo(1);

        assertThat(attempt.map(throwingFunction).onFailureRecover(exception -> null).tryIt().getException())
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testOnFailureThrow() {
        LOGGER.info("Test attempt.onFailureThrow");

        final Function<Integer, Integer> throwingFunction
            = i -> { throw new IllegalStateException(); };

        final var attempt
            = attempt(5);

        assertThatNoException()
            .isThrownBy(() -> attempt.map(Function.identity()).onFailureThrow().tryIt());

        assertThatThrownBy(() -> attempt.map(throwingFunction).onFailureThrow())
            .isInstanceOf(IllegalStateException.class);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPeekFailureLazy() {
        LOGGER.info("Test attempt.peekFailureLazy");

        final Function<Integer, Integer> throwingFunction
            = i -> { throw new IllegalStateException(); };
        final var invocationCountingConsumer
            = super.invocationCountingConsumer((Exception exception) -> {});

        final var attempt
            = attempt(5);

        assertThatThrownBy(() -> attempt.peekFailureLazy(null))
            .isInstanceOf(NullPointerException.class);

        attempt.map(throwingFunction).peekFailureLazy(invocationCountingConsumer);
        assertThat(invocationCountingConsumer.getInvocationCount())
            .isZero();

        assertThat(attempt.map(throwingFunction).peekFailureLazy(invocationCountingConsumer).tryIt())
            .isInstanceOf(Result.Failure.class);
        assertThat(invocationCountingConsumer.getInvocationCount())
            .isEqualTo(1);
        invocationCountingConsumer.resetInvocationCount();

        assertThat(attempt.map(Fun.identity()).peekFailureLazy(invocationCountingConsumer).tryIt())
            .isInstanceOf(Result.Success.class);
        assertThat(invocationCountingConsumer.getInvocationCount())
            .isZero();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPeekFailureEager() {
        LOGGER.info("Test attempt.peekFailureEager");

        final Function<Integer, Integer> throwingFunction
            = i -> { throw new IllegalStateException(); };
        final var invocationCountingConsumer
            = super.invocationCountingConsumer((Exception exception) -> {});

        final var attempt
            = attempt(5);

        assertThatThrownBy(() -> attempt.peekFailureEager(null))
            .isInstanceOf(NullPointerException.class);

        attempt.map(throwingFunction).peekFailureEager(invocationCountingConsumer);
        assertThat(invocationCountingConsumer.getInvocationCount())
            .isEqualTo(1);
        invocationCountingConsumer.resetInvocationCount();

        assertThat(attempt.map(Fun.identity()).peekFailureEager(invocationCountingConsumer).tryIt())
            .isInstanceOf(Result.Success.class);
        assertThat(invocationCountingConsumer.getInvocationCount())
            .isZero();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap() {
        LOGGER.info("Test attempt.map");

        assertThatThrownBy(() -> attempt(5).map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).map(_$ -> null).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).map(_$ -> null).tryIt().getException())
            .isInstanceOf(NullPointerException.class);

        assertThat(attempt(5).map(i -> i * 2).tryIt().getValue())
            .isEqualTo(10);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapUnsafe() {
        LOGGER.info("Test attempt.mapUnsafe");

        assertThatThrownBy(() -> attempt(5).mapUnsafe(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).mapUnsafe(_$ -> null).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).mapUnsafe(_$ -> null).tryIt().getException())
            .isInstanceOf(NullPointerException.class);

        assertThat(attempt(5).mapUnsafe(i -> i * 2).tryIt().getValue())
            .isEqualTo(10);
    }

    @SuppressWarnings({"DataFlowIssue", "ThrowableNotThrown"})
    @Test
    void testLift() {
        LOGGER.info("Test attempt.lift");

        assertThatThrownBy(() -> attempt(5).lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).lift(attempt(_$ -> null)).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).lift(attempt(_$ -> null)).tryIt().getException())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).lift(attempt(Fun.identity())).tryIt().getException())
            .isInstanceOf(NoSuchElementException.class);

        assertThatThrownBy(
                () -> attempt(5)
                        .lift(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                        .tryIt()
                        .getValue()
            )
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).lift(attempt(_$ -> { throw new IllegalArgumentException(); } )).tryIt().getException())
            .isInstanceOf(IllegalArgumentException.class);

        final var exception1
            = attempt(5)
                .map(_$ -> { throw new IllegalStateException(); })
                .lift(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                .tryIt()
                .getException();
        assertThat(exception1)
            .isInstanceOf(IllegalStateException.class);

        final var exception2
            = attempt(5)
                .lift(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                .map(_$ -> { throw new IllegalStateException(); })
                .tryIt()
                .getException();
        assertThat(exception2)
            .isInstanceOf(IllegalArgumentException.class);

        assertThat(attempt(5).lift(attempt(i -> i * 2)).tryIt())
            .isEqualTo(success(10));
    }

    @SuppressWarnings({"DataFlowIssue", "ThrowableNotThrown"})
    void testLiftUnsafe() {
        LOGGER.info("Test attempt.liftUnsafe");

        assertThatThrownBy(() -> attempt(5).liftUnsafe(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).liftUnsafe(attempt(_$ -> null)).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).liftUnsafe(attempt(_$ -> null)).tryIt().getException())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).liftUnsafe(attempt(Fun.identity())).tryIt().getException())
            .isInstanceOf(NoSuchElementException.class);

        assertThatThrownBy(
            () -> attempt(5)
                    .liftUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                    .tryIt()
                    .getValue()
            )
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).liftUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); } )).tryIt().getException())
                .isInstanceOf(IllegalArgumentException.class);

        final var exception1
            = attempt(5)
                .map(_$ -> { throw new IllegalStateException(); })
                .liftUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                .tryIt()
                .getException();
        assertThat(exception1)
            .isInstanceOf(IllegalStateException.class);

        final var exception2
            = attempt(5)
                .liftUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); } ))
                .map(_$ -> { throw new IllegalStateException(); })
                .tryIt()
                .getException();
        assertThat(exception2)
            .isInstanceOf(IllegalArgumentException.class);

        assertThat(attempt(5).liftUnsafe(attempt(i -> i * 2)).tryIt())
            .isEqualTo(success(10));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBind() {
        LOGGER.info("Test attempt.bind");

        assertThatThrownBy(() -> attempt(5).bind(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).bind(_$ -> null).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).bind(i -> attempt(i * 2)).tryIt().getValue())
            .isEqualTo(10);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBindUnsafe() {
        LOGGER.info("Test attempt.bindUnsafe");

        assertThatThrownBy(() -> attempt(5).bindUnsafe(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> attempt(5).bindUnsafe(_$ -> null).tryIt().getValue())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(attempt(5).bindUnsafe(i -> attempt(i * 2)).tryIt().getValue())
            .isEqualTo(10);
    }

    @Test
    void testUnwind() {
        LOGGER.info("Test attempt.unwind");

        final InvocationCountingFun<Integer, Integer> invocationCountingFun
            = super.invocationCountingFun(i -> i * 2);

        final var attempt
            = attempt(5)
                .map(invocationCountingFun)
                .lift(attempt(invocationCountingFun))
                .bind(i -> attempt(invocationCountingFun.apply(i)));

        assertThat(invocationCountingFun.getInvocationCount())
            .isZero();

        attempt.unwind();
        assertThat(invocationCountingFun.getInvocationCount())
            .isEqualTo(3);

        assertThat(attempt.unwind().tryIt().getValue())
            .isEqualTo(40);
    }

}
