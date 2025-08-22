package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable2;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A binary predicate <code>f: A1 × A2 → {true, false}</code>.
 *     </p>
 *     <p>
 *         Contract: inputs must not be {@code null}. Combinators use short‑circuit evaluation
 *         where applicable (e.g., {@code and}, {@code or}, {@code implies}).
 *     </p>
 * </div>
 *
 * @see BiPredicate
 *
 * @param <A1> the type of the first argument
 * @param <A2> the type of the second argument
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred2<A1, A2>
        extends Deferrable2<A1, A2, Boolean>,
                BiPredicate<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Wraps the given {@link BiPredicate} into a {@code Pred2}.
     *     </p>
     *     <p>
     *         Contract: {@code biPredicate} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param biPredicate the predicate to wrap; must not be {@code null}
     * @param <A1>        the first argument type
     * @param <A2>        the second argument type
     * @return a {@code Pred2} delegating to {@code biPredicate}
     *
     * @throws NullPointerException if {@code biPredicate} is {@code null}
     *
     * @since 1.0.0
     */
    static <A1, A2> Pred2<A1, A2> pred2(@NonNull final BiPredicate<? super A1, ? super A2> biPredicate) {
        Objects.requireNonNull(biPredicate, nullValue("biPredicate"));
        return biPredicate::test;
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical negation of the given {@code Pred2}.
     *     </p>
     *     <p>
     *         Contract: {@code pred2} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param pred2 the predicate to negate; must not be {@code null}
     * @param <A1>  the first argument type
     * @param <A2>  the second argument type
     * @return a predicate representing {@code NOT pred2}
     *
     * @throws NullPointerException if {@code pred2} is {@code null}
     *
     * @since 1.0.0
     */
    static <A1, A2> Pred2<A1, A2> not(@NonNull final Pred2<? super A1, ? super A2> pred2) {
        Objects.requireNonNull(pred2, nullValue("pred2"));
        return (a1, a2) -> !pred2.test(a1, a2);
    }

    /**
     * <div>
     *     <p>
     *         Evaluates this predicate for the given inputs.
     *     </p>
     *     <p>
     *         Contract: inputs must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 the first input; must not be {@code null}
     * @param a2 the second input; must not be {@code null}
     * @return {@code true} or {@code false}
     *
     * @since 1.0.0
     */
    @Override
    boolean test(final @NonNull A1 a1,
                 final @NonNull A2 a2);

    /**
     * <div>
     *     <p>
     *         Returns the logical conjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a1, a2)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this AND other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2) -> (this.test(a1, a2) && other.test(a1, a2));
    }

    /**
     * <div>
     *     <p>
     *         Logical conjunction with a {@link BooleanSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this AND booleanSupplier}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2) -> (this.test(a1, a2) && booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Logical conjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this AND supplier.get()}
     *
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.and(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NAND of this predicate and {@code other}.
     *         Implemented as {@code NOT (this AND other)}; short‑circuiting follows {@code and}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code NOT (this AND other)}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.and(other));
    }

    /**
     * <div>
     *     <p>
     *         Logical NAND with a {@link BooleanSupplier}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this AND boolSupplier)}
     *
     * @throws NullPointerException if {@code boolSupplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.and(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         Logical NAND with a {@link Supplier}{@code <Boolean>}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this AND supplier.get())}
     *
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical disjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a1, a2)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this OR other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2) -> (this.test(a1, a2) || other.test(a1, a2));
    }

    /**
     * <div>
     *     <p>
     *         Logical disjunction with a {@link BooleanSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this OR boolSupplier}
     *
     * @throws NullPointerException if {@code boolSupplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (a1, a2) -> (this.test(a1, a2) || boolSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Logical disjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this OR supplier.get()}
     *
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.or(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Logical NOR of this predicate and {@code other} (negation of disjunction).
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code NOT (this OR other)}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.or(other));
    }

    /**
     * <div>
     *     <p>
     *         Logical NOR with a {@link BooleanSupplier}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this OR boolSupplier)}
     *
     * @throws NullPointerException if {@code boolSupplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.or(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         Logical NOR with a {@link Supplier}{@code <Boolean>}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this OR supplier.get())}
     *
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Exclusive OR of this predicate and {@code other}.
     *         Implemented as {@code (this OR other) AND NOT (this AND other)}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this XOR other}
     *
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (this.or(other)).and(not(this.and(other)));
    }

    /**
     * <div>
     *     <p>
     *         Exclusive OR with a {@link BooleanSupplier}.
     *         Implemented as {@code (this OR boolSupplier) AND NOT (this AND boolSupplier)}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this XOR boolSupplier}
     *
     * @throws NullPointerException if {@code boolSupplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (this.or(boolSupplier)).and(not(this.and(boolSupplier)));
    }

    /**
     * <div>
     *     <p>
     *         Exclusive OR with a {@link Supplier}{@code <Boolean>}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this XOR supplier.get()}
     *
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.xor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Partially evaluates this predicate by supplying the first argument.
     *     </p>
     * </div>
     *
     * @param supplier supplies the first argument; must not be {@code null}
     * @return a unary predicate {@code f: A2 → {true, false}}
     *
     * @throws NullPointerException if {@code supplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A2> partial1(@NonNull final Supplier<A1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return a2 -> this.test(supplier.get(), a2);
    }

    /**
     * <div>
     *     <p>
     *         Partially evaluates this predicate by supplying the second argument.
     *     </p>
     * </div>
     *
     * @param supplier supplies the second argument; must not be {@code null}
     * @return a unary predicate {@code f: A1 → {true, false}}
     *
     * @throws NullPointerException if {@code supplier} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A1> partial2(@NonNull final Supplier<A2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return a1 -> this.test(a1, supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Delays the evaluation of this predicate until both inputs are
     *         supplied by the given {@link Supplier}s.
     *     </p>
     *     <p>
     *         Contract: suppliers must not be {@code null} and must not supply {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier1 supplies the first input; must not be {@code null}
     * @param supplier2 supplies the second input; must not be {@code null}
     * @return a deferred computation of this predicate
     *
     * @throws NullPointerException if any argument is {@code null} or any supplier supplies {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Boolean> defer(final @NonNull Supplier<A1> supplier1,
                                    final @NonNull Supplier<A2> supplier2) {
        Objects.requireNonNull(supplier1, nullValue("supplier1"));
        Objects.requireNonNull(supplier2, nullValue("supplier2"));
        return () -> this.test(
                Objects.requireNonNull(supplier1.get(), nullSupplied()),
                Objects.requireNonNull(supplier2.get(), nullSupplied())
        );
    }

}
