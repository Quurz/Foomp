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
 *         A unary operator representing a mapping from a set to itself
 *         <code>f: A → A</code>.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the operand/result type
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
     *         Wraps the given {@link UnaryOperator} into an {@code Operator} that enforces non-null
     *         input and output.
     *     </p>
     * </div>
     *
     * @param operator the unary operator to wrap; must not be {@code null}
     * @param <A>      the operand type
     * @return a new {@code Operator} delegating to {@code operator}
     * @throws NullPointerException if {@code operator} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    static <A> Operator<A> operator(@NonNull final UnaryOperator<A> operator) {
        Objects.requireNonNull(operator, nullValue("operator"));
        return a -> {
            Objects.requireNonNull(a, nullValue("a"));
            return Objects.requireNonNull(operator.apply(a), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Applies this operator to the given argument.
     *     </p>
     *     <p>
     *         Contract: {@code a} must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a the input value; must not be {@code null}
     * @return the result; never {@code null}
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
     *         Returns the composition {@code this ∘ before}.
     *         The resulting operator first applies {@code before}, then applies {@code this}.
     *     </p>
     *     <p>
     *         Contract: {@code before} must not be {@code null}; intermediate and final results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param before the operator to apply first; must not be {@code null}
     * @return the composed operator
     * @throws NullPointerException if {@code before} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    default Operator<A> compose(@NonNull final Operator<A> before) {
        Objects.requireNonNull(before, "Argument 'before' must not be null");
        return a -> {
            Objects.requireNonNull(a, nullValue("a"));
            final var temp
                = Objects.requireNonNull(before.apply(a), nullResult());
            return Objects.requireNonNull(this.apply(temp), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns the composition {@code next ∘ this}.
     *         The resulting operator first applies {@code this}, then applies {@code next}.
     *     </p>
     *     <p>
     *         Contract: {@code next} must not be {@code null}; intermediate and final results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param next the operator to apply afterwards; must not be {@code null}
     * @return the composed operator
     * @throws NullPointerException if {@code next} is {@code null} or any intermediate result is {@code null}
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
     *         Returns the identity operator that yields its input unchanged.
     *     </p>
     *     <p>
     *         Contract: the input must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param <A> the operand/result type
     * @return the identity operator
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
