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

    @Override
    public @NonNull <B> Task<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Task<>(() -> Objects.requireNonNull(transformation.apply(spool.get()), nullResultFrom("transformation")));
    }

    @Override
    public @NonNull <B> Task<B> applyTo(@NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;
    }

    @Override
    public @NonNull <B> Task<B> flatMap(@NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return null;
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
