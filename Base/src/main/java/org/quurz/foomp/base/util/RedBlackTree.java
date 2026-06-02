package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.Value;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public abstract sealed class RedBlackTree<A>
        implements BinaryTree<A>
        permits RedBlackTree.Node,
                RedBlackTree.Leaf {

    protected final InsertionStrategy insertionStrategy;
    protected final Comparer<? super A> comparer;
    protected final int blackHeight;
    protected final boolean black;

    protected RedBlackTree(final InsertionStrategy insertionStrategy,
                           final Comparer<? super A> comparer,
                           final int blackHeight,
                           final boolean black) {
        this.insertionStrategy
            = insertionStrategy;
        this.comparer
            = comparer;
        this.blackHeight
            = blackHeight;
        this.black
            = black;
    }

    @Override
    public boolean isNode() {
        return (this instanceof Node<A>);
    }

    @Override
    public @NonNull A element() {
        return null;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Returns the element stored in this node, if it exists.
     *     </p>
     * </div>
     *
     * @return a {@link Maybe.Some} containing the element, or {@link Maybe.None} if the tree is empty
     *
     * @since 1.1.0
     */
    @Override
    public @NonNull Maybe<A> elementSafe() {
        return null;    // TODO
    }

    @Override
    public @NonNull RedBlackTree<A> left() {
        return switch (this) {
            case Node<A> node -> node.left;
            default -> throw new NoSuchElementException(noValuePresent());
        };
    }

    @Override
    public @NonNull RedBlackTree<A> right() {
        return switch (this) {
            case Node<A> node -> node.right;
            default -> throw new NoSuchElementException(noValuePresent());
        };
    }

    public boolean isRed() {
        return !this.black;
    }

    public boolean isBlack() {
        return this.black;
    }

    @Override
    public int height() {
        return this.blackHeight;
    }

    @Override
    public @NonNull Tree<A> insert(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    @Override
    public boolean contains(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return false;    // TODO
    }

    @Override
    public @NonNull A search(final @NonNull A element)
            throws NoSuchElementException {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    @Override
    public @NonNull Value<A> searchSafe(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    @Override
    public @NonNull List<? extends Tree<A>> children() {
        return List.of();
    }

    @Override
    public @NonNull Tree<A> remove(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;    // TODO
    }

    @Override
    public @NonNull RedBlackTree<A> merge(final @NonNull BinaryTree<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return null;    // TODO
    }

    public static final class Node<A>
            extends RedBlackTree<A> {

        private final A element;
        private final RedBlackTree<A> left;
        private final RedBlackTree<A> right;

        private Node(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer,
                     final A element,
                     final RedBlackTree<A> left,
                     final RedBlackTree<A> right) {
            super(
                insertionStrategy,
                comparer,
                Math.max(left.blackHeight, right.blackHeight) + 1,    // TODO: Das stimmt so nicht
                true
            );
            this.element
                = element;
            this.left
                = left;
            this.right
                = right;
        }

    }

    public static final class Leaf<A>
            extends RedBlackTree<A> {

        private Leaf(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer) {
            super(insertionStrategy, comparer, 1, true);
        }

    }

}
