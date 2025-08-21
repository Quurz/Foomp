package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         The base marker interface for <i>witness</i> types used to simulate
 *         higher‑kinded types (HKTs) in Java.
 *     </p>
 *     <p>
 *         <b>What is a witness type?</b><br/>
 *         A witness type acts as a marker or placeholder that encodes the shape of a
 *         type constructor at the type level. While languages like Scala support HKTs
 *         natively, Java does not; a witness type lets us approximate that capability.
 *     </p>
 *     <p>
 *         Higher‑kinded types allow defining generic types that themselves take generic
 *         parameters. For example, one might want to express a type transformation that
 *         swaps type parameters:
 *     </p>
 *     <p>
 *         <code>public T&lt;B, A&gt; swapIt(T&lt;A, B&gt; toBeSwapped) { ... }</code>
 *     </p>
 *     <p>
 *         Since this is not directly expressible in Java, we simulate it by pairing a
 *         <code>WitnessType</code> with a corresponding {@code HigherN} interface:
 *     </p>
 *     <p>
 *         <code>public &lt;WT extends WitnessType, A, B&gt;<br/>
 *         &nbsp;&nbsp;org.quurz.foomp.higher.Higher2&lt;WT, B, A&gt; swapIt(org.quurz.foomp.higher.Higher2&lt;WT, A, B&gt; toBeSwapped) { ... }</code>
 *     </p>
 *     <p>
 *         <b>How does it work?</b><br/>
 *         The combination of a {@code HigherN} interface and a concrete {@code WT} witness
 *         provides enough structure to model the intended higher‑kinded type at compile time.
 *     </p>
 *
 *     <div>
 *         <p><b>Notes:</b></p>
 *         <ul>
 *             <li><i>HigherN</i> + <i>WT</i>: Together they describe the “type constructor” shape.</li>
 *             <li>The additional type parameters (e.g., <i>A</i>, <i>B</i>) are the regular type arguments
 *                 carried by the encoded higher‑kinded type.</li>
 *         </ul>
 *     </div>
 *
 *     <div>
 *         <p><b>Guidelines:</b></p>
 *         <ul>
 *             <li>Define one distinct witness type per encoded type constructor.</li>
 *             <li>Witness types are typically empty marker interfaces/classes.</li>
 *             <li>Use the witness type exclusively for this encoding to keep the type structure consistent.</li>
 *         </ul>
 *     </div>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface WitnessType {}
