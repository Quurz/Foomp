package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.BiFunction;

/**
 * <div>
 *     <p>
 *         Ein Interface, das die schrittweise Kombination von Werten ermöglicht.
 *         Implementierungen von <code>Combiner</code> definieren eine Methode zur Kombination eines Werts
 *         mit einem weiteren Wert und eine Methode, um die kombinierte Struktur zu finalisieren.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs
 * @param <A> Der Typ des Werts, der in diesem <code>Combiner</code> gespeichert ist
 *
 * @see Combinable
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Combiner<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Kombiniert den aktuellen Wert dieses <code>Combiner</code> mit einem weiteren Wert.
     *         Die Kombination erfolgt mithilfe einer Funktion, die beide Werte zu einem neuen Ergebniswert verknüpft.
     *     </p>
     * </div>
     *
     * @param other Ein weiterer Wert vom Typ <code>Higher1&lt;WT, B&gt;</code>, der kombiniert wird
     * @param combiner Eine Funktion, die den aktuellen und den weiteren Wert zu einem neuen Wert kombiniert
     * @param <B> Der Typ des weiteren Werts, der kombiniert wird
     * @param <R> Der Typ des Ergebnisses der Kombination
     * @return Ein neuer <code>Combiner</code>, der das Ergebnis der Kombination speichert
     *
     * @since 1.0.0
     */
    <B, R> Combiner<WT, R> with(final @NonNull Higher1<WT, B> other,
                                final @NonNull BiFunction<? extends A, ? extends B, ? super R> combiner);

    /**
     * <div>
     *     <p>
     *         Finalisiert die Kombination und liefert den letzten kombinierten Wert.
     *     </p>
     * </div>
     *
     * @return Das finale kombinierte Ergebnis als <code>Higher1&lt;WT, A&gt;</code>
     *
     * @since 1.0.0
     */
    Higher1<WT, A> finish();

}
