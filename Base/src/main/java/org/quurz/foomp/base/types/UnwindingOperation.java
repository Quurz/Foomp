package org.quurz.foomp.base.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <div>
 *     <p>
 *         Documentation-only marker for methods that <em>unwind</em> a value by triggering
 *         evaluation/materialization of deferred or lazy computations.
 *     </p>
 *     <p>
 *         This annotation has no effect on runtime behaviour. It serves to make evaluation
 *         points explicit for readers and tooling (e.g., code reviews, static analysis).
 *     </p>
 *     <p>
 *         Note: This marker denotes <strong>evaluation now</strong>, not laziness. Use it to flag
 *         methods that realize values (e.g., {@code unwind()}, {@code runState(...)}), not methods
 *         that defer computation.
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
     *         Optional comment with additional hints about the kind of evaluation/materialization.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    String comment() default "";

}
