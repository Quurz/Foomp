package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine monadische Schnittstelle für die Lazy-Bewertung von Werten.
 *     </p>
 * </div>
 *
 * @param <A> Typ des Werts, der in der Evaluation verwendet wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public sealed interface Eval<A>
        extends Unwindable<Eval<A>>,
                Transmogrifyable<Eval<A>>,
                Monadic<Eval.µ, A>,
                Value<A>,
                Higher1<Eval.µ, A>
        permits Eval.Now,
                Eval.Later,
                Eval.Always {

    /**
     * <div>
     *     <p>
     *         Witness-Type des <code>Eval</code>s
     *     </p>
     * </div>
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Wandelt einen breiten Higher1-Typ in einen Eval-Typ um.
     *     </p>
     * </div>
     *
     * @param wide der zu konvertierende Higher1-Typ
     * @param <A> der Typ des Werts
     * @return ein Eval-Objekt
     * @throws NullPointerException wenn <code>wide</code> null ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Eval<A> narrow(@NonNull final Higher1<? extends µ, A> wide) {
        return (Eval<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Entpackt einen verschachtelten Higher1-Typ in einen Eval-Typ.
     *     </p>
     * </div>
     *
     * @param wrapped der verschachtelte Higher1-Typ
     * @param <A> der Typ des Werts
     * @return ein Eval-Objekt
     * @throws NullPointerException wenn <code>wrapped</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Eval<A> unwrap(@NonNull final Higher1<? extends µ, ? extends Higher1<? extends µ, A>> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("toJoin"));
        final var narrowed
            = narrow(wrapped);
        return narrow(narrowed.get());
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein Eval-Objekt mit einem sofort verf&uuml;gbaren Wert.
     *     </p>
     * </div>
     *
     * @param value der sofort verf&uuml;gbare Wert
     * @param <A> der Typ des Werts
     * @return ein Eval-Objekt, das den angegebenen Wert enthält
     * @throws NullPointerException wenn <code>value</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalNow(@NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Now<>(value);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein Eval-Objekt, das einen Wert zur Laufzeit liefert.
     *     </p>
     * </div>
     *
     * @param value der Wert, der zur Laufzeit bereitgestellt wird
     * @param <A> der Typ des Werts
     * @return ein Eval-Objekt, das den zur Laufzeit angegebenen Wert enth&auml;lt
     * @throws NullPointerException wenn <code>value</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalLater(@NonNull final A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Later<>(() -> value);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt ein Eval-Objekt, das immer einen Wert zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param value der Wert, der immer bereitgestellt wird
     * @param <A> der Typ des Werts
     * @return ein Eval-Objekt, das immer den angegebenen Wert zur&uuml;ckgibt
     * @throws NullPointerException wenn <code>value</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Eval<A> evalAlways(@NonNull final A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Always<>(() -> value);
    }

    /**
     * <div>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob ein Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @return immer <code>true</code>, da Eval immer einen Wert hat
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den in Eval gespeicherten Wert an und gibt ein neues Eval-Objekt zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Funktion, die auf den gespeicherten Wert angewendet wird
     * @param <B> der Typ des neuen Werts
     * @return ein neues Eval-Objekt mit dem transformierten Wert
     * @throws NullPointerException wenn <code>fMap</code> null ist oder das Ergebnis null
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Eval<B> map(@NonNull final Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation);
        return switch (this) {
            case Now<A> now -> evalNow(Objects.requireNonNull(transformation.apply(now.get()), nullResult()));
            case Later<A> later -> new Later<>(() -> Objects.requireNonNull(transformation.apply(later.get()), nullResult()));
            case Always<A> always -> new Always<>(() -> Objects.requireNonNull(transformation.apply(always.get()), nullResult()));
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den in Eval gespeicherten Wert an und gibt ein neues Eval-Objekt zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Funktion, die auf den gespeicherten Wert angewendet wird
     * @param <B> der Typ des neuen Werts
     * @return ein neues Eval-Objekt mit dem transformierten Wert
     * @throws NullPointerException wenn <code>liftA</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Eval<B> lift(@NonNull final Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return this.map(narrow(transformation).get());
    }

    /**
     * <div>
     *     <p>
     *         Bindet eine Funktion an den in Eval gespeicherten Wert und gibt ein neues Eval-Objekt zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Funktion, die an den gespeicherten Wert gebunden wird
     * @param <B> der Typ des neuen Werts
     * @return ein neues Eval-Objekt mit dem transformierten Wert
     * @throws NullPointerException wenn <code>bindM</code> null ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default <B> Eval<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return switch (this) {
            case Eval.Now<A> now -> unwrap(this.map(transformation));
            case Eval.Later<A> later -> new Later<>(() -> narrow(transformation.apply(later.get())).get());
            case Eval.Always<A> always -> new Always<>(() -> narrow(transformation.apply(always.get())).get());
        };
    }

    /**
     * <div>
     *     <p>
     *         Spult alle Operationen ab und gibt den im Eval gespeicherten Wert und gibt ihn zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return ein neues Eval-Objekt mit dem ermittelten Wert
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Eval<A> unwind() {
       return switch (this) {
           case Now<A> now -> new Now<>(now.get());
           case Later<A> later -> new Later<>(later.supplier);
           case Always<A> always -> new Always<>(always.supplier);
       };
    }

    /**
     * <div>
     *     <p>
     *         Wendet einen Transmogrifier auf das Eval-Objekt an.
     *     </p>
     * </div>
     *
     * @param transmogrifier die Funktion, die auf das Eval-Objekt angewendet wird
     * @param <T> der Typ des Ergebnisses
     * @return das Ergebnis der Transmogrification
     * @throws NullPointerException wenn <code>transmogrifier</code> null ist oder das Ergebnis null
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Eval<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transformer"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert einen sofort verf&uuml;gbaren Wert in Eval.
     *     </p>
     * </div>
     *
     * @param <A> der Typ des Werts
     *
     * @since 1.0.0
     */
    final class Now<A>
            implements Eval<A> {

        private final A value;

        private Now(final A value) {
            this.value
                = value;
        }

        @Override
        @NonNull
        public A get() {
            return this.value;
        }

    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert einen verz&ouml;gerten Wert in Eval, der zur Laufzeit geliefert wird.
     *     </p>
     * </div>
     *
     * @param <A> der Typ des Werts
     *
     * @since 1.0.0
     */
    final class Later<A>
            implements Eval<A> {

        private final Supplier<A> supplier;
        private A memo;

        private Later(final Supplier<A> supplier) {
            this.supplier
                = supplier;
        }

        @Override
        @NonNull
        public A get() {
            if (this.memo == null) {
                this.memo
                    = Objects.requireNonNull(this.supplier.get(), nullSupplied());
            }
            return this.memo;
        }

    }

    /**
     * <div>
     *     <p>
     *         Repr&auml;sentiert einen Wert in Eval, der immer ermittelt wird.
     *     </p>
     * </div>
     *
     * @param <A> der Typ des Werts
     *
     * @since 1.0.0
     */
    final class Always<A>
            implements Eval<A> {

        private final Supplier<A> supplier;

        private Always(final Supplier<A> supplier) {
            this.supplier
                = supplier;
        }

        @Override
        @NonNull
        public A get() {
            return Objects.requireNonNull(this.supplier.get(), nullSupplied());
        }

    }

}
