package org.quurz.foomp.plugins;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Definiert die grundlegenden Operationen für Klassen, die Delegation unterstützen.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des Objekts, an das delegiert wird
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */

public interface Delegator<A> {

    /**
     * <div>
     *     <p>
     *         Gibt das Delegate-Objekt zurück.
     *     </p>
     * </div>
     *
     * @return Das Delegate-Objekt
     *
     * @since 1.0.0
     */

    @NonNull A $__get_delegate();

    /**
     * <div>
     *     <p>
     *         Setzt ein neues Delegate-Objekt.
     *     </p>
     * </div>
     *
     * @param $__delegate Das neue Delegate-Objekt
     * @throws NullPointerException wenn {@code $__delegate} {@code null} ist
     *
     * @since 1.0.0
     */

    void $__set_delegate(final @NonNull A $__delegate);

}
