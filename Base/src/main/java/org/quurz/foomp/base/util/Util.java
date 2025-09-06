package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;

import java.lang.reflect.Modifier;
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

    private Util() {}

    /**
     * <div>
     *     <p>
     *         Ensures that the given collection is not empty.
     *     </p>
     *     <p>
     *         Returns the same collection instance if it is not empty; otherwise throws an
     *         {@link IllegalArgumentException} with the provided message.
     *     </p>
     * </div>
     *
     * @param collection the collection to check; must not be {@code null}
     * @param message    the error message if the collection is empty; must not be {@code null}
     * @param <C>        the concrete collection type
     * @param <A>        the element type
     * @return the same collection instance (for fluent usage)
     * @throws NullPointerException     if {@code collection} or {@code message} is {@code null}
     * @throws IllegalArgumentException if {@code collection} is empty
     *
     * @since 1.0.0
     */
    public static <C extends Collection<A>, A> C requiresNonEmpty(final @NonNull C collection,
                                                                  final @NonNull String message) {
        Objects.requireNonNull(collection, nullValue("collection"));
        Objects.requireNonNull(message, nullValue("message"));

        if (collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return collection;
    }

    /**
     * <div>
     *     <p>
     *         Ensures that the given collection is not empty.
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
    public static <C extends Collection<A>, A, E extends Exception> C requiresNonEmpty(final @NonNull C collection,
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
     *         Ensures that the given map is not empty.
     *     </p>
     *     <p>
     *         Returns the same map instance if it is not empty; otherwise throws an
     *         {@link IllegalArgumentException} with the provided message.
     *     </p>
     * </div>
     *
     * @param map      the map to check; must not be {@code null}
     * @param message  the error message if the map is empty; must not be {@code null}
     * @param <M>      the concrete map type
     * @param <K>      key type
     * @param <V>      value type
     * @return the same map instance (for fluent usage)
     * @throws NullPointerException     if {@code map} or {@code message} is {@code null}
     * @throws IllegalArgumentException if {@code map} is empty
     *
     * @since 1.0.0
     */
    public static <M extends Map<K, V>, K, V> M requiresNonEmpty(final @NonNull M map,
                                                                 final @NonNull String message) {
        Objects.requireNonNull(map, nullValue("map"));
        Objects.requireNonNull(message, nullValue("message"));

        if (map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return map;
    }

    /**
     * <div>
     *     <p>
     *         Ensures that the given map is not empty.
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
    public static <M extends Map<K, V>, K, V, E extends Exception> M requiresNonEmpty(final @NonNull M map,
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
     *         Ensures that all elements in the given {@link Collection} are non-{@code null}.
     *     </p>
     *     <p>
     *         If a {@code null} element is encountered, an exception created by
     *         {@code exceptionConstructor} is thrown. For {@link List} instances the message
     *         includes the index of the offending element; for other collections the container
     *         name is included without an index.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(message)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param collection           the collection to check; must not be {@code null}
     * @param collectionName       a descriptive name used in error messages; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <A>                  the element type
     * @param <C>                  the concrete collection type
     * @param <E>                  the exception type to be thrown
     * @return the same collection instance (for fluent usage)
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if a {@code null} element is found
     *
     * @since 1.0.0
     */
    public static <A, C extends Collection<A>, E extends Exception> C requiresNonNullElements(final @NonNull C collection,
                                                                                              final @NonNull String collectionName,
                                                                                              final @NonNull Fun<String, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(collection, nullValue("collection"));
        Objects.requireNonNull(collectionName, nullValue("collectionName"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        int index
            = 0;
        for (final var element : collection) {
            if (element == null) {
                if (collection instanceof List<?>) {
                    final var message
                        = nullElementInAt(collectionName, index);
                    throw Objects.requireNonNull(exceptionConstructor.apply(message), nullResultFrom("exceptionConstructor"));
                } else {
                    final var message
                        = nullElementIn(collectionName);
                    throw Objects.requireNonNull(exceptionConstructor.apply(message), nullResultFrom("exceptionConstructor"));
                }
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
     *         The returned {@code Fun} throws {@link NullPointerException} with a meaningful message
     *         if the wrapped function returns {@code null}.
     *     </p>
     * </div>
     *
     * @param function     function to wrap; must not be {@code null}
     * @param functionName a descriptive name used in error messages; must not be {@code null}
     * @param <X>          input type
     * @param <Y>          result type
     * @return a {@code Fun} that rejects {@code null} results
     * @throws NullPointerException if {@code function} or {@code functionName} is {@code null}
     *
     * @since 1.0.0
     */
    public static <X, Y> Fun<X, Y> requiresNonNullResult1(final @NonNull Function<X, Y> function,
                                                          final @NonNull String functionName) {
        Objects.requireNonNull(function, nullValue("function"));
        Objects.requireNonNull(functionName, nullValue("functionName"));
        return x -> Objects.requireNonNull(function.apply(x), nullResultFrom(functionName));
    }

    /**
     * <div>
     *     <p>
     *         Wraps a binary function and enforces a non-{@code null} result.
     *     </p>
     *     <p>
     *         The returned {@code Fun2} throws {@link NullPointerException} with a meaningful message
     *         if the wrapped function returns {@code null}.
     *     </p>
     * </div>
     *
     * @param function     function to wrap; must not be {@code null}
     * @param functionName a descriptive name used in error messages; must not be {@code null}
     * @param <X1>         first argument type
     * @param <X2>         second argument type
     * @param <Y>          result type
     * @return a {@code Fun2} that rejects {@code null} results
     * @throws NullPointerException if {@code function} or {@code functionName} is {@code null}
     *
     * @since 1.0.0
     */

    public static <X1, X2, Y> Fun2<X1, X2, Y> requiresNonNullResult2(final BiFunction<X1, X2, Y> function,
                                                                     final String functionName) {
        Objects.requireNonNull(function, nullValue("function"));
        Objects.requireNonNull(functionName, nullValue("functionName"));
        return (x1, x2) -> Objects.requireNonNull(function.apply(x1, x2), nullResultFrom(functionName));
    }

    /**
     * <div>
     *     <p>
     *         Ensures that all elements in the given array are non-{@code null}.
     *     </p>
     *     <p>
     *         If a {@code null} element is encountered, an exception created by
     *         {@code exceptionConstructor} is thrown. The error message includes the array name
     *         and the index of the offending element.
     *     </p>
     *     <p>
     *         Contract: {@code exceptionConstructor.apply(message)} must not return {@code null}.
     *     </p>
     * </div>
     *
     * @param array                the array to check; must not be {@code null}
     * @param arrayName            a descriptive name used in error messages; must not be {@code null}
     * @param exceptionConstructor builds the exception to throw; must not be {@code null}
     * @param <A>                  element type
     * @param <E>                  the exception type to be thrown
     * @return the same array instance (for fluent usage)
     * @throws NullPointerException if any argument is {@code null} or the constructor returns {@code null}
     * @throws E                    if a {@code null} element is found
     *
     * @since 1.0.0
     */
    @SuppressWarnings("ConstantConditions")
    public static <A, E extends Exception> A[] requiresNonNullElements(final @NonNull A[] array,
                                                                       final @NonNull String arrayName,
                                                                       final @NonNull Fun<String, E> exceptionConstructor)
            throws E {
        Objects.requireNonNull(array, nullValue("array"));
        Objects.requireNonNull(arrayName, nullValue("arrayName"));
        Objects.requireNonNull(exceptionConstructor, nullValue("exceptionConstructor"));

        for (int index = 0; index < array.length; index++) {
            if (array[index] == null) {
                final var message
                    = nullElementInAt(arrayName, index);
                throw Objects.requireNonNull(exceptionConstructor.apply(message), nullResultFrom("exceptionConstructor"));
            }
        }
        return array;
    }

    /**
     * <div>
     *     <p>
     *         Checks whether {@code subSet} is a proper subset of {@code superSet}.
     *     </p>
     *     <p>
     *         A proper subset means every element of {@code subSet} is contained in {@code superSet},
     *         and {@code subSet} is strictly smaller than {@code superSet}.
     *     </p>
     * </div>
     *
     * @param superSet the superset; must not be {@code null}
     * @param subSet   the subset candidate; must not be {@code null}
     * @param <A>      element type
     * @return {@code true} if {@code subSet} is a proper subset of {@code superSet}; otherwise {@code false}
     * @throws NullPointerException if {@code superSet} or {@code subSet} is {@code null}
     *
     * @since 1.0.0
     */
    public static <A> boolean isSubSet(final @NonNull Set<A> superSet,
                                       final @NonNull Set<A> subSet) {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));

        return (subSet.size() < superSet.size()) && superSet.containsAll(subSet);
    }

    /**
     * <div>
     *     <p>
     *         Ensures that {@code subSet} is a proper subset of {@code superSet}.
     *     </p>
     *     <p>
     *         Returns the same {@code subSet} instance if the condition holds; otherwise throws
     *         an {@link IllegalArgumentException} with the provided message.
     *     </p>
     * </div>
     *
     * @param superSet the superset; must not be {@code null}
     * @param subSet   the subset candidate; must not be {@code null}
     * @param message  the error message if {@code subSet} is not a proper subset; must not be {@code null}
     * @param <S>      the concrete set type
     * @param <A>      element type
     * @return the same {@code subSet} instance (for fluent usage)
     * @throws NullPointerException     if any argument is {@code null}
     * @throws IllegalArgumentException if {@code subSet} is not a proper subset of {@code superSet}
     *
     * @since 1.0.0
     */
    public static <S extends Set<A>, A> S mustBeSubSet(final @NonNull S superSet,
                                                       final @NonNull S subSet,
                                                       final @NonNull String message) {
        Objects.requireNonNull(superSet, nullValue("superSet"));
        Objects.requireNonNull(subSet, nullValue("subSet"));
        Objects.requireNonNull(message, nullValue("message"));

        if (isSubSet(superSet, subSet)) {
            return subSet;
        } else {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * <div>
     *     <p>
     *         Ensures that the given class represents an interface.
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
    public static <A, E extends RuntimeException> Class<A> mustBeInterface(final @NonNull Class<A> clazz,
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
     *         Ensures that the given class represents a concrete (instantiable) class.
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
    public static <A, E extends RuntimeException> Class<A> mustBeConcrete(final @NonNull Class<A> clazz,
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

}
