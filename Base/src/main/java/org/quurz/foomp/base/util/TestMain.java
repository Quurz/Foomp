package org.quurz.foomp.base.util;

import static org.quurz.foomp.base.util.AVLTree.avlTreeOf;

public class TestMain {

    static void main(final String... args) {
        final var avlTree
            = avlTreeOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(avlTree);
    }

}
