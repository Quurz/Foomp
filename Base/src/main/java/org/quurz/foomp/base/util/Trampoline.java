package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Value;

import java.util.Objects;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;

/**
 * <div>
 *     A bouncy castle!
 *     <br/>
 *     (A playful nod: this “trampoline” lets your recursion bounce safely without blowing the stack.)
 * </div>
 *
 * <div>
 *     <p>
 *         Turns (potentially deep) recursion into a stack‑safe iteration.
 *     </p>
 *     <p>
 *         <cite>To iterate is human, to recurse divine.</cite>
 *     </p>
 *     <p>
 *         Example usage illustrating how it works:
 *     </p>
 *     <pre>{@code
 *          private void play() throws Exception {
 *              final var param = BigInteger.valueOf(100000);
 *              final var result = this.factorial(param);
 *              LOGGER.info("factorial({}) = {}", param, result);
 *          }
 *
 *          private BigInteger factorial(final BigInteger param) {
 *              return this._factorial(param, BigInteger.ONE).get();
 *          }
 *
 *          private Trampoline<BigInteger> _factorial(final BigInteger param,
 *                                                    final BigInteger accu) {
 *              if (param.equals(BigInteger.ONE)) {
 *                  return Trampoline.done(accu);
 *              } else {
 *                  return Trampoline.more(() -> _factorial(param.subtract(BigInteger.ONE), accu.multiply(param)));
 *              }
 *          }
 *     }</pre>
 *     <div>
 *         A naive recursive implementation would easily end in a StackOverflowError… 😉
 *     </div>
 *     <p>
 *         Performance note:
 *         <ul>
 *           <li>Each intermediate step typically allocates a {@code More} instance and a {@code Supplier} capturing the next step.</li>
 *           <li>Very long runs can create many small objects and put considerable pressure on the heap and GC.</li>
 *           <li>If a hotspot is performance‑critical and predictable, consider an explicit iterative version.</li>
 *         </ul>
 *     </p>
 * </div>
 *
 * @param <T> the result type produced by the trampoline
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface Trampoline<T>
        extends Value<T>
        permits Trampoline.More,
                Trampoline.Done {

    /**
     * <div>
     *     <p>
     *         Creates a trampoline representing a pending computation. Calling {@link #get()}
     *         will repeatedly evaluate the supplied next steps until a {@link Done} is reached.
     *     </p>
     *     <p>
     *         Performance note: each “bounce” usually allocates at least one object
     *         (this {@code More} plus its {@code Supplier}). Extremely deep computations may increase
     *         heap usage and GC activity compared to a hand‑written loop.
     *     </p>
     * </div>
     *
     * @param trampoline a supplier providing the next trampoline step; must not be {@code null}
     * @param <T>        the result type
     * @return a {@link Trampoline} representing a running computation
     *
     * @since 1.0.0
     */
    static <T> Trampoline<T> more(final @NonNull Supplier<Trampoline<T>> trampoline) {
        return new More<>(Objects.requireNonNull(trampoline));
    }

    /**
     * <div>
     *     <p>
     *         Creates a trampoline representing a completed computation yielding a result.
     *     </p>
     * </div>
     *
     * @param result the final result; must not be {@code null}
     * @param <T>    the result type
     * @return a {@link Trampoline} representing a finished computation
     *
     * @since 1.0.0
     */
    static <T> Trampoline<T> done(final @NonNull T result) {
        return new Done<>(Objects.requireNonNull(result));
    }

    /**
     * <div>
     *     <p>
     *         Indicates whether a result is present. Returns {@code true} for {@link Done}
     *         and {@code false} for {@link More}.
     *     </p>
     * </div>
     *
     * @return {@code true} if this is a {@link Done}, otherwise {@code false}
     *
     * @since 1.0.0
     */
    @Override
    default boolean isPresent() {
        return (this instanceof Trampoline.Done<T>);
    }

    /**
     * <div>
     *     <p>
     *         Represents a running computation. Holds a supplier for the next trampoline step.
     *     </p>
     * </div>
     *
     * @param <T> the result type
     *
     * @since 1.0.0
     */
    final class More<T>
            implements Trampoline<T> {

        private final Supplier<Trampoline<T>> trampoline;

        private More(final Supplier<Trampoline<T>> trampoline) {
            this.trampoline
                = trampoline;
        }

        /**
         * <div>
         *     <p>
         *         Evaluates the trampoline until a {@link Done} is reached and returns its result.
         *         The evaluation proceeds iteratively to avoid stack growth.
         *     </p>
         * </div>
         *
         * @return the final result
         *
         * @since 1.0.0
         */
        @Override
        public @NonNull T get() {
            var current
                = Objects.requireNonNull(this.trampoline.get(), nullResult());
            while (current instanceof Trampoline.More<T> more) {
                current = Objects.requireNonNull(more.trampoline.get(), nullResult());
            }
            return current.get();
        }

    }

    /**
     * <div>
     *     <p>
     *         Represents a completed computation that holds the final result.
     *     </p>
     * </div>
     *
     * @param <T> the result type
     *
     * @since 1.0.0
     */
    final class Done<T>
            implements Trampoline<T> {

        private final T result;

        private Done(final T result) {
            this.result
                = result;
        }

        /**
         * <div>
         *     <p>
         *         Indicates that a value is present for this {@link Done}.
         *     </p>
         * </div>
         *
         * @return always {@code true}
         *
         * @since 1.0.0
         */
        @Override
        public boolean isPresent() {
            return true;
        }

        /**
         * <div>
         *     <p>
         *         Returns the final result of the computation.
         *     </p>
         * </div>
         *
         * @return the result; never {@code null}
         */
        @Override
        public @NonNull T get() {
            return this.result;
        }

    }

}
