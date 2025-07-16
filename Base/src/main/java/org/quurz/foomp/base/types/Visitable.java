package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Funktionales Interface, das einen besuchbaren Typ definiert.
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
public interface Visitable<A> {

    /**
     * <div>
     *     <p>
     *         Akzeptiert einen Besucher und wendet ihn auf das Element an.
     *     </p>
     * </div>
     *
     * @param visitor Der Besucher, der auf das Element angewendet wird.
     *
     * @since 1.0.0
     */
    <V extends Visitor<A>> V allow(final @NonNull V visitor);

}
