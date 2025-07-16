package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Basis f&uuml;r Higher-Kinded-Typen vom Rang 1.
 *     </p>
 *     <p>
 *         Dies ist die Basis f&uuml;r Higher-Kinded-Typen, die genau einen inneren Typ besitzen. Typischerweise wird
 *         diese Schnittstelle verwendet, um Typen zu modellieren, die einen einzelnen inneren Typ als Parameter
 *         ben&ouml;tigen, wie etwa Funktoren oder Monaden der ersten Ordnung.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs (Siehe auch {@link WitnessType})
 * @param <A> Der 'innere' Typ, der von diesem Higher-Kinded-Typ gehandhabt wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("unused")
public interface Higher1<WT extends WitnessType, A>
        extends Hkt<WT> {

    /**
     * <div>
     *     <p>
     *         Gibt den Rang des Higher-Kinded-Typs zur&uuml;ck.
     *     </p>
     *     <p>
     *         Da dieser Higher-Kinded-Typ genau ein Typ-Argument besitzt, gibt diese Methode immer 1 zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return 1, da dieser Higher-Kinded-Typ genau einen inneren Typ besitzt
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 1;
    }

}
