package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert einen Typ, dessen Elemente nach einer bestimmten Funktion abgebildet werden k&ouml;nnen.
 *     </p>
 *     <p>
 *         &Auml;hnlich wie die `map`-Operation in funktionalen Sprachen erlaubt dieses Interface,
 *         jedes Element eines gegebenen Kontextes in einen neuen Wert abzubilden. Der Kontext
 *         bleibt dabei erhalten.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ, der den Kontext des h&ouml;heren Typs repr&auml;sentiert.
 * @param <A> Der Typ der Elemente, die abgebildet werden sollen.
 * @param <R> Der Typ des Resultats nach der Abbildung.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface H2Mappable<WT extends WitnessType, A, R> {

    /**
     * <div>
     *     <p>
     *         Bildet jedes Element auf einen neuen Wert ab.
     *     </p>
     *     <p>
     *         Wendet die gegebene Funktion `transformation` auf jedes Element des aktuellen Kontexts an
     *         und gibt einen neuen Kontext mit den abgebildeten Werten zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation Die Abbildungsfunktion, die auf jedes Element angewendet wird.
     * @param <B> Der Typ der abgebildeten Werte.
     * @return Ein neuer Kontext mit den abgebildeten Werten.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher2<WT, B, R> map(final @NonNull Function<? super A, ? extends B> transformation);

}
