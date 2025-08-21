package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         A generic container of zero or more elements of type {@code A}.
 *         Implementations provide membership checks and element lookup operations.
 *     </p>
 *     <p>
 *         Unless stated otherwise, implementations should clearly document:
 *     </p>
 *     <ul>
 *         <li>the notion of element equality (e.g., {@code equals} vs. custom comparator),</li>
 *         <li>null‑safety (whether {@code null} elements are allowed),</li>
 *         <li>ordering semantics (if any), and mutability characteristics.</li>
 *     </ul>
 * </div>
 *
 * @param <A> the element type contained in this container
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Container<A> {

    /**
     * <div>
     *     <p>
     *         Checks whether the given element is contained in this container.
     *     </p>
     * </div>
     *
     * @param element the element to test for membership; must not be {@code null}
     * @return {@code true} if the element is contained, {@code false} otherwise
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    boolean contains(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         Searches this container for the given element and returns it if present.
     *     </p>
     *     <p>
     *         The exact matching semantics (e.g., {@code equals}-based or comparator‑based)
     *         are implementation specific and should be documented by the implementation.
     *     </p>
     * </div>
     *
     * @param element the element to search for; must not be {@code null}
     * @return the found element (never {@code null})
     * @throws NoSuchElementException if the element is not present in this container
     * @throws NullPointerException if {@code element} is {@code null}
     *
     * @since 1.0.0
     */
    A search(final @NonNull A element)
            throws NoSuchElementException;

}
