package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestWitnessType;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.Higher2;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class Mappable2Test {

    private static final Logger LOGGER
        = getLogger(Mappable2Test.class);

    private static final class TestMappable2<A1, A2>
            implements Mappable2<TestWitnessType, A1, A2>,
                       Higher2<TestWitnessType, A1, A2> {

        private final A1 first;
        private final A2 second;

        private TestMappable2(final A1 first,
                              final A2 second) {
            this.first
                = first;
            this.second
                = second;
        }

        @Override
        public @NonNull <B1, B2> TestMappable2<B1, B2> mapAll(
                final @NonNull Function<? super A1, ? extends B1> transformation1,
                final @NonNull Function<? super A2, ? extends B2> transformation2) {
            return new TestMappable2<>(transformation1.apply(this.first), transformation2.apply(this.second));
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof TestMappable2<?, ?> that)) return false;

            return Objects.equals(first, that.first) && Objects.equals(second, that.second);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(first);
            result = 31 * result + Objects.hashCode(second);
            return result;
        }
    }

    private final TestMappable2<String, Integer> mappable2
        = new TestMappable2<>("TEST", 42);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap1() {
        LOGGER.info("Test mappable2.map");

        assertThatThrownBy(() -> this.mappable2.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> this.mappable2.map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> this.mappable2.map1(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(this.mappable2.map1(Fun.identity()))
            .isEqualTo(this.mappable2.map(Fun.identity()))
            .isEqualTo(this.mappable2.map1(Function.identity()));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap2() {
        LOGGER.info("Test mappable2.map2");

        assertThatThrownBy(() -> this.mappable2.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> this.mappable2.map2(null))
            .isInstanceOf(NullPointerException.class);
    }

}
