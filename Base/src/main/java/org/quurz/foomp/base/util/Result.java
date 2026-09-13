package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;
import org.quurz.foomp.base.types.XorValue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;

/**
 * <div>
 *     <p>
 *         A {@code Result<A>} represents the outcome of a computation that either succeeded
 *         and returns a value of type {@code A}, or failed and provides an {@link Exception}.
 *     </p>
 *     <p>
 *         This interface is a typical replacement for exceptions in functional style and corresponds to
 *         a {@code XorValue<Exception, A>}, where {@code Left} represents an error and {@code Right}
 *         represents a successful value.
 *     </p>
 * </div>
 *
 * @param <A> The type of the success value
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface Result<A>
        extends Transmogrifyable<Result<A>>,
                XorValue<Exception, A>
        permits Result.Success,
                Result.Failure {

    /**
     * <div>
     *     <p>
     *         Creates a {@code Result} with a successful outcome.
     *     </p>
     * </div>
     *
     * @param value the successful return value
     * @param <A>   the type of the value
     * @return a {@code Result} containing the given value
     * @throws NullPointerException if the value is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Result<A> success(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return new Result.Success<>(value);
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@code Result} with an error.
     *     </p>
     * </div>
     *
     * @param exception the occurred exception
     * @param <A>       the expected type of the success value (required by the interface)
     * @return a {@code Result} containing the error
     * @throws NullPointerException if {@code exception} is {@code null}
     *
     * @since 1.0.0
     */
    static <A> Result<A> failure(final @NonNull Exception exception) {
        Objects.requireNonNull(exception, nullValue("exception"));
        return new Result.Failure<>(exception);
    }

    /**
     * <div>
     *     <p>
     *         Returns {@code true} if this {@code Result} is a successful value.
     *     </p>
     * </div>
     *
     * @return {@code true} if a successful value is present
     *
     * @since 1.0.0
     */
    @Override
    default boolean isRight() {
        return (this instanceof Result.Success);
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #isRight()}.
     *     </p>
     * </div>
     *
     * @return {@code true} if the result is successful
     *
     * @since 1.0.0
     */
    default boolean isSuccess() {
        return this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Returns {@code true} if this {@code Result} contains an error.
     *     </p>
     * </div>
     *
     * @return {@code true} if an error is present
     *
     * @since 1.0.0
     */
    default boolean isFailure() {
        return this.isLeft();
    }

    /**
     * <div>
     *     <p>
     *         Returns the success value.
     *     </p>
     * </div>
     *
     * @return the success value
     * @throws NoSuchElementException if this {@code Result} is an error
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default A getRight()
            throws NoSuchElementException {
        return switch (this) {
            case Result.Success<A> success -> success.value;
            case Result.Failure<A> failure -> throw new NoSuchElementException(noValuePresent(), failure.exception);
        };
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #getRight()}.
     *     </p>
     *     <p>
     *         Returns the success value of this {@code Result} or throws a {@link NoSuchElementException}
     *         if an error is present.
     *     </p>
     * </div>
     *
     * @return the success value of type {@code A}
     * @throws NoSuchElementException if this {@code Result} represents an error
     *
     * @since 1.0.0
     */
    default A getValue()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *     <p>
     *         Returns the exception if an error is present.
     *     </p>
     * </div>
     *
     * @return the contained {@link Exception}
     * @throws NoSuchElementException if this {@code Result} was successful
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    @Override
    @NonNull
    default Exception getLeft()
            throws NoSuchElementException {
        return switch (this) {
            case Result.Success<A> _$ -> throw new NoSuchElementException(noValuePresent());
            case Result.Failure<A> failure -> failure.exception;
        };
    }

    /**
     * <div>
     *     <p>
     *         Alias for {@link #getLeft()}.
     *     </p>
     *     <p>
     *         Returns the contained exception if this {@code Result} is an error,
     *         otherwise throws a {@link NoSuchElementException}.
     *     </p>
     * </div>
     *
     * @return the contained {@link Exception}
     * @throws NoSuchElementException if this {@code Result} is a success value
     *
     * @since 1.0.0
     */
    default Exception getException()
            throws NoSuchElementException {
        return this.getLeft();
    }

    /**
     * <div>
     *     <p>
     *         Converts this {@code Result} to an {@link Either}.
     *     </p>
     *     <p>
     *         In case of success, returns an {@link Either.Right} with the contained value,
     *         in case of failure, returns an {@link Either.Left} with the contained exception.
     *     </p>
     *     <p>
     *         The values are delivered lazily, i.e., evaluated only when needed.
     *     </p>
     * </div>
     *
     * @return an {@link Either} containing either the success value or the exception
     *
     * @since 1.0.0
     */
    default Either<Exception, A> toEither() {
        return switch (this) {
            case Result.Success<A> success -> new Either.Right<>(() -> success.value);
            case Result.Failure<A> failure -> new Either.Left<>(() -> failure.exception);
        };
    }

    /**
     * <div>
     *     <p>
     *         Converts this {@code Result} to a {@code Maybe}.
     *     </p>
     *     <p>
     *         Returns a {@code Some} containing the success value if this is a successful result,
     *         or {@code None} if this result contains an error.
     *     </p>
     * </div>
     *
     * @return a {@code Maybe} containing the success value if present
     *
     * @since 1.0.0
     */
    default Maybe<A> toMaybe() {
        return switch (this) {
            case Result.Success<A> success -> some(success.value);
            case Result.Failure<A> _ -> none();
        };
    }

    /**
     * <div>
     *     <p>
     *         Transforms this {@code Result} into another type using the provided transformation function.
     *     </p>
     * </div>
     *
     * @param transmogrifier the transformation function to apply
     * @param <T>            the target type of the transformation
     * @return the transformed value
     * @throws NullPointerException if {@code transmogrifier} is null or returns null
     *
     * @since 1.0.0
     */
    @Override
    default <T> @NonNull T transmogrify(final @NonNull Function<? super Result<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Success representation of a {@code Result}.
     *     </p>
     * </div>
     *
     * @param <R> the type of the success value
     *
     * @since 1.0.0
     */
    final class Success<R>
            implements Result<R> {

        private final R value;

        private Success(final R value) {
            this.value
                = value;
        }

        /**
         * <div>
         *     <p>
         *         Compares this {@code Success} object with another for equality.
         *     </p>
         * </div>
         *
         * @param o the object to compare with
         * @return {@code true} if the other object is also a {@code Success} with the same value
         *
         * @since 1.0.0
         */
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Success<?> success)) return false;

            return value.equals(success.value);
        }

        /**
         * <div>
         *     <p>
         *         Returns the hash code of the contained value.
         *     </p>
         * </div>
         *
         * @return the hash code
         *
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            return value.hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Returns a string representation of the {@code Success} object.
         *     </p>
         * </div>
         *
         * @return a string representation containing the value
         *
         * @since 1.0.0
         */
        @Override
        public String toString() {
            return new StringJoiner(", ", Success.class.getSimpleName() + "[", "]")
                    .add("value=" + value)
                    .toString();
        }

    }

    /**
     * <div>
     *     <p>
     *         Error representation of a {@code Result}.
     *     </p>
     * </div>
     *
     * @param <R> the expected type of the success value
     *
     * @since 1.0.0
     */
    final class Failure<R>
            implements Result<R> {

        private final Exception exception;

        private Failure(final Exception exception) {
            this.exception
                = exception;
        }

        /**
         * <div>
         *     <p>
         *         Compares this {@code Failure} object with another for equality.
         *     </p>
         * </div>
         *
         * @param o the object to compare with
         * @return {@code true} if the other object is also a {@code Failure} with the same exception
         *
         * @since 1.0.0
         */
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Failure<?> failure)) return false;

            return exception.equals(failure.exception);
        }

        /**
         * <div>
         *     <p>
         *         Returns the hash code of the contained {@link Exception}.
         *     </p>
         * </div>
         *
         * @return the hash code
         *
         * @since 1.0.0
         */
        @Override
        public int hashCode() {
            return exception.hashCode();
        }

        /**
         * <div>
         *     <p>
         *         Returns a string representation of the {@code Failure} object.
         *     </p>
         * </div>
         *
         * @return a string representation containing the exception
         *
         * @since 1.0.0
         */
        @Override
        public String toString() {
            return new StringJoiner(", ", Failure.class.getSimpleName() + "[", "]")
                    .add("exception=" + exception)
                    .toString();
        }

    }

}
