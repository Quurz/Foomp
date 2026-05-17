package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Pred.alwaysFalse;
import static org.quurz.foomp.base.functions.Pred.alwaysTrue;
import static org.quurz.foomp.base.functions.Pred.not;
import static org.quurz.foomp.base.functions.Pred.pred;
import static org.slf4j.LoggerFactory.getLogger;

class PredTest {

    private static final Logger LOGGER
        = getLogger(PredTest.class);


    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAlwaysTrue() {
        LOGGER.info("Test Pred.AlwaysTrue");

        assertThatThrownBy(() -> alwaysTrue().test(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(alwaysTrue().test(5))
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAlwaysFalse() {
        LOGGER.info("Test Pred.AlwaysFalse");

        assertThatThrownBy(() -> alwaysFalse().test(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(alwaysFalse().test(5))
            .isFalse();
    }

    @SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored"})
    @Test
    void testPredicate() {
        LOGGER.info("Test Pred.pred");

        final Predicate<Integer> predicate
            = a -> a < 5;

        assertThatThrownBy(() -> pred(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> pred(predicate).test(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                var result
                    = pred(predicate).test(0);
                assertThat(result)
                    .isTrue();
                result
                    = pred(predicate).test(5);
                assertThat(result)
                    .isFalse();
            });

    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testNot() {
        LOGGER.info("Test Pred.not");

        final Predicate<Integer> predicate
            = a -> a < 5;

        assertThatThrownBy(() -> not(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> not(predicate).test(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                var result
                    = not(predicate).test(0);
                assertThat(result)
                    .isFalse();
                result
                    = not(predicate).test(5);
                assertThat(result)
                    .isTrue();
            });
    }

    @SuppressWarnings({"ConfusingArgumentToVarargsMethod", "DataFlowIssue"})
    @Test
    void testAndStatic() {
        LOGGER.info("Test Pred.and");

        assertThatThrownBy(() -> Pred.and(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.and(alwaysTrue(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.and(null, alwaysTrue()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.and(alwaysTrue(), alwaysTrue(), (Pred<Object>[]) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.and(alwaysTrue(), alwaysTrue(), alwaysTrue()).test(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(Pred.and(alwaysTrue(), alwaysTrue(), alwaysTrue()).test(5))
            .isTrue();
        assertThat(Pred.and(alwaysTrue(), alwaysTrue(), alwaysFalse()).test(5))
            .isFalse();
        assertThat(Pred.and(alwaysTrue(), alwaysFalse(), alwaysTrue()).test(5))
            .isFalse();
        assertThat(Pred.and(alwaysFalse(), alwaysTrue(), alwaysTrue()).test(5))
            .isFalse();
    }

    @SuppressWarnings({"ConfusingArgumentToVarargsMethod", "DataFlowIssue"})
    @Test
    void testOrStatic() {
        LOGGER.info("Test static Pred.or");

        assertThatThrownBy(() -> Pred.or(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.or(alwaysTrue(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.or(null, alwaysTrue()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.or(alwaysTrue(), alwaysTrue(), (Pred<Object>[]) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.or(alwaysTrue(), alwaysTrue(), alwaysTrue()).test(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(Pred.or(alwaysFalse(), alwaysFalse(), alwaysFalse()).test(5))
            .isFalse();
        assertThat(Pred.or(alwaysFalse(), alwaysFalse(), alwaysTrue()).test(5))
            .isTrue();
        assertThat(Pred.or(alwaysFalse(), alwaysTrue(), alwaysFalse()).test(5))
            .isTrue();
        assertThat(Pred.or(alwaysTrue(), alwaysFalse(), alwaysFalse()).test(5))
            .isTrue();
    }

    @SuppressWarnings({"ConfusingArgumentToVarargsMethod", "DataFlowIssue"})
    @Test
    void testXorStatic() {
        LOGGER.info("Test static Pred.xor");

        assertThatThrownBy(() -> Pred.xor(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.xor(alwaysTrue(), null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.xor(null, alwaysTrue()))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.xor(alwaysTrue(), alwaysTrue(), (Pred<Object>[]) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Pred.xor(alwaysTrue(), alwaysTrue(), alwaysTrue()).test(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(Pred.xor(alwaysFalse(), alwaysFalse()).test(5))
            .isFalse();
        assertThat(Pred.xor(alwaysTrue(), alwaysTrue()).test(5))
            .isFalse();
        assertThat(Pred.xor(alwaysTrue(), alwaysFalse()).test(5))
            .isTrue();
        assertThat(Pred.xor(alwaysFalse(), alwaysTrue()).test(5))
            .isTrue();
        assertThat(Pred.xor(alwaysTrue(), alwaysTrue(), alwaysTrue(), alwaysTrue()).test(5))
            .isFalse();
        assertThat(Pred.xor(alwaysTrue(), alwaysFalse(), alwaysTrue(), alwaysFalse()).test(5))
            .isFalse();
        assertThat(Pred.xor(alwaysTrue(), alwaysFalse(), alwaysFalse(), alwaysFalse()).test(5))
            .isTrue();
        assertThat(Pred.xor(alwaysFalse(), alwaysFalse(), alwaysFalse(), alwaysTrue()).test(5))
            .isTrue();
    }

    @Test
    void testNegate() {
        LOGGER.info("Test pred.negate");

        assertThat(alwaysTrue().negate().test(5))
            .isFalse();
        assertThat(alwaysFalse().negate().test(5))
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testAnd() {
        LOGGER.info("Test pred.and");

        assertThatThrownBy(() -> alwaysTrue().and((Pred<Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().and((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().and((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().and(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);

        assertThat(alwaysFalse().and(alwaysFalse()).test(5))
            .isFalse();
        assertThat(alwaysFalse().and(alwaysTrue()).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(alwaysFalse()).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(alwaysTrue()).test(5))
            .isTrue();

        assertThat(alwaysFalse().and(() -> false).test(5))
            .isFalse();
        assertThat(alwaysFalse().and(() -> true).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(() -> false).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(() -> true).test(5))
            .isTrue();

        assertThat(alwaysFalse().and(() -> Boolean.FALSE).test(5))
            .isFalse();
        assertThat(alwaysFalse().and(() -> Boolean.TRUE).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(() -> Boolean.FALSE).test(5))
            .isFalse();
        assertThat(alwaysTrue().and(() -> Boolean.TRUE).test(5))
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testNand() {
        LOGGER.info("Test pred.nand");

        assertThatThrownBy(() -> alwaysTrue().nand((Pred<Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().nand((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().nand((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().nand(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);

        assertThat(alwaysFalse().nand(alwaysFalse()).test(5))
            .isTrue();
        assertThat(alwaysFalse().nand(alwaysTrue()).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(alwaysFalse()).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(alwaysTrue()).test(5))
            .isFalse();

        assertThat(alwaysFalse().nand(() -> false).test(5))
            .isTrue();
        assertThat(alwaysFalse().nand(() -> true).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(() -> false).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(() -> true).test(5))
            .isFalse();

        assertThat(alwaysFalse().nand(() -> Boolean.FALSE).test(5))
            .isTrue();
        assertThat(alwaysFalse().nand(() -> Boolean.TRUE).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(() -> Boolean.FALSE).test(5))
            .isTrue();
        assertThat(alwaysTrue().nand(() -> Boolean.TRUE).test(5))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testOr() {
        LOGGER.info("Test Pred.or");

        assertThatThrownBy(() -> alwaysTrue().or((Pred<Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().or((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().or((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysFalse().or(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);

        assertThat(alwaysFalse().or(alwaysFalse()).test(5))
            .isFalse();
        assertThat(alwaysFalse().or(alwaysTrue()).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(alwaysFalse()).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(alwaysTrue()).test(5))
            .isTrue();

        assertThat(alwaysFalse().or(() -> false).test(5))
            .isFalse();
        assertThat(alwaysFalse().or(() -> true).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(() -> false).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(() -> true).test(5))
            .isTrue();

        assertThat(alwaysFalse().or(() -> Boolean.FALSE).test(5))
            .isFalse();
        assertThat(alwaysFalse().or(() -> Boolean.TRUE).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(() -> Boolean.FALSE).test(5))
            .isTrue();
        assertThat(alwaysTrue().or(() -> Boolean.TRUE).test(5))
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testNor() {
        LOGGER.info("Test Pred.nor");

        assertThatThrownBy(() -> alwaysTrue().nor((Pred<Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().nor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().nor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysFalse().nor(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);

        assertThat(alwaysFalse().nor(alwaysFalse()).test(5))
            .isTrue();
        assertThat(alwaysFalse().nor(alwaysTrue()).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(alwaysFalse()).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(alwaysTrue()).test(5))
            .isFalse();

        assertThat(alwaysFalse().nor(() -> false).test(5))
            .isTrue();
        assertThat(alwaysFalse().nor(() -> true).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(() -> false).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(() -> true).test(5))
            .isFalse();

        assertThat(alwaysFalse().nor(() -> Boolean.FALSE).test(5))
            .isTrue();
        assertThat(alwaysFalse().nor(() -> Boolean.TRUE).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(() -> Boolean.FALSE).test(5))
            .isFalse();
        assertThat(alwaysTrue().nor(() -> Boolean.TRUE).test(5))
            .isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testXor() {
        LOGGER.info("Test Pred.xor");

        assertThatThrownBy(() -> alwaysTrue().xor((Pred<Object>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().xor((BooleanSupplier) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysTrue().xor((Supplier<Boolean>) null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> alwaysFalse().xor(() -> null).test(5))
            .isInstanceOf(NullPointerException.class);

        assertThat(alwaysFalse().xor(alwaysFalse()).test(5))
            .isFalse();
        assertThat(alwaysFalse().xor(alwaysTrue()).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(alwaysFalse()).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(alwaysTrue()).test(5))
            .isFalse();

        assertThat(alwaysFalse().xor(() -> false).test(5))
            .isFalse();
        assertThat(alwaysFalse().xor(() -> true).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(() -> false).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(() -> true).test(5))
            .isFalse();

        assertThat(alwaysFalse().xor(() -> Boolean.FALSE).test(5))
            .isFalse();
        assertThat(alwaysFalse().xor(() -> Boolean.TRUE).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(() -> Boolean.FALSE).test(5))
            .isTrue();
        assertThat(alwaysTrue().xor(() -> Boolean.TRUE).test(5))
            .isFalse();
    }


}
