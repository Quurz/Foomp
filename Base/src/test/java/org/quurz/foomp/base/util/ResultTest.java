package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;
import static org.slf4j.LoggerFactory.getLogger;

class ResultTest {

    private static final Logger LOGGEr
        = getLogger(ResultTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testSuccess() {
        LOGGEr.info("Test Result.success");

        assertThatThrownBy(() -> success(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> success(5));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testFailure() {
        LOGGEr.info("Test Result.failure");

        assertThatThrownBy(() -> failure(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> failure(new IllegalArgumentException()));
    }

    @Test
    void testChecks() {
        LOGGEr.info("Test result.isSuccess and result.isFailure");

        assertThat(success(5).isSuccess())
            .isTrue();
        assertThat(success(5).isFailure())
            .isFalse();
        assertThat(failure(new IllegalArgumentException()).isSuccess())
            .isFalse();
        assertThat(failure(new IllegalArgumentException()).isFailure())
            .isTrue();
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    void testGetters() {
        LOGGEr.info("Test result.getValue and result.getException");

        assertThat(success(5).getValue())
            .isEqualTo(5);
        assertThatThrownBy(() -> success(5).getException())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(noValuePresent());
        assertThat(failure(new IllegalArgumentException()).getException())
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> failure(new IllegalArgumentException()).getValue())
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(noValuePresent());
    }

    @Test
    void testToEither() {
        LOGGEr.info("Test result.toEither");

        final var exception
            = new IllegalArgumentException();

        assertThat(success(5).toEither())
            .isEqualTo(right(5));
        assertThat(failure(exception).toEither())
            .isEqualTo(left(exception));
    }

    @Test
    void testSuccessEqualsAndHashCode() {
        LOGGEr.info("Test Result.Success equals and hashCode");

        assertThat(success(5).equals(success(5)))
            .isTrue();
        assertThat(success(5).equals(success(6)))
            .isFalse();
        assertThat(success(5).hashCode())
            .isEqualTo(success(5).hashCode());
        assertThat(success(5).hashCode())
            .isNotEqualTo(success(6).hashCode());
    }

    @Test
    void testFailureEqualsAndHashCode() {
        LOGGEr.info("Test Result.Failure equals and hashCode");

        final IllegalArgumentException exception
            = new IllegalArgumentException();

        assertThat(failure(exception).equals(failure(exception)))
            .isTrue();
        assertThat(failure(new IllegalArgumentException()).equals(failure(new IllegalArgumentException())))
            .isFalse();
        assertThat(failure(exception).hashCode())
            .isEqualTo(failure(exception).hashCode());
        assertThat(failure(new IllegalArgumentException()).hashCode())
            .isNotEqualTo(failure(new IllegalArgumentException()).hashCode());
    }

    @Test
    void testSuccessToString() {
        LOGGEr.info("Test Result.Success toString");

        assertThat(success(5).toString())
            .isEqualTo("Success[value=5]");
    }

    @Test
    void testFailureToString() {
        LOGGEr.info("Test Result.Failure toString");

        final IllegalArgumentException exception
            = new IllegalArgumentException();

        assertThat(failure(exception).toString())
            .isEqualTo("Failure[exception=java.lang.IllegalArgumentException]");
    }

}
