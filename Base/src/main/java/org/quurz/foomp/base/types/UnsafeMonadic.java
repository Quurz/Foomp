package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Applicable;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

/**
 * <div>
 *     <p>
 *         Ein Interface, das eine Monade beschreibt, bei der Transformationen
 *         checked Exceptions werfen dürfen. Diese Variante ist nützlich, wenn
 *         Ausdrücke in der Monade potenziell fehlschlagen oder explizit
 *         Ausnahmen behandeln müssen.
 *     </p>
 *     <p>
 *         Dieses Interface ist eine unsichere Ergänzung zum typischen
 *         {@code Functor}– oder {@code Monad}-Verhalten, das Ausnahmen nicht
 *         berücksichtigt. Es bietet eine flexible Möglichkeit, Monadenoperationen
 *         mit potenziell fehlerhaften Transformationen auszudrücken.
 *     </p>
 * </div>
 *
 * @param <WT> Der "Zeuge" der Monade (Typklasse), z. B. {@code Attempt}, {@code Option}, {@code Either} usw.
 * @param <A>  Der enthaltene Werttyp.
 *
 * @see Monadic
 * @see org.quurz.foomp.base.util.Attempt
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface UnsafeMonadic<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Führt eine Transformation des enthaltenen Werts durch, wobei die gegebene Funktion
     *         eine checked Exception werfen darf. Dies entspricht der typischen {@code map}-Operation
     *         für Funktoren, jedoch unsicher.
     *     </p>
     * </div>
     *
     * @param <B> Der Typ des neuen Werts nach der Transformation.
     * @param transformation Die Funktion zur Transformation des enthaltenen Werts;
     *                       darf keine {@code null} sein.
     * @return Eine neue Monade mit dem transformierten Wert.
     * @throws Exception Falls die Transformation fehlschlägt.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> mapUnsafe(final @NonNull Applicable<? super A, ? extends B> transformation)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Wendet eine in der Monade eingebettete Funktion auf den enthaltenen Wert an.
     *         Diese Funktion darf eine Exception werfen. Dies entspricht der "ap"-Operation
     *         (applicative functor).
     *     </p>
     * </div>
     *
     * @param <B> Der Typ des neuen Werts nach der Anwendung.
     * @param transformation Eine Monade, die eine Funktion enthält,
     *                       welche auf den aktuellen Wert angewendet werden soll.
     * @return Eine neue Monade mit dem Ergebnis der Anwendung.
     * @throws Exception Falls die Transformation fehlschlägt.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> liftUnsafe(final @NonNull Higher1<? extends WT, Applicable<? super A, ? extends B>> transformation)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Führt eine Sequenz von Berechnungen aus, bei der das Ergebnis einer Transformation
     *         erneut eine Monade ist. Diese Transformation darf ebenfalls eine Exception werfen.
     *         Entspricht dem klassischen {@code flatMap} oder {@code bind} in Monadensystemen.
     *     </p>
     * </div>
     *
     * @param <B> Der Typ des neuen Werts nach der Bindung.
     * @param transformation Eine Funktion, die einen Wert entgegennimmt und eine neue Monade zurückgibt.
     * @return Die resultierende Monade nach Anwendung der Transformation.
     * @throws Exception Falls die Transformation fehlschlägt.
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> bindUnsafe(final @NonNull Applicable<? super A, ? extends Higher1<? extends WT, B>> transformation)
        throws Exception;

}
