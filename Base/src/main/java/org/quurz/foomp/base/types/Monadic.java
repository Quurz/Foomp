package org.quurz.foomp.base.types;

import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Interface für 'Monad'-&auml;hnliche Typen. Im Gegensatz zu {@link Bindable} ist hier auch wirklich alles dabei.
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
public interface Monadic<WT extends WitnessType, A>
        extends Mappable<WT, A>,
                Liftable<WT, A>,
                Bindable<WT, A> {}
