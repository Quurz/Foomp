package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

/**
 * <div>
 *   <p>
 *     A computation description that encapsulates an asynchronous evaluation yielding a {@link Result}.
 *   </p>
 *   <p>
 *     {@code Task} implements {@link Monadic} and {@link Higher1}, allowing functional and monadic transformations
 *     such as {@link #map(Function)}, {@link #applyTo(Higher1)}, and {@link #flatMap(Function)}.
 *     Computations are evaluated lazily and only executed when {@link #runAsync(Executor)} or {@link #runAsync()} is invoked.
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *   </p>
 * </div>
 *
 * @param <A> the type of the computed value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 * @author Junie
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Task<A>
        implements Monadic<Task.µ, A>,
                   Higher1<Task.µ, A>{

    /**
     * <div>
     *   <p>
     *     Witness type for {@code Task} used in the higher‑kinded encoding.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher1} value to a concrete {@code Task}.
     *   </p>
     * </div>
     *
     * @param wide the higher‑kinded value; must not be {@code null}
     * @param <A>  the computed value type
     * @return a {@code Task} instance
     * @throws NullPointerException     if {@code wide} is {@code null}
     * @throws IllegalArgumentException if {@code wide} is not an instance of {@code Task}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Task<A> narrow(final @NonNull Higher1<? extends Task.µ, A> wide) {
        Objects.requireNonNull(wide, nullValue("wide"));
        if (wide instanceof Task<?> task) {
            return (Task<A>) task;
        } else {
            throw new IllegalArgumentException(cantCast("wide", Task.class));
        }

    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Task} that yields the specified value wrapped in a successful {@link Result}.
     *   </p>
     * </div>
     *
     * @param value the value to be wrapped; must not be {@code null}
     * @param <A>   the type of the value
     * @return a {@code Task} producing the given value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> Task<A> task(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Task<>(_ -> CompletableFuture.completedFuture(success(value)));
    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Task} that yields a {@link Nothing} value wrapped in a successful {@link Result}.
     *   </p>
     * </div>
     *
     * @return a {@code Task} producing {@link Nothing#nothing}
     *
     * @since 1.0.0
     */
    public static Task<Nothing> task() {
        return new Task<>(_ -> CompletableFuture.completedFuture(success(nothing)));
    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Task} that evaluates the specified {@link Supplier} asynchronously when run,
     *     yielding the computed value wrapped in a {@link Result}.
     *   </p>
     *   <p>
     *     Any exception thrown during evaluation is captured and returned as a failed {@link Result}.
     *   </p>
     * </div>
     *
     * @param supplier the supplier producing the value; must not be {@code null} and must not return {@code null}
     * @param <A>      the type of the computed value
     * @return a {@code Task} producing the computed value
     * @throws NullPointerException if {@code supplier} is {@code null}
     *
     * @since 1.0.0
     *
     * @author Alexander Schell & Junie
     */
    public static <A> Task<A> taskFrom(final @NonNull Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));

        return new Task<>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            return CompletableFuture.supplyAsync(() -> {
                try {
                    final var value = Objects.requireNonNull(supplier.get(), nullResultFrom("supplier"));
                    return success(value);
                } catch (final Exception exception) {
                    return failure(exception);
                }
            }, executor);
        });
    }

    /**
     * <div>
     *     <p>
     *         Represents an asynchronous computation that can be executed on a specified {@link Executor}.
     *     </p>
     *     <p>
     *         As a functional interface, {@code Executable} can be used as the assignment target for lambda
     *         expressions or method references that perform asynchronous operations yielding a {@link CompletableFuture}
     *         of a {@link Result}.
     *     </p>
     * </div>
     *
     * @param <A> the result value type produced by this execution
     *
     * @see Task
     * @see Result
     * @see Fun
     *
     * @since 1.0.0
     *
     * @author Alexander Schell
     */
    @FunctionalInterface
    public interface Executable<A>
            extends Fun<Executor, CompletableFuture<Result<A>>> {

        /**
         * <div>
         *     <p>
         *         Applies this executable function to the given {@link Executor} by delegating to {@link #execute(Executor)}.
         *     </p>
         * </div>
         *
         * @param executor the executor on which the computation is scheduled; must not be {@code null}
         * @return a {@link CompletableFuture} completing with the {@link Result} of the computation
         * @throws NullPointerException if {@code executor} is {@code null}
         *
         * @since 1.0.0
         */
        @Override
        @NonNull
        default CompletableFuture<Result<A>> apply(final @NonNull Executor executor) {
            Objects.requireNonNull(executor, nullValue("executor"));
            return this.execute(executor);
        }

        /**
         * <div>
         *     <p>
         *         Executes the asynchronous computation using the given {@link Executor}.
         *     </p>
         * </div>
         *
         * @param executor the executor on which the computation is scheduled; must not be {@code null}
         * @return a {@link CompletableFuture} completing with the {@link Result} of the computation
         * @throws NullPointerException if {@code executor} is {@code null}
         *
         * @since 1.0.0
         */
        @NonNull
        CompletableFuture<Result<A>> execute(final @NonNull Executor executor);

    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Task} that executes the specified {@link Runnable} asynchronously when run,
     *     yielding {@link Nothing#nothing} wrapped in a {@link Result}.
     *   </p>
     *   <p>
     *     Any exception thrown during execution is captured and returned as a failed {@link Result}.
     *   </p>
     * </div>
     *
     * @param runnable the action to execute; must not be {@code null}
     * @return a {@code Task} producing {@link Nothing#nothing}
     * @throws NullPointerException if {@code runnable} is {@code null}
     *
     * @since 1.0.0
     *
     * @author Alexander Schell & Junie
     */
    public static @NonNull Task<Nothing> taskFrom(final @NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, nullValue("runnable"));

        return new Task<>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            return CompletableFuture.runAsync(runnable, executor)
                .handle((_, throwable) -> {
                    if (throwable != null) {
                        final var cause = (throwable.getCause() != null && throwable instanceof java.util.concurrent.CompletionException)
                                ? throwable.getCause()
                                : throwable;
                        if (cause instanceof Exception exception) {
                            return Result.failure(exception);
                        } else if (cause instanceof Error error) {
                            throw error;
                        } else {
                            return Result.failure(new RuntimeException(cause));
                        }
                    }
                    return success(nothing);
                });
        });
    }

    /**
     * <div>
     *   <p>
     *     Creates a new {@code Task} from the given {@link Executable}.
     *   </p>
     * </div>
     *
     * @param executable the executable computation; must not be {@code null}
     * @param <A>        the computed value type
     * @return a {@code Task} wrapping the given executable
     * @throws NullPointerException if {@code executable} is {@code null}
     *
     * @since 1.0.0
     *
     * @author Alexander Schell & Junie
     */
    public static <A> Task<A> taskFrom(final @NonNull Executable<A> executable) {
        Objects.requireNonNull(executable, nullValue("executable"));

        return new Task<>(executable);
    }

    /**
     * The underlying asynchronous execution logic of this task.
     */
    private final Executable<A> executable;

    /**
     * Constructs a {@code Task} wrapping the specified {@link Executable}.
     *
     * @param executable the executable computation
     */
    private Task(final Executable<A> executable) {
        this.executable
            = executable;
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
        return new Task<>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            return this.executable.execute(executor).thenApplyAsync(result -> switch (result) {
                case Result.Success<A> success -> {
                    try {
                        final var mapped = Objects.requireNonNull(transformation.apply(success.getValue()), nullResultFrom("transformation"));
                        yield success(mapped);
                    } catch (final Exception exception) {
                        yield Result.failure(exception);
                    }
                }
                case Result.Failure<A> failure -> Result.failure(failure.getException());
            }, executor);
        });
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
        return new Task<>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            final var valueFuture = this.executable.execute(executor);
            final var functionFuture = narrowed.executable.execute(executor);

            return valueFuture.thenCombineAsync(functionFuture, (resVal, resFn) -> switch (resVal) {
                case Result.Failure<A> failVal -> Result.failure(failVal.getException());
                case Result.Success<A> succVal -> switch (resFn) {
                    case Result.Failure<? extends Function<? super A, ? extends B>> failFn -> Result.failure(failFn.getException());
                    case Result.Success<? extends Function<? super A, ? extends B>> succFn -> {
                        try {
                            final var fn = succFn.getValue();
                            final var mapped = Objects.requireNonNull(fn.apply(succVal.getValue()), nullResultFrom("transformation"));
                            yield success(mapped);
                        } catch (final Exception exception) {
                            yield Result.failure(exception);
                        }
                    }
                };
            }, executor);
        });
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
        return new Task<B>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            return this.executable.execute(executor).thenComposeAsync(result -> switch (result) {
                case Result.Success<A> success -> {
                    try {
                        final var nextHigher = Objects.requireNonNull(transformation.apply(success.getValue()), nullResultFrom("transformation"));
                        final var nextTask = narrow(nextHigher);
                        yield nextTask.executable.execute(executor);
                    } catch (final Exception exception) {
                        yield CompletableFuture.completedFuture(Result.<B>failure(exception));
                    }
                }
                case Result.Failure<A> failure -> CompletableFuture.completedFuture(Result.<B>failure(failure.getException()));
            }, executor);
        });
    }

    /**
     * <div>
     *     <p>
     *         Combines this {@code Task} with another {@code Task} using the given {@code combiner} function.
     *     </p>
     *     <p>
     *         Both tasks are executed asynchronously on the supplied {@link Executor} when run, and their
     *         results are combined once both computations have completed.
     *     </p>
     * </div>
     *
     * @param other    the other {@code Task}; must not be {@code null}
     * @param combiner the combining function; must not be {@code null} and must not return {@code null}
     * @param <B>      the other value type
     * @param <C>      the result type
     * @return a new {@code Task} producing the combined result
     * @throws NullPointerException if {@code other} or {@code combiner} is {@code null}
     *
     * @since 1.0.0
     */
    public <B, C> Task<C> zip(final @NonNull Task<B> other,
                              final @NonNull BiFunction<? super A, ? super B, ? extends C> combiner) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(combiner, nullValue("combiner"));

        return new Task<>((final @NonNull Executor executor) -> {
            Objects.requireNonNull(executor, nullValue("executor"));
            final var futureA = this.executable.execute(executor);
            final var futureB = other.executable.execute(executor);

            return futureA.thenCombineAsync(futureB, (resVal, resOther) -> switch (resVal) {
                case Result.Failure<A> failVal -> Result.failure(failVal.getException());
                case Result.Success<A> succVal -> switch (resOther) {
                    case Result.Failure<B> failOther -> Result.failure(failOther.getException());
                    case Result.Success<B> succOther -> {
                        try {
                            final var combined = Objects.requireNonNull(combiner.apply(succVal.getValue(), succOther.getValue()), nullResultFrom("combiner"));
                            yield success(combined);
                        } catch (final Exception exception) {
                            yield Result.failure(exception);
                        }
                    }
                };
            }, executor);
        });
    }

    /**
     * <div>
     *   <p>
     *     Combines this {@code Task} with another {@code Task} using the given {@code combiner} function,
     *     executing {@code other} on the specified {@link Executor}.
     *   </p>
     * </div>
     *
     * @param other    the other {@code Task}; must not be {@code null}
     * @param combiner the combining function; must not be {@code null} and must not return {@code null}
     * @param executor the executor to run {@code other} on; must not be {@code null}
     * @param <B>      the other value type
     * @param <C>      the result type
     * @return a new {@code Task} producing the combined result
     * @throws NullPointerException if {@code other}, {@code combiner}, or {@code executor} is {@code null}
     *
     * @since 1.0.0
     *
     * @author Alexander Schell & Junie
     */
    public <B, C> Task<C> zip(final @NonNull Task<B> other,
                              final @NonNull BiFunction<? super A, ? super B, ? extends C> combiner,
                              final @NonNull Executor executor) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(combiner, nullValue("combiner"));
        Objects.requireNonNull(executor, nullValue("executor"));

        return this.zip(other.executeOn(executor), combiner);
    }

    /**
     * <div>
     *   <p>
     *     Configures this {@code Task} to be executed on the specified {@link Executor},
     *     ignoring any executor supplied to {@link #runAsync(Executor)}.
     *   </p>
     * </div>
     *
     * @param executor the executor to run this task on; must not be {@code null}
     * @return a new {@code Task} bound to the given executor
     * @throws NullPointerException if {@code executor} is {@code null}
     *
     * @since 1.0.0
     *
     * @author Alexander Schell & Junie
     */
    public Task<A> executeOn(final @NonNull Executor executor) {
        Objects.requireNonNull(executor, nullValue("executor"));

        return new Task<>(_ -> this.runAsync(executor));
    }

    /**
     * <div>
     *   <p>
     *     Asynchronously executes this task using the specified executor.
     *   </p>
     *   <p>
     *     Any exception thrown during the execution is captured and wrapped into a failed {@link Result}.
     *     Errors (instances of {@link Error}) are rethrown.
     *   </p>
     * </div>
     *
     * @param executor the executor to run this task on; must not be {@code null}
     * @return a {@link CompletableFuture} completing with a {@link Result} holding the computed value or the occurred exception
     * @throws NullPointerException if {@code executor} is {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public CompletableFuture<Result<A>> runAsync(final @NonNull Executor executor) {
        Objects.requireNonNull(executor, nullValue("executor"));
        try {
            return this.executable.execute(executor)
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
        } catch (final Exception exception) {
            return CompletableFuture.completedFuture(failure(exception));
        }
    }

    /**
     * <div>
     *   <p>
     *     Asynchronously executes this task using the default common pool ({@link ForkJoinPool#commonPool()}).
     *   </p>
     *   <p>
     *     Any exception thrown during the execution is captured and wrapped into a failed {@link Result}.
     *     Errors (instances of {@link Error}) are rethrown.
     *   </p>
     * </div>
     *
     * @return a {@link CompletableFuture} completing with a {@link Result} holding the computed value or the occurred exception
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public CompletableFuture<Result<A>> runAsync() {
        return runAsync(ForkJoinPool.commonPool());
    }

}
