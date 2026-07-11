package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record2.record2;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Record2")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Record2Test {

    private static final Logger LOGGER = getLogger(Record2Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void record2_and_constructor_enforce_non_null() {
            LOGGER.info("Record2.record2 and Record2::<init> NPE contracts");

            assertThatThrownBy(() -> record2(null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record2(null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record2(23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> record2(23, 23));

            assertThatThrownBy(() -> new Record2<>(null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record2<>(null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record2<>(23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> new Record2<>(23, 23));
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void presence_and_getters() {
            LOGGER.info("Record2.isPresent/is1/is2 + get/get1/get2/value accessors");

            final var r = record2("TEST", 23);

            assertThat(r.isPresent()).isTrue();
            assertThat(r.isPresent1()).isTrue();
            assertThat(r.isPresent2()).isTrue();

            assertThat(r.get()).isEqualTo("TEST");
            assertThat(r.get1()).isEqualTo("TEST");
            assertThat(r.get2()).isEqualTo(23);
            assertThat(r.value1()).isEqualTo("TEST");
            assertThat(r.value2()).isEqualTo(23);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with_updates_values_and_enforces_non_null() {
            LOGGER.info("Record2.with1/with2 null contracts and updates");

            final var r = record2("TEST", 23);

            assertThatThrownBy(() -> r.with1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with2(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.with1("TEST2").get1()).isEqualTo("TEST2");
            assertThat(r.with2(24).get2()).isEqualTo(24);
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void map1_and_map_enforce_contracts_and_transform_first() {
            LOGGER.info("Record2.map/map1 contracts and transformation of first component");

            final var r = record2("TEST", 23);

            assertThatThrownBy(() -> r.map1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.map1(String::length).get1()).isEqualTo(4);
            assertThat(r.map(String::length).get1()).isEqualTo(4);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void map2_enforces_contracts_and_transforms_second() {
            LOGGER.info("Record2.map2 contracts and transformation of second component");

            final var r = record2("TEST", 23);

            assertThatThrownBy(() -> r.map2(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.map2(i -> i + 1).get2()).isEqualTo(24);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mapAll_enforces_contracts_and_transforms_both() {
            LOGGER.info("Record2.mapAll contracts and transformation of both components");

            final var r = record2("TEST", 23);

            assertThatThrownBy(() -> r.mapAll(null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, i -> i + 1)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> r.mapAll(String::length, i -> i + 1));

            assertThat(r.mapAll(String::length, i -> i + 1)).isEqualTo(record2(4, 24));
        }
    }

    @Nested
    @DisplayName("Applicative (applyTo)")
    class Applicative_Apply {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void apply_enforces_contracts_and_applies_functions() {
            LOGGER.info("Record2.applyTo null contracts and behaviour");

            final var r = record2("TEST", 23);
            final Record2<Function<String, Integer>, Function<Integer, Integer>> nullReturning1liftA
                = record2(s -> null, i -> i + 1);
            final Record2<Function<String, Integer>, Function<Integer, Integer>> nullReturning2liftA
                = record2(String::length, i -> null);
            final Record2<Function<String, Integer>, Function<Integer, Integer>> okLiftA
                = record2(String::length, i -> i + 1);

            assertThatThrownBy(() -> r.applyTo(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.applyTo(nullReturning1liftA)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.applyTo(nullReturning2liftA)).isInstanceOf(NullPointerException.class);

            assertThat(r.applyTo(okLiftA)).isEqualTo(record2(4, 24));
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Test
        void toTuple_converts_values() {
            LOGGER.info("Record2.toTuple conversion");
            final var r = record2("TEST", 23);
            assertThat(r.toTuple()).isEqualTo(tuple2("TEST", 23));
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void transmogrify_contracts_and_value() {
            LOGGER.info("Record2.transmogrify contracts and result");

            final var r = record2("TEST", 23);

            assertThatThrownBy(() -> r.transmogrify(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.transmogrify(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var result = r.transmogrify(x -> x.get1() + x.get2());
                assertThat(result).isEqualTo("TEST23");
            });
        }


        @SuppressWarnings({"ConstantValue", "EqualsWithItself"})
        @Test
        void equals_hashCode_and_toString() {
            LOGGER.info("Record2.equals/hashCode structural and toString formatting");

            final var r = record2("TEST", 23);
            final var rCopy = record2("TEST", 23);
            final var rDiff1 = record2("TEST2", 23);
            final var rDiff2 = record2("TEST", 24);
            final var rDiff3 = record2("TEST2", 24);

            // hashCode
            assertThat(r).hasSameHashCodeAs(rCopy);
            assertThat(r.hashCode()).isNotEqualTo(rDiff1.hashCode());
            assertThat(r.hashCode()).isNotEqualTo(rDiff2.hashCode());
            assertThat(r.hashCode()).isNotEqualTo(rDiff3.hashCode());

            // equals
            assertThat(r.equals(r)).isTrue();
            assertThat(r.equals(rCopy)).isTrue();
            assertThat(r.equals(rDiff1)).isFalse();
            assertThat(r.equals(rDiff2)).isFalse();
            assertThat(r.equals(rDiff3)).isFalse();
            assertThat(r.equals(null)).isFalse();
            assertThat(r.equals(new Object())).isFalse();

            // toString
            assertThat(r).hasToString("Record2[value1=TEST, value2=23]");
        }
    }
}
