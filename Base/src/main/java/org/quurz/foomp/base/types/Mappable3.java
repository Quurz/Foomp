package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein Interface, das Transformationen (Mapping) von bis zu drei unabh&auml;ngigen Werten eines Objekts erm&ouml;glicht.
 *         Jede der `map`-Methoden wendet eine Funktion auf einen der drei Werte an und gibt eine neue Instanz
 *         mit dem transformierten Wert zur&uuml;ck.
 *     </p>
 * </div>
 *
 * @param <WT> Der Typ des zugrunde liegenden Kontextes oder des Witness-Type.
 * @param <A1> Typ des ersten Werts.
 * @param <A2> Typ des zweiten Werts.
 * @param <A3> Typ des dritten Werts.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Mappable3<WT extends WitnessType, A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den ersten Wert an und gibt eine neue Instanz
     *         mit dem transformierten ersten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des ersten Werts.
     * @param <B1> Der Typ des neuen ersten Werts nach der Transformation.
     * @return Eine neue `Mappable3`-Instanz mit transformiertem ersten Wert.
     *
     * @since 1.0.0
     */
    default <B1> @NonNull Mappable3<WT, B1, A2, A3> map1(final @NonNull Function<? super A1, ? extends B1> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(transformation, Fun.identity(), Fun.identity());
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den zweiten Wert an und gibt eine neue Instanz
     *         mit dem transformierten zweiten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des zweiten Werts.
     * @param <B2> Der Typ des neuen zweiten Werts nach der Transformation.
     * @return Eine neue `Mappable3`-Instanz mit transformiertem zweiten Wert.
     *
     * @since 1.0.0
     */
    default <B2> @NonNull Mappable3<WT, A1, B2, A3> map2(final @NonNull Function<? super A2, ? extends B2> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), transformation, Fun.identity());
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den dritten Wert an und gibt eine neue Instanz
     *         mit dem transformierten dritten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion zur Transformation des dritten Werts.
     * @param <B3> Der Typ des neuen dritten Werts nach der Transformation.
     * @return Eine neue `Mappable3`-Instanz mit transformiertem dritten Wert.
     *
     * @since 1.0.0
     */
    default <B3> @NonNull Mappable3<WT, A1, A2, B3> map3(final @NonNull Function<? super A3, ? extends B3> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapAll(Fun.identity(), Fun.identity(), transformation);
    }

    /**
     * <div>
     *     <p>
     *         Wendet drei separate Funktionen auf die drei Werte des Objekts an und gibt eine neue Instanz
     *         mit allen drei transformierten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation1 Die Funktion zur Transformation des ersten Werts.
     * @param transformation2 Die Funktion zur Transformation des zweiten Werts.
     * @param transformation3 Die Funktion zur Transformation des dritten Werts.
     * @param <B1> Der Typ des neuen ersten Werts nach der Transformation.
     * @param <B2> Der Typ des neuen zweiten Werts nach der Transformation.
     * @param <B3> Der Typ des neuen dritten Werts nach der Transformation.
     * @return Eine neue `Mappable3`-Instanz mit allen drei transformierten Werten.
     *
     * @since 1.0.0
     */
    <B1, B2, B3> Mappable3<WT, B1, B2, B3> mapAll(final @NonNull Function<? super A1, ? extends B1> transformation1,
                                                  final @NonNull Function<? super A2, ? extends B2> transformation2,
                                                  final @NonNull Function<? super A3, ? extends B3> transformation3);

}
