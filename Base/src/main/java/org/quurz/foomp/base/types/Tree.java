package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A generic, hierarchical tree structure holding elements of type {@code A}.
 *         The tree is designed to be immutable and provides core operations for
 *         insertion, deletion, and search.
 *     </p>
 *     <p>
 *         As a {@link Visitable} type, it supports externalised operations via the
 *         Visitor pattern, allowing for flexible traversal and processing of tree nodes.
 *     </p>
 * </div>
 *
 * @param <A> the type of elements held in the tree
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Tree<A>
        extends Visitable<Tree<A>> {

    enum InsertionStrategy {
        Discard,
        Replace
    }

    /**
     * <div>
     *     <p>
     *         Returns the height of this tree node.
     *     </p>
     *     <p>
     *         The height is defined as the number of nodes along the longest path from this
     *         node down to the farthest leaf. A terminal/empty node typically has a height of 0,
     *         while a single leaf node has a height of 1.
     *     </p>
     * </div>
     *
     * @return the height of this tree
     *
     * @since 1.0.0
     */
    int height();

    /**
     * <div>
     *     <p>
     *         Inserts the specified {@code element} into the tree.
     *     </p>
     *     <p>
     *         Contract: {@code element} must not be {@code null}. The method returns a new tree
     *         instance containing the element, preserving the original tree's immutability.
     *     </p>
     * </div>
     *
     * @param element the element to insert; must not be {@code null}
     * @return a new tree instance containing the element
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Tree<A> insert(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Checks if the specified {@code element} is present in the tree.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return {@code true} if the element is present, {@code false} otherwise
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    boolean contains(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code element} in the tree.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return the found element
     * @throws NoSuchElementException if the element is not found
     * @throws NullPointerException   if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull A search(final @NonNull A element)
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Searches for the specified {@code element} in the tree and returns it wrapped in a {@link Value}.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return a {@code Value} containing the element if found, or an empty {@code Value} otherwise
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Value<A> searchSafe(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Returns the direct child nodes of the current tree node.
     *     </p>
     *     <p>
     *         The returned list contains the immediate subtrees. For terminal nodes
     *         (leaves or empty ends), this list is empty.
     *     </p>
     * </div>
     *
     * @return a list of child nodes; may be empty but not {@code null}
     *
     * @since 1.0.0
     */
    @NonNull List<? extends Tree<A>> children();

    /**
     * <div>
     *     <p>
     *         Removes the specified {@code element} from the tree.
     *     </p>
     *     <p>
     *         Contract: {@code element} must not be {@code null}. The method returns a new tree
     *         instance without the element, preserving the original tree's immutability.
     *     </p>
     * </div>
     *
     * @param element the element to remove; must not be {@code null}
     * @return a new tree instance without the element
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    @NonNull Tree<A> remove(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Accepts the given visitor and applies it to this tree node.
     *     </p>
     *     <p>
     *         Contract: {@code visitor} must not be {@code null}. The returned visitor is the
     *         same instance passed in, allowing fluent chaining.
     *     </p>
     * </div>
     *
     * @param visitor the visitor to apply; must not be {@code null}
     * @param <V>     the concrete visitor type
     * @return the same visitor instance
     * @throws NullPointerException if {@code visitor} is {@code null}
     *
     * @since 1.0.0
     */
    @Override
    default <V extends Visitor<? super Tree<A>>> @NonNull V welcome(final @NonNull V visitor) {
        Objects.requireNonNull(visitor, nullValue("visitor"));
        visitor.visit(this);
        return visitor;
    }

}
