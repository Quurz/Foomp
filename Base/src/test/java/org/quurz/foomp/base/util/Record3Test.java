package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record3.record3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Record3")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Record3Test {

    private static final Logger LOGGER = getLogger(Record3Test.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void record3_and_constructor_enforce_non_null() {
            LOGGER.info("Record3.record3 and Record3::<init> NPE contracts");

            assertThatThrownBy(() -> record3(null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> record3(23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> record3(23, 23, 23));

            assertThatThrownBy(() -> new Record3<>(null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(null, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(null, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(null, 23, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(23, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(23, null, 23)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new Record3<>(23, 23, null)).isInstanceOf(NullPointerException.class);
            assertThatNoException().isThrownBy(() -> new Record3<>(23, 23, 23));
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void presence_and_getters() {
            LOGGER.info("Record3.isPresent/is1/is2/is3 + get/value accessors");

            final var r = record3("TEST", 23, 23);

            assertThat(r.isPresent()).isTrue();
            assertThat(r.is1()).isTrue();
            assertThat(r.is2()).isTrue();
            assertThat(r.is3()).isTrue();

            assertThat(r.get()).isEqualTo("TEST");
            assertThat(r.get1()).isEqualTo("TEST");
            assertThat(r.get2()).isEqualTo(23);
            assertThat(r.get3()).isEqualTo(23);

            assertThat(r.value1()).isEqualTo("TEST");
            assertThat(r.value2()).isEqualTo(23);
            assertThat(r.value3()).isEqualTo(23);

            assertThat(r.get1()).isEqualTo(r.value1());
            assertThat(r.get2()).isEqualTo(r.value2());
            assertThat(r.get3()).isEqualTo(r.value3());
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void with_updates_values_and_enforces_non_null() {
            LOGGER.info("Record3.with1/with2/with3 null contracts and updates");

            final var r = record3("TEST", 23, 23);

            assertThatThrownBy(() -> r.with1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.with3(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.with1("TEST2")).isEqualTo(record3("TEST2", 23, 23));
            assertThat(r.with2(24)).isEqualTo(record3("TEST", 24, 23));
            assertThat(r.with3(24)).isEqualTo(record3("TEST", 23, 24));
        }
    }

    @Nested
    @DisplayName("Functional (map)")
    class Functional_Map {

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void map_methods_enforce_contracts_and_transform_components() {
            LOGGER.info("Record3.map1/map2/map3 contracts and transformations");

            final var r = record3("TEST", 23, 3.0d);

            assertThatThrownBy(() -> r.map1(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map2(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map3(null)).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> r.map1(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map2(_$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.map3(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThat(r.map1(String::length)).isEqualTo(record3(4, 23, 3.0d));
            assertThat(r.map2(i -> i + 1)).isEqualTo(record3("TEST", 24, 3.0d));
            assertThat(r.map3(d -> d + 1)).isEqualTo(record3("TEST", 23, 4.0d));
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void mapAll_enforces_contracts_and_transforms_all() {
            LOGGER.info("Record3.mapAll contracts and transformation of all components");

            final var r = record3("TEST", 23, 3.0d);

            assertThatThrownBy(() -> r.mapAll(null, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, null, d -> d + 1)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, i -> i + 1, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(null, i -> i + 1, d -> d + 1)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, null, null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, null, d -> d + 1)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, i -> i + 1, null)).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> r.mapAll(String::length, i -> i + 1, _$ -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(String::length, _$ -> null, d -> d + 1)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.mapAll(_$ -> null, i -> i + 1, d -> d + 1)).isInstanceOf(NullPointerException.class);

            assertThat(r.mapAll(String::length, i -> i + 1, d -> d + 1))
                .isEqualTo(record3(4, 24, 4.0d));
        }
    }

    @Nested
    @DisplayName("Applicative (applyTo)")
    class Applicative_Apply {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void apply_enforces_contracts_and_applies_functions() {
            LOGGER.info("Record3.applyTo null contracts and behaviour");

            final var r = record3("TEST", 23, 3.0d);

            assertThatThrownBy(() -> r.applyTo(null)).isInstanceOf(NullPointerException.class);

            assertThat(r.applyTo(record3(String::length, i -> i + 1, d -> d + 1)))
                .isEqualTo(record3(4, 24, 4.0d));
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Test
        void toTuple_converts_values() {
            LOGGER.info("Record3.toTuple conversion");

            final var r = record3("TEST", 23, 3.0d);
            assertThat(r.toTuple()).isEqualTo(tuple3("TEST", 23, 3.0d));
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void transmogrify_contracts_and_value() {
            LOGGER.info("Record3.transmogrify contracts and result");

            final var r = record3("TEST", 23, 3.0d);

            assertThatThrownBy(() -> r.transmogrify(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> r.transmogrify(_$ -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var result = r.transmogrify(x -> x.get1() + x.get2() + x.get3());
                assertThat(result).isEqualTo("TEST233.0");
            });
        }

        @Test
        void copy_preserves_semantics() {
            LOGGER.info("Record3.copy should keep values");

            final var r = record3("TEST", 23, 3.0d);
            assertThat(r.copy()).isEqualTo(record3("TEST", 23, 3.0d));
        }

        @Test
        void equals_hashCode_and_toString() {
            LOGGER.info("Record3.equals/hashCode structural and toString formatting");

            final var r1 = record3("TEST", 23, 3.0d);
            final var r2 = record3("TEST", 23, 3.0d);
            final var r3 = record3("TEST2", 23, 3.0d);

            // hashCode and equals against different type
            assertThat(r1.hashCode()).isNotEqualTo("GUMPF".hashCode());
            assertThat(r1).isNotEqualTo("GUMPF");

            // equals/hashCode structural
            assertThat(r1).hasSameHashCodeAs(r2);
            assertThat(r1.hashCode()).isNotEqualTo(r3.hashCode());

            assertThat(r1).isEqualTo(r1);
            assertThat(r1).isEqualTo(r2);
            assertThat(r1).isNotEqualTo(r3);

            // toString
            assertThat(r1).hasToString("Record3[value1=TEST, value2=23, value3=3.0]");
        }
    }
}
