package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Nothing;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;

/**
 * <div>
 *     <p>
 *         Ein {@code Receiver} ist ein erweiterter {@link Consumer}, der Eingabewerte verarbeitet
 *         und eine funktionale Schnittstelle f&uuml;r kombinatorische Operationen bereitstellt.
 *     </p>
 *     <p>
 *         Diese Schnittstelle erg&auml;nzt die Standard-Consumer-Funktionalit&auml;t durch die M&ouml;glichkeit,
 *         Werte basierend auf einem {@link Predicate} zu filtern, bevor sie akzeptiert werden.
 *         Zudem implementiert der {@link Fun}-Typ, sodass eine Integration in funktionale
 *         Programmierschnittstellen erm&ouml;glicht wird.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der akzeptierten Eingabewerte
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Receiver<A>
        extends Fun<A, Nothing>,
                Consumer<A> {

    /**
     * <div>
     *     <p>
     *         Erstellt einen neuen {@code Receiver}, der die gegebene {@link Consumer}-Funktionalit&auml;t
     *         ohne Filterung &uuml;bernimmt.
     *     </p>
     * </div>
     *
     * @param consumer die Funktion, die den Wert verarbeitet
     *
     * @param <A> der Typ des Eingabewerts
     *
     * @return ein neuer {@code Receiver}, der den {@code Consumer} kapselt
     *
     * @throws NullPointerException falls {@code consumer} {@code null} ist
     */
    @SuppressWarnings("unused")
    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return receiver(consumer, value -> true);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt einen neuen {@code Receiver}, der die gegebene {@link Consumer}-Funktionalit&auml;t
     *         &uuml;bernimmt und Eingabewerte basierend auf einem {@link Predicate} filtert.
     *     </p>
     * </div>
     *
     * @param consumer die Funktion, die den Wert verarbeitet
     * @param filter die Bedingung, die ein Wert erf&uuml;llen muss, um akzeptiert zu werden
     * @param <A> der Typ des Eingabewerts
     *
     * @return ein neuer {@code Receiver}, der die gegebene Logik kapselt
     *
     * @throws NullPointerException falls {@code consumer} oder {@code filter} {@code null} ist
     */
    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer,
                                    final @NonNull Predicate<A> filter) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(filter, nullValue("filter"));
        return value -> {
            Objects.requireNonNull(value, nullValue("value"));
            if (filter.test(value)) {
                consumer.accept(value);
            }
        };
    }

    static <A> Receiver<A> receiver(final @NonNull Consumer<A> consumer,
                                    final @NonNull Predicate<A> filter,
                                    final @NonNull Function<? super A, ? extends RuntimeException> exceptionBuilder) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(filter, nullValue("filter"));
        Objects.requireNonNull(exceptionBuilder, nullValue("exceptionBuilder"));
        return value -> {
            Objects.requireNonNull(value, nullValue("value"));
            if (filter.test(value)) {
                consumer.accept(value);
            } else {
                throw Objects.requireNonNull(exceptionBuilder.apply(value), nullResultFrom("exceptionBuilder"));
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Verarbeitung eines Eingabewerts durch und gibt ein leeres Ergebnis zur&uuml;ck,
     *         um die {@link Fun}-Schnittstelle zu erf&uuml;llen.
     *     </p>
     * </div>
     *
     * @param value der Eingabewert, der verarbeitet werden soll
     *
     * @return das {@link Nothing}-Objekt
     *
     * @throws NullPointerException falls {@code value} {@code null} ist
     */
    @Override
    @NonNull
    default Nothing apply(final @NonNull A value) {
        this.accept(Objects.requireNonNull(value, nullValue("value")));
        return nothing;
    }

    /**
     * <div>
     *     <p>
     *         Akzeptiert einen Eingabewert und gibt den {@code Receiver} selbst zur&uuml;ck, um eine fluente API zu erm&ouml;glichen.
     *     </p>
     * </div>
     *
     * @param value der Eingabewert, der verarbeitet werden soll
     *
     * @return dieser {@code Receiver}
     *
     * @throws NullPointerException falls {@code value} {@code null} ist
     */
    @NonNull
    default Receiver<A> acceptAndContinue(final @NonNull A value) {
        this.accept(Objects.requireNonNull(value, nullValue("value")));
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet das erste Element sowie beliebig viele weitere Elemente und gibt den aktuellen
     *         {@code Receiver} zur weiteren Verwendung zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param first  das erste Element, das verarbeitet werden soll
     * @param others zus&auml;tzliche Elemente, die verarbeitet werden sollen
     * @return der aktuelle {@code Receiver} zur weiteren Verwendung
     *
     * @throws NullPointerException falls eines der Elemente {@code null} ist
     */
    @SuppressWarnings("unchecked")
    default Receiver<A> acceptAllAndContinue(final @NonNull A first,
                                             final @NonNull A... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(others, nullValue("others"));
        this.accept(first);
        for (final A other : others) {
            this.accept(other);
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet alle Elemente aus einer {@link Iterable}-Sammlung und gibt den aktuellen
     *         {@code Receiver} zur weiteren Verwendung zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param iterator eine Sammlung von Elementen, die verarbeitet werden sollen
     * @return der aktuelle {@code Receiver} zur weiteren Verwendung
     *
     * @throws NullPointerException falls {@code iterator} oder eines der Elemente {@code null} ist
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Iterator<A> iterator) {
        Objects.requireNonNull(iterator, nullValue("iterator"));
        while (iterator.hasNext()) {
            final var value
                = Objects.requireNonNull(iterator.next(), nullSuppliedFrom("iterator"));
            this.accept(value);
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet alle Elemente aus einem {@link Stream} und gibt den aktuellen
     *         {@code Receiver} zur weiteren Verwendung zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param values ein Stream von Elementen, die verarbeitet werden sollen
     * @return der aktuelle {@code Receiver} zur weiteren Verwendung
     *
     * @throws NullPointerException falls {@code values} oder eines der Elemente {@code null} ist
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Stream<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        values.forEach(this);
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Verarbeitet alle Elemente aus einer {@link Collection} und gibt den aktuellen
     *         {@code Receiver} zur weiteren Verwendung zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param values eine Sammlung von Elementen, die verarbeitet werden sollen
     * @return der aktuelle {@code Receiver} zur weiteren Verwendung
     *
     * @throws NullPointerException falls {@code values} oder eines der Elemente {@code null} ist
     */
    default Receiver<A> acceptAllAndContinue(final @NonNull Collection<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        for (final A value : values) {
            this.accept(value);
        }
        return this;
    }

}
