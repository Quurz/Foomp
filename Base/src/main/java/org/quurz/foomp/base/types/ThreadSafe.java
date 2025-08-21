package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Documentation-only marker indicating that the annotated type is intended to be thread-safe.
 *     </p>
 *     <p>
 *         This annotation has no effect on runtime behaviour and does not enforce any concurrency,
 *         visibility, or atomicity guarantees. It communicates intent to readers and tools.
 *         Implementations should document their thread-safety strategy (immutability, confinement,
 *         synchronization, lock-free, etc.).
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
public @interface ThreadSafe {}
