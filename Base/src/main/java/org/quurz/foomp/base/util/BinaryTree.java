package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Komparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.quurz.foomp.base.functions.Komparator.komparator;

public class BinaryTree<A> {

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> BinaryTree<A> binaryTree() {
        return new BinaryTree<A>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL);
    }

    @SuppressWarnings("unchecked")
    public static <A> BinaryTree<A> binaryTree(final @NonNull Comparator<A> comparator) {
        return new BinaryTree<>(komparator(comparator), (Tree<A>) NIL);
    }

    @SafeVarargs
    public static <A extends Comparable<A>> BinaryTree<A> binaryTreeOf(final @NonNull A... values) {
        return null;    // TODO
    }

    @SafeVarargs
    public static <A> BinaryTree<A> binaryTreeOf(final @NonNull Comparator<A> comparator,
                                                 final @NonNull A... values) {
        return null;    // TODO
    }

    public static <A extends Comparable<A>> BinaryTree<A> binaryTreeFrom(final @NonNull Collection<A> values) {
        return null;    // TODO
    }

    public static <A> BinaryTree<A> binaryTreeFrom(final @NonNull Comparator<A> comparator,
                                                   final @NonNull Collection<A> values) {
        return null;    // TODO
    }

    private static final Tree<?> NIL
        = new Tree.Nil<>();

    private static abstract sealed class Tree<A>
            permits Tree.Node,
                    Tree.Leaf,
                    Tree.Nil {

        protected final int height;

        protected abstract A getValue();

        protected abstract Tree<A> getLeft();

        protected abstract Tree<A> getRight();

        protected Tree(final int height) {
            this.height
                = height;
        }

        public final int getHeight() {
            return this.height;
        }

        final static class Node<A>
                extends Tree<A> {

            private final A value;
            private final Tree<A> left;
            private final Tree<A> right;

            private Node(final A value,
                         final Tree<A> left,
                         final Tree<A> right) {
                super(Math.max(left.getHeight(), right.getHeight()) + 1);
                this.value
                    = value;
                this.left
                    = left;
                this.right
                    = right;
            }

            @Override
            protected A getValue() {
                return this.value;
            }

            @Override
            protected Tree<A> getLeft() {
                return this.left;
            }

            @Override
            protected Tree<A> getRight() {
                return this.right;
            }

        }

        final static class Leaf<A>
                extends Tree<A> {

            private final A value;

            private Leaf(final A value) {
                super(0);
                this.value
                    = value;
            }

            @Override
            protected A getValue() {
                return this.value;
            }

            @Override
            protected Tree<A> getLeft() {
                throw new UnsupportedOperationException();    // TODO: Message
            }

            @Override
            protected Tree<A> getRight() {
                throw new UnsupportedOperationException();    // TODO: Message
            }

        }

        final static class Nil<A>
                extends Tree<A> {

            private Nil() {
                super(-1);
            }

            @Override
            protected A getValue() {
                throw new UnsupportedOperationException();    // TODO: Message
            }

            @Override
            protected BinaryTree.Tree<A> getLeft() {
                throw new UnsupportedOperationException();    // TODO: Message
            }

            @Override
            protected BinaryTree.Tree<A> getRight() {
                throw new UnsupportedOperationException();    // TODO: Message
            }

        }

    }

    private Komparator<A> comparator;
    private Tree<A> root;

    private BinaryTree(final Komparator<A> comparator,
                       final Tree<A> root) {
        this.comparator
            = comparator;
        this.root
            = root;
    }

    public BinaryTree<A> insert(final @NonNull A value) {
        return new BinaryTree<>(this.comparator, this.insertRecursive(this.root, value));
    }

    public boolean contains(final @NonNull A value) {
        return false;    // TODO
    }

    public @NonNull A search(final @NonNull A value)
            throws NoSuchElementException {
        return null;    // TODO
    }

    public @NonNull Maybe<A> searchSafe(final @NonNull A value) {
        return Maybe.none();    // TODO
    }

    public BinaryTree<A> remove(final @NonNull A value)
            throws NoSuchElementException {
        return null;    // TODO
    }

    @SuppressWarnings("unchecked")
    private Tree<A> insertRecursive(final Tree<A> current,
                                    final A value) {
        return switch (current) {
            case Tree.Node<A> node -> {
                final var comparison
                    = this.comparator.kompare(value, node.value);
                yield switch (comparison) {
                    case LESS -> new Tree.Node<>(node.getValue(), insertRecursive(node.getLeft(), value), node.getRight());
                    case EQUAL -> node;
                    case GREATER -> new Tree.Node<>(node.getValue(), node.getLeft(), insertRecursive(node.getRight(), value));
                };
            }
            case Tree.Leaf<A> leaf -> {
                final var comparison
                    = this.comparator.kompare(value, leaf.getValue());
                yield switch (comparison) {
                    case LESS -> new Tree.Node<>(leaf.getValue(), new Tree.Leaf<>(value), (Tree<A>) NIL);
                    case EQUAL -> leaf;
                    case GREATER -> new Tree.Node<>(leaf.getValue(), (Tree<A>) NIL, new Tree.Leaf<>(value));
                };
            }
            case Tree.Nil<A> _ -> new Tree.Leaf<>(value);
        };
    }

}
