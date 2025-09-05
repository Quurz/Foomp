package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Trampoline.done;
import static org.quurz.foomp.base.util.Trampoline.more;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Trampoline")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TrampolineTest extends TestHelper {

    private static final Logger LOGGER = getLogger(TrampolineTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void more_should_ThrowNullPointerException_When_SupplierIsNull() {
            LOGGER.info("Trampoline.more(...) should throw NullPointerException when supplier is null");
            assertThatThrownBy(() -> more(null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void done_should_ThrowNullPointerException_When_ResultIsNull() {
            LOGGER.info("Trampoline.done(...) should throw NullPointerException when result is null");
            assertThatThrownBy(() -> done(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour")
    class Behaviour {

        @Test
        void more_should_NotBePresent_And_get_Should_ReturnFinalValue() {
            LOGGER.info("Trampoline.more(...) should not be present and get() should return the final value");
            assertThat(more(() -> done(SOME_STRING_VALUE)).isPresent())
                .isFalse();
            assertThat(more(() -> done(SOME_STRING_VALUE)).get())
                .isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void done_should_BePresent_And_get_Should_ReturnValue() {
            LOGGER.info("Trampoline.done(...) should be present and get() should return the value");
            assertThat(done(SOME_STRING_VALUE).isPresent())
                .isTrue();
            assertThat(done(SOME_STRING_VALUE).get())
                .isEqualTo(SOME_STRING_VALUE);
        }
    }

    @Nested
    @DisplayName("Evaluation example")
    class EvaluationExample {

        @Test
        void trampoline_should_Evaluate_Factorial_Without_StackOverflow() {
            LOGGER.info("Trampoline should evaluate a tail-recursive factorial without stack overflow");
            final var bounce = new Bounce();

            assertThat(bounce.factorial(5))
                .isEqualTo(120);
            assertThat(bounce.hitCount)
                .isEqualTo(5);
        }
    }

    private static final class Bounce {

        private int hitCount = 0;

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
