package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Schnittstelle f&uuml;r ein generisches assoziatives Array.
 *     </p>
 * </div>
 *
 * @param <K> Der Typ der Schl&uuml;ssel im assoziativen Array.
 * @param <V> Der Typ der Werte im assoziativen Array.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Assoc<K, V> {

    /**
     * <div>
     *     <p>
     *         F&uuml;gt ein Schl&uuml;ssel-Wert-Paar in das assoziative Array ein.
     *     </p>
     * </div>
     *
     * @param key Der Schl&uuml;ssel, der eingef&uuml;gt werden soll.
     * @param value Der Wert, der eingef&uuml;gt werden soll.
     * @return Das assoziative Array nach dem Einf&uuml;gen des Schl&uuml;ssel-Wert-Paares.
     */
    @NonNull
    Assoc<K, V> put(final @NonNull K key,
                    final @NonNull V value);

    /**
     * <div>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob das assoziative Array den angegebenen Schl&uuml;ssel enth&auml;lt.
     *     </p>
     * </div>
     *
     * @param key Der Schl&uuml;ssel, der &uuml;berpr&uuml;ft werden soll.
     * @return <code>true</code>, wenn das assoziative Array den Schl&uuml;ssel enth&auml;lt, andernfalls <code>false</code>.
     */
    boolean contains(final @NonNull K key);

    /**
     * <div>
     *     <p>
     *         Sucht einen Wert im assoziativen Array anhand des angegebenen Schl&uuml;ssels.
     *     </p>
     * </div>
     *
     * @param key Der Schl&uuml;ssel, nach dem gesucht werden soll.
     * @return Der gefundene Wert.
     * @throws NoSuchElementException Wenn der Schl&uuml;ssel nicht im assoziativen Array gefunden wird.
     */
    @NonNull
    Value<V> get(final @NonNull K key)
            throws NoSuchElementException;

}
