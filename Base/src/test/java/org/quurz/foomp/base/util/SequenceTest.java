package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Sequence.sequence;
import static org.quurz.foomp.base.util.Sequence.sequenceFrom;
import static org.quurz.foomp.base.util.Sequence.sequenceOf;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
@DisplayName("Sequence")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "DataFlowIssue"})
class SequenceTest
        extends TestHelper {

    private static final Logger LOGGER = getLogger(SequenceTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void sequence_returns_empty_sequence() {
            LOGGER.info("Sequence.sequence() should return an empty sequence");
            final Sequence<String> seq = sequence();
            assertThat(seq).isNotNull();
            assertThat(seq.isNotEmpty()).isFalse();
        }

        @Test
        void sequenceOf_with_null_array_throws() {
            LOGGER.info("Sequence.sequenceOf(null) should throw NullPointerException");
            assertThatThrownBy(() -> sequenceOf((String[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("elements");
        }

        @Test
        void sequenceOf_with_zero_arguments_returns_empty_sequence() {
            LOGGER.info("Sequence.sequenceOf() without args should return empty sequence");
            final Sequence<String> seq = sequenceOf();
            assertThat(seq).isNotNull();
            assertThat(seq.isNotEmpty()).isFalse();
        }

        @Test
        void sequenceOf_with_null_element_throws() {
            LOGGER.info("Sequence.sequenceOf(...) containing null should throw NullPointerException");
            assertThatThrownBy(() -> sequenceOf("A", null, "C"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("elements");
        }

        @Test
        void sequenceOf_with_valid_elements_creates_sequence() {
            LOGGER.info("Sequence.sequenceOf(...) should create non-empty sequence with elements");
            final Sequence<String> seq = sequenceOf("A", "B", "C");
            assertThat(seq.isNotEmpty()).isTrue();
            assertThat(seq.head()).isEqualTo("A");
        }

        @Test
        void sequenceFrom_with_null_collection_throws() {
            LOGGER.info("Sequence.sequenceFrom(null) should throw NullPointerException");
            assertThatThrownBy(() -> sequenceFrom(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("collection");
        }

        @Test
        void sequenceFrom_with_empty_collection_returns_empty_sequence() {
            LOGGER.info("Sequence.sequenceFrom(empty) should return empty sequence");
            final Sequence<String> seq = sequenceFrom(Collections.emptyList());
            assertThat(seq).isNotNull();
            assertThat(seq.isNotEmpty()).isFalse();
        }

        @Test
        void sequenceFrom_with_null_element_in_collection_throws() {
            LOGGER.info("Sequence.sequenceFrom(collectionWithNull) should throw NullPointerException");
            final List<String> listWithNull = Arrays.asList("A", null, "C");
            assertThatThrownBy(() -> sequenceFrom(listWithNull))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("collection");
        }

        @Test
        void sequenceFrom_with_valid_collection_creates_sequence() {
            LOGGER.info("Sequence.sequenceFrom(validList) should create sequence with elements");
            final Sequence<String> seq = sequenceFrom(List.of("X", "Y", "Z"));
            assertThat(seq.isNotEmpty()).isTrue();
            assertThat(seq.head()).isEqualTo("X");
        }
    }

    @Nested
    @DisplayName("Head & HeadSafe")
    class Head_And_HeadSafe {

        @Test
        void head_on_empty_sequence_throws() {
            LOGGER.info("head() on empty sequence should throw NoSuchElementException");
            final Sequence<String> seq = sequence();
            assertThatThrownBy(seq::head)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void head_on_non_empty_sequence_returns_first_element() {
            LOGGER.info("head() on non-empty sequence should return first element");
            final Sequence<String> seq = sequenceOf("First", "Second");
            assertThat(seq.head()).isEqualTo("First");
        }

        @Test
        void headSafe_on_empty_sequence_returns_none() {
            LOGGER.info("headSafe() on empty sequence should return none()");
            final Sequence<String> seq = sequence();
            assertThat(seq.headSafe().isNone()).isTrue();
        }

        @Test
        void headSafe_on_non_empty_sequence_returns_some_with_value() {
            LOGGER.info("headSafe() on non-empty sequence should return some(firstElement)");
            final Sequence<String> seq = sequenceOf("First", "Second");
            final var maybeHead = seq.headSafe();
            assertThat(maybeHead.isSome()).isTrue();
            assertThat(maybeHead.get()).isEqualTo("First");
        }

        @Test
        void head_when_mapped_function_returns_null_throws() {
            LOGGER.info("head() should throw NullPointerException if transformation returns null");
            final Sequence<String> seq = sequenceOf("A").map(s -> null);
            assertThatThrownBy(seq::head)
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Tail")
    class Tail {

        @Test
        void tail_on_empty_sequence_throws() {
            LOGGER.info("tail() on empty sequence should throw NoSuchElementException");
            final Sequence<String> seq = sequence();
            assertThatThrownBy(seq::tail)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void tail_on_single_element_sequence_returns_empty_sequence() {
            LOGGER.info("tail() on single element sequence should return empty sequence");
            final Sequence<String> seq = sequenceOf("Only");
            final Sequence<String> tail = seq.tail();
            assertThat(tail.isNotEmpty()).isFalse();
        }

        @Test
        void tail_preserves_immutability_of_original_sequence() {
            LOGGER.info("tail() should not modify the original sequence");
            final Sequence<String> original = sequenceOf("A", "B", "C");
            final Sequence<String> tail1 = original.tail();
            final Sequence<String> tail2 = original.tail();

            assertThat(original.head()).isEqualTo("A");
            assertThat(tail1.head()).isEqualTo("B");
            assertThat(tail2.head()).isEqualTo("B");
            final var originalList = original.toCollection(ArrayList::new);
            assertThat(originalList).containsExactly("A", "B", "C");
        }

        @Test
        void tail_iterates_through_multiple_elements() {
            LOGGER.info("Repeated tail() calls should walk through all elements");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            assertThat(seq.head()).isEqualTo(1);

            final Sequence<Integer> tail1 = seq.tail();
            assertThat(tail1.isNotEmpty()).isTrue();
            assertThat(tail1.head()).isEqualTo(2);

            final Sequence<Integer> tail2 = tail1.tail();
            assertThat(tail2.isNotEmpty()).isTrue();
            assertThat(tail2.head()).isEqualTo(3);

            final Sequence<Integer> tail3 = tail2.tail();
            assertThat(tail3.isNotEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Cons & Prepend")
    class Cons_And_Prepend {

        @Test
        void cons_with_null_element_throws() {
            LOGGER.info("cons(null) should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A");
            assertThatThrownBy(() -> seq.cons(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("element");
        }

        @Test
        void cons_on_empty_sequence_creates_single_element_sequence() {
            LOGGER.info("cons() on empty sequence should create a sequence with one element");
            final Sequence<String> seq = sequence();
            final Sequence<String> consed = seq.cons("New");
            assertThat(consed.isNotEmpty()).isTrue();
            assertThat(consed.head()).isEqualTo("New");
            assertThat(consed.tail().isNotEmpty()).isFalse();
        }

        @Test
        void cons_on_non_empty_sequence_prepends_element() {
            LOGGER.info("cons() on non-empty sequence should prepend element and preserve existing elements");
            final Sequence<String> original = sequenceOf("B", "C");
            final Sequence<String> consed = original.cons("A");

            assertThat(consed.head()).isEqualTo("A");
            assertThat(consed.tail().head()).isEqualTo("B");
            assertThat(consed.tail().tail().head()).isEqualTo("C");
            assertThat(consed.tail().tail().tail().isNotEmpty()).isFalse();

            // Original sequence remains unchanged
            assertThat(original.head()).isEqualTo("B");
        }

        @Test
        void prepend_delegates_to_cons() {
            LOGGER.info("prepend() should behave identically to cons()");
            final Sequence<Integer> seq = sequenceOf(2, 3);
            final Sequence<Integer> prepended = seq.prepend(1);

            assertThat(prepended.head()).isEqualTo(1);
            assertThat(prepended.tail().head()).isEqualTo(2);
            assertThat(prepended.tail().tail().head()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Append")
    class Append {

        @Test
        void append_with_null_element_throws() {
            LOGGER.info("append(null) should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A");
            assertThatThrownBy(() -> seq.append(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("element");
        }

        @Test
        void append_on_empty_sequence_creates_single_element_sequence() {
            LOGGER.info("append() on empty sequence should create a sequence with one element");
            final Sequence<String> seq = sequence();
            final Sequence<String> appended = seq.append("New");
            assertThat(appended.isNotEmpty()).isTrue();
            assertThat(appended.head()).isEqualTo("New");
            assertThat(appended.tail().isNotEmpty()).isFalse();
        }

        @Test
        void append_on_non_empty_sequence_appends_element() {
            LOGGER.info("append() on non-empty sequence should append element and preserve immutability of original");
            final Sequence<String> original = sequenceOf("A", "B");
            final Sequence<String> appended = original.append("C");

            assertThat(appended.head()).isEqualTo("A");
            assertThat(appended.tail().head()).isEqualTo("B");
            assertThat(appended.tail().tail().head()).isEqualTo("C");
            assertThat(appended.tail().tail().tail().isNotEmpty()).isFalse();

            // Original sequence remains unchanged
            assertThat(original.head()).isEqualTo("A");
            assertThat(original.tail().head()).isEqualTo("B");
            assertThat(original.tail().tail().isNotEmpty()).isFalse();
        }

        @Test
        void append_multiple_elements_in_sequence() {
            LOGGER.info("append() called multiple times should maintain all elements in order");
            final Sequence<Integer> seq = sequenceOf(1)
                .append(2)
                .append(3)
                .append(4);

            assertThat(seq.head()).isEqualTo(1);
            assertThat(seq.tail().head()).isEqualTo(2);
            assertThat(seq.tail().tail().head()).isEqualTo(3);
            assertThat(seq.tail().tail().tail().head()).isEqualTo(4);
            assertThat(seq.tail().tail().tail().tail().isNotEmpty()).isFalse();
        }

        @Test
        void append_on_mapped_sequence_preserves_spool_and_appends() {
            LOGGER.info("append() on mapped sequence should preserve mapping of prior segments");
            final Sequence<String> mapped = sequenceOf("hello", "world")
                .map(String::toUpperCase)
                .append("again");

            assertThat(mapped.head()).isEqualTo("HELLO");
            assertThat(mapped.tail().head()).isEqualTo("WORLD");
            assertThat(mapped.tail().tail().head()).isEqualTo("again");
            assertThat(mapped.tail().tail().tail().isNotEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Behaviour (map)")
    class Behaviour_Map {

        @Test
        void map_with_null_transformation_throws() {
            LOGGER.info("map(null) should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A", "B");
            assertThatThrownBy(() -> seq.map(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void map_on_empty_sequence_returns_empty_sequence() {
            LOGGER.info("map() on empty sequence should return empty sequence");
            final Sequence<String> seq = sequence();
            final Sequence<Integer> mapped = seq.map(String::length);
            assertThat(mapped.isNotEmpty()).isFalse();
        }

        @Test
        void map_is_lazy_and_transforms_on_access() {
            LOGGER.info("map() should be lazy and only evaluate when head() is called");
            final var fun = invocationCountingFun((Function<String, String>) s -> s + "_mapped");
            final Sequence<String> seq = sequenceOf("A", "B");

            final Sequence<String> mapped = seq.map(fun);
            assertThat(fun.getInvocationCount()).isEqualTo(0);

            assertThat(mapped.head()).isEqualTo("A_mapped");
            assertThat(fun.getInvocationCount()).isEqualTo(1);

            assertThat(mapped.tail().head()).isEqualTo("B_mapped");
            assertThat(fun.getInvocationCount()).isEqualTo(2);
        }

        @Test
        void map_preserves_immutability_of_original_sequence() {
            LOGGER.info("map() should not mutate original sequence");
            final Sequence<Integer> original = sequenceOf(1, 2, 3);
            final Sequence<String> mapped = original.map(i -> "val_" + i);

            final var mappedList = mapped.toCollection(ArrayList::new);
            assertThat(mappedList).containsExactly("val_1", "val_2", "val_3");
            final var originalList = original.toCollection(ArrayList::new);
            assertThat(originalList).containsExactly(1, 2, 3);
        }

        @Test
        void map_chaining_composes_functions() {
            LOGGER.info("Multiple map() calls should compose transformations correctly");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<String> mapped = seq
                .map(i -> i * 10)
                .map(i -> "val_" + i);

            assertThat(mapped.head()).isEqualTo("val_10");
            assertThat(mapped.tail().head()).isEqualTo("val_20");
            assertThat(mapped.tail().tail().head()).isEqualTo("val_30");
            assertThat(mapped.tail().tail().tail().isNotEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Behaviour (filter)")
    class Behaviour_Filter {

        @Test
        void filter_with_null_predicate_throws() {
            LOGGER.info("filter(null) should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A", "B");
            assertThatThrownBy(() -> seq.filter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("predicate");
        }

        @Test
        void filter_on_empty_sequence_returns_empty_sequence() {
            LOGGER.info("filter() on empty sequence should return empty sequence");
            final Sequence<String> seq = sequence();
            final Sequence<String> filtered = seq.filter(s -> true);
            assertThat(filtered.isNotEmpty()).isFalse();
        }

        @Test
        void filter_when_no_elements_match_returns_empty_sequence() {
            LOGGER.info("filter() when no element matches should return empty sequence");
            final Sequence<Integer> seq = sequenceOf(1, 3, 5);
            final Sequence<Integer> evens = seq.filter(i -> i % 2 == 0);
            assertThat(evens.isNotEmpty()).isFalse();
        }

        @Test
        void filter_when_all_elements_match_returns_all_elements() {
            LOGGER.info("filter() when all elements match should retain all elements");
            final Sequence<Integer> seq = sequenceOf(2, 4, 6);
            final Sequence<Integer> evens = seq.filter(i -> i % 2 == 0);
            assertThat(evens.isNotEmpty()).isTrue();
            assertThat(evens.head()).isEqualTo(2);
            assertThat(evens.tail().head()).isEqualTo(4);
            assertThat(evens.tail().tail().head()).isEqualTo(6);
            assertThat(evens.tail().tail().tail().isNotEmpty()).isFalse();
        }

        @Test
        void filter_retains_only_matching_elements_including_boundary_elements() {
            LOGGER.info("filter() should filter out non-matching elements including first and last");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3, 4, 5);
            final Sequence<Integer> evens = seq.filter(i -> i % 2 == 0);

            assertThat(evens.head()).isEqualTo(2);
            assertThat(evens.tail().head()).isEqualTo(4);
            assertThat(evens.tail().tail().isNotEmpty()).isFalse();
        }

        @Test
        void filter_preserves_immutability_of_original_sequence() {
            LOGGER.info("filter() should not mutate original sequence");
            final Sequence<Integer> original = sequenceOf(1, 2, 3, 4);
            final Sequence<Integer> filtered = original.filter(i -> i % 2 == 0);

            final var filteredList = filtered.toCollection(ArrayList::new);
            assertThat(filteredList).containsExactly(2, 4);
            final var originalList = original.toCollection(ArrayList::new);
            assertThat(originalList).containsExactly(1, 2, 3, 4);
        }

        @Test
        void filter_works_across_multiple_segments() {
            LOGGER.info("filter() should traverse multiple segments correctly");
            final Sequence<Integer> multiSegmentSeq = sequenceOf(2, 3, 4).cons(1);
            final Sequence<Integer> evens = multiSegmentSeq.filter(i -> i % 2 == 0);

            assertThat(evens.head()).isEqualTo(2);
            assertThat(evens.tail().head()).isEqualTo(4);
            assertThat(evens.tail().tail().isNotEmpty()).isFalse();
        }

        @Test
        void filter_with_mapped_sequence_evaluates_spool() {
            LOGGER.info("filter() on mapped sequence should apply predicate to mapped values");
            final Sequence<Integer> mappedSeq = sequenceOf(1, 2, 3).map(i -> i * 10);
            final Sequence<Integer> filtered = mappedSeq.filter(i -> i > 15);

            assertThat(filtered.head()).isEqualTo(20);
            assertThat(filtered.tail().head()).isEqualTo(30);
            assertThat(filtered.tail().tail().isNotEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Empty & NonEmpty")
    class Empty_And_NonEmpty {

        @Test
        void empty_sequence_reports_isEmpty_true_and_isNotEmpty_false() {
            LOGGER.info("empty sequence should have isEmpty == true and isNotEmpty == false");
            final Sequence<String> seq = sequence();
            assertThat(seq.isEmpty()).isTrue();
            assertThat(seq.isNotEmpty()).isFalse();
        }

        @Test
        void non_empty_sequence_reports_isEmpty_false_and_isNotEmpty_true() {
            LOGGER.info("non-empty sequence should have isEmpty == false and isNotEmpty == true");
            final Sequence<String> seq = sequenceOf("A");
            assertThat(seq.isEmpty()).isFalse();
            assertThat(seq.isNotEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Decons")
    class Decons {

        @Test
        void decons_on_empty_sequence_throws() {
            LOGGER.info("decons() on empty sequence should throw NoSuchElementException");
            final Sequence<String> seq = sequence();
            assertThatThrownBy(seq::decons)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void decons_on_single_element_sequence_returns_head_and_empty_tail() {
            LOGGER.info("decons() on single element sequence should return head and empty tail");
            final Sequence<String> seq = sequenceOf("Single");
            final var pair = seq.decons();

            assertThat(pair.get1()).isEqualTo("Single");
            assertThat(pair.get2().isNotEmpty()).isFalse();
        }

        @Test
        void decons_on_multi_element_sequence_returns_head_and_remainder() {
            LOGGER.info("decons() on multi-element sequence should return head and tail sequence");
            final Sequence<Integer> seq = sequenceOf(10, 20, 30);
            final var pair = seq.decons();

            assertThat(pair.get1()).isEqualTo(10);
            final var tail = pair.get2();
            assertThat(tail.isNotEmpty()).isTrue();
            assertThat(tail.head()).isEqualTo(20);
            assertThat(tail.tail().head()).isEqualTo(30);
        }

        @Test
        void decons_on_mapped_sequence_evaluates_spool() {
            LOGGER.info("decons() on mapped sequence should apply transformation to head");
            final Sequence<String> seq = sequenceOf("hello", "world").map(String::toUpperCase);
            final var pair = seq.decons();

            assertThat(pair.get1()).isEqualTo("HELLO");
            assertThat(pair.get2().head()).isEqualTo("WORLD");
        }
    }

    @Nested
    @DisplayName("Narrow")
    class Narrow {

        @Test
        void narrow_with_null_throws() {
            LOGGER.info("Seq.narrow(null) and Sequence.narrow(null) should throw NullPointerException");
            assertThatThrownBy(() -> org.quurz.foomp.base.types.Seq.narrow(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("other");

            assertThatThrownBy(() -> Sequence.narrow(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("wide");
        }

        @Test
        void narrow_with_sequence_returns_same_instance() {
            LOGGER.info("Seq.narrow(seq) and Sequence.narrow(seq) should return the sequence");
            final Sequence<String> seq = sequenceOf("A", "B");
            final var narrowedSeq = org.quurz.foomp.base.types.Seq.narrow(seq);
            assertThat(narrowedSeq).isSameAs(seq);

            final var narrowedSequence = Sequence.narrow(seq);
            assertThat(narrowedSequence).isSameAs(seq);
        }

        @Test
        void narrow_with_foreign_witness_throws_illegal_argument() {
            LOGGER.info("Sequence.narrow() with non-Sequence should throw IllegalArgumentException");
            final var box = org.quurz.foomp.base.util.Box.box("hello");
            assertThatThrownBy(() -> Sequence.narrow((org.quurz.foomp.higher.Higher1) box))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (toCollection)")
    class Behaviour_ToCollection {

        @Test
        void toCollection_with_null_supplier_throws() {
            LOGGER.info("toCollection(null) should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A", "B");
            assertThatThrownBy(() -> seq.toCollection(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");
        }

        @Test
        void toCollection_when_supplier_returns_null_throws() {
            LOGGER.info("toCollection() when supplier returns null should throw NullPointerException");
            final Sequence<String> seq = sequenceOf("A", "B");
            assertThatThrownBy(() -> seq.toCollection(() -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void toCollection_on_empty_sequence_returns_empty_collection() {
            LOGGER.info("toCollection() on empty sequence should return empty collection");
            final Sequence<String> seq = sequence();
            final var list = seq.toCollection(ArrayList::new);
            assertThat(list).isEmpty();
        }

        @Test
        void toCollection_collects_all_elements_in_order() {
            LOGGER.info("toCollection() should collect all elements into supplied collection in order");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3, 4);
            final var list = seq.toCollection(ArrayList::new);
            assertThat(list).containsExactly(1, 2, 3, 4);
        }

        @Test
        void toCollection_with_mapped_sequence_evaluates_and_collects() {
            LOGGER.info("toCollection() on mapped sequence should collect transformed elements");
            final Sequence<String> seq = sequenceOf("a", "bb", "ccc").map(String::toUpperCase);
            final var list = seq.toCollection(ArrayList::new);
            assertThat(list).containsExactly("A", "BB", "CCC");
        }
    }

    @Nested
    @DisplayName("Behaviour (applyTo)")
    class Behaviour_ApplyTo {

        @Test
        void applyTo_with_null_transformation_throws() {
            LOGGER.info("applyTo(null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.applyTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void applyTo_on_empty_data_sequence_returns_empty_sequence() {
            LOGGER.info("applyTo() on empty data sequence should return empty sequence");
            final Sequence<Integer> emptySeq = sequence();
            final Sequence<java.util.function.Function<Integer, String>> fnSeq = sequenceOf(Object::toString);
            final Sequence<String> result = emptySeq.applyTo(fnSeq);
            assertThat(result.isNotEmpty()).isFalse();
        }

        @Test
        void applyTo_with_empty_function_sequence_returns_empty_sequence() {
            LOGGER.info("applyTo() with empty function sequence should return empty sequence");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<java.util.function.Function<Integer, String>> emptyFnSeq = sequence();
            final Sequence<String> result = seq.applyTo(emptyFnSeq);
            assertThat(result.isNotEmpty()).isFalse();
        }

        @Test
        void applyTo_applies_single_function_to_elements() {
            LOGGER.info("applyTo() with single function should transform all elements");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<java.util.function.Function<Integer, Integer>> fnSeq = sequenceOf(x -> x * 10);
            final Sequence<Integer> result = seq.applyTo(fnSeq);

            final var list = result.toCollection(ArrayList::new);
            assertThat(list).containsExactly(10, 20, 30);
        }

        @Test
        void applyTo_applies_multiple_functions_producing_cartesian_product() {
            LOGGER.info("applyTo() with multiple functions should produce cartesian product in correct order");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<java.util.function.Function<Integer, Integer>> fnSeq = sequenceOf(
                x -> x * 10,
                x -> x + 100
            );
            final Sequence<Integer> result = seq.applyTo(fnSeq);

            final var list = result.toCollection(ArrayList::new);
            assertThat(list).containsExactly(10, 20, 30, 101, 102, 103);
        }

        @Test
        void applyTo_with_multi_segment_sequences() {
            LOGGER.info("applyTo() across multi-segment data and function sequences");
            final Sequence<Integer> dataSeq = sequenceOf(2, 3).cons(1);
            final Sequence<java.util.function.Function<Integer, String>> fnSeq = sequenceOf((java.util.function.Function<Integer, String>) (x -> "B:" + x))
                .cons(x -> "A:" + x);

            final Sequence<String> result = dataSeq.applyTo(fnSeq);
            final var list = result.toCollection(ArrayList::new);
            assertThat(list).containsExactly("A:1", "A:2", "A:3", "B:1", "B:2", "B:3");
        }

        @Test
        void applyTo_is_lazy_until_consumed() {
            LOGGER.info("applyTo() should not eagerly evaluate functions until elements are requested");
            final java.util.concurrent.atomic.AtomicInteger callCount = new java.util.concurrent.atomic.AtomicInteger(0);
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<java.util.function.Function<Integer, Integer>> fnSeq = sequenceOf(x -> {
                callCount.incrementAndGet();
                return x * 2;
            });

            final Sequence<Integer> applied = seq.applyTo(fnSeq);
            assertThat(callCount.get()).isEqualTo(0);

            assertThat(applied.head()).isEqualTo(2);
            assertThat(callCount.get()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Behaviour (flatMap)")
    class Behaviour_FlatMap {

        @Test
        void flatMap_with_null_transformation_throws() {
            LOGGER.info("flatMap(null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.flatMap(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void flatMap_on_empty_sequence_returns_empty_sequence() {
            LOGGER.info("flatMap() on empty sequence should return empty sequence");
            final Sequence<Integer> emptySeq = sequence();
            final Sequence<String> result = emptySeq.flatMap(x -> sequenceOf(x.toString()));
            assertThat(result.isNotEmpty()).isFalse();
        }

        @Test
        void flatMap_flattens_inner_sequences() {
            LOGGER.info("flatMap() should map elements to sequences and flatten them in order");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<Integer> result = seq.flatMap(x -> sequenceOf(x, x * 10));

            final var list = result.toCollection(ArrayList::new);
            assertThat(list).containsExactly(1, 10, 2, 20, 3, 30);
        }

        @Test
        void flatMap_skips_empty_inner_sequences() {
            LOGGER.info("flatMap() should skip empty inner sequences and return empty sequence if all are empty");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3, 4);
            final Sequence<Integer> evenDoubled = seq.flatMap(x -> (x % 2 == 0) ? sequenceOf(x * 2) : sequence());

            final var list = evenDoubled.toCollection(ArrayList::new);
            assertThat(list).containsExactly(4, 8);

            final Sequence<Integer> allEmpty = seq.flatMap(x -> sequence());
            assertThat(allEmpty.isNotEmpty()).isFalse();
        }

        @Test
        void flatMap_preserves_immutability_of_source_and_inner_sequences() {
            LOGGER.info("flatMap() should not mutate source sequence or inner sequences");
            final Sequence<String> inner1 = sequenceOf("A1", "A2");
            final Sequence<String> inner2 = sequenceOf("B1");
            final Sequence<Integer> source = sequenceOf(1, 2);

            final Sequence<String> result = source.flatMap(x -> (x == 1) ? inner1 : inner2);
            final var resultList = result.toCollection(ArrayList::new);
            assertThat(resultList).containsExactly("A1", "A2", "B1");

            // Verify inner sequences are unchanged
            final var inner1List = inner1.toCollection(ArrayList::new);
            assertThat(inner1List).containsExactly("A1", "A2");
            final var inner2List = inner2.toCollection(ArrayList::new);
            assertThat(inner2List).containsExactly("B1");
            final var sourceList = source.toCollection(ArrayList::new);
            assertThat(sourceList).containsExactly(1, 2);
        }

        @Test
        void flatMap_with_mapped_inner_sequences_preserves_inner_spools() {
            LOGGER.info("flatMap() should preserve spools of mapped inner sequences");
            final Sequence<Integer> source = sequenceOf(1, 2);
            final Sequence<String> result = source.flatMap(x ->
                sequenceOf("item" + x).map(String::toUpperCase)
            );

            final var list = result.toCollection(ArrayList::new);
            assertThat(list).containsExactly("ITEM1", "ITEM2");
        }
    }

    @Nested
    @DisplayName("Behaviour (foldLeft)")
    class Behaviour_FoldLeft {

        @Test
        void foldLeft_with_null_init_throws() {
            LOGGER.info("foldLeft(null, fn) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.foldLeft(null, Integer::sum))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");
        }

        @Test
        void foldLeft_with_null_function_throws() {
            LOGGER.info("foldLeft(init, null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.foldLeft(0, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("function");
        }

        @Test
        void foldLeft_on_empty_sequence_returns_init() {
            LOGGER.info("foldLeft() on empty sequence should return initial value");
            final Sequence<Integer> emptySeq = sequence();
            final Integer result = emptySeq.foldLeft(42, Integer::sum);
            assertThat(result).isEqualTo(42);
        }

        @Test
        void foldLeft_on_single_element() {
            LOGGER.info("foldLeft() on single element should apply function once");
            final Sequence<Integer> seq = sequenceOf(5);
            final Integer result = seq.foldLeft(10, (acc, x) -> acc - x);
            assertThat(result).isEqualTo(5);
        }

        @Test
        void foldLeft_folds_left_associative() {
            LOGGER.info("foldLeft() should fold from left to right: ((0 - 1) - 2) - 3 = -6");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Integer result = seq.foldLeft(0, (acc, x) -> acc - x);
            assertThat(result).isEqualTo(-6);
        }

        @Test
        void foldLeft_with_multi_segments() {
            LOGGER.info("foldLeft() should fold across multiple segments");
            final Sequence<String> seq = sequenceOf("b", "c").cons("a").append("d");
            final String result = seq.foldLeft("", (acc, x) -> acc + x);
            assertThat(result).isEqualTo("abcd");
        }

        @Test
        void foldLeft_with_function_returning_null_throws() {
            LOGGER.info("foldLeft() should throw NullPointerException when function returns null");
            final Sequence<String> seq = sequenceOf("a", "b");
            assertThatThrownBy(() -> seq.foldLeft("", (acc, x) -> null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (foldRight)")
    class Behaviour_FoldRight {

        @Test
        void foldRight_with_null_init_throws() {
            LOGGER.info("foldRight(null, fn) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.foldRight(null, Integer::sum))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");
        }

        @Test
        void foldRight_with_null_function_throws() {
            LOGGER.info("foldRight(init, null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.foldRight(0, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("function");
        }

        @Test
        void foldRight_on_empty_sequence_returns_init() {
            LOGGER.info("foldRight() on empty sequence should return initial value");
            final Sequence<Integer> emptySeq = sequence();
            final Integer result = emptySeq.foldRight(42, Integer::sum);
            assertThat(result).isEqualTo(42);
        }

        @Test
        void foldRight_on_single_element() {
            LOGGER.info("foldRight() on single element should apply function once");
            final Sequence<Integer> seq = sequenceOf(5);
            final Integer result = seq.foldRight(10, (x, acc) -> x - acc);
            assertThat(result).isEqualTo(-5);
        }

        @Test
        void foldRight_folds_right_associative() {
            LOGGER.info("foldRight() should fold from right to left: 1 - (2 - (3 - 0)) = 2");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Integer result = seq.foldRight(0, (x, acc) -> x - acc);
            assertThat(result).isEqualTo(2);
        }

        @Test
        void foldRight_with_multi_segments() {
            LOGGER.info("foldRight() should fold across multiple segments");
            final Sequence<String> seq = sequenceOf("b", "c").cons("a").append("d");
            final String result = seq.foldRight("", (x, acc) -> x + acc);
            assertThat(result).isEqualTo("abcd");
        }

        @Test
        void foldRight_with_function_returning_null_throws() {
            LOGGER.info("foldRight() should throw NullPointerException when function returns null");
            final Sequence<String> seq = sequenceOf("a", "b");
            assertThatThrownBy(() -> seq.foldRight("", (x, acc) -> null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (collectToSequence)")
    class Behaviour_CollectToSequence {

        @Test
        void collect_from_stream_creates_sequence() {
            LOGGER.info("collect(collectToSequence()) should collect stream elements into sequence");
            final Sequence<String> seq = Stream.of("alpha", "beta", "gamma")
                .collect(Sequence.collectToSequence());
            final List<String> list = seq.toCollection(ArrayList::new);
            assertThat(list)
                .containsExactly("alpha", "beta", "gamma");
        }

        @Test
        void collect_from_empty_stream_creates_empty_sequence() {
            LOGGER.info("collect(collectToSequence()) on empty stream should return empty sequence");
            final Sequence<String> seq = Stream.<String>empty()
                .collect(Sequence.collectToSequence());
            assertThat(seq.isEmpty()).isTrue();
        }

        @Test
        void collect_with_null_elements_throws() {
            LOGGER.info("collect(collectToSequence()) with null element in stream should throw NullPointerException");
            assertThatThrownBy(() -> Stream.of("a", null, "b").collect(Sequence.collectToSequence()))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (Iterable and Streamable)")
    class Behaviour_Iterable_And_Streamable {

        @Test
        void iterator_on_empty_sequence() {
            LOGGER.info("iterator() on empty sequence should have no next element");
            final Sequence<Integer> seq = sequence();
            assertThat(seq.iterator().hasNext()).isFalse();
        }

        @Test
        void iterator_in_enhanced_for_loop() {
            LOGGER.info("Enhanced for-loop should iterate over sequence elements in order");
            final Sequence<String> seq = sequenceOf("a", "b", "c");
            final List<String> collected = new ArrayList<>();
            for (final String s : seq) {
                collected.add(s);
            }
            assertThat(collected).containsExactly("a", "b", "c");
        }

        @Test
        void stream_allows_standard_stream_operations() {
            LOGGER.info("stream() should return sequential stream of sequence elements");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3, 4, 5);
            final List<Integer> filtered = seq.stream()
                .filter(x -> x % 2 != 0)
                .map(x -> x * 10)
                .toList();
            assertThat(filtered).containsExactly(10, 30, 50);
        }
    }

    @Nested
    @DisplayName("Behaviour (Iteration From Left and Right)")
    class Behaviour_Iteration {

        @Test
        void iterateOverAllElementsFromLeft_null_consumer_throws() {
            LOGGER.info("iterateOverAllElementsFromLeft(null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.iterateOverAllElementsFromLeft(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("consumer");
        }

        @Test
        void iterateOverAllElementsFromRight_null_consumer_throws() {
            LOGGER.info("iterateOverAllElementsFromRight(null) should throw NullPointerException");
            final Sequence<Integer> seq = sequenceOf(1, 2);
            assertThatThrownBy(() -> seq.iterateOverAllElementsFromRight(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("consumer");
        }

        @Test
        void iterateOverAllElementsFromLeft_visits_in_order() {
            LOGGER.info("iterateOverAllElementsFromLeft() should visit all elements from left to right");
            final Sequence<String> seq = sequenceOf("b", "c").cons("a").append("d");
            final List<String> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromLeft(visited::add);
            assertThat(visited).containsExactly("a", "b", "c", "d");
        }

        @Test
        void iterateOverAllElementsFromRight_visits_in_reverse_order() {
            LOGGER.info("iterateOverAllElementsFromRight() should visit all elements from right to left");
            final Sequence<String> seq = sequenceOf("b", "c").cons("a").append("d");
            final List<String> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromRight(visited::add);
            assertThat(visited).containsExactly("d", "c", "b", "a");
        }

        @Test
        void iterateOverAllElements_on_empty_sequence_does_nothing() {
            LOGGER.info("iterateOverAllElementsFromLeft / Right on empty sequence should not invoke consumer");
            final Sequence<Integer> empty = sequence();
            final List<Integer> visitedLeft = new ArrayList<>();
            final List<Integer> visitedRight = new ArrayList<>();
            empty.iterateOverAllElementsFromLeft(visitedLeft::add);
            empty.iterateOverAllElementsFromRight(visitedRight::add);
            assertThat(visitedLeft).isEmpty();
            assertThat(visitedRight).isEmpty();
        }

        @Test
        void iterateOverAllElementsFromRight_applies_spool() {
            LOGGER.info("iterateOverAllElementsFromRight() should apply mapped spool transformations");
            final Sequence<Integer> seq = sequenceOf("1", "2", "3")
                .map(Integer::parseInt);
            final List<Integer> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromRight(visited::add);
            assertThat(visited).containsExactly(3, 2, 1);
        }

        @Test
        void iterateOverAllElementsFromRight_after_tail() {
            LOGGER.info("iterateOverAllElementsFromRight() after tail() should only visit remaining elements in reverse");
            final Sequence<String> seq = sequenceOf("a", "b", "c", "d").tail();
            final List<String> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromRight(visited::add);
            assertThat(visited).containsExactly("d", "c", "b");
        }

        @Test
        void iterateOverAllElementsFromRight_after_multiple_cons_and_append() {
            LOGGER.info("iterateOverAllElementsFromRight() with multiple cons and append calls");
            final Sequence<String> seq = sequenceOf("c", "d")
                .cons("b")
                .cons("a")
                .append("e")
                .append("f");
            final List<String> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromRight(visited::add);
            assertThat(visited).containsExactly("f", "e", "d", "c", "b", "a");
        }

        @Test
        void iterateOverAllElementsFromRight_after_flatMap_and_applyTo() {
            LOGGER.info("iterateOverAllElementsFromRight() after flatMap and applyTo");
            final Sequence<Integer> seq = sequenceOf(1, 2)
                .flatMap(x -> sequenceOf(x, x * 10));
            final List<Integer> visited = new ArrayList<>();
            seq.iterateOverAllElementsFromRight(visited::add);
            assertThat(visited).containsExactly(20, 2, 10, 1);
        }
    }

    @Nested
    @DisplayName("Merge")
    class Merge {

        @Test
        void merge_concatenates_two_sequences_in_order() {
            LOGGER.info("merge concatenates two non-empty sequences");
            final Sequence<String> first = sequenceOf("a", "b");
            final Sequence<String> second = sequenceOf("c", "d");

            final Sequence<String> merged = first.merge(second);
            final List<String> list = merged.toCollection(ArrayList::new);
            assertThat(list).containsExactly("a", "b", "c", "d");
        }

        @Test
        void merge_with_empty_sequences() {
            LOGGER.info("merge with empty sequences returns the non-empty sequence or empty");
            final Sequence<Integer> seq = sequenceOf(1, 2, 3);
            final Sequence<Integer> empty = sequence();

            final Sequence<Integer> merged1 = seq.merge(empty);
            final List<Integer> list1 = merged1.toCollection(ArrayList::new);
            assertThat(list1).containsExactly(1, 2, 3);

            final Sequence<Integer> merged2 = empty.merge(seq);
            final List<Integer> list2 = merged2.toCollection(ArrayList::new);
            assertThat(list2).containsExactly(1, 2, 3);

            final Sequence<Integer> merged3 = empty.merge(empty);
            assertThat(merged3.isNotEmpty()).isFalse();
        }

        @Test
        void merge_multi_segment_sequences() {
            LOGGER.info("merge correctly combines sequences with multiple segments");
            final Sequence<String> first = sequenceOf("b", "c").cons("a").append("d");
            final Sequence<String> second = sequenceOf("f", "g").cons("e").append("h");

            final Sequence<String> merged = first.merge(second);
            final List<String> list = merged.toCollection(ArrayList::new);
            assertThat(list).containsExactly("a", "b", "c", "d", "e", "f", "g", "h");
        }

        @Test
        void merge_preserves_lazy_spools() {
            LOGGER.info("merge preserves lazy evaluation and only evaluates when consumed");
            final java.util.concurrent.atomic.AtomicInteger firstCount = new java.util.concurrent.atomic.AtomicInteger(0);
            final java.util.concurrent.atomic.AtomicInteger secondCount = new java.util.concurrent.atomic.AtomicInteger(0);

            final Sequence<Integer> first = sequenceOf(1, 2).map(x -> {
                firstCount.incrementAndGet();
                return x * 10;
            });
            final Sequence<Integer> second = sequenceOf(3, 4).map(x -> {
                secondCount.incrementAndGet();
                return x * 100;
            });

            final Sequence<Integer> merged = first.merge(second);
            assertThat(firstCount.get()).isEqualTo(0);
            assertThat(secondCount.get()).isEqualTo(0);

            assertThat(merged.head()).isEqualTo(10);
            assertThat(firstCount.get()).isEqualTo(1);
            assertThat(secondCount.get()).isEqualTo(0);

            final List<Integer> list = merged.toCollection(ArrayList::new);
            assertThat(list).containsExactly(10, 20, 300, 400);
            assertThat(firstCount.get()).isEqualTo(3);
            assertThat(secondCount.get()).isEqualTo(2);
        }

        @Test
        void merge_null_check() {
            LOGGER.info("merge(null) throws NullPointerException");
            final Sequence<String> seq = sequenceOf("a");
            assertThatThrownBy(() -> seq.merge(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("other");
        }
    }
}
