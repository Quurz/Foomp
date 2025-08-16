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

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;

/**
 * <div>
 *     <p>
 *         An implementation of an error monad that enables lazy evaluation of computations
 *         and safe handling of exceptions.
 *     </p>
 *     <p>
 *         The class {@code Attempt<A>} stores either a successful value of type {@code A}
 *         or an exception that occurred during computation. It provides methods for
 *         transformation, error handling and safe processing of the contained values.
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
     *         A marker type (witness) that represents {@code Attempt} within the type hierarchy.
     *         This type is used to simulate Higher-Kinded Types in Java.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final class µ implements WitnessType { private µ() {} }

    /**
     * Converts an instance of {@link Higher1} into a concrete {@code Attempt} instance.
     *
     * @param wide the object to be converted into {@code Attempt}
     * @param <A> the type of the contained value
     * @return an {@code Attempt} instance
     * @throws NullPointerException if {@code wide} is null
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> Attempt<A> narrow(final @NonNull  Higher1<? extends Attempt.µ, A> wide) {
        return (Attempt<A>) Objects.requireNonNull(wide, nullValue("wide"));
    }

    /**
     * <div>
     *     <p>
     *         Creates a successful {@code Attempt} instance with the given value.
     *     </p>
     * </div>
     *
     * @param value the value to be stored
     * @param <A> the type of the contained value
     * @return a successful {@code Attempt} instance
     * @throws NullPointerException if {@code value} is null
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
     *         Executes the encapsulated computation and returns the result.
     *     </p>
     *     <p>
     *         This method "unwinds" the lazily stored computation and returns the
     *         result as a {@link Result}. It can be called multiple times.
     *     </p>
     * </div>
     *
     * @return the result of the computation as {@link Result}
     *
     * @since 1.0.0
     */
    @Override
    @UnwindingOperation
    public Result<A> tryIt() {
        return this.spool.get();
    }

    /**
     * <div>
     *     <p>
     *         Executes a recovery action if the computation fails, and returns
     *         a new {@code Attempt} with the recovery value.
     *     </p>
     * </div>
     *
     * @param recover a supplier that provides the recovery value
     * @return a new {@code Attempt} with either the original or the recovery value
     * @throws NullPointerException if {@code recover} is null
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
                        }
                        yield result;
                    }
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Performs recovery if an exception is stored.
     *         Recovery is done using a function that processes the exception
     *         and provides a new value.
     *     </p>
     * </div>
     *
     * @param recover a function that processes the exception and provides a recovery value
     * @return a new {@code Attempt} instance with either the original or recovered value
     * @throws NullPointerException if the function is {@code null} or returns {@code null}
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
     *         Attempts to extract the stored value. However, if the computation failed,
     *         the stored exception is thrown.
     *     </p>
     * </div>
     *
     * @return this {@code Attempt} instance if successful
     * @throws Exception the stored exception if the computation failed
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
     *         Registers an action to be executed if this {@code Attempt} fails.
     *     </p>
     *     <p>
     *         This method is <em>lazy</em>: The provided {@link Consumer} is only called upon
     *         first access to the result (e.g., through {@code isSuccess()}, {@code get()}
     *         or {@code toString()}) - and only if the result actually fails.
     *     </p>
     *     <p>
     *         The method doesn't modify the result but returns a new {@code Attempt}
     *         that will execute the given action in case of failure during evaluation.
     *     </p>
     * </div>
     *
     * @param failureConsumer an action that accepts the {@link Exception} value in case of failure
     * @return an {@code Attempt} that executes the action during evaluation in case of failure
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
     *         Immediately executes an action if the stored result contains an exception.
     *     </p>
     *     <p>
     *         This method is <em>eager</em>: The provided {@link Consumer} is executed
     *         immediately when this method is called - but only if an error exists.</p>
     *     </p>
     *     <p>
     *         The method returns the same {@code Attempt} instance without modifying
     *         its state.
     *     </p>
     * </div>
     *
     * @param failureConsumer an action that is called with the stored {@link Exception}
     * @return the same {@code Attempt} instance
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
     *         Applies a function to the stored value and returns a new {@code Attempt} with
     *         the result of the function. If an exception occurs during the application of the function,
     *         it is stored in the new instance.
     *     </p>
     * </div>
     *
     * @param transformation a function for transforming the value
     * @param <B> the type of the function's result
     * @return a new {@code Attempt} with the transformed value or a stored exception
     * @throws NullPointerException if the function is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <B> Attempt<B> map(final @NonNull Function<? super A, ? extends B> transformation) {
        Objects.requireNonNull(transformation, nullValue("fMap"));
        return new Attempt<>(
            () -> switch (this.spool.get()) {
                    case Result.Success<A> success -> {
                        try {
                            yield success(Objects.requireNonNull(transformation.apply(success.get()), nullResultFrom("fMap")));
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
     *         Performs a transformation on the stored value using an {@link Applicable} instance.
     *         If an exception is thrown during application, it is stored as a failure
     *         in the new {@code Attempt}.
     *     </p>
     *     <p>
     *         This variant of the {@code map} operation supports unchecked exceptions in the transformation
     *         and is therefore intended for unsafe environments.
     *     </p>
     * </div>
     *
     * @param transformation an {@link Applicable} instance for transforming the value
     * @param <B> the type of the new value
     * @return a new {@code Attempt} with transformed value or an error
     * @throws NullPointerException if the transformation is {@code null} or returns {@code null}
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
                            yield success(Objects.requireNonNull(transformation.apply(success.get()), nullResultFrom("fMap")));
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
     *         Lifts the application of a function within a {@link Higher1} to the stored
     *         values in this {@code Attempt}.
     *     </p>
     * </div>
     *
     * @param transformation a {@link Higher1} containing a function to be applied to the values
     * @param <B> the type of the function's result
     * @return a new {@code Attempt} with the result of the lifted function
     * @throws NullPointerException if the given {@link Higher1} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <B> Attempt<B> lift(final @NonNull Higher1<? extends µ, ? extends Function<? super A, ? extends B>> transformation) {
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
     *         Lifts an {@link Applicable} transformation within a {@link Higher1} context to the
     *         stored value. This method allows applying a function stored in an {@code Attempt}
     *         to the stored value of this instance.
     *     </p>
     *     <p>
     *         Unlike {@link #lift(Higher1)}, this variant also allows transformations
     *         that can throw checked or unchecked exceptions.
     *     </p>
     * </div>
     *
     * @param transformation a {@link Higher1} containing an {@link Applicable} transformation
     * @param <B> the target type after applying the function
     * @return a new {@code Attempt} with the result of the lifted transformation or an error
     * @throws NullPointerException if {@code transformation} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <B> Attempt<B> liftUnsafe(final @NonNull Higher1<? extends µ, Applicable<? super A, ? extends B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("liftA"));
        return new Attempt<>(
                () -> switch (narrow(transformation).spool.get()) {
                    case Result.Success<? extends Applicable<? super A, ? extends B>> success
                        -> (Result<B>) this.mapUnsafe(success.getValue()).tryIt();
                    case Result.Failure<Applicable<? super A, ? extends B>> failure
                        -> (Result<B>) failure;
                }
        );
    }

    /**
     * <div>
     *     <p>
     *         Performs the bind operation by applying the given function to the stored value,
     *         and returns a new {@code Attempt} containing the result.
     *     </p>
     * </div>
     *
     * @param transformation a function that transforms the stored value and creates a new {@link Higher1}
     * @param <B> the type of the function's result
     * @return a new {@code Attempt} with the transformed value or a stored exception
     *
     * @throws NullPointerException if the function is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public <B> Attempt<B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends µ, B>> transformation) {
        Objects.requireNonNull(transformation, nullValue("bindM"));
        return new Attempt<>(
            () -> switch (this.map(transformation).tryIt()) {
                case Result.Success<? extends Higher1<? extends µ, B>> success -> narrow(success.getValue()).tryIt();
                case Result.Failure<? extends Higher1<? extends µ, B>> failure -> (Result<B>) failure;
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Performs a bind operation (monadic FlatMap) with an unsafe {@link Applicable} function.
     *         The function returns a new {@link Higher1} that is unwrapped and processed further.
     *     </p>
     *     <p>
     *         In case of error (e.g. through an exception when applying the function) the new {@code Attempt}
     *         will contain the error.
     *     </p>
     * </div>
     *
     * @param transformation an {@link Applicable} function that returns a new {@link Higher1}
     * @param <B>            the type of the transformed value
     * @return a new {@code Attempt} with the result of the transformation or an error
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
     *         Unwinds all operations on this <code>Attempt</code> and returns a new <code>Attempt</code> object
     *     </p>
     * </div>
     *
     * @return The new <code>{@link Attempt}</code>
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
