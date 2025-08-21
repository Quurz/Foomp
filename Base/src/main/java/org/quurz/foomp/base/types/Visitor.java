package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Functional interface defining a visitor in the classic Visitor pattern.
 *         A visitor encapsulates an operation to be performed on a value of type {@code A}.
 *     </p>
 *     <p>
 *         Implementations may perform side effects. Unless stated otherwise, visitors are not
 *         required to be idempotent or thread‑safe; concrete implementations should document
 *         their semantics.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements accepted by this visitor
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Visitor<A> {

    /**
     * <div>
     *     <p>
     *         Visits the given element and performs the visitor’s operation on it.
     *     </p>
     *     <p>
     *         Contract: {@code visitable} must not be {@code null}. Implementations may
     *         perform side effects and should document any constraints (e.g., ordering or reentrancy).
     *     </p>
     * </div>
     *
     * @param visitable the element to visit; must not be {@code null}
     *
     * @since 1.0.0
     */
    void visit(final @NonNull A visitable);

}
