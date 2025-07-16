package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface f&uuml;r Typen, die das unabh&auml;ngige oder gleichzeitige Mapping
 *         &uuml;ber zwei Typ-Parameter erm&ouml;glichen.
 *     </p>
 * </div>
 *
 * @param <WT> der "witness"-Typ, der einen bestimmten Kontext f&uuml;r Mapping-Operationen repr&auml;sentiert
 * @param <A1> der erste Typ-Parameter, der abgebildet werden kann
 * @param <A2> der zweite Typ-Parameter, der abgebildet werden kann
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Mappable2<WT extends WitnessType, A1, A2> {

    /**
     * <div>
     *     <p>
     *         Wendet eine Mapping-Funktion auf den ersten Typ-Parameter an.
     *     </p>
     * </div>
     *
     * @param <B1> der resultierende Typ nach Anwendung der Mapping-Funktion auf {@code A1}
     * @param transformation die Funktion, die auf den ersten Parameter angewendet wird; darf nicht {@code null} sein
     * @return eine neue {@code Mappable2}-Instanz mit transformiertem ersten Typ
     * @throws NullPointerException falls {@code transformation} {@code null} ist
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable2<WT, B1, A2> map(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, "Argument 'transformation' must not be null");
        return this.mapAll(transformation, Fun.identity());
    }

    /**
     * <div>
     *     <p>
     *         Alias f&uuml;r {@link #map(Function)}.
     *     </p>
     * </div>
     *
     * @param <B1> der resultierende Typ nach Anwendung der Mapping-Funktion auf {@code A1}
     * @param transformation die Funktion, die auf den ersten Parameter angewendet wird; darf nicht {@code null} sein
     * @return eine neue {@code Mappable2}-Instanz mit transformiertem ersten Typ
     * @throws NullPointerException falls {@code transformation} {@code null} ist
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable2<WT, B1, A2> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, "Argument 'transformation' must not be null");
        return this.map(transformation);
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Mapping-Funktion auf den zweiten Typ-Parameter an.
     *     </p>
     * </div>
     *
     * @param <B2> der resultierende Typ nach Anwendung der Mapping-Funktion auf {@code A2}
     * @param transformation die Funktion, die auf den zweiten Parameter angewendet wird; darf nicht {@code null} sein
     * @return eine neue {@code Mappable2}-Instanz mit transformiertem zweiten Typ
     * @throws NullPointerException falls {@code transformation} {@code null} ist
     *
     * @since 1.0.0
     */
    default <B2> @NonNull Mappable2<WT, A1, B2> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, "Argument 'transformation' must not be null");
        return this.mapAll(Fun.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Wendet Mapping-Funktionen gleichzeitig auf beide Typ-Parameter an.
     *     </p>
     * </div>
     *
     * @param <B1> der resultierende Typ nach Anwendung der Mapping-Funktion auf {@code A1}
     * @param <B2> der resultierende Typ nach Anwendung der Mapping-Funktion auf {@code A2}
     * @param transformation1 die Funktion, die auf den ersten Parameter angewendet wird; darf nicht {@code null} sein
     * @param transformation2 die Funktion, die auf den zweiten Parameter angewendet wird; darf nicht {@code null} sein
     * @return eine neue {@code Mappable2}-Instanz mit transformierten Typen
     * @throws NullPointerException falls {@code transformation1} oder {@code transformation2} {@code null} ist
     */
    <B1, B2> @NonNull Mappable2<WT, B1, B2> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                   final @NonNull Function<? super A2, ? extends B2> transformation2);

}
