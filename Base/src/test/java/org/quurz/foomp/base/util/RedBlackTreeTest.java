package org.quurz.foomp.base.util;

import org.junit.jupiter.api.*;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.RedBlackTree.*;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("RedBlackTree")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RedBlackTreeTest extends TestHelper {

    private static final Logger LOGGER = getLogger(RedBlackTreeTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void redBlackTree_creates_empty_tree() {
            LOGGER.info("RedBlackTree.redBlackTree() should create an empty tree");
            final RedBlackTree<Integer> tree = redBlackTree();
            assertThat(tree.contains(1)).isFalse();
        }

        @Test
        void redBlackTreeOf_creates_tree_with_elements() {
            LOGGER.info("RedBlackTree.redBlackTreeOf(elements) should create tree with given elements");
            final RedBlackTree<Integer> tree = redBlackTreeOf(3, 1, 2);
            assertThat(tree.contains(1)).isTrue();
            assertThat(tree.contains(2)).isTrue();
            assertThat(tree.contains(3)).isTrue();
            assertThat(tree.contains(4)).isFalse();
        }

        @Test
        void redBlackTreeFrom_creates_tree_from_collection() {
            LOGGER.info("RedBlackTree.redBlackTreeFrom(collection) should create tree from collection");
            final List<Integer> values = List.of(5, 3, 8, 1, 4);
            final RedBlackTree<Integer> tree = redBlackTreeFrom(values);
            values.forEach(v -> assertThat(tree.contains(v)).isTrue());
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        void factory_methods_throw_NPE_on_null_parameters() {
            LOGGER.info("RedBlackTree factories should throw NullPointerException on null parameters");
            assertThatThrownBy(() -> redBlackTree(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> redBlackTreeOf((Integer[]) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> redBlackTreeOf(1, null, 3))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> redBlackTreeFrom(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> {
                List<Integer> listWithNull = new java.util.ArrayList<>();
                listWithNull.add(1);
                listWithNull.add(null);
                redBlackTreeFrom(listWithNull);
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Core Operations")
    class CoreOperations {

        @Test
        @SuppressWarnings("DataFlowIssue")
        void core_methods_throw_NPE_on_null_parameters() {
            LOGGER.info("RedBlackTree methods should throw NullPointerException on null parameters");
            final RedBlackTree<Integer> tree = redBlackTree();
            assertThatThrownBy(() -> tree.insert(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tree.contains(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tree.search(null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> tree.searchSafe(null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void insert_adds_elements_and_maintains_immutability() {
            LOGGER.info("insert() should return a new tree containing the element");
            RedBlackTree<Integer> tree1 = redBlackTree();
            RedBlackTree<Integer> tree2 = tree1.insert(10);
            RedBlackTree<Integer> tree3 = tree2.insert(5);

            assertThat(tree1.contains(10)).isFalse();
            assertThat(tree2.contains(10)).isTrue();
            assertThat(tree2.contains(5)).isFalse();
            assertThat(tree3.contains(10)).isTrue();
            assertThat(tree3.contains(5)).isTrue();
        }

        @Test
        void search_returns_element_or_throws() {
            LOGGER.info("search() should return the element if found, or throw NoSuchElementException");
            RedBlackTree<Integer> tree = redBlackTreeOf(10, 20, 30);
            assertThat(tree.search(20)).isEqualTo(20);
            assertThatThrownBy(() -> tree.search(40)).isInstanceOf(java.util.NoSuchElementException.class);
        }

        @Test
        void searchSafe_returns_Maybe() {
            LOGGER.info("searchSafe() should return Maybe containing the element or None");
            RedBlackTree<Integer> tree = redBlackTreeOf(10, 20, 30);
            checkIsSomeWithValue(tree.searchSafe(20), 20);
            checkIsNone(tree.searchSafe(40));
        }
    }

    @Nested
    @DisplayName("Balancing Properties")
    class BalancingProperties {

        @Test
        void tree_remains_balanced_after_sequential_inserts() {
            LOGGER.info("Tree should handle sequential inserts efficiently");
            RedBlackTree<Integer> tree = redBlackTree();
            int n = 1000;
            for (int i = 1; i <= n; i++) {
                tree = tree.insert(i);
            }
            
            for (int i = 1; i <= n; i++) {
                assertThat(tree.contains(i)).isTrue();
            }
        }

        @Test
        void random_inserts() {
            LOGGER.info("Tree should handle random operations correctly");
            List<Integer> values = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) values.add(i);
            
            List<Integer> shuffled = shuffle(values);
            RedBlackTree<Integer> tree = redBlackTree();
            
            for (Integer v : shuffled) {
                tree = tree.insert(v);
            }
            
            for (Integer v : values) {
                assertThat(tree.contains(v)).isTrue();
            }
        }
    }
}
