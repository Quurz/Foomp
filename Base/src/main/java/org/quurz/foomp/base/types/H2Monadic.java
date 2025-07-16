package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert einen Typ, der die Eigenschaften einer Monade erf&uuml;llt.
 *     </p>
 *     <p>
 *         Dieses Interface kombiniert die Eigenschaften von `H2Mappable`, `H2Liftable` und `H2Bindable`.
 *         Monaden sind ein fundamentales Konzept in der funktionalen Programmierung, das es erm&ouml;glicht,
 *         Berechnungen in einem bestimmten Kontext zu strukturieren und zu kontrollieren.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ, der den Kontext des h&ouml;heren Typs repr&auml;sentiert.
 * @param <A> Der Typ der Elemente im aktuellen Kontext.
 * @param <R> Der Typ des Resultats.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface H2Monadic<WT extends WitnessType, A, R>
        extends H2Mappable<WT, A, R>,
                H2Liftable<WT, A, R>,
                H2Bindable<WT, A, R> {}
