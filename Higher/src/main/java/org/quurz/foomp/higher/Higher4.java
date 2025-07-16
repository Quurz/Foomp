package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Basis für Higher-Kinded-Typen vom Rang 4
 *     </p>
 *     <p>
 *         Diese Schnittstelle dient als Basis f&uuml;r Higher-Kinded-Typen, die genau vier Typ-Argumente besitzen.
 *         Solche Typen werden oft verwendet, um Strukturen zu modellieren, die vier Typen als Parameter ben&ouml;tigen.
 *         Beispiele sind Funktoren oder Monaden der vierten Ordnung.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs (Siehe auch {@link WitnessType})
 * @param <A> Das erste Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <B> Das zweite Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <C> Das dritte Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 * @param <D> Das vierte Typ-Argument, der von diesem Higher-Kinded-Typ gehandhabt wird
 *
 * @since 1.0.0
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher4<WT extends WitnessType, A, B, C, D>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Gibt den Rang des Higher-Kinded-Typs zur&uuml;ck.
     *     </p>
     *     <p>
     *         Da dieser Higher-Kinded-Typ genau vier Typ-Argumente besitzt, gibt diese Methode immer 4 zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return 4, da dieser Higher-Kinded-Typ genau zwei innere Typen besitzt
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 4;
    }

}
