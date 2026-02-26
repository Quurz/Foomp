package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.*;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

@SuppressWarnings("NonAsciiCharacters")
public class CombinableTask<A>
        implements Executable<A>,
                   Combinable<CombinableTask.µ, A>,
                   Monadic<CombinableTask.µ, A>,
                   Higher1<CombinableTask.µ, A> {

    @SuppressWarnings("NonAsciiCharacters")
    public static final class µ implements WitnessType { private µ() {} }

    @SuppressWarnings("unchecked")
    public static <A> CombinableTask<A> narrow(final @NonNull Higher1<? extends µ, A> wide) {
        return (CombinableTask<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    public static <A> CombinableTask<A> combinableTask(final @NonNull A data) {
        Objects.requireNonNull(data, nullValue("data"));
        return new CombinableTask<>(() -> data);
    }

    private static final class MyCombinerImpl<A>
            implements Combiner<µ, A> {

        private final CombinableTask<A> combinableTask;
        private final List<Tuple2<CombinableTask<Object>, BiFunction<Object, Object, Object>>> tasksAndCombiners;

        @SuppressWarnings({"unchecked", "unused"})
        private MyCombinerImpl(final CombinableTask<A> combinableTask) {
            this.combinableTask
                = combinableTask;
            this.tasksAndCombiners
                = new ArrayList<>();
            this.tasksAndCombiners.add(tuple2((CombinableTask<Object>) combinableTask, (a, _$) -> a));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <B, R> @NonNull Combiner<µ, R> with(final @NonNull Higher1<µ, B> other,
                                                   final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner) {
            Objects.requireNonNull(other, nullValue("other"));
            Objects.requireNonNull(combiner, nullValue("combiner"));

            this.tasksAndCombiners.add(
                tuple2((CombinableTask<Object>) narrow(other),
                (BiFunction<Object, Object, Object>) combiner)
            );
            return (Combiner<µ, R>) this;
        }

        @Override
        public @NonNull CombinableTask<A> finish() {
            return new CombinableTask<>(() -> {
//                this.tasksAndCombiners.stream()
//                    .map(tuple -> tuple.map(task -> task.spool.get()))
//                    .
                return null;    // TODO
            });
        }

        @Override
        public @NonNull CombinableTask<A> finishAsync() {
            return null;    // TODO
        }

        @Override
        public @NonNull CombinableTask<A> finishAsync(final @NonNull ExecutorService executorService) {
            return null;    // TODO
        }

    }

    private final Supplier<A> spool;

    private CombinableTask(final Supplier<A> spool) {
        this.spool
            = spool;
    }

    @Override
    public @NonNull Combiner<µ, A> combine() {
        return new MyCombinerImpl<>(this);
    }

    @Override
    public @NonNull <B> CombinableTask<B> map(@NonNull Function<? super A, ? extends B> transformation) {
        return null;    // TODO
    }

    @Override
    public @NonNull <B> CombinableTask<B> applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        return null;    // TODO
    }

    @Override
    public @NonNull <B> CombinableTask<B> bind(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        return null;    // TODO
    }

    @UnwindingOperation
    @Override
    public @NonNull A execute()
            throws Exception {
        return this.spool.get();
    }
}
