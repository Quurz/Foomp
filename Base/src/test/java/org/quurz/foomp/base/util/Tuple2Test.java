package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.slf4j.Logger;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Tuple2")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Tuple2Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Tuple2Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void tuple2_enforces_non_null_and_exposes_accessors() {
            LOGGER.info("Tuple2.tuple2 should enforce non-null and expose accessors");

            assertThatThrownBy(() -> tuple2(null, 5))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, null))
                .isInstanceOf(NullPointerException.class);

            final var t = tuple2(5, 6);

            assertThat(t.is1()).isTrue();
            assertThat(t.isPresent()).isTrue();
            assertThat(t.is2()).isTrue();

            assertThat(t.get()).isEqualTo(5);
            assertThat(t.get1()).isEqualTo(5);
            assertThat(t.get2()).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("Structure")
    class Structure {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with1_replaces_first_and_enforces_non_null() {
            LOGGER.info("Tuple2.with1 should replace first and enforce non-null");
            assertThatThrownBy(() -> tuple2(5, 5).with1(null))
                .isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> {
                final var t = tuple2(5, 6).with1(7);
                assertThat(t.get1()).isEqualTo(7);
                assertThat(t.get2()).isEqualTo(6);
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with2_replaces_second_and_enforces_non_null() {
            LOGGER.info("Tuple2.with2 should replace second and enforce non-null");
            assertThatThrownBy(() -> tuple2(5, 5).with2(null))
                .isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> {
                final var t = tuple2(5, 6).with2(7);
                assertThat(t.get2()).isEqualTo(7);
                assertThat(t.get1()).isEqualTo(5);
            });
        }

        @Test
        void swap_swaps_components_and_copy_preserves_values() {
            LOGGER.info("Tuple2.swap and Tuple2.copy should behave");

            final var t = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
            final var swapped = t.swap();
            final var copy = t.copy();

            assertThat(t.get1()).isEqualTo(SOME_STRING_VALUE);
            assertThat(t.get2()).isEqualTo(SOME_OTHER_STRING_VALUE);

            assertThat(swapped.get1()).isEqualTo(SOME_OTHER_STRING_VALUE);
            assertThat(swapped.get2()).isEqualTo(SOME_STRING_VALUE);

            assertThat(copy.get1()).isEqualTo(SOME_STRING_VALUE);
            assertThat(copy.get2()).isEqualTo(SOME_OTHER_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map_and_map1_transform_first_and_enforce_null_contracts() {
            LOGGER.info("Tuple2.map/map1 should transform first and fail on nulls");

            assertThatThrownBy(() -> tuple2(5, 6).map(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).map(_$ -> null).get1())
                .isInstanceOf(NullPointerException.class);

            final var mapped = tuple2(5, 6).map(i -> i * 2);
            assertThat(mapped.get1()).isEqualTo(10);

            assertThatThrownBy(() -> tuple2(5, 6).map1(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).map1(_$ -> null).get1())
                .isInstanceOf(NullPointerException.class);

            final var mapped1 = tuple2(5, 6).map1(i -> i * 2);
            assertThat(mapped1.get1()).isEqualTo(10);
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map2_transforms_second_and_enforces_null_contracts() {
            LOGGER.info("Tuple2.map2 should transform second and fail on nulls");

            assertThatThrownBy(() -> tuple2(5, 6).map2(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).map2(_$ -> null).get2())
                .isInstanceOf(NullPointerException.class);

            final var mapped = tuple2(5, 6).map2(i -> i * 2);
            assertThat(mapped.get2()).isEqualTo(12);

            assertThatThrownBy(() -> tuple2(5, 6).map2(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).map2(_$ -> null).get2())
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void mapTo1_and_mapTo2_transform_using_both_or_single_values_and_enforce_contracts() {
            LOGGER.info("Tuple2.mapTo1/mapTo2 should transform with contracts");

            assertThatThrownBy(() -> tuple2(5, 6).mapTo1((Fun2<? super Integer, ? super Integer, ?>) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).mapTo1((_i1, _i2) -> null).get1())
                .isInstanceOf(NullPointerException.class);

            final var mt1b = tuple2(5, 6).mapTo1(Integer::sum);
            assertThat(mt1b.get1()).isEqualTo(11);
            assertThat(mt1b.get2()).isEqualTo(6);

            assertThatThrownBy(() -> tuple2(5, 5).mapTo1((Fun<Integer, String>) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 5).mapTo1(i -> null).get())
                .isInstanceOf(NullPointerException.class);

            final var mt1u = tuple2(5, 6).mapTo1(String::valueOf);
            assertThat(mt1u.get1()).isEqualTo("6");
            assertThat(mt1u.get2()).isEqualTo(6);

            assertThatThrownBy(() -> tuple2(5, 6).mapTo2((Fun2<? super Integer, ? super Integer, ?>) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).mapTo2((_i1, _i2) -> null).get2())
                .isInstanceOf(NullPointerException.class);

            final var mt2b = tuple2(5, 6).mapTo2(Integer::sum);
            assertThat(mt2b.get2()).isEqualTo(11);

            assertThatThrownBy(() -> tuple2(5, 5).mapTo2((Fun<Integer, String>) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 5).mapTo2(i -> null).get2())
                .isInstanceOf(NullPointerException.class);

            final var mt2u = tuple2(5, 6).mapTo2(String::valueOf);
            assertThat(mt2u.get2()).isEqualTo("5");
            assertThat(mt2u.get1()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Applicative (lift)")
    class Applicative_Lift {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void lift_applies_functions_and_enforces_null_contracts() {
            LOGGER.info("Tuple2.lift should apply functions and enforce null contracts");

            final Fun<Object, Object> nullReturningFun = _$ -> null;
            final Fun<Object, String> toStringFun = Object::toString;

            assertThatThrownBy(() -> tuple2(5, 6).lift(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).lift(tuple2(nullReturningFun, nullReturningFun)).get1())
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple2(5, 6).lift(tuple2(nullReturningFun, nullReturningFun)).get2())
                .isInstanceOf(NullPointerException.class);

            final var lifted = tuple2(5, 6).lift(tuple2(toStringFun, i -> i + 5));
            assertThat(lifted.get1()).isEqualTo("5");
            assertThat(lifted.get2()).isEqualTo(11);
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Test
        void toRecord_converts_values() {
            LOGGER.info("Tuple2.toRecord should convert values");
            final var t = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
            final var r = t.toRecord();
            assertThat(r.get1()).isEqualTo(SOME_STRING_VALUE);
            assertThat(r.get2()).isEqualTo(SOME_OTHER_STRING_VALUE);
        }

        @Test
        void toPair_converts_values_and_is_independent_of_tuple() {
            LOGGER.info("Tuple2.toPair should evaluate values and return independent mutable container");

            final var t = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
            final var pair = t.toPair();

            // Values copied correctly
            assertThat(pair.get1()).isEqualTo(SOME_STRING_VALUE);
            assertThat(pair.get2()).isEqualTo(SOME_OTHER_STRING_VALUE);

            // Presence reflects non-null values
            assertThat(pair.is1()).isTrue();
            assertThat(pair.is2()).isTrue();

            // Equality is component-wise
            assertThat(pair).isEqualTo(MutablePair.mutablePair(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE));

            // Mutating the pair does not affect the original tuple
            pair.set1("mutated");
            assertThat(pair.get1()).isEqualTo("mutated");
            assertThat(t.get1()).isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void equals_hashCode_and_toString_behave() {
            LOGGER.info("Tuple2.equals/hashCode/toString should behave");

            final var t1 = tuple2(1, "test");
            final var t2 = tuple2(1, "test");
            final var t3 = tuple2(2, "test");

            assertThat(t1).isEqualTo(t1).isEqualTo(t2);
            assertThat(t1).isNotEqualTo(t3).isNotEqualTo(null).isNotEqualTo(SOME_STRING_VALUE);

            assertThat(t1).hasSameHashCodeAs(t1).hasSameHashCodeAs(t2).doesNotHaveSameHashCodeAs(t3);

            assertThat(tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE))
                .hasToString("Tuple2[value1=%s, value2=%s]", SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Unwind")
    class Unwind_ {

        @Test
        void unwind1_evaluates_first_only() {
            LOGGER.info("Tuple2.unwind1 should evaluate first only");

            final var t = tuple2(5, SOME_STRING_VALUE);

            final var invToString = invocationCountingFun(Objects::toString);
            final InvocationCountingFun<Integer, Integer> add5 = invocationCountingFun(i -> i + 5);

            final var spooled = t
                .map(add5)
                .map2(invToString)
                .mapAll(add5, invToString)
                .lift(tuple2(invToString, invToString));

            assertThat(invToString.getInvocationCount()).isZero();
            assertThat(add5.getInvocationCount()).isZero();

            spooled.unwind1();

            assertThat(add5.getInvocationCount()).isEqualTo(2);
            assertThat(invToString.getInvocationCount()).isOne();
        }

        @Test
        void unwind2_evaluates_second_only() {
            LOGGER.info("Tuple2.unwind2 should evaluate second only");

            final var t = tuple2(5, SOME_STRING_VALUE);

            final var invToString = invocationCountingFun(Objects::toString);
            final InvocationCountingFun<Integer, Integer> add5 = invocationCountingFun(i -> i + 5);

            final var spooled = t
                .map(add5)
                .map2(invToString)
                .mapAll(add5, invToString)
                .lift(tuple2(invToString, invToString));

            assertThat(invToString.getInvocationCount()).isZero();
            assertThat(add5.getInvocationCount()).isZero();

            spooled.unwind2();

            assertThat(invToString.getInvocationCount()).isEqualTo(3);
            assertThat(add5.getInvocationCount()).isZero();
        }

        @Test
        void unwind_evaluates_both_and_returns_realized_tuple() {
            LOGGER.info("Tuple2.unwind should evaluate both and return realized tuple");

            final var invToString = invocationCountingFun(Objects::toString);
            final InvocationCountingFun<Integer, Integer> add5 = invocationCountingFun(i -> i + 5);

            final var base = tuple2(5, SOME_STRING_VALUE);

            final var spooled = base
                .map(add5)
                .map2(invToString)
                .mapAll(add5, invToString)
                .lift(tuple2(invToString, invToString));

            assertThat(invToString.getInvocationCount()).isZero();
            assertThat(add5.getInvocationCount()).isZero();

            final var unwound = spooled.unwind();

            assertThat(invToString.getInvocationCount()).isEqualTo(4);
            assertThat(add5.getInvocationCount()).isEqualTo(2);

            assertThat(base.get1()).isEqualTo(5);
            assertThat(base.get2()).isEqualTo(SOME_STRING_VALUE);

            invToString.resetInvocationCount();
            add5.resetInvocationCount();

            assertThat(unwound.get1()).isEqualTo("15");
            assertThat(unwound.get2()).isEqualTo(SOME_STRING_VALUE);
        }
    }
}
