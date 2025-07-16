package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class EqTest {

    private static final Logger LOGGER
        = getLogger(EqTest.class);

    private static final class Equal
            implements Eq<Equal> {

        private final int value1;
        private final int value2;

        private Equal(final int value1,
                      final int value2) {
            this.value1
                = value1;
            this.value2
                = value2;
        }

        @Override
        public boolean eq(@NonNull Equal other) {
            return (other.value2 == this.value2);
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Equal equal)) return false;
            return value1 == equal.value1;
        }

        @Override
        public int hashCode() {
            return value1;
        }

    }

    private final Equal equal1
        = new Equal(17, 23);
    private final Equal equal2
        = new Equal(17, 42);

    @SuppressWarnings({"ResultOfMethodCallIgnored", "DataFlowIssue"})
    @Test
    void testEq() {
        LOGGER.info("Test eq.eq");

        assertThatThrownBy(() -> equal1.eq(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(this.equal1.eq(this.equal2))
            .isFalse();
        assertThat(this.equal1.eq(this.equal1))
            .isTrue();

        assertThatThrownBy(() -> equal1.eq(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equal1.eq(null, equal2))
            .isInstanceOf(NullPointerException.class);
        assertThat(equal1.eq(Equal::eq, this.equal2))
            .isFalse();
        assertThat(equal1.eq(Equal::eq, this.equal1))
            .isTrue();
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testHash() {
        LOGGER.info("Test eq.hash");

        assertThat(this.equal1.hash())
            .isEqualTo(this.equal1.hashCode());

        assertThatThrownBy(() -> equal1.hash(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equal1.hash(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThat(this.equal1.hash(equal -> equal.value2))
            .isEqualTo(23);
    }

}
