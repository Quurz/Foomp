package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.H2Monadic;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher2;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

/**
 * <div>
 *   <p>
 *     Models a classic State monad carrying a state {@code S} and producing a value {@code A}.
 *     Internally this is a function of shape {@code S -> (A, S)}.
 *   </p>
 *   <p>
 *     Semantics:
 *     <ul>
 *       <li><b>map</b>: transforms the produced value, threading the state through unchanged.</li>
 *       <li><b>applyTo</b> (applicative): threads state from the function value to this value:
 *           {@code s0 -> (f, s1) <- tf(s0); (a, s2) <- this(s1); result = (f(a), s2)}.</li>
 *       <li><b>bind</b> (flatMap): sequences computations, passing the new state to the next step:
 *           {@code s0 -> (a, s1) <- this(s0); next = f(a); result <- next(s1)}.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Contract: unless stated otherwise, inputs must not be {@code null} and results must not be {@code null}.
 *     Null‑checks are enforced to keep usage safe and predictable.
 *   </p>
 *   <p>
 *     Examples:
 *   </p>
 *   <pre>{@code
 *   // A simple state: an Integer "tick" that increments on each step
 *   Function<Integer, Tuple2<Boolean, Integer>> run = s -> tuple2((s % 2) == 0, s + 1);
 *   Stateful<Boolean, Integer> st = Stateful.stateful(run);
 *
 *   // map: transform the value; state is preserved
 *   var mapped = st.map(Object::toString);
 *   mapped.runState(1);   // -> ("false", 2)
 *
 *   // applyTo: function-in-stateful applied to current value, threading state correctly
 *   var tf = Stateful.stateful((Integer s) -> tuple2((Function<Boolean, String>) Object::toString, s));
 *   var lifted = st.applyTo(tf);
 *   lifted.runState(1);   // -> ("false", 2)
 *
 *   // bind: sequence and pass along the new state
 *   var bound = st.bind(b -> Stateful.stateOf(b ? "even" : "odd"));
 *   bound.runState(1);    // -> ("odd", 2)
 *
 *   // Utilities:
 *   Stateful.putState(42).execState(0); // -> 42
 *   Stateful.getState().execValue(5);   // -> 5
 *   }</pre>
 * </div>
 *
 * @param <A> the produced value type
 * @param <S> the state type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public class Stateful<A, S>
        implements H2Monadic<Stateful.µ, A, S>,
                   Higher2<Stateful.µ, A, S> {

    /**
     * <div>
     *   <p>
     *     Witness type for {@code Stateful} in the higher‑kinded encoding.
     *   </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *   <p>
     *     Narrows a {@link Higher2} value to a concrete {@code Stateful}.
     *   </p>
     * </div>
     *
     * @param higher the higher‑kinded value; must not be {@code null}
     * @param <A>    the produced value type
     * @param <S>    the state type
     * @return the same instance, viewed as {@code Stateful}
     * @throws NullPointerException if {@code higher} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A, S> Stateful<A, S> narrow(final Higher2<? extends Stateful.µ, A, S> higher) {
        return (Stateful<A, S>) higher;
    }

    /**
     * <div>
     *   <p>
     *     Unwraps a nested {@code Stateful} by one level (monadic join).
     *     Threads the intermediate state produced by the outer computation into the inner one.
     *   </p>
     * </div>
     *
     * @param wrapped the nested stateful computation; must not be {@code null}
     * @param <A>     the produced value type
     * @param <S>     the state type
     * @return a flattened {@code Stateful<A,S>}
     * @throws NullPointerException if {@code wrapped} is {@code null} or any intermediate result is {@code null}
     *
     * @since 1.0.0
     */
    public static <A, S> Stateful<A, S> unwrap(final @NonNull Higher2<? extends Stateful.µ, ? extends Higher2<? extends Stateful.µ, A, S>, S> wrapped) {
        Objects.requireNonNull(wrapped, nullValue("wrapped"));
        final var outer
            = narrow(wrapped);
        return new Stateful<>(state -> {
            final Tuple2<? extends Higher2<? extends µ, A, S>, S> innerAndState
                = outer.runState(state);
            final var inner
                = narrow(innerAndState.get());
            final var state1
                = innerAndState.get2();
            return inner.runState(state1);
        });
    }

    /**
     * <div>
     *   <p>
     *     Creates a {@code Stateful} from a state transition function.
     *   </p>
     * </div>
     *
     * @param runState the state function {@code S -> (A,S)}; must not be {@code null}
     * @param <S>      the state type
     * @param <A>      the produced value type
     * @return a new {@code Stateful}
     * @throws NullPointerException if {@code runState} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A, S> Stateful<A, S> stateful(final @NonNull Function<S, Tuple2<A, S>> runState) {
        Objects.requireNonNull(runState, nullValue("runState"));
        return new Stateful<>(runState);
    }

    /**
     * <div>
     *   <p>
     *     Lifts a plain value into {@code Stateful}: {@code a ↦ (s -> (a, s))}.
     *   </p>
     * </div>
     *
     * @param value the value; must not be {@code null}
     * @param <S>   the state type
     * @param <A>   the produced value type
     * @return a {@code Stateful} that returns {@code value} without changing the state
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A, S> Stateful<A, S> stateOf(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Stateful<>(state -> tuple2(value, state));
    }

    /**
     * <div>
     *   <p>
     *     Returns a {@code Stateful} that yields the current state as value, without changing it.
     *   </p>
     * </div>
     *
     * @param <S> the state type
     * @return a {@code Stateful<S,S>} returning the current state
     *
     * @since 1.0.0
     */
    public static <S> Stateful<S, S> getState() {
        return new Stateful<>(state -> tuple2(state, state));
    }

    /**
     * <div>
     *   <p>
     *     Returns a {@code Stateful} that updates the state using the given modifier.
     *     The produced value is {@link Nothing#nothing}.
     *   </p>
     * </div>
     *
     * @param modifier state transformation; must not be {@code null} and must not return {@code null}
     * @param <S>      the state type
     * @return a {@code Stateful<Nothing,S>} with the modified state
     * @throws NullPointerException if {@code modifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public static <S> Stateful<Nothing, S> modifyState(final @NonNull Function<? super S, ? extends S> modifier) {
        Objects.requireNonNull(modifier, nullValue("modifier"));
        return new Stateful<>(state -> tuple2(nothing, Objects.requireNonNull(modifier.apply(state), nullResult())));
    }

    /**
     * <div>
     *   <p>
     *     Returns a {@code Stateful} that sets the state to a fixed value.
     *     The produced value is {@link Nothing#nothing}.
     *   </p>
     * </div>
     *
     * @param state the new state; must not be {@code null}
     * @param <S>   the state type
     * @return a {@code Stateful<Nothing,S>} that stores {@code state}
     * @throws NullPointerException if {@code state} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public static <S> Stateful<Nothing, S> putState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return new Stateful<>(_$ -> tuple2(nothing, state));
    }

    private final Function<S, Tuple2<A, S>> runState;

    private Stateful(final Function<S, Tuple2<A, S>> runState) {
        this.runState
            = runState;
    }

    /**
     * <div>
     *   <p>
     *     Functor map: transforms the produced value, threading the state through unchanged.
     *   </p>
     * </div>
     *
     * @param transformation value transformation; must not be {@code null} and must not return {@code null}
     * @param <B>            the new value type
     * @return a {@code Stateful<B,S>} with transformed value
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull <B> Stateful<B, S> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Stateful<>(state -> {
            final Tuple2<A, S> resultAndState
                = this.runState.apply(state);
            final var result
                = resultAndState.get();
            final var newState
                = resultAndState.get2();
            return tuple2(Objects.requireNonNull(transformation.apply(result), nullResultFrom("transformation")), newState);
        });
    }

    /**
     * <div>
     *   <p>
     *     Applicative application: applies a function carried in {@code transformation} to
     *     the value of this stateful, threading state left‑to‑right.
     *   </p>
     * </div>
     *
     * @param transformation a stateful function; must not be {@code null}
     * @param <B>            the resulting value type
     * @return a {@code Stateful<B,S>} after applying the function
     * @throws NullPointerException if {@code transformation} is {@code null} or contains {@code null} function/result
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Stateful<B, S> applyTo(final @NonNull Higher2<? extends µ, Function<A, B>, S> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return narrow(transformation).bind(f -> this.map(f));
    }

    /**
     * <div>
     *   <p>
     *     Monadic bind (flatMap): sequences two stateful computations, passing the updated state
     *     from the left to the right computation.
     *   </p>
     * </div>
     *
     * @param transformation function producing the next stateful; must not be {@code null} and must not return {@code null}
     * @param <B>            the resulting value type
     * @return a {@code Stateful<B,S>} representing the sequenced computation
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <B> Stateful<B, S> bind(final @NonNull Function<A, ? extends Higher2<? extends µ, B, S>> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return unwrap(this.map(transformation));
    }

    /**
     * <div>
     *   <p>
     *     Runs the state transition and returns both the value and the new state.
     *   </p>
     * </div>
     *
     * @param state the initial state; must not be {@code null}
     * @return a {@code Tuple2} containing the value and the new state; never {@code null}
     * @throws NullPointerException if {@code state} is {@code null} or the transition returns {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Tuple2<A, S> runState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return Objects.requireNonNull(this.runState.apply(state), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Runs this stateful computation using the state contained in the given tuple.
     *     The first component of the input tuple is ignored.
     *   </p>
     * </div>
     *
     * @param Tuple2 a tuple whose second component is used as input state; must not be {@code null}
     * @return a {@code Tuple2} containing the produced value and the new state; never {@code null}
     * @throws NullPointerException if the input tuple is {@code null} or the transition returns {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Tuple2<A, S> runStateTuple(final @NonNull Tuple2<A, S> Tuple2) {
        Objects.requireNonNull(Tuple2, nullValue("tuple"));
        return Objects.requireNonNull(this.runState.apply(Tuple2.get2()), nullResult());
    }

    /**
     * <div>
     *   <p>
     *     Runs the computation and returns only the produced value.
     *   </p>
     * </div>
     *
     * @param state the initial state; must not be {@code null}
     * @return the produced value; never {@code null}
     * @throws NullPointerException if {@code state} is {@code null} or the transition returns {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public A execValue(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return this.runState(state).get();
    }

    /**
     * <div>
     *   <p>
     *     Runs the computation and returns only the new state.
     *   </p>
     * </div>
     *
     * @param state the initial state; must not be {@code null}
     * @return the new state; never {@code null}
     * @throws NullPointerException if {@code state} is {@code null} or the transition returns {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public S execState(final @NonNull S state) {
        Objects.requireNonNull(state, nullValue("state"));
        return this.runState(state).get2();
    }

}
