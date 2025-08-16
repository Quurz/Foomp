package org.quurz.foomp.plugins;

import java.io.Serial;

/**
 * <div>
 *     <p>
 *         Basislaufzeit-Exception für das Plugin-System.
 *     </p>
 *     <p>
 *         Diese Ausnahme dient als Oberklasse für spezifischere Plugin-bezogene Fehler zur Laufzeit,
 *         z. B. für Lade-, Initialisierungs-, Ausführungs- oder Shutdown-Probleme von Plugins.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class PluginSystemException
        extends RuntimeException {

    @Serial
    private static final long serialVersionUID
        = 23L;

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code PluginSystemException} mit einer Detailnachricht.
     *     </p>
     * </div>
     *
     * @param message die Detailnachricht; sollte das Fehlerbild präzise beschreiben
     * @since 1.0.0
     */
    public PluginSystemException(final String message) {
        super(message);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code PluginSystemException} mit Detailnachricht und Ursache.
     *     </p>
     *     <p>
     *         Verwende diesen Konstruktor, um die ursprüngliche Ursache zu verketten
     *         (Exception Chaining), sodass die Fehlerursache nachvollziehbar bleibt.
     *     </p>
     * </div>
     *
     * @param message die Detailnachricht
     * @param cause   die auslösende Ursache (kann {@code null} sein)
     *
     * @since 1.0.0
     */
    public PluginSystemException(final String message,
                                 final Throwable cause) {
        super(message, cause);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code PluginSystemException} mit der angegebenen Ursache.
     *     </p>
     *     <p>
     *         Praktisch, wenn keine zusätzliche Nachricht erforderlich ist und die Ursache
     *         die Fehlersituation ausreichend beschreibt.
     *     </p>
     * </div>
     *
     * @param cause die auslösende Ursache (kann {@code null} sein)
     *
     * @since 1.0.0
     */
    public PluginSystemException(final Throwable cause) {
        super(cause);
    }

}
