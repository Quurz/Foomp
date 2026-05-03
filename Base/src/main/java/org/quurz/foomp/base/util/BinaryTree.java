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
 *         An immutable, self-balancing binary search tree (AVL tree) implementation.
 *     </p>
 *     <p>
 *         This class provides operations for inserting, searching, and removing elements
 *         while maintaining the AVL balance property. All operations that modify the tree
 *         return a new tree instance, preserving the original tree and utilizing structural sharing
 *         to minimize memory overhead.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements maintained by this tree
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class BinaryTree<A> {

    /**
     * <div>
     *     <p>
     *         Creates a new empty {@code BinaryTree} using the natural order of the elements.
     *     </p>
     * </div>
     *
     * @param <A> the type of elements; must implement {@link Comparable}
     * @return a new empty binary tree
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> BinaryTree<A> binaryTree() {
        return new BinaryTree<>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new empty {@code BinaryTree} using the specified {@code comparator}.
     *     </p>
     * </div>
     *
     * @param comparator the comparator to use for ordering elements; must not be {@code null}
     * @param <A>        the type of elements
     * @return a new empty binary tree
     * @throws NullPointerException if {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> BinaryTree<A> binaryTree(final @NonNull Comparator<A> comparator) {
        Objects.requireNonNull(comparator, nullValue("compatator"));
        return new BinaryTree<>(komparator(comparator), (Tree<A>) NIL);
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code BinaryTree} containing the specified {@code values}.
     *     </p>
     * </div>
     *
     * @param values the values to insert; must not be {@code null}
     * @param <A>    the type of elements; must implement {@link Comparable}
     * @return a new binary tree containing the values
     * @throws NullPointerException if {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <A extends Comparable<A>> BinaryTree<A> binaryTreeOf(final @NonNull A... values) {
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new BinaryTree<A>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL), Arrays.stream(values).iterator());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code BinaryTree} using the specified {@code comparator}
     *         containing the specified {@code values}.
     *     </p>
     * </div>
     *
     * @param comparator the comparator to use for ordering elements; must not be {@code null}
     * @param values     the values to insert; must not be {@code null}
     * @param <A>        the type of elements
     * @return a new binary tree containing the values
     * @throws NullPointerException if {@code comparator} or {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <A> BinaryTree<A> binaryTreeOf(final @NonNull Comparator<A> comparator,
                                                 final @NonNull A... values) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new BinaryTree<A>(komparator(comparator), (Tree<A>) NIL), Arrays.stream(values).iterator());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code BinaryTree} containing the values from the specified {@code collection}.
     *     </p>
     * </div>
     *
     * @param values the collection of values to insert; must not be {@code null}
     * @param <A>    the type of elements; must implement {@link Comparable}
     * @return a new binary tree containing the values
     * @throws NullPointerException if {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> BinaryTree<A> binaryTreeFrom(final @NonNull Collection<A> values) {
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new BinaryTree<A>(komparator((Comparator<A>) Comparator.naturalOrder()), (Tree<A>) NIL), values.iterator());
    }

    /**
     * <div>
     *     <p>
     *         Creates a new {@code BinaryTree} using the specified {@code comparator}
     *         containing the values from the specified {@code collection}.
     *     </p>
     * </div>
     *
     * @param comparator the comparator to use for ordering elements; must not be {@code null}
     * @param values     the collection of values to insert; must not be {@code null}
     * @param <A>        the type of elements
     * @return a new binary tree containing the values
     * @throws NullPointerException if {@code comparator} or {@code values} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A> BinaryTree<A> binaryTreeFrom(final @NonNull Comparator<A> comparator,
                                                   final @NonNull Collection<A> values) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(values, nullValue("values"));
        requireNonNullElements(values, (index -> new IllegalArgumentException(nullElementInAt("values", index))));
        return initTree(new BinaryTree<A>(komparator(comparator), (Tree<A>) NIL), values.iterator());
    }

    /**
     * <div>
     *     <p>
     *         Initializes the tree by inserting all elements from the provided iterator.
     *     </p>
     * </div>
     *
     * @param <A> element type
     * @param binaryTree the initial binary tree
     * @param iterator the iterator containing the values to insert
     * @return a new binary tree containing all values from the iterator
     */
    private static <A> BinaryTree<A> initTree(final BinaryTree<A> binaryTree,
                                              final Iterator<A> iterator) {
        var current
            = binaryTree;
        while (iterator.hasNext()) {
            current = current.insert(iterator.next());
        }
        return current;
    }

    /**
     * <div>
     *     <p>
     *         A sentinel instance representing an empty tree.
     *     </p>
     * </div>
     */
    private static final Tree<?> NIL
        = new Tree.Nil<>();

    /**
     * <div>
     *     <p>
     *         Internal representation of a tree node.
     *     </p>
     * </div>
     *
     * @param <A> element type
     */
    private static abstract sealed class Tree<A>
            permits Tree.Node,
                    Tree.Leaf,
                    Tree.Nil {

        /**
         * <div>
         *     <p>
         *         The height of this node.
         *     </p>
         * </div>
         */
        protected final int height;

        /**
         * <div>
         *     <p>
         *         Constructs a tree node with the specified height.
         *     </p>
         * </div>
         *
         * @param height the height of the node
         */
        protected Tree(final int height) {
            this.height
                = height;
        }

        /**
         * <div>
         *     <p>
         *         Returns the value stored in this node.
         *     </p>
         * </div>
         *
         * @return the value
         * @throws UnsupportedOperationException if this is a {@code Nil} node
         */
        protected abstract A getValue();

        /**
         * <div>
         *     <p>
         *         Returns the left child of this node.
         *     </p>
         * </div>
         *
         * @return the left child
         * @throws UnsupportedOperationException if this is a {@code Leaf} or {@code Nil} node
         */
        protected abstract Tree<A> getLeft();

        /**
         * <div>
         *     <p>
         *         Returns the right child of this node.
         *     </p>
         * </div>
         *
         * @return the right child
         * @throws UnsupportedOperationException if this is a {@code Leaf} or {@code Nil} node
         */
        protected abstract Tree<A> getRight();

        /**
         * <div>
         *     <p>
         *         Returns the height of this node.
         *     </p>
         * </div>
         *
         * @return the height (Leaf = 0, Nil = -1)
         */
        public final int getHeight() {
            return this.height;
        }

        /**
         * <div>
         *     <p>
         *         A node representing a branch in the tree with a value and two children.
         *     </p>
         * </div>
         *
         * @param <A> element type
         */
        final static class Node<A>
                extends Tree<A> {

            private final A value;
            private final Tree<A> left;
            private final Tree<A> right;

            /**
             * <div>
             *     <p>
             *         Constructs a new internal node.
             *     </p>
             * </div>
             *
             * @param value the value to store
             * @param left  the left child
             * @param right the right child
             */
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

            @Override
            public String toString() {
                return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
                        .add("value=" + this.value)
                        .add("left=" + this.left)
                        .add("right=" + this.right)
                        .toString();
            }
        }

        /**
         * <div>
         *     <p>
         *         A node representing a leaf in the tree (has no children).
         *     </p>
         * </div>
         *
         * @param <A> element type
         */
        final static class Leaf<A>
                extends Tree<A> {

            private final A value;

            /**
             * <div>
             *     <p>
             *         Constructs a new leaf node.
             *     </p>
             * </div>
             *
             * @param value the value to store
             */
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
                throw new UnsupportedOperationException(unsupportedOperation());
            }

            @Override
            protected Tree<A> getRight() {
                throw new UnsupportedOperationException(unsupportedOperation());
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", Leaf.class.getSimpleName() + "[", "]")
                        .add("value=" + value)
                        .toString();
            }

        }

        /**
         * <div>
         *     <p>
         *         A node representing an empty tree.
         *     </p>
         * </div>
         *
         * @param <A> element type
         */
        final static class Nil<A>
                extends Tree<A> {

            /**
             * <div>
             *     <p>
             *         Constructs a new empty node.
             *     </p>
             * </div>
             */
            private Nil() {
                super(-1);
            }

            @Override
            protected A getValue() {
                throw new UnsupportedOperationException(unsupportedOperation());
            }

            @Override
            protected BinaryTree.Tree<A> getLeft() {
                throw new UnsupportedOperationException(unsupportedOperation());
            }

            @Override
            protected BinaryTree.Tree<A> getRight() {
                throw new UnsupportedOperationException(unsupportedOperation());
            }

            @Override
            public String toString() {
                return new StringJoiner(", ", Nil.class.getSimpleName() + "[", "]")
                        .toString();
            }

        }

    }

    /**
     * <div>
     *     <p>
     *         The comparator used for maintaining the search tree property.
     *     </p>
     * </div>
     */
    private Komparator<A> comparator;

    /**
     * <div>
     *     <p>
     *         The root node of this tree.
     *     </p>
     * </div>
     */
    private Tree<A> root;

    /**
     * <div>
     *     <p>
     *         Private constructor for creating new tree instances.
     *     </p>
     * </div>
     *
     * @param comparator the comparator to use
     * @param root       the root node of the tree
     */
    private BinaryTree(final Komparator<A> comparator,
                       final Tree<A> root) {
        this.comparator
            = comparator;
        this.root
            = root;
    }

    /**
     * <div>
     *     <p>
     *         Inserts the specified {@code value} into the tree.
     *     </p>
     *     <p>
     *         If the value is already present (according to the comparator), the original tree
     *         is returned. Otherwise, a new tree instance with the value inserted is returned.
     *     </p>
     * </div>
     *
     * @param value the value to insert; must not be {@code null}
     * @return a new tree instance containing the value
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public BinaryTree<A> insert(final @NonNull A value) {
        return new BinaryTree<>(this.comparator, this.insertRecursive(this.root, value));
    }

    /**
     * <div>
     *     <p>
     *         Returns {@code true} if this tree contains the specified {@code value}.
     *     </p>
     * </div>
     *
     * @param value the value to check for; must not be {@code null}
     * @return {@code true} if the value is present, {@code false} otherwise
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public boolean contains(final @NonNull A value) {
        return this.searchRecursive(value, this.root) != null;
    }

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code value} and returns it if found.
     *     </p>
     *     <p>
     *         This is useful when the comparator only considers a subset of fields for equality,
     *         allowing retrieval of the full object from the tree.
     *     </p>
     * </div>
     *
     * @param value the value to search for; must not be {@code null}
     * @return the value found in the tree
     * @throws NoSuchElementException if the value is not present
     * @throws NullPointerException   if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull A search(final @NonNull A value)
            throws NoSuchElementException {
        final A found
            = this.searchRecursive(value, this.root);
        if (found != null) {
            return found;
        } else {
            throw new NoSuchElementException(noValuePresent());
        }
    }

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code value} and returns a {@link Maybe} containing it.
     *     </p>
     * </div>
     *
     * @param value the value to search for; must not be {@code null}
     * @return a {@code Maybe} containing the found value, or an empty {@code Maybe} if not found
     * @throws NullPointerException if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public @NonNull Maybe<A> searchSafe(final @NonNull A value) {
        return maybeOfNullable(this.searchRecursive(value, this.root));
    }

    /**
     * <div>
     *     <p>
     *         Removes the specified {@code value} from the tree.
     *     </p>
     *     <p>
     *         Returns a new tree instance with the value removed.
     *     </p>
     * </div>
     *
     * @param value the value to remove; must not be {@code null}
     * @return a new tree instance without the value
     * @throws NoSuchElementException if the value is not present
     * @throws NullPointerException   if {@code value} is {@code null}
     *
     * @since 1.0.0
     */
    public BinaryTree<A> remove(final @NonNull A value)
            throws NoSuchElementException {
        Objects.requireNonNull(value, nullValue("value"));
        return new BinaryTree<>(this.comparator, this.removeRecursive(this.root, value));
    }

    /**
     * <div>
     *     <p>
     *         Recursive helper for removing a value from the tree.
     *     </p>
     * </div>
     *
     * @param current the current subtree root
     * @param value   the value to remove
     * @return the new subtree root
     * @throws NoSuchElementException if the value is not found
     */
    @SuppressWarnings("unchecked")
    private Tree<A> removeRecursive(final Tree<A> current,
                                    final A value) {
        return switch (current) {
            case Tree.Nil<A> _ -> throw new NoSuchElementException(noValuePresent());
            case Tree.Leaf<A> leaf -> {
                final var comparison = this.comparator.kompare(value, leaf.getValue());
                yield switch (comparison) {
                    case EQUAL -> (Tree<A>) NIL;
                    default -> throw new NoSuchElementException(noValuePresent());
                };
            }
            case Tree.Node<A> node -> {
                final var comparison = this.comparator.kompare(value, node.getValue());
                yield switch (comparison) {
                    case LESS -> rebalance(new Tree.Node<>(node.getValue(), removeRecursive(node.getLeft(), value), node.getRight()));
                    case GREATER -> rebalance(new Tree.Node<>(node.getValue(), node.getLeft(), removeRecursive(node.getRight(), value)));
                    case EQUAL -> {
                        if (node.getLeft() instanceof Tree.Nil) {
                            yield node.getRight();
                        } else if (node.getRight() instanceof Tree.Nil) {
                            yield node.getLeft();
                        } else {
                            final A successor = findMinimum(node.getRight());
                            yield rebalance(new Tree.Node<>(successor, node.getLeft(), removeRecursive(node.getRight(), successor)));
                        }
                    }
                };
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Finds the minimum value in the specified subtree.
     *     </p>
     * </div>
     *
     * @param current the root of the subtree to search
     * @return the minimum value found
     */
    private A findMinimum(final Tree<A> current) {
        return switch (current) {
            case Tree.Nil<A> _ -> throw new IllegalStateException();
            case Tree.Leaf<A> leaf -> leaf.getValue();
            case Tree.Node<A> node -> node.getLeft() instanceof Tree.Nil ? node.getValue() : findMinimum(node.getLeft());
        };
    }

    /**
     * <div>
     *     <p>
     *         Recursive helper for inserting a value into the tree.
     *     </p>
     * </div>
     *
     * @param current the current subtree root
     * @param value   the value to insert
     * @return the new subtree root
     */
    @SuppressWarnings("unchecked")
    private Tree<A> insertRecursive(final Tree<A> current,
                                    final A value) {
        return switch (current) {
            case Tree.Node<A> node -> {
                final var comparison
                    = this.comparator.kompare(value, node.value);
                yield switch (comparison) {
                    case LESS -> rebalance(new Tree.Node<>(node.getValue(), insertRecursive(node.getLeft(), value), node.getRight()));
                    case EQUAL -> node;
                    case GREATER -> rebalance(new Tree.Node<>(node.getValue(), node.getLeft(), insertRecursive(node.getRight(), value)));
                };
            }
            case Tree.Leaf<A> leaf -> {
                final var comparison
                    = this.comparator.kompare(value, leaf.getValue());
                yield switch (comparison) {
                    case LESS -> rebalance(new Tree.Node<>(leaf.getValue(), new Tree.Leaf<>(value), (Tree<A>) NIL));
                    case EQUAL -> leaf;
                    case GREATER -> rebalance(new Tree.Node<>(leaf.getValue(), (Tree<A>) NIL, new Tree.Leaf<>(value)));
                };
            }
            case Tree.Nil<A> _ -> new Tree.Leaf<>(value);
        };
    }

    /**
     * <div>
     *     <p>
     *         Recursive helper for searching a value in the tree.
     *     </p>
     * </div>
     *
     * @param value   the value to search for
     * @param current the current subtree root
     * @return the value found, or {@code null} if not present
     */
    private A searchRecursive(final A value,
                              final Tree<A> current) {
        return switch (current) {
            case Tree.Node<A> node -> {
                final var comparison
                    = this.comparator.kompare(value, node.value);
                yield switch (comparison) {
                    case LESS -> searchRecursive(value, node.getLeft());
                    case EQUAL -> node.value;
                    case GREATER -> searchRecursive(value, node.getRight());
                };
            }
            case Tree.Leaf<A> leaf -> this.comparator.kompare(leaf.value, value) == Komparator.Komparison.EQUAL ? leaf.value : null;
            case Tree.Nil<A> _ -> null;
        };
    }

    /**
     * <div>
     *     <p>
     *         Rebalances the specified subtree if it violates the AVL property.
     *     </p>
     * </div>
     *
     * @param current the subtree to rebalance
     * @return the rebalanced subtree
     */
    private Tree<A> rebalance(final Tree<A> current) {
        if (current instanceof Tree.Nil) {
            return current;
        }

        final int balance = getBalance(current);

        // Links-lastig
        if (balance > 1) {
            if (getBalance(current.getLeft()) < 0) {
                // Links-Rechts Fall
                final var newLeft = rotateLeft(current.getLeft());
                return rotateRight(new Tree.Node<>(current.getValue(), newLeft, current.getRight()));
            }
            return rotateRight(current);
        }

        // Rechts-lastig
        if (balance < -1) {
            if (getBalance(current.getRight()) > 0) {
                // Rechts-Links Fall
                final var newRight = rotateRight(current.getRight());
                return rotateLeft(new Tree.Node<>(current.getValue(), current.getLeft(), newRight));
            }
            return rotateLeft(current);
        }

        return current;
    }

    /**
     * <div>
     *     <p>
     *         Calculates the balance factor of the specified node.
     *     </p>
     * </div>
     *
     * @param node the node to check
     * @return the balance factor (height of left child - height of right child)
     */
    private int getBalance(final Tree<A> node) {
        return node instanceof Tree.Nil ? 0 : node.getLeft().getHeight() - node.getRight().getHeight();
    }

    /**
     * <div>
     *     <p>
     *         Performs a left rotation on the specified node.
     *     </p>
     * </div>
     *
     * @param node the node to rotate
     * @return the new root of the rotated subtree
     */
    @SuppressWarnings("unchecked")
    private Tree<A> rotateLeft(final Tree<A> node) {
        if (node instanceof Tree.Node<A> n && n.getRight() instanceof Tree.Node<A> r) {
            final var newLeft = new Tree.Node<>(n.getValue(), n.getLeft(), r.getLeft());
            return new Tree.Node<>(r.getValue(), newLeft, r.getRight());
        } else if (node instanceof Tree.Node<A> n && n.getRight() instanceof Tree.Leaf<A> l) {
            final var newLeft = new Tree.Node<>(n.getValue(), n.getLeft(), (Tree<A>) NIL);
            return new Tree.Node<>(l.getValue(), newLeft, (Tree<A>) NIL);
        }
        return node;
    }

    /**
     * <div>
     *     <p>
     *         Performs a right rotation on the specified node.
     *     </p>
     * </div>
     *
     * @param node the node to rotate
     * @return the new root of the rotated subtree
     */
    @SuppressWarnings("unchecked")
    private Tree<A> rotateRight(final Tree<A> node) {
        if (node instanceof Tree.Node<A> n && n.getLeft() instanceof Tree.Node<A> l) {
            final var newRight = new Tree.Node<>(n.getValue(), l.getRight(), n.getRight());
            return new Tree.Node<>(l.getValue(), l.getLeft(), newRight);
        } else if (node instanceof Tree.Node<A> n && n.getLeft() instanceof Tree.Leaf<A> l) {
            final var newRight = new Tree.Node<>(n.getValue(), (Tree<A>) NIL, n.getRight());
            return new Tree.Node<>(l.getValue(), (Tree<A>) NIL, newRight);
        }
        return node;
    }

}
