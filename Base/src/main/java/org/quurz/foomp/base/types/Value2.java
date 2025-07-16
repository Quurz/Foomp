package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Wrapper für zwei Werte
 *     </p>
 * </div>
 *
 * @param <A1> Typ des ersten enthaltenen Werts
 * @param <A2> Typ des zweiten enthaltenen Werts
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
     *         Haben wir was im ersten Wert?
     *     </p>
     * </div>
     *
     * @return <code>true</code> falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    boolean is1();

    /**
     * <div>
     *     <p>
     *         Haben wir was im zweiten Wert?
     *     </p>
     * </div>
     *
     * @return <code>true</code> falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    boolean is2();

    /**
     * <div>
     *     <p>
     *         Liefert den ersten Wert dieses <code>Value2</code>-Objekts
     *     </p>
     * </div>
     *
     * @return Der este Wert.
     *
     * @throws NoSuchElementException Falls kein Wert vorhanden sein sollte.
     *
     * @since 1.0.0
     */
    @NonNull A1 get1()
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Liefert den zweiten Wert dieses <code>Value2</code>-Objekts<br />.
     *     </p>
     * </div>
     *
     * @return Der zweite Wert.
     *
     * @throws NoSuchElementException Falls kein Wert vorhanden sein sollte.
     *
     * @since 1.0.0
     */
    @NonNull A2 get2()
            throws NoSuchElementException;

}
