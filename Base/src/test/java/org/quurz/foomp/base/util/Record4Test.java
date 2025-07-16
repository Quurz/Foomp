package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Record4.record4;
import static org.quurz.foomp.base.util.Tuple4.tuple4;
import static org.slf4j.LoggerFactory.getLogger;

class Record4Test {

    private static final Logger LOGGER
        = getLogger(Record4Test.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRecord4() {
        LOGGER.info("Test Record4.record4 and Record4::new");

        assertThatThrownBy(() -> record4(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, 23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, 23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, 23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(null, 23, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, 23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, 23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4(23, 23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> record4(23, 23, 23, 23));

        assertThatThrownBy(() -> new Record4<>(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, 23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, 23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, 23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(null, 23, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, null, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, null, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, null, 23, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, 23, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, 23, null, 23))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Record4<>(23, 23, 23, null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> new Record4<>(23, 23, 23, 23));

    }

    @Test
    void testPresent() {
        LOGGER.info("Test record4.isPresent, record4.is1, record4.is2, record4.is3 and record4.is4");

        final var record4
            = record4("TEST", 23, "TEST", 23);

        assertThat(record4.isPresent())
            .isTrue();
        assertThat(record4.is1())
            .isTrue();
        assertThat(record4.is2())
            .isTrue();
        assertThat(record4.is3())
            .isTrue();
        assertThat(record4.is4())
            .isTrue();
    }

    @Test
    void testGet() {
        LOGGER.info("Test record4.get, record4.get1, record4.get2, record4.get3 and record4.get4");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThat(record4.get())
            .isEqualTo("TEST");
        assertThat(record4.get1())
            .isEqualTo("TEST");
        assertThat(record4.get2())
            .isEqualTo(23);
        assertThat(record4.get3())
            .isEqualTo(3.0d);
        assertThat(record4.get4())
            .isEqualTo(2.0f);

        assertThat(record4.value1())
            .isEqualTo("TEST");
        assertThat(record4.value2())
            .isEqualTo(23);
        assertThat(record4.value3())
            .isEqualTo(3.0d);
        assertThat(record4.value4())
            .isEqualTo(2.0f);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testWith() {
        LOGGER.info("Test record4.with1, record4.with2, record4.with3 and record4.with4");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThatThrownBy(() -> record4.with1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.with2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.with3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.with4(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record4.with1("TEST2"))
            .isEqualTo(record4("TEST2", 23, 3.0d, 2.0f));
        assertThat(record4.with2(24))
            .isEqualTo(record4("TEST", 24, 3.0d, 2.0f));
        assertThat(record4.with3(4.0d))
            .isEqualTo(record4("TEST", 23, 4.0d, 2.0f));
        assertThat(record4.with4(3.0f))
            .isEqualTo(record4("TEST", 23, 3.0d, 3.0f));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMap() {
        LOGGER.info("Test record4.map1, record4.map2, record4.map3 and record4.map4");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThatThrownBy(() -> record4.map1(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map2(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map3(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map4(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> record4.map1(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map2(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map3(_$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.map4(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record4.map1(String::length))
            .isEqualTo(record4(4, 23, 3.0d, 2.0f));
        assertThat(record4.map2(i -> i + 1))
            .isEqualTo(record4("TEST", 24, 3.0d, 2.0f));
        assertThat(record4.map3(d -> d + 1.0d))
            .isEqualTo(record4("TEST", 23, 4.0d, 2.0f));
        assertThat(record4.map4(f -> f + 1.0f))
            .isEqualTo(record4("TEST", 23, 3.0d, 3.0f));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMapAll() {
        LOGGER.info("Test record4.mapAll");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThatThrownBy(() -> record4.mapAll(null, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, null, null, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, null, d -> d + 1.0d, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, i -> i + 1, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, null, d -> d + 1.0d, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, i -> i + 1, null, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(null, i -> i + 1, d -> d + 1.0d, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, null, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, null, null, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, null, d -> d + 1.0d, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, i -> i + 1, null, null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, null, d -> d + 1.0d, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, i -> i + 1, null, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, i -> i + 1, d -> d + 1.0d, null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> record4.mapAll(String::length, i -> i + 1, d -> d + 1.0d, _$ -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, i -> i + 1, _$ -> null, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(String::length, _$ -> null, d -> d + 1.0d, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.mapAll(_$ -> null, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f))
            .isInstanceOf(NullPointerException.class);

        assertThat(record4.mapAll(String::length, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f))
            .isEqualTo(record4(4, 24, 4.0d, 3.0f));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLift() {
        LOGGER.info("Test record4.lift");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThatThrownBy(() -> record4.lift(null))
            .isInstanceOf(NullPointerException.class);

        assertThat(record4.lift(record4(String::length, i -> i + 1, d -> d + 1.0d, f -> f + 1.0f)))
            .isEqualTo(record4(4, 24, 4.0d, 3.0f));
    }

    @Test
    void testToTuple() {
        LOGGER.info("Test record4.tuple");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThat(record4.toTuple())
            .isEqualTo(tuple4("TEST", 23, 3.0d, 2.0f));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("Test record4.transmogrify");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThatThrownBy(() -> record4.transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> record4.transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> {
                final var result
                    = record4.transmogrify(r -> r.get1() + r.get2() + r.get3() + r.get4());

                assertThat(result)
                    .isEqualTo("TEST233.02.0");
            });
    }

    @Test
    void testCopy() {
        LOGGER.info("Test record4.copy");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThat(record4.copy())
            .isEqualTo(record4("TEST", 23, 3.0d, 2.0f));
    }

    @Test
    void testEqualsAndHashCode() {
        LOGGER.info("Test record4.equals and record4.hashCode");

        final var record4First
            = record4("TEST", 23, 3.0d, 2.0f);
        final var record4Second
            = record4("TEST", 23, 3.0d, 2.0f);
        final var record4Third
            = record4("TEST2", 24, 4.0d, 3.0f);

        assertThat(record4First)
            .isEqualTo(record4First);
        assertThat(record4First)
            .isEqualTo(record4Second);
        assertThat(record4First)
            .isNotEqualTo(record4Third);

        assertThat(record4First)
            .hasSameHashCodeAs(record4Second);
        assertThat(record4First.hashCode())
            .isNotEqualTo(record4Third.hashCode());
    }

    @Test
    void testToString() {
        LOGGER.info("Test record4.toString");

        final var record4
            = record4("TEST", 23, 3.0d, 2.0f);

        assertThat(record4)
            .hasToString("Record4[value1=TEST, value2=23, value3=3.0, value4=2.0]");
    }

}
