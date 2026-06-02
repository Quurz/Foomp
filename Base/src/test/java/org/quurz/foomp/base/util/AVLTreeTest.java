package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.quurz.foomp.base.types.Tree;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.AVLTree.avlTree;
import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;
import static org.slf4j.LoggerFactory.getLogger;

class AVLTreeTest extends TestHelper {

    private static final Logger LOGGER
        = getLogger(AVLTreeTest.class);

    private record Element(@NonNull Integer key,
                           @NonNull String value)
        implements Comparable<Element> {

        @Override
        public int compareTo(final @NonNull Element other) {
            return Integer.compare(this.key, other.key);
        }

    }

    @Nested
    class Factory {

        @Test
        void avlTree_creates_empty_tree() {
            LOGGER.info("avlTree() should create an empty tree with height 0");
            final var tree = avlTree();
            assertThat(tree.height()).isEqualTo(0);
            assertThat(tree.children()).isEmpty();
        }

        @Test
        void avlTree_with_strategy_creates_empty_tree() {
            LOGGER.info("avlTree(Strategy) should create an empty tree");
            final var tree = avlTree(Tree.InsertionStrategy.Replace);
            assertThat(tree.height()).isEqualTo(0);
        }

        @Test
        void avlTreeOf_creates_tree_with_elements() {
            LOGGER.info("avlTreeOf(...) should create a tree with given elements");
            final var tree = avlTreeOf(1, 2, 3);
            assertThat(tree.height()).isEqualTo(2);
            assertThat(tree.contains(1)).isTrue();
            assertThat(tree.contains(2)).isTrue();
            assertThat(tree.contains(3)).isTrue();
        }

        @Test
        void avlTreeOf_with_strategy_creates_tree() {
            LOGGER.info("avlTreeOf(Strategy, ...) should create a tree with given elements and strategy");
            final var tree = avlTreeOf(Tree.InsertionStrategy.Discard, 1, 2, 3);
            assertThat(tree.height()).isEqualTo(2);
            assertThat(tree.contains(2)).isTrue();
        }

        @Test
        void avlTreeFrom_creates_tree_from_collection() {
            LOGGER.info("avlTreeFrom(Collection) should create a tree from collection");
            final var tree = AVLTree.avlTreeFrom(List.of(1, 2, 3));
            assertThat(tree.height()).isEqualTo(2);
            assertThat(tree.contains(1)).isTrue();
        }

        @Test
        void avlTreeFrom_with_strategy_creates_tree() {
            LOGGER.info("avlTreeFrom(Strategy, Collection) should create a tree with strategy");
            final var tree = AVLTree.avlTreeFrom(Tree.InsertionStrategy.Replace, List.of(3, 2, 1));
            assertThat(tree.height()).isEqualTo(2);
            assertThat(tree.contains(2)).isTrue();
        }

        @Test
        void avlTreeFrom_with_comparator_creates_tree() {
            LOGGER.info("avlTreeFrom(Comparator, Collection) should create a tree with comparator");
            final var tree = AVLTree.avlTreeFrom(Comparator.reverseOrder(), List.of(1, 2, 3));
            // In reverse order, 3 is "smallest", 1 is "largest"
            assertThat(tree.contains(2)).isTrue();
        }

        @Test
        void avlTreeFrom_empty_collection_returns_leaf() {
            LOGGER.info("avlTreeFrom(EmptyCollection) should return an empty tree");
            final var tree = AVLTree.avlTreeFrom(Collections.<Integer>emptyList());
            assertThat(tree.height()).isEqualTo(0);
        }

        @Test
        void avlTree_with_null_arguments_throws() {
            LOGGER.info("avlTree with null arguments should throw NullPointerException");
            assertThatThrownBy(() -> avlTree((Comparator<Integer>) null))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> avlTree((Tree.InsertionStrategy) null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class Insertion {

        @Test
        void insert_maintains_avl_balance_RR() {
            LOGGER.info("insert should maintain AVL balance in Right-Right case");
            // Right-Right case
            var tree = avlTree(Integer::compareTo);
            tree = tree.insert(1).insert(2).insert(3);
            
            // Should be: 2
            //           / \
            //          1   3
            assertThat(tree.element()).isEqualTo(2);
            assertThat(tree.left().element()).isEqualTo(1);
            assertThat(tree.right().element()).isEqualTo(3);
            assertThat(tree.height()).isEqualTo(2);
            validateAVLProperty(tree);
        }

        @Test
        void insert_maintains_avl_balance_LL() {
            LOGGER.info("insert should maintain AVL balance in Left-Left case");
            // Left-Left case
            var tree = avlTree(Integer::compareTo);
            tree = tree.insert(3).insert(2).insert(1);
            
            // Should be: 2
            //           / \
            //          1   3
            assertThat(tree.element()).isEqualTo(2);
            assertThat(tree.left().element()).isEqualTo(1);
            assertThat(tree.right().element()).isEqualTo(3);
            validateAVLProperty(tree);
        }

        @Test
        void insert_maintains_avl_balance_LR() {
            LOGGER.info("insert should maintain AVL balance in Left-Right case");
            // Left-Right case
            var tree = avlTree(Integer::compareTo);
            tree = tree.insert(3).insert(1).insert(2);
            
            // Should be: 2
            //           / \
            //          1   3
            assertThat(tree.element()).isEqualTo(2);
            assertThat(tree.left().element()).isEqualTo(1);
            assertThat(tree.right().element()).isEqualTo(3);
            validateAVLProperty(tree);
        }

        @Test
        void insert_maintains_avl_balance_RL() {
            LOGGER.info("insert should maintain AVL balance in Right-Left case");
            // Right-Left case
            var tree = avlTree(Integer::compareTo);
            tree = tree.insert(1).insert(3).insert(2);
            
            // Should be: 2
            //           / \
            //          1   3
            assertThat(tree.element()).isEqualTo(2);
            assertThat(tree.left().element()).isEqualTo(1);
            assertThat(tree.right().element()).isEqualTo(3);
            validateAVLProperty(tree);
        }

        @Test
        void insert_with_discard_strategy_does_not_replace() {
            LOGGER.info("insert with Discard strategy should not replace existing elements");
            final var tree = avlTree(Tree.InsertionStrategy.Discard, Element::compareTo);
            final var tree1 = tree.insert(new Element(1, "A"));
            final var tree2 = tree1.insert(new Element(1, "B"));
            
            assertThat(tree1).isSameAs(tree2);
            assertThat(tree1.search(new Element(1, "")).value()).isEqualTo("A");
        }

        @Test
        void insert_with_replace_strategy_replaces_element() {
            LOGGER.info("insert with Replace strategy should replace existing elements");
            final var tree = avlTree(Tree.InsertionStrategy.Replace, Element::compareTo);
            final var tree1 = tree.insert(new Element(1, "A"));
            final var tree2 = tree1.insert(new Element(1, "B"));
            
            assertThat(tree1).isNotSameAs(tree2);
            assertThat(tree2.search(new Element(1, "")).value()).isEqualTo("B");
        }
    }

    @Nested
    class Search {

        @Test
        void contains_returns_correct_results() {
            LOGGER.info("contains should return correct results for existing and non-existing elements");
            final var tree = avlTreeOf(1, 2, 3, 4, 5);
            assertThat(tree.contains(3)).isTrue();
            assertThat(tree.contains(6)).isFalse();
        }

        @Test
        void search_returns_element_or_throws() {
            LOGGER.info("search should return the element if found or throw NoSuchElementException");
            final var tree = avlTreeOf(1, 2, 3);
            assertThat(tree.search(2)).isEqualTo(2);
            assertThatThrownBy(() -> tree.search(4))
                .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void searchSafe_returns_maybe() {
            LOGGER.info("searchSafe should return a Some maybe if found or None if not found");
            final var tree = avlTreeOf(1, 2, 3);
            checkIsSomeWithValue(tree.searchSafe(2), 2);
            checkIsNone(tree.searchSafe(4));
        }

        @Test
        void elementSafe_returns_element_or_none() {
            LOGGER.info("elementSafe should return a Some maybe for non-empty tree or None for empty tree");
            final var tree = avlTreeOf(1);
            checkIsSomeWithValue(tree.elementSafe(), 1);
            checkIsNone(avlTree().elementSafe());
        }

        @Test
        void left_and_right_throw_on_leaf() {
            LOGGER.info("left and right should throw NoSuchElementException on a leaf node");
            final var tree = avlTree();
            assertThatThrownBy(tree::left).isInstanceOf(NoSuchElementException.class);
            assertThatThrownBy(tree::right).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void children_returns_empty_list_for_leaf() {
            LOGGER.info("children should return an empty list for a leaf node");
            assertThat(avlTree().children()).isEmpty();
        }
    }

    @Nested
    class Removal {

        @Test
        void remove_leaf_node() {
            LOGGER.info("remove should correctly remove a leaf node and maintain balance");
            var tree = avlTreeOf(2, 1, 3);
            tree = tree.remove(1);
            assertThat(tree.contains(1)).isFalse();
            assertThat(tree.contains(2)).isTrue();
            assertThat(tree.contains(3)).isTrue();
            validateAVLProperty(tree);
        }

        @Test
        void remove_node_with_one_child() {
            LOGGER.info("remove should correctly remove a node with one child and maintain balance");
            var tree = avlTreeOf(2, 1, 3, 4);
            tree = tree.remove(3);
            assertThat(tree.contains(3)).isFalse();
            assertThat(tree.contains(4)).isTrue();
            validateAVLProperty(tree);
        }

        @Test
        void remove_node_with_two_children() {
            LOGGER.info("remove should correctly remove a node with two children and maintain balance");
            var tree = avlTreeOf(5, 3, 7, 2, 4, 6, 8);
            tree = tree.remove(3);
            assertThat(tree.contains(3)).isFalse();
            assertThat(tree.contains(4)).isTrue();
            assertThat(tree.contains(2)).isTrue();
            validateAVLProperty(tree);
        }

        @Test
        void remove_root_until_empty() {
            LOGGER.info("remove should correctly handle removal of root until tree is empty");
            var tree = avlTreeOf(1, 2, 3);
            tree = tree.remove(2).remove(1).remove(3);
            assertThat(tree.height()).isEqualTo(0);
        }
    }

    @Nested
    class Utilities {

        @Test
        void merge_combines_two_trees() {
            LOGGER.info("merge should correctly combine two AVL trees");
            final var tree1 = avlTreeOf(1, 3, 5);
            final var tree2 = avlTreeOf(2, 4, 6);
            
            final var merged = tree1.merge(tree2);
            
            assertThat(merged.height()).isEqualTo(3);
            for (int i = 1; i <= 6; i++) {
                assertThat(merged.contains(i)).isTrue();
            }
            validateAVLProperty(merged);
        }

        @Test
        void merge_with_overlapping_elements_respects_strategy() {
            LOGGER.info("merge with overlapping elements should respect the insertion strategy");
            // Strategy Discard (default)
            final var tree1 = avlTreeOf(new Element(1, "A"));
            final var tree2 = avlTreeOf(new Element(1, "B"));
            
            final var mergedDiscard = tree1.merge(tree2);
            assertThat(mergedDiscard.search(new Element(1, "")).value()).isEqualTo("A");
            
            // Strategy Replace
            final var tree3 = avlTree(Tree.InsertionStrategy.Replace, Element::compareTo)
                .insert(new Element(1, "A"));
            final var tree4 = avlTreeOf(new Element(1, "B"));
            
            final var mergedReplace = tree3.merge(tree4);
            assertThat(mergedReplace.search(new Element(1, "")).value()).isEqualTo("B");
        }

        @Test
        void collectToAVLTree_collector_works() {
            LOGGER.info("collectToAVLTree collector should correctly collect stream elements into an AVL tree");
            final var tree = Stream.of(3, 1, 2)
                .collect(AVLTree.collectToAVLTree(Tree.InsertionStrategy.Discard, Integer::compareTo));
            
            assertThat(tree.height()).isEqualTo(2);
            assertThat(tree.contains(1)).isTrue();
            assertThat(tree.contains(2)).isTrue();
            assertThat(tree.contains(3)).isTrue();
            validateAVLProperty(tree);
        }

        @Test
        void echo_returns_non_empty_string() {
            LOGGER.info("echo should return a structured string representation of the tree");
            final var tree = avlTreeOf(1, 2, 3);
            assertThat(tree.echo()).contains("2").contains("1").contains("3");
        }

        @Test
        void copy_creates_equal_tree() {
            LOGGER.info("copy should create an equal but different instance of the tree");
            final var tree = avlTreeOf(1, 2, 3, 4, 5);
            final var copy = tree.copy();
            assertThat(tree.echo()).isEqualTo(copy.echo());
            assertThat(tree).isNotSameAs(copy);
        }

        @Test
        void height_is_correct() {
            LOGGER.info("height should correctly reflect the maximum path from root to leaf");
            assertThat(avlTree().height()).isEqualTo(0);
            assertThat(avlTreeOf(1).height()).isEqualTo(1);
            assertThat(avlTreeOf(1, 2).height()).isEqualTo(2);
            assertThat(avlTreeOf(1, 2, 3).height()).isEqualTo(2);
        }

        @Test
        void transmogrify_works() {
            LOGGER.info("transmogrify should correctly apply transformation to the tree");
            final var tree = avlTreeOf(1, 2, 3);
            final String result = tree.transmogrify(t -> "Height: " + t.height());
            assertThat(result).isEqualTo("Height: 2");
        }

        @Test
        void equals_and_hashCode_work() {
            LOGGER.info("equals and hashCode should work based on in-order sequence");
            final var tree1 = avlTreeOf(1, 2, 3);
            final var tree2 = avlTreeOf(3, 2, 1); // Same elements, may have different insertion history
            final var tree3 = avlTreeOf(1, 2);
            final var tree4 = avlTreeOf(1, 2, 4);

            assertThat(tree1).isEqualTo(tree2);
            assertThat(tree1.hashCode()).isEqualTo(tree2.hashCode());

            assertThat(tree1).isNotEqualTo(tree3);
            assertThat(tree1.hashCode()).isNotEqualTo(tree3.hashCode());

            assertThat(tree1).isNotEqualTo(tree4);
        }

        @Test
        void equals_works_with_different_internal_structure() {
            LOGGER.info("equals should return true for trees with same elements but different structures");
            // AVL trees with same elements are usually same structure, but let's be sure.
            // We can compare it with a different BinaryTree implementation if we had one,
            // or just ensure that different insertion orders resulting in same elements are equal.
            final var tree1 = avlTreeOf(2, 1, 3);
            final var tree2 = avlTreeOf(1, 2, 3);
            
            assertThat(tree1).isEqualTo(tree2);
            assertThat(tree1.hashCode()).isEqualTo(tree2.hashCode());
        }
    }

    private void validateAVLProperty(AVLTree<?> tree) {
        if (tree.height() <= 1) return;
        
        int leftHeight = tree.children().get(0).height();
        int rightHeight = tree.children().get(1).height();
        
        assertThat(Math.abs(leftHeight - rightHeight))
            .withFailMessage("AVL property violated at node with element %s: left height %d, right height %d",
                tree.element(), leftHeight, rightHeight)
            .isLessThanOrEqualTo(1);
            
        for (var child : tree.children()) {
            if (child instanceof AVLTree.Node) {
                validateAVLProperty(child);
            }
        }
    }
    
}
