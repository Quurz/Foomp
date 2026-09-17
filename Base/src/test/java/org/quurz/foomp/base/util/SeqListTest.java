package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.higher.Higher1;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.SeqList.collectToSeqList;
import static org.quurz.foomp.base.util.SeqList.seqList;
import static org.quurz.foomp.base.util.SeqList.seqListFrom;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
@DisplayName("SeqList")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "DataFlowIssue"})
class SeqListTest
        extends TestHelper {

    private static final Logger LOGGER = getLogger(SeqListTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void seqList_returns_empty_seqList() {
            LOGGER.info("SeqList.seqList() should return an empty SeqList");
            final SeqList<String> list = seqList();
            assertThat(list).isNotNull();
            assertThat(list.isNotEmpty()).isFalse();
            assertThat(list.isEmpty()).isTrue();
            assertThat(list.size()).isZero();
        }

        @Test
        void seqList_with_null_array_throws() {
            LOGGER.info("SeqList.seqList((String[]) null) should throw NullPointerException");
            assertThatThrownBy(() -> seqList((String[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("elements");
        }

        @Test
        void seqList_with_zero_arguments_returns_empty_seqList() {
            LOGGER.info("SeqList.seqList() without args should return empty SeqList");
            final SeqList<String> list = seqList();
            assertThat(list).isNotNull();
            assertThat(list.isNotEmpty()).isFalse();
        }

        @Test
        void seqList_with_null_element_throws() {
            LOGGER.info("SeqList.seqList(...) containing null should throw NullPointerException");
            assertThatThrownBy(() -> seqList("A", null, "C"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("elements");
        }

        @Test
        void seqList_with_valid_elements_creates_seqList() {
            LOGGER.info("SeqList.seqList(...) should create non-empty SeqList with elements");
            final SeqList<String> list = seqList("A", "B", "C");
            assertThat(list.isNotEmpty()).isTrue();
            assertThat(list.size()).isEqualTo(3);
            assertThat(list.head()).isEqualTo("A");
            assertThat(list.get(1)).isEqualTo("B");
            assertThat(list.get(2)).isEqualTo("C");
        }

        @Test
        void seqListFrom_with_null_collection_throws() {
            LOGGER.info("SeqList.seqListFrom(null) should throw NullPointerException");
            assertThatThrownBy(() -> seqListFrom(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("collection");
        }

        @Test
        void seqListFrom_with_empty_collection_returns_empty_seqList() {
            LOGGER.info("SeqList.seqListFrom(empty) should return empty SeqList");
            final SeqList<String> list = seqListFrom(Collections.emptyList());
            assertThat(list).isNotNull();
            assertThat(list.isNotEmpty()).isFalse();
        }

        @Test
        void seqListFrom_with_null_element_in_collection_throws() {
            LOGGER.info("SeqList.seqListFrom(collectionWithNull) should throw NullPointerException");
            final List<String> listWithNull = Arrays.asList("A", null, "C");
            assertThatThrownBy(() -> seqListFrom(listWithNull))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("collection");
        }

        @Test
        void seqListFrom_with_valid_collection_creates_seqList() {
            LOGGER.info("SeqList.seqListFrom(validList) should create SeqList with elements");
            final SeqList<String> list = seqListFrom(List.of("X", "Y", "Z"));
            assertThat(list.isNotEmpty()).isTrue();
            assertThat(list.size()).isEqualTo(3);
            assertThat(list.head()).isEqualTo("X");
        }
    }

    @Nested
    @DisplayName("Collector")
    class CollectorTest {

        @Test
        void collectToSeqList_collects_stream_elements() {
            LOGGER.info("Stream.collect(collectToSeqList()) should create a populated SeqList");
            final SeqList<String> result = Stream.of("1", "2", "3")
                .collect(collectToSeqList());

            assertThat(result).containsExactly("1", "2", "3");
        }

        @Test
        void collectToSeqList_handles_empty_stream() {
            LOGGER.info("Stream.empty().collect(collectToSeqList()) should return an empty SeqList");
            final SeqList<String> result = Stream.<String>empty()
                .collect(collectToSeqList());

            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void collectToSeqList_handles_parallel_stream() {
            LOGGER.info("Parallel stream collecting to SeqList should preserve combiner merge");
            final SeqList<Integer> result = List.of(1, 2, 3, 4, 5, 6, 7, 8)
                .parallelStream()
                .collect(collectToSeqList());

            assertThat(result).containsExactly(1, 2, 3, 4, 5, 6, 7, 8);
        }
    }

    @Nested
    @DisplayName("Narrow")
    class Narrow {

        @Test
        void narrow_with_null_throws() {
            LOGGER.info("SeqList.narrow(null) should throw NullPointerException");
            assertThatThrownBy(() -> SeqList.narrow(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("wide");
        }

        @Test
        void narrow_with_invalid_witness_throws() {
            LOGGER.info("SeqList.narrow(...) with non-SeqList instance should throw IllegalArgumentException");
            final Higher1<SeqList.µ, String> invalid = new Higher1<>() {};
            assertThatThrownBy(() -> SeqList.narrow(invalid))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void narrow_with_valid_seqList_returns_same_instance() {
            LOGGER.info("SeqList.narrow(seqList) should successfully narrow");
            final SeqList<String> original = seqList("A", "B");
            final Higher1<SeqList.µ, String> wide = original;
            final SeqList<String> narrowed = SeqList.narrow(wide);
            assertThat(narrowed).isSameAs(original);
        }
    }

    @Nested
    @DisplayName("Head & HeadSafe")
    class Head_And_HeadSafe {

        @Test
        void head_on_empty_seqList_throws() {
            LOGGER.info("head() on empty SeqList should throw NoSuchElementException");
            final SeqList<String> list = seqList();
            assertThatThrownBy(list::head)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void head_on_non_empty_seqList_returns_first_element() {
            LOGGER.info("head() on non-empty SeqList should return first element");
            final SeqList<String> list = seqList("First", "Second");
            assertThat(list.head()).isEqualTo("First");
        }

        @Test
        void headSafe_on_empty_seqList_returns_none() {
            LOGGER.info("headSafe() on empty SeqList should return none()");
            final SeqList<String> list = seqList();
            assertThat(list.headSafe().isNone()).isTrue();
        }

        @Test
        void headSafe_on_non_empty_seqList_returns_some_with_value() {
            LOGGER.info("headSafe() on non-empty SeqList should return some(firstElement)");
            final SeqList<String> list = seqList("First", "Second");
            final var maybeHead = list.headSafe();
            assertThat(maybeHead.isSome()).isTrue();
            assertThat(maybeHead.get()).isEqualTo("First");
        }
    }

    @Nested
    @DisplayName("Tail")
    class Tail {

        @Test
        void tail_on_empty_seqList_throws() {
            LOGGER.info("tail() on empty SeqList should throw NoSuchElementException");
            final SeqList<String> list = seqList();
            assertThatThrownBy(list::tail)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void tail_on_single_element_seqList_returns_empty_seqList() {
            LOGGER.info("tail() on single element SeqList should return empty SeqList");
            final SeqList<String> list = seqList("Only");
            final SeqList<String> tail = list.tail();
            assertThat(tail.isNotEmpty()).isFalse();
            assertThat(tail.isEmpty()).isTrue();
        }

        @Test
        void tail_preserves_immutability_of_original_seqList() {
            LOGGER.info("tail() should not modify the original SeqList");
            final SeqList<String> original = seqList("A", "B", "C");
            final SeqList<String> tail1 = original.tail();
            final SeqList<String> tail2 = original.tail();

            assertThat(original.head()).isEqualTo("A");
            assertThat(tail1.head()).isEqualTo("B");
            assertThat(tail2.head()).isEqualTo("B");
            assertThat(tail1).containsExactly("B", "C");
            assertThat(original).containsExactly("A", "B", "C");
        }

        @Test
        void tail_iterates_through_multiple_elements() {
            LOGGER.info("Repeated tail() calls should walk through all elements");
            final SeqList<Integer> list = seqList(1, 2, 3);
            assertThat(list.head()).isEqualTo(1);

            final SeqList<Integer> tail1 = list.tail();
            assertThat(tail1.isNotEmpty()).isTrue();
            assertThat(tail1.head()).isEqualTo(2);

            final SeqList<Integer> tail2 = tail1.tail();
            assertThat(tail2.isNotEmpty()).isTrue();
            assertThat(tail2.head()).isEqualTo(3);

            final SeqList<Integer> tail3 = tail2.tail();
            assertThat(tail3.isNotEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("Cons")
    class Cons {

        @Test
        void cons_with_null_element_throws() {
            LOGGER.info("cons(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.cons(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("element");
        }

        @Test
        void cons_onto_empty_seqList_creates_single_element_seqList() {
            LOGGER.info("cons() onto empty SeqList should return single element SeqList");
            final SeqList<String> list = seqList();
            final SeqList<String> result = list.cons("A");
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.head()).isEqualTo("A");
            assertThat(result).containsExactly("A");
        }

        @Test
        void cons_onto_non_empty_seqList_prepends_element() {
            LOGGER.info("cons() onto non-empty SeqList should prepend element and preserve original");
            final SeqList<String> original = seqList("B", "C");
            final SeqList<String> result = original.cons("A");

            assertThat(result).containsExactly("A", "B", "C");
            assertThat(original).containsExactly("B", "C");
        }
    }

    @Nested
    @DisplayName("Decons")
    class Decons {

        @Test
        void decons_on_empty_seqList_throws() {
            LOGGER.info("decons() on empty SeqList should throw NoSuchElementException");
            final SeqList<String> list = seqList();
            assertThatThrownBy(list::decons)
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void decons_on_single_element_seqList_returns_head_and_empty_tail() {
            LOGGER.info("decons() on single element should return (head, empty)");
            final SeqList<String> list = seqList("Single");
            final Tuple2<String, SeqList<String>> deconstructed = list.decons();

            assertThat(deconstructed.get1()).isEqualTo("Single");
            assertThat(deconstructed.get2().isEmpty()).isTrue();
        }

        @Test
        void decons_on_multiple_elements_returns_head_and_tail() {
            LOGGER.info("decons() on multiple elements should return head and remaining elements");
            final SeqList<String> list = seqList("A", "B", "C");
            final Tuple2<String, SeqList<String>> deconstructed = list.decons();

            assertThat(deconstructed.get1()).isEqualTo("A");
            assertThat(deconstructed.get2()).containsExactly("B", "C");
        }
    }

    @Nested
    @DisplayName("Filter")
    class Filter {

        @Test
        void filter_with_null_predicate_throws() {
            LOGGER.info("filter(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A", "B");
            assertThatThrownBy(() -> list.filter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("pred");
        }

        @Test
        void filter_on_empty_seqList_returns_empty_seqList() {
            LOGGER.info("filter() on empty SeqList should return empty SeqList");
            final SeqList<String> list = seqList();
            final SeqList<String> filtered = list.filter(s -> true);
            assertThat(filtered.isEmpty()).isTrue();
        }

        @Test
        void filter_matching_all_elements_returns_all_elements() {
            LOGGER.info("filter() matching all should retain all elements");
            final SeqList<Integer> list = seqList(1, 2, 3);
            final SeqList<Integer> filtered = list.filter(x -> x > 0);
            assertThat(filtered).containsExactly(1, 2, 3);
        }

        @Test
        void filter_matching_no_elements_returns_empty_seqList() {
            LOGGER.info("filter() matching none should return empty SeqList");
            final SeqList<Integer> list = seqList(1, 2, 3);
            final SeqList<Integer> filtered = list.filter(x -> x > 10);
            assertThat(filtered.isEmpty()).isTrue();
        }

        @Test
        void filter_matching_subset_returns_matching_elements() {
            LOGGER.info("filter() matching subset should return filtered elements");
            final SeqList<Integer> list = seqList(1, 2, 3, 4, 5, 6);
            final SeqList<Integer> filtered = list.filter(x -> x % 2 == 0);
            assertThat(filtered).containsExactly(2, 4, 6);
        }
    }

    @Nested
    @DisplayName("Partition")
    class Partition {

        @Test
        void partition_with_null_predicate_throws() {
            LOGGER.info("partition(null) should throw NullPointerException with 'pred'");
            final SeqList<String> list = seqList("A", "B");
            assertThatThrownBy(() -> list.partition(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("pred");
        }

        @Test
        void partition_on_empty_seqList_returns_two_empty_seqLists() {
            LOGGER.info("partition() on empty SeqList should return two empty lists");
            final SeqList<String> list = seqList();
            final var result = list.partition(s -> true);
            assertThat(result.get1().isEmpty()).isTrue();
            assertThat(result.get2().isEmpty()).isTrue();
        }

        @Test
        void partition_with_mixed_elements_splits_correctly() {
            LOGGER.info("partition() should separate matching and non-matching elements");
            final SeqList<Integer> list = seqList(1, 2, 3, 4, 5);
            final var result = list.partition(x -> x % 2 != 0);

            assertThat(result.get1()).containsExactly(1, 3, 5);
            final var nonMatching = new ArrayList<Integer>();
            result.get2().toCollection(() -> nonMatching);
            assertThat(nonMatching).containsExactly(2, 4);
        }

        @Test
        void partition_all_matching_returns_all_in_first_and_empty_in_second() {
            LOGGER.info("partition() where all match should populate first component only");
            final SeqList<Integer> list = seqList(2, 4, 6);
            final var result = list.partition(x -> x % 2 == 0);

            assertThat(result.get1()).containsExactly(2, 4, 6);
            assertThat(result.get2().isNotEmpty()).isFalse();
        }

        @Test
        void partition_none_matching_returns_empty_in_first_and_all_in_second() {
            LOGGER.info("partition() where none match should populate second component only");
            final SeqList<Integer> list = seqList(1, 3, 5);
            final var result = list.partition(x -> x % 2 == 0);

            assertThat(result.get1().isEmpty()).isTrue();
            final var nonMatching = new ArrayList<Integer>();
            result.get2().toCollection(() -> nonMatching);
            assertThat(nonMatching).containsExactly(1, 3, 5);
        }
    }

    @Nested
    @DisplayName("Span")
    class Span {

        @Test
        void span_with_null_predicate_throws() {
            LOGGER.info("span(null) should throw NullPointerException with 'pred'");
            final SeqList<String> list = seqList("A", "B");
            assertThatThrownBy(() -> list.span(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("pred");
        }

        @Test
        void span_on_empty_seqList_returns_two_empty_seqLists() {
            LOGGER.info("span() on empty SeqList should return two empty lists");
            final SeqList<String> list = seqList();
            final var result = list.span(s -> true);
            assertThat(result.get1().isEmpty()).isTrue();
            assertThat(result.get2().isEmpty()).isTrue();
        }

        @Test
        void span_splits_at_first_false_and_keeps_rest_in_second() {
            LOGGER.info("span() should split at first false and not add later matches to prefix");
            final SeqList<Integer> list = seqList(1, 3, 5, 4, 7, 9);
            final var result = list.span(x -> x % 2 != 0);

            assertThat(result.get1()).containsExactly(1, 3, 5);
            assertThat(result.get2()).containsExactly(4, 7, 9);
        }

        @Test
        void span_all_matching_returns_all_in_first_and_empty_in_second() {
            LOGGER.info("span() where all match should have empty second list");
            final SeqList<Integer> list = seqList(1, 3, 5);
            final var result = list.span(x -> x % 2 != 0);

            assertThat(result.get1()).containsExactly(1, 3, 5);
            assertThat(result.get2().isEmpty()).isTrue();
        }

        @Test
        void span_first_element_fails_returns_empty_first_and_all_in_second() {
            LOGGER.info("span() where first fails should have empty first list");
            final SeqList<Integer> list = seqList(2, 4, 6);
            final var result = list.span(x -> x % 2 != 0);

            assertThat(result.get1().isEmpty()).isTrue();
            assertThat(result.get2()).containsExactly(2, 4, 6);
        }
    }

    @Nested
    @DisplayName("ToCollection")
    class ToCollection {

        @Test
        void toCollection_with_null_supplier_throws() {
            LOGGER.info("toCollection(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.toCollection(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");
        }

        @Test
        void toCollection_when_supplier_returns_null_throws() {
            LOGGER.info("toCollection(() -> null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.toCollection(() -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void toCollection_populates_target_collection() {
            LOGGER.info("toCollection should append all elements into supplied collection");
            final SeqList<String> list = seqList("A", "B", "C");
            final ArrayList<String> target = list.toCollection(ArrayList::new);
            assertThat(target).containsExactly("A", "B", "C");

            final HashSet<String> set = list.toCollection(HashSet::new);
            assertThat(set).containsExactlyInAnyOrder("A", "B", "C");
        }
    }

    @Nested
    @DisplayName("Merge & AppendAll & PrependAll")
    class Merge_And_AppendAll_PrependAll {

        @Test
        void merge_with_null_throws() {
            LOGGER.info("merge(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.merge(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("other");
        }

        @Test
        void merge_two_seqLists_appends_other_behind_this() {
            LOGGER.info("merge() should concatenate two SeqLists in order");
            final SeqList<String> list1 = seqList("A", "B");
            final SeqList<String> list2 = seqList("C", "D");
            final SeqList<String> merged = list1.merge(list2);

            assertThat(merged).containsExactly("A", "B", "C", "D");
            assertThat(list1).containsExactly("A", "B");
            assertThat(list2).containsExactly("C", "D");
        }

        @Test
        void merge_with_empty_seqLists() {
            LOGGER.info("merge() with empty lists should behave correctly");
            final SeqList<String> empty = seqList();
            final SeqList<String> nonNullList = seqList("A", "B");

            assertThat(empty.merge(nonNullList)).containsExactly("A", "B");
            assertThat(nonNullList.merge(empty)).containsExactly("A", "B");
            assertThat(empty.merge(empty).isEmpty()).isTrue();
        }

        @Test
        void appendAll_and_prependAll_default_methods() {
            LOGGER.info("appendAll and prependAll should delegate properly to merge");
            final SeqList<String> list1 = seqList("1", "2");
            final SeqList<String> list2 = seqList("3", "4");

            final Seq<String> appended = list1.appendAll(list2);
            final Seq<String> prepended = list1.prependAll(list2);

            final var appendedList = new ArrayList<String>();
            appended.toCollection(() -> appendedList);
            assertThat(appendedList).containsExactly("1", "2", "3", "4");

            final var prependedList = new ArrayList<String>();
            prepended.toCollection(() -> prependedList);
            assertThat(prependedList).containsExactly("3", "4", "1", "2");
        }

        @Test
        void merge_with_custom_seq_implementation() {
            LOGGER.info("merge() should support merging with custom Seq implementations");
            final SeqList<String> list1 = seqList("A", "B");
            final Seq<String> customSeq = new CustomTestSeq<>(List.of("C", "D"));

            final SeqList<String> merged = list1.merge(customSeq);
            assertThat(merged).containsExactly("A", "B", "C", "D");
        }
    }

    @Nested
    @DisplayName("Map")
    class Map {

        @Test
        void map_with_null_function_throws() {
            LOGGER.info("map(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.map(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void map_on_empty_seqList_returns_empty_seqList() {
            LOGGER.info("map() on empty SeqList should return empty SeqList");
            final SeqList<String> list = seqList();
            final SeqList<Integer> mapped = list.map(String::length);
            assertThat(mapped.isEmpty()).isTrue();
        }

        @Test
        void map_when_transformation_returns_null_throws() {
            LOGGER.info("map() throwing NullPointerException if transformation returns null");
            final SeqList<String> list = seqList("A", "B");
            assertThatThrownBy(() -> list.map(s -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void map_transforms_all_elements_eagerly() {
            LOGGER.info("map() should eagerly apply transformation to all elements");
            final SeqList<String> list = seqList("one", "two", "three");
            final SeqList<Integer> mapped = list.map(String::length);

            assertThat(mapped).containsExactly(3, 3, 5);
        }
    }

    @Nested
    @DisplayName("ApplyTo")
    class ApplyTo {

        @Test
        void applyTo_with_null_transformation_throws() {
            LOGGER.info("applyTo(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.applyTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void applyTo_with_empty_seqList_returns_empty_seqList() {
            LOGGER.info("applyTo() with empty data list should return empty");
            final SeqList<Integer> list = seqList();
            final SeqList<Function<Integer, Integer>> fns = seqList(x -> x * 2);
            final SeqList<Integer> result = list.applyTo(fns);

            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void applyTo_with_empty_functions_returns_empty_seqList() {
            LOGGER.info("applyTo() with empty function sequence should return empty");
            final SeqList<Integer> list = seqList(1, 2, 3);
            final SeqList<Function<Integer, Integer>> fns = seqList();
            final SeqList<Integer> result = list.applyTo(fns);

            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void applyTo_applies_cartesian_product_in_order() {
            LOGGER.info("applyTo() should apply each function to all elements in order");
            final SeqList<Integer> list = seqList(1, 2, 3);
            final SeqList<Function<Integer, Integer>> fns = seqList(
                x -> x * 10,
                x -> x + 100
            );

            final SeqList<Integer> result = list.applyTo(fns);
            assertThat(result).containsExactly(10, 20, 30, 101, 102, 103);
        }

        @Test
        void applyTo_with_custom_seq_witness() {
            LOGGER.info("applyTo() should work with custom Seq witness types");
            final SeqList<String> list = seqList("a", "b");
            final CustomTestSeq<Function<String, String>> customSeqFns = new CustomTestSeq<>(
                List.of(String::toUpperCase, s -> s + "!")
            );

            final SeqList<String> result = list.applyTo(customSeqFns);
            assertThat(result).containsExactly("A", "B", "a!", "b!");
        }

        @Test
        void applyTo_when_function_returns_null_throws() {
            LOGGER.info("applyTo() should throw NullPointerException if any function returns null");
            final SeqList<String> list = seqList("a");
            final SeqList<Function<String, String>> fns = seqList(s -> null);

            assertThatThrownBy(() -> list.applyTo(fns))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("FlatMap")
    class FlatMap {

        @Test
        void flatMap_with_null_transformation_throws() {
            LOGGER.info("flatMap(null) should throw NullPointerException");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.flatMap(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transformation");
        }

        @Test
        void flatMap_on_empty_seqList_returns_empty_seqList() {
            LOGGER.info("flatMap() on empty list should return empty");
            final SeqList<String> list = seqList();
            final SeqList<String> result = list.flatMap(s -> seqList(s, s));
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void flatMap_when_transformation_returns_null_throws() {
            LOGGER.info("flatMap() should throw NullPointerException if transformation returns null");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.flatMap(s -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void flatMap_flattens_generated_sequences() {
            LOGGER.info("flatMap() should flatten all generated SeqLists in order");
            final SeqList<Integer> list = seqList(1, 2, 3);
            final SeqList<Integer> result = list.flatMap(x -> seqList(x, x * 10));

            assertThat(result).containsExactly(1, 10, 2, 20, 3, 30);
        }

        @Test
        void flatMap_with_custom_seq_returned() {
            LOGGER.info("flatMap() should accept any Seq implementation from transformation");
            final SeqList<String> list = seqList("X", "Y");
            final SeqList<String> result = list.flatMap(s -> new CustomTestSeq<>(List.of(s + "1", s + "2")));

            assertThat(result).containsExactly("X1", "X2", "Y1", "Y2");
        }
    }

    @Nested
    @DisplayName("FoldLeft & FoldRight")
    class FoldLeft_And_FoldRight {

        @Test
        void foldLeft_with_null_arguments_throws() {
            LOGGER.info("foldLeft() should validate arguments for null");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.foldLeft(null, (acc, x) -> acc + x))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");

            assertThatThrownBy(() -> list.foldLeft("", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("function");
        }

        @Test
        void foldLeft_when_function_produces_null_throws() {
            LOGGER.info("foldLeft() should throw NullPointerException if function produces null");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.foldLeft("", (acc, x) -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void foldLeft_on_empty_returns_init() {
            LOGGER.info("foldLeft() on empty SeqList should return init value");
            final SeqList<String> list = seqList();
            final String result = list.foldLeft("init", (acc, x) -> acc + x);
            assertThat(result).isEqualTo("init");
        }

        @Test
        void foldLeft_accumulates_left_associative() {
            LOGGER.info("foldLeft() should accumulate in left-associative order");
            final SeqList<String> list = seqList("A", "B", "C");
            final String result = list.foldLeft("0", (acc, el) -> "(" + acc + "+" + el + ")");
            assertThat(result).isEqualTo("(((0+A)+B)+C)");
        }

        @Test
        void foldRight_with_null_arguments_throws() {
            LOGGER.info("foldRight() should validate arguments for null");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.foldRight(null, (x, acc) -> x + acc))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("init");

            assertThatThrownBy(() -> list.foldRight("", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("function");
        }

        @Test
        void foldRight_when_function_produces_null_throws() {
            LOGGER.info("foldRight() should throw NullPointerException if function produces null");
            final SeqList<String> list = seqList("A");
            assertThatThrownBy(() -> list.foldRight("", (x, acc) -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void foldRight_on_empty_returns_init() {
            LOGGER.info("foldRight() on empty SeqList should return init value");
            final SeqList<String> list = seqList();
            final String result = list.foldRight("init", (x, acc) -> x + acc);
            assertThat(result).isEqualTo("init");
        }

        @Test
        void foldRight_accumulates_right_associative() {
            LOGGER.info("foldRight() should accumulate in right-associative order");
            final SeqList<String> list = seqList("A", "B", "C");
            final String result = list.foldRight("0", (el, acc) -> "(" + el + "+" + acc + ")");
            assertThat(result).isEqualTo("(A+(B+(C+0)))");
        }
    }

    @Nested
    @DisplayName("Stream & Iterator")
    class Stream_And_Iterator {

        @Test
        void stream_returns_all_elements_in_encounter_order() {
            LOGGER.info("stream() should yield all elements in order");
            final SeqList<String> list = seqList("Alpha", "Beta", "Gamma");
            final List<String> result = list.stream().toList();
            assertThat(result).containsExactly("Alpha", "Beta", "Gamma");
        }

        @Test
        void iterator_traverses_elements_in_proper_sequence() {
            LOGGER.info("iterator() should traverse all elements");
            final SeqList<Integer> list = seqList(10, 20, 30);
            final Iterator<Integer> iterator = list.iterator();

            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo(10);
            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo(20);
            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo(30);
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        void iterator_remove_throws_UnsupportedOperationException() {
            LOGGER.info("iterator().remove() should throw UnsupportedOperationException");
            final SeqList<String> list = seqList("A", "B");
            final Iterator<String> iterator = list.iterator();
            iterator.next();
            assertThatThrownBy(iterator::remove)
                .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("Get & Size")
    class Get_And_Size {

        @Test
        void size_returns_number_of_elements() {
            LOGGER.info("size() should accurately report element count");
            assertThat(seqList().size()).isZero();
            assertThat(seqList("A").size()).isEqualTo(1);
            assertThat(seqList("A", "B", "C").size()).isEqualTo(3);
        }

        @Test
        void get_returns_element_at_index() {
            LOGGER.info("get(i) should return element at valid index");
            final SeqList<String> list = seqList("First", "Second", "Third");
            assertThat(list.get(0)).isEqualTo("First");
            assertThat(list.get(1)).isEqualTo("Second");
            assertThat(list.get(2)).isEqualTo("Third");
        }

        @Test
        void get_with_invalid_index_throws_IndexOutOfBoundsException() {
            LOGGER.info("get(invalidIndex) should throw IndexOutOfBoundsException");
            final SeqList<String> list = seqList("A", "B");

            assertThatThrownBy(() -> list.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> list.get(2))
                .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> seqList().get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Java List Contract & Immutability")
    class List_Contract_And_Immutability {

        @Test
        void mutating_methods_throw_UnsupportedOperationException() {
            LOGGER.info("Mutating List methods must throw UnsupportedOperationException");
            final SeqList<String> list = seqList("A", "B");

            assertThatThrownBy(() -> list.add("C"))
                .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> list.add(0, "C"))
                .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> list.remove("A"))
                .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> list.remove(0))
                .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> list.set(0, "Z"))
                .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(list::clear)
                .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        void contains_and_indexOf_behave_as_standard_list() {
            LOGGER.info("contains, indexOf, lastIndexOf should work as expected");
            final SeqList<String> list = seqList("A", "B", "A", "C");

            assertThat(list.contains("B")).isTrue();
            assertThat(list.contains("Z")).isFalse();
            assertThat(list.indexOf("A")).isEqualTo(0);
            assertThat(list.lastIndexOf("A")).isEqualTo(2);
            assertThat(list.indexOf("Unknown")).isEqualTo(-1);
        }

        @Test
        void subList_returns_unmodifiable_sublist_view() {
            LOGGER.info("subList() should return view of elements");
            final SeqList<String> list = seqList("A", "B", "C", "D");
            final List<String> sub = list.subList(1, 3);

            assertThat(sub).containsExactly("B", "C");
        }

        @Test
        void equals_and_hashCode_comply_with_list_contract() {
            LOGGER.info("equals and hashCode should match standard java.util.List contract");
            final SeqList<String> seqList = seqList("A", "B", "C");
            final List<String> javaList = List.of("A", "B", "C");

            assertThat(seqList).isEqualTo(javaList);
            assertThat(seqList.hashCode()).isEqualTo(javaList.hashCode());
        }

        @Test
        void toArray_returns_elements_array() {
            LOGGER.info("toArray() should return object array with all elements");
            final SeqList<String> list = seqList("A", "B");
            final Object[] array = list.toArray();
            assertThat(array).containsExactly("A", "B");

            final String[] typedArray = list.toArray(new String[0]);
            assertThat(typedArray).containsExactly("A", "B");
        }
    }

    /**
     * Minimal custom Seq implementation for testing interoperability.
     */
    private static final class CustomTestSeq<A> implements Seq<A>, Higher1<Seq.µ, A> {
        private final List<A> items;

        CustomTestSeq(final List<A> items) {
            this.items = items;
        }

        @Override
        public boolean isNotEmpty() {
            return !this.items.isEmpty();
        }

        @Override
        public @NonNull A head() throws NoSuchElementException {
            if (this.items.isEmpty()) throw new NoSuchElementException();
            return this.items.get(0);
        }

        @Override
        public @NonNull Value<A> headSafe() {
            return this.items.isEmpty() ? Maybe.none() : Maybe.some(this.items.get(0));
        }

        @Override
        public @NonNull Seq<A> tail() throws NoSuchElementException {
            if (this.items.isEmpty()) throw new NoSuchElementException();
            return new CustomTestSeq<>(this.items.subList(1, this.items.size()));
        }

        @Override
        public @NonNull Seq<A> cons(final @NonNull A element) {
            final var list = new ArrayList<A>();
            list.add(element);
            list.addAll(this.items);
            return new CustomTestSeq<>(list);
        }

        @Override
        public @NonNull Value2<A, ? extends Seq<A>> decons() throws NoSuchElementException {
            return Tuple2.tuple2(this.head(), this.tail());
        }

        @Override
        public @NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred) {
            return new CustomTestSeq<>(this.items.stream().filter(pred).toList());
        }

        @Override
        public @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> partition(final @NonNull Predicate<? super A> pred) {
            final var match = new ArrayList<A>();
            final var nonMatch = new ArrayList<A>();
            for (final var item : this.items) {
                if (pred.test(item)) match.add(item);
                else nonMatch.add(item);
            }
            return Tuple2.tuple2(new CustomTestSeq<>(match), new CustomTestSeq<>(nonMatch));
        }

        @Override
        public @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> span(final @NonNull Predicate<? super A> pred) {
            final var prefix = new ArrayList<A>();
            final var remainder = new ArrayList<A>();
            var taking = true;
            for (final var item : this.items) {
                if (taking && pred.test(item)) {
                    prefix.add(item);
                } else {
                    taking = false;
                    remainder.add(item);
                }
            }
            return Tuple2.tuple2(new CustomTestSeq<>(prefix), new CustomTestSeq<>(remainder));
        }

        @Override
        public @NonNull <C extends Collection<? super A>> C toCollection(final @NonNull Supplier<C> init) {
            final var c = init.get();
            c.addAll(this.items);
            return c;
        }

        @Override
        public @NonNull Seq<A> merge(final @NonNull Seq<A> other) {
            final var merged = new ArrayList<A>(this.items);
            other.toCollection(() -> merged);
            return new CustomTestSeq<>(merged);
        }
    }
}
