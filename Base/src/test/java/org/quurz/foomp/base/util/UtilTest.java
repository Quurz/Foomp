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
import static org.quurz.foomp.base.util.Util.requiresNonNullElements;
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
    void testRequiresNonEmptyForCollectionsWithErrorMessage() {
        LOGGER.info("Test Util.requiresNonEmpty for collections with error message");

        final var collection
            = new HashSet<Integer>();
        collection.add(1);

        assertThatThrownBy(() -> Util.requiresNonEmpty((Collection<Object>) null, "Test"))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashSet<Integer>(), (String) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("message");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashSet<Integer>(), "Test"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> Util.requiresNonEmpty(collection, "Test"));
    }

    @SuppressWarnings({"DataFlowIssue", "MismatchedQueryAndUpdateOfCollection"})
    @Test
    void testRequiresNonEmptyForCollectionsWithExceptionSupplier() {
        LOGGER.info("Test Util.requiresNonEmpty for collections with exception supplier");

        final var collection
            = new HashSet<Integer>();
        collection.add(1);

        assertThatThrownBy(() -> Util.requiresNonEmpty((Collection<Object>) null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashSet<Integer>(), (Supplier<? extends Exception>) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashSet<Integer>(), () -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashSet<>(), () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThat(Util.requiresNonEmpty(collection, () -> new IllegalArgumentException("Test")))
            .isEqualTo(collection);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequiresNonEmptyForMapWithErrorMessage() {
        LOGGER.info("Test Util.requiresNonEmpty for Map with error message");

        final var map
            = new HashMap<Integer, String>();
        map.put(1, "One");

        assertThatThrownBy(() -> Util.requiresNonEmpty((Map<?, ?>) null, "Test"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("map");
        assertThatThrownBy(() -> Util.requiresNonEmpty(map, (String) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("message");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashMap<>(), "Test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Test");

        assertThatNoException()
            .isThrownBy(() -> Util.requiresNonEmpty(map, "Test"));
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequiresNonEmptyForMapWithExceptionSupplier() {
        LOGGER.info("Test Util.requiresNonEmpty for Map with exception supplier");

        final var map
            = new HashMap<Integer, String>();
        map.put(1, "One");

        assertThatThrownBy(() -> Util.requiresNonEmpty((Map<?, ?>) null, () -> new IllegalArgumentException("Test")))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("map");
        assertThatThrownBy(() -> Util.requiresNonEmpty(map, (Supplier<? extends Exception>) null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");
        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashMap<>(), () -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionSupplier");

        assertThatThrownBy(() -> Util.requiresNonEmpty(new HashMap<>(), () -> new IllegalArgumentException("Test")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Test");
        assertThat(Util.requiresNonEmpty(map, () -> new IllegalArgumentException("Test")))
            .isEqualTo(map);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequiresNonNullElementsForCollections() {
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

        assertThatThrownBy(() -> Util.requiresNonNullElements((Collection<Object>) null, "collection", IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collection");
        assertThatThrownBy(() -> Util.requiresNonNullElements(new HashSet<>(), null, IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("collectionName");
        assertThatThrownBy(() -> Util.requiresNonNullElements(new HashSet<>(), "collection", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> Util.requiresNonNullElements(setWithNullElements, "collection", message -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> Util.requiresNonNullElements(setWithNullElements, "collection", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("collection");
        assertThat(Util.requiresNonNullElements(setWithoutNullElements, "collection", IllegalArgumentException::new))
            .isEqualTo(setWithoutNullElements);

        assertThatThrownBy(() -> Util.requiresNonNullElements(listWithNullElements, "collection", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("collection")
            .hasMessageContaining("1");
        assertThat(Util.requiresNonNullElements(listWithoutNullElements, "collection", IllegalArgumentException::new))
            .isEqualTo(listWithoutNullElements);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testRequiresNonNullElementsForArrays() {
        LOGGER.info("Test Util.requireNonNullElements for arrays");

        final var arrayWithoutNullElements
            = new Integer[]{1, 2, 3};
        final var arrayWithNullElements
            = new Integer[]{1, null, 3};


        assertThatThrownBy(() -> requiresNonNullElements((Object[]) null, "array", IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("array");
        assertThatThrownBy(() -> requiresNonNullElements(new Object[]{}, null, IllegalArgumentException::new))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("array");
        assertThatThrownBy(() -> requiresNonNullElements(new Object[]{}, "array", null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> requiresNonNullElements(arrayWithNullElements, "array", message -> null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("exceptionConstructor");

        assertThatThrownBy(() -> requiresNonNullElements(arrayWithNullElements, "array", IllegalArgumentException::new))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("array")
            .hasMessageContaining("1");
        assertThat(requiresNonNullElements(arrayWithoutNullElements, "array", IllegalArgumentException::new))
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
            .isFalse();
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

}
