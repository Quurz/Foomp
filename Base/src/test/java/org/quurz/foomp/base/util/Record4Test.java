package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record4.record4;
import static org.quurz.foomp.base.util.Tuple4.tuple4;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Record4")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Record4Test {

    private static final Logger LOGGER = getLogger(Record4Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void record4_and_constructor_enforce_non_null() {
            LOGGER.info("Record4.record4 and Record4::<init> should enforce non-null contracts");

            assertThatThrownBy(() -> record4(null, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, 23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, 23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, 23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(null, 23, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, 23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, 23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record4(23, 23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> record4(23, 23, 23, 23));

            assertThatThrownBy(() -> new Record4<>(null, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, 23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, 23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, 23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(null, 23, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, 23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, 23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record4<>(23, 23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> new Record4<>(23, 23, 23, 23));
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void presence_and_getters() {
            LOGGER.info("Record4.isPresent/is1/is2/is3/is4 should report presence");

            final var r = record4("TEST", 23, "TEST", 23);

            assertThat(r.isPresent()).isTrue();
            assertThat(r.is1()).isTrue();
            assertThat(r.is2()).isTrue();
            assertThat(r.is3()).isTrue();
            assertThat(r.is4()).isTrue();
        }

        @Test
        void get_and_values_return_components() {
            LOGGER.info("Record4.get/get1/get2/get3/get4 should return components; value* should match");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThat(r.get()).isEqualTo("TEST");
            assertThat(r.get1()).isEqualTo("TEST");
            assertThat(r.get2()).isEqualTo(23);
            assertThat(r.get3()).isEqualTo(3.0d);
            assertThat(r.get4()).isEqualTo(2.0f);

            assertThat(r.value1()).isEqualTo("TEST");
            assertThat(r.value2()).isEqualTo(23);
            assertThat(r.value3()).isEqualTo(3.0d);
            assertThat(r.value4()).isEqualTo(2.0f);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with_updates_values_and_enforces_non_null() {
            LOGGER.info("Record4.with* should enforce non-null and update the respective component");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThatThrownBy(() -> r.with1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with3(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with4(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.with1("TEST2")).isEqualTo(record4("TEST2", 23, 3.0d, 2.0f));
            assertThat(r.with2(24)).isEqualTo(record4("TEST", 24, 3.0d, 2.0f));
            assertThat(r.with3(4.0d)).isEqualTo(record4("TEST", 23, 4.0d, 2.0f));
            assertThat(r.with4(3.0f)).isEqualTo(record4("TEST", 23, 3.0d, 3.0f));
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map_methods_enforce_contracts_and_transform_components() {
            LOGGER.info("Record4.map* should enforce non-null and transform the respective component");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThatThrownBy(() -> r.map1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map3(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map4(null)).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> r.map1(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map2(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map3(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map4(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThat(r.map1(String::length)).isEqualTo(record4(4, 23, 3.0d, 2.0f));
            assertThat(r.map2(i -> i + 1)).isEqualTo(record4("TEST", 24, 3.0d, 2.0f));
            assertThat(r.map3(d -> d + 1.0d)).isEqualTo(record4("TEST", 23, 4.0d, 2.0f));
            assertThat(r.map4(f -> f + 1.0f)).isEqualTo(record4("TEST", 23, 3.0d, 3.0f));
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void mapAll_enforces_contracts_and_transforms_all() {
            LOGGER.info("Record4.mapAll should enforce non-null and transform all components");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThatThrownBy(() -> r.mapAll(null, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, i -> i + 1, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, null, d -> d + 1.0d, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, null, null, f -> f + 1.0f)).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> r.mapAll(String::length, i -> i + 1, d -> d + 1.0d, _$ -> null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, i -> i + 1, _$ -> null, f -> f + 1.0f))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, _$ -> null, d -> d + 1.0d, f -> f + 1.0f))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(_$ -> null, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f))
                .isInstanceOf(NullPointerException.class);

            assertThat(r.mapAll(String::length, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f))
                .isEqualTo(record4(4, 24, 4.0d, 3.0f));
        }
    }

    @Nested
    @DisplayName("Applicative (lift)")
    class Applicative_Lift {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void lift_enforces_contracts_and_applies_functions() {
            LOGGER.info("Record4.lift should enforce non-null and apply functions to components");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThatThrownBy(() -> r.lift(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.lift(record4(String::length, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f)))
                .isEqualTo(record4(4, 24, 4.0d, 3.0f));
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Test
        void toTuple_converts_values() {
            LOGGER.info("Record4.toTuple should convert to Tuple4 with same values");

            final var r = record4("TEST", 23, 3.0d, 2.0f);
            assertThat(r.toTuple()).isEqualTo(tuple4("TEST", 23, 3.0d, 2.0f));
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void transmogrify_contracts_and_value() {
            LOGGER.info("Record4.transmogrify should enforce non-null and return a non-null result");

            final var r = record4("TEST", 23, 3.0d, 2.0f);

            assertThatThrownBy(() -> r.transmogrify(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.transmogrify(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var result = r.transmogrify(x -> x.get1() + x.get2() + x.get3() + x.get4());
                assertThat(result).isEqualTo("TEST233.02.0");
            });
        }

        @Test
        void copy_preserves_semantics() {
            LOGGER.info("Record4.copy should produce a structural copy");

            final var r = record4("TEST", 23, 3.0d, 2.0f);
            assertThat(r.copy()).isEqualTo(record4("TEST", 23, 3.0d, 2.0f));
        }

        @Test
        void equals_hashCode_and_toString() {
            LOGGER.info("Record4.equals/hashCode should be structural; toString should be stable formatted");

            final var r1 = record4("TEST", 23, 3.0d, 2.0f);
            final var r2 = record4("TEST", 23, 3.0d, 2.0f);
            final var r3 = record4("TEST2", 24, 4.0d, 3.0f);

            // equals/hashCode structural
            assertThat(r1).isEqualTo(r1);
            assertThat(r1).isEqualTo(r2);
            assertThat(r1).isNotEqualTo(r3);
            assertThat(r1).hasSameHashCodeAs(r2);
            assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());

            // toString
            assertThat(r1).hasToString("Record4[value1=TEST, value2=23, value3=3.0, value4=2.0]");
        }
    }
}
