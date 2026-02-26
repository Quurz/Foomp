package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Provider.provider;
import static org.quurz.foomp.base.functions.Provider.providerFrom;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
class ProviderTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(ProviderTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testProviderWithNullValue() {
        LOGGER.info("Test Provider.provider with null-value");

        assertThatThrownBy(() -> provider(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testProviderWithValidValue() {
        LOGGER.info("Test Provider.provider with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = provider(SOME_STRING_VALUE);

                assertThat(provider.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(provider.isPresent())
                    .isTrue();
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testProviderFromWithNullValue() {
        LOGGER.info("Test Provider.providerFrom with null-value");

        assertThatThrownBy(() -> providerFrom(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testProviderFromWithValidValue() {
        LOGGER.info("Test Provider.providerFrom with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = providerFrom(() -> SOME_STRING_VALUE);

                assertThat(provider.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(provider.isPresent())
                    .isTrue();
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testApply() {
        LOGGER.info("Test provider.apply");

        final var provider
            = provider(SOME_STRING_VALUE);

        assertThatThrownBy(() -> provider.apply(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(provider.apply(nothing))
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapWithNullValue() {
        LOGGER.info("Test provider.map with null-value");

        assertThatThrownBy(() -> provider(SOME_STRING_VALUE).map(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("unused")
    @Test
    void testMapWithNullReturningFMap() {
        LOGGER.info("Test provider.map with null-returning fMap");

        assertThatThrownBy(() -> provider(SOME_STRING_VALUE).map(_$ -> null).get())
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testMapWithValidValue() {
        LOGGER.info("Test provider.map with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = provider(SOME_STRING_VALUE);
                assertThat(provider.map(String::length).get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLiftWithNullValue() {
        LOGGER.info("Test provider.applyTo with null-Value");

        assertThatThrownBy(() -> provider(SOME_STRING_VALUE).applyTo(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testLiftWithValidValue() {
        LOGGER.info("Test provider.applyTo with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = provider(SOME_STRING_VALUE);

                assertThat(provider.applyTo(provider(String::length)).get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBindWithNullValue() {
        LOGGER.info("Test provider.bind with null-value");

        assertThatThrownBy(() -> provider(SOME_STRING_VALUE).bind(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testBindWithValidValue() {
        LOGGER.info("Test provider.bind with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = provider(SOME_STRING_VALUE);

                assertThat(provider.bind(string -> provider(string.length())).get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @Test
    void testCopy() {
        LOGGER.info("Test provider.copy");

        assertThatNoException()
            .isThrownBy(() -> {
                final var provider
                    = provider(SOME_STRING_VALUE);
                final var mappedCopy
                    = provider.copy().map(String::length);

                assertThat(provider.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(mappedCopy.get())
                    .isEqualTo(SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTransmogrifyWithNullValue() {
        LOGGER.info("Test provider.transmogrify with null-value");

        assertThatThrownBy(() -> provider(SOME_STRING_VALUE).transmogrify(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testTransmogrifyWithValidValue() {
        LOGGER.info("Test provider.transmogrify with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var transmogrified
                    = provider(SOME_STRING_VALUE).transmogrify(Provider::get);
                assertThat(transmogrified)
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @Test
    void testUnwind() {
        LOGGER.info("Test provider.unwind");

        assertThatNoException()
            .isThrownBy(() -> {
                final var invocationCountingFun
                    = invocationCountingFun(Fun.identity());
                final var provider
                    = provider(SOME_STRING_VALUE)
                        .map(invocationCountingFun)
                        .applyTo(provider(invocationCountingFun))
                        .bind(obj -> provider(String.valueOf(obj)));

                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(0);

                final var unwound
                    = provider.unwind();

                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(2);
                assertThat(unwound.get())
                    .isEqualTo(SOME_STRING_VALUE);
                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(2);
            });
    }

}
