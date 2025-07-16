package org.quurz.foomp.plugins;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import testclasses.TestContract;
import testclasses.TestImplementation1;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.plugins.proxybuilder.ProxyBuilder.proxyBuilder;
import static org.slf4j.LoggerFactory.getLogger;

class ProxyBuilderTest {

    private static final Logger LOGGER
        = getLogger(ProxyBuilderTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testProxyBuilder() {
        LOGGER.info("Test ProxyBuilder.proxyBuilder(...)");

        assertThatThrownBy(
                () -> proxyBuilder(
                    null,
                    TestContract.class,
                    () -> TestImplementation1.class,
                    TestContract.class.getClassLoader()
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("canonicalName");
        assertThatThrownBy(
                () -> proxyBuilder(
                    "test",
                    null,
                    () -> TestImplementation1.class,
                    TestContract.class.getClassLoader()
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("contract");
        assertThatThrownBy(
                () -> proxyBuilder(
                    "test",
                    TestContract.class,
                    null,
                    TestContract.class.getClassLoader()
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("implementationResolver");
        assertThatThrownBy(
                () -> proxyBuilder(
                    "test",
                    TestContract.class,
                    () -> TestImplementation1.class,
                    null
                )
            )
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("proxyClassLoader");
    }

}
