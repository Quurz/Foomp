package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Da die meisten Container lazy sein sollten, ist es eventuell nicht doof, eine M&ouml;glichkeit zu haben, all die aufgespulten
 *         Operationen auch bewusst abspulen zu können.<br />
 *         Daf&uuml;r sollten die Container-Klassen <code>Unwindable</code> implementieren.
 *     </p>
 * </div>
 *
 * @param <SELF> Typ der implementierenden Unwindable-Klasse
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Unwindable<SELF extends Unwindable<?>> {

    /**
     * <div>
     *     <p>
     *         Spult alle Operationen ab und liefert ein <code>Unwindable</code> zur&uuml;ck
     *     </p>
     * </div>
     *
     * @return Das 'abgespulte' <code>Unwindable</code>
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull SELF unwind();

}
