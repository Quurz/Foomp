package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Basis für typsicher kopierbare Typen
 *     </p>
 * </div>
 *
 * @param <SELF> Typ der implementierenden Copyable-Klasse
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Copyable<SELF extends Copyable<?>> {

    /**
     * <div>
     *     <p>
     *         Erstellt eine Kopie des Objekts
     *     </p>
     * </div>
     *
     * @return Die Kopie
     *
     * @since 1.0.0
     */
    @NonNull SELF copy();

}
