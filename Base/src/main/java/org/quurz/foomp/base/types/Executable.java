package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.Callable;

/**
 * <div>
 *     <p>
 *         Eine, ein Ergebnis liefernde, Aufgabe, deren Ausf&uuml;hrung eine checked Exception werfen kann
 *     </p>
 * </div>
 *
 * @param <A> Typ des Ergebnisses
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Executable<A>
        extends Callable<A> {

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt diese Aufgabe aus und liefert ihr Ergebnis
     *     </p>
     * </div>
     *
     * @return Ergebnis der Ausf&uuml;hrung
     * @throws Exception Falls w&auml;hrend der Ausf&uuml;rung ein Fehler aufgetreten sein sollte
     *
     * @since 1.0.0
     */
    @NonNull
    A execute()
        throws Exception;

    @Override
    default A call()
        throws Exception {
        return execute();
    }

    /**
     * <div>
     *     <p>
     *         Wandelt dieses {@code Executable} in ein {@link SafeExecutable} um,
     *         das Fehler in einem {@link XorValue} kapselt, anstatt eine Exception zu werfen.
     *     </p>
     *     <p>
     *         Die Methode sorgt daf&uuml;r, dass die Ausf&uuml;hrung von {@link #execute()} keine
     *         ungefangenen Ausnahmen mehr wirft. Stattdessen werden m&ouml;gliche Fehler
     *         als linke Werte in einem {@link XorValue} zur&uuml;ckgegeben.
     *     </p>
     * </div>
     *
     * @return Ein {@link SafeExecutable}, das Fehler in einem {@link XorValue} verpackt
     *
     * @since 1.0.0
     */
    default SafeExecutable<A> safe() {
        return this::execute;
    }

}
