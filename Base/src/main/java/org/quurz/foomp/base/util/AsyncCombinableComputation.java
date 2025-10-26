package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

public class AsyncCombinableComputation<A> implements
        Monadic<AsyncCombinableComputation.µ, A>,
        Higher1<AsyncCombinableComputation.µ, A> {

    public static final class µ implements WitnessType { private µ() {} }



    @Override
    public @NonNull <B> Higher1<? extends µ, B> map(@NonNull Function<? super A, ? extends B> transformation) {
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Higher1<? extends µ, B> lift(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Higher1<? extends µ, B> bind(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        return null;    // TODO
    }

}
