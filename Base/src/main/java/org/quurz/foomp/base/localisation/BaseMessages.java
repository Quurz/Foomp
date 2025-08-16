package org.quurz.foomp.base.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * <div>
 *     <p>
 *         Lokalisierung der Meldungen
 *     </p>
 * </div>
 *
 * @since 1.0.0
 * @author Alexander Schell
 */
// TODO: Fehlende Tests nachziehen
public final class BaseMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("BaseMessages", Locale.getDefault());

    private BaseMessages() {}

    /*
        Meldungen für Foomp-Base
     */

    /**
     * <div>
     *     <p>
     *         Es gibt leider keine Werte
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String noValuePresent() {
        return RESOURCE_BUNDLE.getString("NO_VALUE_PRESENT");
    }

    public static String noValuePresentIn(final String containerName) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NO_VALUE_PRESENT_IN"), containerName);
    }

    /**
     * <div>
     *     <p>
     *         Das gesuchte Objekt konnte nicht gefunden werden
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String notFound(final @NonNull Object object) {
        Objects.requireNonNull(object);
        return String.format(RESOURCE_BUNDLE.getString("NOT_FOUND"), object);
    }

    public static String notFoundIn(final @NonNull Object object,
                                    final @NonNull String containerName) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NOT_FOUND_IN"), object, containerName);
    }

    /**
     * <div>
     *     <p>
     *         Null-Argumente sind nicht erlaubt
     *     </p>
     * </div>
     *
     * @param argumentName Name des Null-Arguments
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String nullValue(@NonNull final String argumentName) {
        Objects.requireNonNull(argumentName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ARGUMENT"), argumentName);
    }

    /**
     * <div>
     *     <p>
     *         Eine leere Collection ist wenig hilfreich
     *     </p>
     * </div>
     *
     * @param collectionName Der Name der leeren Collection
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String emptyCollection(final String collectionName) {
        Objects.requireNonNull(collectionName);
        return String.format(RESOURCE_BUNDLE.getString("EMPTY_COLLECTION"), collectionName);
    }

    /**
     * <div>
     *     <p>
     *         Die Collection enthält ein Null-Element. Das ist nicht ideal.
     *     </p>
     * </div>
     *
     * @param containerName Name der Collection
     * @param index Index des Null-Elements in der Collection
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String nullElementInAt(final String containerName,
                                         final int index
    ) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ELEMENT_IN_AT"), containerName, index);
    }

    public static String nullElementIn(final String containerName) {
        Objects.requireNonNull(containerName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_ELEMENT_IN"), containerName);
    }

    /**
     * <div>
     *     <p>
     *         Das Ergebnis darf nicht Null sein
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String nullResult() {
        return RESOURCE_BUNDLE.getString("NULL_RESULT");
    }

    public static String nullResultFrom(final @NonNull String methodName) {
        Objects.requireNonNull(methodName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_RESULT_FROM"), methodName);
    }

    /**
     * <div>
     *     <p>
     *         Ein Supplier sollte niemals Null zurückliefern
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String nullSupplied() {
        return RESOURCE_BUNDLE.getString("NULL_SUPPLIED");
    }

    public static String nullSuppliedFrom(final @NonNull String supplierName) {
        Objects.requireNonNull(supplierName);
        return String.format(RESOURCE_BUNDLE.getString("NULL_SUPPLIED_FROM"), supplierName);
    }

    /**
     * <div>
     *     <p>
     *         Bitte etwas positiver sein!
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String negativeValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("NEGATIVE"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         Sei wirklich positiver!
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String nonPositiveValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("NOT_POSITIVE"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         Eine 0? Was soll ich damit anfangen?
     *     </p>
     * </div>
     *
     * @param parameterName Name des Parameters
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String zeroValue(final @NonNull String parameterName) {
        Objects.requireNonNull(parameterName);
        return String.format(RESOURCE_BUNDLE.getString("ZERO"), parameterName);
    }

    /**
     * <div>
     *     <p>
     *         Generiert eine Fehlermeldung, wenn versucht wird, ein Element in einer Datenstruktur
     *         einzuf&uuml;gen, die keine mehrfach vorkommenden Elemente zul&auml;sst.
     *     </p>
     * </div>
     *
     * @param element Das Element, das bereits in der Datenstruktur vorhanden ist. Es wird
     *                in die Fehlermeldung eingefügt.
     * @return Eine formatierte Fehlermeldung, die anzeigt, dass das Element bereits vorhanden ist.
     *
     * @throws NullPointerException Wenn das angegebene Element {@code null} ist.
     *
     * @since 1.0.0
     */
    public static String duplicateElement(final @NonNull Object element) {
        Objects.requireNonNull(element);
        return String.format(RESOURCE_BUNDLE.getString("DUPLICATE_ELEMENT"), element);
    }

    public static String illegalIntervalBounds(final @NonNull Object lowerBound,
                                               final @NonNull Object upperBound) {
        Objects.requireNonNull(lowerBound);
        Objects.requireNonNull(upperBound);
        return String.format(RESOURCE_BUNDLE.getString("ILLEGAL_INTERVAL_BOUNDS"), lowerBound, upperBound);
    }

    /**
     * <div>
     *     <p>
     *         Etwas ist hier wirklich schiefgelaufen
     *     </p>
     * </div>
     *
     * @return Die entsprechende Meldung
     *
     * @since 1.0.0
     */
    public static String notSupposedToHappen() {
        return RESOURCE_BUNDLE.getString("NOT_SUPPOSED_TO_HAPPEN");
    }

}
