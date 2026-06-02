package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.types.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.quurz.foomp.base.functions.Comparer.comparer;
import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.types.Tree.InsertionStrategy.Discard;

public abstract sealed class RedBlackTree<A>
        implements BinaryTree<A>,
                   Transmogrifyable<RedBlackTree<A>>,
                   Echo,
                   Copyable<RedBlackTree<A>>
        permits RedBlackTree.Node,
                RedBlackTree.Leaf {

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTree() {
        return new Leaf<>(Discard, comparer((Comparator<? super A>) Comparator.naturalOrder()));
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTree(final @NonNull InsertionStrategy insertionStrategy) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        return new Leaf<>(insertionStrategy,comparer((Comparator<? super A>) Comparator.naturalOrder()));
    }

    public static <A> RedBlackTree<A> redBlackTree(final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return new Leaf<>(Discard, comparer(comparator));
    }

    public static <A> RedBlackTree<A> redBlackTree(final @NonNull InsertionStrategy insertionStrategy,
                                                   final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return new Leaf<>(insertionStrategy, comparer(comparator));
    }

    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, Comparator.naturalOrder(), Stream.of(elements));
    }

    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeOf(final @NonNull InsertionStrategy insertionStrategy,
                                                                           final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(insertionStrategy, Comparator.naturalOrder(), Stream.of(elements));
    }

    @SafeVarargs
    public static <A> RedBlackTree<A> redBlackTreeOf(final @NonNull Comparator<? super A> comparator,
                                                     final @NonNull A... elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, comparator, Stream.of(elements));
    }

    @SafeVarargs
    public static <A> RedBlackTree<A> redBlackTreeOf(final @NonNull InsertionStrategy insertionStrategy,
                                                     final @NonNull Comparator<? super A> comparator,
                                                     final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(insertionStrategy, comparator, Stream.of(elements));
    }

    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeFrom(final @NonNull Collection<A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, Comparator.naturalOrder(), elements.stream());
    }

    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                                                             final @NonNull Collection<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(insertionStrategy, Comparator.naturalOrder(), elements.stream());
    }

    public static <A> RedBlackTree<A> redBlackTreeFrom(final @NonNull Comparator<? super A> comparator,
                                                       final @NonNull Collection<A> elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, comparator, elements.stream());
    }

    public static <A> RedBlackTree<A> redBlackTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                                       final @NonNull Comparator<? super A> comparator,
                                                       final @NonNull Collection<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(insertionStrategy, comparator, elements.stream());
    }

    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeFrom(final @NonNull Stream<A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, Comparator.naturalOrder(), elements);
    }

    public static <A> RedBlackTree<A> redBlackTreeFrom(final @NonNull Comparator<? super A> comparator,
                                                       final @NonNull Stream<A> elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return redBlackTreeFrom(Discard, comparator, elements);
    }

    public static <A> RedBlackTree<A> redBlackTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                                       final @NonNull Comparator<? super A> comparator,
                                                       final @NonNull Stream<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return elements.reduce(
            redBlackTree(insertionStrategy, comparator),
            RedBlackTree::insert,
            RedBlackTree::merge
        );
    }

    public static <A> Collector<A, Set<A>, RedBlackTree<A>> collectToRedBlackTree(final @NonNull InsertionStrategy insertionStrategy,
                                                                                  final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));

        return Collector.of(
            HashSet::new,
            Set::add,
            (left, right) -> { left.addAll(right); return left; },
            set -> redBlackTreeFrom(insertionStrategy, comparator, (Collection<A>) set)
        );
    }

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
    public @NonNull RedBlackTree<A> insert(final @NonNull A element) {
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

    @Override
    public @NonNull <T> T transmogrify(@NonNull Function<? super RedBlackTree<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return null;    // TODO
    }

    @Override
    public @NonNull String echo() {
        return "";    // TODO
    }

    @Override
    public boolean equals(final Object other) {
        // TODO: Rekursiv
        if (!(other instanceof RedBlackTree<?> that)) return false;
        return blackHeight == that.blackHeight && black == that.black && insertionStrategy == that.insertionStrategy && Objects.equals(comparer, that.comparer);
    }

    @Override
    public int hashCode() {
        // TODO: Rekursiv
        return Objects.hash(insertionStrategy, comparer, blackHeight, black);
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

        @Override
        public @NonNull RedBlackTree<A> copy() {
            return null;    // TODO
        }

    }

    public static final class Leaf<A>
            extends RedBlackTree<A> {

        private Leaf(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer) {
            super(insertionStrategy, comparer, 1, true);
        }

        @Override
        public @NonNull RedBlackTree<A> copy() {
            return null;
        }

    }

}
