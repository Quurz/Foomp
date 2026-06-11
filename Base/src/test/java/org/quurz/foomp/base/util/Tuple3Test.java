package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun3;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record3.record3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
@DisplayName("Tuple3")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Tuple3Test
        extends TestHelper {

    private static final Logger LOGGER
            = getLogger(Tuple3Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void tuple3_enforces_non_null_and_exposes_accessors() {
            LOGGER.info("Tuple3.tuple3 should enforce non-null and expose accessors");

            final var t = tuple3(1, 2, 3);

            assertThatThrownBy(() -> tuple3(null, 2, 3)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple3(1, null, 3)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple3(1, 2, null)).isInstanceOf(NullPointerException.class);

            assertThat(t.isPresent()).isTrue();
            assertThat(t.is1()).isTrue();
            assertThat(t.is2()).isTrue();
            assertThat(t.is3()).isTrue();

            assertThat(t.get()).isEqualTo(t.get1()).isEqualTo(1);
            assertThat(t.get2()).isEqualTo(2);
            assertThat(t.get3()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Structure")
    class Structure {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with_replaces_components_and_enforces_non_null() {
            LOGGER.info("Tuple3.with* should replace component and enforce non-null");

            final var t = tuple3(1, 2, 3);

            assertThatThrownBy(() -> t.with1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.with2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.with3(null)).isInstanceOf(NullPointerException.class);

            assertThat(t.with1(4)).isEqualTo(tuple3(4, 2, 3));
            assertThat(t.with2(5)).isEqualTo(tuple3(1, 5, 3));
            assertThat(t.with3(6)).isEqualTo(tuple3(1, 2, 6));
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map1_transforms_first_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.map1 should transform first and fail on nulls");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.map1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.map1(_$ -> null).get1()).isInstanceOf(NullPointerException.class);
            assertThat(t.map1(s -> s + " World").get1()).isEqualTo("Hello World");
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map2_transforms_second_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.map2 should transform second and fail on nulls");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.map2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.map2(_$ -> null).get2()).isInstanceOf(NullPointerException.class);
            assertThat(t.map2(i -> i + 1).get2()).isEqualTo(18);
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map3_transforms_third_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.map3 should transform third and fail on nulls");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.map3(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.map3(_$ -> null).get3()).isInstanceOf(NullPointerException.class);
            assertThat(t.map3(d -> d + 1.0D).get3()).isEqualTo(3.0D);
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void mapAll_transforms_all_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.mapAll should transform all and fail on nulls");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.mapAll(Function.identity(), Fun.identity(), null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.mapAll(Function.identity(), null, Function.identity())).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.mapAll(null, Fun.identity(), Function.identity())).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> t.mapAll(_$ -> null, _$ -> null, _$ -> null).get1()).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.mapAll(_$ -> null, _$ -> null, _$ -> null).get2()).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.mapAll(_$ -> null, _$ -> null, _$ -> null).get3()).isInstanceOf(NullPointerException.class);

            assertThat(t.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get1()).isEqualTo("Hello World");
            assertThat(t.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get2()).isEqualTo(18);
            assertThat(t.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get3()).isEqualTo(3.0D);
        }
    }

    @Nested
    @DisplayName("Applicative (applyTo)")
    class Applicative_ApplyTo {

        @Test
        void applyTo_applies_functions_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.applyTo should apply functions and enforce null contracts");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.applyTo(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.applyTo(tuple3(_$ -> null, Fun.identity(), Fun.identity())).unwind())
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.applyTo(tuple3(Function.identity(), _$ -> null, Fun.identity())).unwind())
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.applyTo(tuple3(Function.identity(), Fun.identity(), _$ -> null)).unwind())
                .isInstanceOf(NullPointerException.class);

            assertThat(t.applyTo(tuple3(s -> s + " World", i -> i + 1, d -> d + 1.0D)).unwind())
                .isEqualTo(tuple3("Hello World", 18, 3.0D));
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @SuppressWarnings({"DataFlowIssue"})
        @Test
        void meld_combines_values_and_enforces_null_contracts() {
            LOGGER.info("Tuple3.meld should combine values and enforce null contracts");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThatThrownBy(() -> t.meld(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> t.meld((_1, _2, _3) -> null)).isInstanceOf(NullPointerException.class);

            final Fun3<String, Integer, Double, String> melding
                = (s, i, d) -> s + " " + i + " " + d;

            assertThat(t.meld(melding)).isEqualTo("Hello 17 2.0");
        }


        @Test
        void toRecord_converts_values() {
            LOGGER.info("Tuple3.toRecord should convert values");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThat(t.toRecord()).isEqualTo(record3("Hello", 17, 2.0D));
        }
    }

    @Nested
    @DisplayName("Unwind")
    class Unwind_ {

        @Test
        void unwind1_evaluates_first_only() {
            LOGGER.info("Test tuple3.unwind1");

            final var t = tuple3("Hello", 17, 2.0D);

            final var invocationCountingToString
                = invocationCountingFun(Objects::toString);
            final var invocationCountingIdentity
                = invocationCountingFun(Fun.identity());

            final var spooled
                = t
                    .map1(invocationCountingToString)
                    .map2(invocationCountingIdentity)
                    .map3(invocationCountingIdentity)
                    .mapAll(
                        invocationCountingToString,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                    .applyTo(
                        tuple3(
                            invocationCountingToString,
                            invocationCountingIdentity,
                            invocationCountingIdentity
                        )
                    );

            assertThat(invocationCountingToString.getInvocationCount())
                .isZero();
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();

            spooled.unwind1();

            assertThat(invocationCountingToString.getInvocationCount())
                .isEqualTo(3);
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();
        }

        @Test
        void unwind2_evaluates_second_only() {
            LOGGER.info("Test tuple3.unwind2");

            final var t = tuple3("Hello", 17, 2.0D);

            final var invocationCountingToString
                = invocationCountingFun(Objects::toString);
            final var invocationCountingIdentity
                = invocationCountingFun(Fun.identity());

            final var spooled
                = t
                    .map1(invocationCountingIdentity)
                    .map2(invocationCountingToString)
                    .map3(invocationCountingIdentity)
                    .mapAll(
                        invocationCountingIdentity,
                        invocationCountingToString,
                        invocationCountingIdentity
                    )
                    .applyTo(
                        tuple3(
                            invocationCountingIdentity,
                            invocationCountingToString,
                            invocationCountingIdentity
                        )
                    );

            assertThat(invocationCountingToString.getInvocationCount())
                .isZero();
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();

            spooled.unwind2();

            assertThat(invocationCountingToString.getInvocationCount())
                .isEqualTo(3);
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();
        }

        @Test
        void unwind3_evaluates_third_only() {
            LOGGER.info("Test tuple3.unwind3");

            final var t = tuple3("Hello", 17, 2.0D);

            final var invocationCountingToString
                = invocationCountingFun(Objects::toString);
            final var invocationCountingIdentity
                = invocationCountingFun(Fun.identity());

            final var spooled
                = t
                    .map1(invocationCountingIdentity)
                    .map2(invocationCountingIdentity)
                    .map3(invocationCountingToString)
                    .mapAll(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingToString
                    )
                    .applyTo(
                        tuple3(
                            invocationCountingIdentity,
                            invocationCountingIdentity,
                            invocationCountingToString
                        )
                    );

            assertThat(invocationCountingToString.getInvocationCount())
                .isZero();
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();

            spooled.unwind3();

            assertThat(invocationCountingToString.getInvocationCount())
                .isEqualTo(3);
            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();
        }

        @Test
        void unwind_evaluates_all() {
            LOGGER.info("Test tuple3.unwind");

            final var t = tuple3("Hello", 17, 2.0D);

            final var invocationCountingIdentity
                = invocationCountingFun(Fun.identity());

            final var spooled
                = t
                    .map1(invocationCountingIdentity)
                    .map2(invocationCountingIdentity)
                    .map3(invocationCountingIdentity)
                    .mapAll(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                    .applyTo(
                        tuple3(
                            invocationCountingIdentity,
                            invocationCountingIdentity,
                            invocationCountingIdentity
                        )
                    );

            assertThat(invocationCountingIdentity.getInvocationCount())
                .isZero();

            spooled.unwind();

            assertThat(invocationCountingIdentity.getInvocationCount())
                .isEqualTo(9);
        }
    }

    @Nested
    @DisplayName("Equality and formatting")
    class Equality_And_Formatting {

        @Test
        void equals_and_hashCode_compare_componentwise() {
            LOGGER.info("Test tuple3.equals and tuple3.hashCode");

            final var firstTuple3
                = tuple3("Hello", 17, 2.0D);
            final var secondTuple3
                = tuple3("Hello", 17, 2.0D);
            final var thirdTuple3
                = tuple3("World", 23, 3.0D);

            assertThat(firstTuple3)
                .isEqualTo(firstTuple3)
                .isEqualTo(secondTuple3)
                .isNotEqualTo(thirdTuple3);

            assertThat(firstTuple3)
                .hasSameHashCodeAs(firstTuple3)
                .hasSameHashCodeAs(secondTuple3)
                .doesNotHaveSameHashCodeAs(thirdTuple3);
        }

        @Test
        void toString_formats_values() {
            LOGGER.info("Test tuple3.toString");

            final var t = tuple3("Hello", 17, 2.0D);

            assertThat(t)
                .hasToString("Tuple3[value1=%s, value2=%s, value3=%s]", "Hello", 17, 2.0D);
        }
    }
}
