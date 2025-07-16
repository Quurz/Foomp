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
 * <p>
 *     Ein Eimerchen. Ein Eimerchen sogar mit &Uuml;berlauf.
 * </p>
 *
 * <p>
 *     Der <code>Bucket</code> nimmt solange Elemente an, bis seine maximale Gr&ouml;&szlig;e erreicht ist,
 *     um diese Elemente dann in den Abfluss zu gie&szlig;en. Danach ist er bereit, wieder Elemente aufzunehmen.<br />
 *     Ad Infinitum! &#128513;
 * </p>
 *
 * @param <A> Typ der Elemente, die der <code>Bucket</code> aufnehmen kann.
 *
 * @since 1.0.0
 */
public class Bucket<A> {

    /**
     * <div>
     *     <p>
     *         Baut einen neuen <code>Bucket</code>
     *     </p>
     * </div>
     *
     * @param maxSize Die maximale Gr&ouml;&szlig; des <code>Bucket</code>s
     * @param sink Der Abfluss
     * @return Der neue <code>Bucket</code>
     * @param <A> Typ der Elemente, die der <code>Bucket</code> aufnehmen kann.
     *
     * @throws IllegalArgumentException Falls <code>maxSize &lt; 1</code>
     * @throws NullPointerException Falls <code>sink &#61;&#61; null</code>
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
     *     <p>Erzeugt einen <code>Collector</code> für einen <code>Bucket</code></p>
     *     <p>&#x26A0; Obacht! Als <code>Collector</code> in einem parallelen Stream wird die Reihenfolge nicht beachtet!</p>
     * </div>
     *
     * @param maxSize Die maximale Gr&ouml;&szlig; des <code>Bucket</code>s
     * @param sink Der Abfluss
     * @return Der neue <code>Collector</code>
     * @param <A> Typ der Elemente, die der <code>Bucket</code> aufnehmen kann.
     *
     * @see java.util.stream.Collector
     *
     * @throws IllegalArgumentException Falls <code>maxSize &lt; 1</code>
     * @throws NullPointerException Falls <code>sink &#61;&#61; null</code>
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
     *     <p>
     *         F&uuml;gt dem <code>Bucket</code> ein neues Element hinzu
     *     </p>
     * </div>
     *
     * @param element Das neue Element
     *
     * @throws NullPointerException Falls <code>element &#61;&#61; null</code>
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
     *     <p>
     *         Gie&szlig;t den eventuell vorhandenen Rest an Elementen in diesem <code>Bucket</code> in den Abfluss.
     *     </p>
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
