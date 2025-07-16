package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable;
import org.quurz.foomp.base.types.XorValue;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.noValuePresent;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine funktionale Schnittstelle, die eine Operation auf einem Eingabewert ausf&uuml;hrt und ein Ergebnis zur&uuml;ckgibt.
 *         Diese Schnittstelle stellt sicher, dass die ausgef&uuml;hrte Funktion Fehler sicher behandeln kann.
 *     </P>
 * </div>
 *
 * @param <X> der Eingabetyp
 * @param <Y> der R&uuml;ckgabetyp
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Applicable<X, Y>
        extends Deferrable<X, Y> {

    /**
     * <div>
     *     <p>
     *         Erstellt eine {@code Applicable}-Instanz aus einer gegebenen {@link Function}.
     *         Die resultierende Funktion ist in der Lage, die Eingabe zu verarbeiten und das Ergebnis zur&uuml;ckzugeben.
     *     </p>
     * </div>
     *
     * @param <X> der Eingabetyp der Funktion
     * @param <Y> der R&uuml;ckgabetyp der Funktion
     * @param function die Funktion, die die Eingabe verarbeitet; darf nicht {@code null} sein
     * @return eine {@code Applicable}-Instanz, die die gegebene Funktion kapselt
     * @throws NullPointerException falls {@code function} {@code null} ist
     *
     * @since 1.0.0
     */
    static <X, Y> Applicable<X, Y> applicable(final @NonNull Function<X, Y> function) {
        Objects.requireNonNull(function, nullValue("function"));
        return x -> Objects.requireNonNull(function.apply(x), nullResult());
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt die Operation aus und gibt das Ergebnis zur&uuml;ck.
     *     </p>
     * </div>
     *
     * @param x der Eingabewert
     * @return das Ergebnis der Berechnung
     * @throws Exception falls ein Fehler w&auml;hrend der Berechnung auftritt
     *
     * @since 1.0.0
     */
    Y apply(@NonNull final X x)
        throws Exception;

    /**
     * <div>
     *     <p>
     *         Gibt eine neue Funktion zur&uuml;ck, die die gleiche Berechnung wie {@link #apply(Object)} ausf&uuml;hrt,
     *         aber das Ergebnis in einem {@link XorValue} verpackt. Das Ergebnis kann entweder ein Fehler oder der berechnete Wert sein.
     *     </p>
     * </div>
     *
     * @return eine Funktion, die das Ergebnis als {@code XorValue<Exception, Y>} zur&uuml;ckgibt
     *
     * @since 1.0.0
     */
    default Fun<X, ? extends XorValue<Exception, Y>> safe() {
        return x -> {
            try {
                final var result
                    = this.apply(x);
                return new XorValue<>() {
                    @Override
                    public boolean isRight() {
                        return true;
                    }

                    @Override
                    public @NonNull Exception getLeft()
                            throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }

                    @Override
                    public @NonNull Y getRight() {
                        return result;
                    }
                };
            } catch (final Exception exception) {
                return new XorValue<>() {
                    @Override
                    public boolean isRight() {
                        return false;
                    }

                    @Override
                    public @NonNull Exception getLeft() {
                        return exception;
                    }

                    @Override
                    public @NonNull Y getRight()
                            throws NoSuchElementException {
                        throw new NoSuchElementException(noValuePresent());
                    }
                };
            }
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine {@code Callable}-Instanz zur&uuml;ck, die die aktuelle Operation ausf&uuml;hrt,
     *         wenn der Eingabewert von einem {@link Supplier} bereitgestellt wird.
     *     </p>
     * </div>
     *
     * wenn der Eingabewert von einem {@link Supplier} bereitgestellt wird.
     *
     * @param supplier der {@link Supplier}, der den Eingabewert liefert; darf nicht {@code null} sein
     * @return eine {@code Callable}-Instanz, die den berechneten Wert liefert
     * @throws NullPointerException falls {@code supplier} {@code null} ist
     *
     * @since 1.0.0
     */
    @NonNull
    default Callable<Y> defer(final @NonNull Supplier<X> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return () -> {
            final var x
                = Objects.requireNonNull(supplier.get(), nullSupplied());
            return Objects.requireNonNull(apply(x), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Gibt eine Identit&auml;tsfunktion zur&uuml;ck, die den Eingabewert unver&auml;ndert zur&uuml;ckgibt.
     *         Dies ist n&uuml;tzlich, wenn keine Transformation ben&ouml;tigt wird.
     *     </p>
     * </div>
     *
     * @param <X> der Typ des Eingabewerts
     * @return eine Funktion, die den Eingabewert unver&auml;ndert zur&uuml;ckgibt
     *
     * @since 1.0.0
     */
    static <X> Applicable<X, X> identity() {
        return x -> x;
    }

}
