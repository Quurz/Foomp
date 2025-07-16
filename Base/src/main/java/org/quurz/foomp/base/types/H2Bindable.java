package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert einen Typ, der mit anderen Werten desselben Typs verkn&uuml;pft werden kann.
 *     </p>
 *     <p>
 *         Dieses Interface erm&ouml;glicht die "Verkettung" von Operationen in einem bestimmten Kontext.
 *         Die `bind`-Methode erm&ouml;glicht es, eine Funktion anzuwenden, die einen neuen Wert
 *         desselben Typs erzeugt, und das Ergebnis dieser Anwendung im Kontext zu halten.
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
@FunctionalInterface
public interface H2Bindable<WT extends WitnessType, A, R> {

    /**
     * <div>
     *     <p>
     *         Verkn&uuml;pft diesen Wert mit einer anderen Operation.
     *     </p>
     *     <p>
     *         Wendet die gegebene Funktion `transformation` auf das aktuelle Element an.
     *         Die Funktion `transformation` muss selbst einen Wert desselben Typs zur&uuml;ckgeben.
     *         Das Ergebnis dieser Verkn&uuml;pfung wird ebenfalls im aktuellen Kontext gehalten.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion, die auf das aktuelle Element angewendet wird.
     * @param <B> Der Typ der Elemente nach der Verkn&uuml;pfung.
     * @return Das Ergebnis der Verkn&uuml;pfung.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher2<? extends WT, B, R> bind(final @NonNull Function<A, ? extends Higher2<? extends WT, B, R>> transformation);

}
