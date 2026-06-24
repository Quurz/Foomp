package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@SuppressWarnings("NonAsciiCharacters")
public class Promise<A>
        implements CompletionStage<A>,
                   Future<A>,
                   Monadic<Promise.µ, A>,
                   Higher1<Promise.µ, A> {

    public static class µ implements WitnessType { private µ() {} }

    public static <A> Promise<A> narrow(final @NonNull Higher1<Promise.µ, A> wide) {
        return (Promise<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    @Override
    public @NonNull <U> Promise<U> thenApply(final @NonNull Function<? super A, ? extends U> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return null;
    }

    @Override
    public @NonNull <U> CompletionStage<U> thenApplyAsync(final @NonNull Function<? super A, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenApplyAsync(Function<? super A, ? extends U> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenAccept(Consumer<? super A> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super A> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenAcceptAsync(Consumer<? super A> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenRun(Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> thenRunAsync(Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U, V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super A, ? super U, ? extends V> fn) {
        return null;
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super A, ? super U, ? extends V> fn) {
        return null;
    }

    @Override
    public <U, V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super A, ? super U, ? extends V> fn, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super A, ? super U> action) {
        return null;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super A, ? super U> action) {
        return null;
    }

    @Override
    public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super A, ? super U> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEither(CompletionStage<? extends A> other, Function<? super A, U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends A> other, Function<? super A, U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends A> other, Function<? super A, U> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEither(CompletionStage<? extends A> other, Consumer<? super A> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends A> other, Consumer<? super A> action) {
        return null;
    }

    @Override
    public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends A> other, Consumer<? super A> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return null;
    }

    @Override
    public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenCompose(Function<? super A, ? extends CompletionStage<U>> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super A, ? extends CompletionStage<U>> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> thenComposeAsync(Function<? super A, ? extends CompletionStage<U>> fn, Executor executor) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> handle(BiFunction<? super A, Throwable, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super A, Throwable, ? extends U> fn) {
        return null;
    }

    @Override
    public <U> CompletionStage<U> handleAsync(BiFunction<? super A, Throwable, ? extends U> fn, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<A> whenComplete(BiConsumer<? super A, ? super @NonNull Throwable> action) {
        return null;
    }

    @Override
    public CompletionStage<A> whenCompleteAsync(BiConsumer<? super A, ? super @NonNull Throwable> action) {
        return null;
    }

    @Override
    public CompletionStage<A> whenCompleteAsync(BiConsumer<? super A, ? super @NonNull Throwable> action, Executor executor) {
        return null;
    }

    @Override
    public CompletionStage<A> exceptionally(Function<Throwable, ? extends A> fn) {
        return null;
    }

    @Override
    public CompletableFuture<A> toCompletableFuture() {
        return null;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public A get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public A get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public A resultNow() {
        return Future.super.resultNow();
    }

    @Override
    public Throwable exceptionNow() {
        return Future.super.exceptionNow();
    }

    @Override
    public State state() {
        return Future.super.state();
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
