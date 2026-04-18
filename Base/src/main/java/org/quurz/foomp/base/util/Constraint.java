package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;

/**
 * <div>
 *     <p>
 *         A class that represents a validation constraint which can check values against a predicate
 *         and produce failure results when the constraint is violated.
 *     </p>
 *     <p>
 *         The constraint combines a check predicate with a failure producer function to create
 *         a reusable validation component.
 *     </p>
 * </div>
 *
 * @param <A> the type of values to be validated
 * @param <FAILURE> the type of failure results when validation fails
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class Constraint<A, FAILURE>
        implements Function<A, Maybe<FAILURE>>{

    /**
     * <div>
     *     <p>
     *         Creates a {@link Function} that checks values against the given predicate and returns a constant failure
     *         when the check fails.
     *     </p>
     * </div>
     *
     * @param <A>       the type of values to check
     * @param <FAILURE> the type of the failure result
     * @param check     the predicate to validate values
     * @param failure   the constant failure to return on validation errors
     * @return a function that performs the validation and returns Maybe.none() on success or Maybe.some(failure) on error
     * @throws NullPointerException if check or failure is null
     *
     * @since 1.0.0
     */
    public static <A, FAILURE> Function<A, Maybe<FAILURE>> constraintFunction(final @NonNull Predicate<? super A> check,
                                                                              final @NonNull FAILURE failure) {
        Objects.requireNonNull(check, nullValue("check"));
        Objects.requireNonNull(failure, nullValue("failure"));

        return constraintFunction(check, _ -> failure);
    }

    /**
     * <div>
     *     <p>
     *         Creates a {@link Function} that checks values against the given predicate and produces failures
     *         using the provided failure function when the check fails.
     *     </p>
     * </div>
     *
     * @param <A>             the type of values to check
     * @param <FAILURE>       the type of the failure result
     * @param check           the predicate to validate values
     * @param failureFunction the function to produce failure results from invalid values
     * @return a function that performs the validation and returns Maybe.none() on success or Maybe.some(failure) on error
     * @throws NullPointerException if check or failureFunction is null
     *
     * @since 1.0.0
     */
    public static <A, FAILURE> Function<A, Maybe<FAILURE>> constraintFunction(final @NonNull Predicate<? super A> check,
                                                                              final @NonNull Function<? super A, ? extends FAILURE> failureFunction) {
        Objects.requireNonNull(check, nullValue("check"));
        Objects.requireNonNull(failureFunction, nullValue("failureFunction"));

        return value
                -> check.test(value)
                    ? none()
                    : some(Objects.requireNonNull(failureFunction.apply(value), nullResultFrom("failureFunction")));
    }

    /**
     * <div>
     *     <p>
     *         Creates a new constraint with the given check predicate and constant failure.
     *     </p>
     * </div>
     *
     * @param <A>       the type of values to check
     * @param <FAILURE> the type of the failure result
     * @param check     the predicate to validate values
     * @param failure   the constant failure to return on validation errors
     * @return a new constraint instance
     * @throws NullPointerException if check or failure is null
     *
     * @since 1.0.0
     */
    public static <A, FAILURE> Constraint<A, FAILURE> constraint(final @NonNull Predicate<? super A> check,
                                                                 final @NonNull FAILURE failure) {
        Objects.requireNonNull(check, nullValue("check"));
        Objects.requireNonNull(failure, nullValue("failure"));

        return constraint(check, _ -> failure);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new constraint with the given check predicate and failure function.
     *     </p>
     * </div>
     *
     * @param <A>             the type of values to check
     * @param <FAILURE>       the type of the failure result
     * @param check           the predicate to validate values
     * @param failureFunction the function to produce failure results from invalid values
     * @return a new constraint instance
     * @throws NullPointerException if check or failureFunction is null
     *
     * @since 1.0.0
     */
    public static <A, FAILURE> Constraint<A, FAILURE> constraint(final @NonNull Predicate<? super A> check,
                                                                 final @NonNull Function<? super A, ? extends FAILURE> failureFunction) {
        Objects.requireNonNull(check, nullValue("check"));
        Objects.requireNonNull(failureFunction, nullValue("failureFunction"));

        return new Constraint<>(check, failureFunction);
    }

    private final Predicate<? super A> check;
    private final Function<? super A, ? extends FAILURE> failureFunction;

    private Constraint(final Predicate<? super A> check,
                       final Function<? super A, ? extends FAILURE> failureFunction) {
        this.check
            = check;
        this.failureFunction
            = failureFunction;
    }

    /**
     * <div>
     *     <p>
     *         Checks if the given value satisfies this constraint.
     *     </p>
     * </div>
     *
     * @param value the value to validate
     * @return true if the value is valid, false otherwise
     *
     * @since 1.0.0
     */
    public boolean isValid(final @Nullable A value) {
        return this.check.test(value);
    }

    /**
     * <div>
     *     <p>
     *         Checks if the given value violates this constraint.
     *     </p>
     * </div>
     *
     * @param value the value to validate
     * @return true if the value is invalid, false otherwise
     *
     * @since 1.0.0
     */
    public boolean isInvalid(final @Nullable A value) {
        return !this.check.test(value);
    }

    /**
     * <div>
     *     <p>
     *         Validates the given value and returns a possible failure result if the constraint is violated.
     *     </p>
     * </div>
     *
     * @param value the value to validate
     * @return Maybe.none() if the value is valid, or Maybe.some(failure) if invalid
     *
     * @since 1.0.0
     */
    public @NonNull Maybe<FAILURE> checkViolation(final @Nullable A value) {
        return this.check.test(value)
                ? none()
                : some(Objects.requireNonNull(this.failureFunction.apply(value), nullResultFrom("failureFunction")));
    }

    /**
     * <div>
     *     <p>
     *         Applies the constraint check to the given value and returns a possible failure result
     *         if the value violates the constraint.
     *     </p>
     * </div>
     *
     * @param value the value to validate; may be null
     * @return a {@code Maybe.none()} if the value is valid, or {@code Maybe.some(failure)} if invalid
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Maybe<FAILURE> apply(final @Nullable A value) {
        return this.checkViolation(value);
    }

}
