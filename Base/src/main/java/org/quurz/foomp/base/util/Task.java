package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.UnwindingOperation;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;

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
            .exceptionally(Result::failure);
    }

}
