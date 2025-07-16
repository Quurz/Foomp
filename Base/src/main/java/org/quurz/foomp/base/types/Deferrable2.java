package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface, das eine Berechnung beschreibt, deren zwei Eingabeparameter
 *         aufgeschoben (deferred) werden k&ouml;nnen. Statt die Parameter direkt zu &uuml;bergeben,
 *         werden sie als {@link Supplier} bereitgestellt, um ihre Berechnung erst bei Bedarf auszuf&uuml;hren.
 *     </p>
 *     <p>
 *         Dieses Interface ist n&uuml;tzlich, wenn die Berechnung von Parametern teuer ist
 *         oder die Reihenfolge der Ausf&uuml;hrung kontrolliert werden soll.
 *     </p>
 * </div>
 *
 * @param <A1> der Typ des ersten Eingabeparameters
 * @param <A2> der Typ des zweiten Eingabeparameters
 * @param <B>  der Typ des Ergebnisses der Berechnung
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Deferrable2<A1, A2, B> {

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Berechnung mit den gegebenen Parametern aus, die durch {@link Supplier}
     *         bereitgestellt werden. Die {@code Supplier} erlauben eine verz&ouml;gerte Bereitstellung
     *         der Eingabeparameter.
     *     </p>
     * </div>
     *
     * @param a1 ein {@code Supplier}, der den ersten Eingabeparameter liefert
     * @param a2 ein {@code Supplier}, der den zweiten Eingabeparameter liefert
     *
     * @return das Ergebnis der Berechnung vom Typ {@code B}
     *
     * @throws NullPointerException wenn einer der {@code Supplier} null ist
     */
    @NonNull
    Callable<B> defer(final @NonNull Supplier<A1> a1,
                      final @NonNull Supplier<A2> a2);

}
