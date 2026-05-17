package org.quurz.foomp.base.functions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Pred2.not;
import static org.quurz.foomp.base.functions.Pred2.pred2;
import static org.slf4j.LoggerFactory.getLogger;

class Pred2Test {

    private static final Logger LOGGER
        = getLogger(Pred2Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPred2() {
        LOGGER.info("Test Pred2.pred2");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;

        assertThatThrownBy(() -> pred2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            var result
                = pred2(biPredicate).test(5, "Gumpf");
            assertThat(result)
                .isTrue();
            result
                = pred2(biPredicate).test(5, "?");
            assertThat(result)
                .isFalse();
        });
    }

    @Test
    void testNot() {
        LOGGER.info("Test Pred2.not");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThat(not(pred2).test(5, "Gumpf"))
            .isFalse();
        assertThat(not(pred2).test(5, "?"))
            .isTrue();
    }

    @Test
    void testNegate() {
        LOGGER.info("Test pred2.negate");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThat(pred2.negate().test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.negate().test(5, "?"))
            .isTrue();
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "DataFlowIssue", "unused"})
    @Test
    void testAnd() {
        LOGGER.info("Test pred2.and");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.and((Pred2<Object, Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.and((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.and((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.and(() -> null).test(5, "Gumpf"))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred2.and((_1, _2) -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and((_1, _2) -> true).test(5, "?"))
            .isFalse();
        assertThat(pred2.and((_1, _2) -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and((_1, _2) -> true).test(5, "Gumpf"))
            .isTrue();

        assertThat(pred2.and(() -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and(() -> true).test(5, "?"))
            .isFalse();
        assertThat(pred2.and(() -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and(() -> true).test(5, "Gumpf"))
            .isTrue();

        assertThat(pred2.and(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and(() -> Boolean.TRUE).test(5, "?"))
            .isFalse();
        assertThat(pred2.and(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.and(() -> Boolean.TRUE).test(5, "Gumpf"))
            .isTrue();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testNand() {
        LOGGER.info("Test pred2.nand");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.nand((Pred2<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nand((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nand((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nand(() -> null).test(5, "Gumpf"))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred2.nand((_1, _2) -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand((_1, _2) -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.nand((_1, _2) -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand((_1, _2) -> true).test(5, "Gumpf"))
            .isFalse();

        assertThat(pred2.nand(() -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand(() -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.nand(() -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand(() -> true).test(5, "Gumpf"))
            .isFalse();

        assertThat(pred2.nand(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand(() -> Boolean.TRUE).test(5, "?"))
            .isTrue();
        assertThat(pred2.nand(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.nand(() -> Boolean.TRUE).test(5, "Gumpf"))
            .isFalse();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testOr() {
        LOGGER.info("Test pred2.or");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.or((Pred2<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.or((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.or((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.or(() -> null).test(5, "?"))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred2.or((_1, _2) -> true).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or((_1, _2) -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.or((_1, _2) -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or((_1, _2) -> false).test(5, "?"))
            .isFalse();

        assertThat(pred2.or(() -> true).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or(() -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.or(() -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or(() -> false).test(5, "?"))
            .isFalse();

        assertThat(pred2.or(() -> Boolean.TRUE).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or(() -> Boolean.TRUE).test(5, "?"))
            .isTrue();
        assertThat(pred2.or(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.or(() -> Boolean.FALSE).test(5, "?"))
            .isFalse();
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testNor() {
        LOGGER.info("Test pred2.nor");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.nor((Pred2<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.nor(() -> null).test(5, "?"))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred2.nor((_1, _2) -> true).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor((_1, _2) -> true).test(5, "?"))
            .isFalse();
        assertThat(pred2.nor((_1, _2) -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor((_1, _2) -> false).test(5, "?"))
            .isTrue();

        assertThat(pred2.nor(() -> true).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor(() -> true).test(5, "?"))
            .isFalse();
        assertThat(pred2.nor(() -> false).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor(() -> false).test(5, "?"))
            .isTrue();

        assertThat(pred2.nor(() -> Boolean.TRUE).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor(() -> Boolean.TRUE).test(5, "?"))
            .isFalse();
        assertThat(pred2.nor(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.nor(() -> Boolean.FALSE).test(5, "?"))
            .isTrue();
    }

    @SuppressWarnings({"unused", "DataFlowIssue"})
    @Test
    void testXor() {
        LOGGER.info("Test pred2.xor");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.xor((Pred2<Integer, String>) null))
            .isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> pred2.xor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.xor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.xor(() -> null).test(5, "?"))
            .isInstanceOf(NullPointerException.class);

        assertThat(pred2.xor((_1, _2) -> true).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.xor((_1, _2) -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.xor((_1, _2) -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.xor((_1, _2) -> false).test(5, "?"))
            .isFalse();

        assertThat(pred2.xor(() -> true).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.xor(() -> true).test(5, "?"))
            .isTrue();
        assertThat(pred2.xor(() -> false).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.xor(() -> false).test(5, "?"))
            .isFalse();

        assertThat(pred2.xor(() -> Boolean.TRUE).test(5, "Gumpf"))
            .isFalse();
        assertThat(pred2.xor(() -> Boolean.TRUE).test(5, "?"))
            .isTrue();
        assertThat(pred2.xor(() -> Boolean.FALSE).test(5, "Gumpf"))
            .isTrue();
        assertThat(pred2.xor(() -> Boolean.FALSE).test(5, "?"))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial1() {
        LOGGER.info("Test pred2.partial1");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.partial1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.partial1(() -> null).test("Gumpf"))
            .isInstanceOf(NullPointerException.class);
        assertThat(pred2.partial1(() -> 5).test("?"))
            .isFalse();
        assertThat(pred2.partial1(() -> 5).test("Gumpf"))
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testPartial2() {
        LOGGER.info("Test pred2.partial2");

        final BiPredicate<Integer, String> biPredicate
            = (i, s) -> s.length() == i;
        final var pred2
            = pred2(biPredicate);

        assertThatThrownBy(() -> pred2.partial2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred2.partial2(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);
        assertThat(pred2.partial2(() -> "?").test(5))
            .isFalse();
        assertThat(pred2.partial2(() -> "Gumpf").test(5))
            .isTrue();
    }


}
