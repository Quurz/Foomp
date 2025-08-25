package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import static org.quurz.foomp.base.localisation.BaseMessages.negativeValue;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;

/**
 * <div>
 *   <p>
 *     A simple in-memory batching buffer with overflow behaviour (“bucket”). Elements are accumulated
 *     until the configured maximum size is reached; then the batch is flushed to a provided sink,
 *     the bucket is emptied, and accumulation continues.
 *   </p>
 *   <p>
 *     Intended use cases:
 *   </p>
 *   <ul>
 *     <li>Event/callback pipelines where items arrive one-by-one and should be processed in batches.</li>
 *     <li>Batching for sinks like databases, file/IO, message queues, or HTTP endpoints.</li>
 *     <li>Non-streaming scenarios where you want explicit add()/flush() control.</li>
 *   </ul>
 *   <p>
 *     Notes and guidance:
 *   </p>
 *   <ul>
 *     <li>For stream pipelines on recent JDKs, prefer {@code java.util.stream.Gatherers.windowFixed(int)}
 *         for size-based chunking.</li>
 *     <li>Thread-safety: this implementation uses a write lock around mutation and flush. The current
 *         design invokes the sink while holding the write lock. If the sink is slow or blocking, consider
 *         adapting the implementation to copy-and-release the lock before calling the sink.</li>
 *     <li>Memory: the buffer grows up to {@code maxSize}. There is no time-based flushing in this class.</li>
 *     <li>Future extensions (not implemented here): size-or-time flushing, retry policies on sink failures,
 *         dead-letter sinks, asynchronous sinks with bounded parallelism, metrics hooks.</li>
 *   </ul>
 * </div>
 *
 * @param <A> element type accepted by this bucket
 *
 * @since 1.0.0
 */
public class Bucket<A> {

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Bucket}.
     *   </p>
     *   <p>
     *     Contract:
     *   </p>
     *   <ul>
     *     <li>{@code maxSize} must be at least 1.</li>
     *     <li>{@code sink} must not be {@code null} and must tolerate being called multiple times,
     *         possibly with empty lists if used incorrectly.</li>
     *   </ul>
     * </div>
     *
     * @param maxSize the maximum number of elements per batch; must be {@code >= 1}
     * @param sink    the consumer invoked on flush with the current batch; must not be {@code null}
     * @param <A>     the element type
     * @return a new {@code Bucket<A>}
     * @throws IllegalArgumentException if {@code maxSize < 1}
     * @throws NullPointerException     if {@code sink == null}
     *
     * @since 1.0.0
     */
    public static <A> Bucket<A> bucket(final int maxSize,
                                       final @NonNull Consumer<List<A>> sink) {
        if (maxSize < 1) {
            throw new IllegalArgumentException(negativeValue("maxSize"));
        }
        Objects.requireNonNull(sink, nullValue("sink"));
        return new Bucket<>(maxSize, sink);
    }

    /**
     * <div>
     *   <p>
     *     Creates a {@link Collector} that routes stream elements through a {@code Bucket} and ensures a final flush.
     *   </p>
     *   <p>
     *     Caveats:
     *   </p>
     *   <ul>
     *     <li>In parallel streams, encounter order is not guaranteed by this collector’s simple combiner.</li>
     *     <li>This collector batches by size only. For modern JDKs, {@code Gatherers.windowFixed(maxSize)}
     *         is usually the more idiomatic choice for pure stream pipelines.</li>
     *   </ul>
     * </div>
     *
     * @param maxSize the maximum batch size; must be {@code >= 1}
     * @param sink    the sink invoked with each flushed batch; must not be {@code null}
     * @param <A>     the element type
     * @return a collector that batches elements into the given sink via an internal {@code Bucket}
     *
     * @see java.util.stream.Collector
     *
     * @throws IllegalArgumentException if {@code maxSize < 1}
     * @throws NullPointerException     if {@code sink == null}
     *
     * @since 1.0.0
     */
    public static <A> Collector<A, List<A>, Nothing> toBucket(final int maxSize,
                                                              final @NonNull Consumer<List<A>> sink) {
        if (maxSize < 1) {
            throw new IllegalArgumentException(negativeValue("maxSize"));
        }
        Objects.requireNonNull(sink, nullValue("sink"));
        final var bucket
            = bucket(maxSize, sink);
        return Collector.of(
            ArrayList::new,
            List::add,
            (accu1, accu2) -> {
                accu1.addAll(accu2);
                if (accu1.size() >= maxSize) {
                    accu1.forEach(bucket::add);
                    return new ArrayList<>();
                } else {
                    return accu1;
                }
            },
            accu -> {
                accu.forEach(bucket::add);
                bucket.flush();
                return nothing;
            }
        );
    }

    private final int maxSize;
    private int position;
    private final Consumer<List<A>> sink;
    private final Object[] elements;

    private final ReadWriteLock readWriteLock;

    private Bucket(final int maxSize,
                   final Consumer<List<A>> sink) {
        this.maxSize
            = maxSize;
        this.position
            = 0;
        this.sink
            = sink;
        this.elements
            = new Object[maxSize];

        this.readWriteLock
            = new ReentrantReadWriteLock(true);
    }

    /**
     * <div>
     *   <p>
     *     Adds an element to the bucket. When the bucket reaches {@code maxSize}, the current batch
     *     is flushed to the sink and the bucket is cleared.
     *   </p>
     *   <p>
     *     Concurrency: acquires the write lock. If the flush threshold is reached, {@link #flush()} is called
     *     within the same critical section.
     *   </p>
     * </div>
     *
     * @param element the element to add; must not be {@code null}
     *
     * @throws NullPointerException if {@code element == null}
     *
     * @since 1.0.0
     */
    public void add(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        this.readWriteLock.writeLock().lock();
        try {
            this.elements[this.position++]
                = element;
            if (this.position == this.maxSize) {
                this.flush();
            }
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    /**
     * <div>
     *   <p>
     *     Flushes any currently buffered elements to the sink and clears the bucket.
     *   </p>
     *   <p>
     *     Implementation notes:
     *   </p>
     *   <ul>
     *     <li>This method holds the write lock while copying the current batch and while invoking the sink.</li>
     *     <li>If the sink can block or is slow, consider an alternative design that copies under the lock,
     *         releases the lock, and only then invokes the sink.</li>
     *   </ul>
     * </div>
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public void flush() {
        this.readWriteLock.writeLock().lock();
        try {
            final var list
                = IntStream.range(0, this.position)
                    .mapToObj(i -> (A) this.elements[i])
                    .toList();
            this.sink.accept(list);
            this.position = 0;
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

}
