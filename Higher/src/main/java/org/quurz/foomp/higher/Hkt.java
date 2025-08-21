package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Base interface for higher‑kinded types (HKTs) in Java.
 *     </p>
 *     <p>
 *         Java does not natively support higher‑kinded types. This interface, together with
 *         {@link WitnessType} and the rank‑specific {@code HigherN} interfaces (e.g.
 *         {@link org.quurz.foomp.higher.Higher1}, {@link org.quurz.foomp.higher.Higher2},
 *         {@link org.quurz.foomp.higher.Higher3}, {@link org.quurz.foomp.higher.Higher4}),
 *         provides a lightweight encoding that models type constructors at the type level.
 *     </p>
 *     <p>
 *         A concrete HKT is identified by a witness type {@code WT} that encodes the
 *         shape of the type constructor. For example, a rank‑2 constructor can be encoded as
 *         {@code Higher2<WT, A, B>} and later “fixed” into a concrete implementation using a
 *         type‑specific helper (commonly named {@code fix} or {@code narrow}).
 *     </p>
 * </div>
 *
 * <h2>Usage (sketch)</h2>
 * <pre>{@code
 * // 1) Define a witness type for your constructor (usually an empty marker).
 * public final class MyConstructor implements WitnessType { private MyConstructor() {} }
 *
 * // 2) Use a HigherN interface to express your API in terms of HKTs.
 * <WT extends WitnessType, A, B> Higher2<WT, B, A> swap(Higher2<WT, A, B> value) { ... }
 *
 * // 3) Concrete types provide a fix/narrow helper to “reify” the HKT when needed:
 * var concrete = MyConcreteType.fix(hktValue);
 * }</pre>
 *
 * <h2>Rank (arity)</h2>
 * <p>
 *     The rank (also called arity) specifies how many type parameters the HKT carries.
 *     Rank‑1 types always return {@code 1} from {@link #arity()}, rank‑2 return {@code 2}, etc.
 * </p>
 *
 * @param <WT> The witness type that encodes the shape of the type constructor (see {@link WitnessType})
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Hkt<WT extends WitnessType> {

    /**
     * <div>
     *     <p>
     *         Returns the arity (rank) of this higher‑kinded type.
     *     </p>
     *     <p>
     *         The arity denotes how many type parameters the encoded constructor carries.
     *         For example, a rank‑1 HKT has arity {@code 1}, while a rank‑2 HKT has arity {@code 2}, and so on.
     *     </p>
     * </div>
     *
     * @return the arity (rank) of this HKT
     *
     * @since 1.0.0
     */
    default int arity() {
        return 0;
    }

}
