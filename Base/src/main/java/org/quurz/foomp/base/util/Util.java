package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullElementIn;
import static org.quurz.foomp.base.localisation.BaseMessages.nullElementInAt;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Sammlung von Hilfsmethoden für generische Zwecke.
 *     </p>
 *     <p>
 *         Diese Klasse enthält nur statische Methoden und kann nicht instanziiert werden.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class Util {

    // Privater Konstruktor verhindert Instanziierung
    private Util() {}

    /**
     * <div>
     *     <p>
     *         Prüft, ob die übergebene Collection nicht leer ist.
     *     </p>
     *     <p>
     *         Gibt die Collection zurück, wenn sie nicht leer ist, andernfalls wird eine
     *         {@link IllegalArgumentException} mit der übergebenen Fehlermeldung geworfen.
     *     </p>
     * </div>
     *
     * <p>
     *
     * @param collection die zu prüfende Collection
     * @param message    die Fehlermeldung, falls die Collection leer ist
     * @param <C>        der Typ der Collection
     * @param <A>        der Typ der Elemente in der Collection
     * @return die übergebene Collection, falls sie nicht leer ist
     * @throws NullPointerException     wenn {@code collection} oder {@code message} {@code null} ist
     * @throws IllegalArgumentException wenn {@code collection} leer ist
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
     *         Prüft, ob die übergebene Collection nicht leer ist.
     *     </p>
     *     <p>
     *         Gibt die Collection zurück, wenn sie nicht leer ist, andernfalls wird die vom {@code exceptionSupplier}
     *         gelieferte Ausnahme geworfen.
     *     </p>
     * </div>
     *
     * @param collection         die zu prüfende Collection
     * @param exceptionSupplier  ein Supplier, der die zu werfende Ausnahme liefert, falls die Collection leer ist
     * @param <C>                der Typ der Collection
     * @param <A>                der Typ der enthaltenen Elemente
     * @param <E>                der Typ der Ausnahme, die geworfen wird
     * @return die übergebene Collection, falls sie nicht leer ist
     * @throws NullPointerException wenn {@code collection} oder {@code exceptionSupplier} {@code null} ist,
     *                              oder wenn der Supplier {@code null} zurückliefert
     * @throws E                   wenn {@code collection} leer ist
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
     *         Prüft, ob die übergebene Map nicht leer ist.
     *     </p>
     *     <p>
     *         Gibt die Map zurück, wenn sie nicht leer ist, andernfalls wird eine
     *         {@link IllegalArgumentException} mit der übergebenen Fehlermeldung geworfen.
     *     </p>
     * </div>
     *
     * @param map      die zu prüfende Map
     * @param message  die Fehlermeldung, falls die Map leer ist
     * @param <M>      der Typ der Map
     * @param <K>      der Typ der Schlüssel
     * @param <V>      der Typ der Werte
     * @return die übergebene Map, falls sie nicht leer ist
     * @throws NullPointerException     wenn {@code map} oder {@code message} {@code null} ist
     * @throws IllegalArgumentException wenn {@code map} leer ist
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
     *         Prüft, ob die übergebene Map nicht leer ist.
     *     </p>
     *     <p>
     *         Gibt die Map zurück, wenn sie nicht leer ist, andernfalls wird die vom {@code exceptionSupplier}
     *         gelieferte Ausnahme geworfen.
     *     </p>
     * </div>
     *
     * @param map                die zu prüfende Map
     * @param exceptionSupplier  ein Supplier, der die zu werfende Ausnahme liefert, falls die Map leer ist
     * @param <M>                der Typ der Map
     * @param <K>                der Typ der Schlüssel
     * @param <V>                der Typ der Werte
     * @param <E>                der Typ der Ausnahme, die geworfen wird
     * @return die übergebene Map, falls sie nicht leer ist
     * @throws NullPointerException wenn {@code map} oder {@code exceptionSupplier} {@code null} ist,
     *                              oder wenn der Supplier {@code null} zurückliefert
     * @throws E                   wenn {@code map} leer ist
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
     *         Prüft, ob alle Elemente in der angegebenen {@link Collection} nicht {@code null} sind.
     *     </p>
     *     <p>
     *         Wird ein {@code null}-Element gefunden, wird eine durch {@code exceptionConstructor} erzeugte Ausnahme geworfen.
     *         Der Name der Collection sowie der Index des fehlenden Elements werden zur Fehlerbeschreibung verwendet.
     *     </p>
     * </div>
     *
     * @param collection           die zu prüfende {@code Collection}
     * @param collectionName            ein beschreibender Name für die Collection (z. B. für Fehlermeldungen)
     * @param exceptionConstructor eine Funktion, die aus einer Fehlermeldung eine {@link Exception} erzeugt
     * @param <A>                  der Typ der Collection-Elemente
     * @param <E>                  der Typ der zu werfenden Ausnahme
     *
     * @return dieselbe {@code Collection}, sofern alle Elemente gültig sind
     *
     * @throws NullPointerException wenn ein Parameter {@code null} ist
     * @throws E                    wenn ein Element in der {@code Collection} {@code null} ist
     *
     * @since 1.0.0
     */
    public static <A, E extends Exception> Collection<A> requireNonNullElements(final @NonNull Collection<A> collection,
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
                if (collection instanceof List<A>) {
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
     *         Prüft, ob alle Elemente im übergebenen Array nicht {@code null} sind.
     *     </p>
     *     <p>
     *         Ist ein Element {@code null}, wird eine Ausnahme geworfen, die durch {@code exceptionConstructor}
     *         erzeugt wird. Der übergebene Name des Arrays und der fehlerhafte Index werden in der Meldung verwendet.
     *     </p>
     * </div>
     *
     * @param array                das zu prüfende Array
     * @param arrayName            ein beschreibender Name für das Array (z. B. für Fehlermeldungen)
     * @param exceptionConstructor eine Funktion, die aus einer Fehlermeldung eine {@link Exception} erzeugt
     * @param <A>                  der Typ der Array-Elemente
     * @param <E>                  der Typ der zu werfenden Ausnahme
     *
     * @return das ursprüngliche Array, wenn alle Elemente gültig sind
     *
     * @throws NullPointerException wenn ein Parameter {@code null} ist
     * @throws E                    wenn ein Element im Array {@code null} ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings("ConstantConditions")
    public static <A, E extends Exception> A[] requireNonNullElements(final @NonNull A[] array,
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
     *         Prüft, ob {@code subSet} eine echte Teilmenge von {@code superSet} ist.
     *     </p>
     *     <p>
     *         Eine echte Teilmenge bedeutet, dass {@code subSet} alle Elemente in {@code superSet} enthalten ist,
     *         aber nicht gleich {@code superSet}.
     *     </p>
     * </div>
     *
     * @param superSet die Obermenge
     * @param subSet   die zu prüfende Teilmenge
     * @param <A>      der Typ der enthaltenen Elemente
     * @return {@code true}, wenn {@code subSet} eine echte Teilmenge von {@code superSet} ist; andernfalls {@code false}
     * @throws NullPointerException wenn {@code superSet} oder {@code subSet} {@code null} ist
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
     *         Prüft, ob {@code subSet} eine echte Teilmenge von {@code superSet} ist.
     *     </p>
     *     <p>
     *         Gibt das {@code subSet} zurück, wenn es eine echte Teilmenge von {@code superSet} ist.
     *         Andernfalls wird eine {@link IllegalArgumentException} mit der übergebenen Fehlermeldung geworfen.
     *     </p>
     * </div>
     *
     * @param superSet die Obermenge
     * @param subSet   die zu prüfende Teilmenge
     * @param message  die Fehlermeldung, falls {@code subSet} keine echte Teilmenge ist
     * @param <S>      der Typ des Sets
     * @param <A>      der Typ der enthaltenen Elemente
     * @return das übergebene {@code subSet}, wenn es eine echte Teilmenge ist
     * @throws NullPointerException     wenn ein Argument {@code null} ist
     * @throws IllegalArgumentException wenn {@code subSet} keine echte Teilmenge von {@code superSet} ist
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
     *         Prüft, ob die übergebene Klasse ein Interface ist.
     *     </p>
     *     <p>
     *         Gibt die Klasse zurück, wenn sie ein Interface ist. Andernfalls wird die vom
     *         {@code exceptionSupplier} gelieferte Ausnahme geworfen.
     *     </p>
     * </div>
     *
     * @param clazz             die zu prüfende Klasse
     * @param exceptionSupplier ein Supplier, der die zu werfende Ausnahme liefert, falls die Klasse kein Interface ist
     * @param <A>               der Typ der Klasse
     * @param <E>               der Typ der Ausnahme, die geworfen wird
     * @return die übergebene Klasse, falls sie ein Interface ist
     * @throws NullPointerException wenn {@code clazz} oder {@code exceptionSupplier} {@code null} ist,
     *                              oder wenn der Supplier {@code null} zurückliefert
     * @throws E                    wenn {@code clazz} kein Interface ist
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
     *         Prüft, ob die übergebene Klasse eine konkrete Klasse ist.
     *     </p>
     *     <p>
     *         Eine konkrete Klasse ist weder ein Interface noch abstrakt.
     *         Gibt die Klasse zurück, wenn sie konkret ist. Andernfalls wird die vom
     *         {@code exceptionSupplier} gelieferte Ausnahme geworfen.
     *     </p>
     * </div>
     *
     * @param clazz             die zu prüfende Klasse
     * @param exceptionSupplier ein Supplier, der die zu werfende Ausnahme liefert, falls die Klasse nicht konkret ist
     * @param <A>               der Typ der Klasse
     * @param <E>               der Typ der Ausnahme, die geworfen wird
     * @return die übergebene Klasse, falls sie eine konkrete Klasse ist
     * @throws NullPointerException wenn {@code clazz} oder {@code exceptionSupplier} {@code null} ist,
     *                              oder wenn der Supplier {@code null} zurückliefert
     * @throws E                    wenn {@code clazz} ein Interface oder abstrakt ist
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
