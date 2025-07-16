package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein zweistelliger Operator, der eine Abbildung von einer Menge in dieselbe Menge darstellt <code>f:T &#x2715; T &rarr; T</code>.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der Operanden und des Ergebnisses
 *
 * @see Fun2
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Operator2<A>
        extends BinaryOperator<A>,
                Fun2<A, A, A> {

    /**
     * <div>
     *     <p>
     *         Verpackt den gegebenen <code>BinaryOperator</code> in einen <code>Operator2</code>.
     *     </p>
     * </div>
     *
     * @param binaryOperator der einzupackende <code>BinaryOperator</code>
     * @param <A> der Typ der Operanden
     * @return der neue <code>Operator2</code>
     * @throws NullPointerException falls <code>binaryOperator</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Operator2<A> operator2(@NonNull final BinaryOperator<A> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("as"));
            return Objects.requireNonNull(binaryOperator.apply(a1, a2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator auf die gegebenen Argumente an.
     *     </p>
     * </div>
     *
     * @param a1 das erste Argument des Operators
     * @param a2 das zweite Argument des Operators
     * @return das Ergebnis der Anwendung des Operators
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull
    A apply(@NonNull final A a1,
            @NonNull final A a2);

    /**
     * <div>
     *     <p>
     *         Verkettet diesen Operator mit einem <code>UnaryOperator</code>.
     *     </p>
     * </div>
     *
     * @param after der Operator, der nach diesem Operator angewendet wird
     * @return die Verkettung der beiden Operatoren
     * @throws NullPointerException falls <code>after</code> null ist
     *
     * @since 1.0.0
     */
    default @NonNull Operator2<A> andThen(@NonNull final UnaryOperator<A> after) {
        Objects.requireNonNull(after, nullValue("after"));
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("a2"));
            final var t_
                = Objects.requireNonNull(this.apply(a1, a2), nullResult());
            return Objects.requireNonNull(after.apply(t_), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator partiell auf das erste Argument an.
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das erste Argument
     * @return ein <code>Operator</code>, der das zweite Argument akzeptiert und das Ergebnis liefert
     * @throws NullPointerException falls <code>supplier</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    default @NonNull Operator<A> partial1(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return a2 -> {
            Objects.requireNonNull(a2, nullValue("a2"));
            final var t1
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(t1, a2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator partiell auf das zweite Argument an.
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das zweite Argument
     * @return ein <code>Operator</code>, der das erste Argument akzeptiert und das Ergebnis liefert
     * @throws NullPointerException falls <code>supplier</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    default @NonNull Operator<A> partial2(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return a1 -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            final var t2
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(a1, t2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Vertauscht die Argumente dieses Operators.
     *     </p>
     * </div>
     *
     * @return ein <code>Operator2</code>, der die Argumente vertauscht
     * @since 1.0.0
     */
    @Override
    default @NonNull Operator2<A> flip() {
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("a2"));
            return Objects.requireNonNull(this.apply(a2, a1), nullResult());
        };
    }

}
