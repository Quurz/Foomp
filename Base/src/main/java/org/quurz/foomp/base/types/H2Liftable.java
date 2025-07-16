package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert einen Typ, der eine Funktion in einen h&ouml;heren Kontext heben kann.
 *     </p>
 *     <p>
 *         Dieses Interface erweitert das Konzept von `Liftable` um einen zus&auml;tzlichen
 *         Typ-Parameter `R`, der den Result-Typ repr&auml;sentiert. Dies ist besonders n&uuml;tzlich
 *         f&uuml;r Monaden wie `Cont`, die einen expliziten Result-Typ ben&ouml;tigen.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ, der den Kontext des h&ouml;heren Typs repr&auml;sentiert.
 * @param <A> Der Typ des Arguments der anzuhebenden Funktion.
 * @param <R> Der Typ des Ergebnisses der anzuhebenden Funktion.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface H2Liftable<WT extends WitnessType, A, R> {

    /**
     * <div>
     *     <p>
     *         Hebt eine Funktion in den h&ouml;heren Kontext.
     *     </p>
     *     <p>
     *         Diese Methode nimmt eine Funktion vom Typ `A -> B` und hebt sie in den
     *         Typ `A` in diesem Kontext angewendet werden kann und ein Ergebnis vom Typ
     *         `B` im selben Kontext zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param transformation Die zu hebenden Funktion.
     * @param <B> Der Typ des Ergebnisses der anzuwendenden Funktion.
     * @return Eine neue Funktion im h&ouml;heren Kontext.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher2<? extends WT, B, R> lift(final @NonNull Higher2<? extends WT, Function<A, B>, R> transformation);

}
