package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record3.record3;
import static org.quurz.foomp.base.util.Tuple3.tuple3;
import static org.slf4j.LoggerFactory.getLogger;

class Record3Test {

    private static final Logger LOGGER
        = getLogger(Record3Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRecord3() {
        LOGGER.info("Test Record3.record3 and Record3::new");

        assertThatThrownBy(() -> record3(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3(23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> record3(23, 23, 23));

        assertThatThrownBy(() -> new Record3<>(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record3<>(23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> new Record3<>(23, 23, 23));
    }

    @Test
    void testPresent() {
        LOGGER.info("Test record3.isPresent, record3.is1, record3.is2 and record3.is3");

        final var record3
            = record3("TEST", 23, 23);

        assertThat(record3.isPresent())
            .isTrue();
        assertThat(record3.is1())
            .isTrue();
        assertThat(record3.is2())
            .isTrue();
        assertThat(record3.is3())
            .isTrue();
    }

    @Test
    void testGet() {
        LOGGER.info("Test record3.get, record3.get1, record3.get2 and record3.get3");

        final var record3
            = record3("TEST", 23, 23);

        assertThat(record3.get())
            .isEqualTo("TEST");
        assertThat(record3.get1())
            .isEqualTo("TEST");
        assertThat(record3.get2())
            .isEqualTo(23);
        assertThat(record3.get3())
            .isEqualTo(23);
        assertThat(record3.value1())
            .isEqualTo("TEST");
        assertThat(record3.value2())
            .isEqualTo(23);
        assertThat(record3.value3())
            .isEqualTo(23);

        assertThat(record3.get1())
            .isEqualTo(record3.value1());
        assertThat(record3.get2())
            .isEqualTo(record3.value2());
        assertThat(record3.get3())
            .isEqualTo(record3.value3());
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith() {
        LOGGER.info("Test record3.with1, record3.with2 and record3.with3");

        final var record3
            = record3("TEST", 23, 23);

        assertThatThrownBy(() -> record3.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.with3(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record3.with1("TEST2"))
            .isEqualTo(record3("TEST2", 23, 23));
        assertThat(record3.with2(24))
            .isEqualTo(record3("TEST", 24, 23));
        assertThat(record3.with3(24))
            .isEqualTo(record3("TEST", 23, 24));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test record3.map1, record3.map2 and record3.map3");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThatThrownBy(() -> record3.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.map3(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> record3.map1(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.map2(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.map3(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record3.map1(String::length))
            .isEqualTo(record3(4, 23, 3.0d));
        assertThat(record3.map2(i -> i + 1))
            .isEqualTo(record3("TEST", 24, 3.0d));
        assertThat(record3.map3(d -> d + 1))
            .isEqualTo(record3("TEST", 23, 4.0d));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMapAll() {
        LOGGER.info("Test record3.mapAll");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThatThrownBy(() -> record3.mapAll(null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(null, null, d -> d + 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(null, i -> i + 1, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(null, i -> i + 1, d -> d + 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(String::length, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(String::length, null, d -> d + 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(String::length, i -> i + 1, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> record3.mapAll(String::length, i -> i + 1, _$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(String::length, _$ -> null, d -> d + 1))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.mapAll(_$ -> null, i -> i + 1, d -> d + 1))
            .isInstanceOf(NullPointerException.class);

        assertThat(record3.mapAll(String::length, i -> i + 1, d -> d + 1))
            .isEqualTo(record3(4, 24, 4.0d));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLift() {
        LOGGER.info("Test record3.lift");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThatThrownBy(() -> record3.lift(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record3.lift(record3(String::length, i -> i + 1, d -> d + 1)))
            .isEqualTo(record3(4, 24, 4.0d));
    }

    @Test
    void testToTuple() {
        LOGGER.info("Test record3.tuple");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThat(record3.toTuple())
            .isEqualTo(tuple3("TEST", 23, 3.0d));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("Test record3.transmogrify");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThatThrownBy(() -> record3.transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record3.transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = record3.transmogrify(r -> r.get1() + r.get2() + r.get3());
                assertThat(result)
                    .isEqualTo("TEST233.0");
            });
    }

    @Test
    void testCopy() {
        LOGGER.info("Test record3.copy");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThat(record3.copy())
            .isEqualTo(record3("TEST", 23, 3.0d));
    }

    @Test
    void testHashCodeAndEquals() {
        LOGGER.info("Test record3.hashCode and record3.equals");

        final var record3First
            = record3("TEST", 23, 3.0d);
        final var record3Second
            = record3("TEST", 23, 3.0d);
        final var record3Third
            = record3("TEST2", 23, 3.0d);


        assertThat(record3First.hashCode())
            .isNotEqualTo("GUMPF".hashCode());
        assertThat(record3First)
            .isNotEqualTo("GUMPF");

        assertThat(record3First)
            .hasSameHashCodeAs(record3Second);
        assertThat(record3First.hashCode())
            .isNotEqualTo(record3Third.hashCode());

        assertThat(record3First)
            .isEqualTo(record3First);
        assertThat(record3First)
            .isEqualTo(record3Second);
        assertThat(record3First)
            .isNotEqualTo(record3Third);
    }

    @Test
    void testToString() {
        LOGGER.info("Test record3.toString");

        final var record3
            = record3("TEST", 23, 3.0d);

        assertThat(record3)
            .hasToString("Record3[value1=TEST, value2=23, value3=3.0]");
    }

}
