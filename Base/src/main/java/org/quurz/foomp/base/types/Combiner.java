package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;

public interface Combiner<WT extends WitnessType, A> {

    @NonNull
    <B, R> Combiner<WT, R> with(final @NonNull Higher1<WT, B> other,
                                final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner);

    @NonNull
    Higher1<WT, A> finish();

    @NonNull
    Higher1<WT, A> finishAsync();

    @NonNull
    Higher1<WT, A> finishAsync(final @NonNull ExecutorService executorService);

}
