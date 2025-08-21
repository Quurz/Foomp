/**
 * <div>
 *     <p>
 *         Core module for simulating higher‑kinded types (HKTs) in Java using witness types.
 *     </p>
 *     <p>
 *         This module exposes the HKT encoding via:
 *     </p>
 *     <ul>
 *         <li>{@code WitnessType} – the marker that encodes the type‑constructor shape</li>
 *         <li>{@code Higher1}, {@code Higher2}, {@code Higher3}, {@code Higher4} – rank‑specific HKT interfaces</li>
 *     </ul>
 *     <p>
 *         Use a distinct witness type per encoded type constructor and shape public APIs in terms of
 *         {@code HigherN&lt;WT, …&gt;} interfaces. Concrete types typically provide a static
 *         {@code fix}/{@code narrow} helper to reify HKT values back to the concrete implementation.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 */
module foomp.higher {
    exports org.quurz.foomp.higher;
}
