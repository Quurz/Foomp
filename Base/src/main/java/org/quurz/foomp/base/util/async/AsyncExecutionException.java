package org.quurz.foomp.base.util.async;

import lombok.Getter;

/**
 * <div>
 *     <p>
 *         Eine Ausnahme, die auftritt, wenn eine asynchrone Berechnung fehlschl&auml;gt.
 *         Diese Ausnahme erweitert {@link RuntimeException} und f&uuml;gt zus&auml;tzliche Informationen
 *         &uuml;ber den Namen des Threads hinzu, in dem die Ausnahme ausgel&ouml;st wurde.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
// TODO: Brauche ich die wirklich? Immerhin gibt's die ExecutionException.
@Getter
public class AsyncExecutionException
        extends RuntimeException {

    /**
     * <div>
     *     <p>
     *         Der Name des Threads, in dem die Ausnahme ausgel&ouml;st wurde.
     *     </p>
     * </div>
     */
    protected String threadName;

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code AsyncExecutionException} ohne Detailnachricht.
     *         Der Thread-Name wird automatisch erfasst.
     *     </p>
     * </div>
     */
    public AsyncExecutionException() {
        super();
        this.threadName
            = Thread.currentThread().getName();
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code AsyncExecutionException} mit der angegebenen Detailnachricht.
     *         Der Thread-Name wird automatisch erfasst.
     *     </p>
     * </div>
     *
     * @param message die Detailnachricht.
     */
    public AsyncExecutionException(final String message) {
        super(message);
        this.threadName
            = Thread.currentThread().getName();
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code AsyncExecutionException} mit der angegebenen Detailnachricht
     *         und Ursache. Der Thread-Name wird automatisch erfasst.
     *     </p>
     * </div>
     *
     * @param message die Detailnachricht.
     * @param cause die zugrunde liegende Ursache der Ausnahme.
     */
    public AsyncExecutionException(final String message,
                                   final Throwable cause) {
        super(message, cause);
        this.threadName
            = Thread.currentThread().getName();
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue {@code AsyncExecutionException} mit der angegebenen Ursache.
     *         Der Thread-Name wird automatisch erfasst.
     *     </p>
     * </div>
     *
     * @param cause die zugrunde liegende Ursache der Ausnahme.
     */
    public AsyncExecutionException(final Throwable cause) {
        super(cause);
        this.threadName
            = Thread.currentThread().getName();
    }

}
