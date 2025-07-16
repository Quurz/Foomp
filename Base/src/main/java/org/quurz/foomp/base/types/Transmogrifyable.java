package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Dieses Interface ermöglicht es, ein Objekt in einen anderen Typ zu transformieren,
 *         indem eine benutzerdefinierte Umwandlungs-Funktion angewandt wird.
 *     </p>
 *     <p>
 *         Ab und an kann es n&ouml;tig sein, ein Objekt zu 'transformieren', also in einen anderen Typ umzuwandeln.<br />
 *         Wenn man mit dem neuen Typ flie&szlig;end weiterarbeiten m&ouml;chte, bietet sich an, die zu transformierende Klasse {@code Transmogrifyable} implementieren zu lassen.<br />
 *     </p>
 *     <p>
 *         <p>PS: Andere kamen auch schon auf die Idee zur „Verwandlung“. Siehe <a href="https://www.gocomics.com/calvinandhobbes/1987/03/23">hier</a>.</p>
 *     </p>
 * </div>
 *
 * @param <SELF> Typ der implementierenden Transformable-Klasse
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Transmogrifyable<SELF extends Transmogrifyable<?>> {

    /**
     * <div>
     *     <p>
     *         Wendet die Umwandlungs-Funktion auf dieses Objekt an, um fließend weiter mit dem neuen Typ zu arbeiten.
     *     </p>
     * </div>
     *
     * @param transmogrifier Die Umwandlungs-Funktion
     * @param <T> Der Typ, den die Umwandlung liefert
     * @return Ein Objekt vom Typ <code>T</code>
     *
     * @since 1.0.0
     */
    @NonNull <T> T transmogrify(final @NonNull Function<? super SELF, ? extends T> transmogrifier);

}
