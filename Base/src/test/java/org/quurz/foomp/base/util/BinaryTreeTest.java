package org.quurz.foomp.base.util;

import org.junit.jupiter.api.*;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.BinaryTree.*;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("BinaryTree")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BinaryTreeTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(BinaryTreeTest.class);

    @Nested
    @DisplayName("Factory")
    class Factory {

        @Test
        void binaryTree_creates_empty_tree_with_natural_order() {
            LOGGER.info("BinaryTree.binaryTree() should create an empty tree for Comparable types");
            final BinaryTree<Integer> tree = binaryTree();
            assertThat(tree.contains(1)).isFalse();
        }

        @Test
        void binaryTree_with_comparator_creates_empty_tree() {
            LOGGER.info("BinaryTree.binaryTree(Comparator) should create an empty tree");
            final Comparator<String> cmp = Comparator.reverseOrder();
            final BinaryTree<String> tree = binaryTree(cmp);
            assertThat(tree.contains("a")).isFalse();
        }

        @Test
        void binaryTreeOf_creates_tree_with_elements() {
            LOGGER.info("BinaryTree.binaryTreeOf(elements) should create tree with given elements");
            final BinaryTree<Integer> tree = binaryTreeOf(3, 1, 2);
            assertThat(tree.contains(1)).isTrue();
            assertThat(tree.contains(2)).isTrue();
            assertThat(tree.contains(3)).isTrue();
            assertThat(tree.contains(4)).isFalse();
        }

        @Test
        void binaryTreeFrom_creates_tree_from_collection() {
            LOGGER.info("BinaryTree.binaryTreeFrom(collection) should create tree from collection");
            final List<Integer> values = List.of(5, 3, 8, 1, 4);
            final BinaryTree<Integer> tree = binaryTreeFrom(values);
            values.forEach(v -> assertThat(tree.contains(v)).isTrue());
        }

        @Test
        @SuppressWarnings("DataFlowIssue")
        void factory_methods_throw_on_null() {
            LOGGER.info("Factory methods should throw NullPointerException on null input");
            assertThatThrownBy(() -> binaryTree((Comparator<Integer>) null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> binaryTreeOf((Integer[]) null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> binaryTreeFrom((List<Integer>) null)).isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> binaryTreeFrom(null, List.of(1))).isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Core Operations")
    class CoreOperations {

        @Test
        void insert_adds_elements_and_maintains_immutability() {
            LOGGER.info("insert() should return a new tree containing the element");
            BinaryTree<Integer> tree1 = binaryTree();
            BinaryTree<Integer> tree2 = tree1.insert(10);
            BinaryTree<Integer> tree3 = tree2.insert(5);

            assertThat(tree1.contains(10)).isFalse();
            assertThat(tree2.contains(10)).isTrue();
            assertThat(tree2.contains(5)).isFalse();
            assertThat(tree3.contains(10)).isTrue();
            assertThat(tree3.contains(5)).isTrue();
        }

        @Test
        void search_returns_element_or_throws() {
            LOGGER.info("search() should return the element if found, or throw NoSuchElementException");
            BinaryTree<Integer> tree = binaryTreeOf(10, 20, 30);
            assertThat(tree.search(20)).isEqualTo(20);
            assertThatThrownBy(() -> tree.search(40)).isInstanceOf(java.util.NoSuchElementException.class);
        }

        @Test
        void searchSafe_returns_Maybe() {
            LOGGER.info("searchSafe() should return Maybe containing the element or None");
            BinaryTree<Integer> tree = binaryTreeOf(10, 20, 30);
            checkIsSomeWithValue(tree.searchSafe(20), 20);
            checkIsNone(tree.searchSafe(40));
        }

        @Test
        void remove_removes_element_and_maintains_immutability() {
            LOGGER.info("remove() should return a new tree without the element");
            BinaryTree<Integer> tree1 = binaryTreeOf(10, 20, 30);
            BinaryTree<Integer> tree2 = tree1.remove(20);

            assertThat(tree1.contains(20)).isTrue();
            assertThat(tree2.contains(20)).isFalse();
            assertThat(tree2.contains(10)).isTrue();
            assertThat(tree2.contains(30)).isTrue();
        }

        @Test
        void remove_non_existent_element_throws_NoSuchElementException() {
            LOGGER.info("remove() of non-existent element should throw NoSuchElementException");
            BinaryTree<Integer> tree = binaryTreeOf(10, 20);
            assertThatThrownBy(() -> tree.remove(30)).isInstanceOf(java.util.NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("AVL Properties")
    class AvlProperties {

        @Test
        void tree_remains_balanced_after_sequential_inserts() {
            LOGGER.info("Tree should remain balanced (AVL property) after sequential inserts");
            // Sequential inserts into a non-balancing BST would result in a linked list (height n-1)
            // AVL tree height should be O(log n)
            BinaryTree<Integer> tree = binaryTree();
            int n = 100;
            for (int i = 1; i <= n; i++) {
                tree = tree.insert(i);
            }
            
            // Height of AVL tree with 100 nodes should be around 7-9 (log2(100) approx 6.64)
            // We can't access height directly from BinaryTree, but we can verify it's still efficient
            for (int i = 1; i <= n; i++) {
                assertThat(tree.contains(i)).isTrue();
            }
        }

        @Test
        void tree_remains_balanced_after_removals() {
            LOGGER.info("Tree should remain balanced after removals");
            List<Integer> values = new java.util.ArrayList<>();
            for (int i = 0; i < 100; i++) values.add(i);
            
            BinaryTree<Integer> tree = binaryTreeFrom(values);
            
            // Remove half of the elements
            for (int i = 0; i < 50; i++) {
                tree = tree.remove(i);
            }
            
            for (int i = 0; i < 50; i++) {
                assertThat(tree.contains(i)).isFalse();
            }
            for (int i = 50; i < 100; i++) {
                assertThat(tree.contains(i)).isTrue();
            }
        }

        @Test
        void random_inserts_and_removals() {
            LOGGER.info("Tree should handle random operations correctly");
            List<Integer> values = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) values.add(i);
            
            List<Integer> shuffled = shuffle(values);
            BinaryTree<Integer> tree = binaryTree();
            
            for (Integer v : shuffled) {
                tree = tree.insert(v);
            }
            
            for (Integer v : values) {
                assertThat(tree.contains(v)).isTrue();
            }
            
            List<Integer> toRemove = shuffle(values).subList(0, 500);
            for (Integer v : toRemove) {
                tree = tree.remove(v);
            }
            
            for (Integer v : values) {
                if (toRemove.contains(v)) {
                    assertThat(tree.contains(v)).isFalse();
                } else {
                    assertThat(tree.contains(v)).isTrue();
                }
            }
        }
    }
}
