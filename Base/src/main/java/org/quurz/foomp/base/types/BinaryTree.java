package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.util.Maybe;

/**
 * <div>
 *     <p>
 *         A specialised {@link Tree} representing a binary structure where each node
 *         has at most two children: {@link #left()} and {@link #right()}.
 *     </p>
 *     <p>
 *         Unlike the general {@code Tree} interface, a {@code BinaryTree} node typically
 *         carries a single {@link #element()}.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements held in the tree
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface BinaryTree<A>
        extends Tree<A> {

    /**
     * <div>
     *     <p>
     *         Returns the element held by this node.
     *     </p>
     * </div>
     *
     * @return the element held by this node
     * @throws java.util.NoSuchElementException if the node is a terminal node (e.g., an empty end)
     *
     * @since 1.0.0
     */
    @NonNull A element();

    // TODO: JavaDoc
    @NonNull Value<A> elementSafe();

    /**
     * <div>
     *     <p>
     *         Returns the left child of this tree node.
     *     </p>
     * </div>
     *
     * @return the left child
     * @throws java.util.NoSuchElementException if the node is a terminal node (e.g., an empty end)
     *
     * @since 1.0.0
     */
    @NonNull BinaryTree<A> left();

    /**
     * <div>
     *     <p>
     *         Returns the right child of this tree node.
     *     </p>
     * </div>
     *
     * @return the right child
     * @throws java.util.NoSuchElementException if the node is a terminal node (e.g., an empty end)
     *
     * @since 1.0.0
     */
    @NonNull BinaryTree<A> right();

}
