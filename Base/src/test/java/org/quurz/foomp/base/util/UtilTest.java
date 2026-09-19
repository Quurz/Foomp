package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Util")
class UtilTest {

    @Nested
    @DisplayName("requireNonNegative(int, Supplier)")
    class RequireNonNegativeIntOverload {

        @Test
        @DisplayName("returns value when value is positive")
        void returnsValueWhenPositive() throws Exception {
            assertThat(Util.requireNonNegative(42, () -> new Exception("Negative")))
                    .isEqualTo(42);
        }

        @Test
        @DisplayName("returns value when value is zero")
        void returnsValueWhenZero() throws Exception {
            assertThat(Util.requireNonNegative(0, () -> new Exception("Negative")))
                    .isZero();
        }

        @Test
        @DisplayName("throws exception when value is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requireNonNegative(-1, () -> new IllegalArgumentException("Must be non-negative")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be non-negative");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when exceptionSupplier is null or returns null")
        void throwsOnNullSupplier() {
            assertThatThrownBy(() -> Util.requireNonNegative(1, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegative(-1, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requireNonNegative(Duration, Supplier)")
    class RequireNonNegativeDurationOverload {

        @Test
        @DisplayName("returns duration when positive")
        void returnsDurationWhenPositive() throws Exception {
            final var duration = Duration.ofSeconds(5);
            assertThat(Util.requireNonNegative(duration, () -> new Exception("Negative")))
                    .isSameAs(duration);
        }

        @Test
        @DisplayName("returns duration when zero")
        void returnsDurationWhenZero() throws Exception {
            assertThat(Util.requireNonNegative(Duration.ZERO, () -> new Exception("Negative")))
                    .isEqualTo(Duration.ZERO);
        }

        @Test
        @DisplayName("throws exception when duration is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requireNonNegative(Duration.ofSeconds(-1), () -> new IllegalArgumentException("Must be non-negative")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be non-negative");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when duration or supplier is null")
        void throwsOnNull() {
            assertThatThrownBy(() -> Util.requireNonNegative((Duration) null, () -> new Exception("Fail")))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegative(Duration.ofSeconds(1), null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegative(Duration.ofSeconds(-1), () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requireNonNegativeInt(int, Supplier)")
    class RequireNonNegativeInt {

        @Test
        @DisplayName("returns value when value is positive")
        void returnsValueWhenPositive() throws Exception {
            assertThat(Util.requireNonNegativeInt(42, () -> new Exception("Negative")))
                    .isEqualTo(42);
        }

        @Test
        @DisplayName("returns value when value is zero")
        void returnsValueWhenZero() throws Exception {
            assertThat(Util.requireNonNegativeInt(0, () -> new Exception("Negative")))
                    .isZero();
        }

        @Test
        @DisplayName("throws exception when value is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requireNonNegativeInt(-1, () -> new IllegalArgumentException("Must be non-negative")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be non-negative");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when exceptionSupplier is null or returns null")
        void throwsOnNullSupplier() {
            assertThatThrownBy(() -> Util.requireNonNegativeInt(1, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegativeInt(-1, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requirePositive(int, Supplier)")
    class RequirePositiveIntOverload {

        @Test
        @DisplayName("returns value when value is positive")
        void returnsValueWhenPositive() throws Exception {
            assertThat(Util.requirePositive(10, () -> new Exception("Non-positive")))
                    .isEqualTo(10);
        }

        @Test
        @DisplayName("throws exception when value is zero")
        void throwsWhenZero() {
            assertThatThrownBy(() -> Util.requirePositive(0, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @Test
        @DisplayName("throws exception when value is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requirePositive(-5, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when exceptionSupplier is null or returns null")
        void throwsOnNullSupplier() {
            assertThatThrownBy(() -> Util.requirePositive(1, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositive(0, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requirePositive(Duration, Supplier)")
    class RequirePositiveDurationOverload {

        @Test
        @DisplayName("returns duration when positive")
        void returnsDurationWhenPositive() throws Exception {
            final var duration = Duration.ofMillis(500);
            assertThat(Util.requirePositive(duration, () -> new Exception("Non-positive")))
                    .isSameAs(duration);
        }

        @Test
        @DisplayName("throws exception when duration is zero")
        void throwsWhenZero() {
            assertThatThrownBy(() -> Util.requirePositive(Duration.ZERO, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @Test
        @DisplayName("throws exception when duration is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requirePositive(Duration.ofSeconds(-1), () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when duration or supplier is null")
        void throwsOnNull() {
            assertThatThrownBy(() -> Util.requirePositive((Duration) null, () -> new Exception("Fail")))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositive(Duration.ofSeconds(1), null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositive(Duration.ZERO, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requirePositiveInt(int, Supplier)")
    class RequirePositiveInt {

        @Test
        @DisplayName("returns value when value is positive")
        void returnsValueWhenPositive() throws Exception {
            assertThat(Util.requirePositiveInt(10, () -> new Exception("Non-positive")))
                    .isEqualTo(10);
        }

        @Test
        @DisplayName("throws exception when value is zero")
        void throwsWhenZero() {
            assertThatThrownBy(() -> Util.requirePositiveInt(0, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @Test
        @DisplayName("throws exception when value is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requirePositiveInt(-5, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when exceptionSupplier is null or returns null")
        void throwsOnNullSupplier() {
            assertThatThrownBy(() -> Util.requirePositiveInt(1, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositiveInt(0, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requireNonNegativeDuration(Duration, Supplier)")
    class RequireNonNegativeDuration {

        @Test
        @DisplayName("returns duration when positive")
        void returnsDurationWhenPositive() throws Exception {
            final var duration = Duration.ofSeconds(5);
            assertThat(Util.requireNonNegativeDuration(duration, () -> new Exception("Negative")))
                    .isSameAs(duration);
        }

        @Test
        @DisplayName("returns duration when zero")
        void returnsDurationWhenZero() throws Exception {
            assertThat(Util.requireNonNegativeDuration(Duration.ZERO, () -> new Exception("Negative")))
                    .isEqualTo(Duration.ZERO);
        }

        @Test
        @DisplayName("throws exception when duration is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requireNonNegativeDuration(Duration.ofSeconds(-1), () -> new IllegalArgumentException("Must be non-negative")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be non-negative");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when duration or supplier is null")
        void throwsOnNull() {
            assertThatThrownBy(() -> Util.requireNonNegativeDuration(null, () -> new Exception("Fail")))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegativeDuration(Duration.ofSeconds(1), null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requireNonNegativeDuration(Duration.ofSeconds(-1), () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requirePositiveDuration(Duration, Supplier)")
    class RequirePositiveDuration {

        @Test
        @DisplayName("returns duration when positive")
        void returnsDurationWhenPositive() throws Exception {
            final var duration = Duration.ofMillis(500);
            assertThat(Util.requirePositiveDuration(duration, () -> new Exception("Non-positive")))
                    .isSameAs(duration);
        }

        @Test
        @DisplayName("throws exception when duration is zero")
        void throwsWhenZero() {
            assertThatThrownBy(() -> Util.requirePositiveDuration(Duration.ZERO, () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @Test
        @DisplayName("throws exception when duration is negative")
        void throwsWhenNegative() {
            assertThatThrownBy(() -> Util.requirePositiveDuration(Duration.ofSeconds(-1), () -> new IllegalArgumentException("Must be positive")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Must be positive");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NullPointerException when duration or supplier is null")
        void throwsOnNull() {
            assertThatThrownBy(() -> Util.requirePositiveDuration(null, () -> new Exception("Fail")))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositiveDuration(Duration.ofSeconds(1), null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> Util.requirePositiveDuration(Duration.ZERO, () -> null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("requireNonEmpty(Collection, Supplier)")
    class RequireNonEmptyCollection {

        @Test
        @DisplayName("returns same instance if collection is not empty")
        void returnsSameInstance() throws Exception {
            final var list = new ArrayList<String>();
            list.add("test");
            assertThat(Util.requireNonEmpty(list, () -> new Exception("Fail")))
                    .isSameAs(list);
        }

        @Test
        @DisplayName("throws custom exception if collection is empty")
        void throwsOnEmpty() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new ArrayList<>(), () -> new Exception("Empty")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Empty");
        }
    }

    @Nested
    @DisplayName("requireNonEmpty(Map, Supplier)")
    class RequireNonEmptyMap {

        @Test
        @DisplayName("returns same instance if map is not empty")
        void returnsSameInstance() throws Exception {
            final var map = new HashMap<String, String>();
            map.put("k", "v");
            assertThat(Util.requireNonEmpty(map, () -> new Exception("Fail")))
                    .isSameAs(map);
        }

        @Test
        @DisplayName("throws custom exception if map is empty")
        void throwsOnEmpty() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), () -> new Exception("Empty")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Empty");
        }
    }

    @Nested
    @DisplayName("requireNonNullElements(Collection, ...)")
    class RequireNonNullElementsCollectionNew {

        @Test
        @DisplayName("returns same instance if all elements are non-null")
        void returnsSameInstance() throws Exception {
            final var list = new ArrayList<String>();
            list.add("a");
            assertThat(Util.requireNonNullElements(list, i -> new Exception("Fail")))
                    .isSameAs(list);
        }

        @Test
        @DisplayName("throws custom exception with index for null element")
        void throwsOnNullElement() {
            final var list = new ArrayList<String>();
            list.add("a");
            list.add(null);
            assertThatThrownBy(() -> Util.requireNonNullElements(list, i -> new Exception("Null at " + i)))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Null at 1");
        }

        @Test
        @DisplayName("executes andThen receiver for all elements")
        void executesAndThenReceiver() throws Exception {
            final var list = new ArrayList<String>();
            list.add("a");
            list.add("b");
            final var collected = new ArrayList<String>();
            assertThat(Util.requireNonNullElements(list, collected::add, i -> new Exception("Fail")))
                    .isSameAs(list);
            assertThat(collected).containsExactly("a", "b");
        }
    }

    @Nested
    @DisplayName("requireNonNullElements(Array, ...)")
    class RequireNonNullElementsArrayNew {

        @Test
        @DisplayName("returns same instance if all elements are non-null")
        void returnsSameInstance() throws Exception {
            final String[] array = {"a", "b"};
            assertThat(Util.requireNonNullElements(array, i -> new Exception("Fail")))
                    .isSameAs(array);
        }

        @Test
        @DisplayName("throws custom exception with index for null element")
        void throwsOnNullElement() {
            final String[] array = {"a", null};
            assertThatThrownBy(() -> Util.requireNonNullElements(array, i -> new Exception("Null at " + i)))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Null at 1");
        }

        @Test
        @DisplayName("executes andThen receiver for all array elements")
        void executesAndThenReceiver() throws Exception {
            final String[] array = {"a", "b"};
            final var collected = new ArrayList<String>();
            Util.requireNonNullElements(array, collected::add, i -> new Exception("Fail"));
            assertThat(collected).containsExactly("a", "b");
        }
    }

    @Nested
    @DisplayName("requireNonNullElementsInCollection(Collection, Fun)")
    class RequireNonNullElementsCollection {

        @Test
        @DisplayName("returns same instance if all elements are non-null")
        void returnsSameInstance() throws Exception {
            final var list = new ArrayList<String>();
            list.add("a");
            assertThat(Util.requireNonNullElementsInCollection(list, i -> new Exception("Fail")))
                    .isSameAs(list);
        }

        @Test
        @DisplayName("throws custom exception with index for null element")
        void throwsOnNullElement() {
            final var list = new ArrayList<String>();
            list.add("a");
            list.add(null);
            assertThatThrownBy(() -> Util.requireNonNullElementsInCollection(list, i -> new Exception("Null at " + i)))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Null at 1");
        }

        @Test
        @DisplayName("executes andThen receiver for all elements")
        void executesAndThenReceiver() throws Exception {
            final var list = new ArrayList<String>();
            list.add("a");
            list.add("b");
            final var collected = new ArrayList<String>();
            assertThat(Util.requireNonNullElementsInCollection(list, collected::add, i -> new Exception("Fail")))
                    .isSameAs(list);
            assertThat(collected).containsExactly("a", "b");
        }
    }

    @Nested
    @DisplayName("requireNonNullElementsInArray(Array, Fun)")
    class RequireNonNullElementsArray {

        @Test
        @DisplayName("returns same instance if all elements are non-null")
        void returnsSameInstance() throws Exception {
            final String[] array = {"a", "b"};
            assertThat(Util.requireNonNullElementsInArray(array, i -> new Exception("Fail")))
                    .isSameAs(array);
        }

        @Test
        @DisplayName("throws custom exception with index for null element")
        void throwsOnNullElement() {
            final String[] array = {"a", null};
            assertThatThrownBy(() -> Util.requireNonNullElementsInArray(array, i -> new Exception("Null at " + i)))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Null at 1");
        }

        @Test
        @DisplayName("executes andThen receiver for all array elements")
        void executesAndThenReceiver() throws Exception {
            final String[] array = {"a", "b"};
            final var collected = new ArrayList<String>();
            Util.requireNonNullElementsInArray(array, collected::add, i -> new Exception("Fail"));
            assertThat(collected).containsExactly("a", "b");
        }
    }

    @Nested
    @DisplayName("requireNonNullResult(Function, Fun)")
    class RequireNonNullResultUnary {

        @Test
        @DisplayName("returns working function that returns same instance")
        void returnsWorkingFunction() {
            final var fun = Util.requireNonNullResult(s -> s, s -> new RuntimeException("Fail: " + s));
            final String input = "test";
            assertThat(fun.apply(input)).isSameAs(input);
        }

        @Test
        @DisplayName("returned function throws custom exception on null result")
        void throwsOnNullResult() {
            final var fun = Util.requireNonNullResult(s -> null, s -> new RuntimeException("Null for " + s));
            assertThatThrownBy(() -> fun.apply("input"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Null for input");
        }

        @Test
        @DisplayName("returned function throws NPE on null argument")
        void throwsOnNullArgument() {
            final var fun = Util.requireNonNullResult(s -> s, s -> new RuntimeException("Fail"));
            assertThatThrownBy(() -> fun.apply(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x");
        }
    }

    @Nested
    @DisplayName("requireNonNullResult(BiFunction, Fun2)")
    class RequireNonNullResultBinary {

        @Test
        @DisplayName("returns working function that returns same instance")
        void returnsWorkingFunction() {
            final var fun = Util.requireNonNullResult((a, b) -> a, (a, b) -> new RuntimeException("Fail"));
            final String input = "test";
            assertThat(fun.apply(input, "other")).isSameAs(input);
        }

        @Test
        @DisplayName("returned function throws custom exception on null result")
        void throwsOnNullResult() {
            final var fun = Util.requireNonNullResult((a, b) -> null, (a, b) -> new RuntimeException("Null for " + a + b));
            assertThatThrownBy(() -> fun.apply("a", "b"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Null for ab");
        }

        @Test
        @DisplayName("returned function throws NPE on null arguments")
        void throwsOnNullArguments() {
            final var fun = Util.requireNonNullResult((a, b) -> a, (a, b) -> new RuntimeException("Fail"));
            assertThatThrownBy(() -> fun.apply(null, "b"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x1");
            assertThatThrownBy(() -> fun.apply("a", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x2");
        }
    }

    @Nested
    @DisplayName("requireNonNullResult1")
    class RequireNonNullResult1 {

        @Test
        @DisplayName("returns working function that returns same instance")
        void returnsWorkingFunction() {
            final var fun = Util.requireNonNullResult1(s -> s, s -> new RuntimeException("Fail: " + s));
            final String input = "test";
            assertThat(fun.apply(input)).isSameAs(input);
        }

        @Test
        @DisplayName("returned function throws custom exception on null result")
        void throwsOnNullResult() {
            final var fun = Util.requireNonNullResult1(s -> null, s -> new RuntimeException("Null for " + s));
            assertThatThrownBy(() -> fun.apply("input"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Null for input");
        }

        @Test
        @DisplayName("returned function throws NPE on null argument")
        void throwsOnNullArgument() {
            final var fun = Util.requireNonNullResult1(s -> s, s -> new RuntimeException("Fail"));
            assertThatThrownBy(() -> fun.apply(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x");
        }
    }

    @Nested
    @DisplayName("requireNonNullResult2")
    class RequireNonNullResult2 {

        @Test
        @DisplayName("returns working function that returns same instance")
        void returnsWorkingFunction() {
            final var fun = Util.requireNonNullResult2((a, b) -> a, (a, b) -> new RuntimeException("Fail"));
            final String input = "test";
            assertThat(fun.apply(input, "other")).isSameAs(input);
        }

        @Test
        @DisplayName("returned function throws custom exception on null result")
        void throwsOnNullResult() {
            final var fun = Util.requireNonNullResult2((a, b) -> null, (a, b) -> new RuntimeException("Null for " + a + b));
            assertThatThrownBy(() -> fun.apply("a", "b"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Null for ab");
        }

        @Test
        @DisplayName("returned function throws NPE on null arguments")
        void throwsOnNullArguments() {
            final var fun = Util.requireNonNullResult2((a, b) -> a, (a, b) -> new RuntimeException("Fail"));
            assertThatThrownBy(() -> fun.apply(null, "b"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x1");
            assertThatThrownBy(() -> fun.apply("a", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("x2");
        }
    }

    @Nested
    @DisplayName("isSubSet & requireSubSet")
    class SubsetTests {

        @Test
        @DisplayName("isSubSet returns true for subset or equal sets")
        void isSubSetWorks() {
            assertThat(Util.isSubSet(Set.of(1, 2), Set.of(1))).isTrue();
            assertThat(Util.isSubSet(Set.of(1, 2), Set.of(1, 2))).isTrue();
            assertThat(Util.isSubSet(Set.of(1), Set.of(1, 2))).isFalse();
        }

        @Test
        @DisplayName("requireSubSet returns same instance if condition holds")
        void requireSubSetReturnsSame() throws Exception {
            final var superSet = Set.of(1, 2);
            final var subSet = new HashSet<>(Set.of(1));
            assertThat(Util.requireSubSet(superSet, subSet, (sup, sub) -> new Exception("Fail")))
                    .isSameAs(subSet);
        }

        @Test
        @DisplayName("requireSubSet throws custom exception if not subset")
        void requireSubSetThrows() {
            final var superSet = Set.of(1);
            final var subSet = Set.of(1, 2);
            assertThatThrownBy(() -> Util.requireSubSet(superSet, subSet, (sup, sub) -> new Exception("Not a subset")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a subset");
        }
    }

    @Nested
    @DisplayName("isProperSubSet & requireProperSubSet")
    class ProperSubsetTests {

        @Test
        @DisplayName("isProperSubSet returns true only for strictly smaller subset")
        void isProperSubSetWorks() {
            assertThat(Util.isProperSubSet(Set.of(1, 2), Set.of(1))).isTrue();
            assertThat(Util.isProperSubSet(Set.of(1, 2), Set.of(1, 2))).isFalse();
        }

        @Test
        @DisplayName("requireProperSubSet returns same instance if condition holds")
        void requireProperSubSetReturnsSame() throws Exception {
            final var superSet = Set.of(1, 2);
            final var subSet = new HashSet<>(Set.of(1));
            assertThat(Util.requireProperSubSet(superSet, subSet, (sup, sub) -> new Exception("Fail")))
                    .isSameAs(subSet);
        }

        @Test
        @DisplayName("requireProperSubSet throws custom exception if not proper subset")
        void requireProperSubSetThrows() {
            final var superSet = Set.of(1, 2);
            final var subSet = Set.of(1, 2);
            assertThatThrownBy(() -> Util.requireProperSubSet(superSet, subSet, (sup, sub) -> new Exception("Not proper")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not proper");
        }
    }

    @Nested
    @DisplayName("Class Checks")
    class ClassChecks {

        @Test
        @DisplayName("requireInterfaceType returns same instance for interface")
        void requireInterfaceTypeWorks() {
            assertThat(Util.requireInterfaceType(Runnable.class, () -> new RuntimeException("Fail")))
                    .isSameAs(Runnable.class);
        }

        @Test
        @DisplayName("requireInterfaceType throws on non-interface")
        void requireInterfaceTypeThrows() {
            assertThatThrownBy(() -> Util.requireInterfaceType(String.class, () -> new RuntimeException("Not interface")))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Not interface");
        }

        @Test
        @DisplayName("requireConcreteType returns same instance for concrete class")
        void requireConcreteTypeWorks() {
            assertThat(Util.requireConcreteType(String.class, () -> new RuntimeException("Fail")))
                    .isSameAs(String.class);
        }

        @Test
        @DisplayName("requireConcreteType throws on abstract class or interface")
        void requireConcreteTypeThrows() {
            assertThatThrownBy(() -> Util.requireConcreteType(Runnable.class, () -> new RuntimeException("Not concrete")))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Not concrete");
        }
    }

    @Nested
    @DisplayName("File System Checks")
    class FileSystemChecks {

        @Test
        @DisplayName("requireDirectory (String) returns path for directory")
        void requireDirectoryStringWorks(@TempDir Path tempDir) throws Exception {
            String path = tempDir.toAbsolutePath().toString();
            assertThat(Util.requireDirectory(path, () -> new Exception("Fail")))
                    .isEqualTo(tempDir);
        }

        @Test
        @DisplayName("requireDirectory (String) throws on file")
        void requireDirectoryStringThrows(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            String path = file.toAbsolutePath().toString();
            assertThatThrownBy(() -> Util.requireDirectory(path, () -> new Exception("Not a directory")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a directory");
        }

        @Test
        @DisplayName("requireDirectory (File) returns same instance for directory")
        void requireDirectoryFileWorks(@TempDir Path tempDir) throws Exception {
            java.io.File dir = tempDir.toFile();
            assertThat(Util.requireDirectory(dir, () -> new Exception("Fail")))
                    .isSameAs(dir);
        }

        @Test
        @DisplayName("requireDirectory (File) throws on file")
        void requireDirectoryFileThrows(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            java.io.File f = file.toFile();
            assertThatThrownBy(() -> Util.requireDirectory(f, () -> new Exception("Not a directory")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a directory");
        }

        @Test
        @DisplayName("requireDirectory returns same instance for directory")
        void requireDirectoryWorks(@TempDir Path tempDir) throws Exception {
            assertThat(Util.requireDirectory(tempDir, () -> new Exception("Fail")))
                    .isSameAs(tempDir);
        }

        @Test
        @DisplayName("requireDirectory throws on file")
        void requireDirectoryThrows(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            assertThatThrownBy(() -> Util.requireDirectory(file, () -> new Exception("Not a directory")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a directory");
        }

        @Test
        @DisplayName("requireRegularFile (String) returns path for file")
        void requireRegularFileStringWorks(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            String path = file.toAbsolutePath().toString();
            assertThat(Util.requireRegularFile(path, () -> new Exception("Fail")))
                    .isEqualTo(file);
        }

        @Test
        @DisplayName("requireRegularFile (String) throws on directory")
        void requireRegularFileStringThrows(@TempDir Path tempDir) throws Exception {
            String path = tempDir.toAbsolutePath().toString();
            assertThatThrownBy(() -> Util.requireRegularFile(path, () -> new Exception("Not a file")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a file");
        }

        @Test
        @DisplayName("requireRegularFile (File) returns same instance for file")
        void requireRegularFileFileWorks(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            java.io.File f = file.toFile();
            assertThat(Util.requireRegularFile(f, () -> new Exception("Fail")))
                    .isSameAs(f);
        }

        @Test
        @DisplayName("requireRegularFile (File) throws on directory")
        void requireRegularFileFileThrows(@TempDir Path tempDir) throws Exception {
            java.io.File dir = tempDir.toFile();
            assertThatThrownBy(() -> Util.requireRegularFile(dir, () -> new Exception("Not a file")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a file");
        }

        @Test
        @DisplayName("requireRegularFile returns same instance for file")
        void requireRegularFileWorks(@TempDir Path tempDir) throws Exception {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);
            assertThat(Util.requireRegularFile(file, () -> new Exception("Fail")))
                    .isSameAs(file);
        }

        @Test
        @DisplayName("requireRegularFile throws on directory")
        void requireRegularFileThrows(@TempDir Path tempDir) throws Exception {
            assertThatThrownBy(() -> Util.requireRegularFile(tempDir, () -> new Exception("Not a file")))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Not a file");
        }

        @Test
        @DisplayName("requireReadable (String) returns path for readable path")
        void requireReadableStringWorks(@TempDir Path tempDir) throws Exception {
            String path = tempDir.toAbsolutePath().toString();
            assertThat(Util.requireReadable(path, () -> new Exception("Fail")))
                    .isEqualTo(tempDir);
        }

        @Test
        @DisplayName("requireReadable (File) returns same instance for readable file")
        void requireReadableFileWorks(@TempDir Path tempDir) throws Exception {
            java.io.File f = tempDir.toFile();
            assertThat(Util.requireReadable(f, () -> new Exception("Fail")))
                    .isSameAs(f);
        }

        @Test
        @DisplayName("requireReadable returns same instance for readable path")
        void requireReadableWorks(@TempDir Path tempDir) throws Exception {
            assertThat(Util.requireReadable(tempDir, () -> new Exception("Fail")))
                    .isSameAs(tempDir);
        }

        @Test
        @DisplayName("requireWriteable (String) returns path for writeable path")
        void requireWriteableStringWorks(@TempDir Path tempDir) throws Exception {
            String path = tempDir.toAbsolutePath().toString();
            assertThat(Util.requireWriteable(path, () -> new Exception("Fail")))
                    .isEqualTo(tempDir);
        }

        @Test
        @DisplayName("requireWriteable (File) returns same instance for writeable file")
        void requireWriteableFileWorks(@TempDir Path tempDir) throws Exception {
            java.io.File f = tempDir.toFile();
            assertThat(Util.requireWriteable(f, () -> new Exception("Fail")))
                    .isSameAs(f);
        }

        @Test
        @DisplayName("requireWriteable returns same instance for writeable path")
        void requireWriteableWorks(@TempDir Path tempDir) throws Exception {
            assertThat(Util.requireWriteable(tempDir, () -> new Exception("Fail")))
                    .isSameAs(tempDir);
        }
    }

    @Nested
    @DisplayName("toSafeFileName")
    class StringSanitization {

        @Test
        @DisplayName("toSafeFileName replaces spaces and removes special chars")
        void toSafeFileNameSanitizes() {
            assertThat(Util.toSafeFileName("My Plugin Name!")).isEqualTo("My_Plugin_Name");
            assertThat(Util.toSafeFileName("v1.0.0-beta+build")).isEqualTo("v1.0.0-betabuild"); // + removed
            assertThat(Util.toSafeFileName("User/Name\\Here")).isEqualTo("UserNameHere");
            assertThat(Util.toSafeFileName("..")).isEqualTo("..");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("toSafeFileName throws NPE on null")
        void toSafeFileNameThrowsOnNull() {
            assertThatThrownBy(() -> Util.toSafeFileName(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("name");
        }
    }

}
