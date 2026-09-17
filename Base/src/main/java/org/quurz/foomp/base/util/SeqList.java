package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Seq;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.types.Value2;
import org.quurz.foomp.higher.Higher1;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@SuppressWarnings("NonAsciiCharacters")
public class SeqList<A>
        implements Seq<A>,
                   Higher1<SeqList.µ, A> {

    public static final class µ extends Seq.µ { private µ() {} }

    public static <A> SeqList<A> narrow(final @NonNull Higher1<µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof SeqList<?>) {
            return (SeqList<A>) wide;
        }
        throw new IllegalArgumentException(cantCast("wider", SeqList.class));
    }

    @Override
    public boolean isNotEmpty() {
        return false;    // TODO
    }

    @Override
    public @NonNull A head() throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Value<A> headSafe() {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> tail() throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> cons(@NonNull A element) {
        return null;    // TODO
    }

    @Override
    public @NonNull Value2<A, ? extends Seq<A>> decons() throws NoSuchElementException {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> filter(@NonNull Predicate<? super A> pred) {
        return null;    // TODO
    }

    @Override
    public @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> partition(@NonNull Predicate<? super A> pred) {
        return null;    // TODO
    }

    @Override
    public @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> span(@NonNull Predicate<? super A> pred) {
        return null;    // TODO
    }

    @Override
    public @NonNull <C extends Collection<? super A>> C toCollection(@NonNull Supplier<C> init) {
        return null;    // TODO
    }

    @Override
    public @NonNull Seq<A> merge(@NonNull Seq<A> other) {
        return null;    // TODO
    }

}
