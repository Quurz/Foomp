package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Value;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Eval.evalAlways;
import static org.quurz.foomp.base.util.Eval.evalLater;
import static org.quurz.foomp.base.util.Eval.evalNow;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Eval")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("unused")
class EvalTest
        extends TestHelper {

    private static final Logger LOGGER = getLogger(EvalTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void now_null_throws_and_value_is_eager() {
            LOGGER.info("Eval.evalNow(...) should throw NullPointerException when value is null; eager value should be accessible");
            assertThatThrownBy(() -> evalNow(null))
                .isInstanceOf(NullPointerException.class);
            assertThat(evalNow(SOME_STRING_VALUE).isPresent()).isTrue();
            assertThat(evalNow(SOME_STRING_VALUE).get()).isEqualTo(SOME_STRING_VALUE);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void later_null_throws_and_value_is_lazy_memoized() {
            LOGGER.info("Eval.evalLater(...) should throw NullPointerException when value is null; lazy memoized value should be accessible");
            assertThatThrownBy(() -> evalLater(null))
                .isInstanceOf(NullPointerException.class);
            assertThat(evalLater(SOME_STRING_VALUE).isPresent()).isTrue();
            assertThat(evalLater(SOME_STRING_VALUE).get()).isEqualTo(SOME_STRING_VALUE);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void always_null_throws_and_value_is_lazy_non_memoized() {
            LOGGER.info("Eval.evalAlways(...) should throw NullPointerException when value is null; lazy non-memoized value should be accessible");
            assertThatThrownBy(() -> evalAlways(null))
                .isInstanceOf(NullPointerException.class);
            assertThat(evalAlways(SOME_STRING_VALUE).isPresent()).isTrue();
            assertThat(evalAlways(SOME_STRING_VALUE).get()).isEqualTo(SOME_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Behaviour (map)")
    class Behaviour_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void now_map_is_eager_and_stable() {
            LOGGER.info("Eval.Now.map should be eager and stable (transformation applied once)");
            final var eval = evalNow(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.map(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.map(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var mapped = eval.map(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
                mapped.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void later_map_is_lazy_memoized() {
            LOGGER.info("Eval.Later.map should be lazy and memoized (transformation deferred and applied once)");
            final var eval = evalLater(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.map(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.map(_$ -> null).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var mapped = eval.map(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                mapped.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void always_map_is_lazy_non_memoized() {
            LOGGER.info("Eval.Always.map should be lazy and non-memoized (transformation applied on every access)");
            final var eval = evalAlways(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.map(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.map(_$ -> null).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var mapped = eval.map(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                mapped.get();
                assertThat(fun.getInvocationCount()).isEqualTo(2);
                assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @Nested
    @DisplayName("Behaviour (applyTo)")
    class Behaviour_ApplyTo {

        @Test
        void now_applyTo_is_eager_and_stable() {
            LOGGER.info("Eval.Now.applyTo should be eager and stable (function applied once)");
            final var eval = evalNow(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.applyTo(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.applyTo(evalAlways(_$ -> null)).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var lifted = eval.applyTo(evalNow(fun));
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
                lifted.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void later_applyTo_is_lazy_memoized() {
            LOGGER.info("Eval.Later.applyTo should be lazy and memoized (function deferred and applied once)");
            final var eval = evalLater(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.applyTo(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.applyTo(evalAlways(_$ -> null)).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var lifted = eval.applyTo(evalLater(fun));
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
                lifted.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void always_applyTo_is_lazy_non_memoized() {
            LOGGER.info("Eval.Always.applyTo should be lazy and non-memoized (function applied on every access)");
            final var eval = evalAlways(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());

            assertThatThrownBy(() -> eval.applyTo(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.applyTo(evalAlways(_$ -> null)).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var lifted = eval.applyTo(evalAlways(fun));
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                lifted.get();
                assertThat(fun.getInvocationCount()).isEqualTo(2);
                assertThat(lifted.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Behaviour (flatMap)")
    class Behaviour_FlatMap {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void now_flatMap_is_eager_and_stable() {
            LOGGER.info("Eval.Now.flatMap should be eager and stable (flatMap applied once)");
            final var eval = evalNow(SOME_STRING_VALUE);
            final InvocationCountingFun<String, Eval<Integer>> fun = invocationCountingFun(s -> evalNow(s.length()));

            assertThatThrownBy(() -> eval.flatMap(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.flatMap(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var bound = eval.flatMap(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
                bound.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void later_flatMap_is_lazy_memoized() {
            LOGGER.info("Eval.Later.flatMap should be lazy and memoized (flatMap deferred and applied once)");
            final var eval = evalLater(SOME_STRING_VALUE);
            final InvocationCountingFun<String, Eval<Integer>> fun = invocationCountingFun(s -> evalLater(s.length()));

            assertThatThrownBy(() -> eval.flatMap(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.flatMap(_$ -> null).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var bound = eval.flatMap(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                bound.get();
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void always_flatMap_is_lazy_non_memoized() {
            LOGGER.info("Eval.Always.flatMap should be lazy and non-memoized (flatMap applied on every access)");
            final var eval = evalAlways(SOME_STRING_VALUE);
            final InvocationCountingFun<String, Eval<Integer>> fun = invocationCountingFun(s -> evalAlways(s.length()));

            assertThatThrownBy(() -> eval.flatMap(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.flatMap(_$ -> null).get()).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var bound = eval.flatMap(fun);
                assertThat(fun.getInvocationCount()).isEqualTo(0);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
                assertThat(fun.getInvocationCount()).isEqualTo(1);
                bound.get();
                assertThat(fun.getInvocationCount()).isEqualTo(2);
                assertThat(bound.get()).isEqualTo(SOME_STRING_VALUE.length());
            });
        }
    }

    @Nested
    @DisplayName("flatten (join)")
    class Flatten_ {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void flatten_contracts_and_behaviour() {
            LOGGER.info("Eval.flatten should enforce null contracts and flatten nested Eval");
            assertThatThrownBy(() -> Eval.flatten(null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                // Now(Now(v)) -> Now(v) (value accessible)
                final var nn = evalNow(evalNow(SOME_STRING_VALUE));
                assertThat(Eval.flatten(nn).get()).isEqualTo(SOME_STRING_VALUE);

                // Later(Later(v)) -> Later(v) (value accessible)
                final var ll = evalLater(evalLater(SOME_STRING_VALUE));
                assertThat(Eval.flatten(ll).get()).isEqualTo(SOME_STRING_VALUE);

                // Always(Always(v)) -> Always(v) (value accessible)
                final var aa = evalAlways(evalAlways(SOME_STRING_VALUE));
                assertThat(Eval.flatten(aa).get()).isEqualTo(SOME_STRING_VALUE);

                // Mixed: Now(Later(v)) -> Later(v) and Later(Now(v)) -> Now(v)
                final var nl = evalNow(evalLater(SOME_STRING_VALUE));
                assertThat(Eval.flatten(nl).get()).isEqualTo(SOME_STRING_VALUE);

                final var ln = evalLater(evalNow(SOME_STRING_VALUE));
                assertThat(Eval.flatten(ln).get()).isEqualTo(SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Unwind")
    class Unwind_ {

        @Test
        void now_unwind_materializes_but_remains_stable() {
            LOGGER.info("Eval.Now.unwind should materialize and remain stable (no further transformations executed)");
            final var eval = evalNow(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());
            final var mapped = eval.map(fun);

            assertThat(fun.getInvocationCount()).isEqualTo(1);
            final var unwound = mapped.unwind();
            assertThat(unwound.get()).isEqualTo(SOME_STRING_VALUE);
            assertThat(fun.getInvocationCount()).isEqualTo(1);
            mapped.get();
            assertThat(fun.getInvocationCount()).isEqualTo(1);
            unwound.get();
            assertThat(fun.getInvocationCount()).isEqualTo(1);
        }

        @Test
        void later_unwind_materializes_and_is_stable() {
            LOGGER.info("Eval.Later.unwind should materialize (once) and remain stable afterwards");
            final var eval = evalLater(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());
            final var mapped = eval.map(fun);

            assertThat(fun.getInvocationCount()).isEqualTo(0);
            final var unwound = mapped.unwind();
            assertThat(unwound.get()).isEqualTo(SOME_STRING_VALUE);
            assertThat(fun.getInvocationCount()).isEqualTo(1);
            unwound.get();
            assertThat(fun.getInvocationCount()).isEqualTo(1);
        }

        @Test
        void always_unwind_preserves_non_memoized_semantics() {
            LOGGER.info("Eval.Always.unwind should preserve non-memoized semantics (re-evaluates on subsequent accesses)");
            final var eval = evalAlways(SOME_STRING_VALUE);
            final InvocationCountingFun<String, String> fun = invocationCountingFun(Fun.identity());
            final var mapped = eval.map(fun);

            assertThat(fun.getInvocationCount()).isEqualTo(0);
            final var unwound = mapped.unwind();
            assertThat(unwound.get()).isEqualTo(SOME_STRING_VALUE);
            assertThat(fun.getInvocationCount()).isEqualTo(1);
            mapped.unwind().get();
            assertThat(fun.getInvocationCount()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Misc")
    class Misc {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void transmogrify_contracts_and_valid_usage() {
            LOGGER.info("Eval.transmogrify should enforce NullPointerException contracts and support valid usage");
            final var eval = evalNow(SOME_STRING_VALUE);

            assertThatThrownBy(() -> eval.transmogrify(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> eval.transmogrify(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> eval.transmogrify(Value::get));
        }
    }
}
