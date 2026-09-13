package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

@SuppressWarnings("NonAsciiCharacters")
public class Promise<A>
        implements Value<Result<A>>,
                   Monadic<Promise.µ, A>,
                   Higher1<Promise.µ, A> {

    public static final class µ implements WitnessType { private µ() {} }

    public static <A> Promise<A> narrow(final @NonNull Higher1<Promise.µ, A> wide) {
        return (Promise<A>) Objects.requireNonNull(wide, nullValue("wider"));
    }

    public static <A> Promise<A> promise(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return promise(value, ForkJoinPool.commonPool());
    }

    public static <A> Promise<A> promise(final @NonNull A value,
                                         final @NonNull Executor executor) {
        Objects.requireNonNull(value, nullValue("value"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return new Promise<>(value, executor);
    }

    private sealed interface State<A>
            permits State.Ready,
                    State.Computing,
                    State.Exceptional {

        final class Ready<A>
                implements State<A> {

            final A result;

            private Ready(final A result) {
                this.result
                    = result;
            }

        }

        final class Computing<A>
                implements State<A> {}

        final class Exceptional<A>
                implements State<A> {

            private final Exception exception;

            private Exceptional(final Exception exception) {
                this.exception
                        = exception;
            }

        }

    }

    private final A value;
    private final Executor executor;

    private volatile State<A> state;

    private Promise(final A value,
                    final Executor executor) {
        this.value
            = value;
        this.executor
            = executor;
    }

    @Override
    public boolean isPresent() {
        return (this.state instanceof State.Ready<?>);
    }

    @Override
    public Result<A> get()
        throws NoSuchElementException {
        return switch (this.state) {
            case Promise.State.Ready<A> ready -> success(ready.result);
            case State.Computing<?> _ -> throw new NoSuchElementException(noValuePresent());
            case State.Exceptional<?> exceptional -> failure(exceptional.exception);
        };
    }

    @Override
    public @NonNull <B> Promise<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    public @NonNull <B> Promise<B> map(final @NonNull Function<? super A, ? extends B> transformation,
                                       final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    public @NonNull <B> Promise<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation,
                                           final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

    @Override
    public @NonNull <B> Promise<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;    // TODO
    }

    public @NonNull <B> Promise<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation,
                                           final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

}
