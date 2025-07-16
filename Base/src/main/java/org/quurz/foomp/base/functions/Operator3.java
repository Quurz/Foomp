package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein dreistelliger Operator, der drei Argumente auf ein Ergebnis desselben Typs abbildet <code>f:A &#x2715; A &#x2715; A &#x21A6; A</code>.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der Argumente und des Ergebnisses
 *
 * @see Fun3
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Operator3<A>
        extends Fun3<A, A, A, A> {

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator auf die drei Operanden an
     *     </p>
     * </div>
     *
     * @param a1 Der erste Operand
     * @param a2 Der zweite Operand
     * @param a3 Der dritte Operand
     * @return Ergebnis
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull
    A apply(@NonNull final A a1,
            @NonNull final A a2,
            @NonNull final A a3);

    /**
     * <div>
     *     <p>
     *         Verkettet diesen Operator mit einem <code>UnaryOperator</code>.
     *     </p>
     * </div>
     *
     * @param after der Operator, der nach diesem Operator angewendet wird
     * @return ein zusammengesetzter Operator, der zuerst diesen Operator und dann den <code>after</code>-Operator anwendet
     * @throws NullPointerException falls <code>after</code> null ist
     *
     * @since 1.0.0
     */
    default Operator3<A> andThen(@NonNull UnaryOperator<A> after) {
        Objects.requireNonNull(after, nullValue("after"));
        return (a1, a2, a3) -> {
            Objects.requireNonNull(a1, nullValue("t2"));
            Objects.requireNonNull(a2, nullValue("t2"));
            Objects.requireNonNull(a3, nullValue("t3"));
            final var t
                = Objects.requireNonNull(this.apply(a1, a2, a3), nullResult());
            return Objects.requireNonNull(after.apply(t), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator partiell auf das erste Argument an.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&uuml;r das erste Argument
     * @return ein Operator, der das zweite und dritte Argument akzeptiert und das Ergebnis liefert
     * @throws NullPointerException falls <code>supplier</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial1(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a2, a3) -> {
            Objects.requireNonNull(a2, nullValue("t2"));
            Objects.requireNonNull(a3, nullValue("t3"));
            final var t1
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(t1, a2, a3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator partiell auf das zweite Argument an.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&uuml;r das zweite Argument
     * @return ein Operator, der das erste und dritte Argument akzeptiert und das Ergebnis liefert
     * @throws NullPointerException falls <code>supplier</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial2(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a1, a3) -> {
            Objects.requireNonNull(a1, nullValue("t1"));
            Objects.requireNonNull(a3, nullValue("t3"));
            final var t2
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(a1, t2, a3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator partiell auf das dritte Argument an.
     *     </p>
     * </div>
     *
     * @param supplier ein Lieferant f&uuml;r das dritte Argument
     * @return ein Operator, der das erste und zweite Argument akzeptiert und das Ergebnis liefert
     * @throws NullPointerException falls <code>supplier</code> null ist
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial3(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("t1"));
            Objects.requireNonNull(a2, nullValue("t2"));
            final var t3
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(a1, a2, t3), nullResult());
        };
    }

}
