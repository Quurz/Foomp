package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;

/**
 * <div>
 *     <p>
 *         Basis für Container-Typen, die entweder auf der linken oder auf der rechten Seite einen - M&ouml;glichwerweise von unterschiedlichem Typ - Wert enthalten.<br />
 *         Enth&auml;lt der Container keinen Wert auf der linken, dann sollte er auf der rechten Seite einen enthalten und umgekehrt.
 *     </p>
 * </div>
 *
 * @param <L> Typ des linken inneren Werts
 * @param <R> Typ des rechten inneren Werts
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface XorValue<L, R>
        extends Value<R> {

    /**
     * <div>
     *     <p>
     *         Haben wir was auf der linken Seite?
     *     </p>
     * </div>
     *
     * @return <code>true</code> falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    default boolean isLeft() {
        return !this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Haben wir auf der rechten Seite?
     *     </p>
     * </div>
     *
     * @return <code>true</code> falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    default boolean isPresent() {
        return this.isRight();
    }

    /**
     * <div>
     *     <p>
     *         Haben wir auf der rechten Seite?
     *     </p>
     * </div>
     *
     * @return <code>true</code> falls ein Wert vorhanden ist; <code>false</code>, falls nicht
     *
     * @since 1.0.0
     */
    boolean isRight();

    /**
     * <div>
     *     <p>
     *         Liefert den Wert auf der linken Seite dieses <code>XorValue</code>-Objekts<br />.
     *     </p>
     * </div>
     *
     * @return Der Wert auf der Linken Seite. Kann eine <code>NoSuchElementException</code> werfen, falls kein Wert vorhanden sein sollte.
     *
     * @since 1.0.0
     */
    @NonNull
    L getLeft()
            throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Liefert den Wert auf der rechten Seite dieses <code>XorValue</code>-Objekts<br />.
     *     </p>
     * </div>
     *
     * @return Der Wert auf der rechten Seite. Kann eine <code>NoSuchElementException</code> werfen, falls kein Wert vorhanden sein sollte.
     *
     * @since 1.0.0
     */
    @NonNull
    default R get()
            throws NoSuchElementException {
        return this.getRight();
    }

    /**
     * <div>
     *     <p>
     *         Liefert den Wert auf der rechten Seite dieses <code>XorValue</code>-Objekts<br />.
     *     </p>
     * </div>
     *
     * @return Der Wert auf der rechten Seite. Kann eine <code>NoSuchElementException</code> werfen, falls kein Wert vorhanden sein sollte.
     *
     * @since 1.0.0
     */
    @NonNull
    R getRight()
            throws NoSuchElementException;

}
