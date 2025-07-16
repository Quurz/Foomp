package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.H2Monadic;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Repräsentiert eine Continuation, die eine Funktion speichert, die einen Wert vom Typ {@code A}
 *         entgegennimmt und ein Ergebnis vom Typ {@code R} zurückgibt. Diese Klasse unterstützt eine monadische
 *         Struktur, die es ermöglicht, Funktionen sequenziell zu verknüpfen, wobei der Zustand über Continuations
 *         aufrechterhalten wird, ohne den Kontrollfluss direkt zu verändern.
 *     </p>
 *     <p>
 *         Die {@code Continuation}-Klasse wird oft in funktionalen Programmiersprachen verwendet, um eine
 *         Fortsetzung der Ausführung eines Programms zu modellieren. Sie unterstützt Methoden wie {@code map},
 *         {@code bind}, {@code lift}, und {@code apply}, die es ermöglichen, mit Werten in der Continuation
 *         auf eine funktionale Art zu arbeiten.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des Werts, der in der {@code Continuation} gespeichert ist.
 * @param <R> Der Rückgabetyp der {@code Continuation}.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public class Continuation<A, R>
        implements H2Monadic<Continuation.µ, A, R>,
                   Function<Function<A, R>, R>,
        Higher2<Continuation.µ, A, R> {

    /**
     * <div>
     *     <p>
     *         Ein Marker-Typ (Witness Type) für die {@code Continuation}-Monade.
     *     </p>
     *     <p>
     *         Dieser Typ dient als Identifikator für die {@code Continuation}-Monade innerhalb des
     *         Higher-Kinded Type Systems. Er wird in generischen Methoden und Schnittstellen verwendet,
     *         um den konkreten Typ zu kennzeichnen.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() { } }

    /**
     * <div>
     *     <p>
     *         Wandelt eine {@link Higher2}-Instanz in eine {@code Continuation}-Instanz um.
     *     </p>
     *     <p>
     *         Diese Methode ist hilfreich, um einen allgemeiner typisierten Wert in eine konkrete
     *         {@code Continuation}-Instanz zu transformieren. Dies ermöglicht die Verwendung der Continuation
     *         innerhalb eines Higher-Kinded Type-Systems.
     *     </p>
     * </div>
     *
     * @param higher Ein {@link Higher2}-Wert, der in eine {@code Continuation} umgewandelt werden soll.
     *             Darf nicht {@code null} sein.
     * @param <A>  Der Typ des Werts, der in der {@code Continuation} gespeichert ist.
     * @param <R>  Der Rückgabetyp der {@code Continuation}.
     * @return Eine {@code Continuation}-Instanz, die aus dem gegebenen {@code Higher2}-Wert erstellt wurde.
     * @throws NullPointerException Wenn {@code wide} {@code null} ist.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A, R> Continuation<A, R> narrow(final @NonNull Higher2<? extends µ, A, R> higher) {
        return (Continuation<A, R>) higher;
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code Continuation}-Instanz mit der gegebenen {@code runCont}-Funktion.
     *     </p>
     *     <p>
     *         Die {@code runCont}-Funktion repräsentiert die Ausführung der Continuation, wobei sie
     *         eine Funktion erwartet, die den erzeugten Wert {@code A} konsumiert und ein Ergebnis {@code R} zurückliefert.
     *     </p>
     * </div>
     *
     * @param runCont Eine Funktion, die die Continuation ausführt. Sie nimmt eine Funktion entgegen,
     *                die den Wert {@code A} verarbeitet und ein Ergebnis {@code R} liefert.
     *                Darf nicht {@code null} sein.
     * @param <A>     Der Typ des Werts, der in der Continuation gespeichert ist.
     * @param <R>     Der Rückgabetyp der Continuation.
     * @return Eine neue {@code Continuation}-Instanz, die auf der gegebenen {@code runCont}-Funktion basiert.
     * @throws NullPointerException Wenn {@code runCont} {@code null} ist.
     *
     * @since 1.0.0
     */
    public static <R, A> Continuation<A, R> continuation(final @NonNull Function<Function<A, R>, R> runCont) {
        Objects.requireNonNull(runCont, nullValue("runCont"));
        return new Continuation<>(f -> Objects.requireNonNull(runCont.apply(f), nullResultFrom("runCont")));
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code Continuation}-Instanz, die einen festen Wert {@code value} enthält.
     *     </p>
     *     <p>
     *         Diese Methode dient dazu, eine Continuation zu erstellen, die einen gegebenen Wert
     *         direkt zurückgibt, ohne weitere Berechnungen durchzuführen.
     *     </p>
     * </div>
     *
     * @param value Der Wert, der in der Continuation gespeichert werden soll. Darf nicht {@code null} sein.
     * @param <A>   Der Typ des Werts, der in der Continuation gespeichert ist.
     * @param <R>   Der Rückgabetyp der Continuation (kann beliebig sein, da der Wert direkt zurückgegeben wird).
     * @return Eine neue {@code Continuation}-Instanz, die den gegebenen Wert enthält.
     * @throws NullPointerException Wenn {@code value} {@code null} ist.
     *
     * @since 1.0.0
     */
    public static <R, A> Continuation<A, R> pureContinuation(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Continuation<>(f -> Objects.requireNonNull(f.apply(value), nullResultFrom("runCont")));
    }

    private final Function<Function<A, R>, R> runCont;

    private Continuation(final @NonNull Function<Function<A, R>, R> runCont) {
        this.runCont
            = runCont;
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine gegebene Funktion auf das Ergebnis der Continuation an und gibt eine neue Continuation zurück.
     *     </p>
     *     <p>
     *         Diese Methode ist analog zur `map`-Operation in funktionalen Programmiersprachen.
     *         Sie ermöglicht es, den Wert, der von der Continuation erzeugt wird, zu transformieren, ohne die
     *         grundlegende Struktur der Continuation zu verändern.
     *     </p>
     *     <p>Beispiel:</p>
     *     <pre>{@code
     *         Continuation<Integer, String> cont = Continuation.continuation(k -> k.apply(42));
     *         Continuation<String, String> mappedCont = cont.map(x -> "The value is: " + x);
     *         String result = mappedCont.apply(x -> x);
     *     }</pre>
     * </div>
     *
     * @param <B> Der Typ des neuen Ergebnisses nach Anwendung der Funktion.
     * @param transformation  Eine Funktion, die das Ergebnis von Typ {@code A} in ein Ergebnis vom Typ {@code B} transformiert.
     * @return Eine neue Continuation, die das transformierte Ergebnis enthält.
     * @throws NullPointerException Wenn {@code transformation} oder das von {@code transformation} zurückgegebene Ergebnis {@code null} ist.
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Continuation<>(
            f -> this.runCont.apply(
                f.compose(a -> {
                    // Null-Check auf a, um unerwartete Null-Werte abzufangen
                    Objects.requireNonNull(a, nullValue("a"));
                    return Objects.requireNonNull(transformation.apply(a), nullResultFrom("transformation"));
                })
            )
        );
    }

    /**
     * <div>
     *     <p>
     *         Hebt eine Continuation, die eine Funktion enthält, in diese Continuation hoch.
     *     </p>
     *     <p>
     *         Die gegebene Continuation {@code transformation} enthält eine Funktion, die auf das Ergebnis dieser Continuation
     *         angewendet wird. Das Ergebnis der Anwendung wird in einer neuen Continuation zurückgegeben.
     *     </p>
     * </div>
     *
     * @param <B> Der Typ des neuen Ergebnisses nach Anwendung der Funktion.
     * @param transformation  Eine Continuation, die eine Funktion {@code Function<? super A, ? extends B>} enthält.
     * @return Eine neue Continuation, die das Ergebnis der Anwendung der Funktion auf das Ergebnis dieser Continuation enthält.
     * @throws NullPointerException Wenn {@code transformation} {@code null} ist oder die darin enthaltene Funktion
     *                              ein {@code null}-Ergebnis liefert.
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> lift(@NonNull Higher2<? extends µ, Function<A, B>, R> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final var continuation
                = narrow(transformation);
        return new Continuation<>(f -> continuation.apply(t -> this.map(t).apply(f)));
    }

    /**
     * <div>
     *     <p>
     *         Verkettet die aktuelle Continuation mit einer neuen Continuation, die aus der gegebenen
     *         Funktion {@code transformation} erzeugt wird.
     *     </p>
     *     <p>
     *         Diese Methode führt die aktuelle Continuation aus, um einen Wert {@code a} zu erzeugen,
     *         und übergibt diesen Wert an die Funktion {@code transformation}. Die Funktion liefert eine neue
     *         Continuation zurück, die mit der übergebenen {@code cont}-Funktion weitergeführt wird.
     *     </p>
     * </div>
     *
     * @param transformation Eine Funktion, die den aktuellen Wert {@code a} in eine neue Continuation
     *              vom Typ {@code Continuation<B, R>} transformiert. Sie darf nicht {@code null} sein.
     * @param <B>   Der Typ des Wertes der neu erzeugten Continuation.
     * @return Eine neue Continuation, die die aktuelle Continuation ausführt und mit der neuen
     *         Continuation aus {@code transformation} weitergeführt wird.
     * @throws NullPointerException Wenn {@code transformation} {@code null} ist oder die von {@code bindM}
     *                              erzeugte Continuation {@code null} ist.
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> bind(@NonNull Function<A, ? extends Higher2<? extends µ, B, R>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Continuation<>(f -> this.runCont.apply(a -> narrow(transformation.apply(a)).map(Function.identity()).apply(f)));
    }

    /**
     * <div>
     *     <p>
     *         Wendet die gegebene Funktion auf die Continuation an und führt sie aus,
     *         um ein Ergebnis vom Typ {@code R} zu liefern.
     *     </p>
     *     <p>
     *         Diese Methode führt die gespeicherte Continuation aus, indem sie die
     *         bereitgestellte Funktion {@code computation} auf den inneren Wert anwendet,
     *         der durch die Continuation zur Verfügung gestellt wird.
     *      </p>
     * </div>
     *
     * @param computation Die Funktion, die auf den Wert der Continuation angewendet werden soll.
     * Sie darf nicht {@code null} sein.
     * @return Das Ergebnis der Ausführung der Continuation, nachdem die Funktion angewendet wurde.
     * @throws NullPointerException Wenn {@code computation} {@code null} ist oder die Funktion
     * ein {@code null}-Ergebnis liefert.
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull R apply(final @NonNull Function<A, R> computation) {
        Objects.requireNonNull(computation, nullValue("computation"));
        return Objects.requireNonNull(this.runCont.apply(computation), nullResultFrom("computation"));
    }

    /**
     * <div>
     *     <p>
     *         Führt diese Continuation aus und ignoriert ihr Ergebnis,
     *         um anschließend die übergebene Continuation weiterzuführen.
     *     </p>
     * </div>
     *
     * @param continuation die Continuation, die nach dieser ausgeführt wird
     * @return eine neue Continuation, die diese Continuation ausführt
     *         und danach die übergebene {@code continuation} weiterführt
     * @throws NullPointerException wenn {@code continuation} {@code null} ist
     */
    @SuppressWarnings("unused")
    public <B> Continuation<B, R> then(final @NonNull Continuation<B, R> continuation) {
        Objects.requireNonNull(continuation, nullValue("continuation"));
        return this.bind(ignored -> continuation);
    }

    public static <R, A, B> Continuation<B, R> callCurrentCont(final @NonNull Function<Function<A, Continuation<B, R>>, Continuation<A, R>> computation) {
        Objects.requireNonNull(computation, nullValue("computation"));
        //noinspection unchecked
        return (Continuation<B, R>) new Continuation<>((final Function<Object, R> k) -> computation.apply(a -> new Continuation<>(ignored -> k.apply(a))).apply((Function<A, R>) k));
    }

}
