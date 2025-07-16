package org.quurz.foomp.base.functions;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.functions.MemoisingFun.memoisingFun;
import static org.slf4j.LoggerFactory.getLogger;

class MemoisingFunTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(MemoisingFunTest.class);

    @Test
    @SuppressWarnings({"DataFlowIssue", "unused"})
    void testMemoiseFun() {
        LOGGER.info("Test MemoisingFun.memoisingFun");

        final Fun<Integer, Integer> fun
            = x -> x * x;
        assertThatThrownBy(() -> memoisingFun(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> memoisingFun(_$ -> null).apply(1))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> memoisingFun(Fun.identity()));
        assertThat(memoisingFun(fun).apply(2))
            .isEqualTo(fun.apply(2));
    }

    @Test
    void testApply() {
        LOGGER.info("Test memoisingFun.apply");

        final Fun<Integer, Integer> fun
            = x -> x * x;
        final InvocationCountingFun<Integer, Integer> invocationCountingFun
            = invocationCountingFun(fun);
        final var memoisingFun
            = memoisingFun(invocationCountingFun);

        memoisingFun.apply(2);
        assertThat(invocationCountingFun.getInvocationCount())
            .isEqualTo(1);

        memoisingFun.apply(2);
        assertThat(invocationCountingFun.getInvocationCount())
            .isEqualTo(1);

        memoisingFun.clear().apply(2);
        assertThat(invocationCountingFun.getInvocationCount())
            .isEqualTo(2);
    }

}
