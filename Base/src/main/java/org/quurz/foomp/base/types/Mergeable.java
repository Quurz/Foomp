package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Represents an object that can be merged with another object of the same type.
 *     </p>
 *     <p>
 *         Merging typically results in a new instance that combines the state of both
 *         original objects, following specific rules defined by the implementation.
 *     </p>
 * </div>
 *
 * @param <SELF> the type of the object that can be merged
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Mergeable<SELF extends Mergeable<?>> {

    /**
     * <div>
     *     <p>
     *         Merges this object with the specified other object.
     *     </p>
     * </div>
     *
     * @param other the other object to merge with; must not be {@code null}
     * @return a new object representing the result of the merge
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull SELF merge(final @NonNull SELF other);

}
