package org.quurz.foomp.higher;

/**
 * <div>
 *     <p>
 *         Die Basis der <i>Witness</i>-Typen zur Simulation von Higher-Kinded Types (HKT) in Java.
 *     </p>
 *     <p>
 *         <b>Was ist ein 'Witness'-Typ?</b><br />
 *         Ein 'Witness'-Typ dient in Java als Indikator oder Platzhalter, um die Typstruktur von Higher-Kinded Types zu simulieren –
 *         ein Konzept, das in Sprachen wie Scala nativ unterst&uuml;tzt wird, jedoch in Java standardm&auml;&szlig;ig fehlt.
 *     </p>
 *     <p>
 *         Higher-Kinded Types erm&ouml;glichen die Definition generischer Typen, die selbst andere generische Typen parametrisieren k&ouml;nnen.
 *         Ein Beispiel w&auml;re die F&auml;higkeit, den Typ <i>T&lt;B, A&gt;</i> als R&uuml;ckgabetyp einer Methode zu definieren,
 *         die ein Objekt vom Typ <i>T&lt;A, B&gt;</i> entgegennimmt und dessen Parameter tauscht:
 *     </p>
 *     <p>
 *         <code>public T&lt;B, A&gt; swapIt(T&lt;A, B&gt; toBeSwapped) {...}</code>
 *     </p>
 *     <p>
 *         Da dies in Java nicht direkt m&ouml;glich ist, verwenden wir Witness-Typen, um eine &auml;hnliche Funktionalit&auml;t zu erreichen.
 *         In der Praxis k&ouml;nnte dies so aussehen:
 *     </p>
 *     <p>
 *         <code>public &lt;WT extends WitnessType, A, B&gt; Higher2&lt;WT, B, A&gt; swapIt(Higher2&lt;WT, A, B&gt; toBeSwapped) {...}</code>
 *     </p>
 *     <p>
 *         <b>
 *             Wie funktioniert das?
 *         </b>
 *         <br />
 *         Durch die Kombination von <i>Higher2</i> und <i>WT</i> wird die ben&ouml;tigte Struktur aufgebaut, um
 *         die Eigenschaften eines Higher-Kinded Types zu simulieren.
 *     </p>
 *
 *     <div>
 *         <p>
 *             <b>
 *                 Wichtige Hinweise:
 *             </b>
 *         </p>
 *         <ul>
 *             <li><i>Higher2</i> und <i>WT</i>: Diese beiden Komponenten bilden zusammen die Struktur des eigentlichen Typs <i>T</i>.</li>
 *             <li><i>A</i> und <i>B</i>: Dies sind die normalen generischen Parameter, die von der Methode verarbeitet werden.</li>
 *         </ul>
 *     </div>
 *
 *     <p>
 *         Es ist daher wichtig, dass der <i>WitnessType</i> ausschlie&szlig;lich f&uuml;r diesen Zweck verwendet wird, um die Typkonsistenz zu gew&auml;hrleisten. &#x1F621;
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface WitnessType {}
