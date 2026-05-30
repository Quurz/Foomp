package org.quurz.foomp.base.util;

import static org.quurz.foomp.base.util.AVLTree.avlTree;

public class TestMain {

    static void main(final String... args) {
        AVLTree<Integer> avlTree
            = avlTree();
        System.out.println(avlTree);
        for (int i = 1; i <= 10; i++) {
            avlTree
                = avlTree.insert(i);
            System.out.println(avlTree);
        }

    }

}
