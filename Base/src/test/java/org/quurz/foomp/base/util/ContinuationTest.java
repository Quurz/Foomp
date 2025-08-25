package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Continuation.continuation;
import static org.quurz.foomp.base.util.Continuation.pureContinuation;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Continuation")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ContinuationTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(ContinuationTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void continuation_null_runner_throws_npe_and_non_null_is_ok() {
            LOGGER.info("Continuation.continuation: null runCont should throw, non-null should pass");

            assertThatThrownBy(() -> continuation(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("runCont");

            assertThatNoException()
                .isThrownBy(() -> continuation(a -> a));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void pureContinuation_null_value_throws_npe_and_non_null_is_ok() {
            LOGGER.info("Continuation.pureContinuation: null value should throw, non-null should pass");

            assertThatThrownBy(() -> pureContinuation(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("value");

            assertThatNoException()
                .isThrownBy(() -> pureContinuation(42));
        }
    }

    @Nested
    @DisplayName("Behaviour")
    class Behaviour {

        @Nested
        @DisplayName("map")
        class Map_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void map_transforms_value_and_enforces_null_contracts() {
                LOGGER.info("Continuation.map should transform value and fail on nulls");

                final Function<Function<Integer, Integer>, Integer> runCont1 = f -> f.apply(21);
                final Continuation<Integer, Integer> c1 = continuation(runCont1);
                final Function<Integer, Integer> fMap1 = i -> i * 2;

                final Function<Function<Integer, String>, String> runCont2 = f -> f.apply(21);
                final Continuation<Integer, String> c2 = continuation(runCont2);
                final Function<Integer, String> fMap2 = i -> Integer.toString(i * 2);

                assertThatThrownBy(() -> c1.map(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");
                assertThatThrownBy(() -> c1.map(_$ -> null).apply(_$ -> 17))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");

                assertThat(c1.map(fMap1).apply(Fun.identity()))
                    .isEqualTo(42);

                assertThat(c2.map(fMap2)
                             .map(answer -> "The answer to life, the universe, and everything: " + answer)
                             .apply(Fun.identity()))
                    .isEqualTo("The answer to life, the universe, and everything: 42");
            }
        }

        @Nested
        @DisplayName("lift (applicative)")
        class Lift_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void lift_applies_function_continuation_and_enforces_null_contracts() {
                LOGGER.info("Continuation.lift should apply function-in-continuation to current value");

                final Function<Function<Integer, Integer>, Integer> runCont = f -> f.apply(21);
                final Continuation<Integer, Integer> c = continuation(runCont);
                final Continuation<Function<Integer, String>, Integer> tf =
                    continuation(cont -> cont.apply(i -> Integer.toString(i * 2)));

                assertThatThrownBy(() -> c.lift(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");

                assertThat(c.lift(tf).apply(Integer::valueOf))
                    .isEqualTo(42);
            }
        }

        @Nested
        @DisplayName("bind (flatMap)")
        class Bind_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void bind_sequences_and_enforces_null_contracts() {
                LOGGER.info("Continuation.bind should sequence computations (flatMap) and enforce null contracts");

                final Function<Function<Integer, Integer>, Integer> runCont = f -> f.apply(21);
                final Continuation<Integer, Integer> c = continuation(runCont);

                assertThatThrownBy(() -> c.bind(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformation");

                assertThat(c.bind(i -> pureContinuation(i * 2)).apply(Fun.identity()))
                    .isEqualTo(42);
            }
        }

        @Nested
        @DisplayName("apply (run)")
        class Apply_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void apply_runs_continuation_and_checks_nulls() {
                LOGGER.info("Continuation.apply should run the CPS runner with final continuation");

                final Function<Function<Integer, Integer>, Integer> runCont = f -> f.apply(21);
                final Continuation<Integer, Integer> c = continuation(runCont);

                assertThatThrownBy(() -> c.apply(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("computation");

                assertThat(c.apply(Fun.identity()))
                    .isEqualTo(21);
                assertThat(c.apply(i -> i * 2))
                    .isEqualTo(42);
            }
        }
    }

    @Nested
    @DisplayName("Sequencing helpers")
    class Sequencing {

        @Test
        void then_ignores_current_value_and_runs_next() {
            LOGGER.info("Continuation.then should ignore current value and run the provided continuation");

            final Function<Function<Integer, Integer>, Integer> runCont = f -> f.apply(21);
            final Continuation<Integer, Integer> c = continuation(runCont);
            final InvocationCountingFun<Integer, Integer> f = invocationCountingFun(i -> i * 2);

            //noinspection DataFlowIssue
            assertThatThrownBy(() -> c.then(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("continuation");

            final var mapped = c.map(f);

            assertThat(f.getInvocationCount())
                .isZero();
            assertThat(mapped.then(pureContinuation(23)).apply(Fun.identity()))
                .isEqualTo(23);
            assertThat(f.getInvocationCount())
                .isOne();
        }
    }

    @Nested
    @DisplayName("Control flow (call/cc)")
    class ControlFlow {

        @Test
        void callCurrentCont_supports_early_exit() {
            LOGGER.info("Continuation.callCurrentCont should allow early exit (escape continuation)");

            final var seen = new ArrayList<Integer>();

            final Continuation<Integer, Integer> c = pureContinuation(5);
            c.bind(i -> {
                 seen.add(i);
                 return Continuation.<Integer, Integer, Integer>callCurrentCont(k ->
                         k.apply(42).then(pureContinuation(23))
                     );
                }).apply(f -> {
                    seen.add(f);
                    return 0;
                });

                assertThat(seen.toArray(Integer[]::new))
                    .containsExactly(5, 42);
            }
        }
    }
