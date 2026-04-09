package org.quurz.foomp.base.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.quurz.foomp.base.util.Maybe.*;
import static org.quurz.foomp.base.util.Nothing.nothing;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
@DisplayName("Maybe")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("unused")
class MaybeTest
        extends TestHelper {

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
            assertThatThrownBy(() -> none().ifSome((Consumer<Object>) null)).isInstanceOf(NullPointerException.class);

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

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void ifSome_runnable_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifSome(Runnable) should enforce null contracts and run only on Some");
            // null runnable -> NPE
            assertThatThrownBy(() -> none().ifSome((Runnable) null))
                .isInstanceOf(NullPointerException.class);

            // None: runnable must not run, and Maybe instance is returned unverändert
            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifSome(runnable);
                assertThat(cont).isEqualTo(m);
                verify(runnable, times(0)).run();
            });

            // Some: runnable must run exactly einmal
            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifSome(runnable);
                assertThat(cont).isEqualTo(m);
                verify(runnable, times(1)).run();
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused", "unchecked"})
        @Test
        void ifNone_supplier_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifNone(Supplier) should enforce null contracts and supply only on None");
            // null supplier -> NPE
            assertThatThrownBy(() -> none().ifNone((Supplier<Object>) null))
                .isInstanceOf(NullPointerException.class);

            // supplier that returns null -> NPE
            assertThatThrownBy(() -> none().ifNone(() -> null))
                .isInstanceOf(NullPointerException.class);

            // None: supplier muss genau einmal aufgerufen werden, Ergebnis wird Some(...)
            assertThatNoException().isThrownBy(() -> {
                final var m = Maybe.<String>none();
                final var result = m.ifNone(() -> SOME_STRING_VALUE);
                checkIsSomeWithValue(result, SOME_STRING_VALUE);
            });

            // Some: supplier darf NICHT aufgerufen werden, ursprüngliches Maybe bleibt erhalten
            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var supplier = mockLambda(java.util.function.Supplier.class, () -> SOME_OTHER_STRING_VALUE);
                final var result = m.ifNone(supplier);
                assertThat(result).isEqualTo(m);
                checkIsSomeWithValue(result, SOME_STRING_VALUE);
                verify(supplier, times(0)).get();
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused"})
        @Test
        void ifNone_runnable_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifNone(Runnable) should enforce null contracts and run only on None");
            // null runnable -> NPE
            assertThatThrownBy(() -> none().ifNone((Runnable) null))
                .isInstanceOf(NullPointerException.class);

            // None: runnable muss laufen, Maybe wird unverändert zurückgegeben
            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifNone(runnable);
                assertThat(cont).isEqualTo(m);
                verify(runnable, times(1)).run();
            });

            // Some: runnable darf NICHT laufen, Maybe bleibt wie es ist
            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifNone(runnable);
                assertThat(cont).isEqualTo(m);
                verify(runnable, times(0)).run();
            });
        }

        @SuppressWarnings({"DataFlowIssue", "unused", "unchecked"})
        @Test
        void ifPresentOrElse_behaviour_and_contracts() {
            LOGGER.info("Maybe.ifPresentOrElse should enforce null contracts and run correct branch");
            assertThatThrownBy(() -> none().ifSomeOrElse(_$ -> {}, null))
                .isInstanceOf(NullPointerException.class);

            assertThatNoException().isThrownBy(() -> {
                final var m = none();
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifSomeOrElse(consumer, runnable);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(0)).accept(any());
                verify(runnable, times(1)).run();
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var consumer = mockLambda(Consumer.class, _$ -> {});
                final var runnable = mockLambda(Runnable.class, () -> {});
                final var cont = m.ifSomeOrElse(consumer, runnable);
                assertThat(cont).isEqualTo(m);
                verify(consumer, times(1)).accept(any());
                verify(runnable, times(0)).run();
            });
        }
    }

    @Nested
    @DisplayName("Functional (map, applyTo, flatMap)")
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

        @SuppressWarnings("DataFlowIssue")
        @Nested
        @DisplayName("applyTo")
        class ApplyTo_ {

            @Test
            void applyTo_contracts_and_behaviour() {
                LOGGER.info("Maybe.applyTo should enforce null contracts and apply function only if present");
                assertThatThrownBy(() -> none().applyTo(null))
                    .isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = Maybe.<String>none();
                    final var tf = some((Function<String, Integer>) String::length);
                    final var lifted = m.applyTo(tf);
                    checkIsNone(lifted);
                });

                assertThatThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Integer> fMap = _$ -> null;
                    m.applyTo(some(fMap)).unwind();
                }).isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var lifted = m.applyTo(none()).unwind();
                    checkIsNone(lifted);
                });

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var tf = some(funStringLength);
                    final var lifted = m.applyTo(tf).unwind();
                    checkIsSomeWithValue(lifted, lifted.get());
                });
            }
        }

        @Nested
        @DisplayName("flatMap")
        class FlatMap_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void flatMap_contracts_and_behaviour() {
                LOGGER.info("Maybe.flatMap should enforce null contracts and flatten results");
                assertThatThrownBy(() -> none().flatMap(null))
                    .isInstanceOf(NullPointerException.class);

                assertThatThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Maybe<Integer>> flatMapM = _$ -> null;
                    m.flatMap(flatMapM).unwind();
                }).isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final Fun<String, Maybe<Integer>> flatMapM = s -> some(s.length());
                    final var bound = m.flatMap(flatMapM).unwind();
                    checkIsSomeWithValue(bound, bound.get());
                });
            }
        }

        @Nested
        @DisplayName("flatten (join)")
        class Flatten_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void flatten_contracts_and_behaviour() {
                LOGGER.info("Maybe.flatten should enforce null contracts and flatten nested structures");
                assertThatThrownBy(() -> Maybe.flatten(null))
                    .isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    // None -> None
                    final var mNone = Maybe.<Maybe<String>>none();
                    checkIsNone(Maybe.flatten(mNone));

                    // Some(None) -> None
                    final var mSomeNone = some(Maybe.<String>none());
                    checkIsNone(Maybe.flatten(mSomeNone));

                    // Some(Some(val)) -> Some(val)
                    final var mSomeSome = some(some(SOME_STRING_VALUE));
                    checkIsSomeWithValue(Maybe.flatten(mSomeSome).unwind(), SOME_STRING_VALUE);
                });
            }
        }
    }

    @Nested
    @DisplayName("Conversions and misc")
    class Conversions_And_Misc {

        @Nested
        @DisplayName("with/withNullable")
        class Withers_ {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void with_contracts_and_behaviour() {
                LOGGER.info("Maybe.with should enforce NullPointerException and replace value with Some(newValue)");

                // null -> NPE
                assertThatThrownBy(() -> none().with(null)).isInstanceOf(NullPointerException.class);

                assertThatNoException().isThrownBy(() -> {
                    // on None -> Some(newValue)
                    final var replacedFromNone = Maybe.<Integer>none().with("new");
                    checkIsSomeWithValue(replacedFromNone, "new");

                    // on Some -> Some(newValue)
                    final var replacedFromSome = some(123).with("abc");
                    checkIsSomeWithValue(replacedFromSome, "abc");
                });
            }

            @Test
            void withNullable_behaviour() {
                LOGGER.info("Maybe.withNullable should map null->None and non-null->Some(newValue) regardless of current variant");

                // null -> None (for None and Some)
                assertThatNoException().isThrownBy(() -> {
                    final var fromNone = Maybe.<String>none().withNullable(null);
                    checkIsNone(fromNone);

                    final var fromSome = some("x").withNullable(null);
                    checkIsNone(fromSome);
                });

                // non-null -> Some(newValue) (for None and Some)
                assertThatNoException().isThrownBy(() -> {
                    final var fromNone = Maybe.<String>none().withNullable("z");
                    checkIsSomeWithValue(fromNone, "z");

                    final var fromSome = some("x").withNullable("y");
                    checkIsSomeWithValue(fromSome, "y");
                });
            }
        }

        @Nested
        @DisplayName("filter")
        class Filter_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void filter_contracts_and_behaviour() {
                LOGGER.info("Maybe.filter should enforce null contracts and filter correctly");

                // null predicate -> NPE
                assertThatThrownBy(() -> some(SOME_STRING_VALUE).filter(null))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> Maybe.<String>none().filter(null))
                    .isInstanceOf(NullPointerException.class);

                // None: predicate wird nicht aufgerufen, Ergebnis bleibt None
                assertThatNoException().isThrownBy(() -> {
                    final var m = Maybe.<String>none();
                    final var filtered = m.filter(_$ -> {
                        throw new AssertionError("predicate must not be called for None");
                    });
                    checkIsNone(filtered);
                });

                // Some + predicate = true -> bleibt Some
                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var filtered = m.filter(s -> s.equals(SOME_STRING_VALUE));
                    checkIsSomeWithValue(filtered, SOME_STRING_VALUE);
                });

                // Some + predicate = false -> wird None
                assertThatNoException().isThrownBy(() -> {
                    final var m = some(SOME_STRING_VALUE);
                    final var filtered = m.filter(s -> false);
                    checkIsNone(filtered);
                });
            }
        }

        @Nested
        @DisplayName("zip")
        class Zip_ {

            @SuppressWarnings({"DataFlowIssue", "unused"})
            @Test
            void zip_contracts_and_behaviour() {
                LOGGER.info("Maybe.zip should enforce null contracts and zip correctly");

                // null other / zipper -> NPE
                assertThatThrownBy(() -> some(SOME_STRING_VALUE).zip(null, (a, b) -> a + b))
                    .isInstanceOf(NullPointerException.class);
                assertThatThrownBy(() -> some(SOME_STRING_VALUE).zip(none(), null))
                    .isInstanceOf(NullPointerException.class);

                // None zip None -> None
                assertThatNoException().isThrownBy(() -> {
                    final var a = Maybe.<String>none();
                    final var b = Maybe.<Integer>none();
                    final var zipped = a.zip(b, (s, i) -> s + i);
                    checkIsNone(zipped);
                });

                // None zip Some -> None
                assertThatNoException().isThrownBy(() -> {
                    final var a = Maybe.<String>none();
                    final var b = some(42);
                    final var zipped = a.zip(b, (s, i) -> s + i);
                    checkIsNone(zipped);
                });

                // Some zip None -> None
                assertThatNoException().isThrownBy(() -> {
                    final var a = some(SOME_STRING_VALUE);
                    final var b = Maybe.<Integer>none();
                    final var zipped = a.zip(b, (s, i) -> s + i);
                    checkIsNone(zipped);
                });

                // Some zip Some mit gültigem zipper -> Some(result)
                assertThatNoException().isThrownBy(() -> {
                    final var a = some(SOME_STRING_VALUE);
                    final var b = some(3);
                    final var zipped = a.zip(b, (s, i) -> s + i);
                    checkIsSomeWithValue(zipped, SOME_STRING_VALUE + 3);
                });

                // zipper liefert null -> NPE über requiresNonNullResult2(...)
                assertThatThrownBy(() -> {
                    final var a = some(SOME_STRING_VALUE);
                    final var b = some(3);
                    a.zip(b, (s, i) -> null).unwind();
                }).isInstanceOf(NullPointerException.class);
            }
        }

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
                m.map(fMap).applyTo(some(fMap)).unwind();
                verify(fMap, times(0)).apply(any());
            });

            assertThatNoException().isThrownBy(() -> {
                final var m = some(SOME_STRING_VALUE);
                final var fMap = mockLambda(Fun.class, Fun.identity());

                // map + applyTo: sollten fMap noch nicht ausführen (nur Supplier-Kette aufbauen)
                final var mappedAndLifted = m.map(fMap).applyTo(some(fMap));
                verify(fMap, times(0)).apply(any());

                // flatMap ist jetzt strikt bzgl. Struktur und erzwingt die Auswertung der bisherigen Supplier
                final var cont = mappedAndLifted.flatMap(_$ -> some(SOME_STRING_VALUE));
                verify(fMap, times(2)).apply(any());

                // unwind materialisiert nur noch das Ergebnis von flatMap, ohne fMap erneut aufzurufen
                final var unwound = cont.unwind();
                checkIsSomeWithValue(unwound, SOME_STRING_VALUE);
                verify(fMap, times(2)).apply(any());

                // get() auf dem unwound-Resultat ruft fMap ebenfalls nicht mehr auf
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

        @SuppressWarnings("AssertBetweenInconvertibleTypes")
        @Test
        void equals_and_hashCode_compare_by_variant_and_value() {
            LOGGER.info("Maybe.equals/hashCode should compare None/Some structurally");

            final var none1 = none();
            final var none2 = none();
            final var some1a = some("x");
            final var some1b = some("x");
            final var some2  = some("y");

            // None == None
            assertThat(none1).isEqualTo(none2);
            assertThat(none1.hashCode()).isEqualTo(none2.hashCode());

            // Some(x) == Some(x)
            assertThat(some1a).isEqualTo(some1b);
            assertThat(some1a.hashCode()).isEqualTo(some1b.hashCode());

            // Different values / variants
            assertThat(some1a).isNotEqualTo(some2);
            assertThat(some1a).isNotEqualTo(none1);

            // Different types / null
            assertThat(some1a).isNotEqualTo(null);
            assertThat(some1a).isNotEqualTo("not a maybe");
        }
    }
}
