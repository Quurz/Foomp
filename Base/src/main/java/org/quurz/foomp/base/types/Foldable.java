package org.quurz.foomp.base.types;

/**
 * <div>
 *     <p>
 *         Ein Datentyp, dessen Werte durch eine Funktion aggregiert oder reduziert werden k&ouml;nnen.
 *     </p>
 *     <p>
 *         Diese Schnittstelle vereint {@link FoldableLeft} und {@link FoldableRight} und erlaubt sowohl links- als auch rechtsseitiges Falten.
 *     </p>
 * </div>
 *
 * @param <A> Typ der Elemente, die gefaltet werden
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Foldable<A>
        extends FoldableLeft<A>,
                FoldableRight<A> {}
