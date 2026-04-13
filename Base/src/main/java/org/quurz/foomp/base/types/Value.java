package org.quurz.foomp.base.types;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.quurz.foomp.base.types.UnwindingOperation;

/**
 * <div>
 *     <p>
 *         Minimal value‑carrier contract for a single value of type {@code A}. Implementations
 *         expose presence information and provide access to the value.
 *     </p>
 *     <p>
 *         Nullability/absence policy is implementation‑specific and must be documented clearly:
 *         some implementations may guarantee non‑null values, others may allow absence (similar to
 *         {@code Optional}) or even tolerate {@code null}. Callers should adhere to the concrete
 *         type’s contract.
 *     </p>
 * </div>
 *
 * @param <A> the contained value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Value<A>
        extends Supplier<A> {

    /**
     * <div>
     *     <p>
     *         Returns whether a value is present and can be retrieved without error.
     *     </p>
     * </div>
     *
     * @return {@code true} if a value is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean isPresent();

    /**
     * <div>
     *     <p>
     *         Returns the contained value if present. Depending on the concrete contract, this
     *         method may throw a {@link NoSuchElementException} when no value is present.
     *     </p>
     * </div>
     *
     * @return the contained value
     * @throws NoSuchElementException if no value is present according to the implementation’s contract
     *
     * @since 1.0.0
     */
    A get()
        throws NoSuchElementException;

}
