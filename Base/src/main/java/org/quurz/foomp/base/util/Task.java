package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Result.failure;

@SuppressWarnings("NonAsciiCharacters")
public final class Task<A>
        implements Monadic<Task.µ, A>,
                   Higher1<Task.µ, A>{

    public static final class µ implements WitnessType { private µ() {} }

    @SuppressWarnings("unchecked")
    public static <A> Task<A> narrow(final @NonNull Higher1<? extends Task.µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Task<?> task) {
            return (Task<A>) task;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Task.class));
        }

    }

    public static <A> Task<A> task(final A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Task<>(() -> value);
    }

    public static Task<Nothing> task() {
        return new Task<>(() -> nothing);
    }

    private final Supplier<A> spool;

    private Task(final Supplier<A> spool) {
        this.spool
            = spool;
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the value produced by this task while preserving laziness.
     *     The resulting task will evaluate the original value and apply {@code transformation} when executed.
     *   </p>
     * </div>
     *
     * @param transformation mapping function; must not be {@code null} and must not return {@code null}
     * @param <B>            the new value type
     * @return a mapped {@code Task}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Task<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Task<>(() -> Objects.requireNonNull(transformation.apply(spool.get()), nullResultFrom("transformation")));
    }

    /**
     * <div>
     *   <p>
     *     Applicative applyTo: applies a function contained in another {@code Task} to this value.
     *   </p>
     * </div>
     *
     * @param transformation {@code Task} holding a function; must not be {@code null}
     * @param <B>            the new value type
     * @return a {@code Task} with the applied function
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Task<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final var narrowed
            = narrow(transformation);
        return new Task<>(() -> Objects.requireNonNull(narrowed.spool.get().apply(spool.get()), nullResultFrom("transformation")));
    }

    /**
     * <div>
     *   <p>
     *     Monadic flatMap: maps the value produced by this task to another {@code Task} and flattens the result.
     *   </p>
     * </div>
     *
     * @param transformation mapping to another {@code Task}; must not be {@code null}
     * @param <B>            the new value type
     * @return the bound {@code Task}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Task<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Task<>(() -> narrow(transformation.apply(this.spool.get())).spool.get());
    }

    <B> @NonNull Task<B> map(final @NonNull Function<? super A, ? extends B> transformation,
                             final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

    <B> @NonNull Task<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation,
                                 final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

    <B> @NonNull Task<B> flatMap(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation,
                                 final @NonNull Executor executor) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        Objects.requireNonNull(executor, nullValue("executor"));
        return null;    // TODO
    }

    @UnwindingOperation
    public CompletableFuture<Result<A>> runAsync(final @NonNull Executor executor) {
        Objects.requireNonNull(executor, nullValue("executor"));
        return CompletableFuture.supplyAsync(spool, executor)
            .thenApply(Result::success)
            .exceptionally(throwable -> {
                final var cause = (throwable.getCause() != null && throwable instanceof java.util.concurrent.CompletionException)
                        ? throwable.getCause()
                        : throwable;
                if (cause instanceof Error error) {
                    throw error;
                } else if (cause instanceof Exception exception) {
                    return failure(exception);
                } else {
                    return failure(new RuntimeException(cause));
                }
            });
    }

    @UnwindingOperation
    public CompletableFuture<Result<A>> runAsync() {
        return runAsync(ForkJoinPool.commonPool());
    }

}
