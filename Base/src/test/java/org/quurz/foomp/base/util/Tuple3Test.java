package org.quurz.foomp.base.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun3;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record3.record3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("unused")
class Tuple3Test
        extends TestHelper {

    private static final Logger LOGGER
            = getLogger(Tuple3Test.class);

    @Test
    void dummy() {
        LOGGER.info("Dummy test");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTuple3() {
        LOGGER.info("Test Tuple3.tuple3");

        final var tuple3
            = tuple3(1, 2, 3);

        assertThatThrownBy(() -> tuple3(null, 2, 3))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3(1, null, 3))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3(1, 2, null))
            .isInstanceOf(NullPointerException.class);

        Assertions.assertThat(tuple3.isPresent())
            .isTrue();
        Assertions.assertThat(tuple3.is1())
            .isTrue();
        Assertions.assertThat(tuple3.is2())
            .isTrue();
        Assertions.assertThat(tuple3.is3())
            .isTrue();

        assertThat(tuple3.get())
            .isEqualTo(tuple3.get1())
            .isEqualTo(1);
        assertThat(tuple3.get2())
            .isEqualTo(2);
        assertThat(tuple3.get3())
            .isEqualTo(3);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith() {
        LOGGER.info("Test Tuple3.with");

        final var tuple3
            = tuple3(1, 2, 3);

        assertThatThrownBy(() -> tuple3.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.with3(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple3.with1(4))
            .isEqualTo(tuple3(4, 2, 3));
        assertThat(tuple3.with2(5))
            .isEqualTo(tuple3(1, 5, 3));
        assertThat(tuple3.with3(6))
            .isEqualTo(tuple3(1, 2, 6));
    }

    @SuppressWarnings({"DataFlowIssue", "unused", "preview"})
    @Test
    void testMap1() {
        LOGGER.info("Test tuple3.map1");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.map1(_$ -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThat(tuple3.map1(s -> s + " World").get1())
            .isEqualTo("Hello World");
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap2() {
        LOGGER.info("Test tuple3.map2");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.map2(_$ -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThat(tuple3.map2(i -> i + 1).get2())
            .isEqualTo(18);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap3() {
        LOGGER.info("Test tuple3.map3");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.map3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.map3(_$ -> null).get3())
            .isInstanceOf(NullPointerException.class);
        assertThat(tuple3.map3(d -> d + 1.0D).get3())
            .isEqualTo(3.0D);
    }

    @SuppressWarnings({"DataFlowIssue", "unused", "preview"})
    @Test
    void testMapAll() {
        LOGGER.info("Test tuple3.mapAll");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.mapAll(Function.identity(), Fun.identity(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.mapAll(Function.identity(), null, Function.identity()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.mapAll(null, Fun.identity(), Function.identity()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.mapAll(_$ -> null, _$ -> null, _$ -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.mapAll(_$ -> null, _$ -> null, _$ -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.mapAll(_$ -> null, _$ -> null, _$ -> null).get3())
            .isInstanceOf(NullPointerException.class);
        assertThat(tuple3.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get1())
            .isEqualTo("Hello World");
        assertThat(tuple3.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get2())
            .isEqualTo(18);
        assertThat(tuple3.mapAll(s -> s + " World", i -> i + 1, d -> d + 1.0D).get3())
            .isEqualTo(3.0D);
    }

    @SuppressWarnings({"DataFlowIssue", "preview"})
    @Test
    void testLift() {
        LOGGER.info("test tuple3.lift");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.lift(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> tuple3.lift(tuple3(_$ -> null, Fun.identity(), Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.lift(tuple3(Function.identity(), _$ -> null, Fun.identity())).unwind())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple3.lift(tuple3(Function.identity(), Fun.identity(), _$ -> null)).unwind())
            .isInstanceOf(NullPointerException.class);

        assertThat(tuple3.lift(tuple3(s -> s + " World", i -> i + 1, d -> d + 1.0D)).unwind())
            .isEqualTo(tuple3("Hello World", 18, 3.0D));
    }

    @SuppressWarnings({"DataFlowIssue", "preview"})
    @Test
    void testMeld() {
        LOGGER.info("Test tuple3.meld");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThatThrownBy(() -> tuple3.meld(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> tuple3.meld((_1, _2, _3) -> null))
            .isInstanceOf(NullPointerException.class);

        final Fun3<String, Integer, Double, String> melding
            = (s, i, d) -> s + " " + i + " " + d;

        assertThat(tuple3.meld(melding))
            .isEqualTo("Hello 17 2.0");
    }

    @Test
    void testCopy() {
        LOGGER.info("Test tuple3.copy");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThat(tuple3.copy())
            .isEqualTo(tuple3("Hello", 17, 2.0D));
    }

    @Test
    void testToRecord() {
        LOGGER.info("Test tuple3.record");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThat(tuple3.toRecord())
            .isEqualTo(record3("Hello", 17, 2.0D));
    }

    @Test
    void testUnwind1() {
        LOGGER.info("Test tuple3.unwind1");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple3
                .map1(invocationCountingToString)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .mapAll(
                    invocationCountingToString,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .lift(
                    tuple3(
                        invocationCountingToString,
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
        LOGGER.info("Test tuple3.unwind2");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple3
                .map1(invocationCountingIdentity)
                .map2(invocationCountingToString)
                .map3(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingToString,
                    invocationCountingIdentity
                )
                .lift(
                    tuple3(
                        invocationCountingIdentity,
                        invocationCountingToString,
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
        LOGGER.info("Test tuple3.unwind3");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple3
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingToString)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingToString
                )
                .lift(
                    tuple3(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingToString
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
    void testUnwind() {
        LOGGER.info("Test tuple3.unwind");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        final var invocationCountingIdentity
            = invocationCountingFun(Fun.identity());

        final var spooled
            = tuple3
                .map1(invocationCountingIdentity)
                .map2(invocationCountingIdentity)
                .map3(invocationCountingIdentity)
                .mapAll(
                    invocationCountingIdentity,
                    invocationCountingIdentity,
                    invocationCountingIdentity
                )
                .lift(
                    tuple3(
                        invocationCountingIdentity,
                        invocationCountingIdentity,
                        invocationCountingIdentity
                    )
                );

        assertThat(invocationCountingIdentity.getInvocationCount())
            .isZero();

        spooled.unwind();

        assertThat(invocationCountingIdentity.getInvocationCount())
            .isEqualTo(9);
    }

    @Test
    void testEqualsAndHashcode() {
        LOGGER.info("Test tuple3.equals and tuple3.hashCode");

        final var firstTuple3
            = tuple3("Hello", 17, 2.0D);
        final var secondTuple3
            = tuple3("Hello", 17, 2.0D);
        final var thirdTuple3
            = tuple3("World", 23, 3.0D);

        assertThat(firstTuple3)
            .isEqualTo(firstTuple3)
            .isEqualTo(secondTuple3)
            .isNotEqualTo(thirdTuple3);

        assertThat(firstTuple3)
            .hasSameHashCodeAs(firstTuple3)
            .hasSameHashCodeAs(secondTuple3)
            .doesNotHaveSameHashCodeAs(thirdTuple3);
    }

    @Test
    void testToString() {
        LOGGER.info("Test tuple3.toString");

        final var tuple3
            = tuple3("Hello", 17, 2.0D);

        assertThat(tuple3)
            .hasToString("Tuple3[value1=%s, value2=%s, value3=%s]", "Hello", 17, 2.0D);
    }

}
