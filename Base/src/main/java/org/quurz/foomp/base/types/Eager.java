package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Eine Marker-Annotation, die kennzeichnet, dass eine Methode
 *         ihre Berechnung eager, also sofort und nicht verzögert (lazy), ausf&uuml;hrt.
 *     </p>
 *     <p>
 *         Diese Annotation dient ausschließlich der Dokumentation und hat
 *         keinen Einfluss auf das Laufzeitverhalten der Methode.
 *     </p>
 * </div>
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
public @interface Eager {}
