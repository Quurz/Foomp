package org.quurz.foomp.base.util;

/**
 * <div>
 *     <p>
 *         Ein funktionales Interface, das einem {@link java.util.function.Supplier} ähnelt, jedoch
 *         auch checked Exceptions werfen darf.
 *     </p>
 *     <p>
 *         Diese Schnittstelle ist besonders nützlich in Situationen, in denen ein Lambda-Ausdruck
 *         oder eine Methode verwendet werden soll, die eine Exception werfen kann, z. B. bei
 *         der verzögerten Initialisierung oder innerhalb von try-with-resources-ähnlichen Konstruktionen.
 *     </p>
 *     <pre>{@code
 *         ThrowingSupplier<String> supplier = () -> {
 *             if (Math.random() < 0.5) {
 *             throw new IOException("Unlucky!");
 *         }
 *         return "OK";
 *      };
 *     }</pre>
 * </div>
 *
 * @param <A> Der Typ des von {@code get()} zurückgegebenen Ergebnisses.
 *
 * @author Alexander Schell
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowingSupplier<A> {

    /**
     * <div>
     *     <p>
     *         Führt die Berechnung aus und liefert das Ergebnis zurück.
     *     </p>
     * </div>
     *
     * @return Das Ergebnis der Berechnung.
     * @throws Exception Falls ein Fehler während der Berechnung auftritt.
     *
     * @since 1.0.0
     */
    A get()
        throws Exception;

}
