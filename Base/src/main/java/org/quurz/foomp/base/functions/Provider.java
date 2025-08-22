package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.base.types.Value;
import org.quurz.foomp.base.util.Nothing;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         The {@code Provider} interface represents a functional type that yields a value of type {@code A}
 *         and exposes a focused set of functional programming utilities.
 *     </p>
 *     <p>
 *         It combines traits typically associated with functors and monads to offer a versatile API
 *         for providing and transforming values. As a functional interface, {@code Provider} can be used
 *         directly with lambdas or method references. Common use cases include value encapsulation,
 *         lazy evaluation, and reusable transformations in a functional style.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     </p>
 * </div>
 *
 * <h2>Features</h2>
 * <ul>
 *     <li>Implements {@link Supplier} to provide values.</li>
 *     <li>Supports monadic operations such as {@code map}, {@code lift}, and {@code bind}.</li>
 *     <li>Integrates with {@link Fun}, {@link Value}, and {@link Higher1}.</li>
 *     <li>Provides extras like {@code copy}, {@code unwind}, and {@code transmogrify}.</li>
 * </ul>
 *
 * @param <A> the provided value type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
@FunctionalInterface
public interface Provider<A>
        extends Monadic<Provider.µ, A>,
                Copyable<Provider<A>>,
                Unwindable<Provider<A>>,
                Transmogrifyable<Provider<A>>,
                Fun<Nothing, A>,
                Value<A>,
                Higher1<Provider.µ, A> {

    /**
     * <div>
     *     <p>
     *         The witness type for {@code Provider}.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} instance to a {@code Provider}.
     *     </p>
     *     <p>
     *         Contract: {@code wide} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded value to narrow; must not be {@code null}
     * @param <A>  the provided value type
     * @return a {@code Provider} instance
     * @throws NullPointerException if {@code wide} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static <A> Provider<A> narrow(final @NonNull Higher1<? extends Provider.µ, A> wide) {
        return (Provider<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code Provider} that always returns the given value.
     *     </p>
     *     <p>
     *         Contract: {@code value} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param value the value to provide; must not be {@code null}
     * @param <A>   the value type
     * @return a {@code Provider} yielding {@code value}
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Provider<A> provider(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return () -> value;
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code Provider} that obtains its value from the given {@link Supplier}.
     *     </p>
     *     <p>
     *         Contract: {@code supplier} must not be {@code null} and must not supply {@code null}.
     *     </p>
     * </div>
     *
     * @param supplier the source of the value; must not be {@code null}
     * @param <A>      the provided value type
     * @return a {@code Provider} that pulls values from {@code supplier}
     * @throws NullPointerException if {@code supplier} is {@code null} or supplies {@code null}
     *
     * @since 1.0.0
     */
    static <A> Provider<A> providerFrom(final @NonNull Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return () -> Objects.requireNonNull(supplier.get(), nullSupplied());
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether a valid value is present.
     *     </p>
     * </div>
     *
     * @return always {@code true}; by definition a {@code Provider} carries a value
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return true;
    }

    /**
     * <div>
     *     <p>
     *         Returns the stored value.
     *     </p>
     * </div>
     *
     * @return the stored value; never {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    A get();

    /**
     * <div>
     *     <p>
     *         Applies this function to the given {@link Nothing} placeholder and returns the stored value.
     *     </p>
     *     <p>
     *         Contract: {@code nothing} must not be {@code null}; the stored value must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param nothing the {@link Nothing} placeholder; must not be {@code null}
     * @return the stored value
     * @throws NullPointerException if {@code nothing} is {@code null} or if the stored value is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default A apply(final @NonNull Nothing nothing) {
        Objects.requireNonNull(nothing, nullValue("nothing"));
        return Objects.requireNonNull(this.get(), nullSupplied());
    }

    /**
     * <div>
     *     <p>
     *         Maps the stored value using the given transformation and returns a new {@code Provider}.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the mapping function; must not be {@code null}
     * @param <B>            the resulting value type
     * @return a new {@code Provider} with the transformed value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return () -> Objects.requireNonNull(transformation.apply(this.get()), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Lifts a function into the {@code Provider} context and applies it to the stored value.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must carry a non‑null function.
     *     </p>
     * </div>
     *
     * @param transformation a {@code Provider}-wrapped function to apply; must not be {@code null}
     * @param <B>            the resulting value type
     * @return a new {@code Provider} with the transformed value
     * @throws NullPointerException if {@code transformation} is {@code null} or carries a {@code null} function
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> lift(final @NonNull Higher1<? extends Provider.µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation);
        final var narrowed
            = narrow(transformation);
        return this.map(narrowed.get());
    }

    /**
     * <div>
     *     <p>
     *         Applies a monadic transformation and returns the resulting {@code Provider}.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null}; the returned higher‑kinded value
     *         must narrow to a non‑null {@code Provider} whose {@code get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the monadic transformation
     * @param <B>            the resulting value type
     * @return a new {@code Provider} with the transformed value
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <B> Provider<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return this.map(a -> narrow(transformation.apply(a)).get());
    }

    /**
     * <div>
     *     <p>
     *         Creates an exact copy of this {@code Provider}.
     *     </p>
     *     <p>
     *         Semantics: this default implementation returns a new instance delegating to {@code get()}.
     *         Implementations may override to control evaluation (lazy vs. eager) or copying strategy.
     *     </p>
     * </div>
     *
     * @return a new {@code Provider} instance yielding the same value
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Provider<A> copy() {
        final var self
            = this;
        return self::get;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Transforms this {@code Provider} using the given function.
     *     </p>
     *     <p>
     *         Contract: {@code transmogrifier} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transmogrifier the transformation function; must not be {@code null}
     * @param <T>            the result type
     * @return the transformed object
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default <T> T transmogrify(final @NonNull Function<? super Provider<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         Unwinds this {@code Provider} and returns an eagerly evaluated representation.
     *     </p>
     *     <p>
     *         Contract: the stored value must not be {@code null}.
     *     </p>
     * </div>
     *
     * @return a new {@code Provider} containing the current value
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    default Provider<A> unwind() {
        final var value
            = this.get();
        return () -> value;
    }

}
