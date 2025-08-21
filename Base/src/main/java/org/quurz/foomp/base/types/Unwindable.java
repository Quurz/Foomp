package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Contract for types that can <em>unwind</em> deferred or lazy computations and return
 *         a materialized (stable) representation of themselves.
 *     </p>
 *     <p>
 *         Implementations should document their evaluation semantics (lazy vs. eager parts),
 *         whether unwinding is idempotent, and any performance implications.
 *     </p>
 * </div>
 *
 * @param <SELF> the implementing unwindable type (self type)
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
     *         Unwinds (materializes) deferred or lazy computations and returns an
     *         evaluated representation of this value.
     *     </p>
     *     <p>
     *         Contract: this method must return a non‑null value. Implementations should strive
     *         for idempotency (calling {@code unwind()} multiple times yields equivalent results).
     *     </p>
     * </div>
     *
     * @return the materialized (unwound) value; never {@code null}
     *
     * @since 1.0.0
     */
    @UnwindingOperation
    @NonNull SELF unwind();

}
