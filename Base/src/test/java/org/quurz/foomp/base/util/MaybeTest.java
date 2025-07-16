package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.quurz.foomp.base.util.Maybe.maybeFrom;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Maybe.none;
import static org.quurz.foomp.base.util.Maybe.some;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
class MaybeTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(MaybeTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testSome() {
        LOGGER.info("Test Maybe.some");

        assertThatThrownBy(() -> some(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE);
                checkIsSomeWithValue(maybe, SOME_STRING_VALUE);
            });
    }

    @Test
    void testMaybeOfNullable() {
        LOGGER.info("Test Maybe.maybeOfNullable");

        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = maybeOfNullable(null);
                checkIsNone(maybe);
                assertThatThrownBy(maybe::get)
                    .isInstanceOf(NoSuchElementException.class);
                assertThat(maybe.isNone())
                    .isTrue();
            });
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = maybeOfNullable(SOME_STRING_VALUE);
                checkIsSomeWithValue(maybe, SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings({"OptionalAssignedToNull", "DataFlowIssue"})
    @Test
    void testMaybeFrom() {
        LOGGER.info("Test Maybe.maybeFrom");

        assertThatThrownBy(() -> maybeFrom(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = maybeFrom(Optional.empty());

                checkIsNone(maybe);
                assertThatThrownBy(maybe::get)
                    .isInstanceOf(NoSuchElementException.class);
            });
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = maybeFrom(Optional.of(SOME_STRING_VALUE));
                checkIsSomeWithValue(maybe, SOME_STRING_VALUE);
            });
    }

    @Test
    void testGet() {
        LOGGER.info("Test maybe.get");

        final var maybe
            = none();

        assertThatThrownBy(maybe::get)
            .isInstanceOf(NoSuchElementException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final Maybe<String> some
                    = some(SOME_STRING_VALUE);
                assertThatNoException()
                    .isThrownBy(some::get);
                assertThat(some.get())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testGetOrElse() {
        LOGGER.info("Test Maybe.getOrElse");

        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.getOrElse(null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.getOrElse(() -> null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = none();
                assertThat(maybe.getOrElse(() -> SOME_STRING_VALUE))
                    .isEqualTo(SOME_STRING_VALUE);
            });
        assertThatThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            maybe.getOrElse(null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE);
                assertThat(maybe.getOrElse(() -> SOME_OTHER_STRING_VALUE))
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testGetOrThrow() {
        LOGGER.info("Test maybe.getOrThrow");

        assertThatThrownBy(() -> none().getOrThrow(null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> none().getOrThrow(() -> null))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> none().getOrThrow(() -> new IllegalArgumentException(OUCH)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(OUCH);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE);
                maybe.getOrThrow(() -> new IllegalArgumentException(OUCH));
                assertThat(maybe.get())
                    .isEqualTo(SOME_STRING_VALUE);
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unchecked", "unused"})
    @Test
    void testIfSome() {
        LOGGER.info("Test maybe.ifPresent");

        assertThatThrownBy(() -> none().ifSome(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = none();
                final var consumer
                    = mockLambda(Consumer.class, _$ -> {});
                final var cont
                    = maybe.ifSome(consumer);
                assertThat(cont)
                    .isEqualTo(maybe);
                verify(consumer, times(0))
                    .accept(any());
            });
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE);
                final var consumer
                    = mockLambda(Consumer.class, _$ -> {});
                final var cont
                    = maybe.ifSome(consumer);
                assertThat(cont)
                    .isEqualTo(maybe);
                verify(consumer, times(1))
                    .accept(SOME_STRING_VALUE);
                });
        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.ifPresentOrElse(null, () -> {});
        })
        .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings({"DataFlowIssue", "unused", "unchecked"})
    @Test
    void testIfSomeOrElseWithNullRunnable() {
        LOGGER.info("Test maybe.ifPresentOrElse");

        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.ifPresentOrElse(_$ -> {} , null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                = none();
                final var consumer
                    = mockLambda(Consumer.class, _$ -> {});
                final var runnable
                = mockLambda(Runnable.class, () -> {});
                final var cont
                    = maybe.ifPresentOrElse(consumer, runnable);
                assertThat(cont)
                    .isEqualTo(maybe);
                verify(consumer, times(0))
                    .accept(any());
                verify(runnable, times(1))
                    .run();
            });
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE);
                final var consumer
                    = mockLambda(Consumer.class, _$ -> {});
                final var runnable
                    = mockLambda(Runnable.class, () -> {});
                final var cont
                    = maybe.ifPresentOrElse(consumer, runnable);
                assertThat(cont)
                    .isEqualTo(maybe);
                verify(consumer, times(1)).accept(any());
                verify(runnable, times(0)).run();
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testMapWithNullFunction() {
        LOGGER.info("Test maybe.map");

        assertThatThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            maybe.map(null);
        })
        .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.map(null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            final Maybe<String> maybe
                = some(SOME_STRING_VALUE).map(_$ -> null);
            maybe.get();
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final Maybe<String> maybe
                = none();
            final var mapped
                = maybe.map(String::length);
            checkIsNone(mapped);
        });
        assertThatNoException()
            .isThrownBy(() -> {
                final var maybe
                    = some(SOME_STRING_VALUE).map(String::length).unwind();
                checkIsSomeWithValue(maybe, SOME_STRING_VALUE.length());
            });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testLift() {
        LOGGER.info("Test maybe.lift");

        assertThatThrownBy(() -> none().lift(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final Maybe<String> maybe
                = none();
            final Maybe<Function<String, Integer>> liftA
                = some(String::length);
            final var lifted
                = maybe.lift(liftA);
            checkIsNone(lifted);
        });
        assertThatThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final Fun<String, Integer> fMap
                = _$ -> null;
            final var liftA
                = some(fMap);
            maybe.lift(liftA).unwind();
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final var lifted
                = maybe.lift(none()).unwind();
            checkIsNone(lifted);
        });
        assertThatNoException().isThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final var liftA
                = some(funStringLength);
            final var lifted
                = maybe.lift(liftA).unwind();
            checkIsSomeWithValue(lifted, lifted.get());
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testBindWithNullFunction() {
        LOGGER.info("Test maybe.bind");

        assertThatThrownBy(() -> {
            final var maybe
                = none();
            maybe.bind(null);
        })
        .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final Fun<String, Maybe<Integer>> bindM
                = _$ -> null;
            maybe.bind(bindM).unwind();
        })
        .isInstanceOf(NullPointerException.class);
        assertThatNoException().isThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final Fun<String, Maybe<Integer>> bindM
                = string -> some(string.length());
            final var bound
                = maybe.bind(bindM).unwind();
            checkIsSomeWithValue(bound, bound.get());
        });
    }

    @Test
    void testToOptional() {
        LOGGER.info("Test maybe.toOptional");

        final var none
            = none();
        final var optionalFromNone
            = none.toOptional();

        assertThat(optionalFromNone)
            .isEmpty();

        final var some
            = some(SOME_STRING_VALUE);
        final var optionalFromSome
            = some.toOptional();

        assertThat(optionalFromSome)
            .isPresent();
        assertThat(optionalFromSome.get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @Test
    void testCopy() {
        LOGGER.info("Test maybe.copy");

        final var none
            = none();
        final var copyOfNone
            = none.copy();

        checkIsNone(copyOfNone.unwind());

        final var some
            = some(SOME_STRING_VALUE);
        final var copyOfSome
            = some.copy();

        checkIsSome(copyOfSome.unwind());
        assertThat(copyOfSome.get())
            .isEqualTo(SOME_STRING_VALUE);
    }

    @SuppressWarnings("unused")
    @Test
    void testUnwind() {
        LOGGER.info("Test maybe.unwind");

        assertThatNoException().isThrownBy(() -> {
            final var maybe
                = none();
            final var fMap
                = mockLambda(Fun.class, Fun.identity());
            maybe.map(fMap)
                .lift(some(fMap))
                .unwind();

            verify(fMap, times(0))
                .apply(any());
        });
        assertThatNoException().isThrownBy(() -> {
            final var maybe
                = some(SOME_STRING_VALUE);
            final var fMap
                = mockLambda(Fun.class, Fun.identity());

            final var cont
                = maybe
                    .map(fMap)
                    .lift(some(fMap))
                    .bind(_$ -> some(SOME_STRING_VALUE));
            verify(fMap, times(0))
                .apply(any());

            final var unwound
                = cont.unwind();
            checkIsSomeWithValue(unwound, SOME_STRING_VALUE);
            verify(fMap, times(2))
                .apply(any());

            unwound.get();
            verify(fMap, times(2))
                .apply(any());
        });
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testTransmogrify() {
        LOGGER.info("Test maybe.transmogrify");

        assertThatThrownBy(() -> none().transmogrify(null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> some(SOME_STRING_VALUE).transmogrify(_$ -> nothing));
    }

    @Test
    void testToString() {
        LOGGER.info("Test maybe.toString");

        final var stringFromNone
            = none().toString();
        assertThat(stringFromNone)
            .isEqualTo("None[]");

        final var stringFromSome
            = some(SOME_STRING_VALUE).toString();
        assertThat(stringFromSome)
            .isEqualTo("Some[value=%s]".formatted(SOME_STRING_VALUE));
    }

}
