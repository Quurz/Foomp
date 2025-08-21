package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Type‑safe alternative to {@link Object#clone()} for producing value‑level copies.
 *     </p>
 *     <p>
 *         Implementations should document whether the copy is <em>deep</em> or <em>shallow</em>,
 *         and whether the type is effectively immutable (in which case returning {@code this}
 *         may be acceptable). Copy operations must be null‑safe and return a non‑null instance.
 *     </p>
 * </div>
 *
 * @param <SELF> the implementing copyable type (self type)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Copyable<SELF extends Copyable<?>> {

    /**
     * <div>
     *     <p>
     *         Creates a copy of this instance.
     *     </p>
     *     <p>
     *         The exact semantics (deep vs. shallow copy, shared resources, thread‑safety)
     *         are implementation specific and should be documented by the implementing type.
     *     </p>
     * </div>
     *
     * @return the copy (never {@code null})
     *
     * @since 1.0.0
     */
    @NonNull SELF copy();

}
