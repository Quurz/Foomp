package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.higher.Higher1;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.requireNonNullElementsInArray;
import static org.quurz.foomp.base.util.Util.requireNonNullElementsInCollection;

@SuppressWarnings("NonAsciiCharacters")
public class Sequence<A>
        implements Seq<A>,
                   Higher1<Sequence.µ, A> {

    public static final class µ extends Seq.µ { private µ() {} }

    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequence() {
        return (Sequence<A>) EMPTY_SEQUENCE;
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <A> Sequence<A> sequenceOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        final Sequence<A> newSequence;
        if (elements.length > 0) {
            final Segment<A> segment
                = new Segment<>();
            requireNonNullElementsInArray(
                elements,
                segment::appendNoCopy,
                index -> new NullPointerException(nullElementInAt("elements", index))
            );
            newSequence
                = new Sequence<>(segment, segment);
        } else {
            newSequence
                = (Sequence<A>) EMPTY_SEQUENCE;
        }
        return newSequence;
    }

    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequenceFrom(final @NonNull Collection<A> collection) {
        Objects.requireNonNull(collection, nullValue("collection"));
        final Sequence<A> newSequence;
        if (!collection.isEmpty()) {
            final Segment<A> segment
                = new Segment<>();
            requireNonNullElementsInCollection(
                collection,
                segment::appendNoCopy,
                index -> new NullPointerException(nullElementInAt("collection", index))
            );

            newSequence
                = new Sequence<>(segment, segment);
        } else {
            newSequence
                = (Sequence<A>) EMPTY_SEQUENCE;
        }
        return newSequence;
    }

    private static final class Segment<A> {

        @SuppressWarnings("unchecked")
        private Segment() {
            this.spool
                = (Fun<? super Object, A>) Fun.identity();
        }

        private Segment<A> nextSegment;
        private Segment<A> previousSegment;

        private DoubleLinked<A> firstElement;
        private DoubleLinked<A> lastElement;
        private Fun<? super Object, A> spool;

        private void appendNoCopy(final @NonNull A element) {
            if (this.firstElement == null) {
                this.firstElement
                    = new DoubleLinked<>(element);
                this.lastElement
                    = this.firstElement;
            } else {
                this.lastElement
                    = this.lastElement.appendNoCopy(element);
            }
        }

    }

    private static final Sequence<?> EMPTY_SEQUENCE
        = new Sequence<>(null, null);

    private final Segment<A> firstSegment;
    private final Segment<A> lastSegment;

    private Sequence(final Segment<A> firstSegment,
                     final Segment<A> lastSegment) {
        this.firstSegment
            = firstSegment;
        this.lastSegment
            = lastSegment;
    }

    @Override
    public boolean isNotEmpty() {
        return this.firstSegment != null;
    }

    @Override
    public @NonNull A head()
            throws NoSuchElementException {
        if (   this.firstSegment != null
            && this.firstSegment.firstElement != null) {
            return this.firstSegment.spool.apply(firstSegment.firstElement);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public @NonNull Maybe<A> headSafe() {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> tail()
            throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Sequence<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    public Sequence<A> prepend(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return this.cons(element);
    }

    public Sequence<A> append(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    @Override
    public @NonNull Tuple2<A, Sequence<A>> decons()
            throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred) {
        Objects.requireNonNull(pred, nullValue("pred"));
        return null;    // TODO
    }

    @Override
    public @NonNull <C extends Collection<? super A>> C toCollection(final @NonNull Supplier<C> init) {
        Objects.requireNonNull(init, nullValue("init"));
        return null;    // TODO
    }

}
