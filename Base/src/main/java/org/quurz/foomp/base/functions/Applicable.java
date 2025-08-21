package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable;
import org.quurz.foomp.base.types.XorValue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Functional interface for executing an operation on an input value {@code X} to produce
 *         a result {@code Y}. Extends {@link Deferrable} to support deferred execution.
 *     </p>
 *     <p>
 *         Provides both a direct, exception‑throwing execution model ({@link #apply(Object)})
 *         and a safe adaptor ({@link #safe()}) that captures failures as values using {@link XorValue}.
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
public interface Applicable<X, Y>
        extends Deferrable<X, Y> {

    /**
     * <div>
     *     <p>
     *         Creates an {@code Applicable} from a given {@link Function}. The resulting operation
     *         applies the function to its input and returns the non‑null result.
     *     </p>
     *     <p>
     *         Contract: {@code function} must not be {@code null} and must not return {@code null}.
     *     </p>
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
     *         Contract: the returned function must never return {@code null}.
     *     </p>
     * </div>
     *
     * @return a function yielding {@code XorValue<Exception, Y>} instead of throwing
     *
     * @since 1.0.0
     */
    default Fun<X, ? extends XorValue<Exception, Y>> safe() {
        return x -> {
            try {
                final var result = this.apply(x);
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
     *         Returns a {@link Callable} that defers execution of this operation until invoked,
     *         obtaining the input from the given {@link Supplier}.
     *     </p>
     *     <p>
     *         Contract: {@code supplier} must not be {@code null} and must not supply {@code null}.
     *         The returned callable must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier the input supplier; must not be {@code null}
     * @return a callable that, when called, executes this operation
     * @throws NullPointerException if {@code supplier} is {@code null}, supplies {@code null},
     *                              or if the result of {@link #apply(Object)} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return () -> {
            final var x = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(apply(x), nullResult());
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
