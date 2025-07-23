package org.quurz.foomp.plugins;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.plugins.AbstractDelegatingProxy.DELEGATE_FIELD_NAME;
import static org.quurz.foomp.plugins.DelegatingProxy.delegatingProxy;
import static org.slf4j.LoggerFactory.getLogger;

class DelegatingProxyTest {

    private static final Logger LOGGER
        = getLogger(DelegatingProxyTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testDelegatingProxy() {
        LOGGER.info("Test DelegatingProxy.delegatingProxy(...)");

        assertThatThrownBy(() -> delegatingProxy(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining(DELEGATE_FIELD_NAME);
        assertThatThrownBy(() -> delegatingProxy(null, true))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining(DELEGATE_FIELD_NAME);

        assertThatNoException()
            .isThrownBy(() -> delegatingProxy(String.class));
        assertThatNoException()
            .isThrownBy(() -> delegatingProxy(String.class, false));
    }

}
