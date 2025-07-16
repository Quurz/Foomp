package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

/**
 * <div>
 *     <p>
 *         Ein Interface f&uuml;r Objekte, die als {@link Stream} dargestellt werden k&ouml;nnen.
 *         Dies erm&ouml;glicht eine funktionale Verarbeitung der enthaltenen Elemente.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ der Elemente im Stream
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Streamable<A> {

    /**
     * <div>
     *     <p>
     *         Gibt einen {@link Stream} zur&uuml;ck, der die Elemente dieser Struktur repr&auml;sentiert.
     *         Der Stream sollte nach M&ouml;glichkeit lazy generiert werden.
     *     </p>
     * </div>
     *
     * @return Ein {@link Stream}, der die Elemente dieser Struktur liefert
     *
     * @since 1.0.0
     */
    @NonNull Stream<A> stream();

}
