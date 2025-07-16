package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Ein {@code Container} ist eine Erweiterung des {@code Value}-Interfaces und repr&auml;sentiert eine Sammlung von 0 bis n Elementen.
 *         Implementierungen dieses Interfaces erm&ouml;glichen das &Uuml;berpr&uuml;fen des Vorhandenseins eines Elements und das Suchen von Elementen.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der Elemente, die im Container enthalten sind
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Container<A> {

    /**
     * <div>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob ein bestimmtes Element im Container enthalten ist.
     *     </p>
     * </div>
     *
     * @param element das Element, dessen Vorhandensein &uuml;berpr&uuml;ft werden soll
     *
     * @return {@code true}, wenn das Element im Container enthalten ist, andernfalls {@code false}
     *
     * @throws NullPointerException wenn {@code element} {@code null} ist
     */
    boolean contains(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Sucht ein bestimmtes Element im Container und gibt es zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param element das Element, das im Container gesucht wird
     *
     * @return das gefundene Element
     *
     * @throws NoSuchElementException wenn das Element nicht im Container vorhanden ist
     * @throws NullPointerException wenn {@code element} {@code null} ist
     */
    A search(final @NonNull A element)
            throws NoSuchElementException;

}
