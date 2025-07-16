package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Funktionales Interface, das es einem Objekt erm&ouml;glicht, eine formatierte String-Repr&auml;sentation zu erzeugen.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Echo {

    /**
     * <div>
     *     <p>
     *         Gibt eine formatierte String-Repr&auml;sentation des Objekts zur&uuml;ck, wobei die Standardmethode {@link Object#toString()} verwendet wird.
     *     </p>
     * </div>
     *
     * @return Eine formatierte String-Repr&auml;sentation des Objekts.
     *
     * @since 1.0.0
     */
    @NonNull
    String echo();

    /**
     * <div>
     *     <p>
     *         Gibt eine formatierte String-Repr&auml;sentation des Objekts zur&uuml;ck, wobei eine benutzerdefinierte Funktion verwendet wird, um die Transformation des Objekts in einen String durchzuf&uuml;hren.
     *     </p>
     * </div>
     *
     * @param transformer Eine Funktion, die das Objekt in eine String-Repr&auml;sentation umwandelt.
     *
     * @return Eine formatierte String-Repr&auml;sentation des Objekts.
     *
     * @since 1.0.0
     */
    @NonNull
    default String echo(final @NonNull Function<? super Echo, String> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        return Objects.requireNonNull(transformer.apply(this), nullResult());
    }

}
