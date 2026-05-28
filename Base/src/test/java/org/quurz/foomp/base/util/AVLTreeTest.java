package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AVLTreeTest {

    @Test
    void testInsertAndImmutability() {
        AVLTree<Integer> empty = AVLTree.avlTree();
        AVLTree<Integer> t1 = empty.insert(10);
        AVLTree<Integer> t2 = t1.insert(20);
        AVLTree<Integer> t3 = t2.insert(5);

        // Check immutability
        assertThat(empty.contains(10)).isFalse();
        assertThat(t1.contains(10)).isTrue();
        assertThat(t1.contains(20)).isFalse();
        
        // Check content
        assertThat(t3.contains(10)).isTrue();
        assertThat(t3.contains(20)).isTrue();
        assertThat(t3.contains(5)).isTrue();
        assertThat(t3.contains(15)).isFalse();
    }

    @Test
    void testBalancing() {
        // Simple right rotation (LL case)
        AVLTree<Integer> tree = AVLTree.<Integer>avlTree()
                .insert(30)
                .insert(20)
                .insert(10);
        
        // Root should be 20
        assertThat(tree.element()).isEqualTo(20);
        assertThat(tree.height()).isEqualTo(1);

        // Simple left rotation (RR case)
        tree = AVLTree.<Integer>avlTree()
                .insert(10)
                .insert(20)
                .insert(30);
        
        assertThat(tree.element()).isEqualTo(20);
        assertThat(tree.height()).isEqualTo(1);

        // Double rotation (LR case)
        tree = AVLTree.<Integer>avlTree()
                .insert(30)
                .insert(10)
                .insert(20);
        
        assertThat(tree.element()).isEqualTo(20);
        assertThat(tree.height()).isEqualTo(1);

        // Double rotation (RL case)
        tree = AVLTree.<Integer>avlTree()
                .insert(10)
                .insert(30)
                .insert(20);
        
        assertThat(tree.element()).isEqualTo(20);
        assertThat(tree.height()).isEqualTo(1);
    }

    @Test
    void testConstructors() {
        AVLTree<Integer> tree = AVLTree.avlTreeOf(10, 20, 30);
        assertThat(tree.contains(10)).isTrue();
        assertThat(tree.contains(20)).isTrue();
        assertThat(tree.contains(30)).isTrue();
        assertThat(tree.height()).isEqualTo(1);
    }

    @Test
    void testRemove() {
        AVLTree<Integer> tree = AVLTree.avlTreeOf(10, 20, 30, 40, 50);
        
        // Remove leaf
        tree = tree.remove(50);
        assertThat(tree.contains(50)).isFalse();
        assertThat(tree.contains(40)).isTrue();

        // Remove node with one child
        tree = tree.remove(10);
        assertThat(tree.contains(10)).isFalse();

        // Remove node with two children
        tree = tree.remove(20);
        assertThat(tree.contains(20)).isFalse();
        assertThat(tree.contains(30)).isTrue();
        assertThat(tree.contains(40)).isTrue();
        
        // Check rebalancing after remove
        AVLTree<Integer> t = AVLTree.avlTreeOf(10, 20, 30); // Root 20
        t = t.remove(30); // Should be 20 -> 10 or balanced
        assertThat(t.contains(30)).isFalse();
        assertThat(t.contains(20)).isTrue();
        assertThat(t.contains(10)).isTrue();
    }

    @Test
    void testCopyOnWriteAndSharing() {
        AVLTree<Integer> t1 = AVLTree.avlTreeOf(10, 20, 30, 40, 50);
        // In a balanced tree of 1-50, 20 or 40 might be root.
        // Let's pick a structure where we know subtrees.
        
        AVLTree<Integer> t2 = t1.insert(60);
        
        // t2 should be different from t1
        assertThat(t2).isNotSameAs(t1);
        
        // Parts of the tree that didn't change should be shared.
        // For example, the left subtree of the root (if 30 or 40 is root) might be identical.
        // This is hard to assert without knowing exact structure, so let's use a simpler one.
        
        AVLTree<Integer> simple = AVLTree.avlTreeOf(20, 10, 30);
        AVLTree<Integer> added = simple.insert(40);
        
        // In 'added', the left child (10) should still be the same instance as in 'simple'
        // if 20 stayed root and 40 went to the right.
        if (simple.left() == added.left()) {
            // Success: sharing confirmed
        }
        
        // More reliably: after an insert that only affects one side, the other side MUST be identical
        AVLTree<Integer> root = AVLTree.avlTreeOf(100, 50, 150);
        AVLTree<Integer> next = root.insert(200);
        
        assertThat(next.left()).isSameAs(root.left());
    }

    @Test
    void testComplexRebalancing() {
        // Create a tree that requires rebalancing after multiple inserts and deletes
        AVLTree<Integer> tree = AVLTree.avlTree();
        for (int i = 1; i <= 100; i++) {
            tree = tree.insert(i);
        }
        
        assertThat(tree.height()).isLessThan(10); // AVL height for 100 nodes is ~7-8
        
        for (int i = 1; i <= 50; i++) {
            tree = tree.remove(i);
        }
        
        assertThat(tree.height()).isLessThan(10);
        for (int i = 51; i <= 100; i++) {
            assertThat(tree.contains(i)).isTrue();
        }
    }
}
