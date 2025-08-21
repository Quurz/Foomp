package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Value carrier exposing exactly four components. Implementations provide presence
 *         information and accessors for each component.
 *     </p>
 *     <p>
 *         Nullability/absence semantics are implementation‑specific and must be documented
 *         by the concrete type. Accessors may throw {@link NoSuchElementException} if a
 *         component is not present according to the type’s contract.
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first component
 * @param <A2> the type of the second component
 * @param <A3> the type of the third component
 * @param <A4> the type of the fourth component
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Value4<A1, A2, A3, A4>
        extends Value3<A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Returns whether the first component is present.
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
     *         Returns whether the second component is present.
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
     *         Returns whether the third component is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if the third component is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean is3();

    /**
     * <div>
     *     <p>
     *         Returns whether the fourth component is present.
     *     </p>
     * </div>
     *
     * @return {@code true} if the fourth component is present; {@code false} otherwise
     *
     * @since 1.0.0
     */
    boolean is4();

    /**
     * <div>
     *     <p>
     *         Returns the first component.
     *     </p>
     * </div>
     *
     * @return the first component
     * @throws NoSuchElementException if the first component is not present
     *
     * @since 1.0.0
     */
    @NonNull A1 get1()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the second component.
     *     </p>
     * </div>
     *
     * @return the second component
     * @throws NoSuchElementException if the second component is not present
     *
     * @since 1.0.0
     */
    @NonNull A2 get2()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the third component.
     *     </p>
     * </div>
     *
     * @return the third component
     * @throws NoSuchElementException if the third component is not present
     *
     * @since 1.0.0
     */
    @NonNull A3 get3()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Returns the fourth component.
     *     </p>
     * </div>
     *
     * @return the fourth component
     * @throws NoSuchElementException if the fourth component is not present
     *
     * @since 1.0.0
     */
    @NonNull A4 get4()
        throws NoSuchElementException;

}
