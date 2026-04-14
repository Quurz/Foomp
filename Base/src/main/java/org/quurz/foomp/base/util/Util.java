package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullElementIn;
import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         A small collection of general-purpose helper methods focused on input validation
 *         and defensive programming in a functional style.
 *     </p>
 *     <p>
 *         This class only contains static methods and cannot be instantiated.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class Util {
    
    /**
     * <div>
     *     <p>
     *         Requires that the given {@code collection} is not empty.
     *     </p>
     *     <p>
     *         Returns the same collection instance if it is not empty; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param collection         the collection to check; must not be {@code null}
     * @param exceptionSupplier  supplies the exception to throw when the collection is empty; must not be {@code null}
     * @param <C>                the concrete collection type
     * @param <A>                the element type
     * @param <E>                the exception type to be thrown
     * @return the same collection instance (for fluent usage)
     * @throws NullPointerException if {@code collection} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code collection} is empty
     *
     * @since 1.0.0
     */
    public static <C extends Collection<A>, A, E extends Exception> C requireNonEmpty(final @NonNull C collection,
                                                                                      final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(collection, nullValue("collection"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (collection.isEmpty()) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }

        return collection;
    }
    
    /**
     * <div>
     *     <p>
     *         Requires that the given {@code map} is not empty.
     *     </p>
     *     <p>
     *         Returns the same map instance if it is not empty; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param map                the map to check; must not be {@code null}
     * @param exceptionSupplier  supplies the exception to throw when the map is empty; must not be {@code null}
     * @param <M>                the concrete map type
     * @param <K>                key type
     * @param <V>                value type
     * @param <E>                the exception type to be thrown
     * @return the same map instance (for fluent usage)
     * @throws NullPointerException if {@code map} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code map} is empty
     *
     * @since 1.0.0
     */
    public static <M extends Map<K, V>, K, V, E extends Exception> M requireNonEmpty(final @NonNull M map,
                                                                                     final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(map, nullValue("map"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (map.isEmpty()) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }

        return map;
    }

    /**
     * <div>
     *     <p>
     *         Requires that all elements in the given collection are non-{@code null}.
     *     </p>
     *     <p>
     *         Returns the same collection instance if all elements are non-{@code null};
     *         otherwise throws an exception created by {@code exceptionConstructor}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(index)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param collection           the collection to check; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <A>                  element type
     * @param <C>                  concrete collection type
     * @param <E>                  the exception type to be thrown
     * @return the same collection instance (for fluent usage)
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if a {@code null} element is found
     *
     * @since 1.0.0
     */
    public static <A, C extends Collection<A>, E extends Exception> C requireNonNullElements(final @NonNull C collection,
                                                                                             final @NonNull Fun<Integer, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(collection, nullValue("collection"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        int index = 0;
        for (final var element : collection) {
            if (element == null) {
                throw Objects.requireNonNull(exceptionConstructor.apply(index), nullResultFrom("exceptionConstructor"));
            }
            index++;
        }
        return collection;
    }

    /**
     * <div>
     *     <p>
     *         Wraps a unary function and enforces a non-{@code null} result.
     *     </p>
     *     <p>
     *         The returned {@code Fun} throws a {@link NullPointerException} if its input argument is
     *         {@code null}, and an exception created by {@code exceptionConstructor} if the wrapped
     *         function returns {@code null}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(x)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param function             function to wrap; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <X>                  input type
     * @param <Y>                  result type
     * @param <E>                  exception type
     * @return a {@code Fun} that rejects {@code null} arguments and {@code null} results
     * @throws NullPointerException if {@code function} or {@code exceptionConstructor} is {@code null}
     *
     * @since 1.0.0
     */
    public static <X, Y, E extends RuntimeException> Fun<X, Y> requireNonNullResult1(final @NonNull Function<X, Y> function,
                                                                                     final @NonNull Fun<X, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(function, nullValue("function"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));
        return x -> {
            Objects.requireNonNull(x, nullValue("x"));
            final var y = function.apply(x);
            if (y == null) {
                throw Objects.requireNonNull(exceptionConstructor.apply(x), nullResultFrom("exceptionConstructor"));
            }
            return y;
        };
    }

    /**
     * <div>
     *     <p>
     *         Wraps a binary function and enforces a non-{@code null} result.
     *     </p>
     *     <p>
     *         The returned {@code Fun2} throws a {@link NullPointerException} if any of its input
     *         arguments is {@code null}, and an exception created by {@code exceptionConstructor}
     *         if the wrapped function returns {@code null}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(x1, x2)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param function             function to wrap; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <X1>                 first argument type
     * @param <X2>                 second argument type
     * @param <Y>                  result type
     * @param <E>                  exception type
     * @return a {@code Fun2} that rejects {@code null} arguments and {@code null} results
     * @throws NullPointerException if {@code function} or {@code exceptionConstructor} is {@code null}
     *
     * @since 1.0.0
     */
    public static <X1, X2, Y, E extends RuntimeException> Fun2<X1, X2, Y> requireNonNullResult2(final @NonNull BiFunction<X1, X2, Y> function,
                                                                                                final @NonNull Fun2<X1, X2, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(function, nullValue("function"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));
        return (x1, x2) -> {
            Objects.requireNonNull(x1, nullValue("x1"));
            Objects.requireNonNull(x2, nullValue("x2"));
            final var y = function.apply(x1, x2);
            if (y == null) {
                throw Objects.requireNonNull(exceptionConstructor.apply(x1, x2), nullResultFrom("exceptionConstructor"));
            }
            return y;
        };
    }

    /**
     * <div>
     *     <p>
     *         Requires that all elements in the given array are non-{@code null}.
     *     </p>
     *     <p>
     *         Returns the same array instance if all elements are non-{@code null};
     *         otherwise throws an exception created by {@code exceptionConstructor}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(index)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param array                the array to check; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <A>                  element type
     * @param <E>                  the exception type to be thrown
     * @return the same array instance (for fluent usage)
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if a {@code null} element is found
     *
     * @since 1.0.0
     */
    public static <A, E extends Exception> A[] requireNonNullElements(final @NonNull A[] array,
                                                                      final @NonNull Fun<Integer, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(array, nullValue("array"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        for (int index = 0; index < array.length; index++) {
            if (array[index] == null) {
                throw Objects.requireNonNull(exceptionConstructor.apply(index), nullResultFrom("exceptionConstructor"));
            }
        }
        return array;
    }

    /**
     * <div>
     *     <p>
     *         Requires that {@code subSet} is a subset of {@code superSet}.
     *     </p>
     *     <p>
     *         A subset means every element of {@code subSet} is contained in {@code superSet}.
     *     </p>
     * </div>
     *
     * @param superSet the superset; must not be {@code null}
     * @param subSet   the subset candidate; must not be {@code null}
     * @param <A>      element type
     * @return {@code true} if {@code subSet} is a subset of {@code superSet}; otherwise {@code false}
     * @throws NullPointerException if {@code superSet} or {@code subSet} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> boolean isSubSet(final @NonNull Set<A> superSet,
                                       final @NonNull Set<A> subSet) {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));

        return (subSet.size() <= superSet.size()) && superSet.containsAll(subSet);
    }

    /**
     * <div>
     *     <p>
     *         Requires that {@code subSet} is a subset of {@code superSet}.
     *     </p>
     *     <p>
     *         Returns the same {@code subSet} instance if the condition holds; otherwise throws
     *         an exception created by {@code exceptionConstructor}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(superSet, subSet)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param superSet             the superset; must not be {@code null}
     * @param subSet               the subset candidate; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <S>                  the concrete set type
     * @param <A>                  element type
     * @param <E>                  the exception type to be thrown
     * @return the same {@code subSet} instance (for fluent usage)
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if {@code subSet} is not a subset of {@code superSet}
     *
     * @since 1.0.0
     */
    public static <S extends Set<A>, A, E extends Exception> S requireSubSet(final @NonNull S superSet,
                                                                             final @NonNull S subSet,
                                                                             final @NonNull Fun2<S, S, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        if (isSubSet(superSet, subSet)) {
            return subSet;
        } else {
            throw Objects.requireNonNull(exceptionConstructor.apply(superSet, subSet), nullResultFrom("exceptionConstructor"));
        }
    }

    /**
     * <div>
     *     <p>
     *         Requires that {@code subSet} is a proper subset of {@code superSet}.
     *     </p>
     *     <p>
     *         A <i>proper subset</i> of a set {@code superSet} is a set {@code subSet} that
     *         is a subset of {@code superSet} and is not equal to {@code superSet}.
     *         Mathematically: {@code subSet ⊂ superSet}.
     *     </p>
     * </div>
     *
     * @param superSet the superset to check against; must not be {@code null}
     * @param subSet   the potential proper subset; must not be {@code null}
     * @param <A>      the type of elements in the sets
     * @return {@code true} if {@code subSet} is a proper subset of {@code superSet}; {@code false} otherwise
     * @throws NullPointerException if {@code superSet} or {@code subSet} is {@code null}
     *
     * @see #isSubSet(Set, Set)
     * @since 1.0.0
     */
    public static <A> boolean isProperSubSet(final @NonNull Set<A> superSet,
                                             final @NonNull Set<A> subSet) {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));

        return isSubSet(superSet, subSet) && subSet.size() < superSet.size();
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given {@code subSet} is a proper subset of {@code superSet}.
     *     </p>
     *     <p>
     *         Returns the same {@code subSet} instance if it is a proper subset; otherwise throws an
     *         exception created by {@code exceptionConstructor}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(superSet, subSet)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param superSet             the superset to check against; must not be {@code null}
     * @param subSet               the potential proper subset; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <S>                  the concrete set type
     * @param <A>                  the type of elements in the sets
     * @param <E>                  the exception type to be thrown
     * @return the same {@code subSet} instance if it is a proper subset
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if {@code subSet} is not a proper subset of {@code superSet}
     *
     * @see #isProperSubSet(Set, Set)
     * @since 1.0.0
     */
    public static <S extends Set<A>, A, E extends Exception> S requireProperSubSet(final @NonNull S superSet,
                                                                                   final @NonNull S subSet,
                                                                                   final @NonNull Fun2<S, S, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        if (isProperSubSet(superSet, subSet)) {
            return subSet;
        } else {
            throw Objects.requireNonNull(exceptionConstructor.apply(superSet, subSet), nullResultFrom("exceptionConstructor"));
        }
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given class represents an interface.
     *     </p>
     *     <p>
     *         Returns the same class object if it is an interface; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param clazz             class to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <A>               class type
     * @param <E>               exception type
     * @return the same {@code Class} instance
     * @throws NullPointerException if {@code clazz} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code clazz} is not an interface
     *
     * @since 1.0.0
     */
    public static <A, E extends RuntimeException> Class<A> requireInterface(final @NonNull Class<A> clazz,
                                                                            final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(clazz, nullValue("clazz"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (!clazz.isInterface()) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return clazz;
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given class represents a concrete (instantiable) class.
     *     </p>
     *     <p>
     *         A concrete class is neither an interface nor abstract, nor a primitive or annotation type.
     *         Returns the same class object if it is concrete; otherwise throws the exception supplied by
     *         {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param clazz             class to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <A>               class type
     * @param <E>               exception type
     * @return the same {@code Class} instance
     * @throws NullPointerException if {@code clazz} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code clazz} is an interface, primitive, annotation, or abstract
     *
     * @since 1.0.0
     */
    public static <A, E extends RuntimeException> Class<A> requireConcrete(final @NonNull Class<A> clazz,
                                                                           final @NonNull Supplier<E> exceptionSupplier)
             throws E {
        Objects.requireNonNull(clazz, nullValue("clazz"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (   clazz.isInterface()
            || clazz.isAnnotation()
            || clazz.isPrimitive()
            || Modifier.isAbstract(clazz.getModifiers())
        ) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return clazz;
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given path points to a regular file.
     *     </p>
     *     <p>
     *         Returns the same path instance if it is a regular file; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param path              path to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <E>               exception type
     * @return the same {@code Path} instance
     * @throws NullPointerException if {@code path} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code path} is not a regular file
     *
     * @since 1.0.0
     */
    public static <E extends Exception> Path requireRegularFile(final @NonNull Path path,
                                                                final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(path, nullValue("path"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (!Files.isRegularFile(path)) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return path;
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given path points to an existing directory.
     *     </p>
     *     <p>
     *         Returns the same path instance if it is a directory; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param path              path to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <E>               exception type
     * @return the same {@code Path} instance
     * @throws NullPointerException if {@code path} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code path} is not a directory
     *
     * @since 1.0.0
     */
    public static <E extends Exception> Path requireDirectory(final @NonNull Path path,
                                                              final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(path, nullValue("path"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (!Files.isDirectory(path)) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return path;
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given path is readable.
     *     </p>
     *     <p>
     *         Returns the same path instance if it is readable; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param path              path to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <E>               exception type
     * @return the same {@code Path} instance
     * @throws NullPointerException if {@code path} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code path} is not readable
     *
     * @since 1.0.0
     */
    public static <E extends Exception> Path requireReadable(final @NonNull Path path,
                                                             final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(path, nullValue("path"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (!Files.isReadable(path)) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return path;
    }

    /**
     * <div>
     *     <p>
     *         Requires that the given path is writable.
     *     </p>
     *     <p>
     *         Returns the same path instance if it is writable; otherwise throws the exception
     *         supplied by {@code exceptionSupplier}.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionSupplier.get()} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param path              path to check; must not be {@code null}
     * @param exceptionSupplier supplies the exception to throw if the check fails; must not be {@code null}
     * @param <E>               exception type
     * @return the same {@code Path} instance
     * @throws NullPointerException if {@code path} or {@code exceptionSupplier} is {@code null},
     *                              or if {@code exceptionSupplier.get()} returns {@code null}
     * @throws E                    if {@code path} is not writable
     *
     * @since 1.0.0
     */
    public static <E extends Exception> Path requireWriteable(final @NonNull Path path,
                                                              final @NonNull Supplier<E> exceptionSupplier)
            throws E {
        Objects.requireNonNull(path, nullValue("path"));
        Objects.requireNonNull(exceptionSupplier, nullValue("exceptionSupplier"));

        if (!Files.isWritable(path)) {
            throw Objects.requireNonNull(exceptionSupplier.get(), nullSuppliedFrom("exceptionSupplier"));
        }
        return path;
    }

    /**
     * <div>
     *     <p>
     *         Converts a string into a safe file name.
     *     </p>
     *     <p>
     *         Replaces all whitespaces with underscores and removes all characters
     *         that are not alphanumeric, dots, hyphens, or underscores.
     *     </p>
     * </div>
     *
     * @param name the original name; must not be {@code null}
     * @return a safe file name string
     * @throws NullPointerException if {@code name} is {@code null}
     *
     * @since 1.0.0
     */
    public static String toSafeFileName(final @NonNull String name) {
        Objects.requireNonNull(name, nullValue("name"));
        return name
            .replaceAll("\\s+", "_")
            .replaceAll("[^a-zA-Z0-9._-]", "");
    }

    private Util() {}

}
