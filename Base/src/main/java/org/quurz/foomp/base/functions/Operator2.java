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
 *         A binary operator representing a mapping from a set to itself
 *         <code>f: A × A → A</code>.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the operand/result type
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
     *         Wraps the given {@link BinaryOperator} into an {@code Operator2} that enforces non-null
     *         inputs and results.
     *     </p>
     * </div>
     *
     * @param binaryOperator the binary operator to wrap; must not be {@code null}
     * @param <A>            the operand type
     * @return a new {@code Operator2} delegating to {@code binaryOperator}
     * @throws NullPointerException if {@code binaryOperator} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    static <A> Operator2<A> operator2(@NonNull final BinaryOperator<A> binaryOperator) {
        Objects.requireNonNull(binaryOperator, nullValue("binaryOperator"));
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("a2"));
            return Objects.requireNonNull(binaryOperator.apply(a1, a2), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Applies this operator to the given arguments.
     *     </p>
     *     <p>
     *         Contract: inputs must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 the first operand; must not be {@code null}
     * @param a2 the second operand; must not be {@code null}
     * @return the result; never {@code null}
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
     *         Returns a composed operator that applies {@code this} and then {@code after}.
     *     </p>
     *     <p>
     *         Contract: {@code after} must not be {@code null}; intermediate and final results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param after the unary post-operator; must not be {@code null}
     * @return the composed operator
     * @throws NullPointerException if {@code after} is {@code null} or any intermediate result is {@code null}
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
     *         Partially applies this operator by supplying the first argument from a {@link Supplier}.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the first operand; must not be {@code null}
     * @return an {@link Operator} over the remaining argument
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
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
     *         Partially applies this operator by supplying the second argument from a {@link Supplier}.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the second operand; must not be {@code null}
     * @return an {@link Operator} over the remaining argument
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
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
     *         Returns an operator with flipped argument order.
     *     </p>
     * </div>
     *
     * @return an {@code Operator2} that applies {@code this} with reversed arguments
     *
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
