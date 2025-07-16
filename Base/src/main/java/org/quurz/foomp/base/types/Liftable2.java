package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Interface für 'BiApplicative'-&auml;hnliche Typen
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des implementierenden Higher-Kinded-Typs
 * @param <A1> Der erste 'innere' Typ des Higher-Kinded-Typs
 * @param <A2> Der zweite 'innere' Typ des Higher-Kinded-Typs
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Liftable2<WT extends WitnessType, A1, A2> {

    /**
     * <div>
     *     <p>
     *         Entpackt die im &uuml;bergebenen <code>transformation</code>-Argument - Ein anderes <code>BiLiftable</code> -
     *         enthaltenen Funktionen, wendet sie auf die Inhalte dieses <code>BiLiftable</code>-Objekts
     *         an und verpackt die Ergebnisse in ein neues <code>BiLiftable</code>-Objekt gleichen Typs.
     *     </p>
     * </div>
     *
     * @param transformation Das die anzuwendende Funktion enthaltende <code>BiLiftable</code>-Objekt
     * @param <B1> Der erste 'innere' Typ des neuen <code>BiLiftable</code>-Objekts
     * @param <B2> Der zweite 'innere' Typ des neuen <code>BiLiftable</code>-Objekts
     * @return Ein neues <code>BiLiftable</code>-Objekt
     *
     * @since 1.0.0
     */
    <B1, B2> @NonNull Higher2<WT, B1, B2> lift(
            @NonNull final Higher2<WT, ? extends Function<? super A1, ? extends B1>, ? extends Function<? super A2, ? extends B2>> transformation
    );

}
