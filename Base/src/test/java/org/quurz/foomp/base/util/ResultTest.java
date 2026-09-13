package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Result.failure;
import static org.quurz.foomp.base.util.Result.success;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("Result")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResultTest {

    private static final Logger LOGGER = getLogger(ResultTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void success_and_failure_should_enforce_non_null() {
            LOGGER.info("Result.success/Result.failure should enforce non-null contracts");

            assertThatThrownBy(() -> success(null))
                .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> failure(null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> success("OK"));
            assertThatNoException().isThrownBy(() -> failure(new Exception("E")));
            assertThatNoException().isThrownBy(() -> failure(new AssertionError("Assertion failed")));
            assertThatNoException().isThrownBy(() -> failure(new Throwable("Generic throwable")));
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void isSuccess_isFailure_isRight_should_reflect_variant() {
            LOGGER.info("Result.isSuccess/isFailure/isRight should reflect the current variant");

            final var ok = success("OK");
            final var err = failure(new Exception("E"));

            assertThat(ok.isSuccess()).isTrue();
            assertThat(ok.isFailure()).isFalse();
            assertThat(ok.isRight()).isTrue();

            assertThat(err.isSuccess()).isFalse();
            assertThat(err.isFailure()).isTrue();
            assertThat(err.isRight()).isFalse();
        }

        @Test
        void getRight_should_return_on_success_and_throw_on_failure() {
            LOGGER.info("Result.getRight should return value on success and throw on failure");

            final var ok = success("OK");
            final var err = failure(new Exception("E"));

            assertThat(ok.getRight()).isEqualTo("OK");
            assertThatThrownBy(err::getRight).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void getLeft_should_return_on_failure_and_throw_on_success() {
            LOGGER.info("Result.getLeft should return throwable on failure and throw on success");

            final var ok = success("OK");
            final var err = failure(new Throwable("E"));

            assertThatThrownBy(ok::getLeft).isInstanceOf(NoSuchElementException.class);
            assertThat(err.getLeft()).isInstanceOf(Throwable.class).hasMessage("E");
        }

        @Test
        void getValue_getThrowable_should_alias_getRight_getLeft() {
            LOGGER.info("Result.getValue/getThrowable should alias getRight/getLeft");

            final var ok = success("OK");
            final var err = failure(new Throwable("E"));

            assertThat(ok.getValue()).isEqualTo("OK");
            assertThatThrownBy(err::getValue).isInstanceOf(NoSuchElementException.class);

            assertThat(err.getThrowable()).isInstanceOf(Throwable.class).hasMessage("E");
            assertThatThrownBy(ok::getThrowable).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("Conversions")
    class Conversions {

        @Test
        void toEither_should_convert_success_and_failure() {
            LOGGER.info("Result.toEither should convert Success->Right and Failure->Left");

            final var ok = success("OK");
            final var err = failure(new Throwable("E"));

            final var eOk = ok.toEither();
            final var eErr = err.toEither();

            assertThat(eOk.isRight()).isTrue();
            assertThat(eOk.getRight()).isEqualTo("OK");

            assertThat(eErr.isRight()).isFalse();
            assertThat(eErr.getLeft()).isInstanceOf(Throwable.class).hasMessage("E");
        }

        @Test
        void toMaybe_should_convert_success_to_some_and_failure_to_none() {
            LOGGER.info("Result.toMaybe should convert Success->Some and Failure->None");

            final var ok = success("OK");
            final var err = failure(new Exception("E"));

            final var mOk = ok.toMaybe();
            final var mErr = err.toMaybe();

            assertThat(mOk.isSome()).isTrue();
            assertThat(mOk.get()).isEqualTo("OK");

            assertThat(mErr.isNone()).isTrue();
            assertThatThrownBy(mErr::get).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("Utilities")
    class Utilities {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void transmogrify_should_enforce_contracts_and_return_value() {
            LOGGER.info("Result.transmogrify should enforce non-null and return non-null");

            final var ok = success("OK");
            final var err = failure(new Exception("E"));

            assertThatThrownBy(() -> ok.transmogrify(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> ok.transmogrify(_$ -> null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var s = ok.transmogrify(res -> res.isSuccess() ? "yay" : "nay");
                final var f = err.transmogrify(res -> res.isFailure() ? "nay" : "yay");
                assertThat(s).isEqualTo("yay");
                assertThat(f).isEqualTo("nay");
            });
        }

        @Test
        void equals_hashCode_and_toString_should_be_structural_and_stable() {
            LOGGER.info("Result.equals/hashCode should be structural; toString should contain variant");

            final var s1 = success("OK");
            final var s2 = success("OK");
            final var s3 = success("OTHER");

            // Failure: gleiche Instanz vs. unterschiedliche Instanzen mit gleicher Message
            final var sameEx = new Throwable("E");
            final var eSame1 = failure(sameEx);
            final var eSame2 = failure(sameEx);
            final var e1 = failure(new Throwable("E"));
            final var e2 = failure(new Throwable("E"));
            final var e3 = failure(new Throwable("F"));

            // Success
            assertThat(s1).isEqualTo(s2);
            assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
            assertThat(s1).isNotEqualTo(s3);

            // Failure (Identität zählt, nicht nur Klasse/Message)
            assertThat(eSame1).isEqualTo(eSame2);
            assertThat(eSame1.hashCode()).isEqualTo(eSame2.hashCode());
            assertThat(e1).isNotEqualTo(e2);   // verschiedene Instanzen, gleiche Message → ungleich
            assertThat(e1).isNotEqualTo(e3);

            // Cross-variant inequality
            assertThat(s1).isNotEqualTo(e1);

            // toString (stabiler Präfix)
            assertThat(s1.toString()).startsWith("Success[");
            assertThat(e1.toString()).startsWith("Failure[");
        }
    }
}
