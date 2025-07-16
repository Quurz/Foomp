package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun4;
import org.slf4j.Logger;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record4.record4;
import static org.quurz.foomp.base.util.Tuple4.tuple4;
import static org.slf4j.LoggerFactory.getLogger;

class Tuple4Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Tuple4Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTuple4() {
        LOGGER.info("Test Tuple4.tuple4");

        assertThatThrownBy(() -> tuple4(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4(null, null, null, 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4(null, null, 1, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4(null, 1, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4(1, null, null, null))
            .isInstanceOf(NullPointerException.class);

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThat(tuple4.is1())
            .isEqualTo(tuple4.isPresent())
            .isEqualTo(tuple4.is2())
            .isEqualTo(tuple4.is3())
            .isEqualTo(tuple4.is4())
            .isTrue();

        assertThat(tuple4.get1())
            .isEqualTo(tuple4.get())
            .isEqualTo("Hello");
        assertThat(tuple4.get2())
            .isEqualTo(1);
        assertThat(tuple4.get3())
            .isEqualTo(2.0);
        assertThat(tuple4.get4())
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith() {
        LOGGER.info("Test tuple4.with1, tuple4.with2, tuple4.with3 and tuple4.with4");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.with4(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.with1("World"))
            .isEqualTo(tuple4("World", 1, 2.0, true));
        assertThat(tuple4.with2(2))
            .isEqualTo(tuple4("Hello", 2, 2.0, true));
        assertThat(tuple4.with3(3.0))
            .isEqualTo(tuple4("Hello", 1, 3.0, true));
        assertThat(tuple4.with4(false))
            .isEqualTo(tuple4("Hello", 1, 2.0, false));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test tuple4.map1, tuple4.map2, tuple4.map3, tuple4.map4 and tuple4.mapAll");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map4(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, null, Fun.identity()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, null, Fun.identity(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(null, Fun.identity(), null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), null, null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> tuple4.map1(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map2(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map3(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.map4(_$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(_$ -> null, _$ -> null, _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), _$ -> null, _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), Fun.identity(), _$ -> null, _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.mapAll(Fun.identity(), Fun.identity(), Fun.identity(), _$ -> null).unwind())
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.map1(s -> s + " World").unwind())
            .isEqualTo(tuple4("Hello World", 1, 2.0, true));
        assertThat(tuple4.map2(i -> i + 1).unwind())
            .isEqualTo(tuple4("Hello", 2, 2.0, true));
        assertThat(tuple4.map3(d -> d + 1.0).unwind())
            .isEqualTo(tuple4("Hello", 1, 3.0, true));
        assertThat(tuple4.map4(b -> !b).unwind())
            .isEqualTo(tuple4("Hello", 1, 2.0, false));
        assertThat(tuple4.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0, b -> !b).unwind())
            .isEqualTo(tuple4("Hello World", 2, 3.0, false));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test tuple4.lift");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.lift(tuple4(_$ -> null, Fun.identity(), Fun.identity(), Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.lift(tuple4(Fun.identity(), _$ -> null, Fun.identity(), Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.lift(tuple4(Fun.identity(), Fun.identity(), _$ -> null, Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.lift(tuple4(Fun.identity(), Fun.identity(), Fun.identity(), _$ -> null)).unwind())
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple4.lift(tuple4(s -> s + " World", i -> i + 1, d -> d + 1.0d, b -> !b)).unwind())
            .isEqualTo(tuple4("Hello World", 2, 3.0, false));
    }

    @SuppressWarnings({"DataFlowIssue", "unused", "preview"})
    @Test
    void testMeld() {
        LOGGER.info("Test tuple4.meld");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThatThrownBy(() -> tuple4.meld(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple4.meld((_1, _2, _3, _4) -> null))
            .isInstanceOf(NullPointerException.class);

        final Fun4<String, Integer, Double, Boolean, String> melding
            = (s, i, d, b) -> s + " " + i + " " + d + " " + b;

        assertThat(tuple4.meld(melding))
            .isEqualTo("Hello 1 2.0 true");
    }

    @Test
    void testCopy() {
        LOGGER.info("Test tuple4.copy");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThat(tuple4.copy())
            .isEqualTo(tuple4("Hello", 1, 2.0, true));
    }

    @Test
    void testToRecord() {
        LOGGER.info("Test tuple4.record");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThat(tuple4.toRecord())
            .isEqualTo(record4("Hello", 1, 2.0, true));
    }

    @Test
    void testUnwind1() {
        LOGGER.info("Test tuple4.unwind1");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple4
                .map1(invocationCountingToString)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingToString,
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .lift(
                    tuple4(
                        invocationCountingToString,
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                );

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind1();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(3);
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();
    }

    @Test
    void testUnwind2() {
        LOGGER.info("Test tuple4.unwind2");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingToString)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingToString,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .lift(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingToString,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                );

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind2();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(3);
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();
    }

    @Test
    void testUnwind3() {
        LOGGER.info("Test tuple4.unwind3");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingToString)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingToString,
                    invocationCountingIdentity
                )
                .lift(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingToString,
                        invocationCountingIdentity
                    )
                );

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind3();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(3);
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();
    }

    @Test
    void testUnwind4() {
        LOGGER.info("Test tuple4.unwind4");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingToString)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingToString
                )
                .lift(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingToString
                    )
                );

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind4();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(3);
        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();
    }

    @Test
    void testUnwind() {
        LOGGER.info("Test tuple4.unwind");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple4
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .map4(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .lift(
                    tuple4(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                );

        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind();

        assertThat(invocationCountingIdentity.getInvocationCount())
                .isEqualTo(12);
    }

    @Test
    void testEqualsAndHashcode() {
        LOGGER.info("Test tuple4.equals and tuple4.hashCode");

        final var firstTuple4
            = tuple4("Hello", 1, 2.0, true);
        final var secondTuple4
            = tuple4("Hello", 1, 2.0, true);
        final var thirdTuple4
            = tuple4("World", 2, 3.0, false);

        assertThat(firstTuple4)
            .isEqualTo(firstTuple4)
            .isEqualTo(secondTuple4)
            .isNotEqualTo(thirdTuple4);

        assertThat(firstTuple4)
            .hasSameHashCodeAs(firstTuple4)
            .hasSameHashCodeAs(secondTuple4)
            .doesNotHaveSameHashCodeAs(thirdTuple4);
    }

    @Test
    void testToString() {
        LOGGER.info("Test tuple4.toString");

        final var tuple4
            = tuple4("Hello", 1, 2.0, true);

        assertThat(tuple4.toString())
            .hasToString("Tuple4[value1=%s, value2=%s, value3=%s, value4=%s]", "Hello", 1, 2.0, true);
    }

}
