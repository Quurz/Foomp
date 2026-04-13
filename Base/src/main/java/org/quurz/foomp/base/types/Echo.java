package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Functional interface for producing a formatted string representation of an object.
 *         This offers an explicit, type-level alternative to {@link Object#toString()} where
 *         formatting can be controlled independently of the default Java representation.
 *     </p>
 *     <p>
 *         Implementations must be null-safe and return a non-null string.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Echo {

    /**
     * <div>
     *     <p>
     *         Returns a formatted string representation of this object using the implementation's
     *         default formatting policy. This method must never return {@code null}.
     *     </p>
     * </div>
     *
     * @return a non-null formatted string representation of this object
     *
     * @since 1.0.0
     */
    @NonNull
    String echo();

}
