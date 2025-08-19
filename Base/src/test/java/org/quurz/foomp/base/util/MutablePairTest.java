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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.quurz.foomp.base.util.MutablePair.mutablePair;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("MutablePairTest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MutablePairTest {

    private static final Logger LOGGER
        = getLogger(MutablePairTest.class);

    @Nested
    class Factory {

        @Test
        void should_create_empty_pair() {
            LOGGER.info("Testing creation of empty mutablePair");

            var mutablePair
                = mutablePair(null, null);

            assertFalse(mutablePair.is1());
            assertFalse(mutablePair.is2());
        }

        @Test
        void should_create_pair_with_values() {
            LOGGER.info("Testing creation of mutablePair with values");

            var mutablePair
                = mutablePair("test", 42);

            assertTrue(mutablePair.is1());
            assertTrue(mutablePair.is2());
        }
    }

    @Nested
    class Accessor {

        @Test
        void should_get_and_set_values() {
            LOGGER.info("Testing getter and setter operations");

            var mutablePair
                = mutablePair("initial", 0);

            mutablePair.set1("updated");
            mutablePair.set2(1);

            assertEquals("updated", mutablePair.get1());
            assertEquals(1, mutablePair.get2());
        }

        @Test
        void should_create_new_pair_with_updated_values() {
            LOGGER.info("Testing with operations");

            var mutablePair
                = mutablePair("test", 42);

            var mutablePair1
                = mutablePair.with1("new");
            var mutablePair2
                = mutablePair.with2(99);

            assertEquals("new", mutablePair1.get1());
            assertEquals(99, mutablePair2.get2());
        }
    }

    @Test
    void should_compare_pairs_correctly() {
        LOGGER.info("Testing equality comparison");

        var mutablePair1
            = mutablePair("test", 42);
        var mutablePair2
            = mutablePair("test", 42);
        var mutablePair3
            = mutablePair("different", 42);

        assertEquals(mutablePair1, mutablePair2);
        assertNotEquals(mutablePair1, mutablePair3);
    }

    @Test
    void should_convert_to_string() {
        LOGGER.info("Testing toString representation");

        var mutablePair
            = mutablePair("test", 42);
        assertEquals("MutablePair[value1=test, value2=42]", mutablePair.toString());
    }

}
