package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Komparator;

import java.util.*;

import static org.quurz.foomp.base.functions.Komparator.komparator;
import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;
import static org.quurz.foomp.base.util.Util.requireNonNullElements;

/**
 * <div>
 *     <p>
 *         An immutable, self-balancing red-black tree implementation.
 *     </p>
 *     <p>
 *         This class provides operations for inserting, searching, and maintaining
 *         the red-black balance properties. All operations that modify the tree
 *         return a new tree instance, preserving the original tree and utilizing structural sharing.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements maintained by this tree
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class RedBlackTree<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new empty {@code RedBlackTree} using the natural order of the elements.
     *     </p>
     * </div>
     *
     * @param <A> the type of elements; must implement {@link Comparable}
     * @return a new empty red-black tree
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTree() {
        return new RedBlackTree<>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new empty {@code RedBlackTree} using the specified {@code comparator}.
     *     </p>
     * </div>
     *
     * @param comparator the comparator to use for ordering elements; must not be {@code null}
     * @param <A>        the type of elements
     * @return a new empty red-black tree
     * @throws NullPointerException if {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> RedBlackTree<A> redBlackTree(final @NonNull Comparator<A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return new RedBlackTree<>(komparator(comparator), (Tree<A>) NIL);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code RedBlackTree} containing the specified {@code values}.
     *     </p>
     * </div>
     *
     * @param values the values to insert; must not be {@code null}
     * @param <A>    the type of elements; must implement {@link Comparable}
     * @return a new red-black tree containing the values
     * @throws NullPointerException if {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeOf(final @NonNull A... values) {
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new RedBlackTree<A>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL), Arrays.stream(values).iterator());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code RedBlackTree} containing the elements of the specified {@code collection}.
     *     </p>
     * </div>
     *
     * @param values the values to insert; must not be {@code null}
     * @param <A>    the type of elements; must implement {@link Comparable}
     * @return a new red-black tree containing the values
     * @throws NullPointerException if {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> RedBlackTree<A> redBlackTreeFrom(final @NonNull Collection<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new RedBlackTree<A>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL), values.iterator());
    }

    /**
     * <div>
     *     <p>
     *         Initializes the tree by inserting all elements from the provided iterator.
     *     </p>
     * </div>
     *
     * @param <A> element type
     * @param redBlackTree the initial red-black tree
     * @param iterator the iterator containing the values to insert
     * @return a new red-black tree containing all values from the iterator
     */
    private static <A> RedBlackTree<A> initTree(final RedBlackTree<A> redBlackTree,
                                                final Iterator<A> iterator) {
        var current = redBlackTree;
        while (iterator.hasNext()) {
            current = current.insert(iterator.next());
        }
        return current;
    }

    private static final Tree<?> NIL = new Tree.Nil<>();

    private static abstract sealed class Tree<A>
            permits Tree.Node, Tree.Nil {

        protected final boolean black;

        protected Tree(final boolean black) {
            this.black = black;
        }

        protected abstract A getValue();

        protected abstract Tree<A> getLeft();

        protected abstract Tree<A> getRight();

        private static final class Node<A> extends Tree<A> {
            private final A value;
            private final Tree<A> left;
            private final Tree<A> right;

            private Node(final boolean black, final A value, final Tree<A> left, final Tree<A> right) {
                super(black);
                this.value = value;
                this.left = left;
                this.right = right;
            }

            @Override
            protected A getValue() { return value; }

            @Override
            protected Tree<A> getLeft() { return left; }

            @Override
            protected Tree<A> getRight() { return right; }
        }

        private static final class Nil<A> extends Tree<A> {
            private Nil() { super(true); }

            @Override
            protected A getValue() { throw new UnsupportedOperationException(); }

            @Override
            protected Tree<A> getLeft() { throw new UnsupportedOperationException(); }

            @Override
            protected Tree<A> getRight() { throw new UnsupportedOperationException(); }
        }
    }

    private final Komparator<A> comparator;
    private final Tree<A> root;

    private RedBlackTree(final Komparator<A> comparator, final Tree<A> root) {
        this.comparator = comparator;
        this.root = root;
    }

    /**
     * <div>
     *     <p>
     *         Inserts the specified {@code value} into the tree.
     *     </p>
     * </div>
     *
     * @param value the value to insert; must not be {@code null}
     * @return a new tree instance containing the value
     * @throws NullPointerException if {@code value} is {@code null}
     */
    public RedBlackTree<A> insert(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        Tree<A> newRoot = insertRecursive(this.root, value);
        // The root of a red-black tree must always be black
        return new RedBlackTree<>(this.comparator, forceBlack(newRoot));
    }

    private Tree<A> insertRecursive(final Tree<A> current, final A value) {
        if (current instanceof Tree.Nil) {
            // New nodes are inserted as red
            return new Tree.Node<>(false, value, current, current);
        }

        final var comparison = this.comparator.kompare(value, current.getValue());
        return switch (comparison) {
            case LESS -> rebalance(current.black, current.getValue(), insertRecursive(current.getLeft(), value), current.getRight());
            case GREATER -> rebalance(current.black, current.getValue(), current.getLeft(), insertRecursive(current.getRight(), value));
            case EQUAL -> current;
        };
    }

    /**
     * <div>
     *     <p>
     *         Okasaki-style balancing for red-black trees.
     *     </p>
     * </div>
     */
    private Tree<A> rebalance(boolean black, A x, Tree<A> left, Tree<A> right) {
        if (black) {
            // Case 1: Left child is red and has a red left child
            if (!left.black && left instanceof Tree.Node<A> l && !l.left.black && l.left instanceof Tree.Node<A> ll) {
                return new Tree.Node<>(false, l.value,
                        new Tree.Node<>(true, ll.value, ll.left, ll.right),
                        new Tree.Node<>(true, x, l.right, right));
            }
            // Case 2: Left child is red and has a red right child
            if (!left.black && left instanceof Tree.Node<A> l && !l.right.black && l.right instanceof Tree.Node<A> lr) {
                return new Tree.Node<>(false, lr.value,
                        new Tree.Node<>(true, l.value, l.left, lr.left),
                        new Tree.Node<>(true, x, lr.right, right));
            }
            // Case 3: Right child is red and has a red left child
            if (!right.black && right instanceof Tree.Node<A> r && !r.left.black && r.left instanceof Tree.Node<A> rl) {
                return new Tree.Node<>(false, rl.value,
                        new Tree.Node<>(true, x, left, rl.left),
                        new Tree.Node<>(true, r.value, rl.right, r.right));
            }
            // Case 4: Right child is red and has a red right child
            if (!right.black && right instanceof Tree.Node<A> r && !r.right.black && r.right instanceof Tree.Node<A> rr) {
                return new Tree.Node<>(false, r.value,
                        new Tree.Node<>(true, x, left, r.left),
                        new Tree.Node<>(true, rr.value, rr.left, rr.right));
            }
        }
        return new Tree.Node<>(black, x, left, right);
    }

    private Tree<A> forceBlack(Tree<A> tree) {
        if (tree instanceof Tree.Node<A> node && !node.black) {
            return new Tree.Node<>(true, node.value, node.left, node.right);
        }
        return tree;
    }

    /**
     * <div>
     *     <p>
     *         Checks if the specified {@code value} is present in the tree.
     *     </p>
     * </div>
     *
     * @param value the value to search for; must not be {@code null}
     * @return {@code true} if the value is present, {@code false} otherwise
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public boolean contains(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return searchRecursive(this.root, value).isPresent();
    }

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code value} in the tree.
     *     </p>
     * </div>
     *
     * @param value the value to search for; must not be {@code null}
     * @return the value if found
     * @throws NoSuchElementException if the value is not found
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public A search(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return searchRecursive(this.root, value).orElseThrow(() -> new NoSuchElementException(noValuePresent()));
    }

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code value} in the tree and returns it wrapped in a {@link Maybe}.
     *     </p>
     * </div>
     *
     * @param value the value to search for; must not be {@code null}
     * @return a {@code Maybe} containing the value if found, or {@code None} otherwise
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public Maybe<A> searchSafe(final @NonNull A value) {
        Objects.requireNonNull(value, nullValue("value"));
        return maybeOfNullable(searchRecursive(this.root, value).orElse(null));
    }

    private Optional<A> searchRecursive(Tree<A> current, A value) {
        if (current instanceof Tree.Nil) {
            return Optional.empty();
        }
        final var comparison = this.comparator.kompare(value, current.getValue());
        return switch (comparison) {
            case EQUAL -> Optional.of(current.getValue());
            case LESS -> searchRecursive(current.getLeft(), value);
            case GREATER -> searchRecursive(current.getRight(), value);
        };
    }
}
