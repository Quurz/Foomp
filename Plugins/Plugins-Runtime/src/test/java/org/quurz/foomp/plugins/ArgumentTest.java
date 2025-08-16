package org.quurz.foomp.plugins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.plugins.Argument.argument;
import static org.quurz.foomp.plugins.Argument.extractTypesAndValues;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Argument")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ArgumentTest {

    private static final Logger LOGGER
        = getLogger(ArgumentTest.class);

    @SuppressWarnings("rawtypes")
    private static final Argument[] INVALID_ARGUMENTS_ARRAY
        = new Argument<?>[] {
            argument(String.class),
            null
        };

    @SuppressWarnings("rawtypes")
    private static final Argument[] VALID_ARGUMENTS_ARRAY
        = new Argument<?>[] {
            argument(String.class),
            argument(Integer.class, 123)
        };

    @SuppressWarnings("rawtypes")
    private static final List<Argument> INVALID_ARGUMENTS_LIST
        = Arrays.stream(INVALID_ARGUMENTS_ARRAY)
            .toList();

    @SuppressWarnings("rawtypes")
    private static final List<Argument> VALID_ARGUMENTS_LIST
        = Arrays.stream(VALID_ARGUMENTS_ARRAY)
            .toList();

    private static final Argument<String> ARGUMENT_WITH_VALUE
        = argument(String.class, "<TEST>");
    private static final Argument<Integer> ARGUMENT_WITHOUT_VALUE
        = argument(Integer.class);

    @Nested
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void argument_without_value_rejects_null_type() {
            LOGGER.info("Test Argument.argument(Class): rejects null type");

            assertThatThrownBy(() -> argument(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void argument_with_value_rejects_null_type() {
            LOGGER.info("Test Argument.argument(Class, A): rejects null type");

            assertThatThrownBy(() -> argument(null, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
            assertThatThrownBy(() -> argument(null, new Object()))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("type");
        }

        @Test
        void argument_without_value_accepts_valid_type() {
            LOGGER.info("Test Argument.argument(Class): accepts valid type");

            assertThatNoException()
                .isThrownBy(() -> argument(String.class));
        }

        @Test
        void argument_with_value_accepts_valid_type() {
            LOGGER.info("Test Argument.argument(Class, A): accepts valid type and non-null value");

            assertThatNoException()
                .isThrownBy(() -> argument(String.class, "<TEST>"));
        }

        @Test
        void argument_with_value_accepts_null_value() {
            LOGGER.info("Test Argument.argument(Class, A): accepts null value");

            assertThatNoException()
                .isThrownBy(() -> argument(String.class, null));
        }

    }

    @Nested
    class Extraction {

        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_arrays() {
            LOGGER.info("Test Argument.extractTypesAndValues(Argument<?>[])");

            assertThatThrownBy(() -> extractTypesAndValues((Argument<?>[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("arguments");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_ARRAY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("arguments")
                .hasMessageContaining("1");

            assertThatNoException().isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_ARRAY));
        }

        @SuppressWarnings({"rawtypes", "DataFlowIssue"})
        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_lists() {
            LOGGER.info("Test Argument.extractTypesAndValues(Collection<Argument<?>>)");

            assertThatThrownBy(() -> extractTypesAndValues((List<Argument>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("arguments");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_LIST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("arguments")
                .hasMessageContaining("1");

            assertThatNoException()
                .isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_LIST));
        }

        @SuppressWarnings({"rawtypes", "DataFlowIssue"})
        @Test
        void extractTypesAndValues_validates_inputs_and_succeeds_otherwise_for_streams() {
            LOGGER.info("Test Argument.extractTypesAndValues(Stream<Argument<?>>)");

            assertThatThrownBy(() -> extractTypesAndValues((Stream<Argument>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("arguments");
            assertThatThrownBy(() -> extractTypesAndValues(INVALID_ARGUMENTS_LIST.stream()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("arguments")
                .hasMessageContaining("1");

            assertThatNoException()
                .isThrownBy(() -> extractTypesAndValues(VALID_ARGUMENTS_LIST.stream()));
        }
    }

    @Nested
    class Value_Flags {

        @Test
        void hasValue_and_hasNoValue_behave_consistently() {
            LOGGER.info("Test argument.hasValue() and argument.hasNoValue()");

            assertThat(ARGUMENT_WITH_VALUE.hasValue())
                .isTrue();
            assertThat(ARGUMENT_WITH_VALUE.hasNoValue())
                .isFalse();
            assertThat(ARGUMENT_WITHOUT_VALUE.hasValue())
                .isFalse();
            assertThat(ARGUMENT_WITHOUT_VALUE.hasNoValue())
                .isTrue();
        }
    }

    @Nested
    class Accessors {

        @Test
        void getType_returns_explicit_type() {
            LOGGER.info("Test argument.getType()");

            assertThat(ARGUMENT_WITH_VALUE.getType())
                .isEqualTo(String.class);
            assertThat(ARGUMENT_WITHOUT_VALUE.getType())
                .isEqualTo(Integer.class);
        }

        @Test
        void getValue_returns_value_or_null() {
            LOGGER.info("Test argument.getValue()");

            assertThat(ARGUMENT_WITH_VALUE.getValue())
                .isEqualTo("<TEST>");
            assertThat(ARGUMENT_WITHOUT_VALUE.getValue())
                .isNull();
        }

        @Test
        void getValueSafe_returns_some_or_none() {
            LOGGER.info("Test argument.getValueSafe()");

            assertThat(ARGUMENT_WITH_VALUE.getValueSafe().isSome())
                .isTrue();
            assertThat(ARGUMENT_WITHOUT_VALUE.getValueSafe().isNone())
                .isTrue();
        }
    }

    @Nested
    class Equality_and_HashCode {

        @Test
        void equals_is_reflexive() {
            LOGGER.info("Test argument.equals(Object): reflexivity");
            final var a
                = argument(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(a);
        }

        @Test
        void equals_is_symmetric() {
            LOGGER.info("Test argument.equals(Object): symmetry");
            final var a
                = argument(String.class, "<TEST>");
            final var b
                = argument(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(a);
        }

        @Test
        void equals_is_transitive() {
            LOGGER.info("Test argument.equals(Object): transitivity");
            final var a
                = argument(String.class, "<TEST>");
            final var b
                = argument(String.class, "<TEST>");
            final var c
                = argument(String.class, "<TEST>");
            assertThat(a)
                .isEqualTo(b);
            assertThat(b)
                .isEqualTo(c);
            assertThat(a)
                .isEqualTo(c);
        }

        @Test
        void equals_is_consistent() {
            LOGGER.info("Test argument.equals(Object): consistency");
            final var a1
                = argument(String.class, "<TEST>");
            final var a2
                = argument(String.class, "<TEST>");
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1)
                .isEqualTo(a2); // repeated comparison should yield the same result
        }

        @Test
        void equals_handles_null() {
            LOGGER.info("Test argument.equals(Object): null handling");
            final var a = argument(String.class, "<TEST>");
            assertThat(a).isNotEqualTo(null);
        }

        @Test
        void equals_differs_on_different_value_with_same_type() {
            LOGGER.info("Test argument.equals(Object): different value, same type");
            final var a
                = argument(String.class, "<TEST>");
            final var b
                = argument(String.class, "OTHER");
            assertThat(a)
                .isNotEqualTo(b);
        }

        @Test
        void equals_differs_on_different_type_even_if_values_match_semantically() {
            LOGGER.info("Test argument.equals(Object): different type");
            final var a
                = argument(String.class, "<123>");
            final var c
                = argument(Integer.class, 123);
            assertThat(a)
                .isNotEqualTo(c);
        }

        @Test
        void equals_with_null_values_respects_type() {
            LOGGER.info("Test argument.equals(Object): null values and type");
            final var n1
                = argument(String.class, null);
            final var n2
                = argument(String.class, null);
            final var n3
                = argument(Integer.class, null);

            assertThat(n1)
                .isEqualTo(n2);    // same type + both null -> equal
            assertThat(n1)
                .isNotEqualTo(n3); // different type + both null -> not equal
        }

        @Test
        void hashCode_equal_objects_have_same_hash() {
            LOGGER.info("Test argument.equals(Object): equal objects must have the same hash code");
            final var a1
                = argument(String.class, "<TEST>");
            final var a2
                = argument(String.class, "<TEST>");
            assertThat(a1)
                .isEqualTo(a2);
            assertThat(a1.hashCode())
                .isEqualTo(a2.hashCode());
        }

        @Test
        void hashCode_is_consistent() {
            LOGGER.info("Test argument.hashCode: consistency");
            final var a = argument(String.class, "<TEST>");
            assertThat(a.hashCode()).isEqualTo(a.hashCode());
        }

    }

    @Nested
    class ToString_Contract {

        @Test
        void format_is_stable() {
            LOGGER.info("Test argument.toString()");

            assertThat(ARGUMENT_WITH_VALUE.toString())
                .isEqualTo("Argument[type=class java.lang.String, value=<TEST>]");
            assertThat(ARGUMENT_WITHOUT_VALUE.toString())
                .isEqualTo("Argument[type=class java.lang.Integer, value=null]");
        }

    }

}