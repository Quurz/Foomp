package org.quurz.foomp.plugins;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Test
    void test$__get_delegate_lock() {
        LOGGER.info("Test abstractDelegatingProxy.$__get_delegate_lock()");

        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("<TEST>", new DummyReadWriteLock());

        assertThat(abstractDelegatingProxy.$__get_delegate_lock())
            .isInstanceOf(DummyReadWriteLock.class);
    }

    @Test
    void testConcurrentAccess() {
        LOGGER.info("Test concurrent access");

        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("initial", new ReentrantReadWriteLock(true));
        final int threadCount
            = 10;
        final var startLatch
            = new CountDownLatch(1);
        final var endLatch
            = new CountDownLatch(threadCount);

        // Erstelle mehrere Threads die gleichzeitig lesen/schreiben
        assertThatNoException()
            .isThrownBy(() -> {
                for (int i = 0; i < threadCount; i++) {
                    final int threadNum = i;
                    new Thread(() -> {
                        try {
                            startLatch.await();
                            if (threadNum % 2 == 0) {
                                abstractDelegatingProxy.$__get_delegate();
                            } else {
                                abstractDelegatingProxy.$__set_delegate("new" + threadNum);
                            }
                        } catch (final InterruptedException interruptedException) {
                            fail(interruptedException.getMessage(), interruptedException.getCause());
                        } finally {
                            endLatch.countDown();
                        }
                    }).start();
                }

                startLatch.countDown(); // Starte alle Threads gleichzeitig
                assertThat(endLatch.await(5, TimeUnit.SECONDS)).isTrue();

                // Prüfe, dass der finale Wert ein erwarteter Wert ist
                final var finalValue
                    = (String) abstractDelegatingProxy.$__get_delegate();
                assertThat(finalValue)
                    .matches("new[1-9]");
            }
        );
    }

    @Test
    void testLogkingBehaviour() {
        LOGGER.info("Test logging behaviour");

        final var reentrantRreadWriteLock
            = new ReentrantReadWriteLock(true);
        final var reentrantRreadWriteLockMock
            = spy(reentrantRreadWriteLock);
        final var abstractDelegatingProxy
            = new DummyDelegatingProxy("initial", reentrantRreadWriteLockMock);

        abstractDelegatingProxy.$__set_delegate("new");
        abstractDelegatingProxy.$__get_delegate();

        verify(reentrantRreadWriteLockMock, times(2)).readLock();
        verify(reentrantRreadWriteLockMock, times(2)).writeLock();
    }

}