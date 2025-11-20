package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Util")
class UtilTest {

    @Nested
    @DisplayName("requireNonEmpty(Collection)")
    class RequireNonEmptyCollection {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NPE if collection is null")
        void throwsNpeIfCollectionIsNull() {
            assertThatThrownBy(() -> Util.requireNonEmpty((Collection<Object>) null, "Test"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("collection");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NPE if message is null")
        void throwsNpeIfMessageIsNull() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<>(), (String) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("message");
        }

        @Test
        @DisplayName("throws IllegalArgumentException if collection is empty")
        void throwsIaeIfCollectionIsEmpty() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<>(), "Test"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test");
        }

        @Test
        @DisplayName("returns collection if not empty")
        void returnsCollectionIfNotEmpty() {
            final var collection = new HashSet<Integer>();
            collection.add(1);

            assertThat(Util.requireNonEmpty(collection, "Test"))
                    .isSameAs(collection);
        }
    }

    @Nested
    @DisplayName("requireNonEmpty(Collection, Supplier)")
    class RequireNonEmptyCollectionSupplier {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NPE if exception supplier is null")
        void throwsNpeIfSupplierIsNull() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<>(), (Supplier<Exception>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("exceptionSupplier");
        }

        @Test
        @DisplayName("throws custom exception if collection is empty")
        void throwsCustomException() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashSet<>(), () -> new IllegalStateException("Custom")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Custom");
        }

        @Test
        @DisplayName("returns collection if not empty")
        void returnsCollectionIfNotEmpty() {
            final var collection = new HashSet<Integer>();
            collection.add(1);

            assertThat(Util.requireNonEmpty(collection, () -> new IllegalStateException("Custom")))
                    .isSameAs(collection);
        }
    }

    @Nested
    @DisplayName("requireNonEmpty(Map)")
    class RequireNonEmptyMap {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("throws NPE if map is null")
        void throwsNpeIfMapIsNull() {
            assertThatThrownBy(() -> Util.requireNonEmpty((Map<?, ?>) null, "Test"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("map");
        }

        @Test
        @DisplayName("throws IllegalArgumentException if map is empty")
        void throwsIaeIfMapIsEmpty() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), "Test"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test");
        }

        @Test
        @DisplayName("returns map if not empty")
        void returnsMapIfNotEmpty() {
            final var map = new HashMap<Integer, String>();
            map.put(1, "One");

            assertThat(Util.requireNonEmpty(map, "Test"))
                    .isSameAs(map);
        }
    }

    @Nested
    @DisplayName("requireNonEmpty(Map, Supplier)")
    class RequireNonEmptyMapSupplier {

        @Test
        @DisplayName("throws custom exception if map is empty")
        void throwsCustomException() {
            assertThatThrownBy(() -> Util.requireNonEmpty(new HashMap<>(), () -> new IllegalStateException("Custom")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Custom");
        }
    }

    @Nested
    @DisplayName("requireNonNullElements(Collection)")
    class RequireNonNullElementsCollection {

        @Test
        @DisplayName("throws exception for null element in Set")
        void throwsExceptionForSet() {
            final var set = new HashSet<Integer>();
            set.add(1);
            set.add(null);

            assertThatThrownBy(() -> Util.requireNonNullElements(set, "testSet", IllegalArgumentException::new))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("testSet");
        }

        @Test
        @DisplayName("throws exception for null element in List with index")
        void throwsExceptionForList() {
            final var list = new ArrayList<Integer>();
            list.add(1);
            list.add(null);

            assertThatThrownBy(() -> Util.requireNonNullElements(list, "testList", IllegalArgumentException::new))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("testList")
                    .hasMessageContaining("1");
        }

        @Test
        @DisplayName("returns collection if all elements non-null")
        void returnsCleanCollection() {
            final var list = new ArrayList<>();
            list.add(1);
            assertThat(Util.requireNonNullElements(list, "list", IllegalArgumentException::new))
                    .isSameAs(list);
        }
    }

    @Nested
    @DisplayName("requireNonNullElements(Array)")
    class RequireNonNullElementsArray {

        @Test
        @DisplayName("throws exception for null element in array with index")
        void throwsExceptionForArray() {
            final var array = new Integer[]{1, null};

            assertThatThrownBy(() -> Util.requireNonNullElements(array, "testArray", IllegalArgumentException::new))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("testArray")
                    .hasMessageContaining("1");
        }

        @Test
        @DisplayName("returns array if all elements non-null")
        void returnsCleanArray() {
            final var array = new Integer[]{1, 2};
            assertThat(Util.requireNonNullElements(array, "array", IllegalArgumentException::new))
                    .isSameAs(array);
        }
    }

    @Nested
    @DisplayName("Set Subset Checks")
    class SetSubsetChecks {

        @Test
        @DisplayName("isSubSet returns true for equal sets")
        void isSubSetForEqualSets() {
            final var a = Set.of(1, 2);
            final var b = Set.of(1, 2);
            assertThat(Util.isSubSet(a, b)).isTrue();
        }

        @Test
        @DisplayName("isSubSet returns true for proper subset")
        void isSubSetForProperSubset() {
            final var superSet = Set.of(1, 2, 3);
            final var subSet = Set.of(1, 2);
            assertThat(Util.isSubSet(superSet, subSet)).isTrue();
        }

        @Test
        @DisplayName("isSubSet returns false if candidate is larger")
        void isSubSetForLargerCandidate() {
            final var superSet = Set.of(1);
            final var subSet = Set.of(1, 2);
            assertThat(Util.isSubSet(superSet, subSet)).isFalse();
        }

        @Test
        @DisplayName("isSubSet returns true for empty sets")
        void isSubSetForEmptySets() {
            assertThat(Util.isSubSet(Set.of(), Set.of())).isTrue();
            assertThat(Util.isSubSet(Set.of(1), Set.of())).isTrue();
        }

        @Test
        @DisplayName("mustBeSubSet throws if not subset")
        void mustBeSubSetThrows() {
            final var superSet = Set.of(1);
            final var subSet = Set.of(1, 2);

            assertThatThrownBy(() -> Util.mustBeSubSet(superSet, subSet, "Error"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Error");
        }
    }

    @Nested
    @DisplayName("Class Type Checks")
    class ClassTypeChecks {

        private interface TestInterface {}
        private abstract static class TestAbstractClass {}

        @Test
        @DisplayName("mustBeInterface throws for concrete class")
        void mustBeInterfaceThrows() {
            assertThatThrownBy(() -> Util.mustBeInterface(String.class, () -> new IllegalArgumentException("Bad")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("mustBeInterface passes for interface")
        void mustBeInterfacePasses() {
            assertThatNoException()
                    .isThrownBy(() -> Util.mustBeInterface(TestInterface.class, () -> new IllegalArgumentException("Bad")));
        }

        @Test
        @DisplayName("mustBeConcrete throws for abstract class")
        void mustBeConcreteThrowsForAbstract() {
            assertThatThrownBy(() -> Util.mustBeConcrete(TestAbstractClass.class, () -> new IllegalArgumentException("Bad")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("mustBeConcrete passes for String")
        void mustBeConcretePasses() {
            assertThatNoException()
                    .isThrownBy(() -> Util.mustBeConcrete(String.class, () -> new IllegalArgumentException("Bad")));
        }
    }

    @Nested
    @DisplayName("Functional Wrappers")
    class FunctionalWrappers {

        @SuppressWarnings("unused")
        @Test
        @DisplayName("requiresNonNullResult1 throws on null result")
        void functionThrowsOnNull() {
            final var fun = Util.requiresNonNullResult1((Function<String, String>) s -> null, "fun");
            assertThatThrownBy(() -> fun.apply("in"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("fun");
        }

        @SuppressWarnings("unused")
        @Test
        @DisplayName("requiresNonNullResult2 throws on null result")
        void biFunctionThrowsOnNull() {
            final var fun = Util.requiresNonNullResult2((BiFunction<String, String, String>) (a, b) -> null, "fun");
            assertThatThrownBy(() -> fun.apply("a", "b"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("fun");
        }
    }

    @Nested
    @DisplayName("File System Checks")
    class FileSystemChecks {

        @Test
        @DisplayName("mustBeDirectory throws for file")
        void mustBeDirectoryThrowsForFile(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("file.txt");
            Files.createFile(file);

            assertThatThrownBy(() -> Util.mustBeDirectory(file, () -> new IOException("Not a dir")))
                    .isInstanceOf(IOException.class)
                    .hasMessage("Not a dir");
        }

        @Test
        @DisplayName("mustBeDirectory passes for directory")
        void mustBeDirectoryPasses(@TempDir Path tempDir) {
            assertThatNoException()
                    .isThrownBy(() -> Util.mustBeDirectory(tempDir, () -> new IOException("Fail")));
        }

        @Test
        @DisplayName("mustBeRegularFile throws for directory")
        void mustBeRegularFileThrowsForDirectory(@TempDir Path tempDir) {
            assertThatThrownBy(() -> Util.mustBeRegularFile(tempDir, () -> new IOException("Not a file")))
                    .isInstanceOf(IOException.class)
                    .hasMessage("Not a file");
        }

        @Test
        @DisplayName("mustBeRegularFile passes for file")
        void mustBeRegularFilePasses(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("test.txt");
            Files.createFile(file);

            assertThatNoException()
                    .isThrownBy(() -> Util.mustBeRegularFile(file, () -> new IOException("Fail")));
        }

        @Test
        @DisplayName("mustBeReadable passes for readable file")
        void mustBeReadablePasses(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("read.txt");
            Files.createFile(file);
            // By default created files are readable
            assertThat(Util.mustBeReadable(file, () -> new IOException("Fail"))).isEqualTo(file);
        }
        
        @Test
        @DisplayName("mustBeWriteable passes for writeable file")
        void mustBeWriteablePasses(@TempDir Path tempDir) throws IOException {
            Path file = tempDir.resolve("write.txt");
            Files.createFile(file);
            // By default created files are writeable
            assertThat(Util.mustBeWriteable(file, () -> new IOException("Fail"))).isEqualTo(file);
        }
    }

    @Nested
    @DisplayName("String Sanitization")
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
