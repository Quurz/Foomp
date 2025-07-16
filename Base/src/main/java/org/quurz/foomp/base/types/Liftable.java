package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Interface für 'Applicative'-&auml;hnliche Typen
 *     </p>
 * </div>
 *
 * @param <WT> Der 'innere' Typ des Higher-Kinded-Typs
 * @param <A> Der 'innere' Typ des Higher-Kinded-Typs
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Liftable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *        Entpackt die im &uuml;bergebene <code>transformation</code>-Argument - Ein anderes <code>Liftable</code> - enthaltene
     *        Funktion, wendet sie auf den Inhalt dieses <code>Liftable</code>-Objekts an und verpackt das Ergebnis in ein
     *        neues <code>Liftable</code>-Objekt gleichen Typs.
     *     </p>
     * </div>
     *
     * @param transformation Das die anzuwendende Funktion enthaltende <code>Liftable</code>-Objekt
     * @param <B> Der 'innere' Typ des neuen <code>Liftable</code>-Objekts
     * @return Ein neues <code>Liftable</code>-Objekt
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> lift(final @NonNull Higher1<? extends WT, ? extends Function<? super A, ? extends B>> transformation);

}
