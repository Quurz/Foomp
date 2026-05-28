package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Tree;

import java.util.*;
import java.util.stream.Stream;

import static org.quurz.foomp.base.functions.Comparer.Relation.EQUAL;
import static org.quurz.foomp.base.functions.Comparer.comparer;
import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.util.Maybe.maybeOfNullable;

/**
 * <div>
 *     <p>
 *         An implementation of a self-balancing binary search tree (AVL tree).
 *     </p>
 * </div>
 *
 * @param <A> the type of elements maintained by this tree
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public abstract sealed class AVLTree<A>
        implements BinaryTree<A>
        permits AVLTree.Node,
                AVLTree.Leaf,
                AVLTree.Empty {

    /**
     * <div>
     *     <p>
     *         Creates an empty AVL tree using the natural ordering of its elements.
     *     </p>
     * </div>
     *
     * @param <A> the element type, must be {@link Comparable}
     * @return an empty AVL tree
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> AVLTree<A> avlTree() {
        return new Empty<>(comparer((Comparator<? super A>) Comparator.naturalOrder()));
    }

    /**
     * <div>
     *     <p>
     *         Creates an empty AVL tree using the specified comparator.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param comparator the comparator to determine the order of elements; must not be {@code null}
     * @return an empty AVL tree
     * @throws NullPointerException if {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> AVLTree<A> avlTree(final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return new Empty<>(comparer(comparator));
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree containing the specified elements using their natural ordering.
     *     </p>
     * </div>
     *
     * @param <A>      the element type, must be {@link Comparable}
     * @param elements the elements to be included in the tree; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> AVLTree<A> avlTreeOf(final @NonNull A... elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(Comparator.naturalOrder(), Stream.of(elements));
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree containing the specified elements using the given comparator.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param comparator the comparator to determine the order; must not be {@code null}
     * @param elements   the elements to be included; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <A> AVLTree<A> avlTreeOF(final @NonNull Comparator<? super A> comparator,
                                           final @NonNull A... elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(comparator, Stream.of(elements));
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree from a collection of elements using their natural ordering.
     *     </p>
     * </div>
     *
     * @param <A>      the element type, must be {@link Comparable}
     * @param elements the collection of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A extends Comparable<A>> AVLTree<A> avlTreeFrom(final @NonNull Collection<A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(Comparator.naturalOrder(), elements.stream());
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree from a collection of elements using the specified comparator.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param comparator the comparator to determine the order; must not be {@code null}
     * @param elements   the collection of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> AVLTree<A> avlTreeFrom(final @NonNull Comparator<? super A> comparator,
                                             final @NonNull Collection<A> elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(comparator, elements.stream());
    }

    /**
     * <div>
     *     <p>
     *         Constructs an AVL tree from a stream of elements.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param comparator the comparator to determine the order
     * @param elements   the stream of elements
     * @return an AVL tree containing the elements
     *
     * @since 1.0.0
     */
    private static <A> AVLTree<A> construct(final Comparator<? super A> comparator,
                                            final Stream<A> elements) {
        return elements.reduce(
                AVLTree.avlTree(comparator),
                AVLTree::insert,
                (t1, t2) -> {
                    throw new UnsupportedOperationException("Parallel streams are not supported");
                }
        );
    }

    protected final Comparer<? super A> comparer;
    protected final int height;

    /**
     * <div>
     *     <p>
     *         Base constructor for AVL tree implementations.
     *     </p>
     * </div>
     *
     * @param comparer the comparer used for element ordering
     * @param height   the height of this tree
     *
     * @since 1.0.0
     */
    protected AVLTree(final Comparer<? super A> comparer,
                      final int height) {
        this.comparer
            = comparer;
        this.height
            = height;
    }

    /**
     * <div>
     *     <p>
     *         Returns the element stored in this node.
     *     </p>
     * </div>
     *
     * @return the element stored in this node
     * @throws NoSuchElementException if this is an empty tree
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A element() {
        return switch (this) {
            case Node<A> node -> node.element;
            case Leaf<A> leaf -> leaf.element;
            case Empty<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns the left child of this node.
     *     </p>
     * </div>
     *
     * @return the left child
     * @throws NoSuchElementException if this is not a branch node (e.g. leaf or empty)
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull AVLTree<A> left() {
        return switch (this) {
            case Node<A> node -> node.left;
            default -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *     <p>
     *         Returns the right child of this node.
     *     </p>
     * </div>
     *
     * @return the right child
     * @throws NoSuchElementException if this is not a branch node (e.g. leaf or empty)
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull AVLTree<A> right() {
        return switch (this) {
            case Node<A> node -> node.right;
            default -> throw new NoSuchElementException(noValuePresent());
        };
    }

    /**
     * <div>
     *     <p>
     *         Inserts an element into the tree and returns the resulting balanced tree.
     *         Since this implementation is immutable, a new tree (or modified copy)
     *         is returned while the original tree remains unchanged.
     *     </p>
     * </div>
     *
     * @param element the element to insert; must not be {@code null}
     * @return the new tree containing the element
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull AVLTree<A> insert(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        // return insertRecursive(element, this.comparer, this, false);
        return null;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Checks if the tree contains the specified element.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return {@code true} if the element is found, {@code false} otherwise
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public boolean contains(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return searchRecursive(element, this.comparer, this) != null;
    }

    /**
     * <div>
     *     <p>
     *         Searches for an element in the tree and returns the found instance.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return the element found in the tree
     * @throws NoSuchElementException if the element is not found
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull A search(final @NonNull A element) throws NoSuchElementException {
        Objects.requireNonNull(element, nullValue("element"));
        final var result
            = searchRecursive(element, this.comparer, this);
        if (result != null) {
            return result;
        } else {
            throw new NoSuchElementException(notFound(element));
        }
    }

    /**
     * <div>
     *     <p>
     *         Searches for an element in the tree and returns it wrapped in a {@link Maybe}.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return a {@link Maybe} containing the element if found, or an empty {@link Maybe}
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull Maybe<A> searchSafe(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        return maybeOfNullable(searchRecursive(element, this.comparer, this));
    }

    /**
     * <div>
     *     <p>
     *         Returns the children of this tree node.
     *     </p>
     * </div>
     *
     * @return a list of child trees
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull List<AVLTree<A>> children() {
        return switch (this) {
            case Node<A> node -> List.of(node.left, node.right);
            case Leaf<A> _ -> List.of();
            case Empty<A> _ -> List.of();
        };
    }

    /**
     * <div>
     *     <p>
     *         Removes an element from the tree and returns the resulting balanced tree.
     *     </p>
     * </div>
     *
     * @param element the element to remove; must not be {@code null}
     * @return the new tree with the element removed
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull AVLTree<A> remove(final @NonNull A element) {
        Objects.requireNonNull(element, nullValue("element"));
        // return removeRecursive(element, this.comparer, this);
        return null;    // TODO
    }

    /**
     * <div>
     *     <p>
     *         Returns the height of this tree.
     *     </p>
     * </div>
     *
     * @return the height of the tree (-1 for empty, 0 for leaf)
     *
     * @since 1.0.0
     */
    @Override
    public int height() {
        return this.height;
    }

    /**
     * <div>
     *     <p>
     *         The internal node class for the AVL tree.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     *
     * @since 1.0.0
     */
    public static final class Node<A>
            extends AVLTree<A> {

        private final A element;
        private final AVLTree<A> left;
        private final AVLTree<A> right;

        /**
         * <div>
         *     <p>
         *         Constructs a new internal node.
         *     </p>
         * </div>
         *
         * @param comparer the comparer to use
         * @param element  the element stored in this node
         * @param left     the left subtree
         * @param right    the right subtree
         *
         * @since 1.0.0
         */
        private Node(final Comparer<? super A> comparer,
                     final A element,
                     final AVLTree<A> left,
                     final AVLTree<A> right) {

            super(comparer, Math.max(left.height, right.height) + 1);
            this.element
                = element;
            this.left
                = left;
            this.right
                = right;
        }

    }

    /**
     * <div>
     *     <p>
     *         The internal leaf class for the AVL tree, representing a node without children.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     *
     * @since 1.0.0
     */
    public static final class Leaf<A>
            extends AVLTree<A> {

        private final A element;

        /**
         * <div>
         *     <p>
         *         Constructs a new leaf node.
         *     </p>
         * </div>
         *
         * @param comparer the comparer to use
         * @param element  the element stored in this leaf
         *
         * @since 1.0.0
         */
        private Leaf(final Comparer<? super A> comparer,
                     final A element) {
            super(comparer, 0);
            this.element
                = element;
        }

    }

    /**
     * <div>
     *     <p>
     *         The internal empty class for the AVL tree, representing an empty subtree.
     *     </p>
     * </div>
     *
     * @param <A> the element type
     *
     * @since 1.0.0
     */
    public static final class Empty<A>
            extends AVLTree<A> {

        /**
         * <div>
         *     <p>
         *         Constructs a new empty subtree.
         *     </p>
         * </div>
         *
         * @param comparator the comparer to use
         *
         * @since 1.0.0
         */
        private Empty(final Comparer<? super A> comparator) {
            super(comparator, -1);
        }

    }

    private static <A> AVLTree<A> insertRecursive(final A element,
                                                  final Comparer<? super A> comparer,
                                                  final AVLTree<A> current) {
        return switch (current) {
            case Empty<A> _
                -> new Leaf<>(comparer, element);
            case Leaf<A> leaf
                -> switch (comparer.compare(element, leaf.element)) {
                    case LESS -> null;    // TODO
                    case EQUAL -> leaf;
                    case GREATER -> null;   // TODO
                };
            case Node<A> node
                -> switch (comparer.compare(element, node.element)) {
                    case LESS -> null;    // TODO
                    case EQUAL -> node;
                    case GREATER -> null;    // TODO
                };
        };
    }

    /**
     * <div>
     *     <p>
     *         Recursively searches for an element in the tree.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param element    the element to search for
     * @param comparer the comparer to use
     * @param current    the current subtree being searched
     * @return the found element, or {@code null} if not found
     *
     * @since 1.0.0
     */
    private static <A> A searchRecursive(final A element,
                                         final Comparer<? super A> comparer,
                                         final AVLTree<A> current) {
        return switch (current) {
            case Node<A> node -> {
                final var relation
                    = comparer.compare(element, node.element);
                yield switch (relation) {
                    case LESS -> searchRecursive(element, comparer, node.left);
                    case EQUAL -> node.element;
                    case GREATER -> searchRecursive(element, comparer, node.right);
                };
            }
            case Leaf<A> leaf
                -> comparer.compare(element, leaf.element) == EQUAL
                    ? leaf.element
                    : null;
            case Empty<A> _ -> null;
        };
    }

}
