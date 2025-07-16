package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable4;

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
 *         Eine vierstellige Funktion <code>f:X1 &#x2715; X2 &#x2715; X3 &#x2715; X4 &rarr; Y</code>
 *     </p>
 * </div>
 *
 * @param <X1> Typ des ersten Arguments
 * @param <X2> Typ des zweiten Arguments
 * @param <X3> Typ des dritten Arguments
 * @param <X4> Typ des vierten Arguments
 * @param <Y> Typ des Ergebnisses
 *
 * @since 1.0.
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Fun4<X1, X2, X3, X4, Y>
        extends Deferrable4<X1, X2, X3, X4, Y> {

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion auf die vier &uuml;bergebenen Argumente an
     *     </p>
     * </div>
     *
     * @param x1 Das erste Argument
     * @param x2 Das zweite Argument
     * @param x3 Das dritte Argument
     * @param x4 Das vierte Argument
     * @return Ergebnis
     *
     * @since 1.0.0
     */
    @NonNull Y apply(final @NonNull X1 x1,
                     final @NonNull X2 x2,
                     final @NonNull X3 x3,
                     final @NonNull X4 x4);

    /**
     * <div>
     *     <p>
     *         Hintereinanderschaltung von Abbildungen
     *     </p>
     * </div>
     *
     * @param next Eine Funktion <code>Y &rarr; Z</code>
     * @param <Z> Typ des Ergebnisses der Funktion <code>next</code> und damit auch der Hintereinanderschaltung
     * @return Die Hintereinanderschaltung
     *
     * @throws NullPointerException Falls <code>next &#61;&#61; null</code>
     *
     * @since 1.0.0
     */
    default <Z> @NonNull Fun4<X1, X2, X3, X4, Z> andThen(@NonNull final Function<? super Y, ? extends Z> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return (x1, x2, x3, x4) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(next.apply(this.apply(x1, x2, x3, x4)), nullResult());
        };
    }

    @Override
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X1> a1,
                              final @NonNull Supplier<X2> a2,
                              final @NonNull Supplier<X3> a3,
                              final @NonNull Supplier<X4> a4) {
        Objects.requireNonNull(a1, nullValue("a1"));
        Objects.requireNonNull(a2, nullValue("a2"));
        Objects.requireNonNull(a3, nullValue("a3"));
        Objects.requireNonNull(a4, nullValue("a4"));
        return () -> {
            final var x1
                = Objects.requireNonNull(a1.get(), nullSuppliedFrom("a1"));
            final var x2
                = Objects.requireNonNull(a2.get(), nullSuppliedFrom("a2"));
            final var x3
                = Objects.requireNonNull(a3.get(), nullSuppliedFrom("a3"));
            final var x4
                = Objects.requireNonNull(a4.get(), nullSuppliedFrom("a4"));
            return Objects.requireNonNull(this.apply(x1, x2, x3, x4), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion partiell auf das erste Argument an
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das erste Argument
     * @return Eine Funktion <code>f:X2 &#x2715; X3 &#x2715; X4 &rarr; Y</code>
     *
     * @since 1.0.0
     */
    default @NonNull Fun3<X2, X3, X4, Y> partial1(@NonNull final Supplier<X1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x2, x3, x4) -> {
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(this.apply(supplier.get(), x2, x3, x4), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion partiell auf das zweite Argument an
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das zweite Argument
     * @return Eine Funktion <code>f:X1 &#x2715; X3 &#x2715; X4 &rarr; Y</code>
     *
     * @since 1.0.0
     */
    default @NonNull Fun3<X1, X3, X4, Y> partial2(@NonNull final Supplier<X2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x1, x3, x4) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x3, nullValue("x3"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(this.apply(x1, supplier.get(), x3, x4), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion partiell auf das dritte Argument an
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das dritte Argument
     * @return Eine Funktion <code>f:X1 &#x2715; X2 &#x2715; X4 &rarr; Y</code>
     *
     * @since 1.0.0
     */
    default @NonNull Fun3<X1, X2, X4, Y> partial3(@NonNull final Supplier<X3> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x1, x2, x4) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(this.apply(x1, x2, supplier.get(), x4), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet die Funktion partiell auf das vierte Argument an
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das vierte Argument
     * @return Eine Funktion <code>f:X1 &#x2715; X2 &#x2715; X3 &rarr; Y</code>
     *
     * @since 1.0.0
     */
    default @NonNull Fun3<X1, X2, X3, Y> partial4(@NonNull final Supplier<X4> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (x1, x2, x3) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            return Objects.requireNonNull(this.apply(x1, x2, x3, supplier.get()), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         'Curried' diese <code>Fun4</code>.<br />
     *     </p>
     * </div>
     *
     * @return Eine Funktion <code>f:X1 &rarr; (g:X2 &rarr; (h:X3 &rarr; (i:X4 &rarr; Y)))</code>
     *
     * @since 1.0.0
     */
    default @NonNull Fun<X1, Fun<X2, Fun<X3, Fun<X4, Y>>>> curry() {
        return x1 -> x2 -> x3 -> x4 -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(this.apply(x1, x2, x3, x4), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         'Uncurried' die &uuml;bergebene Funktion.<br />
     *     </p>
     * </div>
     *
     * @param curried Die 'curried' Funktion <code>f:X1 &rarr; (g:X2 &rarr; (h:X3 &rarr; (i:X4 &rarr; Y)))</code>
     * @param <X1> Typ des ersten Parameters
     * @param <X2> Typ des zweiten Parameters
     * @param <X3> Typ des dritten Parameters
     * @param <X4> Typ des vierten Parameters
     * @param <Y> Typ des Ergebnisses
     * @return Eine Funktion <code>f:X1 &#x2715; X2 &#x2715; X3 &#x2715; X4 &rarr; Y</code>
     *
     * @since 1.0.0
     */
    static <X1, X2, X3, X4, Y> Fun4<X1, X2, X3, X4, Y> uncurry(@NonNull final Function<X1, Function<X2, Function<X3, Function<X4, Y>>>> curried) {
        Objects.requireNonNull(curried, nullValue("curried"));
        return (x1, x2, x3, x4) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            Objects.requireNonNull(x3, nullValue("x3"));
            Objects.requireNonNull(x4, nullValue("x4"));
            return Objects.requireNonNull(curried.apply(x1).apply(x2).apply(x3).apply(x4), nullResult());
        };
    }

}
