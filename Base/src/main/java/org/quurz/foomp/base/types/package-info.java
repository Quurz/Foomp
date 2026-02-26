/**
 * <div>
 *     <p>
 *         Foundational type abstractions used across the Foomp API.
 *     </p>
 *     <p>
 *         This package provides core building blocks (interfaces and marker types) that enable
 *         a functional, composable programming style and serve as the basis for higher‑level
 *         utilities in other packages.
 *     </p>
 *     <p>
 *         Typical abstractions include (non‑exhaustive):
 *     </p>
 *     <ul>
 *         <li>Value/Value2 – typed value carriers and pairs</li>
 *         <li>Transmogrifyable – transformation hook for fluent conversion</li>
 *         <li>Copyable – contract for creating value‑level copies</li>
 *         <li>Unwindable – contract for materializing deferred/lazy values</li>
 *         <li>Monadic/Appliable – functional composition contracts</li>
 *         <li>Marker annotations (e.g., for mutability or unwinding)</li>
 *     </ul>
 *     <p>
 *         Higher‑kinded type support (HKTs) is provided by the {@code foomp.higher} module and
 *         integrates with these base types where appropriate.
 *     </p>
 * </div>
 */
package org.quurz.foomp.base.types;