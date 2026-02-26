package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Rank-1 applicative functor interface: applies a function inside the context to
 *         the carried value, producing a value of the same constructor shape.
 *     </p>
 *     <p>
 *         This interface is the rank-1 analog to Haskell's {@code Applicative} functor class.
 *         Conceptually, this corresponds to applying {@code f (A -> B)} to
 *         {@code f A} to obtain {@code f B}.
 *     </p>
 *     <p>
 *         Contract: the provided higher‑kinded value must not be {@code null}, must not contain
 *         a {@code null} function, and the result must not be {@code null}.
 *     </p>
 * </div>
 *
 * @param <WT> the witness type representing the higher‑kinded constructor
 * @param <A>  the input type consumed by the function
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Appliable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Applies a function {@code A -> B} inside the context to the carried value,
     *         yielding a value of the same constructor shape with {@code B}.
     *     </p>
     * </div>
     *
     * @param transformation a higher‑kinded value carrying the function {@code A -> B}; must not be {@code null}
     * @param <B>            the new carried type after applying the function
     * @return a {@code Higher1<WT, B>} value; never {@code null}
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> applyTo(final @NonNull Higher1<? extends WT, ? extends Function<? super A, ? extends B>> transformation);

}
