package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Defines a left‑associative fold over a data structure.
 *     </p>
 *     <p>
 *         The fold starts from an initial accumulator and applies the given function
 *         from left to right across the elements.
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
public interface FoldableLeft<A> {

    /**
     * <div>
     *     <p>
     *         Folds the elements from left to right using an initial value and a binary function.
     *     </p>
     *     <p>
     *         The fold begins with {@code init} and iteratively applies {@code function}
     *         to the current accumulator and each element in encounter order.
     *     </p>
     * </div>
     *
     * @param init     the initial accumulator value; must not be {@code null}
     * @param function the folding function combining the previous accumulator and the current element; must not return {@code null}
     * @param <B>      the accumulator/result type
     * @return the accumulated result of the fold (never {@code null})
     *
     * @since 1.0.0
     */
    <B> @NonNull B foldLeft(final @NonNull B init,
                            final @NonNull BiFunction<? super B, ? super A, ? extends B> function);

}
