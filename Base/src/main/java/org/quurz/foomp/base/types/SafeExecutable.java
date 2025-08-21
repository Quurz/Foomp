package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Functional interface for “safe” execution of a task: it never throws, but returns
 *         an {@link XorValue} that encodes either a failure ({@code Left}) or a success
 *         ({@code Right} carrying the result).
 *     </p>
 *     <p>
 *         This is the non-throwing counterpart to {@link Executable}. Use
 *         {@link Executable#safe()} to convert an {@code Executable} into a {@code SafeExecutable}.
 *     </p>
 *     <p>
 *         Contract:
 *     </p>
 *     <ul>
 *         <li>{@link #executeSafe()} must never throw.</li>
 *         <li>It must never return {@code null}.</li>
 *         <li>On success, the result is returned as {@code Right}; on failure, the exception is returned as {@code Left}.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the result type on success
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface SafeExecutable<A> {

    /**
     * <div>
     *     <p>
     *         Executes the task and returns an {@link XorValue} describing either the exception
     *         (failure) or the computed result (success). This method never throws.
     *     </p>
     * </div>
     *
     * @return a non-null {@code XorValue<Exception, A>} representing failure or success
     *
     * @since 1.0.0
     */
    @NonNull
    XorValue<Exception, A> executeSafe();

}
