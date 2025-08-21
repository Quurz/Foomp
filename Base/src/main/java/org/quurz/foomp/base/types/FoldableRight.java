package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Defines a right‑associative fold over a data structure.
 *     </p>
 *     <p>
 *         The fold starts at the right end of the structure and applies the given function
 *         from right to left across the elements.
 *     </p>
 *     <p>
 *         Contract: the accumulator, elements, and the folding function must not be {@code null},
 *         and the folding function must not return {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the element type being folded
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface FoldableRight<A> {

    /**
     * <div>
     *     <p>
     *         Folds the elements from right to left using an initial value and a binary function.
     *     </p>
     *     <p>
     *         The fold begins with {@code init} and applies {@code function} iteratively,
     *         starting at the right end of the structure.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the folding function combining the current element and the previous accumulator; must not return {@code null}
     * @param <B>      the accumulator/result type
     * @return the accumulated result of the fold (never {@code null})
     *
     * @since 1.0.0
     */
    <B> @NonNull B foldRight(final @NonNull B init,
                             final @NonNull BiFunction<? super A, ? super B, ? extends B> function);

}
