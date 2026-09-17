package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.types.Dict;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.quurz.foomp.base.util.Dictionary.dictionary;
import static org.quurz.foomp.base.util.Dictionary.dictionaryFrom;
import static org.quurz.foomp.base.util.Dictionary.dictionaryOf;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

@DisplayName("Dictionary Tests")
class DictionaryTest {

    private static final Logger LOGGER = Logger.getLogger(DictionaryTest.class.getName());

    private record CollidingKey(String id, int forcedHash) {
        @Override
        public int hashCode() {
            return forcedHash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof CollidingKey other)) return false;
            return Objects.equals(this.id, other.id);
        }
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethods {

        @Test
        void empty_dictionary() {
            LOGGER.info("empty dictionary should contain nothing");
            final Dictionary<String, Integer> dict = dictionary();
            assertFalse(dict.contains("a"));
            assertFalse(dict.getSafe("a").isPresent());
            assertThrows(NoSuchElementException.class, () -> dict.get("a"));
        }

        @Test
        void dictionaryOf_varargs() {
            LOGGER.info("dictionaryOf creates dictionary from Tuple2 entries");
            final Dictionary<String, Integer> dict = dictionaryOf(
                tuple2("a", 1),
                tuple2("b", 2),
                tuple2("c", 3)
            );
            assertTrue(dict.contains("a"));
            assertTrue(dict.contains("b"));
            assertTrue(dict.contains("c"));
            assertEquals(1, dict.get("a"));
            assertEquals(2, dict.get("b"));
            assertEquals(3, dict.get("c"));
        }

        @Test
        void dictionaryFrom_map() {
            LOGGER.info("dictionaryFrom creates dictionary from java.util.Map");
            final Map<String, Integer> map = new HashMap<>();
            map.put("x", 10);
            map.put("y", 20);
            final Dictionary<String, Integer> dict = dictionaryFrom(map);
            assertEquals(10, dict.get("x"));
            assertEquals(20, dict.get("y"));
        }

        @Test
        void factory_methods_null_checks() {
            LOGGER.info("null checks for factory methods");
            assertThrows(NullPointerException.class, () -> dictionaryOf((Tuple2<String, Integer>[]) null));
            assertThrows(NullPointerException.class, () -> dictionaryOf(tuple2("a", 1), null));
            assertThrows(NullPointerException.class, () -> dictionaryFrom(null));
        }
    }

    @Nested
    @DisplayName("Behaviour: Put, Get, Contains and Remove")
    class PutGetContainsRemove {

        @Test
        void put_and_get() {
            LOGGER.info("put inserts and get retrieves values");
            final Dict<String, Integer> d1 = dictionary();
            final Dict<String, Integer> d2 = d1.put("k1", 100);
            final Dict<String, Integer> d3 = d2.put("k2", 200);

            assertEquals(100, d3.get("k1"));
            assertEquals(200, d3.get("k2"));
            assertEquals(Maybe.some(100), d3.getSafe("k1"));
            assertEquals(Maybe.some(200), d3.getSafe("k2"));
            assertEquals(Maybe.none(), d3.getSafe("k3"));
        }

        @Test
        void put_overwrites_existing_key() {
            LOGGER.info("put overwrites existing key with new value");
            final Dict<String, Integer> d1 = dictionaryOf(tuple2("a", 1));
            final Dict<String, Integer> d2 = d1.put("a", 99);

            assertEquals(99, d2.get("a"));
            assertEquals(1, d1.get("a")); // Original is immutable
        }

        @Test
        void contains_checks_presence() {
            LOGGER.info("contains returns true if present, false otherwise");
            final Dict<String, Integer> dict = dictionaryOf(tuple2("k1", 1));
            assertTrue(dict.contains("k1"));
            assertFalse(dict.contains("k2"));
        }

        @Test
        void remove_existing_key() {
            LOGGER.info("remove removes existing key");
            final Dict<String, Integer> d1 = dictionaryOf(tuple2("a", 1), tuple2("b", 2));
            final Dict<String, Integer> d2 = d1.remove("a");

            assertFalse(d2.contains("a"));
            assertTrue(d2.contains("b"));
            assertEquals(2, d2.get("b"));
            assertTrue(d1.contains("a")); // Immutability
        }

        @Test
        void remove_non_existing_key_throws() {
            LOGGER.info("remove non existing key throws NoSuchElementException");
            final Dict<String, Integer> dict = dictionaryOf(tuple2("a", 1));
            assertThrows(NoSuchElementException.class, () -> dict.remove("b"));
        }

        @Test
        void null_checks() {
            LOGGER.info("null arguments throw NullPointerException");
            final Dict<String, Integer> dict = dictionary();
            assertThrows(NullPointerException.class, () -> dict.put(null, 1));
            assertThrows(NullPointerException.class, () -> dict.put("a", null));
            assertThrows(NullPointerException.class, () -> dict.contains(null));
            assertThrows(NullPointerException.class, () -> dict.get(null));
            assertThrows(NullPointerException.class, () -> dict.getSafe(null));
            assertThrows(NullPointerException.class, () -> dict.remove(null));
        }
    }

    @Nested
    @DisplayName("Hash Collisions")
    class HashCollisions {

        @Test
        void collision_handling_in_put_get_contains_and_remove() {
            LOGGER.info("multiple keys with identical hashcode are resolved cleanly");
            final CollidingKey k1 = new CollidingKey("key1", 42);
            final CollidingKey k2 = new CollidingKey("key2", 42);
            final CollidingKey k3 = new CollidingKey("key3", 42);

            Dict<CollidingKey, String> dict = dictionary();
            dict = dict.put(k1, "val1");
            dict = dict.put(k2, "val2");
            dict = dict.put(k3, "val3");

            assertTrue(dict.contains(k1));
            assertTrue(dict.contains(k2));
            assertTrue(dict.contains(k3));
            assertEquals("val1", dict.get(k1));
            assertEquals("val2", dict.get(k2));
            assertEquals("val3", dict.get(k3));

            // Update in collision chain
            final Dict<CollidingKey, String> updated = dict.put(k2, "val2_updated");
            assertEquals("val2_updated", updated.get(k2));
            assertEquals("val1", updated.get(k1));
            assertEquals("val3", updated.get(k3));
            assertEquals("val2", dict.get(k2)); // Immutability

            // Remove middle element in collision chain
            final Dict<CollidingKey, String> removedMiddle = dict.remove(k2);
            assertFalse(removedMiddle.contains(k2));
            assertTrue(removedMiddle.contains(k1));
            assertTrue(removedMiddle.contains(k3));

            // Remove all elements
            final Dict<CollidingKey, String> removedAll = removedMiddle.remove(k1).remove(k3);
            assertFalse(removedAll.contains(k1));
            assertFalse(removedAll.contains(k2));
            assertFalse(removedAll.contains(k3));
        }
    }

    @Nested
    @DisplayName("Immutability & Laziness")
    class ImmutabilityAndLaziness {

        @Test
        void immutability_across_operations() {
            LOGGER.info("operations do not modify prior dictionary instances");
            final Dict<String, Integer> base = dictionaryOf(tuple2("a", 1), tuple2("b", 2));
            final Dict<String, Integer> added = base.put("c", 3);
            final Dict<String, Integer> modified = base.put("a", 10);
            final Dict<String, Integer> removed = base.remove("a");

            // Base remains untouched
            assertTrue(base.contains("a"));
            assertTrue(base.contains("b"));
            assertFalse(base.contains("c"));
            assertEquals(1, base.get("a"));

            // Derived instances reflect their specific state
            assertTrue(added.contains("c"));
            assertEquals(10, modified.get("a"));
            assertFalse(removed.contains("a"));
        }

        @Test
        void map_is_evaluated_lazily() {
            LOGGER.info("map is evaluated lazily upon demand");
            final AtomicInteger counter = new AtomicInteger(0);

            final Dictionary<String, Integer> dict = dictionaryOf(
                tuple2("a", 10),
                tuple2("b", 20)
            );

            final Dictionary<String, Integer> mapped = dict.map(x -> {
                counter.incrementAndGet();
                return x * 2;
            });

            // No evaluation before get()
            assertEquals(0, counter.get());

            // Accessing "a" evaluates once for "a"
            assertEquals(20, mapped.get("a"));
            assertEquals(1, counter.get());

            // Accessing "b" evaluates once for "b"
            assertEquals(40, mapped.get("b"));
            assertEquals(2, counter.get());
        }

        @Test
        void mapped_dictionary_with_hash_collisions() {
            LOGGER.info("mapped dictionary retains collision resolution lazily");
            final CollidingKey k1 = new CollidingKey("c1", 100);
            final CollidingKey k2 = new CollidingKey("c2", 100);

            final Dictionary<CollidingKey, String> dict = dictionaryOf(
                tuple2(k1, "hello"),
                tuple2(k2, "world")
            );

            final Dictionary<CollidingKey, Integer> mapped = dict.map(String::length);
            assertEquals(5, mapped.get(k1));
            assertEquals(5, mapped.get(k2));
        }
    }

    @Nested
    @DisplayName("ToMap")
    class ToMap {

        @Test
        void toMap_on_empty_dictionary_returns_empty_map() {
            LOGGER.info("toMap on empty dictionary yields empty map");
            final Dict<String, Integer> dict = dictionary();
            final Map<String, Integer> map = dict.toMap(HashMap::new);
            assertTrue(map.isEmpty());
        }

        @Test
        void toMap_on_filled_dictionary_populates_all_entries() {
            LOGGER.info("toMap on populated dictionary populates all entries");
            final Dict<String, Integer> dict = dictionaryOf(
                tuple2("a", 1),
                tuple2("b", 2),
                tuple2("c", 3)
            );
            final Map<String, Integer> map = dict.toMap(HashMap::new);
            assertEquals(3, map.size());
            assertEquals(1, map.get("a"));
            assertEquals(2, map.get("b"));
            assertEquals(3, map.get("c"));
        }

        @Test
        void toMap_with_hash_collisions_populates_all_colliding_keys() {
            LOGGER.info("toMap preserves all entries even under hash collisions");
            final CollidingKey k1 = new CollidingKey("c1", 42);
            final CollidingKey k2 = new CollidingKey("c2", 42);
            final CollidingKey k3 = new CollidingKey("c3", 42);

            final Dict<CollidingKey, String> dict = dictionaryOf(
                tuple2(k1, "val1"),
                tuple2(k2, "val2"),
                tuple2(k3, "val3")
            );

            final Map<CollidingKey, String> map = dict.toMap(HashMap::new);
            assertEquals(3, map.size());
            assertEquals("val1", map.get(k1));
            assertEquals("val2", map.get(k2));
            assertEquals("val3", map.get(k3));
        }

        @Test
        void toMap_unwinds_lazy_spools() {
            LOGGER.info("toMap unwinds lazy value transformations");
            final AtomicInteger counter = new AtomicInteger(0);
            final Dictionary<String, Integer> dict = dictionaryOf(
                tuple2("a", 10),
                tuple2("b", 20)
            );
            final Dictionary<String, Integer> mapped = dict.map(x -> {
                counter.incrementAndGet();
                return x * 3;
            });

            assertEquals(0, counter.get());
            final Map<String, Integer> map = mapped.toMap(HashMap::new);
            assertEquals(2, counter.get());
            assertEquals(30, map.get("a"));
            assertEquals(60, map.get("b"));
        }

        @Test
        void toMap_null_checks() {
            LOGGER.info("toMap throws NullPointerException on null init or null supplied target");
            final Dict<String, Integer> dict = dictionaryOf(tuple2("a", 1));
            assertThrows(NullPointerException.class, () -> dict.toMap(null));
            assertThrows(NullPointerException.class, () -> dict.toMap(() -> null));
        }
    }

    @Nested
    @DisplayName("Narrow")
    class Narrow {

        @Test
        @SuppressWarnings("unchecked")
        void narrow_higher1_with_null_throws() {
            LOGGER.info("Dictionary.narrow((Higher1) null) should throw NullPointerException");
            assertThatThrownBy(() -> Dictionary.narrow((Higher1<Dictionary.µ, Integer>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("wide");
        }

        @Test
        @SuppressWarnings("unchecked")
        void narrow_higher2_with_null_throws() {
            LOGGER.info("Dictionary.narrow((Higher2) null) should throw NullPointerException");
            assertThatThrownBy(() -> Dictionary.narrow((Higher2<Dictionary.µ, String, Integer>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("wide");
        }

        @Test
        void narrow_higher1_with_valid_dictionary_returns_same_instance() {
            LOGGER.info("Dictionary.narrow(Higher1) should return the same dictionary instance");
            final Dictionary<String, Integer> dict = dictionaryOf(tuple2("a", 1));
            final Higher1<Dictionary.µ, Integer> higher = dict;
            final Dictionary<String, Integer> narrowed = Dictionary.narrow(higher);
            assertThat(narrowed).isSameAs(dict);
        }

        @Test
        void narrow_higher2_with_valid_dictionary_returns_same_instance() {
            LOGGER.info("Dictionary.narrow(Higher2) should return the same dictionary instance");
            final Dictionary<String, Integer> dict = dictionaryOf(tuple2("a", 1));
            final Higher2<Dictionary.µ, String, Integer> higher = dict;
            final Dictionary<String, Integer> narrowed = Dictionary.narrow(higher);
            assertThat(narrowed).isSameAs(dict);
        }

        @Test
        @SuppressWarnings({"rawtypes", "unchecked"})
        void narrow_higher1_with_foreign_witness_throws_illegal_argument() {
            LOGGER.info("Dictionary.narrow(Higher1) with foreign instance should throw IllegalArgumentException");
            final var box = Box.box("hello");
            assertThatThrownBy(() -> Dictionary.narrow((Higher1) box))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @SuppressWarnings({"rawtypes", "unchecked"})
        void narrow_higher2_with_foreign_witness_throws_illegal_argument() {
            LOGGER.info("Dictionary.narrow(Higher2) with foreign instance should throw IllegalArgumentException");
            final var pair = Tuple2.tuple2("a", 1);
            assertThatThrownBy(() -> Dictionary.narrow((Higher2) pair))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("ApplyTo")
    class ApplyTo {

        @Test
        void applyTo_applies_functions_to_matching_keys_only() {
            LOGGER.info("applyTo computes key intersection and applies transformation functions");
            final Dictionary<String, String> dict = dictionaryOf(
                tuple2("Heinz", "Hund"),
                tuple2("Klaus", "Katze"),
                tuple2("Mimi", "Maus")
            );

            final Dictionary<String, Function<String, Integer>> fnDict = dictionaryOf(
                tuple2("Heinz", String::length),
                tuple2("Klaus", s -> s.length() * 10),
                tuple2("Waldi", s -> 99)
            );

            final Dictionary<String, Integer> applied = dict.applyTo(fnDict);

            assertTrue(applied.contains("Heinz"));
            assertTrue(applied.contains("Klaus"));
            assertFalse(applied.contains("Mimi"));
            assertFalse(applied.contains("Waldi"));

            assertEquals(4, applied.get("Heinz"));
            assertEquals(50, applied.get("Klaus"));
        }

        @Test
        void applyTo_on_empty_dictionary_returns_empty_dictionary() {
            LOGGER.info("applyTo on empty dictionary yields empty dictionary");
            final Dictionary<String, String> emptyDict = dictionary();
            final Dictionary<String, Function<String, Integer>> fnDict = dictionaryOf(
                tuple2("Heinz", String::length)
            );

            final Dictionary<String, Integer> applied = emptyDict.applyTo(fnDict);
            assertFalse(applied.contains("Heinz"));
        }

        @Test
        void applyTo_with_empty_function_dictionary_returns_empty_dictionary() {
            LOGGER.info("applyTo with empty function dictionary yields empty dictionary");
            final Dictionary<String, String> dict = dictionaryOf(
                tuple2("Heinz", "Hund")
            );
            final Dictionary<String, Function<String, Integer>> emptyFnDict = dictionary();

            final Dictionary<String, Integer> applied = dict.applyTo(emptyFnDict);
            assertFalse(applied.contains("Heinz"));
        }

        @Test
        void applyTo_is_evaluated_lazily() {
            LOGGER.info("applyTo defers function application until get or toMap is called");
            final AtomicInteger counter = new AtomicInteger(0);
            final Dictionary<String, String> dict = dictionaryOf(
                tuple2("Heinz", "Hund")
            );

            final Dictionary<String, Function<String, Integer>> fnDict = dictionaryOf(
                tuple2("Heinz", s -> {
                    counter.incrementAndGet();
                    return s.length();
                })
            );

            final Dictionary<String, Integer> applied = dict.applyTo(fnDict);
            assertEquals(0, counter.get());

            assertEquals(4, applied.get("Heinz"));
            assertEquals(1, counter.get());
        }

        @Test
        void applyTo_with_hash_collisions_works_correctly() {
            LOGGER.info("applyTo preserves and transforms colliding keys correctly");
            final CollidingKey k1 = new CollidingKey("c1", 42);
            final CollidingKey k2 = new CollidingKey("c2", 42);
            final CollidingKey k3 = new CollidingKey("c3", 42);

            final Dictionary<CollidingKey, Integer> dict = dictionaryOf(
                tuple2(k1, 10),
                tuple2(k2, 20),
                tuple2(k3, 30)
            );

            final Dictionary<CollidingKey, Function<Integer, String>> fnDict = dictionaryOf(
                tuple2(k1, x -> "val-" + x),
                tuple2(k3, x -> "triple-" + x)
            );

            final Dictionary<CollidingKey, String> applied = dict.applyTo(fnDict);

            assertTrue(applied.contains(k1));
            assertFalse(applied.contains(k2));
            assertTrue(applied.contains(k3));

            assertEquals("val-10", applied.get(k1));
            assertEquals("triple-30", applied.get(k3));
        }

        @Test
        void applyTo_null_checks() {
            LOGGER.info("applyTo throws NullPointerException on null transformation");
            final Dictionary<String, String> dict = dictionaryOf(tuple2("a", "b"));
            assertThrows(NullPointerException.class, () -> dict.applyTo((Dictionary<String, Function<String, Integer>>) null));
            assertThrows(NullPointerException.class, () -> dict.applyTo((Higher1<Dictionary.µ, Function<String, Integer>>) null));
        }

        @Test
        void applyTo_via_higher1_delegates_to_typed_applyTo() {
            LOGGER.info("applyTo via Higher1 interface applies functions to matching keys");
            final Dictionary<String, String> dict = dictionaryOf(
                tuple2("Heinz", "Hund")
            );
            final Higher1<Dictionary.µ, Function<String, Integer>> fnDict = dictionaryOf(
                tuple2("Heinz", String::length)
            );

            final Dictionary<String, Integer> applied = dict.applyTo(fnDict);
            assertTrue(applied.contains("Heinz"));
            assertEquals(4, applied.get("Heinz"));
        }

        @Test
        @SuppressWarnings({"rawtypes", "unchecked"})
        void applyTo_foreign_witness_throws_illegal_argument() {
            LOGGER.info("applyTo throws IllegalArgumentException on non-Dictionary Higher1 instance");
            final Dictionary<String, String> dict = dictionaryOf(tuple2("a", "b"));
            final var box = Box.box((Function<String, Integer>) String::length);
            assertThatThrownBy(() -> dict.applyTo((Higher1) box))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
