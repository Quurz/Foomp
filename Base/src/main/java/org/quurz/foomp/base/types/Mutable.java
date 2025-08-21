package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Documentation-only marker indicating that the annotated type maintains mutable state.
 *     </p>
 *     <p>
 *         This annotation has no effect on runtime behaviour and does not imply any particular
 *         thread-safety or visibility guarantees. It merely communicates intent to readers and tools.
 *         Consider annotating state-changing methods with {@link MutatingOperation} as well.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = {
    ElementType.TYPE
})
@Documented
public @interface Mutable {}