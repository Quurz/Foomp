package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Pred;
import org.quurz.foomp.base.types.UnwindingOperation;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Result.failure;

@SuppressWarnings("NonAsciiCharacters")
public class Task<A> {

    public static <A> Task<A> task(final A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Task<>(() -> value);
    }

    public static Task<Nothing> task() {
        return new Task<>(() -> nothing);
    }

    private Supplier<A> spool;

    private Task(final Supplier<A> spool) {
        this.spool
            = spool;
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
