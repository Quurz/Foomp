package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.util.Nothing;
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
 *         Die {@code Provider}-Schnittstelle stellt einen funktionalen Typ dar, der einen Wert des Typs {@code A}
 *         liefert und verschiedene Funktionalit&auml;ten f&uuml;r die funktionale Programmierung bereitstellt.
 *         Sie kombiniert Merkmale von Funktoren, Monaden und weiteren Konzepten, um eine vielseitige API
 *         f&uuml;r die Bereitstellung und Transformation von Werten zu erm&ouml;glichen.
 *     </p>
 *     <p>
 *         Als funktionale Schnittstelle kann {@code Provider} direkt mit Lambda-Ausdr&uuml;cken oder Method-Referenzen verwendet werden.
 *         Ein {@code Provider} wird h&auml;ufig genutzt, um Werte zu kapseln, lazy zu evaluieren oder die
 *         Wiederverwendbarkeit und Transformation von Werten in einer funktionalen Weise zu erleichtern.
 *     </p>
 * </div>
 *
 * <h2>Eigenschaften:</h2>
 * <ul>
 *     <li>Implementiert {@link Supplier}, um Werte bereitzustellen.</li>
 *     <li>Unterst&uuml;tzt Monaden-Operationen wie {@code map} und {@code bind}.</li>
 *     <li>Ist als {@link Fun}, {@link Value} und {@link Higher1} kompatibel.</li>
 *     <li>Bietet zus&auml;tzliche Funktionen wie {@code copy}, {@code unwind} und {@code transmogrify}.</li>
 * </ul>
 *
 * <h2>Typparameter:</h2>
 * <ul>
 *     <li>{@code A} – der Typ des bereitgestellten Wertes.</li>
 * </ul>
 *
 * @param <A> der Typ des bereitgestellten Wertes
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
@FunctionalInterface
public interface Provider<A>
        extends Monadic<Provider.µ, A>,
                Copyable<Provider<A>>,
                Unwindable<Provider<A>>,
                Transmogrifyable<Provider<A>>,
                Fun<Nothing, A>,
                Value<A>,
                Higher1<Provider.µ, A> {

    /**
     * <div>
     *     <p>
     *         Der Witness-Typ f&uuml;r <code>Provider</code>
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Wandelt ein {@code Higher1}-Objekt in einen {@code Provider} um.
     *     </p>
     * </div>
     *
     * @param wide das zu konvertierende {@code Higher1}-Objekt
     * @param <A>  der Typ des bereitgestellten Wertes
     * @return ein {@code Provider}-Objekt
     * @throws NullPointerException falls {@code wide} {@code null} ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Provider<A> narrow(final @NonNull Higher1<? extends Provider.µ, A> wide) {
        return (Provider<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Erstellt einen {@code Provider}, der stets den angegebenen Wert zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param value der Wert, der bereitgestellt wird
     * @param <A>   der Typ des Wertes
     * @return ein {@code Provider}, der den angegebenen Wert liefert
     * @throws NullPointerException falls {@code value} {@code null} ist
     *
     * @since 1.0.0
     */
    static <A> Provider<A> provider(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return () -> value;
    }

    /**
     * <div>
     *     <p>
     *         Erstellt einen {@code Provider}, der den Wert von einem {@code Supplier} bezieht.
     *     </p>
     * </div>
     *
     * @param supplier die Quelle des Wertes
     * @param <A>      der Typ des bereitgestellten Wertes
     * @return ein {@code Provider}, der Werte aus dem {@code Supplier} bezieht
     * @throws NullPointerException falls {@code supplier} oder der von ihm gelieferte Wert {@code null} ist
     *
     * @since 1.0.0
     */
    static <A> Provider<A> providerFrom(final @NonNull Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return () -> Objects.requireNonNull(supplier.get(), nullSupplied());
    }

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob der Provider einen g&uuml;ltigen Wert enth&auml;lt.
     *     </p>
     * </div>
     *
     * @return immer {@code true}, da ein {@code Provider} per Definition einen Wert enth&auml;lt
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
     *         Gibt den gespeicherten Wert zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return der gespeicherte Wert
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    A get();

    /**
     * <div>
     *     <p>
     *         Wendet diese Funktion auf das gegebene {@link Nothing}-Argument an und gibt das gespeicherte Ergebnis zur&uuml;ck.
     *     </p>
     *     <p>
     *         Diese Methode erzwingt die Evaluation des gespeicherten Werts, falls er noch nicht berechnet wurde.
     *         Da {@link Nothing} keine tats&auml;chlichen Werte enth&auml;lt, dient es hier lediglich als Platzhalter.
     *     </p>
     * </div>
     *
     * @param nothing der Platzhalterwert {@link Nothing}; darf nicht {@code null} sein
     * @return der gespeicherte Wert dieses {@code Provider}-Objekts
     * @throws NullPointerException wenn {@code nothing} oder der gespeicherte Wert {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default A apply(final @NonNull Nothing nothing) {
        Objects.requireNonNull(nothing, nullValue("nothing"));
        return Objects.requireNonNull(this.get(), nullSupplied());
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion auf den gespeicherten Wert an und gibt das Ergebnis als neuen {@code Provider} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die Transformationsfunktion
     * @param <B>  der neue Typ des Wertes
     * @return ein neuer {@code Provider} mit dem transformierten Wert
     * @throws NullPointerException falls {@code fMap} oder das Ergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return () -> Objects.requireNonNull(transformation.apply(this.get()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Hebt eine Funktion in den Kontext eines {@code Provider} an und wendet sie an.
     *     </p>
     * </div>
     *
     * @param transformation die Funktion, die in den Kontext eines {@code Provider} gehoben wurde
     * @param <B>   der Ergebnis-Typ
     * @return ein neuer {@code Provider} mit dem transformierten Wert
     * @throws NullPointerException falls {@code liftA} oder die enthaltene Funktion {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> lift(final @NonNull Higher1<? extends Provider.µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation);
        final var narrowed
            = narrow(transformation);
        return this.map(narrowed.get());
    }

    /**
     * <div>
     *     <p>
     *         Wendet eine monadische Transformation an und gibt das Ergebnis als neuen {@code Provider} zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param transformation die monadische Transformationsfunktion
     * @param <B>   der Ergebnis-Typ
     * @return ein neuer {@code Provider} mit dem transformierten Wert
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return this.map(a -> narrow(transformation.apply(a)).get());
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine exakte Kopie dieses {@code Provider}.
     *     </p>
     * </div>
     *
     * @return eine neue Instanz des {@code Provider}, die denselben Wert liefert
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Provider<A> copy() {
        final var self
            = this;
        return self::get;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Transformiert diesen {@code Provider} mithilfe der angegebenen Funktion.
     *     </p>
     * </div>
     *
     * @param transmogrifier die Transformationsfunktion
     * @param <T>            der Ergebnis-Typ der Transformation
     * @return das transformierte Objekt
     * @throws NullPointerException falls {@code transmogrifier} {@code null} ist oder ein {@code null}-Ergebnis liefert
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Provider<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Wickelt diesen {@code Provider} ab und liefert eine nicht-lazy evaluierte Version.
     *     </p>
     * </div>
     *
     * @return ein neuer {@code Provider}, der den aktuellen Wert enth&auml;lt
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Provider<A> unwind() {
        final var value
            = this.get();
        return () -> value;
    }

}
