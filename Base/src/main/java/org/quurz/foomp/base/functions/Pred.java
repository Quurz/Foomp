package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.functions.MemoisingPred.memoisingPred;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface describing a boolean condition over values of type {@code A}.
 *     </p>
 *     <p>
 *         Extends the standard {@link Predicate} with additional logical combinators such as
 *         NAND, NOR, and XOR, and provides utilities for composing predicates via methods like
 *         {@code and}, {@code or}, and {@code negate}.
 *     </p>
 *     <p>
 *         Contract: inputs must not be {@code null}. Combinators use short‑circuit evaluation
 *         where applicable (e.g., {@code and}, {@code or}).
 *     </p>
 * </div>
 *
 * @param <A> the input type checked by this predicate
 *
 * @see Predicate
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred<A>
        extends Predicate<A> {

    /**
     * <div>
     *     <p>
     *         Returns a predicate that always yields {@code true}, regardless of the input value.
     *     </p>
     *     <p>
     *         Contract: the input must not be {@code null}. A {@link NullPointerException} is thrown
     *         if a {@code null} input is provided.
     *     </p>
     * </div>
     *
     * @param <A> the input type accepted by the predicate
     * @return a predicate that always returns {@code true}
     * @throws NullPointerException if the input is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Pred<A> alwaysTrue() {
        return object -> {
            Objects.requireNonNull(object, nullValue("object"));
            return true;
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns a predicate that always yields {@code false}, regardless of the input value.
     *     </p>
     *     <p>
     *         Contract: the input must not be {@code null}. A {@link NullPointerException} is thrown
     *         if a {@code null} input is provided.
     *     </p>
     * </div>
     *
     * @param <A> the input type accepted by the predicate
     * @return a predicate that always returns {@code false}
     * @throws NullPointerException if the input is {@code null}
     *
     * @since 1.0.0
     */
    static <A>Pred<A> alwaysFalse() {
        return object -> {
            Objects.requireNonNull(object, nullValue("object"));
            return false;
        };
    }

    /**
     * <div>
     *     <p>
     *         Wraps the given {@link Predicate} into a {@code Pred}.
     *     </p>
     *     <p>
     *         Contract: {@code predicate} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param predicate the predicate to wrap; must not be {@code null}
     * @param <A>       the input type
     * @return a {@code Pred} delegating to {@code predicate}
     *
     * @throws NullPointerException if {@code predicate} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Pred<A> pred(@NonNull final Predicate<A> predicate) {
        Objects.requireNonNull(predicate);
        return predicate::test;
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical negation of the given {@code Pred}.
     *     </p>
     *     <p>
     *         Contract: {@code predicate} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param predicate the predicate to negate; must not be {@code null}
     * @param <A>       the input type
     * @return a predicate representing {@code NOT predicate}
     *
     * @throws NullPointerException if {@code predicate} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Pred<A> not(@NonNull final Predicate<A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return a -> !predicate.test(a);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical conjunction of all provided predicates:
     *         {@code first AND second AND ... others}.
     *     </p>
     *     <p>
     *         Contract: all predicates must not be {@code null}. Short‑circuits from left to right.
     *     </p>
     * </div>
     *
     * @param first  the first predicate; must not be {@code null}
     * @param second the second predicate; must not be {@code null}
     * @param others additional predicates; must not be {@code null}
     * @param <A>    the input type
     * @return the conjunction of all predicates
     *
     * @throws NullPointerException if any predicate array or element is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> and(final @NonNull Pred<A> first,
                           final @NonNull Pred<A> second,
                           final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.and(second);
        for (final var pred : others) {
            result
                = result.and(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical disjunction of all provided predicates:
     *         {@code first OR second OR ... others}.
     *     </p>
     *     <p>
     *         Contract: all predicates must not be {@code null}. Short‑circuits from left to right.
     *     </p>
     * </div>
     *
     * @param first  the first predicate; must not be {@code null}
     * @param second the second predicate; must not be {@code null}
     * @param others additional predicates; must not be {@code null}
     * @param <A>    the input type
     * @return the disjunction of all predicates
     *
     * @throws NullPointerException if any predicate array or element is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> or(final @NonNull Pred<A> first,
                          final @NonNull Pred<A> second,
                          final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.or(second);
        for (final var pred : others) {
            result
                = result.or(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Returns the exclusive OR of all provided predicates, implemented as a left‑to‑right fold of XOR:
     *         {@code (((first XOR second) XOR ...) XOR others[n])}.
     *     </p>
     *     <p>
     *         Contract: all predicates must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param first  the first predicate; must not be {@code null}
     * @param second the second predicate; must not be {@code null}
     * @param others additional predicates; must not be {@code null}
     * @param <A>    the input type
     * @return the XOR combination of all predicates
     *
     * @throws NullPointerException if any predicate array or element is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> xor(final @NonNull Pred<A> first,
                           final @NonNull Pred<A> second,
                           final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("value1"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.xor(second);
        for (final var pred : others) {
            result
                = result.xor(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Evaluates this predicate for the given input.
     *     </p>
     *     <p>
     *         Contract: {@code a} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param a the input value; must not be {@code null}
     * @return {@code true} or {@code false}
     *
     * @since 1.0.0
     */
    @Override
    @Pure
    boolean test(final @NonNull A a);

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
    @Override
    @NonNull
    default Pred<A> negate() {
        return not(this);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical conjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a)} is {@code true}.
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
    default Pred<A> and(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return a -> (this.test(a) && other.test(a));
    }

    /**
     * <div>
     *     <p>
     *         Performs a logical conjunction with a {@code BoolSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a)} is {@code true}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return the conjunction
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> and(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return a -> (this.test(a) && boolSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Performs a logical conjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return the conjunction
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> and(final @NonNull Supplier<Boolean> supplier) {
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
    default Pred<A> nand(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.and(other));
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NAND with a {@link BooleanSupplier}.
     *         Equivalent to {@code NOT (this AND boolSupplier)}; short‑circuits accordingly.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return the NAND combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nand(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.and(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NAND with a {@link Supplier}{@code <Boolean>}.
     *         Equivalent to {@code NOT (this AND supplier.get())}; short‑circuits accordingly.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return the NAND combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nand(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical disjunction of this predicate and {@code other}.
     *         Short‑circuits: {@code other} is evaluated only if {@code this.test(a)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code this OR other}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> or(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return a -> (this.test(a) || other.test(a));
    }

    /**
     * <div>
     *     <p>
     *         Performs a logical disjunction with a {@link BooleanSupplier}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return the disjunction
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> or(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return a -> (this.test(a) || boolSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         Performs a logical disjunction with a {@link Supplier}{@code <Boolean>}.
     *         Short‑circuits: the supplier is invoked only if {@code this.test(a)} is {@code false}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return the disjunction
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> or(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
            = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.or(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NOR of this predicate and {@code other} (negation of disjunction).
     *     </p>
     * </div>
     *
     * @param other the predicate to combine with; must not be {@code null}
     * @return a predicate representing {@code NOT (this OR other)}
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.or(other));
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NOR with a {@link BooleanSupplier}.
     *         Equivalent to {@code NOT (this OR boolSupplier)}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return the NOR combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.or(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         Returns the logical NOR with a {@link Supplier}{@code <Boolean>}.
     *         Equivalent to {@code NOT (this OR supplier.get())}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return the NOR combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Returns the exclusive OR (XOR) of this predicate and {@code other}.
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
    default Pred<A> xor(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (this.or(other)).and(not(this.and(other)));
    }

    /**
     * <div>
     *     <p>
     *         Returns the exclusive OR (XOR) with a {@link BooleanSupplier}.
     *         Implemented as {@code (this OR boolSupplier) AND NOT (this AND boolSupplier)}.
     *     </p>
     * </div>
     *
     * @param boolSupplier supplies the right‑hand boolean; must not be {@code null}
     * @return the XOR combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> xor(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (this.or(boolSupplier)).and(not(this.and(boolSupplier)));
    }

    /**
     * <div>
     *     <p>
     *         Returns the exclusive OR (XOR) with a {@link Supplier}{@code <Boolean>}.
     *         Implemented as {@code (this OR supplier.get()) AND NOT (this AND supplier.get())}.
     *     </p>
     * </div>
     *
     * @param supplier supplies the right‑hand boolean; must not be {@code null}
     * @return the XOR combination
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> xor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier);
        final BooleanSupplier boolSupplier
            = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.xor(boolSupplier);
    }

    default MemoisingPred<A> memoise() {
        return memoisingPred(this);
    }

}
