package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

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
