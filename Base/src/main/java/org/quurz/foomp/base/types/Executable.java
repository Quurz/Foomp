package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;

/**
 * <div>
 *     <p>
 *         Functional interface for tasks that produce a result and may throw a checked exception.
 *         This mirrors {@link Callable} but exposes an explicit {@code execute()} method. The
 *         companion {@link #safe()} adapter returns a {@link SafeExecutable} that never throws
 *         and instead wraps failures in an {@link XorValue}.
 *     </p>
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>{@link #execute()} must not return {@code null}.</li>
 *         <li>{@link #call()} delegates to {@link #execute()}.</li>
 *         <li>{@link #safe()} returns a wrapper that never throws; failures are represented as Left,
 *             successes as Right.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the result type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Executable<A>
        extends Callable<A> {

    /**
     * <div>
     *     <p>
     *         Performs this task and returns its result.
     *     </p>
     * </div>
     *
     * @return the non-null result of the execution
     * @throws Exception if an error occurs during execution
     *
     * @since 1.0.0
     */
    @NonNull
    A execute()
        throws Exception;

    @Override
    default A call()
        throws Exception {
        return execute();
    }

    /**
     * <div>
     *     <p>
     *         Returns a {@link SafeExecutable} view of this task that never throws. Instead of
     *         propagating exceptions, errors are returned as {@code Left} and successful results
     *         as {@code Right} in an {@link XorValue}.
     *     </p>
     * </div>
     *
     * @return a {@code SafeExecutable} wrapper for this task
     *
     * @since 1.0.0
     */
    default @NonNull SafeExecutable<A> safe() {
        final var self
            = this;

        return () -> {
            try {
                final var result
                    = self.execute();

                return new XorValue<>() {

                    @Override
                    public boolean isRight() {
                        return true;
                    }

                    @Override
                    public @NonNull Exception getLeft()
                            throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }

                    @Override
                    public @NonNull A getRight()
                            throws NoSuchElementException {
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
                    public @NonNull Exception getLeft()
                            throws NoSuchElementException {
                        return exception;
                    }

                    @Override
                    public @NonNull A getRight()
                            throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }

                };
            }
        };
    }

}
