package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Wrapper für einen optionalen Wert vom Typ <code>A</code>.
 *         Dieses Interface stellt eine vereinfachte API zur Verfügung,
 *         um mit möglicherweise nicht vorhandenen Werten umzugehen.
 *     </p>
 *     <p>
 *         Das Interface erweitert {@link Supplier} und bietet Methoden,
 *         um den enthaltenen Wert sicher zu prüfen und abzurufen.
 *     </p>
 * </div>
 *
 * @param <A> Der Typ des enthaltenen Werts
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Value<A>
        extends Supplier<A> {

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob ein Wert vorhanden ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls ein Wert vorhanden ist; andernfalls <code>false</code>
     *
     * @since 1.0.0
     */
    boolean isPresent();

    /**
     * <div>
     *     <p>
     *         Gibt den enthaltenen Wert zur&uuml;ck, falls einer vorhanden ist.
     *         Falls kein Wert vorhanden ist, wird eine
     *         {@link NoSuchElementException} ausgel&ouml;st.
     *     </p>
     * </div>
     *
     * @return Der enthaltene Wert
     *
     * @throws NoSuchElementException Falls kein Wert vorhanden ist
     *
     * @since 1.0.0
     */
    @NonNull A get()
            throws NoSuchElementException;

}
