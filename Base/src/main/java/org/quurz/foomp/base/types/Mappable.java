package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Interface f&uuml;r 'Functor'-&auml;hnliche Typen
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des implementierenden Higher-Kinded-Typs
 * @param <A> Der 'innere' Typ des Higher-Kinded-Typs
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Mappable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Nimmt den inneren Wert dieses <code>Mappable</code>-Objekts, wendet die &uuml;bergebene Funktion auf ihn an und
     *         verpackt das Ergebnis in ein neues <code>Mappable</code>-Objekt gleichen Typs.
     *     </p>
     * </div>
     *
     * @param transformation Die anzuwendende Funktion
     * @param <B> Der Typ des inneren Werts des neuen <code>Mappable</code>-Objekts
     * @return Das neue <code>Mappable</code>-Objekt
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> map(final @NonNull Function<? super A, ? extends B> transformation);

}
