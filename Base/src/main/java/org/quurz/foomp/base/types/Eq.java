package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine funktionale Schnittstelle zur Definition von Gleichheitsoperationen.
 *         Inspiriert von Haskells <code>Eq</code>-Typeclass erlaubt dieses Interface
 *         eine flexible Implementierung von Gleichheitskriterien.
 *     </p>
 *     <p>
 *         Diese Schnittstelle unterst&uuml;tzt:
 *         <ul>
 *             <li>Eine typensichere Gleichheit, definiert durch die Methode {@link #eq(Eq)}.</li>
 *             <li>Eine benutzerdefinierte Gleichheit mittels {@link BiPredicate}, definiert durch die Methode {@link #eq(BiPredicate, Eq)}.</li>
 *             <li>Eine Standard-Hash-Wert-Berechnung, bereitgestellt durch die Methode {@link #hash()}.</li>
 *             <li>Eine benutzerdefinierte Hash-Wert-Berechnung, definiert durch die Methode {@link #hash(Function)}.</li>
 *         </ul>
 *     </p>
 * </div>
 *
 * @param <SELF> Der Typ, der diese Schnittstelle implementiert.
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Eq<SELF extends Eq<?>> {

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob das aktuelle Objekt gleich dem angegebenen Objekt ist.
     *     </p>
     * </div>
     *
     * @param other Das Objekt, mit dem Gleichheit überprüft wird.
     * @return <code>true</code>, wenn beide Objekte gleich sind, andernfalls <code>false</code>.
     *
     * @throws NullPointerException Falls <code>other</code> <code>null</code> ist.
     *
     * @since 1.0.0
     */
    boolean eq(final @NonNull SELF other);

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine Gleichheitspr&uuml;fung mit einer benutzerdefinierten Vergleichslogik durch.
     *     </p>
     * </div>
     *
     * @param equals   Eine benutzerdefinierte Vergleichslogik, dargestellt durch ein {@link BiPredicate}.
     * @param other  Das Objekt, mit dem die Gleichheit gepr&uuml;ft werden soll. Darf nicht <code>null</code> sein.
     *
     * @return <code>true</code>, wenn die beiden Objekte gem&auml;&szlig; der bereitgestellten Vergleichslogik
     *         als gleich betrachtet werden; andernfalls <code>false</code>.
     *
     * @throws NullPointerException Falls <code>equals</code> oder <code>other</code> <code>null</code> ist.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    default boolean eq(final @NonNull BiPredicate<SELF, SELF> equals,
                       final @NonNull Eq<SELF> other) {
        Objects.requireNonNull(equals, nullValue("equals"));
        Objects.requireNonNull(other, nullValue("other"));
        return equals.test((SELF) this, (SELF) other);
    }

    /**
     * <div>
     *     <p>
     *         Berechnet den Hash-Wert f&uml;r dieses Objekt unter Verwendung der Standard-Hash-Implementierung.
     *     </p>
     * </div>
     *
     * @return Ein Hash-Wert, der auf der Standard-Hash-Implementierung von {@link Objects#hash(Object...)} basiert.
     *
     * @since 1.0.0
     */
    default int hash() {
        return this.hashCode();
    }

    /**
     * <div>
     *     <p>
     *         Berechnet den Hash-Wert dieses Objekts mithilfe einer benutzerdefinierten Hash-Funktion.
     *     </p>
     * </div>
     *
     * @param hashFunction Eine benutzerdefinierte Funktion, die den Hash-Wert berechnet. Darf nicht <code>null</code> sein.
     *
     * @return Der von der <code>hashFunction</code> berechnete Hash-Wert.
     *
     * @throws NullPointerException Falls <code>hashFunction</code> oder ihr Rückgabewert <code>null</code> ist.
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    default int hash(final @NonNull Function<SELF, Integer> hashFunction) {
        Objects.requireNonNull(hashFunction, nullValue("hashFunction"));
        return Objects.requireNonNull(hashFunction.apply((SELF) this), nullResult());
    }

}
