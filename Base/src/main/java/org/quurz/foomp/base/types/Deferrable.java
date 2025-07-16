package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Interface zur Verz&ouml;gerung der Ausf&uuml;hrung einer Berechnung. Bietet eine Methode, die eine Eingabe
 *         akzeptiert und die Berechnung auf einen sp&auml;teren Zeitpunkt vertagt, indem sie ein {@link Callable}-Objekt zur&uuml;ckgibt.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ der Eingabe f&uuml;r die verz&ouml;gerte Berechnung
 * @param <B> Der R&uuml;ckgabetyp der Berechnung
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Deferrable<A, B> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine verz&ouml;gerte Berechnung auf Basis der gegebenen Eingabe. Die Berechnung wird erst
     *         ausgef&uuml;hrt, wenn das resultierende {@link Callable} aufgerufen wird.
     *     </p>
     * </div>
     *
     * @param a Ein {@link Supplier} f&uuml;r die Eingabe der Berechnung
     * @return Ein {@link Callable}, das die Berechnung ausf&uuml;hrt und das Ergebnis liefert, wenn es aufgerufen wird
     */
    @NonNull
    Callable<B> defer(final @NonNull Supplier<A> a);

}
