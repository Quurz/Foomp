package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.quurz.foomp.base.types.Deferrable3;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface, das eine Funktion mit drei Argumenten darstellt.
 *         Das {@code Fun3}-Interface definiert eine tern&auml;re Funktion, die drei Eingabe-Argumente
 *         annimmt und ein Ergebnis liefert.
 *     </p>
 * </div>
 *
 * @param <X1> der Typ des ersten Arguments
 * @param <X2> der Typ des zweiten Arguments
 * @param <X3> der Typ des dritten Arguments
 * @param <Y>  der Typ des Ergebnisses
 *
 * @since 1.0.0
 *
 *
 */
@FunctionalInterface
public interface Fun3<X1, X2, X3, Y>
        extends Deferrable3<X1, X2, X3, Y> {

    /**
     * <div>
     *     <p>
     *         Wendet diese Funktion auf die gegebenen Argumente an.
     *     </p>
     * </div>
     *
     * @param x1 das erste Argument
     * @param x2 das zweite Argument
     * @param x3 das dritte Argument
     * @return das Ergebnis der Funktionsanwendung
     */
    @Pure
    @NonNull Y apply(@NonNull final X1 x1,
                     @NonNull final X2 x2,
                     @NonNull final X3 x3);

    /**
     * <div>
     *     <p>
     *         Gibt eine zusammengesetzte Funktion zur&#252;ck, die diese Funktion auf ihre Eingaben
     *         anwendet und dann die Funktion {@code next} auf das Ergebnis anwendet.
     *     </p>
     * </div>
     *
     * @param <Z>  der Ausgabetyp der {@code next}-Funktion
     * @param next die Funktion, die nach dieser Funktion angewendet werden soll
     * @return eine zusammengesetzte Funktion, die zuerst diese Funktion und dann {@code next} anwendet
     * @throws NullPointerException wenn {@code next} {@code null} ist
     */
    default <Z> @NonNull Fun3<X1, X2, X3, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, "Argument 'next' must not be null");
        return (x1, x2, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(next.apply(this.apply(x1, x2, x3)), nullResult());
        };
    }

    @Override
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X1> a1,
                              final @NonNull Supplier<X2> a2,
                              final @NonNull Supplier<X3> a3) {
        Objects.requireNonNull(a1, nullValue("a1"));
        Objects.requireNonNull(a2, nullValue("a2"));
        Objects.requireNonNull(a3, nullValue("a3"));
        return () -> {
            final var x1
                = Objects.requireNonNull(a1.get(), nullSuppliedFrom("a1"));
            final var x2
                = Objects.requireNonNull(a2.get(), nullSuppliedFrom("a2"));
            final var x3
                = Objects.requireNonNull(a3.get(), nullSuppliedFrom("a3"));
            return Objects.requireNonNull(this.apply(x1, x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diese Funktion teilweise an, indem das erste Argument &#252;ber den
     *         {@code supplier} bereitgestellt wird, und gibt eine Funktion zur&#252;ck,
     *         die die verbleibenden Argumente akzeptiert.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&#252;r das erste Argument
     * @return eine neue Funktion, die das zweite und dritte Argument akzeptiert
     * @throws NullPointerException wenn {@code supplier} {@code null} ist
     */
    default @NonNull Fun2<X2, X3, Y> partial1(@NonNull final Supplier<X1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x2, x3) -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(supplier.get(), x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diese Funktion teilweise an, indem das zweite Argument &#252;ber den
     *         {@code supplier} bereitgestellt wird, und gibt eine Funktion zur&#252;ck,
     *         die die verbleibenden Argumente akzeptiert.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&#252;r das zweite Argument
     * @return eine neue Funktion, die das erste und dritte Argument akzeptiert
     * @throws NullPointerException wenn {@code supplier} {@code null} ist
     */
    default @NonNull Fun2<X1, X3, Y> partial2(@NonNull final Supplier<X2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x1, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(x1, supplier.get(), x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diese Funktion teilweise an, indem das dritte Argument &#252;ber den
     *         {@code supplier} bereitgestellt wird, und gibt eine Funktion zur&#252;ck,
     *         die die verbleibenden Argumente akzeptiert.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&#252;r das dritte Argument
     * @return eine neue Funktion, die das erste und zweite Argument akzeptiert
     * @throws NullPointerException wenn {@code supplier} {@code null} ist
     */
    default @NonNull Fun2<X1, X2, Y> partial3(@NonNull final Supplier<X3> supplier) {
        Objects.requireNonNull(supplier, "Argument 'supplier' must not be null");
        return (x1, x2) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            return this.apply(x1, x2, supplier.get());
        };
    }

    /**
     * <div>
     *     <p>
     *         Curried diese Funktion in eine verschachtelte Kette von un&#228;ren Funktionen.
     *     </p>
     * </div>
     *
     * @return eine curried Form dieser Funktion
     */
    default @NonNull Fun<X1, Fun<X2, Fun<X3, Y>>> curry() {
        return x1 -> x2 -> x3 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(x1, x2, x3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Konvertiert eine curried Funktion in eine {@code Fun3}-Instanz.
     *     </p>
     * </div>
     *
     * @param curried die curried Funktion, die konvertiert werden soll
     * @param <X1>    der Typ des ersten Arguments
     * @param <X2>    der Typ des zweiten Arguments
     * @param <X3>    der Typ des dritten Arguments
     * @param <Y>     der Typ des Ergebnisses
     * @return eine {@code Fun3}-Instanz, die die gegebene curried Funktion darstellt
     * @throws NullPointerException wenn {@code curried} {@code null} ist
     */
    static <X1, X2, X3, Y> Fun3<X1, X2, X3, Y> uncurry(@NonNull final Function<X1, Function<X2, Function<X3, Y>>> curried) {
        Objects.requireNonNull(curried, nullValue("curried"));
        return (x1, x2, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(curried.apply(x1).apply(x2).apply(x3), nullResult());
        };
    }

}
