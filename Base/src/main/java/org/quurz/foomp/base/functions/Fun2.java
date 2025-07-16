package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.quurz.foomp.base.types.Deferrable2;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface f&Uuml;r eine bin&auml;re Funktion, die zwei Argumente vom Typ {@code X1} und {@code X2} akzeptiert
 *         und ein Ergebnis vom Typ {@code Y} liefert. Diese Schnittstelle erweitert {@link BiFunction} und bietet zus&auml;tzliche
 *         Funktionalit&auml;ten f&uuml;r Currying, Partialanwendung und das Vertauschen der Argumente.
 *     </p>
 * </div>
 *
 * @param <X1> Der Typ des ersten Arguments der Funktion
 * @param <X2> Der Typ des zweiten Arguments der Funktion
 * @param <Y> Der R&uuml;ckgabetyp der Funktion
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Fun2<X1, X2, Y>
        extends Deferrable2<X1, X2, Y>,
                BiFunction<X1, X2, Y> {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine {@code Fun2}-Instanz aus einer gegebenen {@link BiFunction}.
     *     </p>
     * </div>
     *
     * @param function Die zu verwendende BiFunction
     * @param <X1> Der Typ des ersten Arguments der Funktion
     * @param <X2> Der Typ des zweiten Arguments der Funktion
     * @param <Y> Der R&uuml;ckgabetyp der Funktion
     * @return Eine neue {@code Fun2}-Instanz
     * @throws NullPointerException Wenn die gegebene Funktion {@code null} ist
     *
     * @since 1.0.0
     */
    static <X1, X2, Y> Fun2<X1, X2, Y> fun2(final @NonNull BiFunction<X1, X2, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return (x1, x2) -> Objects.requireNonNull(function.apply(x1, x2), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion auf die angegebenen Argumente an.
     *     </p>
     * </div>
     *
     * @param x1 Das erste Argument
     * @param x2 Das zweite Argument
     * @return Das Ergebnis der Funktion
     * @throws NullPointerException Wenn eines der Argumente {@code null} ist oder das Ergebnis {@code null} ergibt
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull Y
    apply(final @NonNull X1 x1,
          final @NonNull X2 x2);

    /**
     * <div>
     *     <p>
     *         Verkettet diese Funktion mit einer nachfolgenden Funktion. Das Ergebnis dieser Funktion wird als Argument
     *         f&uuml;r die gegebene Funktion {@code next} verwendet.
     *     </p>
     * </div>
     *
     * @param next Die nachfolgende Funktion, die auf das Ergebnis angewendet wird
     * @param <Z> Der R&uuml;ckgabetyp der nachfolgenden Funktion
     * @return Eine neue {@code Fun2}-Funktion, die die Verkettung der beiden Funktionen repr&auml;sentiert
     * @throws NullPointerException Wenn {@code next} oder das Ergebnis {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    default <Z> @NonNull Fun2<X1, X2, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return (x1, x2) -> {
            final var y
                = Objects.requireNonNull(this.apply(x1, x2), nullResult());
            return Objects.requireNonNull(next.apply(y), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine {@link Callable}-Instanz, die die Ausf&uuml;hrung der zweistelligen Funktion verz&ouml;gert.
     *         Dabei werden die Eingabeparameter durch die angegebenen {@link Supplier}-Instanzen bereitgestellt.
     *         Die Methode &uuml;berpr&uuml;ft, ob die Supplier und die von ihnen gelieferten Werte nicht {@code null} sind,
     *         und wirft andernfalls eine {@link NullPointerException}.
     *     </p>
     * </div>
     *
     * @param s1 ein {@link Supplier}, der den ersten Eingabeparameter liefert; darf nicht {@code null} sein
     * @param s2 ein {@link Supplier}, der den zweiten Eingabeparameter liefert; darf nicht {@code null} sein
     * @return ein {@link Callable}, das die Funktion mit den durch die Supplier bereitgestellten Eingaben ausf&uuml;hrt
     * @throws NullPointerException falls einer der Supplier oder die von ihnen gelieferten Werte {@code null} ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X1> s1,
                              final @NonNull Supplier<X2> s2) {
        Objects.requireNonNull(s1, nullValue("s1"));
        Objects.requireNonNull(s2, nullValue("s2"));
        return () -> {
            final var x1
                = Objects.requireNonNull(s1.get(), nullSuppliedFrom("s1"));
            final var x2
                = Objects.requireNonNull(s2.get(), nullSuppliedFrom("s2"));
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Funktion, bei der das erste Argument dieser Funktion durch den gegebenen {@link Supplier} bereitgestellt wird.
     *     </p>
     * </div>
     *
     * @param supplier Ein Supplier, der das erste Argument liefert
     * @return Eine Funktion, die nur das zweite Argument ben&Ouml;tigt
     * @throws NullPointerException Wenn {@code supplier} oder das gelieferte Argument {@code null} ist
     * @since 1.0.0
     */
    default @NonNull Fun<X2, Y> partial1(@NonNull final Supplier<X1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return x2 -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            final var x1
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine Funktion, bei der das zweite Argument dieser Funktion durch den gegebenen {@link Supplier} bereitgestellt wird.
     *     </p>
     * </div>
     *
     * @param supplier Ein Supplier, der das zweite Argument liefert
     * @return Eine Funktion, die nur das erste Argument ben&Ouml;tigt
     * @throws NullPointerException Wenn {@code supplier} oder das gelieferte Argument {@code null} ist
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X1, Y> partial2(@NonNull final Supplier<X2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return x1 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            final var x2
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Funktion zur&uuml;ck, die diese Funktion mit vertauschten Argumenten aufruft.
     *     </p>
     * </div>
     *
     * @return Eine neue Funktion mit vertauschten Argumenten
     *
     * @since 1.0.0
     */
    default @NonNull Fun2<X2, X1, Y> flip() {
        return (x2, x1) -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x1, nullValue("x1"));
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine Currying-Transformation durch, indem das erste Argument gebunden wird und eine Funktion zur&uuml;ckgegeben wird,
     *         die nur das zweite Argument akzeptiert.
     *     </p>
     * </div>
     *
     * @return Eine curried Funktion, die das erste Argument bindet und eine Funktion f&Uuml;r das zweite Argument liefert
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X1, Fun<X2, Y>> curry() {
        return x1 -> x2 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return Objects.requireNonNull(this.apply(x1, x2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         'Entcurryt' eine curried Funktion, um sie als normale zweistellige Funktion zu verwenden.
     *     </p>
     * </div>
     *
     * @param curried Die curried Funktion
     * @param <X1> Der Typ des ersten Arguments der Funktion
     * @param <X2> Der Typ des zweiten Arguments der Funktion
     * @param <Y> Der R&uuml;ckgabetyp der Funktion
     * @return Eine {@code Fun2}-Funktion, die die curried Funktion decurried
     * @throws NullPointerException Wenn {@code curried} oder eines der gebundenen Argumente {@code null} ist
     * @since 1.0.0
     */
    static <X1, X2, Y> Fun2<X1, X2, Y> uncurry(@NonNull final Fun<X1, Fun<X2, Y>> curried) {
        Objects.requireNonNull(curried, nullValue("curried"));
        return (x1, x2) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return Objects.requireNonNull(curried.apply(x1).apply(x2), nullResult());
        };
    }

}
