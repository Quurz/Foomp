package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.base.types.Monadic;
import org.quurz.foomp.base.types.Triable;
import org.quurz.foomp.base.types.UnsafeMonadic;
import org.quurz.foomp.base.types.Unwindable;
import org.quurz.foomp.base.types.UnwindingOperation;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.functions.Applicable.applicable;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

/**
 * <div>
 *     <p>
 *         A lazy error monad for computations that may succeed with a value or fail with an exception.
 *     </p>
 *     <p>
 *         An {@code Attempt<A>} defers evaluation by storing a supplier of {@link Result}{@code <A>}.
 *         On evaluation, it produces either a {@code Result.Success<A>} or a {@code Result.Failure<A>}.
 *         The API provides transformations, error handling, and safe processing while preserving laziness
 *         where possible.
 *     </p>
 *     <p>
 *         Contract: unless stated otherwise, inputs must not be {@code null}. Transformations should not
 *         return {@code null}. Exceptions thrown by user-provided functions are captured and represented
 *         as failures.
 *     </p>
 * </div>
 *
 * @param <A> the type of the contained value
 * @since 1.0.0
 */
@SuppressWarnings("NonAsciiCharacters")
public final class Attempt<A>
        implements Monadic<Attempt.µ, A>,
                   UnsafeMonadic<Attempt.µ, A>,
                   Unwindable<Attempt<A>>,
                   Triable<A>,
                   Higher1<Attempt.µ, A> {

    /**
     * <div>
     *     <p>
     *         Marker type (witness) representing {@code Attempt} in higher‑kinded encodings.
     *         Enables type‑safe simulation of Higher‑Kinded Types in Java.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * <div>
     *     <p>
     *         Narrows a {@link Higher1} value to a concrete {@code Attempt}.
     *     </p>
     *     <p>
     *         Contract: {@code wide} must not be {@code null}.
     *     </p>
     * </div>
     *
     * @param wide the higher‑kinded value to narrow; must not be {@code null}
     * @param <A>  the carried value type
     * @return an {@code Attempt} instance
     * @throws NullPointerException if {@code wide} is {@code null}
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Attempt<A> narrow(final @NonNull Higher1<? extends Attempt.µ, A> wide) {
        return (Attempt<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Creates a successful {@code Attempt} with the given value.
     *     </p>
     *     <p>
     *         The computation is stored lazily and will yield {@code Success(value)} when evaluated.
     *     </p>
     * </div>
     *
     * @param value the value to store; must not be {@code null}
     * @param <A>   the carried value type
     * @return a successful {@code Attempt}
     * @throws NullPointerException if {@code value} is {@code null}
     * @since 1.0.0
     */
    public static <A> Attempt<A> attempt(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Attempt<>(() -> success(value));
    }

    private final Supplier<Result<A>> spool;

    private Attempt(final Supplier<Result<A>> spool) {
        this.spool
            = spool;
    }

    /**
     * <div>
     *     <p>
     *         Evaluates the deferred computation and returns its {@link Result}.
     *     </p>
     *     <p>
     *         This method “unwinds” the lazy computation and is safe to call multiple times; it does not throw,
     *         but returns failures as {@code Result.Failure}.
     *     </p>
     * </div>
     *
     * @return the evaluation outcome as {@link Result}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public @NonNull Result<A> tryIt() {
        return this.spool.get();
    }

    /**
     * <div>
     *     <p>
     *         Specifies a lazy recovery path if evaluation fails, using the given supplier.
     *     </p>
     *     <p>
     *         Recovery is attempted only when the computation is evaluated and only in the failure case.
     *         If the supplier throws or yields {@code null}, the resulting failure will contain that exception
     *         (with the original exception added as suppressed in case of {@link NullPointerException}).
     *     </p>
     * </div>
     *
     * @param recover supplies the recovery value; must not be {@code null}
     * @return an {@code Attempt} that yields either the original success or the recovery value
     * @throws NullPointerException if {@code recover} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public Attempt<A> onFailureRecover(final @NonNull Supplier<A> recover) {
        Objects.requireNonNull(recover, nullValue("recover"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                    case Result.Success<A> success-> success;
                    case Result.Failure<A> failure -> {
                        Result<A> result;
                        try {
                            final var value
                                = Objects.requireNonNull(recover.get(), nullSuppliedFrom("recover"));
                            result
                                = success(value);
                        } catch (final NullPointerException nullPointerException) {
                            nullPointerException.addSuppressed(failure.getException());
                            result
                                = failure(nullPointerException);
                        } catch (final Exception exception) {
                            // Preserve original failure context
                            exception.addSuppressed(failure.getException());
                            result
                                = failure(exception);
                        }
                        yield result;
                    }
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Specifies a lazy recovery function if evaluation fails.
     *     </p>
     *     <p>
     *         The function receives the stored exception and returns a recovery value.
     *         If it throws or returns {@code null}, the resulting failure contains the thrown exception.
     *     </p>
     * </div>
     *
     * @param recover maps the exception to a recovery value; must not be {@code null}
     * @return an {@code Attempt} yielding either the original success or the recovered value
     * @throws NullPointerException if {@code recover} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    public Attempt<A> onFailureRecover(final @NonNull Function<? super Exception, ? extends A> recover) {
        Objects.requireNonNull(recover, nullValue("recover"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                case Result.Success<A> success -> success;
                case Result.Failure<A> failure -> {
                    Result<A> result;
                    try {
                        final A value
                            = Objects.requireNonNull(recover.apply(failure.getException()), nullResultFrom("recover"));
                        result
                            = success(value);
                    } catch (final Exception exception) {
                        // Preserve original failure context
                        exception.addSuppressed(failure.getException());
                        result
                            = failure(exception);
                    }
                    yield result;
                }
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Unwinds this computation, throwing the stored exception on failure.
     *     </p>
     *     <p>
     *         If successful, returns {@code this} unchanged. Otherwise, throws the stored exception.
     *     </p>
     * </div>
     *
     * @return this {@code Attempt} if successful
     * @throws Exception the stored exception if evaluation fails
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @UnwindingOperation
    public Attempt<A> onFailureThrow()
            throws Exception {
        return switch (this.spool.get()) {
            case Result.Success<A> _$ -> this;
            case Result.Failure<A> failure -> throw failure.getException();
        };
    }

    /**
     * <div>
     *     <p>
     *         Registers a lazy side effect to be executed if evaluation fails.
     *     </p>
     *     <p>
     *         The consumer is invoked during evaluation and only for failures. The returned
     *         {@code Attempt} preserves laziness and does not modify the underlying result.
     *     </p>
     * </div>
     *
     * @param failureConsumer consumes the failure exception; must not be {@code null}
     * @return an {@code Attempt} that will run the side effect on failure during evaluation
     * @throws NullPointerException if {@code failureConsumer} is {@code null}
     *
     * @since 1.0.0
     */
    public Attempt<A> peekFailureLazy(final @NonNull Consumer<? super Exception> failureConsumer) {
        Objects.requireNonNull(failureConsumer, nullValue("peek"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                case Result.Success<A> success -> success;
                case Result.Failure<A> failure -> {
                    failureConsumer.accept(failure.getException());
                    yield failure;
                }
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Executes a side effect immediately if the current state is failure.
     *     </p>
     *     <p>
     *         Eager variant: evaluates now and returns {@code this} unchanged.
     *     </p>
     * </div>
     *
     * @param failureConsumer consumes the failure exception; must not be {@code null}
     * @return this {@code Attempt}
     * @throws NullPointerException if {@code failureConsumer} is {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    public Attempt<A> peekFailureEager(final @NonNull Consumer<? super Exception> failureConsumer) {
        Objects.requireNonNull(failureConsumer, nullValue("failureConsumer"));
        final var result
            = this.spool.get();
        if (result.isFailure()) {
            failureConsumer.accept(result.getException());
        }
        return this;
    }

    /**
     * <div>
     *     <p>
     *         Maps the successful value using the given function and captures thrown exceptions as failures.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation mapping function for the success value; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} with the transformed value or a failure
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @NonNull
    @Override
    public <B> Attempt<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return this.mapUnsafe(applicable(transformation));
    }

    /**
     * <div>
     *     <p>
     *         Maps the successful value using an {@link Applicable} transformation, capturing exceptions as failures.
     *     </p>
     *     <p>
     *         Contract: {@code transformation} must not be {@code null} and must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param transformation the applicable to apply; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} with the transformed value or a failure
     * @throws NullPointerException if {@code transformation} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Attempt<B> mapUnsafe(final @NonNull Applicable<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("transformation"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                case Result.Success<A> success -> {
                    try {
                        yield success(Objects.requireNonNull(transformation.apply(success.get()), nullResultFrom("transformation")));
                    } catch (final Exception exception) {
                        yield (Result<B>) failure(exception);
                    }
                }
                case Result.Failure<A> failure -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Lifts a function stored inside a {@link Higher1} into this context and applies it to the value.
     *     </p>
     *     <p>
     *         If the function container fails, the failure is propagated; otherwise the function is applied lazily.
     *     </p>
     * </div>
     *
     * @param transformation a higher‑kinded container of a function; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} representing the lifted application
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <B> Attempt<B> applyTo(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Attempt<>(
            () -> switch (narrow(transformation).spool.get()) {
                case Result.Success<? extends Function<? super A, ? extends B>> success
                    -> (Result<B>) this.map(success.getValue()).tryIt();
                case Result.Failure<? extends Function<? super A, ? extends B>> failure
                    -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Lifts an {@link Applicable} transformation stored inside a {@link Higher1} and applies it to the value.
     *     </p>
     *     <p>
     *         Exceptions thrown by the transformation are captured as failures.
     *     </p>
     * </div>
     *
     * @param transformation a higher‑kinded container of an {@link Applicable}; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} representing the lifted application
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Attempt<B> applyToUnsafe(final @NonNull Higher1<? extends µ, ? extends Applicable<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Attempt<>(
            () -> switch (narrow(transformation).spool.get()) {
                case Result.Success<? extends Applicable<? super A, ? extends B>> success
                    -> (Result<B>) this.mapUnsafe(success.getValue()).tryIt();
                case Result.Failure<? extends Applicable<? super A, ? extends B>> failure
                    -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Monadic bind (flatMap): applies the function to the success value, flattens the result.
     *     </p>
     *     <p>
     *         Failures short‑circuit and are propagated unchanged.
     *     </p>
     * </div>
     *
     * @param transformation maps the value to another {@link Higher1}; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} representing the flattened result
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    public <B> Attempt<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return this.bindUnsafe(applicable(transformation));
    }

    /**
     * <div>
     *     <p>
     *         Unsafe monadic bind: like {@link #bind(Function)} but accepts an {@link Applicable} that may throw.
     *     </p>
     *     <p>
     *         Exceptions thrown by the transformation are captured as failures.
     *     </p>
     * </div>
     *
     * @param transformation an {@link Applicable} mapping to another {@link Higher1}; must not be {@code null}
     * @param <B>            the target type
     * @return a new {@code Attempt} representing the flattened result
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <B> Attempt<B> bindUnsafe(@NonNull Applicable<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return new Attempt<>(
            () -> switch (this.mapUnsafe(transformation).tryIt()) {
                case Result.Success<? extends Higher1<? extends µ, B>> success -> narrow(success.getValue()).tryIt();
                case Result.Failure<? extends Higher1<? extends µ, B>> failure -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Unwinds the current state and returns an {@code Attempt} that consistently yields this state.
     *     </p>
     *     <p>
     *         Eagerly materializes the current {@link Result}, while preserving the success/failure information.
     *     </p>
     * </div>
     *
     * @return a new {@code Attempt} that, when evaluated, yields the current result
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    @NonNull
    public Attempt<A> unwind() {
        final var result
            = this.spool.get();
        return new Attempt<>(() -> result);
    }

}
