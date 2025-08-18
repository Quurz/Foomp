package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Marker annotation for operations that produce side effects after object construction,
 *         e.g. mutating the receiver's state, modifying collaborators, performing I/O, or changing
 *         global/thread-local state.
 *     </p>
 *     <p>
 *         Note: Constructors are intentionally not targeted; object initialization itself is not
 *         considered a mutating operation in this sense.
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
public @interface MutatingOperation {}
