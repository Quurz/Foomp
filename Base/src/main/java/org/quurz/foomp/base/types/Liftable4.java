package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher4;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Ein Interface, das eine Lift-Operation f&uuml;r vier Typen innerhalb eines Higher-Kinded Types definiert.
 *         Dies erm&ouml;glicht die Anwendung von Funktionen auf Werte innerhalb eines kontextbehafteten Typs.
 *     </p>
 * </div>
 *
 * @param <WT>  Der Witness-Typ, der den kontextbehafteten Typ repr&auml;sentiert.
 * @param <A1>  Der Typ des ersten Werts.
 * @param <A2>  Der Typ des zweiten Werts.
 * @param <A3>  Der Typ des dritten Werts.
 * @param <A4>  Der Typ des vierten Werts.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Liftable4<WT extends WitnessType, A1, A2, A3, A4> {

    /**
     * <div>
     *     <p>
     *         Hebt eine Menge von Funktionen an, um sie auf Werte innerhalb eines Higher-Kinded Types anzuwenden.
     *         Jede Funktion wird auf den entsprechenden Wert im Kontext angewendet, um ein neues, transformiertes
     *         Higher-Kinded Type-Objekt zu erzeugen.
     *     </p>
     * </div>
     *
     * @param transformation Ein {@code Higher4}-Wert, der vier Funktionen enth&auml;lt,
     *              die auf die entsprechenden Werte angewendet werden sollen.
     * @param <B1>  Der Ergebnis-Typ der ersten Funktion.
     * @param <B2>  Der Ergebnis-Typ der zweiten Funktion.
     * @param <B3>  Der Ergebnis-Typ der dritten Funktion.
     * @param <B4>  Der Ergebnis-Typ der vierten Funktion.
     *
     * @return Ein neuer {@code Higher4}-Wert, der die transformierten Ergebnisse enth&auml;lt.
     *
     * @since 1.0.0
     */
    <B1, B2, B3, B4> @NonNull Higher4<WT, B1, B2, B3, B4> lift(
            @NonNull final Higher4<
                WT,
                ? extends Function<? super A1, ? extends B1>,
                ? extends Function<? super A2, ? extends B2>,
                ? extends Function<? super A3, ? extends B3>,
                ? extends Function<? super A4, ? extends B4>
            > transformation
    );

}
