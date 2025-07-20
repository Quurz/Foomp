package org.quurz.foomp.plugins;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.plugins.Argument.argument;
import static org.quurz.foomp.plugins.Argument.extractTypesAndValues;
import static org.slf4j.LoggerFactory.getLogger;

class ArgumentTest {

    private static final Logger LOGGER
        = getLogger(ArgumentTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testArgument() {
        LOGGER.info("Test Argument.argument");

        assertThatThrownBy(
                () -> argument(null, null)
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("type");

        assertThatNoException()
            .isThrownBy(() -> argument(String.class, null));
        assertThatNoException()
            .isThrownBy(() -> argument(String.class, "<TEST>"));
    }

    @Test
    void testExtractTypesAndValues() {
        LOGGER.info("Test Argument.extractTypesAndValues");

        final var arguments
            = new Argument[] {argument(String.class, "<TEST>")};

        final var argumentsWithNullElement
            = new Argument[] {
                argument(String.class, "<TEST>"),
                null
            };

        assertThatThrownBy(
                () -> extractTypesAndValues(null)
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("arguments");

        assertThatThrownBy(() -> extractTypesAndValues(argumentsWithNullElement))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("arguments")
            .hasMessageContaining("1");

        assertThatNoException()
            .isThrownBy(() -> {
                final var typesAndValues
                    = extractTypesAndValues(arguments);

                assertThat(typesAndValues.get1().length)
                    .isEqualTo(1);
                assertThat(typesAndValues.get2().length)
                    .isEqualTo(1);

                assertThat(typesAndValues.get1()[0])
                    .isEqualTo(String.class);
                assertThat(typesAndValues.get2()[0])
                    .isEqualTo("<TEST>");
            });
    }

    @Test
    void testGetType() {
        LOGGER.info("Test argument.getType");

        final var argument
            = argument(String.class, "<TEST>");
        assertThat(argument.getType())
            .isEqualTo(String.class);

        final var nullValueArgument
            = argument(Integer.class, null);
        assertThat(nullValueArgument.getType())
            .isEqualTo(Integer.class);
    }

    @Test
    void testGetValue() {
        LOGGER.info("Test argument.getValue");

        final var testValue = "<TEST>";
        final var argument
            = argument(String.class, testValue);
        assertThat(argument.getValue())
            .isEqualTo(testValue);

        final var nullValueArgument
            = argument(String.class, null);
        assertThat(nullValueArgument.getValue())
            .isNull();

    }

    @Test
    void testEqualsAndHashCode() {
        LOGGER.info("Test argument.equals and hashCode");

        final var argument1
            = argument(String.class, "<TEST>");
        final var argument2
            = argument(String.class, "<TEST>");
        final var argument3
            = argument(Object.class, "<TEST>");
        final var argument4
            = argument(String.class, ">OTHER>");
        final var nullValueArgument1
            = argument(String.class, null);
        final var nullValueArgument2
            = argument(String.class, null);

        assertThat(argument1)
            .isEqualTo(argument1)        // selbst
            .isEqualTo(argument2)        // gleicher Wert und Typ
            .isNotEqualTo(argument3)     // gleicher Wert, anderer Typ
            .isNotEqualTo(argument4)     // anderer Wert, gleicher Typ
            .isNotEqualTo(null)     // null
            .isNotEqualTo("test");  // anderer Objekttyp

        assertThat(nullValueArgument1)
            .isEqualTo(nullValueArgument2);   // null-Werte, gleicher Typ

        // Test hashCode
        assertThat(argument1.hashCode())
            .isEqualTo(argument2.hashCode())
            .isNotEqualTo(argument3.hashCode())
            .isNotEqualTo(argument4.hashCode());

        assertThat(nullValueArgument1.hashCode())
            .isEqualTo(nullValueArgument2.hashCode());
    }

    @Test
    void testToString() {
        LOGGER.info("Test argument.toString");

        final var testValue = "<TEST>";
        final var argument = argument(String.class, testValue);

        assertThat(argument.toString())
            .isEqualTo("Argument[type=class java.lang.String, value=<TEST>]");

        final var nullValueArgument = argument(Integer.class, null);
        assertThat(nullValueArgument.toString())
            .isEqualTo("Argument[type=class java.lang.Integer, value=null]");
    }

}
