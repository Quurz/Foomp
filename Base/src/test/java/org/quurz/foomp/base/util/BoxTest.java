package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.Higher1;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Box.box;
import static org.quurz.foomp.base.util.Box.narrow;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
@DisplayName("Box")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"unused", "NonAsciiCharacters"})
class BoxTest
        extends TestHelper {

    private static final Logger LOGGER = getLogger(BoxTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void box_with_null_throws() {
            LOGGER.info("Box.box(null) should throw NullPointerException");
            assertThatThrownBy(() -> box(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void box_with_valid_value_creates_box() {
            LOGGER.info("Box.box(value) should create a non-null Box containing the value");
            assertThatNoException().isThrownBy(() -> {
                final var b = box(SOME_STRING_VALUE);
                assertThat(b).isNotNull();
                checkIsSomeWithValue(b, SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void narrow_with_null_throws() {
            LOGGER.info("Box.narrow(null) should throw NullPointerException");
            assertThatThrownBy(() -> narrow(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void narrow_with_valid_higher_returns_box() {
            LOGGER.info("Box.narrow(higher) should return the concrete Box");
            final Higher1<Box.µ, String> higher = box(SOME_STRING_VALUE);
            final var narrowed = narrow(higher);
            assertThat(narrowed).isNotNull();
            assertThat(narrowed).isSameAs(higher);
            assertThat(narrowed.get()).isEqualTo(SOME_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void isPresent_returns_true() {
            LOGGER.info("Box.isPresent() should always return true");
            final var b = box(SOME_STRING_VALUE);
            assertThat(b.isPresent()).isTrue();
        }

        @Test
        void get_evaluates_and_returns_value() {
            LOGGER.info("Box.get() should evaluate and return the contained value");
            final var b = box(SOME_STRING_VALUE);
            assertThat(b.get()).isEqualTo(SOME_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Behaviour (map)")
    class Behaviour_Map {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void map_with_null_throws() {
            LOGGER.info("Box.map(null) should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.map(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void map_is_lazy_and_transforms_value_on_evaluation() {
            LOGGER.info("Box.map should preserve laziness and apply transformation upon evaluation");
            final var b = box(SOME_STRING_VALUE);
            final var fun = invocationCountingFun((Function<String, String>) s -> s + "_mapped");

            final var mapped = b.map(fun);
            assertThat(fun.getInvocationCount()).isZero();

            assertThat(mapped.get()).isEqualTo(SOME_STRING_VALUE + "_mapped");
            assertThat(fun.getInvocationCount()).isEqualTo(1);
        }

        @Test
        void map_returning_null_throws_on_evaluation() {
            LOGGER.info("Box.map transformation returning null should throw NullPointerException on get()");
            final var b = box(SOME_STRING_VALUE);
            final var mapped = b.map(s -> null);
            assertThatThrownBy(mapped::get)
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (applyTo)")
    class Behaviour_ApplyTo {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void applyTo_with_null_throws() {
            LOGGER.info("Box.applyTo(null) should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.applyTo(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void applyTo_applies_function_in_box_lazily() {
            LOGGER.info("Box.applyTo should apply the boxed function upon evaluation");
            final var fnBox = box((Fun<String, Integer>) String::length);
            final var valueBox = box("Hello");

            final var applied = valueBox.applyTo(fnBox);
            assertThat(applied.get()).isEqualTo(5);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void applyTo_function_returning_null_throws_on_evaluation() {
            LOGGER.info("Box.applyTo with function returning null should throw NullPointerException on get()");
            final var fnBox = box((Fun<String, String>) s -> null);
            final var valueBox = box(SOME_STRING_VALUE);

            final var applied = valueBox.applyTo(fnBox);
            assertThatThrownBy(applied::get)
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour (flatMap)")
    class Behaviour_FlatMap {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void flatMap_with_null_throws() {
            LOGGER.info("Box.flatMap(null) should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.flatMap(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void flatMap_transforms_and_flattens() {
            LOGGER.info("Box.flatMap should transform the value into another Box and flatten it");
            final var b = box(SOME_STRING_VALUE);
            final var result = b.flatMap(s -> box(s.length()));

            assertThat(result.get()).isEqualTo(SOME_STRING_VALUE.length());
        }

        @Test
        void flatMap_returning_null_throws() {
            LOGGER.info("Box.flatMap returning null Higher1 should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.flatMap(s -> null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Copyable and Unwindable")
    class Copyable_And_Unwindable {

        @Test
        void copy_creates_new_box_with_same_value() {
            LOGGER.info("Box.copy() should create an equal Box instance");
            final var b = box(SOME_STRING_VALUE);
            final var copy = b.copy();

            assertThat(copy).isNotNull();
            assertThat(copy).isNotSameAs(b);
            assertThat(copy).isEqualTo(b);
            assertThat(copy.get()).isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void unwind_evaluates_and_returns_eager_box() {
            LOGGER.info("Box.unwind() should evaluate the value and return an unwound Box");
            final var b = box(SOME_STRING_VALUE);
            final var unwound = b.unwind();

            assertThat(unwound).isNotNull();
            assertThat(unwound).isEqualTo(b);
            assertThat(unwound.get()).isEqualTo(SOME_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Transmogrifyable")
    class Transmogrifyable_Tests {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void transmogrify_with_null_throws() {
            LOGGER.info("Box.transmogrify(null) should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.transmogrify(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void transmogrify_returning_null_throws() {
            LOGGER.info("Box.transmogrify returning null should throw NullPointerException");
            final var b = box(SOME_STRING_VALUE);
            assertThatThrownBy(() -> b.transmogrify(bx -> null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void transmogrify_transforms_box_instance() {
            LOGGER.info("Box.transmogrify should transform the box via the given function");
            final var b = box(SOME_STRING_VALUE);
            final var result = b.transmogrify(bx -> bx.get().toUpperCase());

            assertThat(result).isEqualTo(SOME_STRING_VALUE.toUpperCase());
        }
    }

    @Nested
    @DisplayName("Object methods")
    class Object_Methods {

        @SuppressWarnings({"EqualsWithItself", "ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Test
        void equals_and_hashCode_contracts() {
            LOGGER.info("Box equals and hashCode should satisfy standard contracts");
            final var b1 = box(SOME_STRING_VALUE);
            final var b2 = box(SOME_STRING_VALUE);
            final var b3 = box(SOME_OTHER_STRING_VALUE);

            // Reflexive
            assertThat(b1.equals(b1)).isTrue();

            // Symmetric
            assertThat(b1.equals(b2)).isTrue();
            assertThat(b2.equals(b1)).isTrue();
            assertThat(b1.hashCode()).isEqualTo(b2.hashCode());

            // Non-equal
            assertThat(b1.equals(b3)).isFalse();
            assertThat(b1.equals(null)).isFalse();
            assertThat(b1.equals("non-box-string")).isFalse();
        }

        @Test
        void toString_contains_class_name_and_value() {
            LOGGER.info("Box.toString() should format correctly with class name and evaluated value");
            final var b = box(SOME_STRING_VALUE);
            assertThat(b.toString()).isEqualTo("Box{spool.get=" + SOME_STRING_VALUE + "}");
        }
    }
}
