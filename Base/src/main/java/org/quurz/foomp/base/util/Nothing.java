package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Transmogrifyable;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Leider kennt Java das leere Tupel nicht. Deswegen gibt's das <code>Nothing</code>.<br />
 *         Die <code>null</code>-Referenz ist ein echtes Problem. Das sollte mit dem <code>Nothing</code>-Singleton vermeidbar sein.
 *         Siehe auch {@link Maybe.None} falls man eine typisierte L&ouml;sung braucht.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public final class Nothing
        implements Transmogrifyable<Nothing> {

    /**
     * <div>
     *     <p>
     *         Das <code>Nothing</code>-Singleton
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    public static final Nothing nothing
            = new Nothing();

    /**
     * <div>
     *     <p>
     *         Privater Konstruktor, um die Instanziierung zu verhindern.
     *     </p>
     * </div>
     */
    private Nothing() {}

    /**
     * <div>
     *     <p>
     *         Transmogrifiziert das <code>Nothing</code>-Objekt mit der angegebenen Funktion.
     *     </p>
     *     <p>
     *         Eine fast religiöse Sache: Schöpfung aus dem Nichts.   &#128512;
     *     </p>
     * </div>
     *
     * @param transmogrifier Die Funktion, die auf das <code>Nothing</code>-Objekt angewendet wird.
     * @param <T> Der Rückgabetyp der Funktion.
     * @return Das Ergebnis der Anwendung der Funktion auf das <code>Nothing</code>-Objekt.
     * @throws NullPointerException Wenn der <code>transmogrifier</code> oder das Ergebnis der Anwendung <code>null</code> ist.
     */
    @Override
    public <T> @NonNull T transmogrify(@NonNull Function<? super Nothing, ? extends T> transmogrifier) {
        Objects.requireNonNull(transmogrifier, nullValue("transmogrifier"));
        return Objects.requireNonNull(transmogrifier.apply(this), nullResultFrom("transmogrifier"));
    }

    /**
     * <div>
     *     <p>
     *         &Uuml;berpr&uuml;ft, ob das angegebene Objekt gleich dem aktuellen {@code Nothing}-Objekt ist.
     *     </p>
     * </div>
     *
     * @param obj Das zu vergleichende Objekt.
     * @return <code>true</code>, wenn das angegebene Objekt ein <code>Nothing</code>-Objekt ist, andernfalls <code>false</code>.
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Nothing;
    }

    /**
     * <div>
     *     <p>
     *         Gibt den Hashcode des {@code Nothing}-Objekts zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Der Hashcode des <code>Nothing</code>-Objekts.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine String-Repr&auml;sentation des {@code Nothing}-Objekts zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @return Eine String-Repräsentation des <code>Nothing</code>-Objekts.
     */
    @Override
    public String toString() {
        return "Nothing";
    }

}
