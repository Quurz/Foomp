package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A functional interface for defining equality operations.
 *         Inspired by Haskell's <code>Eq</code> typeclass, this interface allows
 *         for flexible implementation of equality criteria.
 *     </p>
 *     <p>
 *         This interface supports:
 *         <ul>
 *             <li>Type-safe equality, defined by the method {@link #eq(Eq)}.</li>
 *             <li>Custom equality using {@link BiPredicate}, defined by the method {@link #eq(BiPredicate, Eq)}.</li>
 *             <li>Standard hash value calculation, provided by the method {@link #hash()}.</li>
 *             <li>Custom hash value calculation, defined by the method {@link #hash(Function)}.</li>
 *         </ul>
 *     </p>
 * </div>
 *
 * @param <SELF> The type implementing this interface.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Eq<SELF extends Eq<?>> {

    /**
     * <div>
     *     <p>
     *         Checks if the current object equals the given object.
     *     </p>
     * </div>
     *
     * @param other The object to check equality with.
     * @return <code>true</code> if both objects are equal, <code>false</code> otherwise.
     *
     * @throws NullPointerException If <code>other</code> is <code>null</code>.
     *
     * @since 1.0.0
     */
    boolean eq(final @NonNull SELF other);

    /**
     * <div>
     *     <p>
     *         Performs an equality check using custom comparison logic.
     *     </p>
     * </div>
     *
     * @param equals   A custom comparison logic represented by a {@link BiPredicate}.
     * @param other  The object to check equality with. Must not be <code>null</code>.
     *
     * @return <code>true</code> if both objects are considered equal according to the provided
     *         comparison logic; <code>false</code> otherwise.
     *
     * @throws NullPointerException If either <code>equals</code> or <code>other</code> is <code>null</code>.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    default boolean eq(final @NonNull BiPredicate<SELF, SELF> equals,
                       final @NonNull Eq<SELF> other) {
        Objects.requireNonNull(equals, nullValue("equals"));
        Objects.requireNonNull(other, nullValue("other"));
        return equals.test((SELF) this, (SELF) other);
    }

    /**
     * <div>
     *     <p>
     *         Calculates the hash value for this object using the standard hash implementation.
     *     </p>
     * </div>
     *
     * @return A hash value based on the standard hash implementation of {@link Objects#hash(Object...)}.
     *
     * @since 1.0.0
     */
    default int hash() {
        return this.hashCode();
    }

    /**
     * <div>
     *     <p>
     *         Calculates the hash value of this object using a custom hash function.
     *     </p>
     * </div>
     *
     * @param hashFunction A custom function that calculates the hash value. Must not be <code>null</code>.
     *
     * @return The hash value calculated by the <code>hashFunction</code>.
     *
     * @throws NullPointerException If <code>hashFunction</code> or its return value is <code>null</code>.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    default int hash(final @NonNull Function<SELF, Integer> hashFunction) {
        Objects.requireNonNull(hashFunction, nullValue("hashFunction"));
        return Objects.requireNonNull(hashFunction.apply((SELF) this), nullResult());
    }

}
