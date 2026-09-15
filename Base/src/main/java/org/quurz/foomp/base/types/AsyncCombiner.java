package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public interface AsyncCombiner<WT extends WitnessType, A>
        extends Combiner<WT, A> {

    @NonNull
    <B, R> Combiner<WT, R> with(final @NonNull Higher1<? extends WT, B> other,
                                final @NonNull BiFunction<? super A, ? super B, ? extends R> combiner,
                                final @NonNull Executor executor);

}
