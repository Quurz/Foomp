package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun4;
import org.slf4j.Logger;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record4.record4;
import static org.quurz.foomp.base.util.Tuple4.tuple4;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Tuple4")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Tuple4Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Tuple4Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void tuple4_enforces_non_null_and_exposes_accessors() {
            LOGGER.info("Test Tuple4.tuple4");

            assertThatThrownBy(() -> tuple4(null, null, null, null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple4(null, null, null, 1))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple4(null, null, 1, null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple4(null, 1, null, null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tuple4(1, null, null, null))
                .isInstanceOf(NullPointerException.class);

            final var t = tuple4("Hello", 1, 2.0, true);

            assertThat(t.is1())
                .isEqualTo(t.isPresent())
                .isEqualTo(t.is2())
                .isEqualTo(t.is3())
                .isEqualTo(t.is4())
                .isTrue();

            assertThat(t.get1()).isEqualTo(t.get()).isEqualTo("Hello");
            assertThat(t.get2()).isEqualTo(1);
            assertThat(t.get3()).isEqualTo(2.0);
            assertThat(t.get4()).isTrue();
        }
    }

    @Nested
    @DisplayName("Structure")
    class Structure {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with_replaces_components_and_enforces_non_null() {
        LOGGER.info("Test tuple4.with1, tuple4.with2, tuple4.with3 and tuple4.with4");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with4(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.with1("World"))
            .isEqualTo(tuple4("World", 1, 2.0, true));
        assertThat(tuple4.with2(2))
            .isEqualTo(tuple4("Hello", 2, 2.0, true));
        assertThat(tuple4.with3(3.0))
            .isEqualTo(tuple4("Hello", 1, 3.0, true));
        assertThat(tuple4.with4(false))
            .isEqualTo(tuple4("Hello", 1, 2.0, false));
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map_transforms_each_and_enforces_null_contracts() {
        LOGGER.info("Test tuple4.map1, tuple4.map2, tuple4.map3, tuple4.map4 and tuple4.mapAll");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map4(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, null, Fun.identity()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, Fun.identity(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, Fun.identity(), null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), null, null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> tuple4.map1(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map2(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map3(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map4(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(_$ -> null, _$ -> null, _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), _$ -> null, _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), Fun.identity(), _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), Fun.identity(), Fun.identity(), _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.map1(s -> s + " World").unwind())
            .isEqualTo(tuple4("Hello World", 1, 2.0, true));
        assertThat(tuple4.map2(i -> i + 1).unwind())
            .isEqualTo(tuple4("Hello", 2, 2.0, true));
        assertThat(tuple4.map3(d -> d + 1.0).unwind())
            .isEqualTo(tuple4("Hello", 1, 3.0, true));
        assertThat(tuple4.map4(b -> !b).unwind())
            .isEqualTo(tuple4("Hello", 1, 2.0, false));
        assertThat(tuple4.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0, b -> !b).unwind())
            .isEqualTo(tuple4("Hello World", 2, 3.0, false));
        }
    }

    @Nested
    @DisplayName("Applicative (applyTo)")
    class Applicative_ApplyTo {

        @Test
        void applyTo_applies_functions_and_enforces_null_contracts() {
        LOGGER.info("Test tuple4.applyTo");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.applyTo(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.applyTo(tuple4(_$ -> null, Fun.identity(), Fun.identity(), Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.applyTo(tuple4(Fun.identity(), _$ -> null, Fun.identity(), Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.applyTo(tuple4(Fun.identity(), Fun.identity(), _$ -> null, Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.applyTo(tuple4(Fun.identity(), Fun.identity(), Fun.identity(), _$ -> null)).unwind())
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.applyTo(tuple4(s -> s + " World", i -> i + 1, d -> d + 1.0d, b -> !b)).unwind())
            .isEqualTo(tuple4("Hello World", 2, 3.0, false));
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void meld_combines_values_and_enforces_null_contracts() {
        LOGGER.info("Test tuple4.meld");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.meld(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.meld((_1, _2, _3, _4) -> null))
            .isInstanceOf(NullPointerException.class);

        final Fun4<String, Integer, Double, Boolean, String> melding
            = (s, i, d, b) -> s + " " + i + " " + d + " " + b;
 
        assertThat(tuple4.meld(melding))
            .isEqualTo("Hello 1 2.0 true");
        }
 
 
        @Test
        void toRecord_converts_values() {
        LOGGER.info("Test tuple4.record");
 
        final var tuple4
            = tuple4("Hello", 1, 2.0, true);
 
        assertThat(tuple4.toRecord())
            .isEqualTo(record4("Hello", 1, 2.0, true));
        }
    }
 
    @Nested
    @DisplayName("Unwind")
    class Unwind_ {
 
        @Test
        void unwind1_evaluates_first_only() {
        LOGGER.info("Test tuple4.unwind1");
 
        final var tuple4
            = tuple4("Hello", 1, 2.0, true);
 
        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());
 
        final var spooled
            = tuple4
                .map1(invocationCountingToString)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingToString,
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .applyTo(
                    tuple4(
                        invocationCountingToString,
                        invocationCountingIdentity,
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
        LOGGER.info("Test tuple4.unwind2");
 
        final var tuple4
            = tuple4("Hello", 1, 2.0, true);
 
        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());
 
        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingToString)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingToString,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .applyTo(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingToString,
                        invocationCountingIdentity,
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
        LOGGER.info("Test tuple4.unwind3");
 
        final var tuple4
            = tuple4("Hello", 1, 2.0, true);
 
        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());
 
        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingToString)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingToString,
                    invocationCountingIdentity
                )
                .applyTo(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingToString,
                        invocationCountingIdentity
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
        LOGGER.info("Test tuple4.unwind");
 
        final var tuple4
            = tuple4("Hello", 1, 2.0, true);
 
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());
 
        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .applyTo(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                );
 
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();
 
        spooled.unwind();
 
        assertThat(invocationCountingIdentity.getInvocationCount())
                .isEqualTo(12);
        }
    }

    @Nested
    @DisplayName("Equality and formatting")
    class Equality_And_Formatting {

        @Test
        void equals_and_hashCode_compare_componentwise() {
        LOGGER.info("Test tuple4.equals and tuple4.hashCode");

        final var firstTuple4
            = tuple4("Hello", 1, 2.0, true);
        final var secondTuple4
            = tuple4("Hello", 1, 2.0, true);
        final var thirdTuple4
            = tuple4("World", 2, 3.0, false);

        assertThat(firstTuple4)
            .isEqualTo(firstTuple4)
            .isEqualTo(secondTuple4)
            .isNotEqualTo(thirdTuple4);

        assertThat(firstTuple4)
            .hasSameHashCodeAs(firstTuple4)
            .hasSameHashCodeAs(secondTuple4)
            .doesNotHaveSameHashCodeAs(thirdTuple4);
        }

        @Test
        void toString_formats_values() {
        LOGGER.info("Test tuple4.toString");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThat(tuple4)
            .hasToString("Tuple4[value1=%s, value2=%s, value3=%s, value4=%s]", "Hello", 1, 2.0, true);
        }
    }

}
