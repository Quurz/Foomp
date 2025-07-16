package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.types.Bindable;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Liftable2;
import org.quurz.foomp.base.types.Mappable;
import org.quurz.foomp.base.types.Swappable;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.XorValue;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;

/**
 * <div>
 *     <p>
 *         Ein generischer Container, der entweder einen Wert des Typs {@code L} (linke Seite)
 *         oder einen Wert des Typs {@code R} (rechte Seite) enth&auml;lt.
 *     </p>
 *     <p>
 *         Dieser Typ wird h&auml;ufig verwendet, um alternative Ergebnisse oder Fehlerf&auml;lle zu
 *         modellieren, ohne Ausnahmen zu werfen.
 *     </p>
 * </div>
 *
 * @param <L> der Typ des Wertes auf der linken Seite
 * @param <R> der Typ des Wertes auf der rechten Seite
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Either<L, R>
        extends Mappable<Either.µ, R>,
                Liftable2<Either.µ, L, R>,
                Bindable<Either.µ, R>,
                Swappable<Either<R, L>, L, R>,
                Copyable<Either<L, R>>,
                Unwindable<Either<L, R>>,
                Transmogrifyable<Either<L, R>>,
                XorValue<L, R>,
                Higher2<Either.µ, L, R>,
                Higher1<Either.µ, R>
        permits Either.Left,
                Either.Right {


    /**
     * <div>
     *     <p>
     *         Der {@code WitnessType} f&uuml;r den {@code Either}-Typ.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Wandelt ein {@code Higher2} in ein {@code Either} um.
     *     </p>
     * </div>
     *
     * @param wide das breite {@code Higher2}
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     *
     * @return das umgewandelte {@code Either}
     *
     * @since 1.0.0
     */
    static <L, R> Either<L, R> narrow(final @NonNull Higher2<µ, L, R> wide) {
        return (Either<L, R>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Entpackt ein {@code Either} aus.
     *     </p>
     * </div>
     *
     * @param wrapped das eingepackte {@code Either}
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     *
     * @return das ausgewickelte {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> unwrap(final @NonNull Either<?, ? extends Higher1<? extends µ, R>> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("wrapped"));
        final var narrowed
            = narrow(wrapped);
        return narrow((Higher2<µ, L, R>) narrowed.get());
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein {@code Either} mit einem linken Wert.
     *     </p>
     * </div>
     *
     * @param value der linke Wert
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     * @return das erzeugte {@code Either}
     *
     * @since 1.0.0
     */
    static <L, R> Either<L, R> left(final @NonNull L value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Left<>(() -> value);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein {@code Either} mit einem rechten Wert.
     *     </p>
     * </div>
     *
     * @param value der rechte Wert
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     *
     * @return das erzeugte {@code Either}
     *
     * @since 1.0.0
     */
    static <L, R> Either<L, R> right(final @NonNull R value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Right<>(() -> value);
    }

    /**
     * <div>
     *     <p>
     *         Gibt an, ob das {@code Either} einen linken Wert enth&auml;lt.
     *     </p>
     * </div>
     *
     * @return true, wenn das {@code Either} einen linken Wert enth&auml;lt, sonst false
     *
     * @since 1.0.0
     */
    @Override
    default boolean isLeft() {
        return !this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Gibt den linken Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der linke Wert
     * @throws NoSuchElementException wenn kein linker Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default L getLeft()
            throws NoSuchElementException {
        return switch (this) {
            case Left<L, R> left -> Objects.requireNonNull(left.spool.get(), nullSupplied());
            case Right<L, R> _$ -> throw new NoSuchElementException(noValuePresent());
        };
    }


    /**
     * <div>
     *     <p>
     *         Gibt den linken Wert zur&uuml;ck oder einen alternativen Wert, wenn kein linker Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param supplier der Lieferant des alternativen Wertes
     *
     * @return der linke Wert oder der alternative Wert
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default L getLeftOrElse(@NonNull final Supplier<L> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Left<L, R> left -> left.getLeft();
            case Right<L, R> _$ -> Objects.requireNonNull(supplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den linken Wert sicher zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return ein {@code Maybe.Some} mit dem linken Wert oder ein {@code Maybe.None}, wenn kein linker Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Maybe<L> getLeftSafe() {
        return switch (this) {
            case Left<L, R> left -> some(left.getLeft());
            case Right<L, R> _$ -> none();
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den linken Wert zur&uuml;ck oder wirft eine Ausnahme, wenn kein linker Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param exceptionSupplier der Lieferant der Ausnahme
     * @param <E> der Typ der Ausnahme
     *
     * @return der linke Wert
     *
     * @throws E die Ausnahme, wenn kein linker Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default <E extends Exception> L getLeftOrThrow(@NonNull final Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Left<L, R> left -> left.getLeft();
            case Right<L, R> _$ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt an, ob das {@code Either} einen rechten Wert enth&auml;lt.
     *     </p>
     * </div>
     *
     * @return true, wenn das {@code Either} einen rechten Wert enth&auml;lt, sonst false
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Gibt an, ob das {@code Either} einen rechten Wert enth&auml;lt.
     *     </p>
     * </div>
     *
     * @return true, wenn das {@code Either} einen rechten Wert enth&auml;lt, sonst false
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unused")
    default boolean isRight() {
        return switch (this) {
            case Left<L, R> $_ -> false;
            case Right<L, R> $_ -> true;
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den rechten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der rechte Wert
     * @throws NoSuchElementException wenn kein rechter Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default R get()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *     <p>
     *         Gibt den rechten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der rechte Wert
     *
     * @throws NoSuchElementException wenn kein rechter Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default R getRight()
            throws NoSuchElementException {
        return switch (this) {
            case Left<L, R> $_ -> throw new NoSuchElementException(noValuePresent());
            case Right<L, R> right -> Objects.requireNonNull(right.spool.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den rechten Wert zur&uuml;ck oder einen alternativen Wert, wenn kein rechter Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param supplier der Lieferant des alternativen Wertes
     *
     * @return der rechte Wert oder der alternative Wert
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default R getRightOrElse(@NonNull final Supplier<R> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Left<L, R> $_ -> Objects.requireNonNull(supplier.get(), nullSupplied());
            case Right<L, R> right -> this.getRight();
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den rechten Wert sicher zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return ein {@code Maybe.Some} mit dem rechten Wert oder ein {@code Maybe.None}, wenn kein rechter Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default Maybe<R> getRightSafe() {
        return switch (this) {
            case Left<L, R> $_ -> none();
            case Right<L, R> right -> some(right.getRight());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt den rechten Wert zur&uuml;ck oder wirft eine Ausnahme, wenn kein rechter Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param exceptionSupplier der Lieferant der Ausnahme
     * @param <E> der Typ der Ausnahme
     * @return der rechte Wert
     *
     * @throws E die Ausnahme, wenn kein rechter Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @SuppressWarnings("unused")
    @NonNull
    default <E extends Exception> R getRightOrThrow(@NonNull final Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Left<L, R> $_ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
            case Right<L, R> right -> right.getRight();
        };
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine Aktion aus, wenn ein linker Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param consumer der Verbraucher des linken Wertes
     *
     * @return das {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifLeft(@NonNull final Consumer<L> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return this.ifEither(consumer, _$ -> {});
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine Aktion aus, wenn ein rechter Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param consumer der Verbraucher des rechten Wertes
     *
     * @return das {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifRight(@NonNull final Consumer<R> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return this.ifEither(_$ -> {}, consumer);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine Aktion aus, wenn ein linker oder rechter Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @param leftConsumer der Verbraucher des linken Wertes
     * @param rightConsumer der Verbraucher des rechten Wertes
     *
     * @return das {@code Either}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Either<L, R> ifEither(@NonNull final Consumer<L> leftConsumer,
                                  @NonNull final Consumer<R> rightConsumer) {
        Objects.requireNonNull(leftConsumer, nullValue("leftConsumer"));
        Objects.requireNonNull(rightConsumer, nullValue("rightConsumer"));
        switch (this) {
            case Left<L, R> left -> leftConsumer.accept(left.getLeft());
            case Right<L, R> right -> rightConsumer.accept(right.getRight());
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Mappt den rechten Wert.
     *     </p>
     * </div>
     *
     * @param transformation die Mapping-Funktion
     * @param <S> der Typ des gemappten Wertes
     *
     * @return das gemappte {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <S> Either<L, S> map(final @NonNull Function<? super R, ? extends S> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return this.mapRight(transformation);
    }

    /**
     * <div>
     *     <p>
     *         Mappt den rechten Wert.
     *     </p>
     * </div>
     *
     * @param fMap die Mapping-Funktion
     * @param <S> der Typ des gemappten Wertes
     *
     * @return das gemappte {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <S> Either<L, S> mapRight(@NonNull final Function<? super R, ? extends S> fMap) {
        Objects.requireNonNull(fMap, nullValue("fMap"));
        return switch (this) {
            case Left<L, R> left -> (Either<L, S>) left;
            case Right<L, R> right -> new Right<>(() -> {
                final var r
                    = Objects.requireNonNull(right.spool.get(), nullSupplied());
                return Objects.requireNonNull(fMap.apply(r), nullResult());
            });
        };
    }

    /**
     * <div>
     *     <p>
     *         Mappt den linken Wert.
     *     </p>
     * </div>
     *
     * @param fMap die Mapping-Funktion
     * @param <M> der Typ des gemappten Wertes
     *
     * @return das gemappte {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <M> Either<M, R> mapLeft(@NonNull final Function<? super L, ? extends M> fMap) {
        Objects.requireNonNull(fMap, nullValue("fMap"));
        return switch (this) {
            case Left<L, R> left -> new Left<>(() -> {
                final var l
                    = Objects.requireNonNull(left.spool.get(), nullSupplied());
                return Objects.requireNonNull(fMap.apply(l), nullResult());
            });
            case Right<L, R> right -> (Either<M, R>) right;
        };
    }

    /**
     * <div>
     *     <p>
     *         Mappt den linken und rechten Wert.
     *     </p>
     * </div>
     *
     * @param fMapLeft die Mapping-Funktion f&uuml;r den linken Wert
     * @param fMapRight die Mapping-Funktion f&uuml;r den rechten Wert
     * @param <M> der Typ des gemappten linken Wertes
     * @param <S> der Typ des gemappten rechten Wertes
     *
     * @return das gemappte {@code Either}
     *
     * @since 1.0.0
     */
    // TODO: Test
    @SuppressWarnings("unchecked")
    @NonNull
    default <M, S> Either<M, S> mapEither(final @NonNull Fun<? super L, ? extends M> fMapLeft,
                                          final @NonNull Fun<? super R, ? extends S> fMapRight) {
        Objects.requireNonNull(fMapLeft, nullValue("fMapLeft"));
        Objects.requireNonNull(fMapRight, nullValue("fMapRight"));
        return (Either<M, S>) this.mapLeft(fMapLeft).mapRight(fMapRight);
    }

    /**
     * <div>
     *     <p>
     *         Wendet die gegebene Funktion an, die im {@code Either}-Objekt enthalten ist.
     *     </p>
     * </div>
     *
     * @param transformation Das die anzuwendende Funktion enthaltende {@code Either}-Objekt
     * @param <M> Der Typ des gemappten linken Wertes
     * @param <S> Der Typ des gemappten rechten Wertes
     *
     * @return Das gemappte {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    default <M, S> Either<M, S> lift(final @NonNull Higher2<µ, ? extends Function<? super L, ? extends M>, ? extends Function<? super R, ? extends S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        final Either<? extends Function<? super L, ? extends M>, ? extends Function<? super R, ? extends S>> narrowed
            = narrow(transformation);
        return switch (this) {
            case Left<L, R> left -> narrowed.isLeft()
                                        ? (Either<M, S>) left.mapLeft(narrowed.getLeft())
                                        : (Either<M, S>) left;
            case Right<L, R> right -> narrowed.isRight()
                                        ? (Either<M, S>) right.mapRight(narrowed.getRight())
                                        : (Either<M, S>) right;
        };
    }

    /**
     * <div>
     *     <p>
     *         Bindet den rechten Wert an eine Funktion, die ein {@code Either} zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param transformation die Bindefunktion
     * @param <S> der Typ des gebundenen Wertes
     *
     * @return das gebundene {@code Either}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    default <S> Either<L, S> bind(final @NonNull Function<? super R, ? extends Higher1<? extends µ, S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return switch (this) {
            case Right<L, R> right -> (Either<L, S>) unwrap(right.map(transformation));
            case Left<L, R> left -> (Either<L, S>) left;
        };
    }

    /**
     * <div>
     *     <p>
     *         Vertauscht die linke und rechte Seite des {@code Either}.
     *     </p>
     * </div>
     *
     * @return das vertauschte {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Either<R, L> swap() {
        return switch (this) {
            case Left<L, R> left -> new Right<>(left.spool);
            case Right<L, R> right -> new Left<>(right.spool);
        };
    }

    /**
     * <div>
     *     <p>
     *         Kopiert das {@code Either}.
     *     </p>
     * </div>
     *
     * @return die Kopie des {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Either<L, R> copy() {
        return switch (this) {
            case Left<L, R> left -> new Left<>(left.spool);
            case Right<L, R> right -> new Right<>(right.spool);
        };
    }

    /**
     * <div>
     *     <p>
     *         Transmogrifiziert das {@code Either} mit einer gegebenen Funktion.
     *     </p>
     * </div>
     *
     * @param transmogrifier die Transmogrifizierungsfunktion
     * @param <T> der Typ des transmogrifizierten Wertes
     *
     * @return der transmogrifizierte Wert
     *
     * @since 1.0.0
     */
    @Override
    default <T> @NonNull T transmogrify(final @NonNull Function<? super Either<L, R>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Spult alle aufgewickelten Operationen in diesem {@code Either} ab.
     *     </p>
     * </div>
     *
     * @return das entwirrte {@code Either}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Either<L, R> unwind() {
        return switch (this) {
            case Left<L, R> left -> left(left.getLeft());
            case Right<L, R> right -> right(right.getRight());
        };
    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert die linke Seite eines {@code Either}.
     *     </p>
     * </div>
     *
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     *
     * @since 1.0.0
     */
    final class Left<L, R>
            implements Either<L, R> {

        private final Supplier<L> spool;

        Left(final Supplier<L> spool) {
            this.spool
                = spool;
        }

        /**
         * <div>
         *     <p>
         *         Vergleicht dieses {@code Left} mit einem anderen Objekt auf Gleichheit.
         *         Zwei {@code Left}-Instanzen sind gleich, wenn der enthaltene linke Wert gleich ist.
         *     </p>
         * </div>
         *
         * @param o das zu vergleichende Objekt
         * @return {@code true}, wenn das andere Objekt ein {@code Left} mit dem gleichen Wert ist; ansonsten {@code false}
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public boolean equals(Object o) {
            if (!(o instanceof Left<?, ?> left)) return false;

            return spool.get().equals(left.spool.get());
        }

        /**
         * <div>
         *     <p>
         *         Berechnet den Hash-Code f&uuml;r dieses {@code Left}, basierend auf dem Hash-Code des enthaltenen linken Wertes.
         *     </p>
         * </div>
         *
         * @return der Hash-Code dieses {@code Left}
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public int hashCode() {
            return spool.get().hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Gibt eine String-Darstellung des linken Wertes zur&uuml;ck.
         *     </p>
         * </div>
         *
         * @return die String-Darstellung des linken Wertes
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Left.class.getSimpleName() + "{", "}")
                .add(String.valueOf(Objects.requireNonNull(this.spool.get(), nullResult())))
                .toString();
        }

    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert die rechte Seite eines {@code Either}.
     *     </p>
     * </div>
     *
     * @param <L> der Typ des linken Wertes
     * @param <R> der Typ des rechten Wertes
     *
     * @since 1.0.0
     */
    final class Right<L, R>
            implements Either<L, R> {

       private final Supplier<R> spool;

        Right(final Supplier<R> spool) {
            this.spool
                = spool;
        }

        /**
         * <div>
         *     <p>
         *         Vergleicht dieses {@code Right} mit einem anderen Objekt auf Gleichheit.
         *         {@code Right}-Instanzen sind gleich, wenn der enthaltene rechte Wert gleich ist.
         *     </p>
         * </div>
         *
         * @param o das zu vergleichende Objekt
         * @return {@code true}, wenn das andere Objekt ein {@code Right} mit dem gleichen Wert ist; ansonsten {@code false}
         *
         * @since 1.0.0
         */
        @UnwindingOperation
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Right<?, ?> right)) return false;

            return spool.get().equals(right.spool.get());
        }

        /**
         * <div>
         *     <p>
         *         Berechnet den Hash-Code f&uuml;r dieses {@code Right}, basierend auf dem Hash-Code des enthaltenen rechten Wertes.
         *     </p>
         * </div>
         *
         * @return der Hash-Code dieses {@code Right}
         *
         * @since 1.0.0
         */
        @UnwindingOperation
        @Override
        public int hashCode() {
            return spool.get().hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Gibt eine String-Darstellung des rechten Wertes zur&uuml;ck.
         *     </p>
         * </div>
         *
         * @return die String-Darstellung des rechten Wertes
         *
         * @since 1.0.0
         */
        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Right.class.getSimpleName() + "{", "}")
                .add(String.valueOf(Objects.requireNonNull(this.spool.get(), nullResult())))
                .toString();
        }

    }

    /**
     * <div>
     *     <p>
     *         Gibt die Anzahl der Typ-Argumente zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return die Anzahl der Argumente
     *
     * @since 1.0.0
     */
    @Override
    default int arity() {
        return 1;
    }

}
