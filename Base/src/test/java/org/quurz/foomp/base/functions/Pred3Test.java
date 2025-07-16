package org.quurz.foomp.base.functions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Pred3.not;
import static org.slf4j.LoggerFactory.getLogger;


class Pred3Test {

    private static final Logger LOGGER
        = getLogger(Pred3Test.class.getName());

    private final Pred3<Integer, String, Integer> pred3
        = (low, s, high) -> low < s.length() && s.length() < high;

    @Test
    void testNot() {
        LOGGER.info("Test Pred3.not");

        assertThat(not(pred3).test(0, "Gumpf", 6))
            .isFalse();
        assertThat(not(pred3).test(17, "?", 23))
            .isTrue();
    }

    @Test
    void testNegate() {
        LOGGER.info("Test pred3.negate");

        assertThat(pred3.negate().test(0, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.negate().test(17, "?", 23))
            .isTrue();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testAnd() {
        LOGGER.info("Test pred3.and");

        assertThatThrownBy(() -> pred3.and((Pred3<Integer, String, Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.and((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.and((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.and(() -> null).test(1, "Gumpf", 6))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred3.and((_1, _2, _3) -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and((_1, _2, _3) -> true).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and((_1, _2, _3) -> false).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.and((_1, _2, _3) -> true).test(1, "Gumpf", 6))
            .isTrue();

        assertThat(pred3.and(() -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and(() -> true).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and(() -> false).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.and(() -> true).test(1, "Gumpf", 6))
            .isTrue();

        assertThat(pred3.and(() -> Boolean.FALSE).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and(() -> Boolean.TRUE).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.and(() -> Boolean.FALSE).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.and(() -> Boolean.TRUE).test(1, "Gumpf", 6))
            .isTrue();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNand() {
        LOGGER.info("Test pred3.nand");

        assertThatThrownBy(() -> pred3.nand((Pred3<Integer, String, Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nand((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nand((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nand(() -> null).test(1, "Gumpf", 6))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred3.nand((_1, _2, _3) -> false).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand((_1, _2, _3) -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand((_1, _2, _3) -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.nand((_1, _2, _3) -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.nand(() -> false).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand(() -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand(() -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.nand(() -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.nand(() -> Boolean.FALSE).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand(() -> Boolean.TRUE).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nand(() -> Boolean.FALSE).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.nand(() -> Boolean.TRUE).test(1, "Gumpf", 6))
            .isFalse();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testOr() {
        LOGGER.info("Test pred3.or");

        assertThatThrownBy(() -> pred3.or((Pred3<Integer, String, Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.or((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.or((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.or(() -> null).test(17, "?", 23))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred3.or((_1, _2, _3) -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.or((_1, _2, _3) -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.or((_1, _2, _3) -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.or((_1, _2, _3) -> true).test(1, "Gumpf", 6))
            .isTrue();

        assertThat(pred3.or(() -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.or(() -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.or(() -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.or(() -> true).test(1, "Gumpf", 6))
            .isTrue();

        assertThat(pred3.or(() -> Boolean.FALSE).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.or(() -> Boolean.TRUE).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.or(() -> Boolean.FALSE).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.or(() -> Boolean.TRUE).test(1, "Gumpf", 6))
            .isTrue();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNor() {
        LOGGER.info("Test pred3.nor");

        assertThatThrownBy(() -> pred3.nor((Pred3<Integer, String, Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.nor(() -> null).test(17, "?", 23))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred3.nor((_1, _2, _3) -> false).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nor((_1, _2, _3) -> true).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.nor((_1, _2, _3) -> false).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.nor((_1, _2, _3) -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.nor(() -> false).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nor(() -> true).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.nor(() -> false).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.nor(() -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.nor(() -> Boolean.FALSE).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.nor(() -> Boolean.TRUE).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.nor(() -> Boolean.FALSE).test(1, "Gumpf", 6))
            .isFalse();
        assertThat(pred3.nor(() -> Boolean.TRUE).test(1, "Gumpf", 6))
            .isFalse();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testXor() {
        LOGGER.info("Test pred3.xor");

        assertThatThrownBy(() -> pred3.xor((Pred3<Integer, String, Integer>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.xor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.xor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.xor(() -> null).test(17, "?", 23))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred3.xor((_1, _2, _3) -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.xor((_1, _2, _3) -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.xor((_1, _2, _3) -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.xor((_1, _2, _3) -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.xor(() -> false).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.xor(() -> true).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.xor(() -> false).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.xor(() -> true).test(1, "Gumpf", 6))
            .isFalse();

        assertThat(pred3.xor(() -> Boolean.FALSE).test(17, "?", 23))
            .isFalse();
        assertThat(pred3.xor(() -> Boolean.TRUE).test(17, "?", 23))
            .isTrue();
        assertThat(pred3.xor(() -> Boolean.FALSE).test(1, "Gumpf", 6))
            .isTrue();
        assertThat(pred3.xor(() -> Boolean.TRUE).test(1, "Gumpf", 6))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial1() {
        LOGGER.info("Test pred3.partial1");

        assertThatThrownBy(() -> pred3.partial1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.partial1(() -> null).test("Gumpf", 6))
            .isInstanceOf(NullPointerException.class);
        assertThat(pred3.partial1(() -> 1).test("Gumpf", 6))
            .isTrue();
        assertThat(pred3.partial1(() -> 17).test("?", 23))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial2() {
        LOGGER.info("Test pred3.partial2");

        assertThatThrownBy(() -> pred3.partial2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.partial2(() -> null).test(1, 6))
            .isInstanceOf(NullPointerException.class);
        assertThat(pred3.partial2(() -> "Gumpf").test(1, 6))
            .isTrue();
        assertThat(pred3.partial2(() -> "?").test(17, 23))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial3() {
        LOGGER.info("Test pred3.partial3");

        assertThatThrownBy(() -> pred3.partial3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.partial3(() -> null).test(1, "Gumpf"))
            .isInstanceOf(NullPointerException.class);
        assertThat(pred3.partial3(() -> 6).test(1, "Gumpf"))
            .isTrue();
        assertThat(pred3.partial3(() -> 23).test(17, "?"))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testDefer() {
        LOGGER.info("Test pred3.defer");

        assertThatThrownBy(() -> pred3.defer(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(null, null, () -> 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(null, () -> "Gumpf", null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(() -> 3, null, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> pred3.defer(() -> null, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(() -> null, () -> null, () -> 23).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(() -> null, () -> "Gumpf", () -> null).call())
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred3.defer(() -> 4, () -> null, () -> null).call())
            .isInstanceOf(NullPointerException.class);

        Assertions.assertThatNoException()
            .isThrownBy(() -> {
                assertThat(pred3.defer(() -> 4, () -> "Gumpf", () -> 23).call())
                    .isTrue();
            });
    }

}
