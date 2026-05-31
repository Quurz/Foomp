package org.quurz.foomp.base.util;

import static org.quurz.foomp.base.util.AVLTree.avlTree;

public class TestMain {

    static void main(final String... args) {
        System.out.println("Vollständiger Baum:");
        AVLTree<Integer> avlTree = avlTree();
        for (int i = 1; i <= 3; i++) avlTree = avlTree.insert(i);
        System.out.println(avlTree.echo());

        System.out.println("\nBaum mit nur linkem Kind (2 -> 1):");
        AVLTree<Integer> leftOnly = avlTree();
        leftOnly = leftOnly.insert(2).insert(1);
        System.out.println(leftOnly.echo());

        System.out.println("\nBaum mit nur rechtem Kind (1 -> 2):");
        AVLTree<Integer> rightOnly = avlTree();
        rightOnly = rightOnly.insert(1).insert(2);
        System.out.println(rightOnly.echo());

        System.out.println("\nGroßer Baum:");
        AVLTree<Integer> bigTree = avlTree();
        for (int i = 1; i <= 10; i++) bigTree = bigTree.insert(i);
        System.out.println(bigTree.echo());
    }

}
