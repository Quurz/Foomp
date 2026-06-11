package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Pair.pair;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Pair")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PairTest {

    private static final Logger LOGGER
        = getLogger(PairTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void empty_pair_has_no_values() {
            LOGGER.info("Pair.pair(null, null) should create an empty pair");

            var p = pair(null, null);

            assertFalse(p.is1());
            assertFalse(p.is2());
        }

        @Test
        void pair_with_values_reports_presence() {
            LOGGER.info("Pair.pair(value1, value2) should reflect presence");
            var p = pair("test", 42);

            assertTrue(p.is1());
            assertTrue(p.is2());
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void get_and_set_update_values_and_presence_flags() {
            LOGGER.info("Pair.get*/set* should read/write values and update presence flags");

            var p = pair("initial", 0);

            p.set1("updated");
            p.set2(1);

            assertEquals("updated", p.get1());
            assertEquals(1, p.get2());
            assertTrue(p.is1());
            assertTrue(p.is2());

            p.set1(null);
            assertFalse(p.is1());
            assertNull(p.get1());
        }

        @Test
        void with_methods_return_new_instances_with_replaced_values() {
            LOGGER.info("Pair.with* should return new pair with replaced value and keep other");

            var p = pair("test", 42);

            var p1 = p.with1("new");
            var p2 = p.with2(99);

            assertEquals("new", p1.get1());
            assertEquals(42, p1.get2());

            assertEquals("test", p2.get1());
            assertEquals(99, p2.get2());

            // original unchanged
            assertEquals("test", p.get1());
            assertEquals(42, p.get2());
        }
    }

    @Nested
    @DisplayName("Utilities")
    class Utilities {

        @Test
        void equals_and_hashCode_compare_componentwise() {
            LOGGER.info("Pair.equals/hashCode should compare by value1/value2");

            var a = pair("test", 42);
            var b = pair("test", 42);
            var c = pair("different", 42);

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
            assertNotEquals(a, c);
        }

        @Test
        void toString_formats_values() {
            LOGGER.info("Pair.toString should include both values");

            var p = pair("test", 42);
            assertEquals("Pair[value1=test, value2=42]", p.toString());
        }

        @Test
        void toTuple_wraps_values_in_Maybe() {
            LOGGER.info("Pair.toTuple should return Tuple2<Maybe<A1>, Maybe<A2>> reflecting nullability");

            var p1 = pair("x", 7);
            var p2 = pair(null, 7);
            var p3 = pair("x", null);
            var p4 = pair(null, null);

            assertEquals(tuple2(maybeOfNullable("x"), maybeOfNullable(7)), p1.toTuple());
            assertEquals(tuple2(maybeOfNullable(null), maybeOfNullable(7)), p2.toTuple());
            assertEquals(tuple2(maybeOfNullable("x"), maybeOfNullable(null)), p3.toTuple());
            assertEquals(tuple2(maybeOfNullable(null), maybeOfNullable(null)), p4.toTuple());
        }

        @Test
        void isPresent_delegates_to_is1() {
            LOGGER.info("Pair.isPresent should delegate to is1()");
            var p = pair(null, "x");
            assertFalse(p.isPresent());
            p.set1("y");
            assertTrue(p.isPresent());
        }
    }
}
