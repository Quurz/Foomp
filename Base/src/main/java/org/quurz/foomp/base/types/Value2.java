package org.quurz.foomp.base.types;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Pair‑like value carrier exposing two components, commonly referred to as “first” and “second”.
 *         Implementations provide presence information and accessors for both components.
 *     </p>
 *     <p>
 *         Nullability policy is implementation‑specific and must be documented clearly:
 *         some implementations may guarantee non‑null components, others may allow absence or tolerate
 *         {@code null}. Callers should consult the concrete type’s contract.
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first component
 * @param <A2> the type of the second component
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Value2<A1, A2>
        extends Value<A1> {

    /**
     * <div>
     *     <p>
     *         Returns whether the first component is present (can be retrieved without error).
     *     </p>
     * </div>
     *
     * @return {@code true} if the first component is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean is1();

    /**
     * <div>
     *     <p>
     *         Returns whether the second component is present (can be retrieved without error).
     *     </p>
     * </div>
     *
     * @return {@code true} if the second component is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean is2();

    /**
     * <div>
     *     <p>
     *         Returns the first component of this {@code Value2}.
     *     </p>
     * </div>
     *
     * @return the first component
     * @throws NoSuchElementException if the first component is not present according to this type’s contract
     *
     * @since 1.0.0
     */
    A1 get1()
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the second component of this {@code Value2}.
     *     </p>
     * </div>
     *
     * @return the second component
     * @throws NoSuchElementException if the second component is not present according to this type’s contract
     *
     * @since 1.0.0
     */
    A2 get2()
            throws NoSuchElementException;

}
