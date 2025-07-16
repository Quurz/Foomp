package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.slf4j.Logger;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

class Tuple2Test
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(Tuple2Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTuple2() {
        LOGGER.info("Test Tuple2.tuple2");

        final var tuple2
            = tuple2(5, 6);

        assertThatThrownBy(() -> tuple2(null, 5))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, null))
            .isInstanceOf(NullPointerException.class);
        assertThat(tuple2.is1())
            .isTrue();
        assertThat(tuple2.isPresent())
            .isTrue();
        assertThat(tuple2.is2())
            .isTrue();
        assertThat(tuple2.get())
            .isEqualTo(5);
        assertThat(tuple2.get1())
            .isEqualTo(5);
        assertThat(tuple2.get2())
            .isEqualTo(6);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith1() {
        LOGGER.info("Test tuple2.withFirst");

        assertThatThrownBy(() -> tuple2(5, 5).with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var tuple
                    = tuple2(5, 6).with1(7);
                assertThat(tuple.get1())
                    .isEqualTo(7);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith2() {
        LOGGER.info("Test tuple2.withSecond");

        assertThatThrownBy(() -> tuple2(5, 5).with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var tuple
                    = tuple2(5, 6).with2(7);
                assertThat(tuple.get2())
                    .isEqualTo(7);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMeld() {
        LOGGER.info("Test tuple2.meld");

        assertThatThrownBy(() -> tuple2(5, 5).meld(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 5).meld((_i1, _i2) -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = tuple2(5, 5).meld(Integer::sum);
                assertThat(result)
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMapTo1() {
        LOGGER.info("Test tuple2.mapToFirst");

        assertThatThrownBy(() -> tuple2(5, 6).mapTo1((Fun2<? super Integer, ? super Integer, ?>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).mapTo1((_i1, _i2) -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).mapTo1(Integer::sum);
                assertThat(mapped.get1())
                    .isEqualTo(11);
                assertThat(mapped.get2())
                    .isEqualTo(6);
            });

        assertThatThrownBy(() -> tuple2(5, 5).mapTo1((Fun<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 5).mapTo1(i -> null).get())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).mapTo1(String::valueOf);
                assertThat(mapped.get1())
                    .isEqualTo("6");
                assertThat(mapped.get2())
                    .isEqualTo(6);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMapTo2() {
        LOGGER.info("Test tuple2.mapToSecond");

        assertThatThrownBy(() -> tuple2(5, 6).mapTo2((Fun2<? super Integer, ? super Integer, ?>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).mapTo2((_i1, _i2) -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).mapTo2(Integer::sum);
                assertThat(mapped.get2())
                    .isEqualTo(11);
            });

        assertThatThrownBy(() -> tuple2(5, 5).mapTo2((Fun<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 5).mapTo2(i -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).mapTo2(String::valueOf);
                assertThat(mapped.get2())
                    .isEqualTo("5");
                assertThat(mapped.get1())
                    .isEqualTo(5);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap1() {
        LOGGER.info("Test tuple2.mapFirst");

        assertThatThrownBy(() -> tuple2(5, 6).map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).map(_$ -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).map(i -> i * 2);
                assertThat(mapped.get1())
                    .isEqualTo(10);
            });

        assertThatThrownBy(() -> tuple2(5, 6).map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).map1(_$ -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).map1(i -> i * 2);
                assertThat(mapped.get1())
                    .isEqualTo(10);
            });

        assertThatThrownBy(() -> tuple2(5, 6).map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).map1(_$ -> null).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).map1(i -> i * 2);
                assertThat(mapped.get1())
                    .isEqualTo(10);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap2() {
        LOGGER.info("Test tuple2.mapSecond");

        assertThatThrownBy(() -> tuple2(5, 6).map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).map2(_$ -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).map2(i -> i * 2);
                assertThat(mapped.get2())
                    .isEqualTo(12);
            });

        assertThatThrownBy(() -> tuple2(5, 6).map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).map2(_$ -> null).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var mapped
                    = tuple2(5, 6).map2(i -> i * 2);
                assertThat(mapped.get2())
                    .isEqualTo(12);
                });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test tuple2.lift");

        final Fun<Object, Object> nullReturningFun
            = _$ -> null;
        final Fun<Object, String> toStringFun
            = Object::toString;

        assertThatThrownBy(() -> tuple2(5, 6).lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).lift(tuple2(nullReturningFun, nullReturningFun)).get1())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> tuple2(5, 6).lift(tuple2(nullReturningFun, nullReturningFun)).get2())
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var lifted
                    = tuple2(5, 6).lift(tuple2(toStringFun, i -> i + 5));
                assertThat(lifted.get1())
                    .isEqualTo("5");
                assertThat(lifted.get2())
                    .isEqualTo(11);
            });
    }

    @Test
    void testSwap() {
        LOGGER.info("Test tuple2.swap");

        final var tuple
            = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
        final var swapped
            = tuple.swap();

        assertThat(tuple.get1())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(tuple.get2())
            .isEqualTo(SOME_OTHER_STRING_VALUE);
        assertThat(swapped.get1())
            .isEqualTo(SOME_OTHER_STRING_VALUE);
        assertThat(swapped.get2())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testCopy() {
        LOGGER.info("Test tuple2.copy");

        final var copy
            = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE).copy();

        assertThat(copy.get1())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(copy.get2())
            .isEqualTo(SOME_OTHER_STRING_VALUE);
    }

    @Test
    void testToRecord() {
        LOGGER.info("Test tuple2.record");

        final var tuple
            = tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
        final var record
            = tuple.toRecord();

        assertThat(record.get1())
            .isEqualTo(SOME_STRING_VALUE);
        assertThat(record.get2())
            .isEqualTo(SOME_OTHER_STRING_VALUE);
    }

    @Test
    void testUnwind1() {
        LOGGER.info("Test tuple2.unwind1");

        final var tuple2
            = tuple2(5, SOME_STRING_VALUE);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final InvocationCountingFun<Integer, Integer> invocationCountingAddFive
            = invocationCountingFun(i -> i + 5);

        final var spooled
            = tuple2
                .map(invocationCountingAddFive)
                .map2(invocationCountingToString)
                .mapAll(invocationCountingAddFive, invocationCountingToString)
                .lift(tuple2(invocationCountingToString, invocationCountingToString));

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingAddFive.getInvocationCount())
            .isZero();

        spooled.unwind1();

        assertThat(invocationCountingAddFive.getInvocationCount())
            .isEqualTo(2);
        assertThat(invocationCountingToString.getInvocationCount())
            .isOne();
    }

    @Test
    void testUnwind2() {
        LOGGER.info("Test tuple2.unwind2");

        final var tuple2
            = tuple2(5, SOME_STRING_VALUE);

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final InvocationCountingFun<Integer, Integer> invocationCountingAddFive
            = invocationCountingFun(i -> i + 5);

        final var spooled
            = tuple2
                .map(invocationCountingAddFive)
                .map2(invocationCountingToString)
                .mapAll(invocationCountingAddFive, invocationCountingToString)
                .lift(tuple2(invocationCountingToString, invocationCountingToString));

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingAddFive.getInvocationCount())
            .isZero();

        spooled.unwind2();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(3);
        assertThat(invocationCountingAddFive.getInvocationCount())
            .isZero();
    }

    @Test
    void testUnwind() {
        LOGGER.info("Test tuple2.unwind");

        final var invocationCountingToString
            = invocationCountingFun(Objects::toString);
        final InvocationCountingFun<Integer, Integer> invocationCountingAddFive
            = invocationCountingFun(i -> i + 5);

        final var tuple2
            = tuple2(5, SOME_STRING_VALUE);

        final var spooled
            = tuple2
                .map(invocationCountingAddFive)
                .map2(invocationCountingToString)
                .mapAll(invocationCountingAddFive, invocationCountingToString)
                .lift(tuple2(invocationCountingToString, invocationCountingToString));

        assertThat(invocationCountingToString.getInvocationCount())
            .isZero();
        assertThat(invocationCountingAddFive.getInvocationCount())
            .isZero();

        final var unwound
            = spooled.unwind();

        assertThat(invocationCountingToString.getInvocationCount())
            .isEqualTo(4);
        assertThat(invocationCountingAddFive.getInvocationCount())
            .isEqualTo(2);

        assertThat(tuple2.get1())
            .isEqualTo(5);
        assertThat(tuple2.get2())
            .isEqualTo(SOME_STRING_VALUE);

        invocationCountingToString.resetInvocationCount();
        invocationCountingAddFive.resetInvocationCount();

        assertThat(unwound.get1())
            .isEqualTo("15");
        assertThat(unwound.get2())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    void testEquals() {
        LOGGER.info("Test tuple2.equals");

        final var tuple1
            = tuple2(1, "test");
        final var tuple2
            = tuple2(1, "test");
        final var tuple3
            = tuple2(2, "test");

        assertThat(tuple1)
            .isEqualTo(tuple1)
            .isEqualTo(tuple2);
        assertThat(tuple1)
            .isNotEqualTo(tuple3);
        assertThat(tuple1)
            .isNotEqualTo(null);
        assertThat(tuple1)
            .isNotEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testHashCode() {
        LOGGER.info("Test tuple2.hashCode");

        final var firstTuple2
            = tuple2(1, "test");
        final var secondTuple2
            = tuple2(1, "test");
        final var thirdTuple2
            = tuple2(2, "test");

        assertThat(firstTuple2)
            .hasSameHashCodeAs(firstTuple2)
            .hasSameHashCodeAs(secondTuple2)
            .doesNotHaveSameHashCodeAs(thirdTuple2);
    }

    @Test
    void testToString() {
        LOGGER.info("Test tuple2.toString");

        assertThat(tuple2(SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE))
            .hasToString("Tuple2[value1=%s, value2=%s]", SOME_STRING_VALUE, SOME_OTHER_STRING_VALUE);
    }

}
