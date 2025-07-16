package org.quurz.foomp.base.util;

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
class EitherTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(EitherTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLeftWithNullValue() {
        LOGGER.info("Test Either.left with null-value");

        assertThatThrownBy(() -> left(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testLeftWithValidValue() {
        LOGGER.info("Test Either.left with valid value");

        assertThatNoException()
            .isThrownBy(() -> {
                final var left
                    = left(SOME_STRING_VALUE);
                checkIsLeftWithValue(left, SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRightWithNullValue() {
        LOGGER.info("Test Either.right with null-value");

        assertThatThrownBy(() -> right(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testRightWithValidValue() {
        LOGGER.info("Test Either.right with valid value");

        assertThatNoException().isThrownBy(() -> {
            final var right
                = right(SOME_OTHER_STRING_VALUE);
            checkIsRight(right);
        });
    }

    @Test
    void testIsLeftOnRight() {
        LOGGER.info("Test either.isLeft on Either.Right");

        final var either
            = right(SOME_STRING_VALUE);

        checkIsRight(either);
    }

    @Test
    void testIsLeftOnLeft() {
        LOGGER.info("Test either.isLeft on Either.Left");

        final var either
            = left(SOME_STRING_VALUE);

        checkIsLeft(either);
    }

    @Test
    @SuppressWarnings("unused")
    void testGetLeftOnRight() {
        LOGGER.info("Test either.getLeft on Either.Right");

        assertThatThrownBy(() -> {
            final var either
                = right(SOME_STRING_VALUE);
            final var _$
                = either.getLeft();
        })
        .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetLeftOrElseOnRight() {
        LOGGER.info("Test either.getLeftOrElse on Either.Right");

        final var either
            = right(SOME_STRING_VALUE);

        assertThat(either.getLeftOrElse(() -> SOME_OTHER_STRING_VALUE))
            .isEqualTo(SOME_OTHER_STRING_VALUE);
    }

    @Test
    void testGetLeftOrElseOnLeft() {
        LOGGER.info("Test either.getLeftOrElse on Either.Left");

        final var either
            = left(SOME_STRING_VALUE);

        assertThat(either.getLeftOrElse(() -> SOME_OTHER_STRING_VALUE))
            .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testGetLeftSafeOnRight() {
        LOGGER.info("Test either.getLeftSafe on Either.Right");

        assertThatNoException().isThrownBy(() -> {
            final var either
                = right(SOME_STRING_VALUE);
            checkIsNone(either.getLeftSafe());
        });
    }

    @Test
    void testGetLeftSafeOnLeft() {
        LOGGER.info("Test either.getLeftSafe on Either.Left");

        assertThatNoException().isThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            checkIsSomeWithValue(either.getLeftSafe(), SOME_STRING_VALUE);
        });
    }

    @Test
    @SuppressWarnings("unused")
    void testGetLeftOrThrowOnRight() {
        LOGGER.info("Test either.getLeftOrThrow on Either.Right");

        assertThatThrownBy(() -> {
            final var _$
                = right(SOME_STRING_VALUE).getLeftOrThrow(() -> new IllegalArgumentException(OUCH));
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(OUCH);
    }

    @Test
    @SuppressWarnings("unused")
    void testGetLeftOrThrowOnLeft() {
        LOGGER.info("Test either.getLeftOrThrow on Either.Left");

        assertThatNoException().isThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            final var value
                = either.getLeftOrThrow(() -> new IllegalArgumentException(OUCH));
            assertThat(value)
                .isEqualTo(SOME_STRING_VALUE);
        });
    }

    @Test
    @SuppressWarnings("unused")
    void testGetOnLeft() {
        LOGGER.info("Test either.get on Either.Left");

        assertThatThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            final var _$
                = either.get();
        })
        .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetOnRight() {
        LOGGER.info("Test either.get on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> checkIsRightWithValue(right(SOME_STRING_VALUE), SOME_STRING_VALUE));
    }

    @Test
    @SuppressWarnings("unused")
    void testGetRightOnLeft() {
        LOGGER.info("Test either.getRight on Either.Left");

        assertThatThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            final var _$
                = either.getRight();
        })
        .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetRightOnRight() {
        LOGGER.info("Test either.getRight on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> checkIsRightWithValue(right(SOME_STRING_VALUE), SOME_STRING_VALUE));
    }

    @Test
    void testGetRightOrElseOnLeft() {
        LOGGER.info("Test either.getRightOrElse on Either.Left");

        final var either
            = left(SOME_STRING_VALUE);

        assertThat(either.getRightOrElse(() -> SOME_OTHER_STRING_VALUE))
            .isEqualTo(SOME_OTHER_STRING_VALUE);
    }

    @Test
    void testGetRightOrElseOnRight() {
        LOGGER.info("Test either.getRightOrElse on Either.Right");

        final var either
            = right(SOME_STRING_VALUE);

        assertThat(either.getRightOrElse(() -> SOME_OTHER_STRING_VALUE))
            .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testGetRightSafeOnLeft() {
        LOGGER.info("Test either.getRightSafe on Either.Left");

        assertThatNoException().isThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            checkIsNone(either.getRightSafe());
        });
    }

    @Test
    void testGetRightSafeOnRight() {
        LOGGER.info("Test either.getRightSafe on Either.Right");

        assertThatNoException().isThrownBy(() -> {
           final var either
                = right(SOME_STRING_VALUE);
           checkIsSomeWithValue(either.getRightSafe(), SOME_STRING_VALUE);
        });
    }

    @Test
    @SuppressWarnings("unused")
    void testGetRightOrThrowOnLeft() {
        LOGGER.info("Test either.getRightOrThrow on Either.Left");

        assertThatThrownBy(() -> {
            final var either
                = left(SOME_STRING_VALUE);
            final var _$
                = either.getRightOrThrow(() -> new IllegalArgumentException(OUCH));
        })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(OUCH);
    }

    @Test
    void testGetRightOrThrowOnRight() {
        LOGGER.info("Test either.getRightOrThrow on Either.Right");

        assertThatNoException().isThrownBy(() -> {
            final var either
                = right(SOME_STRING_VALUE);
            assertThat(either.getRightOrThrow(() -> new IllegalArgumentException(OUCH)))
                .isEqualTo(SOME_STRING_VALUE);
        });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testIfLeftWithNullConsumer() {
        LOGGER.info("Test either.ifLeft with null-consumer");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).ifLeft(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Test
    void testIfLeftOnRight() {
        LOGGER.info("Test either.ifLeft on Either.Right");

        assertThatNoException()
                .isThrownBy(() -> {
                    final var either
                        = right(SOME_STRING_VALUE);
                    final var consumer
                        = mockLambda(Consumer.class, _$ -> {});
                    final var cont
                        = either.ifLeft(consumer);

                    assertThat(cont)
                        .isEqualTo(either);
                    verify(consumer, times(0))
                        .accept(any());
                });
    }

    @SuppressWarnings({"unused", "unchecked"})
    @Test
    void testIfLeftOnLeft() {
        LOGGER.info("Test either.ifLeft on Either.Left");

        assertThatNoException()
                .isThrownBy(() -> {
                    final var either
                        = left(SOME_STRING_VALUE);
                    final var consumer
                        = mockLambda(Consumer.class, _$ -> {});
                    final var cont
                        = either.ifLeft(consumer);

                    assertThat(cont)
                        .isEqualTo(either);
                    verify(consumer, times(1))
                        .accept(SOME_STRING_VALUE);
                });
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Test
    void testIfRightOnLeft() {
        LOGGER.info("Test either.ifRight on Either.Left");

        assertThatNoException()
                .isThrownBy(() -> {
                    final var either
                        = left(SOME_STRING_VALUE);
                    final var consumer
                        = mockLambda(Consumer.class, _$ -> {});
                    final var cont
                        = either.ifRight(consumer);

                    assertThat(cont)
                        .isEqualTo(either);
                    verify(consumer, times(0))
                        .accept(any());
                });
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Test
    void testIfRightOnRight() {
        LOGGER.info("Test either.ifRight on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = right(SOME_STRING_VALUE);
                final var consumer
                    = mockLambda(Consumer.class, _$ -> {});
                final var cont
                    = either.ifRight(consumer);

                assertThat(cont)
                    .isEqualTo(either);
                verify(consumer, times(1))
                    .accept(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapWithNullFunction() {
        LOGGER.info("Test either.map with null-function");

        assertThatThrownBy(() -> right(SOME_STRING_VALUE).map(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testMapOnLeft() {
        LOGGER.info("Test either.map on Either.Left");

        assertThatNoException()
            .isThrownBy(() -> {
                final Either<String, Integer> either
                    = left(SOME_STRING_VALUE);
                final var mapped
                    = either.map(i -> i + 1);

                checkIsLeftWithValue(mapped, SOME_STRING_VALUE);
            });
    }

    @Test
    void testMapOnRight() {
        LOGGER.info("Test either.map on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> {
                final Either<String, Integer> either
                    = right(0);
                final var mapped
                    = either.map(i -> i + 1);

                checkIsRightWithValue(mapped, 1);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapLeftWithNullFun() {
        LOGGER.info("Test either.map with null-fun");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).map(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testMapLeftOnRight() {
        LOGGER.info("Test either.mapLeft on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> {
                final Either<String, Integer> either
                    = right(0);
                final var mapped
                    = either.mapLeft(String::length);

                checkIsRight(mapped);
            });
    }

    @Test
    void testMapLeftOnLeft() {
        LOGGER.info("Test either.mapLeft on Either.Left");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = left(SOME_STRING_VALUE);
                final var mapped
                    = either.mapLeft(String::length);

                checkIsLeftWithValue(mapped, SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMapEitherWithNullFunctions() {
        LOGGER.info("Test either.mapEither with null-functions");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).mapEither(null, Object::toString))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> right(SOME_STRING_VALUE).mapEither(Object::toString, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testMapEitherOnLeft() {
        LOGGER.info("Test either.mapEither on Either.Left");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = left(SOME_STRING_VALUE);
                final var mapped
                    = either.mapEither(String::length, Object::toString);

                checkIsLeftWithValue(mapped, SOME_STRING_VALUE.length());
            });
    }

    @Test
    void testMapEitherOnRight() {
        LOGGER.info("Test either.mapEither on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = right(SOME_STRING_VALUE);
                final var mapped
                    = either.mapEither(Object::toString, String::length);

                checkIsRightWithValue(mapped, SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testLiftWithNullLiftA() {
        LOGGER.info("Test either.lift with null-liftA");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).lift(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testLiftOnLeftWithValidRightLiftA() {
        LOGGER.info("Test either.lift on Either.Left with valid right liftA");

        assertThatNoException()
            .isThrownBy(() -> {
                final Either<String, Integer> either
                    = left(SOME_STRING_VALUE);
                final Either<Fun<String, String>, Fun<Integer, Integer>> liftA
                    = right(i -> i + 1);

                checkIsLeftWithValue(either.lift(liftA), SOME_STRING_VALUE);
            });
    }

    @Test
    void testLiftOnLeftWithValidLeftLiftA() {
        LOGGER.info("Test either.lift on Either.Left with valid left liftA");

        assertThatNoException()
                .isThrownBy(() -> {
                    final Either<String, Integer> either
                        = left(SOME_STRING_VALUE);
                    final Either<Fun<String, Integer>, Fun<Integer, Integer>> liftA
                        = left(String::length);

                    checkIsLeftWithValue(either.lift(liftA), SOME_STRING_VALUE.length());
                });
    }

    @Test
    void testLiftOnRightWithValidLeftLiftA() {
        LOGGER.info("Test either.lift on Either.Right with valid left liftA");

        assertThatNoException()
                .isThrownBy(() -> {
                    final Either<Integer, String> either
                        = right(SOME_STRING_VALUE);
                    final Either<Fun<Integer, Integer>, Fun<String, Integer>> liftA
                        = left(i -> i + 1);

                    checkIsRightWithValue(either.lift(liftA), SOME_STRING_VALUE);
                });
    }

    @Test
    void testLiftOnRightWithValidRightLiftA() {
        LOGGER.info("Test either.lift on Either.Right with valid right liftA");

        assertThatNoException()
                .isThrownBy(() -> {
                    final Either<Integer, String> either
                            = right(SOME_STRING_VALUE);
                    final Either<Fun<Integer, Integer>, Fun<String, Integer>> liftA
                            = right(String::length);

                    checkIsRightWithValue(either.lift(liftA), SOME_STRING_VALUE.length());
                });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBindWithNullBindM() {
        LOGGER.info("Test either.bind with null-bindM");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).bind(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testBindOnLeftWithValidBindM() {
        LOGGER.info("Test either.bind on Either.Left with valid bindM");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = left(SOME_STRING_VALUE);
                final var boundLeft
                    = either.bind(obj -> left(String.valueOf(obj)));
                final var boundRight
                    = either.bind(obj -> right(String.valueOf(obj)));

                checkIsLeftWithValue(boundLeft, SOME_STRING_VALUE);
                checkIsLeftWithValue(boundRight, SOME_STRING_VALUE);
            });
    }

    @Test
    void testBindOnRightWithValidBindM() {
        LOGGER.info("Test either.bind on Either.Right with valid bindM");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = right(SOME_STRING_VALUE);
                final var boundLeft
                    = either.bind(string -> left(funStringLength.apply(string)));
                final var boundRight
                    = either.bind(string -> right(funStringLength.apply(string)));

                checkIsLeftWithValue(boundLeft, SOME_STRING_VALUE.length());
                checkIsRightWithValue(boundRight, SOME_STRING_VALUE.length());
            });
    }

    @Test
    void testSwapOnLeft() {
        LOGGER.info("Test either.swap on Either.Left");

        checkIsRightWithValue(left(SOME_STRING_VALUE).swap(), SOME_STRING_VALUE);
    }

    @Test
    void testSwapOnRight() {
        LOGGER.info("Test either.swap on Either.Right");

        checkIsLeftWithValue(right(SOME_STRING_VALUE).swap(), SOME_STRING_VALUE);
    }

    @Test
    void testCopyOnLeft() {
        LOGGER.info("Test either.copy on Either.Left");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = left(SOME_STRING_VALUE);
                final var mappedCopy
                    = either.copy().mapLeft(funStringLength);

                checkIsLeftWithValue(either, SOME_STRING_VALUE);
                checkIsLeftWithValue(mappedCopy, SOME_STRING_VALUE.length());
            });
    }

    @Test
    void testCopyOnRight() {
        LOGGER.info("Test either.copy on Either.Right");

        assertThatNoException()
            .isThrownBy(() -> {
                final var either
                    = right(SOME_STRING_VALUE);
                final var mappedCopy
                    = either.copy().map(funStringLength);

                checkIsRightWithValue(either, SOME_STRING_VALUE);
                checkIsRightWithValue(mappedCopy, SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testTransmogrifyWithNullTransmogrifier() {
        LOGGER.info("Test either.transmogrify with null-transmogrifier");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).transmogrify(null))
            .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrifyWithNullReturningTransmogrifier() {
        LOGGER.info("Test either.transmogrify with null-returning transmogrifier");

        assertThatThrownBy(() -> left(SOME_STRING_VALUE).transmogrify(_$ -> null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testTransmogrifyWithValidTransmogrifier() {
        LOGGER.info("Test either.transmogrify with valid transmogrifier");

        assertThatNoException()
            .isThrownBy(() -> {
                final var left
                    = left(SOME_STRING_VALUE);
                final var right
                    = right(SOME_STRING_VALUE);

                left.transmogrify(Objects::toString);
                right.transmogrify(Objects::toString);
            });
    }

    @Test
    void testUnwind() {
        LOGGER.info("Test either.unwind");

        assertThatNoException()
            .isThrownBy(() -> {
                final var invocationCountingFun
                    = invocationCountingFun(Fun.identity());
                final var either
                    = left(SOME_STRING_VALUE)
                        .mapLeft(invocationCountingFun)
                        .mapRight(invocationCountingFun)
                        .swap()
                        .mapLeft(invocationCountingFun)
                        .map(invocationCountingFun);

                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(0);

                either.unwind();

                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(2);
                invocationCountingFun.resetInvocationCount();

                final var unwound
                    = either.swap().bind(obj -> left(obj.toString())).unwind();

                assertThat(invocationCountingFun.getInvocationCount())
                    .isEqualTo(2);
                assertThat(unwound.isLeft())
                    .isTrue();
                assertThat(unwound.getLeft())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void testToString() {
        LOGGER.info("Test either.toString");

        assertThatNoException()
            .isThrownBy(() -> {
                left(SOME_STRING_VALUE).toString();
                right(SOME_STRING_VALUE).toString();
            });
    }

    @Test
    void testArity() {
        LOGGER.info("Test Either.arity");

        assertThat(left(SOME_STRING_VALUE).arity())
            .isEqualTo(1);
        assertThat(right(SOME_STRING_VALUE).arity())
            .isEqualTo(1);
    }

}
