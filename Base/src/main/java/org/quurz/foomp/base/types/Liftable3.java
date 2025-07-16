package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher3;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Repr&auml;sentiert eine Struktur, die eine "Anhebungsoperation" auf drei parametrisierte Werte erm&ouml;glicht.
 *         Diese Struktur erm&ouml;glicht das Anwenden von Funktionen auf jeden einzelnen der drei Werte
 *         in einer kontextsensitiven Umgebung und folgt einem applicative-funktor-&auml;hnlichen Prinzip.
 *     </p>
 *     <p>
 *         Durch diese Struktur können Funktionen, die sich in einer "Umgebung" befinden, auf Werte angewandt werden,
 *         die sich ebenfalls in derselben Umgebung befinden. Das Interface ist nützlich in funktionalen Kontexten,
 *         in denen eine Kombination von drei Werten unter Beibehaltung des Kontexts benötigt wird.
 *     </p>
 * </div>
 *
 * @param <WT> der Zeuge-Typ (Typklasse), der angibt, zu welcher Art von Funktor die Struktur geh&ouml;rt.
 * @param <A1> der Typ des ersten Werts.
 * @param <A2> der Typ des zweiten Werts.
 * @param <A3> der Typ des dritten Werts.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Liftable3<WT extends WitnessType, A1, A2, A3> {

    /**
     * <div>
     *     <p>
     *         Hebt eine Kombination von Funktionen an und wendet sie auf die entsprechenden Werte an.
     *         Diese Methode erm&ouml;glicht das Anwenden dreier Funktionen auf drei Werte, die in einem
     *         Kontext enthalten sind, und gibt das Ergebnis im selben Kontext zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation ein {@code Higher3}-Typ, der die Funktionen für die Werte enth&auml;lt.
     * @param <B1> der Typ des ersten Ergebniswerts nach Anwendung der Funktion.
     * @param <B2> der Typ des zweiten Ergebniswerts nach Anwendung der Funktion.
     * @param <B3> der Typ des dritten Ergebniswerts nach Anwendung der Funktion.
     * @return ein neuer {@code Higher3}-Typ, der die Ergebniswerte in derselben Umgebung
     *         wie die Ursprungswerte enthält.
     * @throws NullPointerException wenn {@code transformation} null ist.
     *
     * @since 1.0.0
     */
    <B1, B2, B3> @NonNull Higher3<WT, B1, B2, B3> lift(
            @NonNull final Higher3<
                WT,
                ? extends Function<? super A1, ? extends B1>,
                ? extends Function<? super A2, ? extends B2>,
                ? extends Function<? super A3, ? extends B3>
            > transformation
    );

}
