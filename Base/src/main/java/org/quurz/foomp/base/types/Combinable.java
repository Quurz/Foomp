package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Ein Interface für Typen, die kombiniert werden können.
 *         Implementierungen von <code>Combinable</code> bieten eine Methode zum Erzeugen eines
 *         {@code Combiner}, um schrittweise Werte zu kombinieren.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs
 * @param <A> Der Typ des Werts, der kombiniert werden kann
 *
 * @see Combiner
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Combinable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Startet eine neue Kombination und gibt einen <code>Combiner</code> zurück,
     *         um schrittweise weitere Werte hinzuzufügen.
     *     </p>
     * </div>
     *
     * @return Ein neuer {@code Combiner, der verwendet werden kann, um weiter Werte zu kombinieren
     *
     * @since 1.0.0
     */
    @NonNull Combiner<WT, A> combine();

}
