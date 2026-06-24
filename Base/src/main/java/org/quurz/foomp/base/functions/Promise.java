package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@SuppressWarnings("NonAsciiCharacters")
public class Promise<A>
        extends CompletableFuture<A>
        implements Monadic<Promise.µ, A>,
                   Higher1<Promise.µ, A> {

    public static class µ implements WitnessType { private µ() {} }

    public static <A> Promise<A> narrow(final @NonNull Higher1<Promise.µ, A> wide) {
        return (Promise<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    @Override
    public @NonNull <B> Promise<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    /// TODO
    }

}
