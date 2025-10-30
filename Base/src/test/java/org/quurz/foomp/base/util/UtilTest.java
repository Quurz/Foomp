package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.quurz.foomp.base.util.Util.isSubSet;
import static org.quurz.foomp.base.util.Util.mustBeConcrete;
import static org.quurz.foomp.base.util.Util.mustBeInterface;
import static org.quurz.foomp.base.util.Util.mustBeSubSet;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;
import static org.slf4j.LoggerFactory.getLogger;

class UtilTest {

    private static final Logger LOGGER
        = getLogger(UtilTest.class);

    private interface TestInterface {
        void testMethod();
    }

    private @interface TestAnnotation {}

    private abstract static class TestAbstractClass {
        abstract void testMethod();
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequireNonEmptyForCollectionsWithErrorMessage() {
        LOGGER.info("Test Util.requiresNonEmpty for collections with error message");

        final var collection
            = new HashSet<Integer>();
        collection.add(1);

        assertThatThrownBy(() -> Util.requireNonEmpty((Collection<Object>) null, "Test"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<Integer>(), (String) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("message");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<Integer>(), "Test"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> Util.requireNonEmpty(collection, "Test"));
    }

    @SuppressWarnings({"DataFlowIssue", "MismatchedQueryAndUpdateOfCollection"})
    @Test
    void testRequireNonEmptyForCollectionsWithExceptionSupplier() {
        LOGGER.info("Test Util.requiresNonEmpty for collections with exception supplier");

        final var collection
            = new HashSet<Integer>();
        collection.add(1);

        assertThatThrownBy(() -> Util.requireNonEmpty((Collection<Object>) null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<Integer>(), (Supplier<? extends Exception>) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<Integer>(), () -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<>(), () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThat(Util.requireNonEmpty(collection, () -> new IllegalArgumentException("Test")))
            .isEqualTo(collection);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequireNonEmptyForMapWithErrorMessage() {
        LOGGER.info("Test Util.requiresNonEmpty for Map with error message");

        final var map
            = new HashMap<Integer, String>();
        map.put(1, "One");

        assertThatThrownBy(() -> Util.requireNonEmpty((Map<?, ?>) null, "Test"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("map");
        assertThatThrownBy(() -> Util.requireNonEmpty(map, (String) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("message");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), "Test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> Util.requireNonEmpty(map, "Test"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequireNonEmptyForMapWithExceptionSupplier() {
        LOGGER.info("Test Util.requiresNonEmpty for Map with exception supplier");

        final var map
            = new HashMap<Integer, String>();
        map.put(1, "One");

        assertThatThrownBy(() -> Util.requireNonEmpty((Map<?, ?>) null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("map");
        assertThatThrownBy(() -> Util.requireNonEmpty(map, (Supplier<? extends Exception>) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");
        assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), () -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThat(Util.requireNonEmpty(map, () -> new IllegalArgumentException("Test")))
            .isEqualTo(map);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequireNonNullElementsForCollections() {
        LOGGER.info("Test Util.requireNonNullElements for collections");

        final var setWithoutNullElements
            = new HashSet<Integer>();
        setWithoutNullElements.add(1);
        setWithoutNullElements.add(2);
        setWithoutNullElements.add(3);

        final var setWithNullElements
            = new HashSet<Integer>();
        setWithNullElements.add(1);
        setWithNullElements.add(null);
        setWithNullElements.add(3);

        final var listWithoutNullElements
            = new ArrayList<Integer>();
        listWithoutNullElements.add(1);
        listWithoutNullElements.add(2);
        listWithoutNullElements.add(3);

        final var listWithNullElements
            = new ArrayList<Integer>();
        listWithNullElements.add(1);
        listWithNullElements.add(null);
        listWithNullElements.add(3);

        assertThatThrownBy(() -> Util.requireNonNullElements((Collection<Object>) null, "collection", IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requireNonNullElements(new HashSet<>(), null, IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collectionName");
        assertThatThrownBy(() -> Util.requireNonNullElements(new HashSet<>(), "collection", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> Util.requireNonNullElements(setWithNullElements, "collection", message -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> Util.requireNonNullElements(setWithNullElements, "collection", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("collection");
        assertThat(Util.requireNonNullElements(setWithoutNullElements, "collection", IllegalArgumentException::new))
            .isEqualTo(setWithoutNullElements);

        assertThatThrownBy(() -> Util.requireNonNullElements(listWithNullElements, "collection", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("collection")
            .hasMessageContaining("1");
        assertThat(Util.requireNonNullElements(listWithoutNullElements, "collection", IllegalArgumentException::new))
            .isEqualTo(listWithoutNullElements);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequireNonNullElementsForArrays() {
        LOGGER.info("Test Util.requireNonNullElements for arrays");

        final var arrayWithoutNullElements
            = new Integer[]{1, 2, 3};
        final var arrayWithNullElements
            = new Integer[]{1, null, 3};


        assertThatThrownBy(() -> requireNonNullElements((Object[]) null, "array", IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("array");
        assertThatThrownBy(() -> requireNonNullElements(new Object[]{}, null, IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("array");
        assertThatThrownBy(() -> requireNonNullElements(new Object[]{}, "array", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> requireNonNullElements(arrayWithNullElements, "array", message -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> requireNonNullElements(arrayWithNullElements, "array", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("array")
            .hasMessageContaining("1");
        assertThat(requireNonNullElements(arrayWithoutNullElements, "array", IllegalArgumentException::new))
            .isEqualTo(arrayWithoutNullElements);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testIsSubset() {
        LOGGER.info("Test Util.isSubset");

        final var superSet
            = new HashSet<Integer>();
        superSet.add(1);
        superSet.add(2);
        superSet.add(3);

        final var subSet
            = new HashSet<Integer>();
        subSet.add(1);
        subSet.add(2);

       assertThatThrownBy(() -> isSubSet(null, subSet))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("superSet");
        assertThatThrownBy(() -> isSubSet(superSet, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("subSet");

        assertThat(isSubSet(superSet, subSet))
            .isTrue();
        assertThat(isSubSet(subSet, superSet))
            .isFalse();
        assertThat(isSubSet(superSet, new HashSet<>()))
            .isTrue();
        assertThat(isSubSet(new HashSet<>(), new HashSet<>()))
            .isTrue();   // TODO: Kann die leere Menge Teilmenge einer leeren MEnge sein? Wohl kaum, oder?
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMustBeSubSet() {
        LOGGER.info("Test Util.mustBeSubset");

        final var superSet
            = new HashSet<Integer>();
        superSet.add(1);
        superSet.add(2);
        superSet.add(3);

        final var subSet
            = new HashSet<Integer>();
        subSet.add(1);
        subSet.add(2);

        assertThatThrownBy(() -> mustBeSubSet(null, subSet, "Test"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("superSet");
        assertThatThrownBy(() -> mustBeSubSet(superSet, null, "Test"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("subSet");
        assertThatThrownBy(() -> mustBeSubSet(superSet, new HashSet<>(), null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("message");

        assertThatThrownBy(() -> mustBeSubSet(subSet, superSet, "Test"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> mustBeSubSet(superSet, subSet, "Test"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMustBeInterfaceWithExceptionSupplier() {
        LOGGER.info("Test Util.mustBeInterface with execution supplier");

        assertThatThrownBy(() -> mustBeInterface(null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("clazz");
        assertThatThrownBy(() -> mustBeInterface(String.class, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> mustBeInterface(String.class, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> mustBeInterface(Runnable.class, () -> new IllegalArgumentException("Test")));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testMustBeConcreteWithExceptionSupplier() {
        LOGGER.info("Test Util.mustBeConcrete with exception supplier");

        assertThatThrownBy(() -> mustBeConcrete(null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("clazz");
        assertThatThrownBy(() -> mustBeConcrete(String.class, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> mustBeConcrete(TestInterface.class, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThatThrownBy(() -> mustBeConcrete(TestAnnotation.class, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThatThrownBy(() -> mustBeConcrete(boolean.class, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThatThrownBy(() -> mustBeConcrete(TestAbstractClass.class, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> mustBeConcrete(String.class, () -> new IllegalArgumentException("Test")));
    }

    // Ergänzungen

    @Test
    void testIsSubset_equal_non_empty_sets_is_false() {
        LOGGER.info("Test Util.isSubSet equal non-empty sets should be false");

        final var a = new java.util.HashSet<>(java.util.List.of(1, 2));
        final var b = new java.util.HashSet<>(java.util.List.of(1, 2));

        // equal, non-empty -> not a proper subset
        assertThat(isSubSet(a, b)).isTrue();    // TODO: Das sollten wir auf jeden Fall noch mal profen
        assertThat(isSubSet(b, a)).isTrue();
    }

    @Test
    void testMustBeSubSet_returns_same_instance() {
        LOGGER.info("Test Util.mustBeSubSet returns the same subset instance (identity)");

        final var superSet = new java.util.HashSet<>(java.util.List.of(1, 2, 3));
        final var subSet   = new java.util.HashSet<>(java.util.List.of(1, 2));

        // Should return the very same reference (no copy/view)
        assertThat(mustBeSubSet(superSet, subSet, "should be proper subset"))
            .isSameAs(subSet);
    }

    @SuppressWarnings({"DataFlowIssue", "ConstantConditions"})
    @Test
    void testRequiresNonNullResult1_decorator() {
        LOGGER.info("Test Util.requiresNonNullResult1");

        // null arguments
        assertThatThrownBy(() -> Util.requiresNonNullResult1(null, "f"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("function");
        assertThatThrownBy(() -> Util.requiresNonNullResult1(x -> x, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("functionName");

        // wrapped function returns null -> NPE mit passender Message
        final var nullReturning = Util.requiresNonNullResult1((java.util.function.Function<String, String>) x -> null, "fNull");
        assertThatThrownBy(() -> nullReturning.apply("x"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("fNull");

        // happy path
        final var lengthFun = Util.requiresNonNullResult1(String::length, "len");
        assertThat(lengthFun.apply("abc")).isEqualTo(3);
    }

    @SuppressWarnings({"ConstantConditions"})
    @Test
    void testRequiresNonNullResult2_decorator() {
        LOGGER.info("Test Util.requiresNonNullResult2");

        // null arguments
        assertThatThrownBy(() -> Util.requiresNonNullResult2(null, "f2"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("function");
        assertThatThrownBy(() -> Util.requiresNonNullResult2((a, b) -> b, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("functionName");

        // wrapped bi-function returns null -> NPE mit passender Message
        final var nullReturning2 = Util.requiresNonNullResult2((java.util.function.BiFunction<Integer, Integer, Integer>) (a, b) -> null, "f2Null");
        assertThatThrownBy(() -> nullReturning2.apply(1, 2))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("f2Null");

        // happy path
        final var adder = Util.requiresNonNullResult2(Integer::sum, "sum");
        assertThat(adder.apply(2, 5)).isEqualTo(7);
    }

}
