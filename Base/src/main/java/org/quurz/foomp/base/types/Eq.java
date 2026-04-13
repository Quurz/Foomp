package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         A functional interface for defining type-safe equality operations.
 *         This interface provides a dedicated, contract-based alternative to
 *         the standard {@link Object#equals(Object)} method, allowing for
 *         precise control over equality criteria.
 *     </p>
 *     <p>
 *         Implementations must ensure that {@link #eq(Eq)} follows the standard
 *         equivalence relation properties (reflexive, symmetric, transitive).
 *     </p>
 * </div>
 *
 * @param <SELF> The type implementing this interface.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Eq<SELF extends Eq<?>> {

    /**
     * <div>
     *     <p>
     *         Checks if the current object equals the given object.
     *     </p>
     * </div>
     *
     * @param other The object to check equality with.
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     *
     * @throws NullPointerException If <code>other</code> is <code>null</code>.
     *
     * @since 1.0.0
     */
    boolean eq(final @NonNull SELF other);

}
