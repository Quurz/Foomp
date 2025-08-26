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
@DisplayName("Maybe")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MaybeTest extends TestHelper {

    private static final Logger LOGGER = getLogger(MaybeTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void some_with_null_value_throws() {
            LOGGER.info("Maybe.some(...) should throw NullPointerException when value is null");
            assertThatThrownBy(() -> some(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void some_with_valid_value_is_some() {
            LOGGER.info("Maybe.some(...) should create Some when value is non-null");
            assertThatNoException().isThrownBy(() -> {
                final var maybe = some(SOME_STRING_VALUE);
                checkIsSomeWithValue(maybe, SOME_STRING_VALUE);
            });
        }

        @Test
        void maybeOfNullable_handles_null_and_non_null() {
            LOGGER.info("Maybe.maybeOfNullable should map null->None and non-null->Some");
            assertThatNoException().isThrownBy(() -> {
                final var noneMaybe = maybeOfNullable(null);
                checkIsNone(noneMaybe);
                assertThatThrownBy(noneMaybe::get).isInstanceOf(NoSuchElementException.class);
                assertThat(noneMaybe.isNone()).isTrue();
            });
            assertThatNoException().isThrownBy(() -> {
                final var someMaybe = maybeOfNullable(SOME_STRING_VALUE);
                checkIsSomeWithValue(someMaybe, SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"OptionalAssignedToNull", "DataFlowIssue"})
        @Test
        void maybeFrom_converts_optional() {
            LOGGER.info("Maybe.maybeFrom should convert Optional to Maybe");
            assertThatThrownBy(() -> maybeFrom(null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var m = maybeFrom(Optional.empty());
                checkIsNone(m);
                assertThatThrownBy(m::get).isInstanceOf(NoSuchElementException.class);
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = maybeFrom(Optional.of(SOME_STRING_VALUE));
                checkIsSomeWithValue(m, SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Accessors")
    class Accessors {

        @Test
        void get_on_none_throws_and_on_some_returns() {
            LOGGER.info("Maybe.get should throw on None and return value on Some");
            final var mNone = none();
            assertThatThrownBy(mNone::get).isInstanceOf(NoSuchElementException.class);

            assertThatNoException().isThrownBy(() -> {
                final var mSome = some(SOME_STRING_VALUE);
                assertThat(mSome.get()).isEqualTo(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void getOrElse_contracts_and_values() {
            LOGGER.info("Maybe.getOrElse should enforce null contracts and return correct fallback/value");
            assertThatThrownBy(() -> none().getOrElse(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> none().getOrElse(() -> null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() ->
                assertThat(none().getOrElse(() -> SOME_STRING_VALUE)).isEqualTo(SOME_STRING_VALUE)
            );

            assertThatThrownBy(() -> some(SOME_STRING_VALUE).getOrElse(null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() ->
                assertThat(some(SOME_STRING_VALUE).getOrElse(() -> SOME_OTHER_STRING_VALUE))
                    .isEqualTo(SOME_STRING_VALUE)
            );
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void getOrThrow_contracts_and_values() {
            LOGGER.info("Maybe.getOrThrow should enforce null contracts and throw supplied/return value");
            assertThatThrownBy(() -> none().getOrThrow(null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> none().getOrThrow(() -> null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> none().getOrThrow(() -> new IllegalArgumentException(OUCH)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OUCH);

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                assertThat(m.getOrThrow(() -> new IllegalArgumentException(OUCH)))
                    .isEqualTo(SOME_STRING_VALUE);
            });
        }
    }

    @Nested
    @DisplayName("Utilities (peeks)")
    class Utilities_Peeks {

        @SuppressWarnings({"DataFlowIssue", "unchecked", "unused"})
        @Test
        void ifSome_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifSome should enforce null contracts and behave as peek");
            assertThatThrownBy(() -> none().ifSome(null)).isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = m.ifSome(consumer);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(0)).accept(any());
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var cont = m.ifSome(consumer);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(1)).accept(SOME_STRING_VALUE);
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused", "unchecked"})
        @Test
        void ifPresentOrElse_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifPresentOrElse should enforce null contracts and run correct branch");
            assertThatThrownBy(() -> none().ifPresentOrElse(_$ -> {}, null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifPresentOrElse(consumer, runnable);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(0)).accept(any());
                verify(runnable, times(1)).run();
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifPresentOrElse(consumer, runnable);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(1)).accept(any());
                verify(runnable, times(0)).run();
            });
        }
    }

    @Nested
    @DisplayName("Functional (map, lift, bind)")
    class Functional {

        @Nested
        @DisplayName("map")
        class Map_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void map_contracts_and_laziness() {
                LOGGER.info("Maybe.map should enforce null contracts and preserve laziness");
                assertThatThrownBy(() -> some(SOME_STRING_VALUE).map(null))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> none().map(null))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> some(SOME_STRING_VALUE).map(_$ -> null).get())
                    .isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var mappedNone = Maybe.<String>none().map(String::length);
                    checkIsNone(mappedNone);
                });

                assertThatNoException().isThrownBy(() -> {
                    final var mappedSome = some(SOME_STRING_VALUE).map(String::length).unwind();
                    checkIsSomeWithValue(mappedSome, SOME_STRING_VALUE.length());
                });
            }
        }

        @Nested
        @DisplayName("lift")
        class Lift_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void lift_contracts_and_behaviour() {
                LOGGER.info("Maybe.lift should enforce null contracts and apply function only if present");
                assertThatThrownBy(() -> none().lift(null))
                    .isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = Maybe.<String>none();
                    final var tf = some((Function<String, Integer>) String::length);
                    final var lifted = m.lift(tf);
                    checkIsNone(lifted);
                });

                assertThatThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Integer> fMap = _$ -> null;
                    m.lift(some(fMap)).unwind();
                }).isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var lifted = m.lift(none()).unwind();
                    checkIsNone(lifted);
                });

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var tf = some(funStringLength);
                    final var lifted = m.lift(tf).unwind();
                    checkIsSomeWithValue(lifted, lifted.get());
                });
            }
        }

        @Nested
        @DisplayName("bind")
        class Bind_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void bind_contracts_and_behaviour() {
                LOGGER.info("Maybe.bind should enforce null contracts and flatten results");
                assertThatThrownBy(() -> none().bind(null))
                    .isInstanceOf(NullPointerException.class);

                assertThatThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Maybe<Integer>> bindM = _$ -> null;
                    m.bind(bindM).unwind();
                }).isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Maybe<Integer>> bindM = s -> some(s.length());
                    final var bound = m.bind(bindM).unwind();
                    checkIsSomeWithValue(bound, bound.get());
                });
            }
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Test
        void toOptional_converts_correctly() {
            LOGGER.info("Maybe.toOptional should convert Some/None to Optional");
            assertThat(none().toOptional()).isEmpty();

            final var someMaybe = some(SOME_STRING_VALUE);
            final var opt = someMaybe.toOptional();
            assertThat(opt).isPresent();
            assertThat(opt.get()).isEqualTo(SOME_STRING_VALUE);
        }

        @Test
        void copy_preserves_value_and_laziness() {
            LOGGER.info("Maybe.copy should preserve value and laziness");
            final var noneCopy = none().copy();
            checkIsNone(noneCopy.unwind());

            final var someCopy = some(SOME_STRING_VALUE).copy();
            checkIsSome(someCopy.unwind());
            assertThat(someCopy.get()).isEqualTo(SOME_STRING_VALUE);
        }

        @SuppressWarnings("unused")
        @Test
        void unwind_materializes_some_and_keeps_none() {
            LOGGER.info("Maybe.unwind should materialize Some and keep None");
            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var fMap = mockLambda(Fun.class, Fun.identity());
                m.map(fMap).lift(some(fMap)).unwind();
                verify(fMap, times(0)).apply(any());
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var fMap = mockLambda(Fun.class, Fun.identity());
                final var cont = m.map(fMap).lift(some(fMap)).bind(_$ -> some(SOME_STRING_VALUE));

                verify(fMap, times(0)).apply(any());

                final var unwound = cont.unwind();
                checkIsSomeWithValue(unwound, SOME_STRING_VALUE);
                verify(fMap, times(2)).apply(any());

                unwound.get();
                verify(fMap, times(2)).apply(any());
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void transmogrify_contracts() {
            LOGGER.info("Maybe.transmogrify should enforce NullPointerException contracts");
            assertThatThrownBy(() -> none().transmogrify(null))
                .isInstanceOf(NullPointerException.class);
            assertThatNoException()
                .isThrownBy(() -> some(SOME_STRING_VALUE).transmogrify(_$ -> nothing));
        }

        @Test
        void toString_formats_variants() {
            LOGGER.info("Maybe.toString should format Some and None");
            assertThat(none().toString()).isEqualTo("None[]");
            assertThat(some(SOME_STRING_VALUE).toString())
                .isEqualTo("Some[value=%s]".formatted(SOME_STRING_VALUE));
        }
    }
}
