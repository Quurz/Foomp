package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.quurz.foomp.base.types.Deferrable3;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A ternary predicate <code>f: A1 × A2 × A3 → {true, false}</code>.
 *     </p>
 *     <p>
 *         Contract: inputs must not be {@code null}. Combinators operate with short‑circuit semantics
 *         between predicates (this vs. other) where applicable (e.g., {@code and}, {@code or}).
 *     </p>
 * </div>
 *
 * @param <A1> the type of the first argument
 * @param <A2> the type of the second argument
 * @param <A3> the type of the third argument
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred3<A1, A2, A3>
        extends Deferrable3<A1, A2, A3, Boolean> {

    /**
     * <div>
     *     <p>
     *         Returns the logical negation of the given {@code Pred3}.
     *     </p>
     *     <p>
     *         Contract: {@code pred3} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param pred3 the predicate to negate; must not be {@code null}
     * @param <A1>  the type of the first argument
     * @param <A2>  the type of the second argument
     * @param <A3>  the type of the third argument
     * @return a predicate representing {@code NOT pred3}
     *
     * @since 1.0.0
     */
    static <A1, A2, A3> Pred3<A1, A2, A3> not(final @NonNull Pred3<? super A1, ? super A2, ? super A3> pred3) {
        Objects.requireNonNull(pred3);
        return (a1, a2, a3) -> !pred3.test(a1, a2, a3);
    }

    /**
     * <div>
     *     <p>
     *         Evaluates this predicate for the given inputs.
     *     </p>
     *     <p>
     *         Contract: all inputs must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a1 the first input; must not be {@code null}
     * @param a2 the second input; must not be {@code null}
     * @param a3 the third input; must not be {@code null}
     * @return {@code true} or {@code false}
     *
     * @since 1.0.0
     */
    @Pure
    boolean test(final @NonNull A1 a1,
                 final @NonNull A2 a2,
                 final @NonNull A3 a3);

    /**
     * <div>
     *     <p>
     *         Returns the logical negation of this predicate.
     *     </p>
     * </div>
     *
     * @return the negated predicate
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> negate() {
        return not(this);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical conjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a1, a2, a3)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this AND other}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) && other.test(a1, a2, a3));
    }

    /**
     * <div>
     *     <p>
     *         Logical conjunction with a {@link BooleanSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2, a3)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this AND booleanSupplier}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) && booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Logical conjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2, a3)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this AND supplier.get()}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(final @NonNull Supplier<Boolean> supplier) {
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(@NonNull final Pred3<A1, A2, A3> other) {
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
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this AND booleanSupplier)}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return not(this.and(booleanSupplier));
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical disjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a1, a2, a3)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this OR other}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) || other.test(a1, a2, a3));
    }

    /**
     * <div>
     *     <p>
     *         Logical disjunction with a {@link BooleanSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2, a3)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this OR booleanSupplier}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) || booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Logical disjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a1, a2, a3)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this OR supplier.get()}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(final @NonNull Supplier<Boolean> supplier) {
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(@NonNull final Pred3<A1, A2, A3> other) {
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
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code NOT (this OR booleanSupplier)}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return not(this.or(booleanSupplier));
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(final @NonNull Supplier<Boolean> supplier) {
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(@NonNull final Pred3<A1, A2, A3> other) {
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
     * @param booleanSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return a predicate representing {@code this XOR booleanSupplier}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, "booleanSupplier");
        return (this.or(booleanSupplier)).and(not(this.and(booleanSupplier)));
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
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(final @NonNull Supplier<Boolean> supplier) {
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
     * @return a binary predicate {@code f: A2 × A3 → {true, false}}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A2, A3> partial1(@NonNull final Supplier<A1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a2, a3) -> this.test(supplier.get(), a2, a3);
    }

    /**
     * <div>
     *     <p>
     *         Partially evaluates this predicate by supplying the second argument.
     *     </p>
     * </div>
     *
     * @param supplier supplies the second argument; must not be {@code null}
     * @return a binary predicate {@code f: A1 × A3 → {true, false}}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A3> partial2(@NonNull final Supplier<A2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a1, a3) -> this.test(a1, supplier.get(), a3);
    }

    /**
     * <div>
     *     <p>
     *         Partially evaluates this predicate by supplying the third argument.
     *     </p>
     * </div>
     *
     * @param supplier supplies the third argument; must not be {@code null}
     * @return a binary predicate {@code f: A1 × A2 → {true, false}}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> partial3(@NonNull final Supplier<A3> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a1, a2) -> this.test(a1, a2, supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Delays the evaluation of this predicate until all three inputs are supplied
     *         by the given {@link Supplier}s.
     *     </p>
     *     <p>
     *         Contract: suppliers must not be {@code null} and must not supply {@code null} values.
     *     </p>
     * </div>
     *
     * @param supplier1 supplies the first input; must not be {@code null}
     * @param supplier2 supplies the second input; must not be {@code null}
     * @param supplier3 supplies the third input; must not be {@code null}
     * @return a {@link Callable} that evaluates the predicate when called
     * @throws NullPointerException if any supplier is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Boolean> defer(final @NonNull Supplier<A1> supplier1,
                                    final @NonNull Supplier<A2> supplier2,
                                    final @NonNull Supplier<A3> supplier3) {
        Objects.requireNonNull(supplier1, nullValue("supplier1"));
        Objects.requireNonNull(supplier2, nullValue("supplier2"));
        Objects.requireNonNull(supplier3, nullValue("supplier3"));
        return () -> this.test(
                        Objects.requireNonNull(supplier1.get(), nullSupplied()),
                        Objects.requireNonNull(supplier2.get(), nullSupplied()),
                        Objects.requireNonNull(supplier3.get(), nullSupplied())
                     );
    }

}
