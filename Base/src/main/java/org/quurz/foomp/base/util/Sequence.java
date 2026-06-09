package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Provider;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

@SuppressWarnings("NonAsciiCharacters")
public sealed interface Sequence<A>
    extends Seq<A>,
            Higher1<Sequence.µ, A>
    permits Sequence.Element,
            Sequence.Empty {

    final class µ extends Seq.µ implements WitnessType { private µ() {} }

    @SuppressWarnings("unchecked")
    static <A> Sequence<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        return (Sequence<A>) wide;
    }

    @SuppressWarnings("unchecked")
    static <A> Sequence<A> sequence() {
        return (Sequence<A>) Empty.INSTANCE;
    }

    @SafeVarargs
    static <A> Sequence<A> sequenceOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        Sequence<A> sequence
            = sequence();
        for (int i = (elements.length - 1); i >= 0; --i) {
             final var element
                 = elements[i];
            if (element != null) {
                sequence
                    = sequence.cons(element);
            } else {
                throw new IllegalArgumentException(nullElementInAt("elements", i));
            }
        }
        return sequence;
    }

    @SuppressWarnings("unchecked")
    static <A> Sequence<A> sequenceFrom(final Collection<? extends A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return sequenceOf((A[]) elements.toArray());
    }

    @Override
    default boolean isNotEmpty() {
        return (this instanceof Sequence.Element<A>);
    }

    @Override
    @UnwindingOperation
    default @NonNull A head() throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> element.spool.get();
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    @Override
    default @NonNull Maybe<A> headSafe() {
        return switch (this) {
            case Element<A> element -> Maybe.Some.fromSpool(element.spool);
            case Empty<A> _ -> none();
        };
    }

    @Override
    default @NonNull Sequence<A> tail()
            throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> element.tail;
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());    // TODO: Bessere Meldung
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NonNull Sequence<A> cons(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return switch (this) {
            case Element<A> _ -> new Element<>(() -> element, this);
            case Empty<A> _ -> new Element<>(() -> element, (Sequence<A>) Empty.INSTANCE);
        };
    }

    @Override
    default @NonNull Tuple2<A, Sequence<A>> decons()
            throws NoSuchElementException {
        return switch (this) {
            case Element<A> element -> tuple2(element.head(), element.tail);
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    default @NonNull Sequence<A> reverse() {
        return reverseRecursive(this, sequence()).get();
    }

    private static <A> Trampoline<Sequence<A>> reverseRecursive(final Sequence<A> current,
                                                                final Sequence<A> accu) {
        return switch (current) {
            case Element<A> e -> Trampoline.more(() -> reverseRecursive(e.tail(), accu.cons(e.head())));
            case Empty<A> _ -> Trampoline.done(accu);
        };
    }

    @Override
    @UnwindingOperation
    default @NonNull Sequence<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return filterRecursive(predicate, this, sequence()).get().reverse();
    }

    private static <A> Trampoline<Sequence<A>> filterRecursive(final Predicate<? super A> predicate,
                                                               final Sequence<A> current,
                                                               final Sequence<A> accu) {
        return switch (current) {
            case Element<A> element
                -> {
                    final var value
                        = element.spool.get();
                    yield predicate.test(value)
                        ? Trampoline.more(() -> filterRecursive(predicate, element.tail, accu.cons(value)))
                        : Trampoline.more(() -> filterRecursive(predicate, element.tail, accu));
                }
            case Empty<A> _
                -> Trampoline.done(accu);
        };
    }

    @Override
    @UnwindingOperation
    default <C extends Collection<? super A>> @NonNull C toCollection(final @NonNull Supplier<C> init) {
        return null;    // TODO
    }

    default int getSize() {
        return 0;    // TODO
    }

    final class Element<A>
            implements Sequence<A> {

        private final Provider<A> spool;
        private final Sequence<A> tail;

        private Element(final Provider<A> spool,
                        final Sequence<A> tail) {
            this.spool
                = spool;
            this.tail
                = tail;
        }

    }

    final class Empty<A>
            implements Sequence<A> {

        private static final Sequence<?> INSTANCE
            = new Empty<>();

        private Empty() {}

    }

}
