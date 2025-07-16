package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Kann man's umdrehen?<br />
 *         Muss man's umdrehen können?<br />
 *         Ei, dann nehmen wir <code>Swappable</code>! &#128512;
 *     </p>
 * </div>
 *
 * @param <SELF> Typ der implementierenden Swappable-Klasse
 * @param <A> Der erste 'innere' Typ des Swappable
 * @param <B> Der zweite 'innere' Typ des Swappable
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Swappable<SELF extends Swappable<?, B, A>, A, B> {

    /**
     * <div>
     *     <p>
     *         Dreht das {@code Swappable<A, B>} um zu einem {@code Swappable<B, A>}
     *     </p>
     * </div>
     *
     * @return Das {@code Swappable<B, A>}
     *
     * @since 1.0.0
     */
    @NonNull SELF swap();

}
