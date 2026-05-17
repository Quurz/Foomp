package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Functional interface for types that can be visited by a {@link Visitor}.
 *         This enables externalising operations over the visited element while keeping
 *         the element’s structure unchanged.
 *     </p>
 *     <p>
 *         Typical usage follows the Visitor pattern: a visitable accepts a visitor and
 *         invokes the visitor’s operation on itself.
 *     </p>
 * </div>
 *
 * @param <A> the type of the element being visited
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
     *         Accepts the given visitor and applies it to this element.
     *     </p>
     *     <p>
     *         Contract: {@code visitor} must not be {@code null}. The returned visitor is the
     *         same instance passed in, allowing fluent chaining when desired.
     *     </p>
     * </div>
     *
     * @param visitor the guest to be received; must not be {@code null}
     * @param <V>     the concrete visitor type
     * @return the same visitor instance, for fluent chaining
     *
     * @since 1.0.0
     */
     @NonNull <V extends Visitor<? super A>> V welcome(final @NonNull V visitor);

}
