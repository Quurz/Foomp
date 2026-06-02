package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Comparer;
import org.quurz.foomp.base.types.BinaryTree;
import org.quurz.foomp.base.types.Copyable;
import org.quurz.foomp.base.types.Echo;
import org.quurz.foomp.base.types.Transmogrifyable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
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
                   Transmogrifyable<AVLTree<A>>,
                   Echo,
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

    /**
     * <div>
     *     <p>
     *         Creates an empty AVL tree using the natural ordering and the specified insertion strategy.
     *     </p>
     * </div>
     *
     * @param <A>               the element type, must be {@link Comparable}
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @return an empty AVL tree
     * @throws NullPointerException if {@code insertionStrategy} is {@code null}
     *
     * @since 1.0.0
     */
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

    /**
     * <div>
     *     <p>
     *         Creates an empty AVL tree using the specified insertion strategy and comparator.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param comparator        the comparator to determine the order of elements; must not be {@code null}
     * @return an empty AVL tree
     * @throws NullPointerException if {@code insertionStrategy} or {@code comparator} is {@code null}
     *
     * @since 1.0.0
     */
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
        return avlTreeFrom(Discard, Comparator.naturalOrder(), Stream.of(elements));
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree containing the specified elements using their natural ordering
     *         and the specified insertion strategy.
     *     </p>
     * </div>
     *
     * @param <A>               the element type, must be {@link Comparable}
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param elements          the elements to be included in the tree; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code insertionStrategy} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <A extends Comparable<A>> AVLTree<A> avlTreeOf(final @NonNull InsertionStrategy insertionStrategy,
                                                                 final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(insertionStrategy, Comparator.naturalOrder(), Stream.of(elements));
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
    public static <A> AVLTree<A> avlTreeOf(final @NonNull Comparator<? super A> comparator,
                                           final @NonNull A... elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(Discard, comparator, Stream.of(elements));
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree containing the specified elements using the given comparator
     *         and insertion strategy.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param comparator        the comparator to determine the order; must not be {@code null}
     * @param elements          the elements to be included; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code insertionStrategy}, {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <A> AVLTree<A> avlTreeOf(final @NonNull InsertionStrategy insertionStrategy,
                                           final @NonNull Comparator<? super A> comparator,
                                           final @NonNull A... elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(insertionStrategy, comparator, Stream.of(elements));
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
        return avlTreeFrom(Discard, Comparator.naturalOrder(), elements.stream());
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree from a collection of elements using their natural ordering
     *         and the specified insertion strategy.
     *     </p>
     * </div>
     *
     * @param <A>               the element type, must be {@link Comparable}
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param elements          the collection of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code insertionStrategy} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A extends Comparable<A>> AVLTree<A> avlTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                                                   final @NonNull Collection<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(insertionStrategy, Comparator.naturalOrder(), elements.stream());
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
        return avlTreeFrom(Discard, comparator, elements.stream());
    }

    /**
     * <div>
     *     <p>
     *         Creates an AVL tree from a collection of elements using the specified insertion strategy
     *         and comparator.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param comparator        the comparator to determine the order; must not be {@code null}
     * @param elements          the collection of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code insertionStrategy}, {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> AVLTree<A> avlTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                             final @NonNull Comparator<? super A> comparator,
                                             final @NonNull Collection<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(insertionStrategy, comparator, elements.stream());
    }

    /**
     * <div>
     *     <p>
     *         Constructs an AVL tree from a stream of elements using the natural ordering.
     *     </p>
     * </div>
     *
     * @param <A>      the element type, must be {@link Comparable}
     * @param elements the stream of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A extends Comparable<A>> AVLTree<A> avlTreeFrom(final @NonNull Stream<A> elements) {
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(Discard, Comparator.naturalOrder(), elements);
    }

    /**
     * <div>
     *     <p>
     *         Constructs an AVL tree from a stream of elements using the specified comparator.
     *     </p>
     * </div>
     *
     * @param <A>        the element type
     * @param comparator the comparator to determine the order; must not be {@code null}
     * @param elements   the stream of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> AVLTree<A> avlTreeFrom(final @NonNull Comparator<? super A> comparator,
                                             final @NonNull Stream<A> elements) {
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return avlTreeFrom(Discard, comparator, elements);
    }

    /**
     * <div>
     *     <p>
     *         Constructs an AVL tree from a stream of elements using the specified insertion strategy and comparator.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use when inserting duplicate elements; must not be {@code null}
     * @param comparator        the comparator to determine the order; must not be {@code null}
     * @param elements          the stream of elements; must not be {@code null}
     * @return an AVL tree containing the elements
     * @throws NullPointerException if {@code insertionStrategy}, {@code comparator} or {@code elements} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> AVLTree<A> avlTreeFrom(final @NonNull InsertionStrategy insertionStrategy,
                                             final @NonNull Comparator<? super A> comparator,
                                             final @NonNull Stream<A> elements) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));
        Objects.requireNonNull(elements, nullValue("elements"));
        return elements.reduce(
            AVLTree.avlTree(insertionStrategy, comparator),
            AVLTree::insert,
            (t1, t2) -> {
                throw new UnsupportedOperationException("Parallel streams are not supported");
            }
        );
    }

    /**
     * <div>
     *     <p>
     *         Returns a collector that accumulates elements into an AVL tree.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use for duplicates; must not be {@code null}
     * @param comparator        the comparator to determine the order; must not be {@code null}
     * @return a collector that creates an AVL tree
     * @throws NullPointerException if any argument is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> Collector<A, Set<A>, AVLTree<A>> collectToAVLTree(final @NonNull InsertionStrategy insertionStrategy,
                                                                        final @NonNull Comparator<? super A> comparator) {
        Objects.requireNonNull(insertionStrategy, nullValue("insertionStrategy"));
        Objects.requireNonNull(comparator, nullValue("comparator"));

        return Collector.of(
            HashSet::new,
            Set::add,
            (left, right) -> { left.addAll(right); return left; },
            set -> avlTreeFrom(insertionStrategy, comparator, (Collection<A>) set)
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
     * @param insertionStrategy the strategy to use when inserting duplicate elements
     * @param comparer          the comparer used for element ordering
     * @param height            the height of this tree
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
     *         Checks if this tree instance represents a node containing an element and children.
     *     </p>
     * </div>
     *
     * @return {@code true} if this is a node, {@code false} if it is a terminal leaf
     *
     * @since 1.0.0
     */
    @Override
    public boolean isNode() {
        return (this instanceof Node<A>);
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
     * @since 1.0.0
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
        return insertRecursive(insertionStrategy, comparer, element, this).get1();
    }

    /**
     * <div>
     *     <p>
     *         Checks if the tree contains the specified element.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return {@code true} if the element is present, {@code false} otherwise
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
        return removeRecursive(this.comparer, element, this);
    }

    /**
     * <div>
     *     <p>
     *         Merges this AVL tree with another binary tree.
     *     </p>
     * </div>
     *
     * @param other the other binary tree to merge with; must not be {@code null}
     * @return a new AVL tree representing the result of the merge
     * @throws NullPointerException if {@code other} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull AVLTree<A> merge(final @NonNull BinaryTree<A> other) {
        Objects.requireNonNull(other, nullValue("other"));

        AVLTree<A> merged
            = this;
        final Deque<BinaryTree<A>> stack
            = new ArrayDeque<>();
        stack.push(other);

        while (!stack.isEmpty()) {
            final BinaryTree<A> current = stack.pop();

            if (current.isNode()) {
                merged
                    = merged.insert(current.element());
                stack.push(current.right());
                stack.push(current.left());
            }
        }

        return merged;
    }

    /**
     * <div>
     *     <p>
     *         Transforms this AVL tree into another representation using the provided transmogrifier function.
     *     </p>
     * </div>
     *
     * @param transmogrifier the function to apply to this tree; must not be {@code null}
     * @param <T>            the target type of the transformation
     * @return the result of the transformation; never {@code null}
     * @throws NullPointerException if {@code transmogrifier} is {@code null} or returns {@code null}
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull <T> T transmogrify(final @NonNull Function<? super AVLTree<A>, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         Returns a structured string representation of this tree.
     *     </p>
     * </div>
     *
     * @return a structured string representation
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull String echo() {
        return echoRecursive("", "", this).trim();
    }

    /**
     * <div>
     *     <p>
     *         Compares the specified object with this tree for equality.
     *     </p>
     *     <p>
     *         Two AVL trees are considered equal if they contain the same elements in the same
     *         order (in-order traversal). The internal structure (balancing) of the trees
     *         may differ.
     *     </p>
     * </div>
     *
     * @param other the object to be compared for equality with this tree
     * @return {@code true} if the specified object is equal to this tree, {@code false} otherwise
     *
     * @since 1.0.0
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof AVLTree<?> that)) {
            return false;
        }

        return equalsRecursive(this, that);
    }

    /**
     * <div>
     *     <p>
     *         Recursively compares two AVL trees for equality.
     *     </p>
     * </div>
     *
     * @param tree1 the first tree
     * @param tree2 the second tree
     * @return {@code true} if the trees are equal, {@code false} otherwise
     *
     * @since 1.0.0
     */
    private static boolean equalsRecursive(final AVLTree<?> tree1, final AVLTree<?> tree2) {
        if (!tree1.isNode() && !tree2.isNode()) {
            return true;
        }
        if (tree1.isNode() && tree2.isNode()) {
            return Objects.equals(tree1.element(), tree2.element())
                && equalsRecursive(tree1.left(), tree2.left())
                && equalsRecursive(tree1.right(), tree2.right());
        }
        return false;
    }

    /**
     * <div>
     *     <p>
     *         Returns the hash code value for this tree.
     *     </p>
     *     <p>
     *         The hash code is calculated based on the elements of the tree in their
     *         in-order sequence.
     *     </p>
     * </div>
     *
     * @return the hash code value for this tree
     *
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return hashCodeRecursive(this);
    }

    /**
     * <div>
     *     <p>
     *         Recursively calculates the hash code for the given tree node.
     *     </p>
     * </div>
     *
     * @param current the node to calculate the hash code for
     * @return the calculated hash code
     *
     * @since 1.0.0
     */
    private int hashCodeRecursive(final AVLTree<A> current) {
        if (!current.isNode()) {
            return 1;
        }
        int h = hashCodeRecursive(current.left());
        h = 31 * h + Objects.hashCode(current.element());
        h = 31 * h + hashCodeRecursive(current.right());
        return h;
    }

    /**
     * <div>
     *     <p>
     *         Recursively generates a structured string representation of the tree.
     *     </p>
     * </div>
     *
     * @param <A>            the element type
     * @param prefix         the prefix for the current line
     * @param childrenPrefix the prefix for children lines
     * @param current        the current node being processed
     * @return the formatted string for the current subtree
     *
     * @since 1.0.0
     */
    private static <A> String echoRecursive(final String prefix,
                                            final String childrenPrefix,
                                            final AVLTree<A> current) {
        return switch (current) {
            case Node<A> node -> {
                final var stringBuilder
                    = new StringBuilder();
                stringBuilder.append(prefix).append(node.element).append("\n");

                if (node.left instanceof Node<A> || node.right instanceof Node<A>) {
                    stringBuilder.append(echoRecursive(childrenPrefix + "├── ", childrenPrefix + "│   ", node.left));
                    stringBuilder.append(echoRecursive(childrenPrefix + "└── ", childrenPrefix + "    ", node.right));
                }

                yield stringBuilder.toString();
            }
            case Leaf<A> _ -> prefix.contains("──") ? prefix + "[empty]\n" : "";
        };
    }
    
    /**
     * <div>
     *     <p>
     *         Returns a string representation of this tree.
     *     </p>
     * </div>
     *
     * @return a string representation
     *
     * @since 1.0.0
     */
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
         * @param insertionStrategy the strategy to use when inserting duplicate elements
         * @param comparer          the comparer to use
         * @param element           the element stored in this node
         * @param left              the left subtree
         * @param right             the right subtree
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

        /**
         * <div>
         *     <p>
         *         Returns a deep copy of this node and its subtrees.
         *     </p>
         * </div>
         *
         * @return a copy of this tree
         *
         * @since 1.0.0
         */
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

        /**
         * <div>
         *     <p>
         *         Constructs a new leaf node.
         *     </p>
         * </div>
         *
         * @param insertionStrategy the strategy to use when inserting duplicate elements
         * @param comparer          the comparer to use
         *
         * @since 1.0.0
         */
        private Leaf(final InsertionStrategy insertionStrategy,
                     final Comparer<? super A> comparer) {
            super(insertionStrategy, comparer, 0);
        }

        /**
         * <div>
         *     <p>
         *         Returns a copy of this leaf.
         *     </p>
         * </div>
         *
         * @return a new leaf instance
         *
         * @since 1.0.0
         */
        @Override
        public @NonNull AVLTree<A> copy() {
            return new Leaf<>(this.insertionStrategy, this.comparer);
        }

    }

    /**
     * <div>
     *     <p>
     *         Recursively inserts an element into the tree, maintaining AVL balance.
     *     </p>
     * </div>
     *
     * @param <A>               the element type
     * @param insertionStrategy the strategy to use for duplicate elements; must not be {@code null}
     * @param comparer          the comparer to use; must not be {@code null}
     * @param element           the element to insert; must not be {@code null}
     * @param current           the current node in the recursion; must not be {@code null}
     * @return a tuple containing the new root of the subtree and a boolean indicating if the height changed
     *
     * @since 1.0.0
     */
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
                            final AVLTree<A> newNode;
                            if (childNodeAndChangeFlag.get2()) {
                                newNode
                                    = rebalance(
                                        new Node<>(
                                            insertionStrategy,
                                            comparer,
                                            current.element(),
                                            childNodeAndChangeFlag.get(),
                                            current.right()
                                        )
                                    );
                            } else {
                                newNode
                                    = current;
                            }
                            yield tuple2(newNode, childNodeAndChangeFlag.get2());
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
                            final AVLTree<A> newNode;
                            if (childNodeAndChangeFlag.get2()) {
                                newNode
                                    = rebalance(
                                        new Node<>(
                                            insertionStrategy,
                                            comparer,
                                            current.element(),
                                            current.left(),
                                            childNodeAndChangeFlag.get()
                                        )
                                    );
                            } else {
                                newNode
                                    = current;
                            }
                            yield tuple2(newNode, childNodeAndChangeFlag.get2());
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
     *         Calculates the balance factor of the given tree.
     *     </p>
     *     <p>
     *         The balance factor is defined as the height of the right subtree minus the height
     *         of the left subtree.
     *     </p>
     * </div>
     *
     * @param tree the tree to calculate the balance factor for
     * @return the balance factor (right height - left height)
     *
     * @since 1.0.0
     */
    private static int balanceFactor(final AVLTree<?> tree) {
        return switch (tree) {
            case Node<?> node -> node.right.height - node.left.height;
            case Leaf<?> _ -> 0;
        };
    }

    /**
     * <div>
     *     <p>
     *         Rebalances the given node if its balance factor exceeds 1 or is less than -1.
     *     </p>
     * </div>
     *
     * @param <A>  the element type
     * @param node the node to rebalance
     * @return the rebalanced node (might be the same or a new root after rotation)
     *
     * @since 1.0.0
     */
    private static <A> Node<A> rebalance(final Node<A> node) {
        final var balanceFactor
            = balanceFactor(node);

        if (balanceFactor > 1) {
            // Der Baum ist rechtslastig
            if (balanceFactor(node.right) < 0) {
                // Fall RL (Right-Left): Rechtes Kind ist linkslastig
                final var rightRotatedChild
                    = rotateRight((Node<A>) node.right);
                return rotateLeft(new Node<>(
                    node.insertionStrategy,
                    node.comparer,
                    node.element,
                    node.left,
                    rightRotatedChild
                ));
            }
            // Fall RR (Right-Right)
            return rotateLeft(node);
        } else if (balanceFactor < -1) {
            // Der Baum ist linkslastig
            if (balanceFactor(node.left) > 0) {
                // Fall LR (Left-Right): Linkes Kind ist rechtslastig
                final var leftRotatedChild
                    = rotateLeft((Node<A>) node.left);
                return rotateRight(new Node<>(
                    node.insertionStrategy,
                    node.comparer,
                    node.element,
                    leftRotatedChild,
                    node.right
                ));
            }
            // Fall LL (Left-Left)
            return rotateRight(node);
        }

        // Der Baum ist ausgeglichen
        return node;
    }

    /**
     * <div>
     *     <p>
     *         Performs a left rotation around the given node.
     *     </p>
     * </div>
     *
     * @param <A>  the element type
     * @param node the node to rotate around
     * @return the new root after rotation
     *
     * @since 1.0.0
     */
    private static <A> Node<A> rotateLeft(final Node<A> node) {
        final var pivot
            = (Node<A>) node.right;
        return new Node<>(
            node.insertionStrategy,
            node.comparer,
            pivot.element,
            new Node<>(
                node.insertionStrategy,
                node.comparer,
                node.element,
                node.left,
                pivot.left
            ),
            pivot.right
        );
    }

    /**
     * <div>
     *     <p>
     *         Performs a right rotation around the given node.
     *     </p>
     * </div>
     *
     * @param <A>  the element type
     * @param node the node to rotate around
     * @return the new root after rotation
     *
     * @since 1.0.0
     */
    private static <A> Node<A> rotateRight(final Node<A> node) {
        final var pivot
            = (Node<A>) node.left;
        return new Node<>(
            node.insertionStrategy,
            node.comparer,
            pivot.element,
            pivot.left,
            new Node<>(
                node.insertionStrategy,
                node.comparer,
                node.element,
                pivot.right,
                node.right
            )
        );
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

    /**
     * <div>
     *     <p>
     *         Recursively removes an element from the tree and rebalances the affected subtrees.
     *     </p>
     * </div>
     *
     * @param <A>      the element type
     * @param comparer the comparer to use for navigation
     * @param element  the element to remove
     * @param current  the current node in the recursion
     * @return the new root of the subtree after removal and rebalancing
     *
     * @since 1.0.0
     */
    private static <A> AVLTree<A> removeRecursive(final Comparer<? super A> comparer,
                                                  final A element,
                                                  final AVLTree<A> current) {
        return switch (current) {
            case Leaf<A> _
                -> current;
            case Node<A> node
                -> switch (comparer.compare(element, node.element)) {
                    case LESS -> {
                        final var newLeft = removeRecursive(comparer, element, node.left);
                        yield newLeft == node.left ? node : rebalance(new Node<>(node.insertionStrategy, node.comparer, node.element, newLeft, node.right));
                    }
                    case GREATER -> {
                        final var newRight = removeRecursive(comparer, element, node.right);
                        yield newRight == node.right ? node : rebalance(new Node<>(node.insertionStrategy, node.comparer, node.element, node.left, newRight));
                    }
                    case EQUAL -> {
                        if (node.left instanceof Leaf<A>) {
                            yield node.right;
                        } else if (node.right instanceof Leaf<A>) {
                            yield node.left;
                        } else {
                            final var successor = findMin(node.right);
                            yield rebalance(new Node<>(
                                node.insertionStrategy,
                                node.comparer,
                                successor,
                                node.left,
                                removeRecursive(comparer, successor, node.right)
                            ));
                        }
                    }
                };
        };
    }

    /**
     * <div>
     *     <p>
     *         Finds the minimum element in the given tree (the leftmost node).
     *     </p>
     * </div>
     *
     * @param <A>  the element type
     * @param tree the tree to search in
     * @return the minimum element found
     * @throws NoSuchElementException if the tree is empty
     *
     * @since 1.0.0
     */
    private static <A> A findMin(final AVLTree<A> tree) {
        return switch (tree) {
            case Node<A> node -> node.left instanceof Leaf<A> ? node.element : findMin(node.left);
            case Leaf<A> _ -> throw new NoSuchElementException();
        };
    }

}
