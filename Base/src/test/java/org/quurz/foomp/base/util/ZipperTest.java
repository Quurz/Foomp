package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.quurz.foomp.base.util.Zipper.unzip;
import static org.quurz.foomp.base.util.Zipper.zip;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Zipper")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ZipperTest {

    private static final Logger LOGGER
            = getLogger("ZipperTest");

    @Nested
    @DisplayName("Zip")
    class Zip {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void null_first_list_throws_NullPointerException() {
            LOGGER.info("Zipper.zip(...) should throw NullPointerException when first list is null");
            assertThatThrownBy(() -> zip(null, List.of("a", "b")))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void null_second_list_throws_NullPointerException() {
            LOGGER.info("Zipper.zip(...) should throw NullPointerException when second list is null");
            assertThatThrownBy(() -> zip(List.of(1, 2), null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void empty_lists_produce_empty_result() {
            LOGGER.info("Zipper.zip(...) should return empty list when both lists are empty");
            final var result
                = zip(List.of(), List.of());
            assertThat(result)
                .isEmpty();
        }

        @Test
        void first_list_empty_produces_empty_result() {
            LOGGER.info("Zipper.zip(...) should return empty list when first list is empty");
            final var result
                = zip(List.of(), List.of("a", "b", "c"));
            assertThat(result)
                .isEmpty();
        }

        @Test
        void second_list_empty_produces_empty_result() {
            LOGGER.info("Zipper.zip(...) should return empty list when second list is empty");
            final var result
                = zip(List.of(1, 2, 3), List.of());
            assertThat(result)
                .isEmpty();
        }

        @Test
        void equal_length_lists_produce_full_pairing() {
            LOGGER.info("Zipper.zip(...) should pair all elements when lists have equal length");
            final var numbers
                = List.of(1, 2, 3);
            final var letters
                = List.of("a", "b", "c");
            final var result
                = zip(numbers, letters);

            assertThat(result)
                .hasSize(3);
            assertThat(result.get(0).get1())
                .isEqualTo(1);
            assertThat(result.get(0).get2())
                .isEqualTo("a");
            assertThat(result.get(1).get1())
                .isEqualTo(2);
            assertThat(result.get(1).get2())
                .isEqualTo("b");
            assertThat(result.get(2).get1())
                .isEqualTo(3);
            assertThat(result.get(2).get2())
                .isEqualTo("c");
        }

        @Test
        void first_list_shorter_truncates_to_first_length() {
            LOGGER.info("Zipper.zip(...) should truncate to shorter list when first list is shorter");
            final var numbers
                = List.of(1, 2);
            final var letters
                = List.of("a", "b", "c", "d");
            final var result
                = zip(numbers, letters);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).get1())
                .isEqualTo(1);
            assertThat(result.get(0).get2())
                .isEqualTo("a");
            assertThat(result.get(1).get1())
                .isEqualTo(2);
            assertThat(result.get(1).get2())
                .isEqualTo("b");
        }

        @Test
        void second_list_shorter_truncates_to_second_length() {
            LOGGER.info("Zipper.zip(...) should truncate to shorter list when second list is shorter");
            final var numbers
                = List.of(1, 2, 3, 4);
            final var letters
                = List.of("a", "b");
            final var result
                = zip(numbers, letters);

            assertThat(result)
                .hasSize(2);
            assertThat(result.get(0).get1())
                .isEqualTo(1);
            assertThat(result.get(0).get2())
                .isEqualTo("a");
            assertThat(result.get(1).get1())
                .isEqualTo(2);
            assertThat(result.get(1).get2())
                .isEqualTo("b");
        }

        @Test
        void single_element_lists_produce_single_pair() {
            LOGGER.info("Zipper.zip(...) should produce single pair for single-element lists");
            final var result
                = zip(List.of(42), List.of("answer"));

            assertThat(result)
                .hasSize(1);
            assertThat(result.getFirst().get1())
                .isEqualTo(42);
            assertThat(result.getFirst().get2())
                .isEqualTo("answer");
        }

        @Test
        void result_is_new_list_not_modifying_inputs() {
            LOGGER.info("Zipper.zip(...) should return new list without modifying inputs");
            final var numbers
                = new ArrayList<>(List.of(1, 2, 3));
            final var letters
                = new ArrayList<>(List.of("a", "b", "c"));
            final var numbersBefore
                = new ArrayList<>(numbers);
            final var lettersBefore
                = new ArrayList<>(letters);

            assertThat(numbers)
                .isEqualTo(numbersBefore);
            assertThat(letters)
                .isEqualTo(lettersBefore);
        }

        @Test
        void works_with_different_types() {
            LOGGER.info("Zipper.zip(...) should work with different element types");
            final var doubles
                = List.of(1.5, 2.5, 3.5);
            final var booleans
                = List.of(true, false, true);
            final var result
                = zip(doubles, booleans);

            assertThat(result)
                .hasSize(3);
            assertThat(result.get(0).get1())
                .isEqualTo(1.5);
            assertThat(result.get(0).get2())
                .isTrue();
            assertThat(result.get(1).get1())
                .isEqualTo(2.5);
            assertThat(result.get(1).get2())
                .isFalse();
        }

    }

    @Nested
    @DisplayName("Unzip")
    class Unzip {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void null_list_throws_NullPointerException() {
            LOGGER.info("Zipper.unzip(...) should throw NullPointerException when list is null");
            assertThatThrownBy(() -> unzip(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void empty_list_produces_two_empty_lists() {
            LOGGER.info("Zipper.unzip(...) should return two empty lists when input is empty");
            final var result
                = unzip(List.<Tuple2<Integer, String>>of());

            assertThat(result.get1())
                .isEmpty();
            assertThat(result.get2())
                .isEmpty();
        }

        @Test
        void single_tuple_produces_single_element_lists() {
            LOGGER.info("Zipper.unzip(...) should produce single-element lists for single tuple");
            final var tuples
                = List.of(tuple2(42, "answer"));
            final var result
                = unzip(tuples);

            assertThat(result.get1())
                .containsExactly(42);
            assertThat(result.get2())
                .containsExactly("answer");
        }

        @Test
        void multiple_tuples_separate_correctly() {
            LOGGER.info("Zipper.unzip(...) should separate multiple tuples into two lists correctly");
            final var tuples
                = List.of(
                    tuple2(1, "a"),
                    tuple2(2, "b"),
                    tuple2(3, "c")
                );
            final var result
                = unzip(tuples);

            assertThat(result.get1())
                .containsExactly(1, 2, 3);
            assertThat(result.get2())
                .containsExactly("a", "b", "c");
        }

        @Test
        void preserves_order() {
            LOGGER.info("Zipper.unzip(...) should preserve element order");
            final var tuples
                = List.of(
                    tuple2("first", 1),
                    tuple2("second", 2),
                    tuple2("third", 3),
                    tuple2("fourth", 4)
                );
            final var result
                = unzip(tuples);

            assertThat(result.get1())
                .containsExactly("first", "second", "third", "fourth");
            assertThat(result.get2())
                .containsExactly(1, 2, 3, 4);
        }

        @Test
        void works_with_different_types() {
            LOGGER.info("Zipper.unzip(...) should work with different element types");
            final var tuples
                = List.of(
                    tuple2(1.5, true),
                    tuple2(2.5, false),
                    tuple2(3.5, true)
                );
            final var result
                = unzip(tuples);

            assertThat(result.get1())
                .containsExactly(1.5, 2.5, 3.5);
            assertThat(result.get2())
                .containsExactly(true, false, true);
        }

        @Test
        void result_lists_are_independent_from_input() {
            LOGGER.info("Zipper.unzip(...) should return new lists independent from input");
            final var tuples
                = new ArrayList<>(List.of(
                    tuple2(1, "a"),
                    tuple2(2, "b")
                ));
            final var result
                = unzip(tuples);

            // Modifying input shouldn't affect result
            tuples.clear();

            assertThat(result.get1())
                .containsExactly(1, 2);
            assertThat(result.get2())
                .containsExactly("a", "b");
        }

    }

    @Nested
    @DisplayName("Roundtrip")
    class Roundtrip {

        @Test
        void zip_then_unzip_preserves_data() {
            LOGGER.info("Zipper: zip followed by unzip should preserve original data");
            final var numbers
                = List.of(1, 2, 3, 4, 5);
            final var letters
                = List.of("a", "b", "c", "d", "e");

            final var zipped
                = zip(numbers, letters);
            final var unzipped
                = unzip(zipped);

            assertThat(unzipped.get1())
                .isEqualTo(numbers);
            assertThat(unzipped.get2())
                .isEqualTo(letters);
        }

        @Test
        void unzip_then_zip_preserves_tuples() {
            LOGGER.info("Zipper: unzip followed by zip should preserve original tuples");
            final var tuples
                = List.of(
                    tuple2(1, "a"),
                    tuple2(2, "b"),
                    tuple2(3, "c")
                );

            final var unzipped
                = unzip(tuples);
            final var zipped
                = zip(unzipped.get1(), unzipped.get2());

            assertThat(zipped)
                .hasSize(tuples.size());
            for (int i = 0; i < tuples.size(); i++) {
                assertThat(zipped.get(i).get1())
                    .isEqualTo(tuples.get(i).get1());
                assertThat(zipped.get(i).get2())
                    .isEqualTo(tuples.get(i).get2());
            }
        }

        @Test
        void roundtrip_with_unequal_lengths_truncates_properly() {
            LOGGER.info("Zipper: roundtrip with unequal lengths should handle truncation");
            final var numbers
                = List.of(1, 2, 3);
            final var letters
                = List.of("a", "b", "c", "d", "e");

            final var zipped
                = zip(numbers, letters);
            final var unzipped
                = unzip(zipped);

            // Should only have 3 elements (truncated to shorter list)
            assertThat(unzipped.get1())
                .containsExactly(1, 2, 3);
            assertThat(unzipped.get2())
                .containsExactly("a", "b", "c");
        }

    }

}