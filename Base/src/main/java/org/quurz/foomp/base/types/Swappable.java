package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Functional interface for structures whose two inner components can be swapped.
 *         Implementations return a value of the same constructor shape with the type
 *         parameters exchanged.
 *     </p>
 *     <p>
 *         Typical use cases include pair/tuple-like structures where {@code (A, B)} can be
 *         transformed into {@code (B, A)} while preserving the overall shape.
 *     </p>
 * </div>
 *
 * @param <SELF> the implementing swappable type (self type) after swapping, i.e. {@code Swappable<?, B, A>}
 * @param <A>    the first inner type before swapping
 * @param <B>    the second inner type before swapping
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Swappable<SELF extends Swappable<?, B, A>, A, B> {

    /**
     * <div>
     *     <p>
     *         Returns this structure with its two inner components swapped:
     *         {@code Swappable<A, B>} → {@code Swappable<B, A>}.
     *     </p>
     * </div>
     *
     * @return a non-null instance with {@code A} and {@code B} exchanged
     *
     * @since 1.0.0
     */
    @NonNull SELF swap();

}
