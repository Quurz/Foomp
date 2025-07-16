package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record2.record2;
import static org.quurz.foomp.base.util.Tuple2.tuple2;
import static org.slf4j.LoggerFactory.getLogger;

class Record2Test {

    private static final Logger LOGGER
        = getLogger(Record2Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRecord2() {
        LOGGER.info("Test Record2.record2 and Record2::new");

        assertThatThrownBy(() -> record2(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2(null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2(23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> record2(23, 23));

        assertThatThrownBy(() -> new Record2<>(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record2<>(null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record2<>(23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> new Record2<>(23, 23));
    }

    @Test
    void testPresent() {
        LOGGER.info("Test record2.isPresent, record2.is1 and record2.is2");

        final var record2
            = record2("TEST", 23);

        assertThat(record2.isPresent())
            .isTrue();
        assertThat(record2.is1())
            .isTrue();
        assertThat(record2.is2())
            .isTrue();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testGet() {
        LOGGER.info("Test record.get, record2.get1 and record2.get2");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.with2(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record2.get())
            .isEqualTo("TEST");
        assertThat(record2.get1())
            .isEqualTo("TEST");
        assertThat(record2.get2())
            .isEqualTo(23);
        assertThat(record2.value1())
            .isEqualTo("TEST");
        assertThat(record2.value2())
            .isEqualTo(23);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith() {
        LOGGER.info("Test record2.with1 and record2.with2");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.with2(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record2.with1("TEST2").get1())
            .isEqualTo("TEST2");
        assertThat(record2.with2(24).get2())
            .isEqualTo(24);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap1() {
        LOGGER.info("Test record2.map1, record2.map and record2.mapFirst");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.map(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.map1(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record2.map1(String::length).get1())
            .isEqualTo(4);
        assertThat(record2.map(String::length).get1())
            .isEqualTo(4);
        assertThat(record2.map1(String::length).get1())
            .isEqualTo(4);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMap2() {
        LOGGER.info("Test record2.map2 and record2.mapSecond");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.map2(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record2.map2(i -> i + 1).get2())
            .isEqualTo(24);
        assertThat(record2.map2(i -> i + 1).get2())
            .isEqualTo(24);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapAll() {
        LOGGER.info("Test record2.mapAll");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.mapAll(null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.mapAll(null, i -> i + 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.mapAll(String::length, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> record2.mapAll(String::length, i -> i + 1));

        assertThat(record2.mapAll(String::length, i -> i + 1))
            .isEqualTo(record2(4, 24));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test record2.lift");

        final var record2
            = record2("TEST", 23);
        final Record2<Function<String, Integer>, Function<Integer, Integer>> nullReturning1liftA
            = record2(s -> null, i -> i + 1);
        final Record2<Function<String, Integer>, Function<Integer, Integer>> nullReturning2liftA
            = record2(String::length, i -> null);
        final Record2<Function<String, Integer>, Function<Integer, Integer>> noNullReturningliftA
            = record2(String::length, i -> i + 1);

        assertThatThrownBy(() -> record2.lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.lift(nullReturning1liftA))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.lift(nullReturning2liftA))
            .isInstanceOf(NullPointerException.class);

        assertThat(record2.lift(noNullReturningliftA))
            .isEqualTo(record2(4, 24));
    }

    @Test
    void testToTuple() {
        LOGGER.info("Test record2.tuple");

        final var record2
            = record2("TEST", 23);

        assertThat(record2.toTuple())
            .isEqualTo(tuple2("TEST", 23));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("Test record2.transmogrify");

        final var record2
            = record2("TEST", 23);

        assertThatThrownBy(() -> record2.transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record2.transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = record2.transmogrify(r -> r.get1() + r.get2());
                assertThat(result)
                    .isEqualTo("TEST23");
            });
    }

    @Test
    void testCopy() {
        LOGGER.info("Test record2.copy");

        final var record2
            = record2("TEST", 23);

        assertThat(record2.copy())
            .isEqualTo(record2("TEST", 23));
    }

    @SuppressWarnings({"ConstantValue", "EqualsWithItself"})
    @Test
    void testHashCodeAndEquals() {
        LOGGER.info("Test record2.hashCode and record2.equals");

        final var record2
            = record2("TEST", 23);
        final var record2Copy
            = record2("TEST", 23);
        final var record2Different1
            = record2("TEST2", 23);
        final var record2Different2
            = record2("TEST", 24);
        final var record2Different3
            = record2("TEST2", 24);

        assertThat(record2)
            .hasSameHashCodeAs(record2Copy);
        assertThat(record2.hashCode())
            .isNotEqualTo(record2Different1.hashCode());
        assertThat(record2.hashCode())
            .isNotEqualTo(record2Different2.hashCode());
        assertThat(record2.hashCode())
            .isNotEqualTo(record2Different3.hashCode());

        assertThat(record2.equals(record2))
            .isTrue();
        assertThat(record2.equals(record2Copy))
            .isTrue();
        assertThat(record2.equals(record2Different1))
            .isFalse();
        assertThat(record2.equals(record2Different2))
            .isFalse();
        assertThat(record2.equals(record2Different3))
            .isFalse();
        assertThat(record2.equals(null))
            .isFalse();
        assertThat(record2.equals(new Object()))
            .isFalse();
    }

    @Test
    void testToString() {
        LOGGER.info("Test record2.toString");

        final var record2
            = record2("TEST", 23);

        assertThat(record2)
            .hasToString("Record2[value1=TEST, value2=23]");
    }

}
