package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Basis f&uuml;r Higher-Kinded-Typen vom Rang 2.
 *     </p>
 *     <p>
 *         Diese Schnittstelle dient als Basis f&uuml;r Higher-Kinded-Typen, die genau zwei innere Typen besitzen.
 *         Solche Typen werden oft verwendet, um Strukturen zu modellieren, die zwei Typen als Parameter ben&ouml;tigen.
 *         Beispiele für solche Typen sind Funktoren oder Monaden der zweiten Ordnung, die zwei Typen als Parameter ben&ouml;tigen.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs (Siehe auch {@link WitnessType})
 * @param <A> Das erste Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <B> Das zweite Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher2<WT extends WitnessType, A, B>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Gibt den Rang des Higher-Kinded-Typs zur&uuml;ck.
     *     </p>
     *     <p>
     *         Da dieser Higher-Kinded-Typ genau zwei Typ-Argumente besitzt, gibt diese Methode immer 2 zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return 2, da dieser Higher-Kinded-Typ genau zwei innere Typen besitzt
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 2;
    }

}
