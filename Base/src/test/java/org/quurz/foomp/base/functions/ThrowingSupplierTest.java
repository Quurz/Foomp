package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.slf4j.LoggerFactory.getLogger;

class ThrowingSupplierTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(ThrowingSupplierTest.class);

    @Test
    void testGetReturnsValue() {
        LOGGER.info("Test ThrowingSupplier.get() returns value");

        final ThrowingSupplier<String> supplier = () -> "Hello";

        assertThatNoException().isThrownBy(() -> {
            final var result = supplier.get();
            assertThat(result).isEqualTo("Hello");
        });
    }

    @Test
    void testGetThrowsCheckedException() {
        LOGGER.info("Test ThrowingSupplier.get() throws checked exception");

        final var testException = new IOException("Test IO exception");
        final ThrowingSupplier<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(supplier::get)
            .isSameAs(testException);
    }

    @Test
    void testGetThrowsRuntimeException() {
        LOGGER.info("Test ThrowingSupplier.get() throws runtime exception");

        final var testException = new IllegalStateException("Test state exception");
        final ThrowingSupplier<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(supplier::get)
            .isSameAs(testException);
    }

    @Test
    void testGetIsCalledOnEachInvocation() {
        LOGGER.info("Test ThrowingSupplier.get() is called on each invocation");

        final var invocationCounter = new AtomicInteger(0);
        final ThrowingSupplier<Integer> supplier = invocationCounter::incrementAndGet;

        assertThatNoException().isThrownBy(() -> {
            assertThat(supplier.get()).isEqualTo(1);
            assertThat(supplier.get()).isEqualTo(2);
            assertThat(supplier.get()).isEqualTo(3);
        });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApplyWithNullNothing() {
        LOGGER.info("Test ThrowingSupplier.apply() with null Nothing");

        final ThrowingSupplier<String> supplier = () -> "Hello";

        assertThatThrownBy(() -> supplier.apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testApplyWithNothingReturnsValue() {
        LOGGER.info("Test ThrowingSupplier.apply() with Nothing returns value");

        final ThrowingSupplier<String> supplier = () -> "World";

        assertThatNoException().isThrownBy(() -> {
            final var result = supplier.apply(nothing);
            assertThat(result).isEqualTo("World");
        });
    }

    @Test
    void testApplyWithNothingThrowsException() {
        LOGGER.info("Test ThrowingSupplier.apply() with Nothing throws exception");

        final var testException = new Exception("Test exception");
        final ThrowingSupplier<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(() -> supplier.apply(nothing))
            .isSameAs(testException);
    }

    @Test
    void testApplyCallsGet() {
        LOGGER.info("Test ThrowingSupplier.apply() calls get()");

        final var invocationCounter = new AtomicInteger(0);
        final ThrowingSupplier<Integer> supplier = invocationCounter::incrementAndGet;

        assertThatNoException().isThrownBy(() -> {
            // Call via apply()
            final var result1 = supplier.apply(nothing);
            assertThat(result1).isEqualTo(1);
            
            // Call via get()
            final var result2 = supplier.get();
            assertThat(result2).isEqualTo(2);
            
            // Both methods should increment the same counter
            assertThat(invocationCounter.get()).isEqualTo(2);
        });
    }

    @Test
    void testApplyIgnoresNothingValue() {
        LOGGER.info("Test ThrowingSupplier.apply() ignores Nothing value (but validates it)");

        final ThrowingSupplier<String> supplier = () -> "Constant";

        assertThatNoException().isThrownBy(() -> {
            // The Nothing value is validated but otherwise ignored
            final var result = supplier.apply(nothing);
            assertThat(result).isEqualTo("Constant");
        });
    }

    @Test
    void testThrowingSupplierWithDifferentReturnTypes() {
        LOGGER.info("Test ThrowingSupplier with different return types");

        final ThrowingSupplier<Integer> intSupplier = () -> 42;
        final ThrowingSupplier<String> stringSupplier = () -> "test";
        final ThrowingSupplier<Boolean> boolSupplier = () -> true;

        assertThatNoException().isThrownBy(() -> {
            assertThat(intSupplier.get()).isEqualTo(42);
            assertThat(stringSupplier.get()).isEqualTo("test");
            assertThat(boolSupplier.get()).isTrue();
        });
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    void testThrowingSupplierAsApplicable() {
        LOGGER.info("Test ThrowingSupplier can be used as Applicable");

        final ThrowingSupplier<String> supplier = () -> "Hello";
        final Applicable<?, String> applicable = supplier;

        assertThatNoException().isThrownBy(() -> {
            @SuppressWarnings("unchecked")
            final var result = ((Applicable<Object, String>) applicable).apply(nothing);
            assertThat(result).isEqualTo("Hello");
        });
    }

    @Test
    void testThrowingSupplierWithSafeMethod() {
        LOGGER.info("Test ThrowingSupplier.safe() inherited from Applicable");

        final ThrowingSupplier<String> successSupplier = () -> "Success";
        final ThrowingSupplier<String> failureSupplier = () -> {
            throw new IllegalStateException("Failure");
        };

        assertThatNoException().isThrownBy(() -> {
            // Test successful execution
            final var successResult = successSupplier.safe().apply(nothing);
            checkIsRightWithValue(successResult, "Success");

            // Test failure execution
            final var failureResult = failureSupplier.safe().apply(nothing);
            checkIsLeftWithPredicate(
                failureResult,
                ex -> ex instanceof IllegalStateException && ex.getMessage().equals("Failure")
            );
        });
    }

    @Test
    void testThrowingSupplierWithDeferMethod() {
        LOGGER.info("Test ThrowingSupplier.defer() inherited from Applicable");

        final ThrowingSupplier<String> supplier = () -> "Deferred";

        assertThatNoException().isThrownBy(() -> {
            final var callable = supplier.defer(() -> nothing);
            final var result = callable.call();
            assertThat(result).isEqualTo("Deferred");
        });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testThrowingSupplierWithDeferMethodAndNullSupplier() {
        LOGGER.info("Test ThrowingSupplier.defer() with null supplier throws exception");

        final ThrowingSupplier<String> supplier = () -> "Test";

        assertThatThrownBy(() -> supplier.defer(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testThrowingSupplierWithDeferMethodAndThrowingSupplier() {
        LOGGER.info("Test ThrowingSupplier.defer() propagates exceptions");

        final var testException = new IOException("Deferred exception");
        final ThrowingSupplier<String> supplier = () -> {
            throw testException;
        };

        final var callable = supplier.defer(() -> nothing);

        assertThatThrownBy(callable::call)
            .isSameAs(testException);
    }

    @Test
    void testThrowingSupplierComposition() {
        LOGGER.info("Test ThrowingSupplier can be composed");

        final var counter = new AtomicInteger(0);
        final ThrowingSupplier<Integer> supplier = counter::incrementAndGet;

        assertThatNoException().isThrownBy(() -> {
            // First invocation
            assertThat(supplier.get()).isEqualTo(1);
            
            // Second invocation via apply
            assertThat(supplier.apply(nothing)).isEqualTo(2);
            
            // Third invocation via safe
            final var safeResult = supplier.safe().apply(nothing);
            checkIsRightWithValue(safeResult, 3);
        });
    }

    @Test
    void testThrowingSupplierExceptionMessage() {
        LOGGER.info("Test ThrowingSupplier preserves exception messages");

        final var exceptionMessage = "Detailed error message";
        final ThrowingSupplier<String> supplier = () -> {
            throw new RuntimeException(exceptionMessage);
        };

        assertThatThrownBy(supplier::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(exceptionMessage);
    }

    @Test
    void testThrowingSupplierWithComplexComputations() {
        LOGGER.info("Test ThrowingSupplier with complex computations");

        final ThrowingSupplier<String> supplier = () -> {
            // Simulate complex computation
            final var sb = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                sb.append(i);
            }
            return sb.toString();
        };

        assertThatNoException().isThrownBy(() -> {
            final var result = supplier.get();
            assertThat(result).isEqualTo("01234");
        });
    }

    @Test
    void testThrowingSupplierMultipleExceptionTypes() {
        LOGGER.info("Test ThrowingSupplier can throw multiple exception types");

        final ThrowingSupplier<String> ioExceptionSupplier = () -> {
            throw new IOException("IO problem");
        };

        final ThrowingSupplier<String> illegalStateSupplier = () -> {
            throw new IllegalStateException("State problem");
        };

        final ThrowingSupplier<String> checkedExceptionSupplier = () -> {
            throw new Exception("Generic problem");
        };

        assertThatThrownBy(ioExceptionSupplier::get)
            .isInstanceOf(IOException.class);

        assertThatThrownBy(illegalStateSupplier::get)
            .isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(checkedExceptionSupplier::get)
            .isInstanceOf(Exception.class);
    }

}
