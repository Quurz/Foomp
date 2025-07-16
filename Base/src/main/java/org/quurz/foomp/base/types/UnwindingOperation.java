package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Markiert eine Methode als "Unwinding Operation", was bedeutet, dass die Methode
 *         Werte "lazy" bereitstellt, also erst bei tats&auml;chlichem Zugriff berechnet.
 *         Diese Kennzeichnung signalisiert dem Nutzer, dass die Methode
 *         auf Anforderung ausgew&auml;hlte Werte zur&uuml;ckgibt, ohne den Zustand zu ver&auml;ndern.
 *     </p>
 *     <p>
 *         Sie ist n&uuml;tzlich für Klassen und Strukturen, die mit „lazy evaluation“ arbeiten und
 *         den Wert oder Zustand nur dann berechnen oder abrufen, wenn es n&ouml;tig ist, um
 *         Ressourcen zu sparen.
 *     </p>
 * </div>
 *
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {
    ElementType.METHOD
})
@Documented
public @interface UnwindingOperation {

    /**
     * <div>
     *     <p>
     *         Ein optionaler Kommentar, um zus&auml;tzliche Hinweise zur
     *         Verwendung oder zur Art der "lazy" Evaluation zu geben.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    String comment() default "";

}
