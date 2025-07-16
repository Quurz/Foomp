package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.functions.MemoisingFun.memoisingFun;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine funktionale Schnittstelle, die erweiterte Funktionalit&auml;t für Funktionen bereitstellt,
 *         wie Komposition, Memoisierung und Null-Pr&uuml;fung.
 *     </p>
 * </div>
 *
 * @param <X> der Eingabetyp der Funktion
 * @param <Y> der Rückgabetyp der Funktion
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Fun<X, Y>
        extends Function<X, Y>,
                Applicable<X, Y> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code Fun}-Instanz aus einer gegebenen {@link Function},
     *         die zus&auml;tzliche Null-Pr&uuml;fungen für Eingabe- und Ausgabeparameter bietet.
     *     </p>
     * </div>
     *
     * @param <X> der Eingabetyp der Funktion
     * @param <Y> der R&uuml;ckgabetyp der Funktion
     * @param function die Funktion, die in die {@code Fun}-Instanz umgewandelt wird; darf nicht {@code null} sein
     * @return eine neue {@code Fun}-Instanz mit der angegebenen Funktionalit&auml;t
     *
     * @since 1.0.0
     */
    static <X, Y> Fun<X, Y> fun(@NonNull final Function<X, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return Objects.requireNonNull(function.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion auf den gegebenen Eingabewert an.
     *     </p>
     * </div>
     *
     * @param x der Eingabewert; darf nicht {@code null} sein
     * @return das Ergebnis der Funktion; darf nicht {@code null} sein
     * @throws NullPointerException falls {@code x} oder das Resultat {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull
    Y apply(@NonNull final X x);

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue Funktion, die die gegebene Funktion vor dieser Funktion ausführt.
     *     </p>
     * </div>
     *
     * @param <W> der Eingabetyp der ersten Funktion
     * @param first die erste anzuwendende Funktion; darf nicht {@code null} sein
     * @return eine zusammengesetzte Funktion, die zuerst {@code first} und dann diese Funktion aufruft
     * @throws NullPointerException falls {@code first} oder deren Resultat {@code null} ist
     *
     * @since 1.0.0
     */
    default <W> @NonNull Fun<W, Y> compose(@NonNull final Fun<? super W, ? extends X> first) {
        Objects.requireNonNull(first, nullValue("first"));
        return w -> {
            final var x
                = Objects.requireNonNull(first.apply(w), nullResult());
            return Objects.requireNonNull(this.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue Funktion, die die gegebene Funktion nach dieser Funktion ausführt.
     *     </p>
     * </div>
     *
     * @param <Z> der R&uuml;ckgabetyp der neuen Funktion
     * @param next die Funktion, die nach dieser Funktion ausgef&uuml;hrt wird; darf nicht {@code null} sein
     * @return eine zusammengesetzte Funktion, die zuerst diese Funktion und dann {@code next} aufruft
     * @throws NullPointerException falls {@code next} oder deren Resultat {@code null} ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("SuspiciousNameCombination")
    default <Z> @NonNull Fun<X, Z> andThen(@NonNull final Fun<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return x -> {
            final var y
                = Objects.requireNonNull(this.apply(x), nullResult());
            return Objects.requireNonNull(next.apply(y), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine memoisierte Version dieser Funktion zur&uuml;ck, die Ergebnisse zwischenspeichert und
     *         so die Performance bei mehrfachen Aufrufen mit denselben Eingaben verbessern kann.
     *     </p>
     * </div>
     *
     * @return eine memoisierte Version dieser Funktion
     *
     * @since 1.0.0
     */
    default MemoisingFun<X, Y> memoise() {
        return memoisingFun(this);
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Version dieser Funktion zur&uuml;ck, die zus&auml;tzliche Null-Pr&uuml;fungen für den
     *         Eingabeparameter und das Ergebnis durchf&uuml;hrt.
     *     </p>
     * </div>
     *
     * @return eine Null-pr&uuml;fende Version dieser Funktion
     *
     * @since 1.0.0
     */
    default Fun<X, Y> nullSafe() {
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return Objects.requireNonNull(this.apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt diese Funktion als {@code Applicable} zurück.
     *     </p>
     * </div>
     *
     * @return diese Funktion als {@code Applicable}
     *
     * @since 1.0.0
     */
    default Applicable<X, Y> applicable() {
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Identit&auml;tsfunktion zur&uuml;ck, die jeden Eingabewert unver&auml;ndert zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param <X> der Typ der Eingabe und Ausgabe
     * @return die Identit&Auml;tsfunktion für den Typ {@code X}
     *
     * @since 1.0.0
     */
    static <X> Fun<X, X> identity() {
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            return x;
        };
    }

}
