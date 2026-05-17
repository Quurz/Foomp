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

class CheckedProviderTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(CheckedProviderTest.class);

    @Test
    void testGetReturnsValue() {
        LOGGER.info("Test CheckedProvider.get() returns value");

        final CheckedProvider<String> supplier = () -> "Hello";

        assertThatNoException().isThrownBy(() -> {
            final var result = supplier.get();
            assertThat(result).isEqualTo("Hello");
        });
    }

    @Test
    void testGetThrowsCheckedException() {
        LOGGER.info("Test CheckedProvider.get() throws checked exception");

        final var testException = new IOException("Test IO exception");
        final CheckedProvider<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(supplier::get)
            .isSameAs(testException);
    }

    @Test
    void testGetThrowsRuntimeException() {
        LOGGER.info("Test CheckedProvider.get() throws runtime exception");

        final var testException = new IllegalStateException("Test state exception");
        final CheckedProvider<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(supplier::get)
            .isSameAs(testException);
    }

    @Test
    void testGetIsCalledOnEachInvocation() {
        LOGGER.info("Test CheckedProvider.get() is called on each invocation");

        final var invocationCounter = new AtomicInteger(0);
        final CheckedProvider<Integer> supplier = invocationCounter::incrementAndGet;

        assertThatNoException().isThrownBy(() -> {
            assertThat(supplier.get()).isEqualTo(1);
            assertThat(supplier.get()).isEqualTo(2);
            assertThat(supplier.get()).isEqualTo(3);
        });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApplyWithNullNothing() {
        LOGGER.info("Test CheckedProvider.apply() with null Nothing");

        final CheckedProvider<String> supplier = () -> "Hello";

        assertThatThrownBy(() -> supplier.apply(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testApplyWithNothingReturnsValue() {
        LOGGER.info("Test CheckedProvider.apply() with Nothing returns value");

        final CheckedProvider<String> supplier = () -> "World";

        assertThatNoException().isThrownBy(() -> {
            final var result = supplier.apply(nothing);
            assertThat(result).isEqualTo("World");
        });
    }

    @Test
    void testApplyWithNothingThrowsException() {
        LOGGER.info("Test CheckedProvider.apply() with Nothing throws exception");

        final var testException = new Exception("Test exception");
        final CheckedProvider<String> supplier = () -> {
            throw testException;
        };

        assertThatThrownBy(() -> supplier.apply(nothing))
            .isSameAs(testException);
    }

    @Test
    void testApplyCallsGet() {
        LOGGER.info("Test CheckedProvider.apply() calls get()");

        final var invocationCounter = new AtomicInteger(0);
        final CheckedProvider<Integer> supplier = invocationCounter::incrementAndGet;

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
        LOGGER.info("Test CheckedProvider.apply() ignores Nothing value (but validates it)");

        final CheckedProvider<String> supplier = () -> "Constant";

        assertThatNoException().isThrownBy(() -> {
            // The Nothing value is validated but otherwise ignored
            final var result = supplier.apply(nothing);
            assertThat(result).isEqualTo("Constant");
        });
    }

    @Test
    void testCheckedProviderWithDifferentReturnTypes() {
        LOGGER.info("Test CheckedProvider with different return types");

        final CheckedProvider<Integer> intSupplier = () -> 42;
        final CheckedProvider<String> stringSupplier = () -> "test";
        final CheckedProvider<Boolean> boolSupplier = () -> true;

        assertThatNoException().isThrownBy(() -> {
            assertThat(intSupplier.get()).isEqualTo(42);
            assertThat(stringSupplier.get()).isEqualTo("test");
            assertThat(boolSupplier.get()).isTrue();
        });
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    void testCheckedProviderAsApplicable() {
        LOGGER.info("Test CheckedProvider can be used as Applicable");

        final CheckedProvider<String> supplier = () -> "Hello";
        final Applicable<?, String> applicable = supplier;

        assertThatNoException().isThrownBy(() -> {
            @SuppressWarnings("unchecked")
            final var result = ((Applicable<Object, String>) applicable).apply(nothing);
            assertThat(result).isEqualTo("Hello");
        });
    }

    @Test
    void testCheckedProviderWithSafeMethod() {
        LOGGER.info("Test CheckedProvider.safe() inherited from Applicable");

        final CheckedProvider<String> successSupplier = () -> "Success";
        final CheckedProvider<String> failureSupplier = () -> {
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
    void testCheckedProviderComposition() {
        LOGGER.info("Test CheckedProvider can be composed");

        final var counter = new AtomicInteger(0);
        final CheckedProvider<Integer> supplier = counter::incrementAndGet;

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
    void testCheckedProviderExceptionMessage() {
        LOGGER.info("Test CheckedProvider preserves exception messages");

        final var exceptionMessage = "Detailed error message";
        final CheckedProvider<String> supplier = () -> {
            throw new RuntimeException(exceptionMessage);
        };

        assertThatThrownBy(supplier::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(exceptionMessage);
    }

    @Test
    void testCheckedProviderWithComplexComputations() {
        LOGGER.info("Test CheckedProvider with complex computations");

        final CheckedProvider<String> supplier = () -> {
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
    void testCheckedProviderMultipleExceptionTypes() {
        LOGGER.info("Test CheckedProvider can throw multiple exception types");

        final CheckedProvider<String> ioExceptionSupplier = () -> {
            throw new IOException("IO problem");
        };

        final CheckedProvider<String> illegalStateSupplier = () -> {
            throw new IllegalStateException("State problem");
        };

        final CheckedProvider<String> checkedExceptionSupplier = () -> {
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
