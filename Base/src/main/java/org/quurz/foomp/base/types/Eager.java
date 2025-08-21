package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Deprecated: Prefer {@link UnwindingOperation} to mark methods that trigger evaluation or materialization.
 *         {@code Eager} was intended as a documentation‑only marker for immediate (non‑lazy) execution,
 *         but its semantics overlap with {@code UnwindingOperation}.
 *     </p>
 *     <p>
 *         This annotation remains for backward compatibility and has no effect on runtime behaviour.
 *     </p>
 *     <p>
 *         <b>Note:</b> This annotation may be repurposed in the future if a distinct “eager‑by‑design” marker is required.
 *     </p>
 * </div>
 *
 * @deprecated Use {@link UnwindingOperation} instead.
 * @since 1.0.0
 */
@Deprecated
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface Eager {}
