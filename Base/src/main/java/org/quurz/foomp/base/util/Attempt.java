package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Triable;
import org.quurz.foomp.base.types.UnsafeMonadic;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

/**
 * <div>
 *   <p>
 *     Eine Implementierung einer Fehler-Monade, die verzögerte Auswertung von Berechnungen
 *     und die sichere Handhabung von Ausnahmen ermöglicht.
 *   </p>
 *   <p>
 *     Die Klasse {@code Attempt<A>} speichert entweder einen erfolgreichen Wert des Typs {@code A}
 *     oder eine Ausnahme, die während der Berechnung aufgetreten ist. Sie bietet Methoden zur
 *     Transformation, Fehlerbehandlung und sicheren Verarbeitung der enthaltenen Werte.
 *   </p>
 * </div>
 *
 * @param <A> der Typ des enthaltenen Werts
 * @since 1.0.0
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Attempt<A>
        implements Monadic<Attempt.µ, A>,
                   UnsafeMonadic<Attempt.µ, A>,
                   Unwindable<Attempt<A>>,
                   Triable<A>,
                   Higher1<Attempt.µ, A> {

    /**
     * Ein Marker-Typ (Witness), der {@code Attempt} innerhalb der Typ-Hierarchie repräsentiert.
     * Dieser Typ wird verwendet, um Higher-Kinded Types in Java zu simulieren.
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * Wandelt eine Instanz von {@link Higher1} in eine konkrete {@code Attempt}-Instanz um.
     *
     * @param wide das Objekt, das in {@code Attempt} umgewandelt werden soll
     * @param <A> der Typ des enthaltenen Werts
     * @return eine {@code Attempt}-Instanz
     * @throws NullPointerException falls {@code wide} null ist
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Attempt<A> narrow(final @NonNull  Higher1<? extends Attempt.µ, A> wide) {
        return (Attempt<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * Erzeugt eine erfolgreiche {@code Attempt}-Instanz mit dem gegebenen Wert.
     *
     * @param value der Wert, der gespeichert werden soll
     * @param <A> der Typ des enthaltenen Werts
     * @return eine erfolgreiche {@code Attempt}-Instanz
     * @throws NullPointerException falls {@code value} null ist
     * @since 1.0.0
     */
    public static <A> Attempt<A> attempt(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Attempt<>(() -> success(value));
    }

    private final Supplier<Result<A>> spool;

    private Attempt(final Supplier<Result<A>> spool) {
        this.spool
            = spool;
    }

    /**
     * <div>
     *     <p>
     *         Führt die gekapselte Berechnung aus und liefert das Ergebnis.
     *     </p>
     *     <p>
     *         Diese Methode "entrollt" die lazy gespeicherte Berechnung und gibt das
     *         Ergebnis in Form eines {@link Result} zurück. Sie kann mehrfach aufgerufen
     *         werden.
     *     </p>
     * </div>
     *
     * @return das Ergebnis der Berechnung als {@link Result}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public Result<A> tryIt() {
        return this.spool.get();
    }

    /**
     * Führt eine Wiederherstellungsaktion aus, falls die Berechnung fehlschlägt, und gibt
     * ein neues {@code Attempt} mit dem Wiederherstellungswert zurück.
     *
     * @param recover ein Supplier, der den Wiederherstellungswert bereitstellt
     * @return ein neues {@code Attempt} mit dem ursprünglichen oder dem Wiederherstellungswert
     * @throws NullPointerException falls {@code recover} null ist
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public Attempt<A> onFailureRecover(final @NonNull Supplier<A> recover) {
        Objects.requireNonNull(recover, nullValue("recover"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                    case Result.Success<A> success-> success;
                    case Result.Failure<A> failure -> {
                        Result<A> result;
                        try {
                            final var value
                                = Objects.requireNonNull(recover.get(), nullSuppliedFrom("recover"));
                            result
                                = success(value);
                        } catch (final NullPointerException nullPointerException) {
                            nullPointerException.addSuppressed(failure.getException());
                            result
                                = failure(nullPointerException);
                        }
                        yield result;
                    }
            }
        );
    }

    /**
     * Führt eine Wiederherstellung durch, falls eine Ausnahme gespeichert ist.
     * Die Wiederherstellung erfolgt mithilfe einer Funktion, die die Ausnahme verarbeitet
     * und einen neuen Wert liefert.
     *
     * @param recover eine Funktion, die die Ausnahme verarbeitet und einen Wiederherstellungswert liefert
     * @return eine neue {@code Attempt}-Instanz mit dem ursprünglichen oder wiederhergestellten Wert
     * @throws NullPointerException falls die Funktion {@code null} ist oder {@code null} liefert
     * @since 1.0.0
     */
    public Attempt<A> onFailureRecover(final @NonNull Function<? super Exception, ? extends A> recover) {
        Objects.requireNonNull(recover, nullValue("recover"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                case Result.Success<A> success -> success;
                case Result.Failure<A> failure -> {
                    Result<A> result;
                    try {
                        final A value
                            = Objects.requireNonNull(recover.apply(failure.getException()), nullResultFrom("recover"));
                        result
                            = success(value);
                    } catch (final Exception exception) {
                        result
                            = failure(exception);
                    }
                    yield result;
                }
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Versucht, den gespeicherten Wert zu extrahieren. Falls die Berechnung jedoch fehlgeschlagen ist,
     *         wird die gespeicherte Ausnahme geworfen.
     *     </p>
     * </div>
     *
     * @return diese {@code Attempt}-Instanz, falls sie erfolgreich ist
     * @throws Exception die gespeicherte Ausnahme, falls die Berechnung fehlgeschlagen ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    public Attempt<A> onFailureThrow()
            throws Exception {
        return switch (this.spool.get()) {
            case Result.Success<A> _$ -> this;
            case Result.Failure<A> failure -> throw failure.getException();
        };
    }

    /**
     * <div>
     *     <p>
     *         Registriert eine Aktion, die ausgeführt wird, falls dieses {@code Attempt} fehlschlägt.
     *     </p>
     *     <p>
     *         Diese Methode ist <em>lazy</em>: Der übergebene {@link Consumer} wird erst beim
     *         ersten Zugriff auf das Ergebnis (z.&nbsp;B. durch {@code isSuccess()}, {@code get()}
     *         oder {@code toString()}) aufgerufen – und nur, wenn das Ergebnis tatsächlich
     *         oder {@code toString()}) aufgerufen – und nur, wenn das Ergebnis tatsächlich
     *     </p>
     *     <p>
     *         Die Methode verändert das Ergebnis nicht, sondern gibt ein neues {@code Attempt}
     *         zurück, das bei Auswertung die gegebene Aktion im Fehlerfall "mitliest".
     *     </p>
     * </div>
     *
     * @param failureConsumer eine Aktion, die im Fehlerfall den {@link Exception}-Wert entgegennimmt
     * @return ein {@code Attempt}, das im Fehlerfall die Aktion beim Auswerten ausführt
     * @throws NullPointerException falls {@code failureConsumer} {@code null} ist
     */
    public Attempt<A> peekFailureLazy(final @NonNull Consumer<? super Exception> failureConsumer) {
        Objects.requireNonNull(failureConsumer, nullValue("peek"));
        return new Attempt<>(
                () -> switch (this.spool.get()) {
                    case Result.Success<A> success -> success;
                    case Result.Failure<A> failure -> {
                        failureConsumer.accept(failure.getException());
                        yield failure;
                    }
                }
        );
    }

    /**
     * <div>
     *     <p>
     *         Führt eine Aktion sofort aus, falls das gespeicherte Ergebnis eine Ausnahme enthält.
     *     </p>
     *     <p>
     *         Diese Methode ist <em>eager</em>: Der übergebene {@link Consumer} wird sofort
     *         bei Aufruf dieser Methode ausgeführt – aber nur, wenn ein Fehler vorliegt.</p>
     *     </p>
     *     <p>
     *         Die Methode gibt dieselbe {@code Attempt}-Instanz zurück, verändert deren
     *         Zustand jedoch nicht.
     *     </p>
     * </div>
     *
     * @param failureConsumer eine Aktion, die mit der gespeicherten {@link Exception} aufgerufen wird
     * @return dieselbe {@code Attempt}-Instanz
     * @throws NullPointerException falls {@code failureConsumer} {@code null} ist
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Attempt<A> peekFailureEager(final @NonNull Consumer<? super Exception> failureConsumer) {
        Objects.requireNonNull(failureConsumer, nullValue("failureConsumer"));
        final var result
            = this.spool.get();
        if (result.isFailure()) {
            failureConsumer.accept(result.getException());
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine Funktion auf den gespeicherten Wert an und gibt ein neues {@code Attempt} mit
     *         dem Ergebnis der Funktion zurück. Tritt während der Anwendung der Funktion eine Ausnahme auf,
     *         wird diese in der neuen Instanz gespeichert.
     *     </p>
     * </div>
     *
     * @param transformation eine Funktion zur Transformation des Werts
     * @param <B> der Typ des Ergebnisses der Funktion
     * @return ein neues {@code Attempt} mit dem transformierten Wert oder einer gespeicherten Ausnahme
     * @throws NullPointerException falls die Funktion {@code null} ist oder {@code null} liefert
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <B> Attempt<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                    case Result.Success<A> success -> {
                        try {
                            yield success(Objects.requireNonNull(transformation.apply(success.get()), nullResultFrom("fMap")));
                        } catch (final Exception exception) {
                            yield (Result<B>) failure(exception);
                        }
                    }
                    case Result.Failure<A> failure -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Führt eine Transformation auf dem gespeicherten Wert durch, wobei eine {@link Applicable}-Instanz
     *         verwendet wird. Falls während der Anwendung eine Ausnahme geworfen wird, wird diese als Fehlschlag
     *         im neuen {@code Attempt} gespeichert.
     *     </p>
     *     <p>
     *         Diese Variante der {@code map}-Operation unterstützt nicht-checked Ausnahmen in der Transformation
     *         und ist damit für unsichere Umgebungen gedacht.
     *     </p>
     * </div>
     *
     * @param transformation eine {@link Applicable}-Instanz zur Transformation des Werts
     * @param <B> der Typ des neuen Werts
     * @return ein neues {@code Attempt} mit transformiertem Wert oder einem Fehler
     * @throws NullPointerException falls die Transformation {@code null} ist oder {@code null} liefert
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Attempt<B> mapUnsafe(final @NonNull Applicable<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Attempt<>(
                () -> switch (this.spool.get()) {
                    case Result.Success<A> success -> {
                        try {
                            yield success(Objects.requireNonNull(transformation.apply(success.get()), nullResultFrom("fMap")));
                        } catch (final Exception exception) {
                            yield (Result<B>) failure(exception);
                        }
                    }
                    case Result.Failure<A> failure -> (Result<B>) failure;
                }
        );
    }

    /**
     * Hebt die Anwendung einer Funktion innerhalb eines {@link Higher1} auf die gespeicherten
     * Werte in diesem {@code Attempt} an.
     *
     * @param transformation ein {@link Higher1}, das eine Funktion enthält, die auf die Werte angewendet wird
     * @param <B> der Typ des Ergebnisses der Funktion
     * @return ein neues {@code Attempt} mit dem Ergebnis der angehobenen Funktion
     * @throws NullPointerException falls das gegebene {@link Higher1} {@code null} ist
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <B> Attempt<B> lift(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Attempt<>(
            () -> switch (narrow(transformation).spool.get()) {
                case Result.Success<? extends Function<? super A, ? extends B>> success
                    -> (Result<B>) this.map(success.getValue()).tryIt();
                case Result.Failure<? extends Function<? super A, ? extends B>> failure
                    -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Hebt eine {@link Applicable}-Transformation innerhalb eines {@link Higher1}-Kontexts auf den
     *         gespeicherten Wert an. Diese Methode erlaubt die Anwendung einer in einem {@code Attempt}
     *         gespeicherten Funktion auf den gespeicherten Wert dieser Instanz.
     *     </p>
     *     <p>
     *         Anders als {@link #lift(Higher1)} erlaubt diese Variante auch Transformationen,
     *         die checked oder unchecked Exceptions werfen können.
     *     </p>
     * </div>
     *
     * @param transformation ein {@link Higher1}, das eine {@link Applicable}-Transformation enthält
     * @param <B> der Zieltyp nach Anwendung der Funktion
     * @return ein neues {@code Attempt} mit dem Ergebnis der angehobenen Transformation oder einem Fehler
     * @throws NullPointerException falls {@code transformation} {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <B> Attempt<B> liftUnsafe(final @NonNull Higher1<? extends µ, Applicable<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Attempt<>(
                () -> switch (narrow(transformation).spool.get()) {
                    case Result.Success<? extends Applicable<? super A, ? extends B>> success
                        -> (Result<B>) this.mapUnsafe(success.getValue()).tryIt();
                    case Result.Failure<Applicable<? super A, ? extends B>> failure
                        -> (Result<B>) failure;
                }
        );
    }

    /**
     * Führt die Bind-Operation durch, indem die gegebene Funktion auf den gespeicherten Wert angewendet wird,
     * und gibt ein neues {@code Attempt} zurück, das das Ergebnis enthält.
     *
     * @param transformation eine Funktion, die den gespeicherten Wert transformiert und ein neues {@link Higher1} erzeugt
     * @param <B> der Typ des Ergebnisses der Funktion
     *
     * @return ein neues {@code Attempt} mit dem transformierten Wert oder einer gespeicherten Ausnahme
     *
     * @throws NullPointerException falls die Funktion {@code null} ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public <B> Attempt<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return new Attempt<>(
            () -> switch (this.map(transformation).tryIt()) {
                case Result.Success<? extends Higher1<? extends µ, B>> success -> narrow(success.getValue()).tryIt();
                case Result.Failure<? extends Higher1<? extends µ, B>> failure -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Führt eine Bind-Operation (monadisches FlatMap) mit einer unsicheren {@link Applicable}-Funktion durch.
     *         Die Funktion liefert ein neues {@link Higher1}, das entpackt und weiterverarbeitet wird.
     *     </p>
     *     <p>
     *         Im Fehlerfall (z. B. durch eine Exception beim Anwenden der Funktion) wird das neue {@code Attempt}
     *         den Fehler enthalten.
     *     </p>
     * </div>
     *
     * @param transformation eine {@link Applicable}-Funktion, die ein neues {@link Higher1} zurückgibt
     * @param <B> der Typ des transformierten Werts
     * @return ein neues {@code Attempt} mit dem Ergebnis der Transformation oder einem Fehler
     * @throws NullPointerException falls {@code transformation} {@code null} ist
     * @throws Exception falls die Transformation selbst eine Exception wirft
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Attempt<B> bindUnsafe(@NonNull Applicable<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return new Attempt<>(
                () -> switch (this.mapUnsafe(transformation).tryIt()) {
                    case Result.Success<? extends Higher1<? extends µ, B>> success -> narrow(success.getValue()).tryIt();
                    case Result.Failure<? extends Higher1<? extends µ, B>> failure -> (Result<B>) failure;
                }
        );
    }

    /**
     * <div>
     *     <p>
     *         Spult alle Operationen auf diesem <code>Attempt</code> ab und liefert ein neues <code>Attempt</code>-Objekt
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
    public Attempt<A> unwind() {
        final var result
            = this.spool.get();
        return new Attempt<>(() -> result);
    }

}
