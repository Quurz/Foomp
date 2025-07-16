package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestWitnessType;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class Mappable3Test {

    private static final Logger LOGGER
        = getLogger(Mappable3Test.class);

    private static final class TestMappable3<A1, A2, A3>
            implements Mappable3<TestWitnessType, A1, A2, A3> {

        private final A1 first;
        private final A2 second;
        private final A3 third;

        private TestMappable3(final A1 first,
                              final A2 second,
                              final A3 third) {
            this.first
                = first;
            this.second
                = second;
            this.third
                = third;
        }

        @Override
        public <B1, B2, B3> Mappable3<TestWitnessType, B1, B2, B3> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                                          final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                                          final @NonNull Function<? super A3, ? extends B3> transformation3) {
            return new TestMappable3<>(transformation1.apply(this.first), transformation2.apply(this.second), transformation3.apply(this.third));
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof TestMappable3<?, ?, ?> that)) return false;
            return Objects.equals(first, that.first) && Objects.equals(second, that.second) && Objects.equals(third, that.third);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(first);
            result = 31 * result + Objects.hashCode(second);
            result = 31 * result + Objects.hashCode(third);
            return result;
        }
    }

    private final TestMappable3<String, Integer, Boolean> mappable3
        = new TestMappable3<>("TEST", 42, true);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap1() {
        LOGGER.info("Test mappable3.map1");

        assertThatThrownBy(() -> this.mappable3.map1(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable3.map1(String::length))
            .isEqualTo(new TestMappable3<>(4, 42, true));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap2() {
        LOGGER.info("Test mappable3.map2");

        assertThatThrownBy(() -> this.mappable3.map2(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable3.map2(i -> i + 1))
            .isEqualTo(new TestMappable3<>("TEST", 43, true));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap3() {
        LOGGER.info("Test mappable3.map3");

        assertThatThrownBy(() -> this.mappable3.map3(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable3.map3(b -> !b))
            .isEqualTo(new TestMappable3<>("TEST", 42, false));
    }

}
