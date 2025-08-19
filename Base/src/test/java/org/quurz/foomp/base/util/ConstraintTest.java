package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.Constraint.constraintFunction;
import static org.quurz.foomp.base.util.Constraint.constraint;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Constraint")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ConstraintTest {

    private static final Logger LOGGER
        = getLogger(ConstraintTest.class);

    @Nested
    class FunctionFactory {

        @Test
        void should_ReturnFun_When_BuildingWith_ConstantFailure() {
            LOGGER.info("Constraint.constraintFunction(check, failure) should return Fun<A, Maybe<Failure>>");

            final Predicate<String> check
                = s -> s != null && s.length() >= 3;
            final String failure
                = "too short";

            final var constraintCheck
                = constraintFunction(check, failure);

            assertThat(constraintCheck)
                .isNotNull();
            assertThat(constraintCheck.apply("abc").isNone())
                .isTrue();   // valid -> none()
            assertThat(constraintCheck.apply("a").isPresent())
                .isTrue();   // invalid -> some()
            assertThat(constraintCheck.apply(null)
                .isPresent()).isTrue();  // null -> per contract handled by predicate
        }

        @Test
        void should_ReturnFun_When_BuildingWith_FunctionFailure() {
            LOGGER.info("Constraint.constraintFunction(check, failureFn) should return Fun<A, Maybe<Failure>>");

            Predicate<Integer> check
                = i -> i != null && i % 2 == 0;
            Function<Integer, String> failureFn
                = i -> "must be even, was " + i;

            final var fun
                = Constraint.constraintFunction(check, failureFn);

            assertThat(fun)
                .isNotNull();
            assertThat(fun.apply(2)
                .isNone()).isTrue();
            assertThat(fun.apply(3)
                .isPresent()).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void should_throw_NPE_when_constraintFunction_check_is_null() {
            LOGGER.info("constraintFunction(null, ...) should throw NPE mentioning 'check'");

            assertThatThrownBy(() -> constraintFunction(null, "x"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("check");

            assertThatThrownBy(() -> constraintFunction(null, (Function<String, String>) s -> "x"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("check");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void should_throw_NPE_when_constraintFunction_constant_failure_is_null() {
            LOGGER.info("constraintFunction(check, (FAILURE)null) should throw NPE mentioning 'failure'");

            final Predicate<String> check
                = Objects::nonNull;

            assertThatThrownBy(() -> constraintFunction(check, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("failure");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void should_throw_NPE_when_constraintFunction_failureFn_is_null() {
            LOGGER.info("constraintFunction(check, (Function)null) should throw NPE mentioning 'failureFunction'");

            final Predicate<String> check
                = Objects::nonNull;

            assertThatThrownBy(() -> constraintFunction(check, (Function<String, String>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("failureFunction");
        }
    }

    @Nested
    class Factory {

        @Test
        void should_CreateConstraint_When_Using_ConstantFailure() {
            LOGGER.info("Constraint.constraint(check, failure) should create instance and behave");

            final Predicate<String> check
                = s -> s != null && !s.isBlank();
            final String failure
                = "must not be null or blank";

            final var constraint
                = constraint(check, failure);

            assertThat(constraint)
                .isNotNull();
            assertThat(constraint.checkViolation("ok").isNone())
                .isTrue();
            assertThat(constraint.checkViolation(" ").isPresent())
                .isTrue();
            assertThat(constraint.checkViolation(null).isPresent())
                .isTrue();
        }

        @Test
        void should_CreateConstraint_When_Using_FunctionFailure() {
            LOGGER.info("Constraint.constraint(check, failureFn) should create instance and behave");

            final Predicate<Integer> check
                = i -> i != null && i > 0;
            final Function<Integer, String> failureFn
                = i -> "must be positive, was " + i;

            final var constraint
                = constraint(check, failureFn);

            assertThat(constraint)
                .isNotNull();
            assertThat(constraint.checkViolation(1)
                .isNone()).isTrue();
            assertThat(constraint.checkViolation(0)
                .isPresent()).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void should_throw_NPE_when_constraint_constant_failure_is_null() {
            LOGGER.info("constraint(check, (FAILURE)null) should throw NPE mentioning 'failure'");

            final Predicate<String> check
                = Objects::nonNull;

            assertThatThrownBy(() -> constraint(check, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("failure");
        }
    }

    @Nested
    class Behaviour {

        @Test
        void should_ReturnNoneAndFlags_When_ValueIsValid() {
            LOGGER.info("Constraint: valid value -> none(), isValid=true, isInvalid=false");

            final var constraint
                = constraint(
                    (Integer i) -> i != null && i > 10,
                    i -> "must be > 10"
                );

            assertThat(constraint.checkViolation(11).isNone())
                .isTrue();
            assertThat(constraint.isValid(11))
                .isTrue();
            assertThat(constraint.isInvalid(11))
                .isFalse();
        }

        @Test
        void should_ReturnSomeAndFlags_When_ValueIsInvalid() {
            LOGGER.info("Constraint: invalid value -> some(failure), isValid=false, isInvalid=true");

            final var constraint
                = constraint(
                    (Integer i) -> i != null && i > 10,
                    i -> "must be > 10"
                );

            assertThat(constraint.checkViolation(10).isPresent())
                .isTrue();
            assertThat(constraint.isValid(10))
                .isFalse();
            assertThat(constraint.isInvalid(10))
                .isTrue();
        }

        @Test
        void should_HandleNull_AsSpecifiedBy_PredicateContract() {
            LOGGER.info("Constraint: null handling is defined by predicate contract");

            final var constraint
                = constraint(
                    (String s) -> s != null && !s.isBlank(),
                    s -> "must not be null or blank"
                );

            assertThat(constraint.checkViolation(null).isPresent())
                .isTrue();
            assertThat(constraint.isValid(null))
                .isFalse();
            assertThat(constraint.isInvalid(null))
                .isTrue();
        }

        @Test
        void should_ReturnNone_When_ApplyingTo_ValidValue() {
            LOGGER.info("Constraint.apply(): valid value -> none()");

            final var constraint
                    = constraint(
                    (Integer i) -> i != null && i > 0,
                    i -> "must be positive"
            );

            assertThat(constraint.apply(1).isNone())
                .isTrue();
        }

        @Test
        void should_ReturnSome_When_ApplyingTo_InvalidValue() {
            LOGGER.info("Constraint.apply(): invalid value -> some(failure)");

            final var constraint
                = constraint(
                    (Integer i) -> i != null && i > 0,
                    i -> "must be positive"
                );

            assertThat(constraint.apply(0).isPresent())
                    .isTrue();
        }

        @Test
        void should_HandleNull_When_Applying() {
            LOGGER.info("Constraint.apply(): null handling is defined by predicate contract");

            final var constraint
                = constraint(
                    (String s) -> s != null && !s.isBlank(),
                    s -> "must not be null or blank"
                );

            assertThat(constraint.apply(null).isPresent())
                .isTrue();
        }

        @Test
        void should_throw_NPE_when_failureFunction_returns_null() {
            LOGGER.info("Constraint should throw NPE when failureFunction returns null on violation");

            final var c = constraint(
                (String s) -> s != null && !s.isBlank(),
                (Function<String, String>) s -> null
            );

            assertThatThrownBy(() -> c.checkViolation(""))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("failureFunction");

            // apply delegiert auf checkViolation
            assertThatThrownBy(() -> c.apply(""))
                .isInstanceOf(NullPointerException.class);
        }
    }

}
