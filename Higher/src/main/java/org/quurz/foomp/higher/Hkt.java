package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Die Basis aller Higher-Kinded-Typen.
 *     </p>
 *     <p>
 *         Diese Schnittstelle bildet die Grundlage f&uuml;r die Implementierung von Higher-Kinded-Typen (HKT) in Java, die
 *         es erm&ouml;glicht, Typen h&ouml;herer Ordnung zu simulieren. Jeder Higher-Kinded-Typ wird durch einen
 *         Witness-Typ <code>WT</code> spezifiziert, der im Allgemeinen den "Typ-Konstruktor" beschreibt, den der
 *         Higher-Kinded-Typ repr&auml;sentiert.
 *     </p>
 *     <p>
 *         Beispiel: In Sprachen, die HKT direkt unterst&uuml;tzen, kann man komplexe Typen wie <code>Option&lt;A&gt;</code>
 *         abstrahieren und manipulieren. Diese Schnittstelle stellt eine M&ouml;glichkeit bereit, solche Typkonstrukte
 *         indirekt zu modellieren, indem der Witness-Typ als Marker dient.
 *     </p>
 * </div>
 *
 * @param <WT> Der Witness-Typ des Higher-Kinded-Typs (siehe auch {@link WitnessType}).
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface Hkt<WT extends WitnessType> {

    /**
     * <div>
     *     <p>
     *         Liefert die Arit&auml;t des Higher-Kinded-Typs.
     *     </p>
     *     <p>
     *         Die Arit&auml;t (oder auch Rang) des Higher-Kinded-Typs gibt an, wie viele "innere" Typ-Parameter der Typ
     *         aufnimmt. Beispielsweise hat ein Typkonstruktor wie <code>Option&lt;A&gt;</code> die Arit&auml;t 1,
     *         w&auml;hrend ein hypothetischer Typ <code>BiFunction&lt;A, B, C&gt;</code> eine Arit&auml;t von 2 h&auml;tte.
     *     </p>
     *     <p>
     *         Obwohl die Methode eine informative Rolle spielt, ist sie optional und dient in den meisten F&auml;llen nur
     *         dazu, generelle Informationen &uuml;ber die Struktur von Typen im HKT-Framework zu liefern.
     *     </p>
     * </div>
     *
     * @return Die Arit&auml;t des implementierenden Higher-Kinded-Typs.
     *
     * @since 1.0.0
     */
    // TODO: Entfernen. Ist momentan ünerflüssig und verwirrt nur.
    default int arity() {
        return 0;
    }

}
