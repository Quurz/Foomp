package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.UnaryOperator;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein einstelliger Operator, der eine Abbildung von einer Menge in dieselbe Menge darstellt <code>f:T &#x21A6; T</code>.
 *     </p>
 * </div>
 *
 * @param <A> der Typ des Operanden und des Ergebnisses
 *
 * @see UnaryOperator
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Operator<A>
        extends UnaryOperator<A>,
                Fun<A, A> {

    /**
     * <div>
     *     <p>
     *         Verpackt den gegebenen <code>UnaryOperator</code> in einen <code>Operator</code>.
     *     </p>
     * </div>
     *
     * @param operator der einzupackende <code>UnaryOperator</code>
     * @param <A> der Typ des Operanden
     * @return der neue <code>Operator</code>
     * @throws NullPointerException falls <code>operator</code> null ist
     *
     * @since 1.0.0
     */
    static <A> Operator<A> operator(@NonNull final UnaryOperator<A> operator) {
        Objects.requireNonNull(operator, nullValue("operator"));
        return a -> {
            Objects.requireNonNull(a, nullValue("t"));
            return Objects.requireNonNull(operator.apply(a), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Wendet diesen Operator auf das gegebene Argument an.
     *     </p>
     * </div>
     *
     * @param a das Argument des Operators
     * @return das Ergebnis der Anwendung des Operators
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    @NonNull
    A apply(@NonNull final A a);

    /**
     * <div>
     *     <p>
     *         Komponiert diesen Operator mit einem anderen Operator.
     *     </p>
     * </div>
     *
     * @param before der Operator, der vor diesem Operator angewendet wird
     * @return die Komposition der beiden Operatoren
     * @throws NullPointerException falls <code>before</code> null ist
     *
     * @since 1.0.0
     */
    default Operator<A> compose(@NonNull final Operator<A> before) {
        Objects.requireNonNull(before, "Argument 'before' must not be null");
        return a -> {
            Objects.requireNonNull(a, nullValue("o"));
            final var temp
                = Objects.requireNonNull(before.apply(a), nullResult());
            return Objects.requireNonNull(this.apply(temp), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Verkettet diesen Operator mit einem anderen Operator.
     *     </p>
     * </div>
     *
     * @param next der Operator, der nach diesem Operator angewendet wird
     * @return die Verkettung der beiden Operatoren
     * @throws NullPointerException falls <code>next</code> null ist
     *
     * @since 1.0.0
     */
    default Operator<A> andThen(@NonNull final Operator<A> next) {
        Objects.requireNonNull(next, nullValue("next"));
        return a -> {
            final var temp
                = Objects.requireNonNull(this.apply(a), nullResult());
            return Objects.requireNonNull(next.apply(temp), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Liefert den identischen Operator, der das Argument unver&auml;ndert zur&uuml;ckgibt.
     *     </p>
     * </div>
     *
     * @param <A> der Typ des Arguments und des Ergebnisses
     * @return der identische Operator
     *
     * @since 1.0.0
     */
    static <A> Operator<A> identity() {
        return a -> {
            Objects.requireNonNull(a, nullValue("a"));
            return a;
        };
    }

}
