package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
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

@DisplayName("Bucket")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"DataFlowIssue", "MismatchedQueryAndUpdateOfCollection", "unused"})
class BucketTest {

    static final Logger LOGGER = getLogger(BucketTest.class);

    static final Consumer<List<Object>> NULL_SINK = o -> {};

    static class Sink<A>
            implements Consumer<List<A>> {
        List<A> elements = new ArrayList<>();

        @Override
        public void accept(final @NonNull List<A> as) {
            this.elements.addAll(as);
        }

        public void clear() {
            this.elements.clear();
        }
    }

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void creating_bucket_with_illegal_max_size_throws() {
            LOGGER.info("Trying to create a bucket with an illegal max-size");
            assertThatThrownBy(() -> bucket(0, NULL_SINK))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maxSize");
        }

        @Test
        void creating_bucket_with_null_sink_throws() {
            LOGGER.info("Trying to create a bucket with an illegal sink");
            assertThatThrownBy(() -> bucket(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("sink");
        }
    }

    @Nested
    @DisplayName("Add/Flush")
    class Add_Flush {

        @Test
        void adding_null_element_throws() {
            LOGGER.info("Trying to add a null element to a bucket");
            assertThatThrownBy(() -> bucket(1, NULL_SINK).add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("element");
        }

        @Test
        void add_without_reaching_threshold_does_not_flush() {
            LOGGER.info("Trying to add elements without reaching threshold, expecting no flush");
            final var sink = new Sink<String>();
            final var b = bucket(15, sink);

            final var data = IntStream.range(0, 10)
                .mapToObj(i -> secure().next(15))
                .toList();

            data.forEach(b::add);

            assertThat(sink.elements).isEmpty();
        }

        @Test
        void explicit_flush_delivers_buffered_elements() {
            LOGGER.info("Add elements below threshold, then flush explicitly");
            final var sink = new Sink<String>();
            final var b = bucket(15, sink);

            final var data = IntStream.range(0, 10)
                .mapToObj(i -> secure().next(15))
                .toList();

            data.forEach(b::add);
            b.flush();

            assertThat(sink.elements)
                .isNotEmpty()
                .isEqualTo(data);
        }
    }

    @Nested
    @DisplayName("Collector")
    class Collector_ {

        @Test
        void toBucket_with_illegal_max_size_throws() {
            LOGGER.info("Trying to create a bucket-collector with an illegal max-size");
            final var data = new ArrayList<String>();
            assertThatThrownBy(() -> data.stream().collect(toBucket(0, NULL_SINK)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("maxSize");
        }

        @Test
        void toBucket_with_null_sink_throws() {
            LOGGER.info("Trying to create a bucket-collector with an illegal sink");
            final var data = new ArrayList<String>();
            assertThatThrownBy(() -> data.stream().collect(toBucket(1, null)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("sink");
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        void sequential_stream_collects_in_order() {
            LOGGER.info("Test bucket-collector (sequential)");
            final var sink = new Sink<String>();

            final var data = IntStream.range(0, 24)
                .mapToObj(i -> secure().next(15))
                .toList();

            data.stream().collect(toBucket(50, sink));

            assertThat(sink.elements)
                .isNotEmpty()
                .isEqualTo(data);
        }

        @Test
        void parallel_stream_collects_all_elements_not_guaranteeing_order() {
            LOGGER.info("Test bucket-collector (parallel)");
            final var sink = new Sink<String>();

            final var data = IntStream.range(0, 300)
                .mapToObj(i -> secure().next(15))
                .toList();

            data.parallelStream().collect(toBucket(5, sink));

            assertThat(sink.elements)
                .isNotEmpty()
                .hasSameSizeAs(data)
                .hasSameElementsAs(data); // order not guaranteed by this collector
        }
    }
}
