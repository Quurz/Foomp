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

class Mappable4Test {

    private static final Logger LOGGER
        = getLogger(Mappable4Test.class);

    private static final class TestMappable4<A1, A2, A3, A4>
            implements Mappable4<TestWitnessType, A1, A2, A3, A4> {

        private final A1 first;
        private final A2 second;
        private final A3 third;
        private final A4 fourth;

        private TestMappable4(final A1 first,
                              final A2 second,
                              final A3 third,
                              final A4 fourth) {
            this.first
                = first;
            this.second
                = second;
            this.third
                = third;
            this.fourth
                = fourth;
        }

        @Override
        public @NonNull <B1, B2, B3, B4> Mappable4<TestWitnessType, B1, B2, B3, B4> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                                                           final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                                                           final @NonNull Function<? super A3, ? extends B3> transformation3,
                                                                                           final @NonNull Function<? super A4, ? extends B4> transformation4) {
            return new TestMappable4<>(transformation1.apply(this.first), transformation2.apply(this.second), transformation3.apply(this.third), transformation4.apply(this.fourth));
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof TestMappable4<?, ?, ?, ?> that)) return false;
            return Objects.equals(first, that.first)
                    && Objects.equals(second, that.second)
                    && Objects.equals(third, that.third)
                    && Objects.equals(fourth, that.fourth);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(first);
            result = 31 * result + Objects.hashCode(second);
            result = 31 * result + Objects.hashCode(third);
            result = 31 * result + Objects.hashCode(fourth);
            return result;
        }
    }

    private final TestMappable4<String, Integer, Boolean, Double> mappable4
        = new TestMappable4<>("TEST", 42, true, 3.00d);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap1() {
        LOGGER.info("Test mappable4.map1");

        assertThatThrownBy(() -> this.mappable4.map1(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable4.map1(String::length))
            .isEqualTo(new TestMappable4<>(4, 42, true, 3.00d));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap2() {
        LOGGER.info("Test mappable4.map2");

        assertThatThrownBy(() -> this.mappable4.map2(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable4.map2(i -> i + 1))
            .isEqualTo(new TestMappable4<>("TEST", 43, true, 3.00d));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap3() {
        LOGGER.info("Test mappable4.map3");

        assertThatThrownBy(() -> this.mappable4.map3(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable4.map3(b -> !b))
            .isEqualTo(new TestMappable4<>("TEST", 42, false, 3.00d));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap4() {
        LOGGER.info("Test mappable4.map4");

        assertThatThrownBy(() -> this.mappable4.map4(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable4.map4(d -> d + 1))
            .isEqualTo(new TestMappable4<>("TEST", 42, true, 4.00d));
    }

}
