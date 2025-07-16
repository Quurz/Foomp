package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Funktionales Interface, das einen Besucher im Visitor-Pattern definiert.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des Elements, das besucht wird.
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
     *         Besucht das angegebene Element.
     *     </p>
     * </div>
     *
     * @param visitable Das Element, das besucht wird.
     *
     * @since 1.0.0
     */
    void visit(final @NonNull A visitable);

}
