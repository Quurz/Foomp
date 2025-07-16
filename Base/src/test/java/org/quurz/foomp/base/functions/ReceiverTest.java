package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestException;
import org.quurz.foomp.base.util.Nothing;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.quurz.foomp.base.functions.Receiver.receiver;
import static org.slf4j.LoggerFactory.getLogger;

class ReceiverTest {

    private static final Logger LOGGER
        = getLogger(ReceiverTest.class);

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testReceiver() {
        LOGGER.info("Test Receiver.receiver(...)");

        assertThatThrownBy(() -> receiver(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> receiver(_$ -> {}));
        assertThat(receiver(_$ -> {}))
            .isNotNull();

        assertThatThrownBy(() -> receiver(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver(null, _$ -> true))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver(_$ -> {}, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> receiver(_$ -> {}, _$ -> true));
        assertThat(receiver(_$ -> {}, _$ -> true))
            .isNotNull();

        assertThatThrownBy(() -> receiver(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver(null, null, _$ -> new TestException("TEST")))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver(null, _$ -> true, _$ -> new TestException("TEST")))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> receiver(_$ -> {}, _$ -> true, _$ -> new TestException("TEST")));
        assertThat(receiver(_$ -> {}, _$ -> true, _$ -> new TestException("TEST")))
            .isNotNull();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testApply() {
        LOGGER.info("Test receiver.apply");

        final var basket
            = new ArrayList<Integer>();

        {
            final Receiver<Integer> receiver
                = receiver(basket::add);

            assertThatThrownBy(() -> receiver.apply(null))
                .isInstanceOf(NullPointerException.class);
            assertThatNoException()
                .isThrownBy(() -> {
                    receiver.apply(1);
                    assertThat(basket)
                        .containsExactly(1);
                });
            basket.clear();
        }

        {
            final Receiver<Integer> receiver
                = receiver(basket::add, i -> (i % 2) == 0);

            assertThatNoException()
                .isThrownBy(() -> {
                    receiver.apply(1);
                    receiver.apply(2);
                    receiver.apply(3);

                    assertThat(basket)
                        .containsExactly(2);
                });
            basket.clear();
        }

        {
            final Receiver<Integer> receiver
                = receiver(basket::add, i -> (i % 2) == 0, _$ -> new TestException("TEST"));

            assertThatThrownBy(() -> receiver.apply(3))
                .isInstanceOf(TestException.class);

            assertThatNoException()
                .isThrownBy(() -> {
                    receiver.apply(2);
                    receiver.apply(4);
                    receiver.apply(6);

                    assertThat(basket)
                        .containsExactly(2, 4, 6);
                });
        }

        {
            final Receiver<Integer> receiver
                = receiver(basket::add, i -> (i % 2) == 0, _$ -> null);

            assertThatThrownBy(() -> receiver.apply(1))
                .isInstanceOf(NullPointerException.class);
        }

        {
            final Receiver<Integer> receiver
                = receiver(basket::add);

            assertThat(receiver.apply(1))
                .isNotNull()
                .isInstanceOf(Nothing.class);

            basket.clear();
        }

    }

    @Test
    void testAcceptAndContinue() {
        LOGGER.info("Test receiver.acceptAndContinue");

        final var basket
            = new ArrayList<Integer>();

        assertThatNoException()
            .isThrownBy(() -> {
                final Receiver<Integer> receiver
                    = receiver(basket::add);

                assertThat(receiver.acceptAndContinue(1))
                    .isNotNull()
                    .isInstanceOf(Receiver.class);
                assertThat(basket)
                    .containsExactly(1);

                receiver.accept(2);
                assertThat(basket)
                    .containsExactly(1, 2);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "ConfusingArgumentToVarargsMethod", "RedundantOperationOnEmptyContainer"})
    @Test
    void testAcceptAllAndContinue() {
        LOGGER.info("Test receiver.acceptAllAndContinue(...)");

        final var basket
            = new ArrayList<Integer>();

        final var valuesWithoutNull
            = new ArrayList<Integer>();
        valuesWithoutNull.add(1);
        valuesWithoutNull.add(2);
        valuesWithoutNull.add(3);

        final var valuesWithNull
            = new ArrayList<Integer>();
        valuesWithNull.add(null);

        final Receiver<Integer> receiver
            = receiver(basket::add);

        assertThatThrownBy(() -> receiver.acceptAllAndContinue(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver.acceptAllAndContinue(null, 2, 3))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver.acceptAllAndContinue(1, null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(receiver.acceptAllAndContinue(1, 2, 3))
                    .isNotNull()
                    .isInstanceOf(Receiver.class);
                assertThat(basket)
                    .containsExactly(1, 2, 3);
            });

        basket.clear();

        assertThatThrownBy(() -> receiver.acceptAllAndContinue((Iterator<Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver.acceptAllAndContinue(valuesWithNull.iterator()))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(receiver.acceptAllAndContinue(valuesWithoutNull.iterator()))
                    .isNotNull()
                    .isInstanceOf(Receiver.class);
                assertThat(basket)
                    .containsExactly(1, 2, 3);
            });

        basket.clear();

        assertThatThrownBy(() -> receiver.acceptAllAndContinue((Stream<Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver.acceptAllAndContinue(valuesWithNull.stream()))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(receiver.acceptAllAndContinue(valuesWithoutNull.stream()))
                    .isNotNull()
                    .isInstanceOf(Receiver.class);
                assertThat(basket)
                    .containsExactly(1, 2, 3);
            });

        basket.clear();

        assertThatThrownBy(() -> receiver.acceptAllAndContinue((Collection<Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> receiver.acceptAllAndContinue(valuesWithNull))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                assertThat(receiver.acceptAllAndContinue(valuesWithoutNull))
                    .isNotNull()
                    .isInstanceOf(Receiver.class);
                assertThat(basket)
                    .containsExactly(1, 2, 3);
            });
    }

}
