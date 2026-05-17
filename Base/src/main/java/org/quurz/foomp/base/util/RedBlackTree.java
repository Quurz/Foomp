package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Tree;
import org.quurz.foomp.base.types.Value;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

public abstract sealed class RedBlackTree<A>
        implements BinaryTree<A>
        permits RedBlackTree.Node,
                RedBlackTree.Leaf,
                RedBlackTree.Empty {

    public static final class Node<A>
            extends RedBlackTree<A> {}

    public static final class Leaf<A>
            extends RedBlackTree<A> {}

    public static final class Empty<A>
            extends RedBlackTree<A> {}

    @Override
    public @NonNull A element() {
        return null;
    }

    @Override
    public @NonNull BinaryTree<A> left() {
        return null;
    }

    @Override
    public @NonNull BinaryTree<A> right() {
        return null;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public @NonNull Tree<A> insert(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;
    }

    @Override
    public boolean contains(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return false;
    }

    @Override
    public @NonNull A search(final @NonNull A element)
            throws NoSuchElementException {
        Objects.requireNonNull(element, nullValue("element"));
        return null;
    }

    @Override
    public @NonNull Value<A> searchSafe(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;
    }

    @Override
    public @NonNull List<? extends Tree<A>> children() {
        return List.of();
    }

    @Override
    public @NonNull Tree<A> remove(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return null;
    }

}
