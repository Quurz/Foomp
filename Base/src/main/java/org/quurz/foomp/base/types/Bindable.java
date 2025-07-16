package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.function.Function;

/**
 * <div>
 *     <p>
 *         Ein Interface, das Monad-&auml;hnliche Bindungsoperationen für Typen definiert,
 *         die einen 'inneren' Wert enthalten und eine bind-Methode anbieten. Diese
 *         Bindungsmethode erm&ouml;glicht die Verkettung von Operationen,
 *         die auf den inneren Wert zugreifen, &auml;hnlich wie das monadische <code>bind</code> in Haskell.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des implementierenden Higher-Kinded-Typs
 * @param <A> Der Typ des 'inneren' Werts in diesem <code>Bindable</code>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Bindable<WT extends WitnessType, A> {

    /**
     * <div>
     *     <p>
     *         Wendet die angegebene Funktion auf den inneren Wert dieses <code>Bindable</code>-Objekts an,
     *         um ein neues <code>Bindable</code>-Objekt mit dem neuen inneren Wert zurückzugeben.
     *         Diese Methode unterst&uuml;tzt die Verkettung weiterer bind-Operationen,
     *         die auf diesem <code>Bindable</code>-Objekt basieren.
     *     </p>
     * </div>
     *
     * @param transformation Die Funktion, die auf den inneren Wert angewendet wird und ein neues
     *              <code>Bindable</code>-Objekt zurückgibt.
     * @param <B> Der Typ des 'inneren' Werts des neuen <code>Bindable</code>-Objekts
     * @return Ein neues <code>Bindable</code>-Objekt mit dem neuen inneren Wert
     *
     * @since 1.0.0
     */
    <B> @NonNull Higher1<? extends WT, B> bind(final @NonNull Function<? super A, ? extends Higher1<? extends WT, B>> transformation);

}
