package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.XorValue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;

/**
 * <div>
 *     <p>
 *         Ein {@code Result<A>} repräsentiert das Ergebnis einer Berechnung, die entweder erfolgreich war
 *         und einen Wert vom Typ {@code A} zurückliefert, oder fehlgeschlagen ist und eine {@link Exception} liefert.
 *     </p>
 *     <p>
 *         Diese Schnittstelle ist ein typischer Ersatz für Ausnahmen in funktionalem Stil und entspricht
 *         einem {@code XorValue<Exception, A>}, wobei {@code Left} einem Fehler und {@code Right} einem
 *         erfolgreichen Wert entspricht.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des Erfolgswertes
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface Result<A>
        extends Transmogrifyable<Result<A>>,
                XorValue<Exception, A>
        permits Result.Success,
                Result.Failure {

    /**
     * <div>
     *     <p>
     *         Erzeugt ein {@code Result} mit einem erfolgreichen Ergebnis.
     *     </p>
     * </div>
     *
     * @param value der erfolgreiche Rückgabewert
     * @param <A>   der Typ des Werts
     * @return ein {@code Result}, das den gegebenen Wert enthält
     * @throws NullPointerException wenn der Wert {@code null} ist
     *
     * @since 1.0.0
     */
    static <A> Result<A> success(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Result.Success<>(value);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein {@code Result} mit einem Fehler.
     *     </p>
     * </div>
     *
     * @param exception die aufgetretene Ausnahme
     * @param <A>       der erwartete Typ des Erfolgswertes (wird durch das Interface benötigt)
     * @return ein {@code Result}, das den Fehler enthält
     * @throws NullPointerException wenn {@code exception} {@code null} ist
     *
     * @since 1.0.0
     */
    static <A> Result<A> failure(final @NonNull Exception exception) {
        Objects.requireNonNull(exception, nullValue("exception"));
        return new Result.Failure<>(exception);
    }

    /**
     * <div>
     *     <p>
     *         Gibt {@code true} zurück, wenn dieses {@code Result} ein erfolgreicher Wert ist.
     *     </p>
     * </div>
     *
     * @return {@code true}, wenn ein erfolgreicher Wert vorliegt
     *
     * @since 1.0.0
     */
    @Override
    default boolean isRight() {
        return (this instanceof Result.Success);
    }

    /**
     * <div>
     *     <p>
     *         Alias für {@link #isRight()}.
     *     </p>
     * </div>
     *
     * @return {@code true}, wenn das Ergebnis erfolgreich ist
     *
     * @since 1.0.0
     */
    default boolean isSuccess() {
        return this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Gibt {@code true} zurück, wenn dieses {@code Result} einen Fehler enthält.
     *     </p>
     * </div>
     *
     * @return {@code true}, wenn ein Fehler vorliegt
     *
     * @since 1.0.0
     */
    default boolean isFailure() {
        return this.isLeft();
    }

    /**
     * <div>
     *     <p>
     *         Gibt den Erfolgswert zurück.
     *     </p>
     * </div>
     *
     * @return der Erfolgswert
     * @throws NoSuchElementException wenn dieses {@code Result} ein Fehler ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default A getRight()
            throws NoSuchElementException {
        return switch (this) {
            case Result.Success<A> success -> success.value;
            case Result.Failure<A> failure -> throw new NoSuchElementException(noValuePresent(), failure.exception);
        };
    }

    /**
     * <div>
     *     <p>
     *         Alias für {@link #getRight()}.
     *     </p>
     *     <p>
     *         Gibt den Erfolgswert dieses {@code Result} zurück oder wirft eine {@link NoSuchElementException},
     *         falls ein Fehler vorliegt.
     *     </p>
     * </div>
     *
     * @return der Erfolgswert vom Typ {@code A}
     * @throws NoSuchElementException wenn dieses {@code Result} einen Fehler repräsentiert
     *
     * @since 1.0.0
     */
    default A getValue()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *     <p>
     *         Gibt die Ausnahme zurück, falls ein Fehler vorliegt.
     *     </p>
     * </div>
     *
     * @return die enthaltene {@link Exception}
     * @throws NoSuchElementException wenn dieses {@code Result} erfolgreich war
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default Exception getLeft()
            throws NoSuchElementException {
        return switch (this) {
            case Result.Success<A> _$ -> throw new NoSuchElementException(noValuePresent());
            case Result.Failure<A> failure -> failure.exception;
        };
    }

    /**
     * <div>
     *     <p>
     *         Alias für {@link #getLeft()}.
     *     </p>
     *     <p>
     *         Gibt die enthaltene Ausnahme zurück, falls dieses {@code Result} ein Fehler ist,
     *         andernfalls wird eine {@link NoSuchElementException} geworfen.
     *     </p>
     * </div>
     *
     * @return die enthaltene {@link Exception}
     * @throws NoSuchElementException wenn dieses {@code Result} ein Erfolgswert ist
     *
     * @since 1.0.0
     */
    default Exception getException()
            throws NoSuchElementException {
        return this.getLeft();
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses {@code Result} in ein {@link Either} um.
     *     </p>
     *     <p>
     *         Im Erfolgsfall wird ein {@link Either.Right} mit dem enthaltenen Wert zurückgegeben,
     *         im Fehlerfall ein {@link Either.Left} mit der enthaltenen Ausnahme.
     *     </p>
     *     <p>
     *         Die Werte werden lazy geliefert, d.&nbsp;h. erst bei Bedarf ausgewertet.
     *     </p>
     * </div>
     *
     * @return ein {@link Either}, das entweder den Erfolgswert oder die Ausnahme enthält
     *
     * @since 1.0.0
     */
    default Either<Exception, A> toEither() {
        return switch (this) {
            case Result.Success<A> success -> new Either.Right<>(() -> success.value);
            case Result.Failure<A> failure -> new Either.Left<>(() -> failure.exception);
        };
    }

    // TODO: Test & JavaDoc
    default Maybe<A> toMaybe() {
        return switch (this) {
            case Result.Success<A> success -> some(success.value);
            case Result.Failure<A> failure -> none();
        };
    }

    // TODO: Test & JavaDoc
    @Override
    default <T> @NonNull T transmogrify(final @NonNull Function<? super Result<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Erfolgs-Repräsentation eines {@code Result}.
     *     </p>
     * </div>
     *
     * @param <R> der Typ des Erfolgswertes
     *
     * @since 1.0.0
     */
    final class Success<R>
            implements Result<R> {

        private final R value;

        private Success(final R value) {
            this.value
                = value;
        }

        /**
         * <div>
         *     <p>
         *         Vergleicht dieses {@code Success}-Objekt mit einem anderen auf Gleichheit.
         *     </p>
         * </div>
         *
         * @param o das zu vergleichende Objekt
         * @return {@code true}, wenn das andere Objekt ebenfalls ein {@code Success} mit gleichem Wert ist
         *
         * @since 1.0.0
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Success<?> success)) return false;

            return value.equals(success.value);
        }

        /**
         * <div>
         *     <p>
         *         Gibt den Hashcode des enthaltenen Wertes zurück.
         *     </p>
         * </div>
         *
         * @return der Hashcode
         *
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            return value.hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Gibt eine String-Repräsentation des {@code Success}-Objekts zurück.
         *     </p>
         * </div>
         *
         * @return eine String-Repräsentation mit dem enthaltenen Wert
         *
         * @since 1.0.0
         */
        @Override
        public String toString() {
            return new StringJoiner(", ", Success.class.getSimpleName() + "[", "]")
                    .add("value=" + value)
                    .toString();
        }

    }

    /**
     * <div>
     *     <p>
     *         Fehler-Repräsentation eines {@code Result}.
     *     </p>
     * </div>
     *
     * @param <R> der erwartete Typ des Erfolgswertes
     *
     * @since 1.0.0
     */
    final class Failure<R>
            implements Result<R> {

        private final Exception exception;

        private Failure(final Exception exception) {
            this.exception
                = exception;
        }

        /**
         * <div>
         *     <p>
         *         Vergleicht dieses {@code Failure}-Objekt mit einem anderen auf Gleichheit.
         *     </p>
         * </div>
         *
         * @param o das zu vergleichende Objekt
         * @return {@code true}, wenn das andere Objekt ebenfalls ein {@code Failure} mit gleicher Exception ist
         *
         * @since 1.0.0
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Failure<?> failure)) return false;

            return exception.equals(failure.exception);
        }

        /**
         * <div>
         *     <p>
         *         Gibt den Hashcode der enthaltenen {@link Exception} zurück.
         *     </p>
         * </div>
         *
         * @return der Hashcode
         *
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            return exception.hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Gibt eine String-Repräsentation des {@code Failure}-Objekts zurück.
         *     </p>
         * </div>
         *
         * @return eine String-Repräsentation mit der enthaltenen Exception
         *
         * @since 1.0.0
         */
        @Override
        public String toString() {
            return new StringJoiner(", ", Failure.class.getSimpleName() + "[", "]")
                    .add("exception=" + exception)
                    .toString();
        }

    }

}
