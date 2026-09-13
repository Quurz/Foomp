package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.Higher1;
import org.slf4j.Logger;

import org.quurz.foomp.base.functions.Applicable;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Attempt.attempt;
import static org.quurz.foomp.base.util.Result.success;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Attempt")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AttemptTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(AttemptTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void attempt_null_throws_NullPointerException() {
            LOGGER.info("Attempt.attempt(...) should throw NullPointerException when value is null");
            assertThatThrownBy(() -> attempt(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void attempt_value_creates_success() {
            LOGGER.info("Attempt.attempt(...) should create a successful Attempt when value is non-null");
            assertThatNoException().isThrownBy(() -> attempt(5));
        }

    }

    @Nested
    @DisplayName("TryIt")
    class TryIt {

        @Test
        void returns_success_result() {
            LOGGER.info("Attempt.tryIt() should return a Success result");
            assertThat(attempt(5).tryIt())
                .isEqualTo(success(5));
        }
    }

    @Nested
    @DisplayName("Recover")
    class Recover {

        @Nested
        @DisplayName("With Supplier")
        class With_Supplier {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void null_supplier_throws_NullPointerException() {
                LOGGER.info("Attempt.onFailureRecover(Supplier) should throw NullPointerException when recover is null");
                assertThatThrownBy(() -> attempt(5).onFailureRecover((Supplier<Integer>) null))
                    .isInstanceOf(NullPointerException.class);
            }

            @Test
            void supplier_not_invoked_on_success() {
                LOGGER.info("Attempt.onFailureRecover(Supplier) should not invoke supplier when computation succeeds");
                final var supplier = invocationCountingSupplier(() -> 23);
                assertThat(attempt(5).map(Function.identity()).onFailureRecover(supplier).tryIt())
                    .isEqualTo(success(5));
                assertThat(supplier.getInvocationCount())
                    .isZero();
            }

            @Test
            void supplier_used_on_failure_and_produces_success() {
                LOGGER.info("Attempt.onFailureRecover(Supplier) should invoke supplier on failure and recover with its value");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                final var supplier = invocationCountingSupplier(() -> 23);
                assertThat(attempt(5).map(throwing).onFailureRecover(supplier).tryIt())
                    .isEqualTo(success(23));
                assertThat(supplier.getInvocationCount())
                    .isEqualTo(1);
            }

            @Test
            void null_from_supplier_becomes_npe_failure() {
                LOGGER.info("Attempt.onFailureRecover(Supplier) should turn null from supplier into NullPointerException failure");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                assertThat(attempt(5).map(throwing).onFailureRecover(() -> null).tryIt().getThrowable())
                    .isInstanceOf(NullPointerException.class);
            }

            @Test
            void supplier_throws_other_exception_and_preserves_original_as_suppressed() {
                LOGGER.info("Attempt.onFailureRecover(Supplier) should propagate supplier exception and keep original as suppressed");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException("ORIGINAL"); };
                final var safeExecutableLikeSupplier = (Supplier<Integer>) () -> { throw new IllegalArgumentException("RECOVER"); };

                final var failure = attempt(5).map(throwing).onFailureRecover(safeExecutableLikeSupplier).tryIt().getThrowable();
                assertThat(failure)
                    .isInstanceOf(IllegalArgumentException.class);
                assertThat(failure.getSuppressed().length)
                    .isEqualTo(1);
                assertThat(failure.getSuppressed()[0])
                    .isInstanceOf(IllegalStateException.class);
            }

        }

        @Nested
        @DisplayName("With Function")
        class With_Function {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void null_function_throws_NullPointerException() {
                LOGGER.info("Attempt.onFailureRecover(Function) should throw NullPointerException when recover is null");
                assertThatThrownBy(() -> attempt(5).onFailureRecover((Function<? super Throwable, Integer>) null))
                    .isInstanceOf(NullPointerException.class);
            }

            @Test
            void function_not_invoked_on_success() {
                LOGGER.info("Attempt.onFailureRecover(Function) should not invoke recover on success");
                final var fun = invocationCountingFun((Throwable e) -> 23);
                assertThat(attempt(5).map(Function.identity()).onFailureRecover(fun).tryIt())
                    .isEqualTo(success(5));
                assertThat(fun.getInvocationCount())
                    .isZero();
            }

            @Test
            void function_used_on_failure_and_produces_success() {
                LOGGER.info("Attempt.onFailureRecover(Function) should invoke recover on failure and produce success");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                final var fun = invocationCountingFun((Throwable e) -> 23);
                assertThat(attempt(5).map(throwing).onFailureRecover(fun).tryIt())
                    .isEqualTo(success(23));
                assertThat(fun.getInvocationCount())
                    .isEqualTo(1);
            }

            @Test
            void null_from_function_becomes_npe_failure() {
                LOGGER.info("Attempt.onFailureRecover(Function) should turn null from recover into NullPointerException failure");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                assertThat(attempt(5).map(throwing).onFailureRecover(_$ -> null).tryIt().getThrowable())
                    .isInstanceOf(NullPointerException.class);
            }

            @Test
            void function_throws_exception_and_preserves_original_as_suppressed() {
                LOGGER.info("Attempt.onFailureRecover(Function) should propagate recover exception and keep original as suppressed");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException("ORIGINAL"); };
                final var recover = (Function<Throwable, Integer>) e -> { throw new IllegalArgumentException("RECOVER"); };

                final var failure = attempt(5).map(throwing).onFailureRecover(recover).tryIt().getThrowable();
                assertThat(failure)
                    .isInstanceOf(IllegalArgumentException.class);
                assertThat(failure.getSuppressed().length)
                    .isEqualTo(1);
                assertThat(failure.getSuppressed()[0])
                    .isInstanceOf(IllegalStateException.class);
            }

        }

        @Nested
        @DisplayName("Throw")
        class Throwing {

            @Test
            void returns_self_on_success() {
                LOGGER.info("Attempt.onFailureThrow() should return self on success");
                assertThatNoException()
                    .isThrownBy(() -> attempt(5).map(Function.identity()).onFailureThrow().tryIt());
            }

            @Test
            void throws_original_exception_on_failure() {
                LOGGER.info("Attempt.onFailureThrow() should throw the original exception on failure");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                assertThatThrownBy(() -> attempt(5).map(throwing).onFailureThrow())
                    .isInstanceOf(IllegalStateException.class);
            }

        }

    }

    @Nested
    @DisplayName("Behaviour")
    class Behaviour {

        @Nested
        @DisplayName("Functional (map, applyTo, flatMap)")
        class Functional {

            @Nested
            @DisplayName("Map")
            class Map_ {

                @SuppressWarnings("DataFlowIssue")
                @Test
                void null_function_throws_NullPointerException() {
                    LOGGER.info("Attempt.map(...) should throw NullPointerException when transformation is null");
                    assertThatThrownBy(() -> attempt(5).map(null))
                        .isInstanceOf(NullPointerException.class);
                }

                @Test
                void null_result_from_function_becomes_failure_NPE() {
                    LOGGER.info("Attempt.map(...) should turn null result into NullPointerException failure");
                    assertThatThrownBy(() -> attempt(5).map(_$ -> null).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).map(_$ -> null).tryIt().getThrowable())
                        .isInstanceOf(NullPointerException.class);
                }

                @Test
                void mapping_works() {
                    LOGGER.info("Attempt.map(...) should map value when transformation succeeds");
                    assertThat(attempt(5).map(i -> i * 2).tryIt().getValue())
                        .isEqualTo(10);
                }

            }

            @Nested
            @DisplayName("Apply")
            class ApplyTo_ {

                @Test
                void applyTo_container_is_failure_hits_failure_branch() {
                    LOGGER.info("Attempt.applyTo(...) should hit failure branch when container is Failure");

                    // Container: Evaluation führt sofort zu Failure (IllegalStateException)
                    Attempt<Function<Integer, Integer>> failingContainer =
                        Attempt.attempt(0).map(_$ -> { throw new IllegalStateException("CONTAINER"); });

                    final var result = Attempt.attempt(5).applyTo(failingContainer).tryIt();

                    assertThat(result.isFailure()).isTrue();
                    assertThat(result.getThrowable())
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("CONTAINER");
                }

                @SuppressWarnings({"DataFlowIssue", "ThrowableNotThrown"})
                @Test
                void applyTo_various_paths() {
                    LOGGER.info("Attempt.applyTo(...) should handle nulls, thrown exceptions and success paths correctly");

                    // null HK-value
                    assertThatThrownBy(() -> attempt(5).applyTo(null))
                        .isInstanceOf(NullPointerException.class);

                    // function container returns null -> NPE
                    assertThatThrownBy(() -> attempt(5).applyTo(attempt(_$ -> null)).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).applyTo(attempt(_$ -> null)).tryIt().getThrowable())
                        .isInstanceOf(NullPointerException.class);

                    // identity -> exception access
                    assertThatThrownBy(() -> attempt(5).applyTo(attempt(Fun.identity())).tryIt().getThrowable())
                        .isInstanceOf(NoSuchElementException.class);

                    // function container throws
                    assertThatThrownBy(
                        () -> attempt(5).applyTo(attempt(_$ -> { throw new IllegalArgumentException(); })).tryIt().getValue()
                    ).isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).applyTo(attempt(_$ -> { throw new IllegalArgumentException(); })).tryIt().getThrowable())
                        .isInstanceOf(IllegalArgumentException.class);

                    // precedence cases
                    final var ex1 = attempt(5)
                        .map(_$ -> { throw new IllegalStateException(); })
                        .applyTo(attempt(_$ -> { throw new IllegalArgumentException(); }))
                        .tryIt().getThrowable();
                    assertThat(ex1).isInstanceOf(IllegalStateException.class);

                    final var ex2 = attempt(5)
                        .applyTo(attempt(_$ -> { throw new IllegalArgumentException(); }))
                        .map(_$ -> { throw new IllegalStateException(); })
                        .tryIt().getThrowable();
                    assertThat(ex2).isInstanceOf(IllegalArgumentException.class);

                    // happy path
                    assertThat(attempt(5).applyTo(attempt(i -> i * 2)).tryIt())
                        .isEqualTo(success(10));
                }

                @Test
                void applyTo_function_returns_null_results_in_npe_failure() {
                    LOGGER.info("Attempt.applyTo(...) should fail with NPE when lifted function returns null");
                    final var liftedNull = attempt((Function<Integer, Integer>) (_$ -> null));
                    assertThat(attempt(5).applyTo(liftedNull).tryIt().getThrowable())
                        .isInstanceOf(NullPointerException.class);
                }

            }

            @Nested
            @DisplayName("flatMap")
            class FlatMap_ {

                @SuppressWarnings("DataFlowIssue")
                @Test
                void null_function_throws_NullPointerException() {
                    LOGGER.info("Attempt.flatMap(...) should throw NullPointerException when transformation is null");
                    assertThatThrownBy(() -> attempt(5).flatMap(null))
                        .isInstanceOf(NullPointerException.class);
                }

                @Test
                void transformation_throws_exception_results_in_failure() {
                    LOGGER.info("Attempt.flatMap(...) should capture exceptions from transformation as failure");
                    final var failure = attempt(5).flatMap(_$ -> { throw new IllegalArgumentException("FLAT_MAP"); }).tryIt().getThrowable();
                    assertThat(failure).isInstanceOf(IllegalArgumentException.class);
                }

                @Test
                void null_monadic_result_causes_access_failure() {
                    LOGGER.info("Attempt.flatMap(...) should fail when transformation returns null monadic value");
                    assertThatThrownBy(() -> attempt(5).flatMap(_$ -> null).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                }

                @Test
                void flatMap_works() {
                    LOGGER.info("Attempt.flatMap(...) should flatMap and flatten the resulting Attempt");
                    assertThat(attempt(5).flatMap(i -> attempt(i * 2)).tryIt().getValue())
                        .isEqualTo(10);
                }

            }

            @Nested
            @DisplayName("Unsafe variants")
            class Unsafe {

                @SuppressWarnings("DataFlowIssue")
                @Test
                void mapUnsafe_null_applicable_throws_NullPointerException() {
                    LOGGER.info("Attempt.mapUnsafe(...) should throw NullPointerException when applicable is null");
                    assertThatThrownBy(() -> attempt(5).mapUnsafe(null))
                        .isInstanceOf(NullPointerException.class);
                }

                @Test
                void mapUnsafe_null_result_becomes_failure_NPE() {
                    LOGGER.info("Attempt.mapUnsafe(...) should turn null result into NullPointerException failure");
                    assertThatThrownBy(() -> attempt(5).mapUnsafe(_$ -> null).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).mapUnsafe(_$ -> null).tryIt().getThrowable())
                        .isInstanceOf(NullPointerException.class);
                }

                @SuppressWarnings({"DataFlowIssue", "ThrowableNotThrown"})
                @Test
                void applyToUnsafe_various_paths() {
                    LOGGER.info("Attempt.applyToUnsafe(...) should handle nulls, thrown exceptions and success paths correctly");

                    assertThatThrownBy(() -> attempt(5).applyToUnsafe(null))
                        .isInstanceOf(NullPointerException.class);

                    assertThatThrownBy(() -> attempt(5).applyToUnsafe(attempt(_$ -> null)).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).applyToUnsafe(attempt(_$ -> null)).tryIt().getThrowable())
                        .isInstanceOf(NullPointerException.class);

                    assertThatThrownBy(() -> attempt(5).applyToUnsafe(attempt(Fun.identity())).tryIt().getThrowable())
                        .isInstanceOf(NoSuchElementException.class);

                    assertThatThrownBy(
                        () -> attempt(5)
                                .applyToUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); }))
                                .tryIt().getValue()
                    ).isInstanceOf(NoSuchElementException.class);
                    assertThat(attempt(5).applyToUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); })).tryIt().getThrowable())
                        .isInstanceOf(IllegalArgumentException.class);

                    final var ex1 = attempt(5)
                        .map(_$ -> { throw new IllegalStateException(); })
                        .applyToUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); }))
                        .tryIt().getThrowable();
                    assertThat(ex1).isInstanceOf(IllegalStateException.class);

                    final var ex2 = attempt(5)
                        .applyToUnsafe(attempt(_$ -> { throw new IllegalArgumentException(); }))
                        .map(_$ -> { throw new IllegalStateException(); })
                        .tryIt().getThrowable();
                    assertThat(ex2).isInstanceOf(IllegalArgumentException.class);

                    assertThat(attempt(5).applyToUnsafe(attempt(i -> i * 2)).tryIt())
                        .isEqualTo(success(10));
                }

                @Test
                void applyToUnsafe_container_is_failure_hits_failure_branch() {
                    LOGGER.info("Attempt.applyToUnsafe(...) should hit failure branch when container is Failure");

                    // Container: Evaluation führt sofort zu Failure (IllegalStateException)
                    Attempt<Applicable<Integer, Integer>> failingContainer =
                        Attempt.attempt(0).map(_$ -> { throw new IllegalStateException("CONTAINER"); });

                    final var result = Attempt.attempt(5).applyToUnsafe(failingContainer).tryIt();

                    assertThat(result.isFailure()).isTrue();
                    assertThat(result.getThrowable())
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("CONTAINER");
                }

                @SuppressWarnings("DataFlowIssue")
                @Test
                void flatMapUnsafe_null_applicable_throws_NullPointerException() {
                    LOGGER.info("Attempt.flatMapUnsafe(...) should throw NullPointerException when applicable is null");
                    assertThatThrownBy(() -> attempt(5).flatMapUnsafe(null))
                        .isInstanceOf(NullPointerException.class);
                }

                @Test
                void flatMapUnsafe_null_monadic_result_causes_access_failure() {
                    LOGGER.info("Attempt.flatMapUnsafe(...) should fail when transformation returns null monadic value");
                    assertThatThrownBy(() -> attempt(5).flatMapUnsafe(_$ -> null).tryIt().getValue())
                        .isInstanceOf(NoSuchElementException.class);
                }

                @Test
                void flatMapUnsafe_works() {
                    LOGGER.info("Attempt.flatMapUnsafe(...) should flatMap and flatten the resulting Attempt");
                    assertThat(attempt(5).flatMapUnsafe(i -> attempt(i * 2)).tryIt().getValue())
                        .isEqualTo(10);
                }

            }

        }

        @Nested
        @DisplayName("Peek")
        class Peek {

            @Test
            void lazy_consumer_invoked_only_on_failure_during_evaluation() {
                LOGGER.info("Attempt.peekFailureLazy(...) should invoke consumer only when evaluation fails");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                final var consumer = invocationCountingConsumer((Throwable e) -> {});

                attempt(5).map(throwing).peekFailureLazy(consumer);
                assertThat(consumer.getInvocationCount()).isZero();

                assertThat(attempt(5).map(throwing).peekFailureLazy(consumer).tryIt())
                    .isInstanceOf(Result.Failure.class);
                assertThat(consumer.getInvocationCount()).isEqualTo(1);

                consumer.resetInvocationCount();

                assertThat(attempt(5).map(Fun.identity()).peekFailureLazy(consumer).tryIt())
                    .isInstanceOf(Result.Success.class);
                assertThat(consumer.getInvocationCount()).isZero();
            }

            @Test
            void eager_consumer_invoked_immediately_on_failure() {
                LOGGER.info("Attempt.peekFailureEager(...) should invoke consumer immediately when already in failure");
                final var throwing = (Function<Integer, Integer>) i -> { throw new IllegalStateException(); };
                final var consumer = invocationCountingConsumer((Throwable e) -> {});

                attempt(5).map(throwing).peekFailureEager(consumer);
                assertThat(consumer.getInvocationCount()).isEqualTo(1);

                consumer.resetInvocationCount();

                assertThat(attempt(5).map(Fun.identity()).peekFailureEager(consumer).tryIt())
                    .isInstanceOf(Result.Success.class);
                assertThat(consumer.getInvocationCount()).isZero();
            }

        }

        @Nested
        @DisplayName("Unwind")
        class Unwind_ {

            @Test
            void materializes_current_result_and_keeps_value() {
                LOGGER.info("Attempt.unwind() should materialize current result and preserve value");
                final InvocationCountingFun<Integer, Integer> invocationCountingFun
                    = invocationCountingFun(i -> i * 2);

                final var attempt
                    = attempt(5)
                        .map(invocationCountingFun)
                        .applyTo(attempt(invocationCountingFun))
                        .flatMap(i -> attempt(invocationCountingFun.apply(i)));

                assertThat(invocationCountingFun.getInvocationCount())
                    .isZero();

                final var unwound
                    = attempt.unwind();
                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(3);

                final var value
                    = unwound.unwind().tryIt().getValue();
                assertThat(value)
                    .isEqualTo(40);
                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(3);
            }

        }

    }

    @SuppressWarnings("NonAsciiCharacters")
    @Nested
    @DisplayName("Helpers")
    class Helpers {

        @Test
        void narrow_should_return_same_instance_and_not_throw() {
            LOGGER.info("Attempt.narrow(...) should return the same instance without throwing");
            final var att
                = attempt(42);
            final var widened
                = (Higher1<Attempt.µ, Integer>) att;
            final var narrowed
                = Attempt.narrow(widened);
            assertThat(narrowed).isSameAs(att);
        }

    }

}
