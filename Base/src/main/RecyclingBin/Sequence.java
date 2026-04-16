package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

@SuppressWarnings("NonAsciiCharacters")
public class Sequence<A>
        implements Seq<A>,
                   Streamable<A>,
                   Iterable<A>,
                   Monadic<Sequence.µ, A>,
                   Copyable<Sequence<A>>,
                   Transmogrifyable<Sequence<A>>,
                   Higher1<Sequence.µ, A> {

    public static class µ implements WitnessType { protected µ() {} }

    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> narrow(final @NonNull Higher1<? extends Sequence.µ, ? extends A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        return (Sequence<A>) wide;
    }

    public static <A> Sequence<A> sequence() {
        return new Sequence<>();
    }

    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequenceFrom(final List<? extends A> collection) {
        Objects.requireNonNull(collection, nullValue("collection"));
        return new Sequence<>(object -> (A) object, collection.toArray());
    }

    @SuppressWarnings("unchecked")
    public static <A> Sequence<A> sequenceOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        requireNonNullElements(elements, index -> new NullPointerException(nullElementInAt("elements", index)));
        return new Sequence<>(object -> (A) object, elements);
    }

    private final Fun<Object, A> extractor;
    private final Object[] elements;

    @SuppressWarnings("unchecked")
    private Sequence() {
        this(object -> (A) object, new Object[0]);
    }

    private Sequence(final Fun<Object, A> extractor,
                     final Object[] elements) {
        this.extractor
            = extractor;
        this.elements
            = elements;
    }

    @Override
    public boolean isNotEmpty() {
        return (this.elements.length > 0);
    }

    @Override
    @UnwindingOperation
    public @NonNull A head()
            throws NoSuchElementException {
        return this.extractor.apply(this.elements[0]);
    }

    @Override
    public @NonNull Seq<A> tail()
            throws NoSuchElementException {
        final var tailElements
            = Arrays.copyOfRange(this.elements, 1, this.elements.length);
        return new Sequence<>(this.extractor, tailElements);
    }

    @Override
    public @NonNull Seq<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        final var elementsCopy
            = Arrays.copyOf(this.elements, this.elements.length + 1);
        elementsCopy[this.elements.length]
            = element;
        return new Sequence<>(this.extractor, elementsCopy);
    }

    @Override
    public @NonNull Seq<A> consAll(@NonNull Higher1<? extends Seq.µ, A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return null;    // TODO
    }

    @Override
    public @NonNull Tuple2<A, Sequence<A>> decons()
            throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Sequence<A> filter(final @NonNull Predicate<? super A> pred) {
        return null;    // TODO
    }

    @Override
    public @NonNull <C extends Collection<? super A>> C toCollection(final @NonNull Supplier<C> init) {
        return null;    // TODO
    }

    @Override
    public @NonNull List<A> toList() {
        return null;    // TODO
    }

    @Override
    public @NonNull Set<A> toSet() {
        return null;    // TODO
    }

    @Override
    public int getSize() {
        return this.elements.length;
    }

    @Override
    public @NonNull Stream<A> stream() {
        return Stream.empty();    // TODO
    }

    @Override
    public @NonNull Iterator<A> iterator() {
        return null;    // TODO
    }

    @Override
    public void forEach(final @NonNull Consumer<? super A> action) {
        Objects.requireNonNull(action, nullValue("action"));
        // TODO
    }

    @Override
    public @NonNull Spliterator<A> spliterator() {
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Sequence<B> map(@NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Sequence<B> applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Sequence<B> flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull Sequence<A> copy() {
        return null;    // TODO
    }

    @Override
    public @NonNull <T> T transmogrify(final @NonNull Function<? super Sequence<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return null;    // TODO
    }

}
