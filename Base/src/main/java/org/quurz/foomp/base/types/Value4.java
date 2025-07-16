package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Repräsentiert eine Struktur mit genau vier enthaltenen Werten.
 *         Jeder der vier Werte kann abgefragt werden, und es ist garantiert, dass
 *         immer genau vier Werte existieren.
 *     </p>
 *     <p>
 *         Falls ein Element <code>null</code> ist, wird beim Zugriff darauf
 *         eine <code>NoSuchElementException</code> geworfen.
 *     </p>
 * </div>
 *
 * @param <A1> Der Typ des ersten Elements
 * @param <A2> Der Typ des zweiten Elements
 * @param <A3> Der Typ des dritten Elements
 * @param <A4> Der Typ des vierten Elements
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
     *         Pr&uuml;ft, ob das vierte Element vorhanden ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls das vierte Element vorhanden ist, sonst <code>false</code>
     *
     * @since 1.0.0
     */
    boolean is4();

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

    /**
     * <div>
     *     <p>
     *         Gibt das vierte Element zur&uuml;ck, falls es vorhanden ist.
     *     </p>
     * </div>
     *
     * @return Das vierte Element
     * @throws NoSuchElementException Falls das vierte Element nicht vorhanden ist
     *
     * @since 1.0.0
     */
    @NonNull A4 get4()
        throws NoSuchElementException;

}
