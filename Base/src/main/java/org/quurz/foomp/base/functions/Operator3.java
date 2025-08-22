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
 *         A ternary operator mapping three arguments to a result of the same type
 *         <code>f: A × A × A → A</code>.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <A> the operand/result type
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
     *         Applies this operator to the three operands.
     *     </p>
     *     <p>
     *         Contract: inputs must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 the first operand; must not be {@code null}
     * @param a2 the second operand; must not be {@code null}
     * @param a3 the third operand; must not be {@code null}
     * @return the result; never {@code null}
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
     *         Returns a composed operator that applies {@code this} and then the given {@link UnaryOperator}.
     *     </p>
     *     <p>
     *         Contract: {@code after} must not be {@code null}; intermediate and final results must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param after the unary post-operator; must not be {@code null}
     * @return a composed operator applying {@code this} and then {@code after}
     * @throws NullPointerException if {@code after} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    default Operator3<A> andThen(@NonNull UnaryOperator<A> after) {
        Objects.requireNonNull(after, nullValue("after"));
        return (a1, a2, a3) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("a2"));
            Objects.requireNonNull(a3, nullValue("a3"));
            final var t
                = Objects.requireNonNull(this.apply(a1, a2, a3), nullResult());
            return Objects.requireNonNull(after.apply(t), nullResult());
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
     * @return an {@link Operator2} over the remaining arguments
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial1(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a2, a3) -> {
            Objects.requireNonNull(a2, nullValue("a2"));
            Objects.requireNonNull(a3, nullValue("a3"));
            final var t1
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(t1, a2, a3), nullResult());
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
     * @return an {@link Operator2} over the remaining arguments
     * @throws NullPointerException if the supplier or its value is {@code null}, or the result is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial2(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a1, a3) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a3, nullValue("a3"));
            final var t2
                    = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(a1, t2, a3), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Partially applies this operator by supplying the third argument from a {@link Supplier}.
     *     </p>
     *     <p>
     *         Contract: the supplier and its value must not be {@code null}; the result must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the third operand; must not be {@code null}
     * @return an {@link Operator2} over the remaining arguments
     * @throws NullPointerException if the supplier or its value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull default Operator2<A> partial3(@NonNull final Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return (a1, a2) -> {
            Objects.requireNonNull(a1, nullValue("a1"));
            Objects.requireNonNull(a2, nullValue("a2"));
            final var t3
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(this.apply(a1, a2, t3), nullResult());
        };
    }

}
