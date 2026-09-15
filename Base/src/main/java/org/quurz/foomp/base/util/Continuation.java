package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.H2Monadic;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.cantCast;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Models a continuation in continuation‑passing style (CPS). A continuation stores a function
 *     of the shape {@code runCont: (A -> R) -> R}, i.e., it expects a consumer for the produced
 *     value {@code A} and returns a result of type {@code R}.
 *   </p>
 *   <p>
 *     This corresponds to Haskell’s {@code Cont r a}, where the parameter order here is {@code <A, R>}
 *     (value, result). In other words: {@code Continuation<A, R>} ≙ {@code Cont r a} with {@code r = R}
 *     and {@code a = A}.
 *   </p>
 *   <p>
 *     The type supports the usual functor/applicative/monad operations:
 *     {@code map}, {@code applyTo} (applicative application), {@code flatMap} (flatMap), and {@code apply}
 *     to run the continuation with a final computation.
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     Null‑checks are enforced to keep Java usage safe while preserving CPS semantics.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   // 1) Plain CPS pipeline: map and flatMap
 *   // runCont: (A -> R) -> R, with A = Integer, R = Integer
 *   var cont = Continuation.<Integer, Integer>pureContinuation(5)
 *       .map(i -> i * 2)            // 10
 *       .flatMap(i -> Continuation.pureContinuation(i + 1)); // 11
 *
 *   // Apply the final continuation (A -> R). Here we choose identity to get the produced value:
 *   var result = cont.apply(x -> x); // 11
 *
 *   // 2) Early exit with call/cc (call-with-current-continuation)
 *   // If a condition holds, invoke k(...) to short-circuit and return immediately.
 *   var earlyExit = Continuation.<Integer, Integer>pureContinuation(5)
 *       .flatMap(i -> Continuation.callCurrentCont(
 *           (java.util.function.Function<Integer, Continuation<Integer, Integer>>) k -> {
 *               if (i > 0) {
 *                   // abort the rest of the computation and return 42 now
 *                   return k.apply(42);
 *               } else {
 *                   // proceed normally
 *                   return Continuation.pureContinuation(i);
 *               }
 *           }
 *       ));
 *
 *   var exited = earlyExit.apply(x -> x); // 42
 *   }</pre>
 * </div>
 *
 * @param <A> the produced value type (the input of the final continuation function)
 * @param <R> the overall result type of the CPS computation
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public class Continuation<A, R>
        implements H2Monadic<Continuation.µ, A, R>,
                   Function<Function<A, R>, R>,
                   Higher2<Continuation.µ, A, R> {

    /**
     * <div>
     *   <p>
     *     Witness type for {@code Continuation} in the higher‑kinded encoding.
     *     Used to keep the constructor identity across {@code Higher2}-based APIs.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() { } }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher2} value to a concrete {@code Continuation}.
     *     Useful when working with higher‑kinded APIs.
     *   </p>
     * </div>
     *
     * @param higher a {@link Higher2} value to be viewed as {@code Continuation}; must not be {@code null}
     * @param <A>    the produced value type
     * @param <R>    the result type
     * @return the same instance, viewed as {@code Continuation}
     * @throws NullPointerException     if {@code higher} is {@code null}
     * @throws IllegalArgumentException if {@code higher} is not an instance of {@code Continuation}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A, R> Continuation<A, R> narrow(final @NonNull Higher2<? extends µ, A, R> higher) {
        Objects.requireNonNull(higher, nullValue("higher"));
        if (higher instanceof Continuation<?, ?> continuation) {
            return (Continuation<A, R>) continuation;
        } else {
            throw new IllegalArgumentException(cantCast("higher", Continuation.class));
        }
    }

    /**
     * <div>
     *   <p>
     *     Constructs a {@code Continuation} from a CPS runner function {@code runCont}.
     *     The runner consumes a continuation {@code (A -> R)} and produces {@code R}.
     *   </p>
     * </div>
     *
     * @param runCont the CPS runner, of type {@code (A -> R) -> R}; must not be {@code null}
     * @param <A>     the produced value type
     * @param <R>     the result type
     * @return a new {@code Continuation}
     * @throws NullPointerException if {@code runCont} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public static <R, A> Continuation<A, R> continuation(final @NonNull Function<Function<A, R>, R> runCont) {
        Objects.requireNonNull(runCont, nullValue("runCont"));
        return new Continuation<>(f -> Objects.requireNonNull(runCont.apply(f), nullResultFrom("runCont")));
    }

    /**
     * <div>
     *   <p>
     *     Lifts a plain value into a {@code Continuation}: {@code a ↦ (k -> k(a))}.
     *   </p>
     * </div>
     *
     * @param value the value to applyTo; must not be {@code null}
     * @param <A>   the produced value type
     * @param <R>   the result type (arbitrary for the literal continuation)
     * @return a continuation that immediately applies {@code value} to the provided continuation
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public static <R, A> Continuation<A, R> pureContinuation(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Continuation<>(f -> Objects.requireNonNull(f.apply(value), nullResultFrom("runCont")));
    }

    private final Function<Function<A, R>, R> runCont;

    private Continuation(final @NonNull Function<Function<A, R>, R> runCont) {
        this.runCont
            = runCont;
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the produced value while preserving the CPS structure.
     *     Defined as {@code map g = k -> runCont (a -> k (g a))}.
     *   </p>
     * </div>
     *
     * @param <B>            the target value type
     * @param transformation mapping {@code A -> B}; must not be {@code null} and must not return {@code null}
     * @return a continuation producing {@code B} within the same {@code R}
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Continuation<>(
            f -> this.runCont.apply(
                f.compose(a -> {
                    Objects.requireNonNull(a, nullValue("a"));
                    return Objects.requireNonNull(transformation.apply(a), nullResultFrom("transformation"));
                })
            )
        );
    }

    /**
     * <div>
     *   <p>
     *     Applicative application (applyTo): applies a continuation holding a function to this continuation’s value.
     *     Conceptually, {@code ap cf cx = k -> cf.run (t -> cx.map(t).run(k))}.
     *   </p>
     * </div>
     *
     * @param <B>            the target value type
     * @param transformation a continuation producing a function {@code A -> B}; must not be {@code null}
     * @return a continuation producing {@code B}
     * @throws NullPointerException if {@code transformation} is {@code null} or its function yields {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> applyTo(@NonNull Higher2<? extends µ, Function<A, B>, R> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        final var continuation
            = narrow(transformation);
        return new Continuation<>(f -> continuation.apply(t -> this.map(t).apply(f)));
    }

    /**
     * <div>
     *   <p>
     *     Monadic flatMap (flatMap): sequences CPS computations. Defined as
     *     {@code flatMap f = k -> runCont (a -> f(a).runCont(k))}.
     *   </p>
     * </div>
     *
     * @param <B>            the target value type
     * @param transformation {@code A -> Continuation<B, R>}; must not be {@code null}
     * @return a continuation producing {@code B}
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Continuation<B, R> flatMap(@NonNull Function<A, ? extends Higher2<? extends µ, B, R>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Continuation<>(f -> this.runCont.apply(a -> narrow(transformation.apply(a)).map(Function.identity()).apply(f)));
    }

    /**
     * <div>
     *   <p>
     *     Runs this continuation with the provided final continuation function (the “consumer” of {@code A}).
     *   </p>
     * </div>
     *
     * @param computation the final continuation {@code A -> R}; must not be {@code null}
     * @return the computed {@code R}
     * @throws NullPointerException if {@code computation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull R apply(final @NonNull Function<A, R> computation) {
        Objects.requireNonNull(computation, nullValue("computation"));
        return Objects.requireNonNull(this.runCont.apply(computation), nullResultFrom("computation"));
    }

    /**
     * <div>
     *   <p>
     *     Sequences this continuation and then proceeds with the given continuation, ignoring this value.
     *     Equivalent to {@code this.flatMap(_ -> continuation)}.
     *   </p>
     * </div>
     *
     * @param continuation the next continuation to run; must not be {@code null}
     * @param <B>          the next value type
     * @return a continuation that first runs {@code this}, then {@code continuation}
     * @throws NullPointerException if {@code continuation} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public <B> Continuation<B, R> then(final @NonNull Continuation<B, R> continuation) {
        Objects.requireNonNull(continuation, nullValue("continuation"));
        return this.flatMap(ignored -> continuation);
    }

    /**
     * <div>
     *   <p>
     *     call/cc (call-with-current-continuation) for {@code Continuation}: provides an escape continuation {@code k}
     *     to the computation, allowing early exit or non-local control flow.
     *   </p>
     *   <p>
     *     Usage sketch:
     *     {@code callCurrentCont(k -> /* build a computation that may invoke k(a) to short-circuit *\/ ...)}
     *   </p>
     * </div>
     *
     * @param computation a function receiving the current continuation {@code k} and producing a computation
     * @param <R>         the result type
     * @param <A>         the input value type for {@code k}
     * @param <B>         the produced value type of the resulting continuation
     * @return a continuation that runs {@code computation} with the current continuation
     * @throws NullPointerException if {@code computation} is {@code null}
     *
     * @since 1.0.0
     */
    public static <R, A, B> Continuation<B, R> callCurrentCont(final @NonNull Function<Function<A, Continuation<B, R>>, Continuation<A, R>> computation) {
        Objects.requireNonNull(computation, nullValue("computation"));
        //noinspection unchecked
        return (Continuation<B, R>) new Continuation<>((final Function<Object, R> k) -> computation.apply(a -> new Continuation<>(ignored -> k.apply(a))).apply((Function<A, R>) k));
    }

}
