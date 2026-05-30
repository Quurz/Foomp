package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Copyable;

import java.util.*;
import java.util.stream.Stream;

import static org.quurz.foomp.base.functions.Comparer.comparer;
import static org.quurz.foomp.base.localisation.BaseMessages.*;
import static org.quurz.foomp.base.types.Tree.InsertionStrategy.Discard;
import static org.quurz.foomp.base.util.Maybe.*;
import static org.quurz.foomp.base.util.Tuple2.tuple2;

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
        implements BinaryTree<A>,
                   Copyable<AVLTree<A>>
        permits AVLTree.Node,
                AVLTree.Leaf {

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
        return new Leaf<>(Discard, comparer((Comparator<? super A>) Comparator.naturalOrder()));
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable<A>> AVLTree<A> avlTree(final @NonNull InsertionStrategy insertionStrategy) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        return new Leaf<>(insertionStrategy,comparer((Comparator<? super A>) Comparator.naturalOrder()));
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
        return new Leaf<>(Discard, comparer(comparator));
    }

    public static <A> AVLTree<A> avlTree(final @NonNull InsertionStrategy insertionStrategy,
                                         final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        return new Leaf<>(insertionStrategy, comparer(comparator));
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
        return construct(Discard, Comparator.naturalOrder(), Stream.of(elements));
    }

    public static <A extends Comparable<A>> AVLTree<A> avlTreeOf(final @NonNull InsertionStrategy insertionStrategy,
                                                                 final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(insertionStrategy, Comparator.naturalOrder(), Stream.of(elements));
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
        return construct(Discard, comparator, Stream.of(elements));
    }

    @SafeVarargs
    public static <A> AVLTree<A> avlTreeOF(final @NonNull InsertionStrategy insertionStrategy,
                                           final @NonNull Comparator<? super A> comparator,
                                           final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(insertionStrategy, comparator, Stream.of(elements));
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
        return construct(Discard, Comparator.naturalOrder(), elements.stream());
    }

    public static <A extends Comparable<A>> AVLTree<A> avlTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                                                   final @NonNull Collection<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return construct(insertionStrategy, Comparator.naturalOrder(), elements.stream());
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
        return construct(Discard, comparator, elements.stream());
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
    private static <A> AVLTree<A> construct(final InsertionStrategy insertionStrategy,
                                            final Comparator<? super A> comparator,
                                            final Stream<A> elements) {
        return elements.reduce(
            AVLTree.avlTree(insertionStrategy, comparator),
            AVLTree::insert,
            (t1, t2) -> {
                throw new UnsupportedOperationException("Parallel streams are not supported");
            }
        );
    }

    protected final InsertionStrategy insertionStrategy;
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
    protected AVLTree(final InsertionStrategy insertionStrategy,
                      final Comparer<? super A> comparer,
                      final int height) {
        this.insertionStrategy
            = insertionStrategy;
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
            case Leaf<A> _ -> throw new NoSuchElementException(noValuePresent());
        };
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
        return switch (this) {
            case Node<A> node -> some(node.element);
            case Leaf<A> _ -> none();
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
        return insertRecursive(this.insertionStrategy, this.comparer, element, this).get();
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
        return searchRecursive(this.comparer, element, this) != null;
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
            = searchRecursive(this.comparer, element, this);
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
        return maybeOfNullable(searchRecursive(this.comparer, element, this));
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

    @Override
    public String toString() {
        return switch (this) {
            case Node<A> node -> String.format("Node(%s, %s, %s)", node.left, node.element, node.right);
            case Leaf<A> _ -> "Leaf()";
        };
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
        private Node(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer,
                     final A element,
                     final AVLTree<A> left,
                     final AVLTree<A> right) {

            super(insertionStrategy, comparer, Math.max(left.height, right.height) + 1);
            this.element
                = element;
            this.left
                = left;
            this.right
                = right;
        }

        @Override
        public @NonNull AVLTree<A> copy() {
            return new Node<>(
                this.insertionStrategy,
                this.comparer,
                this.element,
                this.left.copy(),
                this.right.copy()
            );
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

        private Leaf(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer) {
            super(insertionStrategy, comparer, 0);
        }

        @Override
        public @NonNull AVLTree<A> copy() {
            return new Leaf<>(this.insertionStrategy, this.comparer);
        }

    }

    private static <A> Tuple2<AVLTree<A>, Boolean> insertRecursive(final InsertionStrategy insertionStrategy,
                                                                   final Comparer<? super A> comparer,
                                                                   final A element,
                                                                   final AVLTree<A> current) {
        return switch (current) {
            case Node<A> node
                -> switch (comparer.compare(element, node.element)) {
                    case LESS
                        -> {
                            final var childNodeAndChangeFlag
                                = insertRecursive(insertionStrategy, comparer, element, node.left);
                            final AVLTree<A> copyOnWriteNode;
                            if (childNodeAndChangeFlag.get2()) {
                                copyOnWriteNode
                                    = new Node<>(insertionStrategy, comparer, current.element(), childNodeAndChangeFlag.get(), current.right());
                            } else {
                                copyOnWriteNode
                                    = current;
                            }
                            yield tuple2(copyOnWriteNode, childNodeAndChangeFlag.get2());
                        }
                    case EQUAL
                        -> switch (insertionStrategy) {
                            case Discard
                                -> tuple2(node, Boolean.FALSE);
                            case Replace
                                -> tuple2(
                                    new Node<>(
                                        insertionStrategy,
                                        comparer,
                                        element,
                                        node.left.copy(),
                                        node.right.copy()
                                    ),
                                    Boolean.TRUE
                                );
                        };
                    case GREATER
                        -> {
                            final var childNodeAndChangeFlag
                                = insertRecursive(insertionStrategy, comparer, element, node.right);
                            final AVLTree<A> copyOnWriteNode;
                            if (childNodeAndChangeFlag.get2()) {
                                copyOnWriteNode
                                    = new Node<>(insertionStrategy, comparer, current.element(), current.left(), childNodeAndChangeFlag.get());
                            } else {
                                copyOnWriteNode
                                    = current;
                            }
                            yield tuple2(copyOnWriteNode, childNodeAndChangeFlag.get2());
                        }
                };
            case Leaf<A> _
                -> tuple2(
                    new Node<>(
                        insertionStrategy,
                        comparer,
                        element,
                        new Leaf<>(insertionStrategy, comparer),
                        new Leaf<>(insertionStrategy, comparer)
                    ),
                    Boolean.TRUE
                );
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
    private static <A> A searchRecursive(final Comparer<? super A> comparer,
                                         final A element,
                                         final AVLTree<A> current) {
        return switch (current) {
            case Node<A> node
                -> switch (comparer.compare(element, node.element)) {
                    case LESS -> searchRecursive(comparer, element, node.left);
                    case EQUAL -> node.element;
                    case GREATER -> searchRecursive(comparer, element, node.right);
                };
            case Leaf<A> _
                -> null;
        };
    }

}
