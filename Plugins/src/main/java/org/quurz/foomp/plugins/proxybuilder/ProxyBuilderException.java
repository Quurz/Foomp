package org.quurz.foomp.plugins.proxybuilder;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Laufzeit-Ausnahme, die beim Erstellen oder Instanziieren von Proxy-Klassen auftritt.
 *     </p>
 *     <p>
 *         Diese Exception wird typischerweise geworfen, wenn beim Einsatz von ByteBuddy zur
 *         Generierung oder beim Laden einer dynamischen Proxy-Klasse ein Fehler auftritt –
 *         z. B. bei fehlerhaften Typen, Reflektionsproblemen oder ungültiger Konfiguration.
 *     </p>
 *     <p>
 *         Die {@code ProxyBuilderException} kapselt sowohl technische Fehlerursachen
 *         (z. B. {@link java.lang.reflect.InvocationTargetException}),
 *         als auch semantische Fehler, die durch ungültige Eingaben an den {@link ProxyBuilder}
 *         (z. B. {@link java.lang.reflect.InvocationTargetException}),
 *         entstehen können.
 *     </p>
 * </div>
 *
 * @see ProxyBuilder
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class ProxyBuilderException
        extends RuntimeException {

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code ProxyBuilderException} mit einer Fehlermeldung.
     *     </p>
     * </div>
     *
     * @param message die Fehlerbeschreibung (nicht {@code null})
     *
     * @since 1.0.0
     */
    public ProxyBuilderException(final @NonNull String message) {
        super(message);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code ProxyBuilderException} mit einer Fehlermeldung
     *         und einer zugrundeliegenden Ursache.
     *     </p>
     * </div>
     *
     * @param message die Fehlerbeschreibung (nicht {@code null})
     * @param cause   die zugrundeliegende Ausnahme (nicht {@code null})
     *
     *  @since 1.0.0
     */
    public ProxyBuilderException(final @NonNull String message,
                                 final @NonNull Throwable cause) {
        super(message, cause);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt eine neue {@code ProxyBuilderException} mit einer zugrundeliegenden Ursache
     *         und einer Standardfehlermeldung (die aus der Ursache abgeleitet wird).
     *     </p>
     * </div>
     *
     * @param cause die zugrundeliegende Ausnahme (nicht {@code null})
     *
     * @since 1.0.0
     */
    public ProxyBuilderException(final @NonNull Throwable cause) {
        super(cause);
    }

}
