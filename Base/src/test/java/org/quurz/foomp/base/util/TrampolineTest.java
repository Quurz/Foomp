package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Trampoline.done;
import static org.quurz.foomp.base.util.Trampoline.more;
import static org.slf4j.LoggerFactory.getLogger;

class TrampolineTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(TrampolineTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTrampolineMore() {
        LOGGER.info("Test Trampoline.more");

        assertThatThrownBy(() -> more(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(more(() -> done(SOME_STRING_VALUE)).isPresent())
            .isFalse();
        assertThat(more(() -> done(SOME_STRING_VALUE)).get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTrampolineDone() {
        LOGGER.info("Test Trampoline.done");

        assertThatThrownBy(() -> done(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(done(SOME_STRING_VALUE).isPresent())
            .isTrue();
        assertThat(done(SOME_STRING_VALUE).get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testTrampoline() {
        LOGGER.info("Test Trampoline");

        final var bounce
            = new Bounce();

        assertThat(bounce.factorial(5))
            .isEqualTo(120);
        assertThat(bounce.hitCount)
            .isEqualTo(5);
    }

    private static final class Bounce {

        private int hitCount
            = 0;

        @SuppressWarnings("SameParameterValue")
        private int factorial(final int param) {
            return this._factorial(param, 1).get();
        }

       private Trampoline<Integer> _factorial(final int param,
                                                 final int accu) {
           ++this.hitCount;
           if (param == 1) {
               return done(accu);
           } else {
               return more(() -> _factorial(param - 1, accu * param));
           }
       }
    }

}
