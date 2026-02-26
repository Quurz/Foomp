package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.quurz.foomp.base.util.Either.left;
import static org.quurz.foomp.base.util.Either.right;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
@DisplayName("Either")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EitherTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(EitherTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void left_with_null_value_throws() {
            LOGGER.info("Either.left with null-value should throw");
            assertThatThrownBy(() -> left(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void left_with_valid_value_is_left() {
            LOGGER.info("Either.left with valid value");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                checkIsLeftWithValue(e, SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void right_with_null_value_throws() {
            LOGGER.info("Either.right with null-value should throw");
            assertThatThrownBy(() -> right(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void right_with_valid_value_is_right() {
            LOGGER.info("Either.right with valid value");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_OTHER_STRING_VALUE);
                checkIsRight(e);
            });
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void isLeft_reports_correctly() {
            LOGGER.info("either.isLeft on Right/Left");

            checkIsRight(right(SOME_STRING_VALUE));
            checkIsLeft(left(SOME_STRING_VALUE));
        }

        @Test
        @SuppressWarnings("unused")
        void getLeft_on_right_throws() {
            LOGGER.info("either.getLeft on Right should throw");
            assertThatThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                final var $_ = e.getLeft();
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void getLeftOrElse_returns_supplier_on_right() {
            LOGGER.info("either.getLeftOrElse on Right");
            final var e = right(SOME_STRING_VALUE);
            assertThat(e.getLeftOrElse(() -> SOME_OTHER_STRING_VALUE))
                .isEqualTo(SOME_OTHER_STRING_VALUE);
        }

        @Test
        void getLeftOrElse_returns_left_on_left() {
            LOGGER.info("either.getLeftOrElse on Left");
            final var e = left(SOME_STRING_VALUE);
            assertThat(e.getLeftOrElse(() -> SOME_OTHER_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void getLeftSafe_returns_none_on_right() {
            LOGGER.info("either.getLeftSafe on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                checkIsNone(e.getLeftSafe());
            });
        }

        @Test
        void getLeftSafe_returns_some_on_left() {
            LOGGER.info("either.getLeftSafe on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                checkIsSomeWithValue(e.getLeftSafe(), SOME_STRING_VALUE);
            });
        }

        @Test
        @SuppressWarnings("unused")
        void getLeftOrThrow_on_right_throws_supplied() {
            LOGGER.info("either.getLeftOrThrow on Right");
            assertThatThrownBy(() -> {
                final var $_ = right(SOME_STRING_VALUE)
                    .getLeftOrThrow(() -> new IllegalArgumentException(OUCH));
            }).isInstanceOf(IllegalArgumentException.class)
              .hasMessage(OUCH);
        }

        @Test
        @SuppressWarnings("unused")
        void getLeftOrThrow_on_left_returns_value() {
            LOGGER.info("either.getLeftOrThrow on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var v = e.getLeftOrThrow(() -> new IllegalArgumentException(OUCH));
                assertThat(v).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @Test
        @SuppressWarnings("unused")
        void get_on_left_throws() {
            LOGGER.info("either.get on Left");
            assertThatThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var $_ = e.get();
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void get_on_right_returns_value() {
            LOGGER.info("either.get on Right");
            assertThatNoException().isThrownBy(() ->
                checkIsRightWithValue(right(SOME_STRING_VALUE), SOME_STRING_VALUE)
            );
        }

        @Test
        @SuppressWarnings("unused")
        void getRight_on_left_throws() {
            LOGGER.info("either.getRight on Left");
            assertThatThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var $_ = e.getRight();
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void getRight_on_right_returns_value() {
            LOGGER.info("either.getRight on Right");
            assertThatNoException().isThrownBy(() ->
                checkIsRightWithValue(right(SOME_STRING_VALUE), SOME_STRING_VALUE)
            );
        }

        @Test
        void getRightOrElse_returns_supplier_on_left() {
            LOGGER.info("either.getRightOrElse on Left");
            final var e = left(SOME_STRING_VALUE);
            assertThat(e.getRightOrElse(() -> SOME_OTHER_STRING_VALUE))
                .isEqualTo(SOME_OTHER_STRING_VALUE);
        }

        @Test
        void getRightOrElse_returns_right_on_right() {
            LOGGER.info("either.getRightOrElse on Right");
            final var e = right(SOME_STRING_VALUE);
            assertThat(e.getRightOrElse(() -> SOME_OTHER_STRING_VALUE))
                .isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void getRightSafe_returns_none_on_left() {
            LOGGER.info("either.getRightSafe on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                checkIsNone(e.getRightSafe());
            });
        }

        @Test
        void getRightSafe_returns_some_on_right() {
            LOGGER.info("either.getRightSafe on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                checkIsSomeWithValue(e.getRightSafe(), SOME_STRING_VALUE);
            });
        }

        @Test
        @SuppressWarnings("unused")
        void getRightOrThrow_on_left_throws_supplied() {
            LOGGER.info("either.getRightOrThrow on Left");
            assertThatThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var $_ = e.getRightOrThrow(() -> new IllegalArgumentException(OUCH));
            }).isInstanceOf(IllegalArgumentException.class)
              .hasMessage(OUCH);
        }

        @Test
        void getRightOrThrow_on_right_returns_value() {
            LOGGER.info("either.getRightOrThrow on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                assertThat(e.getRightOrThrow(() -> new IllegalArgumentException(OUCH)))
                    .isEqualTo(SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Utilities (peeks)")
    class Utilities {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void ifLeft_with_null_consumer_throws() {
            LOGGER.info("either.ifLeft with null-consumer");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).ifLeft(null))
                .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings({"unused", "unchecked"})
        @Test
        void ifLeft_on_right_does_not_invoke_consumer() {
            LOGGER.info("either.ifLeft on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = e.ifLeft(consumer);

                assertThat(cont).isEqualTo(e);
                verify(consumer, times(0)).accept(any());
            });
        }

        @SuppressWarnings({"unused", "unchecked"})
        @Test
        void ifLeft_on_left_invokes_consumer_once() {
            LOGGER.info("either.ifLeft on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = e.ifLeft(consumer);

                assertThat(cont).isEqualTo(e);
                verify(consumer, times(1)).accept(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"unchecked", "unused"})
        @Test
        void ifRight_on_left_does_not_invoke_consumer() {
            LOGGER.info("either.ifRight on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = e.ifRight(consumer);

                assertThat(cont).isEqualTo(e);
                verify(consumer, times(0)).accept(any());
            });
        }

        @SuppressWarnings({"unchecked", "unused"})
        @Test
        void ifRight_on_right_invokes_consumer_once() {
            LOGGER.info("either.ifRight on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = e.ifRight(consumer);

                assertThat(cont).isEqualTo(e);
                verify(consumer, times(1)).accept(SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Behaviour (map/mapLeft/mapEither)")
    class Behaviour_Map {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void map_with_null_function_throws() {
            LOGGER.info("either.map with null-function");
            assertThatThrownBy(() -> right(SOME_STRING_VALUE).map(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void map_on_left_is_noop() {
            LOGGER.info("either.map on Left");
            assertThatNoException().isThrownBy(() -> {
                final Either<String, Integer> e = left(SOME_STRING_VALUE);
                final var mapped = e.map(i -> i + 1);
                checkIsLeftWithValue(mapped, SOME_STRING_VALUE);
            });
        }

        @Test
        void map_on_right_transforms_value() {
            LOGGER.info("either.map on Right");
            assertThatNoException().isThrownBy(() -> {
                final Either<String, Integer> e = right(0);
                final var mapped = e.map(i -> i + 1);
                checkIsRightWithValue(mapped, 1);
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mapLeft_with_null_function_throws() {
            LOGGER.info("either.mapLeft with null-fun");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).map(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void mapLeft_on_right_is_noop() {
            LOGGER.info("either.mapLeft on Right");
            assertThatNoException().isThrownBy(() -> {
                final Either<String, Integer> e = right(0);
                final var mapped = e.mapLeft(String::length);
                checkIsRight(mapped);
            });
        }

        @Test
        void mapLeft_on_left_transforms_value() {
            LOGGER.info("either.mapLeft on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var mapped = e.mapLeft(String::length);
                checkIsLeftWithValue(mapped, SOME_STRING_VALUE.length());
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mapEither_with_null_functions_throws() {
            LOGGER.info("either.mapEither with null-functions");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).mapEither(null, Object::toString))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> right(SOME_STRING_VALUE).mapEither(Object::toString, null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void mapEither_on_left_maps_left() {
            LOGGER.info("either.mapEither on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var mapped = e.mapEither(String::length, Object::toString);
                checkIsLeftWithValue(mapped, SOME_STRING_VALUE.length());
            });
        }

        @Test
        void mapEither_on_right_maps_right() {
            LOGGER.info("either.mapEither on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                final var mapped = e.mapEither(Object::toString, String::length);
                checkIsRightWithValue(mapped, SOME_STRING_VALUE.length());
            });
        }
    }

    @Nested
    @DisplayName("Behaviour (applyTo)")
    class Behaviour_ApplyTo {

        @Test
        void applyTo_with_null_tf_throws() {
            LOGGER.info("either.applyTo with null-TF");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).applyTo(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void applyTo_on_left_with_right_function_leaves_left() {
            LOGGER.info("either.applyTo on Left with right function");
            assertThatNoException().isThrownBy(() -> {
                final Either<String, Integer> e = left(SOME_STRING_VALUE);
                final Either<Fun<String, String>, Fun<Integer, Integer>> tf = right(i -> i + 1);
                checkIsLeftWithValue(e.applyTo(tf), SOME_STRING_VALUE);
            });
        }

        @Test
        void applyTo_on_left_with_left_function_transforms_left() {
            LOGGER.info("either.applyTo on Left with left function");
            assertThatNoException().isThrownBy(() -> {
                final Either<String, Integer> e = left(SOME_STRING_VALUE);
                final Either<Fun<String, Integer>, Fun<Integer, Integer>> tf = left(String::length);
                checkIsLeftWithValue(e.applyTo(tf), SOME_STRING_VALUE.length());
            });
        }

        @Test
        void applyTo_on_right_with_left_function_leaves_right() {
            LOGGER.info("either.applyTo on Right with left function");
            assertThatNoException().isThrownBy(() -> {
                final Either<Integer, String> e = right(SOME_STRING_VALUE);
                final Either<Fun<Integer, Integer>, Fun<String, Integer>> tf = left(i -> i + 1);
                checkIsRightWithValue(e.applyTo(tf), SOME_STRING_VALUE);
            });
        }

        @Test
        void applyTo_on_right_with_right_function_transforms_right() {
            LOGGER.info("either.applyTo on Right with right function");
            assertThatNoException().isThrownBy(() -> {
                final Either<Integer, String> e = right(SOME_STRING_VALUE);
                final Either<Fun<Integer, Integer>, Fun<String, Integer>> tf = right(String::length);
                checkIsRightWithValue(e.applyTo(tf), SOME_STRING_VALUE.length());
            });
        }
    }

    @Nested
    @DisplayName("Behaviour (flatMap)")
    class Behaviour_FlatMap {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void flatMap_with_null_flatMapM_throws() {
            LOGGER.info("either.flatMap with null-flatMapM");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).flatMap(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void flatMap_on_left_is_noop() {
            LOGGER.info("either.flatMap on Left");
            assertThatNoException().isThrownBy(() -> {
                final var e = left(SOME_STRING_VALUE);
                final var boundLeft = e.flatMap(obj -> left(String.valueOf(obj)));
                final var boundRight = e.flatMap(obj -> right(String.valueOf(obj)));

                checkIsLeftWithValue(boundLeft, SOME_STRING_VALUE);
                checkIsLeftWithValue(boundRight, SOME_STRING_VALUE);
            });
        }

        @Test
        void flatMap_on_right_sequences_to_left_or_right() {
            LOGGER.info("either.flatMap on Right");
            assertThatNoException().isThrownBy(() -> {
                final var e = right(SOME_STRING_VALUE);
                final var toLeft = e.flatMap(string -> left(funStringLength.apply(string)));
                final var toRight = e.flatMap(string -> right(funStringLength.apply(string)));

                checkIsLeftWithValue(toLeft, SOME_STRING_VALUE.length());
                checkIsRightWithValue(toRight, SOME_STRING_VALUE.length());
            });
        }
    }

    @Nested
    @DisplayName("Properties and Misc")
    class PropertiesAndMisc {

        @Test
        void swap_twice_is_identity() {
            LOGGER.info("swap().swap() should be identity");
            final var l = left(SOME_STRING_VALUE);
            final var r = right(SOME_STRING_VALUE);

            assertThat(l.swap().swap()).isEqualTo(l);
            assertThat(r.swap().swap()).isEqualTo(r);
        }

        @Test
        void copy_preserves_semantics_not_identity() {
            LOGGER.info("copy() should preserve value and side");
            final var l = left(SOME_STRING_VALUE);
            final var r = right(SOME_STRING_VALUE);

            final var lCopy = l.copy();
            final var rCopy = r.copy();

            assertThat(lCopy).isEqualTo(l);
            assertThat(rCopy).isEqualTo(r);
        }

        @Test
        void swap_left_right_behaviour() {
            LOGGER.info("swap on Left/Right");
            checkIsRightWithValue(left(SOME_STRING_VALUE).swap(), SOME_STRING_VALUE);
            checkIsLeftWithValue(right(SOME_STRING_VALUE).swap(), SOME_STRING_VALUE);
        }

        @Test
        void copy_on_left_and_right() {
            LOGGER.info("copy on Left/Right");
            assertThatNoException().isThrownBy(() -> {
                final var eL = left(SOME_STRING_VALUE);
                final var mappedCopyL = eL.copy().mapLeft(funStringLength);
                checkIsLeftWithValue(eL, SOME_STRING_VALUE);
                checkIsLeftWithValue(mappedCopyL, SOME_STRING_VALUE.length());

                final var eR = right(SOME_STRING_VALUE);
                final var mappedCopyR = eR.copy().map(funStringLength);
                checkIsRightWithValue(eR, SOME_STRING_VALUE);
                checkIsRightWithValue(mappedCopyR, SOME_STRING_VALUE.length());
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void transmogrify_contracts() {
            LOGGER.info("transmogrify NPE contracts and valid usage");
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).transmogrify(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> left(SOME_STRING_VALUE).transmogrify(_$ -> null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                left(SOME_STRING_VALUE).transmogrify(Objects::toString);
                right(SOME_STRING_VALUE).transmogrify(Objects::toString);
            });
        }

        @Test
        void unwind_materializes_without_changing_semantics() {
            LOGGER.info("unwind should evaluate lazily stored suppliers without changing side/value");

            assertThatNoException().isThrownBy(() -> {
                final var invocationCountingFun = invocationCountingFun(Fun.identity());
                final var either = left(SOME_STRING_VALUE)
                    .mapLeft(invocationCountingFun)
                    .mapRight(invocationCountingFun)
                    .swap()
                    .mapLeft(invocationCountingFun)
                    .map(invocationCountingFun);

                assertThat(invocationCountingFun.getInvocationCount()).isEqualTo(0);

                either.unwind();

                assertThat(invocationCountingFun.getInvocationCount()).isEqualTo(2);
                invocationCountingFun.resetInvocationCount();

                final var unwound = either.swap().flatMap(obj -> left(obj.toString())).unwind();

                assertThat(invocationCountingFun.getInvocationCount()).isEqualTo(2);
                assertThat(unwound.isLeft()).isTrue();
                assertThat(unwound.getLeft()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        void toString_executes_without_error() {
            LOGGER.info("toString on Left/Right");
            assertThatNoException().isThrownBy(() -> {
                left(SOME_STRING_VALUE).toString();
                right(SOME_STRING_VALUE).toString();
            });
        }

        @Test
        void arity_reports_right_projection() {
            LOGGER.info("Either.arity");
            assertThat(left(SOME_STRING_VALUE).arity()).isEqualTo(1);
            assertThat(right(SOME_STRING_VALUE).arity()).isEqualTo(1);
        }
    }
}
