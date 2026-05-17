package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.XorValue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.*;

/**
 * <div>
 *     <p>
 *         Functional interface for executing an operation on an input value {@code X} to produce
 *         a result {@code Y}.
 *     </p>
 *     <p>
 *         Execution semantics:
 *     </p>
 *     <ul>
 *         <li>Operations may throw checked exceptions (see {@link #apply(Object)}).</li>
 *         <li>Side effects are allowed; {@code Applicable} does <em>not</em> require purity or
 *             referential transparency. Implementations should document their behaviour.</li>
 *     </ul>
 *     <p>
 *         Safe adaptor: {@link #safe()} performs the same computation but never throws; failures are
 *         returned as {@code Left(Exception)}, successes as {@code Right(Y)} using {@link XorValue}.
 *     </p>
 *     <p>
 *         Memoization: Use {@link MemoisingApplicable} only for pure/referentially transparent operations.
 *         Memoizing non‑pure operations can yield stale or misleading cached values and missing side effects.
 *     </p>
 * </div>
 *
 * @param <X> the input type
 * @param <Y> the result type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Applicable<X, Y> {

    /**
     * <div>
     *     <p>
     *         Creates an {@code Applicable} from a given {@link Function}. The resulting operation
     *         applies the function to its input and returns the non‑null result.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>{@code function} must not be {@code null} and must not return {@code null}.</li>
     *         <li>Side effects in {@code function} are allowed. If you need memoization, prefer
     *             {@link MemoisingApplicable#memoisingApplicable(Applicable)} with a pure function.</li>
     *     </ul>
     * </div>
     *
     * @param <X> the input type of the function
     * @param <Y> the result type of the function
     * @param function the function to wrap; must not be {@code null}
     * @return an {@code Applicable} that applies the given function
     * @throws NullPointerException if {@code function} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    static <X, Y> Applicable<X, Y> applicable(final @NonNull Function<X, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return x -> Objects.requireNonNull(function.apply(x), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Executes the operation and returns the result.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>{@code x} must not be {@code null}; the result must not be {@code null}.</li>
     *         <li>Implementations may perform side effects and may throw checked exceptions.</li>
     *     </ul>
     * </div>
     *
     * @param x the input value
     * @return the computed result
     * @throws Exception if an error occurs during execution
     *
     * @since 1.0.0
     */
    Y apply(@NonNull final X x)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Returns a function that performs the same computation as {@link #apply(Object)} but
     *         captures failures as {@link XorValue} instead of throwing. On success, {@code Right(Y)};
     *         on failure, {@code Left(Exception)}.
     *     </p>
     *     <p>
     *         Contract:
     *     </p>
     *     <ul>
     *         <li>The returned function must never return {@code null}.</li>
     *         <li>Side effects of the underlying operation still occur during evaluation.</li>
     *         <li>Exceptions are not thrown but returned as {@code Left(Exception)}.</li>
     *     </ul>
     * </div>
     *
     * @return a function yielding {@code XorValue<Exception, Y>} instead of throwing
     *
     * @since 1.0.0
     */
    default Fun<X, ? extends XorValue<Exception, Y>> safe() {
        return x -> {
            try {
                final var result
                    = this.apply(x);
                return new XorValue<>() {
                    @Override
                    public boolean isRight() {
                        return true;
                    }
                    @Override
                    public @NonNull Exception getLeft() throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }
                    @Override
                    public @NonNull Y getRight() {
                        return result;
                    }
                };
            } catch (final Exception exception) {
                return new XorValue<>() {
                    @Override
                    public boolean isRight() {
                        return false;
                    }
                    @Override
                    public @NonNull Exception getLeft() {
                        return exception;
                    }
                    @Override
                    public @NonNull Y getRight() throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }
                };
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns an identity operation that yields its input unchanged.
     *     </p>
     * </div>
     *
     * @param <X> the input type
     * @return an identity {@code Applicable}
     *
     * @since 1.0.0
     */
    static <X> Applicable<X, X> identity() {
        return x -> x;
    }

}
