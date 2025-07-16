package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.H2Monadic;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *     <p>
 *         Eine State-Monade, die eine Berechnung mit einem inneren Zustand <i>S</i> und einem Ergebnis <i>A</i> repr&auml;sentiert.
 *     </p>
 * </div>
 *
 * @param <S> Typ des Zustands
 * @param <A> Typ des Werts
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public class Stateful<A, S>
        implements H2Monadic<Stateful.µ, A, S>,
                   Higher2<Stateful.µ, A, S> {

    public static final class µ implements WitnessType { private µ() {} }

    @SuppressWarnings("unchecked")
    public static <A, S> Stateful<A, S> narrow(final Higher2<? extends Stateful.µ, A, S> higher) {
        return (Stateful<A, S>) higher;
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein neues {@code Stateful}-Objekt mit der gegebenen Zustandsfunktion.
     *     </p>
     * </div>
     *
     * @param runState Die Funktion, die den Zustand verarbeitet und ein Ergebnis mit einem neuen Zustand liefert
     * @param <S> Typ des Zustands
     * @param <A> Typ des Ergebnisses
     * @return Ein neues {@code Stateful}-Objekt
     *
     * @since 1.0.0
     */
    public static <A, S> Stateful<A, S> stateful(final @NonNull Function<S, Tuple2<A, S>> runState) {
        Objects.requireNonNull(runState, nullValue("runState"));
        return new Stateful<>(runState);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine {@code Stateful}-Instanz mit einem festen Wert und unver&auml;ndertem Zustand.
     *     </p>
     * </div>
     *
     * @param value Der festgelegte Wert
     * @param <S> Typ des Zustands
     * @param <A> Typ des Werts
     * @return Ein {@code Stateful}-Objekt, das den gegebenen Wert zur&uuml;ckgibt, ohne den Zustand zu &auml;ndern
     *
     * @since 1.0.0
     */
    public static <A, S> Stateful<A, S> stateOf(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Stateful<>(state -> tuple2(value, state));
    }

    /**
     * <div>
     *     <p>
     *         Gibt den aktuellen Zustand als Ergebnis zur&uuml;ck, ohne ihn zu &auml;ndern.
     *     </p>
     * </div>
     *
     * @param <S> Typ des Zustands
     * @return Ein {@code Stateful}-Objekt, das den aktuellen Zustand als Ergebnis liefert
     *
     * @since 1.0.0
     */
    public static <S> Stateful<S, S> getState() {
        return new Stateful<>(state -> tuple2(state, state));
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein {@code Stateful}-Objekt, das den Zustand gem&auml;&szlig; einer &uuml;bergebenen Funktion modifiziert.
     *     </p>
     * </div>
     *
     * @param modifier Funktion, die den Zustand transformiert
     * @param <S> Typ des Zustands
     * @return Ein {@code Stateful}-Objekt, das den modifizierten Zustand zur&uuml;ckgibt
     *
     * @since 1.0.0
     */
    public static <S> Stateful<Nothing, S> modifyState(final @NonNull Function<? super S, ? extends S> modifier) {
        Objects.requireNonNull(modifier, nullValue("modifier"));
        return new Stateful<>(state -> tuple2(nothing, Objects.requireNonNull(modifier.apply(state), nullResult())));
    }

    /**
     * <div>
     *     <p>
     *         Setzt den Zustand auf einen festen Wert und gibt ihn zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param state Der neue Zustand
     * @param <S> Typ des Zustands
     * @return Ein {@code Stateful}-Objekt, das den neuen Zustand speichert und zurückgibt
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public static <S> Stateful<Nothing, S> putState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return new Stateful<>(_$ -> tuple2(nothing, state));
    }

    private final Function<S, Tuple2<A, S>> runState;

    private Stateful(final Function<S, Tuple2<A, S>> runState) {
        this.runState
            = runState;
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf das Ergebnis an, ohne den Zustand zu ver&auml;ndern.
     *     </p>
     * </div>
     *
     * @param transformation Funktion zur Transformation des Ergebnisses
     * @param <B> Typ des neuen Ergebnisses
     * @return Ein {@code Stateful}-Objekt mit dem transformierten Ergebnis
     *
     * @since 1.0.0
     */
    public @NonNull <B> Stateful<B, S> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Stateful<>(state -> {
            final Tuple2<A, S> resultAndState
                = this.runState.apply(state);
            final var result
                = resultAndState.get();
            return tuple2(Objects.requireNonNull(transformation.apply(result), nullResult()), state);
        });
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion, die in einem anderen {@code Stateful}-Objekt enthalten ist, auf das Ergebnis an.
     *     </p>
     * </div>
     *
     * @param transformation Ein {@code Stateful}-Objekt, das die Funktion zum Anwenden enth&auml;lt
     * @param <B> Typ des Ergebnisses nach der Anwendung der Funktion
     * @return Ein {@code Stateful}-Objekt mit dem transformierten Ergebnis
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Stateful<B, S> lift(final @NonNull Higher2<? extends µ, Function<A, B>, S> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Stateful<>(state -> this.runState(state).map(narrow(transformation).execValue(state)).with2(state));
    }

    /**
     * <div>
     *     <p>
     *         Verkettet eine Berechnung, die eine neue {@code Stateful}-Instanz zur&uuml;ckgibt, und verarbeitet das Ergebnis.
     *     </p>
     * </div>
     *
     * @param transformation Funktion, die das Ergebnis transformiert und eine neue {@code Stateful}-Instanz liefert
     * @param <B> Typ des neuen Ergebnisses
     * @return Ein {@code Stateful}-Objekt, das die verkn&uuml;pfte Berechnung repr&auml;sentiert
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Stateful<B, S> bind(final @NonNull Function<A, ? extends Higher2<? extends µ, B, S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Stateful<>(state -> narrow(this.map(transformation).runState(state).get1()).runState(state));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Zustandstransformation aus und gibt das Ergebnis und den neuen Zustand zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param state Der Anfangszustand
     * @return Ein {@code Tuple} mit dem Ergebnis und dem neuen Zustand
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Tuple2<A, S> runState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return Objects.requireNonNull(this.runState.apply(state), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt den Zustand mit einem gegebenen Tuple aus und gibt das Ergebnis zur&uuml;ck.
     *     </p>
     *     <p>
     *         Diese Methode wendet die `runState`-Funktion auf den Zustand des &uuml;bergebenen Tuples an
     *         und gibt das Ergebnis als neues Tuple zur&uuml;ck. Der erste Wert des zur&uuml;ckgegebenen Tuples
     *         ist das Ergebnis der Zustandstransformation, w&auml;hrend der zweite Wert der neue Zustand ist.</p>
     *     </p>
     * </div>
     *
     * @param Tuple2 Das Tuple, das den aktuellen Zustand und den Eingabewert enth&auml;lt.
     *              Es darf nicht null sein.
     * @return Ein neues Tuple, das das Ergebnis der Zustandstransformation und den neuen Zustand enth&auml;lt.
     *
     * @throws NullPointerException Wenn <code>tuple</code> oder das Ergebnis von `runState` null ist.
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Tuple2<A, S> runStateTuple(final @NonNull Tuple2<A, S> Tuple2) {
        Objects.requireNonNull(Tuple2, nullValue("tuple"));
        return Objects.requireNonNull(this.runState.apply(Tuple2.get2()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Berechnung aus und gibt nur das Ergebnis zur&uuml;ck, ohne den Zustand.
     *     </p>
     * </div>
     *
     * @param state Der Anfangszustand
     * @return Das Ergebnis der Berechnung
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public A execValue(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return this.runState(state).get();
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Berechnung aus und gibt nur den neuen Zustand zur&uuml;ck, ohne das Ergebnis.
     *     </p>
     * </div>
     *
     * @param state Der Anfangszustand
     * @return Der neue Zustand nach der Berechnung
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public S execState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return this.runState(state).get2();
    }

}
