package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Basis für Higher-Kinded-Typen vom Rang 3.
 *     </p>
 *     <p>
 *         Diese Schnittstelle dient als Basis f&uuml;r Higher-Kinded-Typen, die genau drei innere Typen besitzen.
 *         Solche Typen werden oft verwendet, um Strukturen zu modellieren, die drei Typen als Parameter ben&ouml;tigen.
 *         Beispiele sind Funktoren oder Monaden der dritten Ordnung.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs (Siehe auch {@link WitnessType})
 * @param <A> Das erste Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <B> Das zweite Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <C> Das dritte Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher3<WT extends WitnessType, A, B, C>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Gibt den Rang des Higher-Kinded-Typs zur&uuml;ck.
     *     </p>
     *     <p>
     *         Da dieser Higher-Kinded-Typ genau drei Typ-Argumente besitzt, gibt diese Methode immer 3 zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return 3, da dieser Higher-Kinded-Typ genau zwei innere Typen besitzt
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 3;
    }

}
