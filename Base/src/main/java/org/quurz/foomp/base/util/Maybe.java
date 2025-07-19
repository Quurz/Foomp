package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.requiresNonNullResult2;

/**
 * <div>
 *     <p>
 *         Entweder wir haben was, oder wir haben nix. Das aber immerhin typisiert.
 *     </p>
 * </div>
 *
 * @param <A> Typ des Inhalts
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Maybe<A>
        extends Monadic<Maybe.µ, A>,
                Copyable<Maybe<A>>,
                Unwindable<Maybe<A>>,
                Transmogrifyable<Maybe<A>>,
                Value<A>,
                Higher1<Maybe.µ, A>
        permits Maybe.Some,
                Maybe.None {

    /**
     * <div>
     *     <p>
     *         Der Witness-Typ für <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Fixiert das übergebene <code>Higher1</code> zu einem <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @param wide Das zu fixierende <code>Higher1</code>
     * @param <A> Typ des Inhalts
     * @return Das <code>Maybe</code>
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Maybe<A> narrow(final @NonNull  Higher1<? extends µ, A> wide) {
        return (Maybe<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues, einen Wert enthaltendes <code>Maybe.Some</code>
     *     </p>
     * </div>
     *
     * @param value Der Inhalt des <code>Maybe</code>
     * @param <A> Typ des Inhalts
     * @return Das neue <code>Maybe</code>
     *
     * @since 1.0.0
     */
    static <A> Maybe<A> some(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Some<>(() -> value);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues Maybe, das, in Abh&auml;ngikeit vom &uuml;bergebenen Wert, entweder leer oder gef&uuml;llt sein kann.
     *     </p>
     * </div>
     *
     * @param value Der eventuelle Inhalt des <code>Maybe</code> - Handelt es sich um einen <code>null</code>-Wert, ist das <code>Maybe</code> - &Uuml;berraschung! - leer.
     * @return Das neue <code>Maybe</code>
     * @param <A> Typ des Inhalts
     *
     * @since 1.0.0
     */
    static <A> Maybe<A> maybeOfNullable(@Nullable final A value) {
        return value != null
            ? some(value)
            : none();
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues, leeres <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @return Das neue <code>Maybe</code>
     * @param <A> Typ des Inhalts
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Maybe<A> none() {
        return (Maybe<A>) None.NONE;
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues <code>Maybe</code> aus dem &uuml;bergebenen <code>Optional</code>
     *     </p>
     * </div>
     *
     * @param optional Das <code>Optional</code> - Enth&auml;lt das <code>Optional</code> keinen Wert, ist auch das neue <code>Maybe</code> leer.
     * @return Das neue <code>Maybe</code>
     * @param <A> Typ des Inhalts
     *
     * @since 1.0.0
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <A> Maybe<A> maybeFrom(@NonNull Optional<A> optional) {
        Objects.requireNonNull(optional, nullValue("optional"));
        return optional.map(Maybe::some).orElseGet(Maybe::none);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return this.isSome();
    }

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob dieses <code>Maybe</code> einen Wert enth&auml;lt oder nicht
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    default boolean isSome() {
        return (this instanceof Maybe.Some<A>);
    }

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob dieses <code>Maybe</code> einen Wert enth&auml;lt oder nicht
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls kein Wert vorhanden ist; <code>false</code>, falls doch
     *
     * @since 1.0.0
     */
    default boolean isNone() {
        return !this.isSome();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @Override
    @UnwindingOperation
    @NonNull
    default A get()
            throws NoSuchElementException {
        return switch (this) {
            case Some<A> some -> Objects.requireNonNull(some.spool.get(), nullSupplied());
            case None<A> _$ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *     <p>
     *         Falls dieses <code>Maybe</code> keinen Wert enthalten sollte, wird der Wert aus dem &uuml;bergebenen <code>Supplier</code>
     *         genommen. Falls ein Wert vorhanden sein sollte, wird dieser zur&uuml;ck gegeben.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier</code>
     * @return Entweder der Inhalt des <code>Maybe</code> oder, falls kein Wert vorhanden ist, der vom <code>Supplier</code> gelieferte Wert
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @UnwindingOperation
    @NonNull
    default A getOrElse(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return switch (this) {
            case Some<A> some -> some.get();
            case None<A> _$ -> Objects.requireNonNull(supplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *     <p>
     *         Enth&auml;lt dieses <Code>Maybe</Code> einen Wert, wird dieser zur&uuml;ck gegeben. Ansonsten wird die vom
     *         <code>Supplier</code> gelieferte Exception geworfen
     *     </p>
     * </div>
     *
     * @param exceptionSupplier Der <code>Supplier</code>
     * @return Der Inhalt dieses <code>Maybe</code>, falls vorhanden
     * @param <E> Typ des <code>Throwable</code>s
     * @throws E Falls kein Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"SwitchLabeledRuleCanBeCodeBlock", "unused"})
    @UnwindingOperation
    @NonNull
    default <E extends Throwable> A getOrThrow(final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));
        return switch (this) {
            case Some<A> some -> some.get();
            case None<A> _$ -> throw Objects.requireNonNull(exceptionSupplier.get(), nullSupplied());
        };
    }

    /**
     * <div>
     *     <p>
     *         Enth&auml;lt dieses <code>Maybe</code> einen Wert, wird dieser an den &uuml;bergebenen <code>Consumer</code> weitergegeben
     *     </p>
     * </div>
     *
     * @param consumer Der <code>Consumer</code>
     * @return Diese <code>Maybe</code>-Instanz
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Maybe<A> ifSome(final @NonNull Consumer<A> consumer) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        if (this instanceof Maybe.Some<A> some) {
            consumer.accept(some.get());
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Enth&auml;lt dieses <code>Maybe</code> einen Wert, wird dieser an den &uuml;bergebenen <code>Consumer</code> weitergegeben.
     *         Falls nicht, wird das <code>Runnable</code> ausgef&uuml;hrt.
     *     </p>
     * </div>
     *
     * @return Diese <code>Maybe</code>-Instanz
     * @param consumer Der <code>Consumer</code>
     * @param orElse Das <code>Runnable</code>
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull
    default Maybe<A> ifPresentOrElse(final @NonNull Consumer<A> consumer,
                                     final @NonNull Runnable orElse) {
        Objects.requireNonNull(consumer, nullValue("consumer"));
        Objects.requireNonNull(orElse, nullValue("orElse"));
        if (this.isSome()) {
            consumer.accept(this.get());
        } else {
            orElse.run();
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Nimmt den inneren Wert dieses <code>Maybe</code>-Objekts, wendet die &uuml;bergebene Funktion auf ihn an und
     *         verpackt das Ergebnis in ein neues <code>Maybe</code>-Objekt gleichen Typs.
     *     </p>
     * </div>
     *
     * @param transformation Die anzuwendende Funktion
     * @param <B> Der Typ des inneren Werts des neuen <code>Maybe</code>-Objekts
     * @return Das neue <code>Maybe</code>-Objekt
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unchecked"})
    @Override
    @NonNull
    default <B> Maybe<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return switch (this) {
            case Some<A> some -> (Maybe<B>) new Some<>(() -> {
                final var v
                    = Objects.requireNonNull(some.spool.get(), nullSupplied());
                return Objects.requireNonNull(transformation.apply(v), nullResult());
            });
            case None<A> none -> (Maybe<B>) none;
        };
    }

    /**
     * <div>
     *     <p>
     *         Entpackt die im übergebene <code>liftA</code>-Argument - Ein anderes <code>Maybe</code> - enthaltene
     *         Funktion, wendet sie auf den Inhalt dieses <code>Maybe</code>-Objekts an und verpackt as Ergebnis in ein
     *         neues <code>Maybe</code>-Objekt gleichen Typs.
     *     </p>
     * </div>
     *
     * @param transformation Das die anzuwendende Funktion enthaltende <code>Maybe</code>-Objekt
     * @param <B> Der 'innere' Typ des neuen <code>Maybe</code>-Objekts
     * @return Ein neues <code>Maybe</code>-Objekt
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"unused"})
    @Override
    @NonNull
    default <B> Maybe<B> lift(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation);
        final var narrowed
            = narrow(transformation);
        return narrowed.isSome()
            ? this.map(narrowed.get())
            : none();
    }

    /**
     * <div>
     *     <p>
     *         Nimmt den Wert dieses <code>Maybe</code>-Objekts, wendet die übergebene Funktion darauf an
     *         und liefert das neue <code>Maybe</code>-Objekt zurück.
     *     </p>
     * </div>
     *
     * @param transformation Die anzuwendende Funktion
     * @param <B> Der Typ des 'inneren' Werts des neuen <code>Maybe</code>-Objekts
     * @return Das neue <code>Maybe</code>-Objekt
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Maybe<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return this.map(a -> narrow(transformation.apply(a)).get());
    }

    /**
     * <div>
     *     <p>
     *         Wandelt diese <code>Maybe</code>-Instanz in ein <code>Optional</code> um:<br />
     *         <code>Maybe.None</code> &rarr; leeres <code>Optional</code><br />
     *         <code>Maybe.Some</code> &rarr; gef&uuml;lltes <code>Optional</code>
     *     </p>
     * </div>
     *
     * @return Das <code>Optional</code>
     *
     * @since 1.0.0
     */
    default Optional<A> toOptional() {
        return this.map(Optional::of).getOrElse(Optional::empty);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Kopie dieses <code>Maybe</code>s
     *     </p>
     * </div>
     *
     * @return Die Kopie
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Maybe<A> copy() {
        return switch (this) {
            case Some<A> some -> new Some<>(some.spool);
            case None<A> none -> none;
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Umwandlungs-Funktion auf dieses <code>Maybe</code> an und liefert das Ergebnis zurück
     *     </p>
     * </div>
     *
     * @param transmogrifier Die Umwandlungs-Funktion
     * @param <T> Der Typ, den die Umwandlung liefert
     * @return Ein Objekt vom Typ <code>T</code>
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Maybe<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Spult alle Operationen auf diesem <code>Maybe</code> ab und liefert ein neues <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @return Das neue <code>Maybe</code>
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Maybe<A> unwind() {
        return switch (this) {
            case Some<A> some -> some(some.get());
            case None<A> none -> none;
        };
    }

    /**
     * <div>
     *     <p>
     *         Filtert den Wert dieses <code>Maybe</code> anhand des übergebenen Prädikats.
     *         Falls das Prädikat <code>false</code> zurückgibt oder dieses <code>Maybe</code> leer ist,
     *         wird ein leeres <code>Maybe</code> zurückgegeben.
     *     </p>
     * </div>
     *
     * @param predicate Das Prädikat
     * @return Ein neues <code>Maybe</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Maybe<A> filter(final @NonNull Predicate<? super A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return this.bind(a -> predicate.test(a) ? some(a) : none());
    }

    /**
     * <div>
     *     <p>
     *         Kombiniert die Werte dieses und des übergebenen <code>Maybe</code>s
     *         mithilfe der übergebenen Funktion. Falls eines der <code>Maybe</code>s
     *         leer ist, ist auch das Ergebnis leer.
     *     </p>
     * </div>
     *
     * @param other Das andere <code>Maybe</code>
     * @param zipper Die Kombinationsfunktion
     * @param <B> Typ des Werts des anderen <code>Maybe</code>s
     * @param <C> Ergebnistyp
     * @return Ein neues <code>Maybe</code> mit dem kombinierten Wert
     *
     * @since 1.0.0
     */
    @NonNull
    default <B, C> Maybe<C> zip(final @NonNull Maybe<B> other,
                                final @NonNull BiFunction<A, B, C> zipper) {
        Objects.requireNonNull(other, nullValue("other"));
        Objects.requireNonNull(zipper, nullValue("zipper"));
        return this.bind(a -> other.map(b -> requiresNonNullResult2(zipper, "zipper").apply(a, b)));
    }


    /**
     * <div>
     *     <p>
     *         Das gef&uuml;llte <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @param <A> Typ des Inhalts
     *
     * @since 1.0.0
     */
    final class Some<A>
            implements Maybe<A> {

        private final Supplier<A> spool;

        private Some(final Supplier<A> spool) {
            this.spool
                = spool;
        }

        @Override
        @UnwindingOperation
        public String toString() {
            return new StringJoiner(", ", Some.class.getSimpleName() + "[", "]")
                    .add("value=" + this.spool.get())
                    .toString();
        }
    }

    /**
     * <div>
     *     <p>
     *         Das leere <code>Maybe</code>
     *     </p>
     * </div>
     *
     * @param <A> Der Typ des <code>Maybe.None</code>
     *
     * @since 1.0.0
     */
    final class None<A>
            implements Maybe<A> {

        private static final Maybe<Void> NONE
            = new None<>();

        private None() {}

        @Override
        public String toString() {
            return new StringJoiner(", ", None.class.getSimpleName() + "[", "]")
                    .toString();
        }

    }

}
