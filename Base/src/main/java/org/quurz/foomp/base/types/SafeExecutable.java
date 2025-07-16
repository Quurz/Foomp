package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.localisation.BaseMessages;
import org.quurz.foomp.base.util.Either;

import java.util.NoSuchElementException;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;

/**
 * <div>
 *     <p>
 *         Ein etwas sichereres {@link Executable}
 *     </p>
 * </div>
 *
 * @param <A> Typ des Ergebnisses
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface SafeExecutable<A>
        extends Executable<A> {

    /**
     * <div>
     *     <p>
     *         Sollte die Ausf&uuml;hrung des <code>SafeExecutable</code> eine Exception werfen, wird diese in einem {@link XorValue} verpackt
     *     </p>
     *     <p>
     *         Implementierende Klassen sollten diese Methode passend &uuml;berschreiben. Zum Beispiel mit einem {@link Either}
     *     </p>
     * </div>
     *
     * @return Das in einem <code>XorValue&lt;Exception, A&gt;</code> verpackte Ergebnis - Auf der rechten Seite das m&ouml;gliche Ergebnis, auf der Linken die m&ouml;glicherweise aufgetretene Exception
     *
     * @since 1.0.0
     */
    // TODO: Extends Exception?
    default @NonNull XorValue<? extends Throwable, A> executeSafe() {
        try {
            final var a
                = this.execute();
            return new XorValue<>() {
                @Override
                public boolean isRight() {
                    return true;
                }

                @Override
                public @NonNull Exception getLeft() throws NoSuchElementException {
                    throw new NoSuchElementException(noValuePresent());
                }

                @Override
                public @NonNull A getRight() throws NoSuchElementException {
                    return a;
                }
            };
        } catch (final Throwable exception) {
            return new XorValue<>() {
                @Override
                public boolean isRight() {
                    return false;
                }

                @Override
                public @NonNull Throwable getLeft() throws NoSuchElementException {
                    return exception;
                }

                @Override
                public @NonNull A getRight() throws NoSuchElementException {
                    throw new NoSuchElementException(noValuePresent());
                }
            };
        }
    }

}
