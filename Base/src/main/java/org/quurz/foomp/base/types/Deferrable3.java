package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface, das eine Berechnung mit drei Eingabewerten verz&ouml;gert ausf&uuml;hrt.
 *     </p>
 * </div>
 *
 * @param <A1> der Typ des ersten Eingabewerts
 * @param <A2> der Typ des zweiten Eingabewerts
 * @param <A3> der Typ des dritten Eingabewerts
 *
 * @param <B> der Typ des Ergebnisses der Berechnung
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Deferrable3<A1, A2, A3, B> {

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Berechnung verz&ouml;gert aus, indem die drei Eingabewerte bei Bedarf bereitgestellt werden.
     *     </p>
     * </div>
     *
     * @param a1 ein {@link Supplier}, der den ersten Eingabewert liefert
     * @param a2 ein {@link Supplier}, der den zweiten Eingabewert liefert
     * @param a3 ein {@link Supplier}, der den dritten Eingabewert liefert
     *
     * @return das Ergebnis der Berechnung
     *
     * @throws NullPointerException wenn einer der bereitgestellten {@link Supplier} {@code null} ist
     */
    @NonNull
    Callable<B> defer(final @NonNull Supplier<A1> a1,
                      final @NonNull Supplier<A2> a2,
                      final @NonNull Supplier<A3> a3);

}
