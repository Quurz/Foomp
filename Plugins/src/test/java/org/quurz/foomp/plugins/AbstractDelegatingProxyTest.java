package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.plugins.AbstractDelegatingProxy.DELEGATE_FIELD_NAME;
import static org.quurz.foomp.plugins.AbstractDelegatingProxy.DELEGATE_LOCK_FIELD_NAME;
import static org.slf4j.LoggerFactory.getLogger;

class AbstractDelegatingProxyTest {

    private static final Logger LOGGER
        = getLogger(AbstractDelegatingProxyTest.class);

    private static final class DummyDelegatingProxy
        extends AbstractDelegatingProxy<Object> {

        DummyDelegatingProxy(final @NonNull Object $__delegate,
                             final @NonNull ReadWriteLock lockDelegate) {
            super($__delegate, lockDelegate);
        }

    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAbstractDelegatingProxyNewInstance() {
        LOGGER.info("Test AbstractDelegatingProxy.new(...)");

        assertThatThrownBy(() -> new DummyDelegatingProxy(null, new ReentrantReadWriteLock()))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("delegate");
        assertThatThrownBy(() -> new DummyDelegatingProxy(new Object(), null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining(DELEGATE_LOCK_FIELD_NAME);

        assertThatNoException()
            .isThrownBy(() -> new DummyDelegatingProxy(new Object(), new ReentrantReadWriteLock()));
    }

    @Test
    void test$__get_delegate() {
        LOGGER.info("Test abstractDelegatingProxy.$__get_delegate");

        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("<TEST>", new ReentrantReadWriteLock());

        assertThat(abstractDelegatingProxy.$__get_delegate())
            .isEqualTo("<TEST>");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void test$__set_delegate() {
        LOGGER.info("Test abstractDelegatingProxy.$__set_delegate()");

        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("<TEST>", new ReentrantReadWriteLock());

        assertThatThrownBy(() -> abstractDelegatingProxy.$__set_delegate(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining(DELEGATE_FIELD_NAME);

        assertThatNoException()
            .isThrownBy(() -> abstractDelegatingProxy.$__set_delegate("<NEW_TEST>"));
        assertThat(abstractDelegatingProxy.$__get_delegate())
            .isEqualTo("<NEW_TEST>");
    }

    void test$__get_delegate_lock() {
        LOGGER.info("Test abstractDelegatingProxy.$__get_delegate_lock()");

        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("<TEST>", new DummyReadWriteLock());

        assertThat(abstractDelegatingProxy.$__get_delegate_lock())
            .isInstanceOf(DummyReadWriteLock.class);
    }

}
