/**
 * <div>
 *   <p>
 *     Functional building blocks and typeclasses used across the base layer:
 *     higher-order functions (Fun, Fun2/3/4), operators, and collections/seq abstractions.
 *   </p>
 *   <p>
 *     Contracts:
 *     <ul>
 *       <li>Non-null by default: inputs and results must not be {@code null} unless stated otherwise.</li>
 *       <li>Composition-first APIs: prefer combinators over inheritance.</li>
 *       <li>Documented runtime characteristics: strictness (eager/lazy), mutability, complexity.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Related:
 *     <ul>
 *       <li>{@code Fun}/{@code Fun2}/{@code Fun3}/{@code Fun4} – function family with strict contracts</li>
 *       <li>{@code Operator}/{@code Operator2}/{@code Operator3} – closed-over operators on A</li>
 *       <li>{@code Seq} – sequence abstraction with convenience materialization</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Note: We like our APIs deterministic and our docs friendly. A little humor included – handle with care. 🙂
 *   </p>
 * </div>
 *
 * @since 1.0.0
 */
package org.quurz.foomp.base.functions;