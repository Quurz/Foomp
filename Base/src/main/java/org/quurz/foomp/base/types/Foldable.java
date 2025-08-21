package org.quurz.foomp.base.types;

/**
 * <div>
 *     <p>
 *         A data type whose elements can be aggregated (reduced) using a folding function.
 *     </p>
 *     <p>
 *         This interface unifies {@link FoldableLeft} and {@link FoldableRight} and allows
 *         both left‑ and right‑associative folds. Implementations should document order,
 *         strictness (eager vs. lazy), and null‑safety guarantees.
 *     </p>
 * </div>
 *
 * @param <A> the element type being folded
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Foldable<A>
        extends FoldableLeft<A>,
                FoldableRight<A> {}
