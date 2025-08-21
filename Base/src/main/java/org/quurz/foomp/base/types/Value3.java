package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Value carrier exposing exactly three components. Implementations provide presence
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
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Value3<A1, A2, A3>
        extends Value2<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob das erste Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls das erste Element vorhanden ist, sonst <code>false</code>
     *
     * @since 1.0.0
     */
    boolean is1();

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob das zweite Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls das zweite Element vorhanden ist, sonst <code>false</code>
     *
     * @since 1.0.0
     */
    boolean is2();

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob das dritte Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls das dritte Element vorhanden ist, sonst <code>false</code>
     *
     * @since 1.0.0
     */
    boolean is3();

    /**
     * <div>
     *     <p>
     *         Gibt das erste Element zur&uuml;ck, falls es vorhanden ist.
     *     </p>
     * </div>
     *
     * @return Das erste Element
     * @throws NoSuchElementException Falls das erste Element nicht vorhanden ist
     *
     * @since 1.0.0
     */
    @NonNull A1 get1()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Gibt das zweite Element zur&uuml;ck, falls es vorhanden ist.
     *     </p>
     * </div>
     *
     * @return Das zweite Element
     * @throws NoSuchElementException Falls das zweite Element nicht vorhanden ist
     *
     * @since 1.0.0
     */
    @NonNull A2 get2()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Gibt das dritte Element zur&uuml;ck, falls es vorhanden ist.
     *     </p>
     * </div>
     *
     * @return Das dritte Element
     * @throws NoSuchElementException Falls das dritte Element nicht vorhanden ist
     *
     * @since 1.0.0
     */
    @NonNull A3 get3()
        throws NoSuchElementException;

}
