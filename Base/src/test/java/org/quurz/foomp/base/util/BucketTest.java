package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.secure;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Bucket.bucket;
import static org.quurz.foomp.base.util.Bucket.toBucket;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({"DataFlowIssue", "MismatchedQueryAndUpdateOfCollection", "unused"})
class BucketTest {

    static final Logger LOGGER
        = getLogger(BucketTest.class);

    static final Consumer<List<Object>> NULL_SINK
        = o -> {};

    static class Sink<A>
            implements Consumer<List<A>> {

        List<A> elements
            = new ArrayList<>();

        @Override
        public void accept(final @NonNull List<A> as) {
            this.elements.addAll(as);
        }

        public void clear() {
            this.elements.clear();
        }

    }

    @Test
    void testBucketWithIllegalMaxSize() {
        LOGGER.info("Trying to create a bucket with an illegal max-size");

        assertThatThrownBy(() -> bucket(0, NULL_SINK))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxSize");
    }

    @Test
    void testBucketWithIllegalSink() {
        LOGGER.info("Trying to create a bucket with an illegal sink");

        assertThatThrownBy(() -> bucket(1, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("sink");
    }

    @Test
    void testAddWithIllegalElement() {
        LOGGER.info("Trying to add a null element to a bucket");

        assertThatThrownBy(() -> bucket(1, NULL_SINK).add(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("element");
    }

    @Test
    void testAddWithoutFlush() {
        LOGGER.info("Trying to add a element to a bucket without flush");

        final var sink
            = new Sink<String>();

        final var bucket
            = bucket(15, sink);

        final var testStrings
                = IntStream.range(0, 10)
                    .mapToObj(i -> secure().next(15))
                    .toList();

        testStrings.forEach(bucket::add);

        assertThat(sink.elements).isEmpty();
    }

    @Test
    void testAddWithExplicitFlush() {
        LOGGER.info("Trying to add a element to a bucket, followed by flush");

        final var sink
            = new Sink<String>();

        final var bucket
            = bucket(15, sink);

        final var testStrings
            = IntStream.range(0, 10)
                .mapToObj(i -> secure().next(15))
                .toList();

        testStrings.forEach(bucket::add);

        bucket.flush();

        assertThat(sink.elements)
            .isNotEmpty()
            .isEqualTo(testStrings);
    }

    @Test
    void testToBucketWithIllegalMaxSize() {
        LOGGER.info("Trying to create a bucket-collector with an illegal max-size");

        final var testStrings
            = new ArrayList<String>();
        assertThatThrownBy(() -> testStrings.stream().collect(toBucket(0, NULL_SINK)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxSize");
    }

    @Test
    void testToBucketWithIllegalSink() {
        LOGGER.info("Trying to create a bucket-collector with an illegal sink");

        final var testStrings
                = new ArrayList<String>();
        assertThatThrownBy(() -> testStrings.stream().collect(toBucket(1, null)))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("sink");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testToBucket() {
        LOGGER.info("Test bucket-collector (sequential)");

        final var sink
            = new Sink<String>();

        final var testStrings
            = IntStream.range(0, 24)
                .mapToObj(i -> secure().next(15))
                .toList();

        testStrings.stream()
            .collect(toBucket(50, sink));

        assertThat(sink.elements)
            .isNotEmpty()
            .isEqualTo(testStrings);
    }

    @Test
    void testToBucketParallel() {
        LOGGER.info("Test bucket-collector (parallel)");

        final var sink
            = new Sink<String>();

        final var testStrings
            = IntStream.range(0, 300)
                .mapToObj(i -> secure().next(15))
                .toList();

        final var v
            = testStrings.parallelStream()
                .collect(toBucket(5, sink));

        assertThat(sink.elements)
            .isNotEmpty()
            .hasSameSizeAs(testStrings)
            .hasSameElementsAs(testStrings);
    }

}
