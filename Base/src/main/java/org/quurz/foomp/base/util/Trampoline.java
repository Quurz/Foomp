package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Value;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * <div>
 *     Eine H&uuml;pfburg!
 * </div>
 *
 * <div>
 *     <p>
 *         Wandelt eine Rekursion in eine stack-sichere Iteration
 *     </p>
 *     <p>
 *         <cite>To iterate is human, to recurse divine</cite>
 *     </p>
 *     <p>
 *         Ein Beispiel sagt vielleicht mehr dar&uuml;ber aus, was das Dingen eigentlich macht:
 *     </p>
 *     <pre>{@code
 *          private void play()
 *              throws Exception {
 *              final var param
 *                  = BigInteger.valueOf(100000);
 *              final var result
 *                  = this.factorial(param);
 *              LOGGER.info("factorial({}) = {}", param, result);
 *          }
 *
 *          private BigInteger factorial(final BigInteger param) {
 *              return this._factorial(param, BigInteger.ONE).get();
 *          }
 *
 *          private Trampoline<BigInteger> _factorial(final BigInteger param,
 *                                                    final BigInteger accu) {
 *              if (param.equals(BigInteger.ONE)) {
 *                  return Trampoline.done(accu);
 *              } else {
 *                  return Trampoline.more(() -> _factorial(param.subtract(BigInteger.ONE), accu.multiply(param)));
 *              }
 *          }
 *     }</pre>
 *     <div>
 *         H&auml;tte man das einfach rekursiv versucht, w&auml;re einem ein StackOverflowError um die Ohren geflogen... &#128539;
 *     </div>
 * </div>
 *
 * @param <T> der Typ des Ergebnisses, das vom Trampolin erzeugt wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface Trampoline<T>
        extends Value<T>
        permits Trampoline.More,
                Trampoline.Done {

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz eines Trampolins, das eine Berechnung
     *         repr&auml;sentiert, die noch l&auml;uft. Dieses Trampolin liefert weitere
     *         Trampoline, bis ein endg&uuml;ltiges Ergebnis erzielt wird.
     *     </p>
     * </div>
     *
     * @param trampoline ein Supplier, der das n&auml;chste Trampolin zur Auswertung bereitstellt
     * @param <T> der Typ des Ergebnisses, das vom Trampolin erzeugt wird
     * @return ein {@link Trampoline}, das eine laufende Berechnung darstellt
     *
     * @since 1.0.0
     */
    static <T> Trampoline<T> more(final @NonNull Supplier<Trampoline<T>> trampoline) {
        return new More<>(Objects.requireNonNull(trampoline));
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz eines Trampolins, das eine abgeschlossene Berechnung
     *         mit einem Ergebnis repr&auml;sentiert.
     *     </p>
     * </div>
     *
     * @param result das Ergebnis der Berechnung
     * @param <T> der Typ des Ergebnisses, das vom Trampolin erzeugt wird
     * @return ein {@link Trampoline}, das eine abgeschlossene Berechnung darstellt
     *
     * @since 1.0.0
     */
    static <T> Trampoline<T> done(final @NonNull T result) {
        return new Done<>(Objects.requireNonNull(result));
    }

    /**
     * <div>
     *     <p>
     *         &Uuml;berprüft, ob das Trampolin einen Wert enth&auml;lt. Diese Methode gibt true zur&uuml;ck,
     *         wenn das Trampolin eine {@link Done}-Instanz ist.
     *     </p>
     * </div>
     *
     * @return true, wenn das Trampolin einen vorhandenen Wert hat, andernfalls false
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return (this instanceof Trampoline.Done<T>);
    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert eine laufende Berechnung im Trampolinmechanismus.
     *         Diese Klasse bietet eine M&ouml;glichkeit, die Berechnung fortzusetzen,
     *         indem ein Supplier für das n&auml;chste Trampolin zur Ausf&uuml;hrung bereitgestellt wird.
     *     </p>
     * </div>
     *
     * @param <T> der Typ des Ergebnisses, das vom Trampolin erzeugt wird
     *
     * @since 1.0.0
     */
    final class More<T>
            implements Trampoline<T> {

        private final Supplier<Trampoline<T>> trampoline;

        private More(final Supplier<Trampoline<T>> trampoline) {
            this.trampoline
                = trampoline;
        }

        /**
         * <div>
         *     <p>
         *         Holt das endg&uuml;ltige Ergebnis des Trampolins, indem die Berechnung ausgewertet wird.
         *         Es wird fortgefahren, die bereitgestellten Trampoline auszuwerten, bis ein
         *         abgeschlossenes Trampolin erreicht wird.
         *     </p>
         * </div>
         *
         * @return das Ergebnis der abgeschlossenen Berechnung
         *
         * @since 1.0.0
         */
        @Override
        public @NonNull T get() {
            var current
                = this.trampoline.get();
            while (current instanceof Trampoline.More<T> more) {
                current = more.trampoline.get();
            }
            return current.get();
        }

    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert eine abgeschlossene Berechnung im Trampolinmechanismus,
     *         die das endg&uuml;ltige Ergebnis kapselt.
     *     </p>
     * </div>
     *
     * @param <T> der Typ des Ergebnisses, das vom Trampolin erzeugt wird
     *
     * @since 1.0.0
     */
    final class Done<T>
            implements Trampoline<T> {

        private final T result;

        private Done(final T result) {
            this.result
                = result;
        }

        /**
         * <div>
         *     <p>
         *         &Uuml;berpr&uuml;ft, ob das Trampolin einen Wert enth&auml;lt. Im Fall von
         *         {@link Done} gibt diese Methode true zurück.
         *     </p>
         * </div>
         *
         * @return true, was anzeigt, dass das Trampolin einen vorhandenen Wert hat
         *
         * @since 1.0.0
         */
        @Override
        public boolean isPresent() {
            return true;
        }

        /**
         * <div>
         *     <p>
         *         Holt das Ergebnis der abgeschlossenen Berechnung.
         *     </p>
         * </div>
         *
         * @return das Ergebnis der Berechnung
         */
        @Override
        public @NonNull T get() {
            return this.result;
        }

    }

}
