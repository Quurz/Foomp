package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Represents a computation that either successfully produces a value of type {@code A}
 *         or fails with an {@link Exception}.
 *     </p>
 *     <p>
 *         The {@code Triable} interface acts as a functional abstraction for lazy or repeatable
 *         computations whose outcome is uncertain. It can be used to model fault-tolerant or
 *         exception-prone processes – similar to a {@code Supplier}, but with explicit error
 *         handling via {@link XorValue}.
 *     </p>
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>This method must never throw; failures are returned as {@code Left(Exception)}.</li>
 *         <li>The returned {@link XorValue} must not be {@code null}.</li>
 *         <li>Implementations should document whether evaluation is lazy/eager and whether calls are idempotent.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the expected result type on success
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Triable<A> {

    /**
     * <div>
     *     <p>
     *         Executes the computation and returns either the exception (failure) or the computed
     *         result (success). This method must not throw.
     *     </p>
     * </div>
     *
     * @return a non-null {@link XorValue} containing either {@code Left(Exception)} on failure
     *         or {@code Right(A)} on success
     *
     * @since 1.0.0
     */
    @NonNull
    XorValue<Exception, A> tryIt();

}
