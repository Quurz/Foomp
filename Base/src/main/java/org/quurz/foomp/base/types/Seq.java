package org.quurz.foomp.base.types;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.higher.Higher1;
import org.quurz.foomp.higher.WitnessType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <div>
 *     <p>
 *         Eine Sequenz von Werten des Typs <code>A</code>. Diese kann Elemente enthalten, leer sein oder
 *         durch Verkettung erweitert werden.
 *     </p>
 * </div>
 *
 * @param <A> Typ der Elemente
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@SuppressWarnings("NonAsciiCharacters")
public interface Seq<A>
        extends Container<A>,
                Value<A>,
                Streamable<A>,
                Iterable<A>,
                Higher1<Seq.µ, A> {

    /**
     * <div>
     *     <p>
     *         Markerklasse zur Typisierung von {@link WitnessType} f&uuml;r <code>Seq</code>.
     *     </p>
     * </div>
     *
     * @since 1.0.0
     */
    class µ implements WitnessType { protected µ() {} }

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob diese Sequenz nicht leer ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls diese Sequenz mindestens ein Element enth&auml;lt, <code>false</code> andernfalls
     *
     * @since 1.0.0
     */
    boolean isNotEmpty();

    /**
     * <div>
     *     <p>
     *         Pr&uuml;ft, ob diese Sequenz leer ist.
     *     </p>
     * </div>
     *
     * @return <code>true</code>, falls diese Sequenz keine Elemente enth&auml;lt, <code>false</code> andernfalls
     *
     * @since 1.0.0
     */
    default boolean isEmpty() {
        return !this.isNotEmpty();
    }

    /**
     * <div>
     *     <p>
     *         Gibt das erste Element der Sequenz zur&uuml;ck. Vergleichbar mit Lisps <code>car</code>.
     *     </p>
     * </div>
     *
     * @return Der Kopf der Sequenz
     *
     * @throws NoSuchElementException falls die Sequenz leer ist
     *
     * @since 1.0.0
     */
    @NonNull A head()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Gibt die restlichen Elemente der Sequenz zur&uuml;ck, ohne das erste Element.
     *     </p>
     * </div>
     *
     * @return Die restliche Sequenz ohne das erste Element
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> tail();

    /**
     * <div>
     *     <p>
     *         F&uuml;gt ein Element am Ende der Sequenz hinzu.
     *     </p>
     * </div>
     *
     * @param element Das hinzuzuf&uuml;gende Element
     * @return Eine neue Sequenz mit dem Element als Kopf
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> cons(final @NonNull A element);

    /**
     * <div>
     *     <p>
     *         F&uuml;gt eine weitere Sequenz von Elementen am Ende dieser Sequenz an.
     *     </p>
     * </div>
     *
     * @param other Die hinzuzuf&uuml;gende Sequenz
     * @return Eine neue Sequenz mit den hinzugef&uuml;gten Elementen
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> consAll(final @NonNull Higher1<? extends Seq.µ, A> other);

    /**
     * <div>
     *     <p>
     *         Zerlegt die Sequenz in ihren Kopf und den Rest.
     *     </p>
     * </div>
     *
     * @return Ein Tupel aus dem ersten Element und der restlichen Sequenz
     *
     * @throws NoSuchElementException falls die Sequenz leer ist
     *
     * @since 1.0.0
     */
    @NonNull Value2<A, ? extends Seq<A>> decons()
        throws NoSuchElementException;

    /**
     * <div>
     *     <p>
     *         Teilt diese Sequenz in zwei Teile anhand eines Pr&auml;dikats.
     *     </p>
     * </div>
     *
     * @param predicate Das Pr&auml;dikat zur Teilung der Sequenz
     * @return Ein Tupel aus den passenden und nicht passenden Elementen
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> split(final @NonNull Predicate<? super A> predicate);

    /**
     * <div>
     *     <p>
     *         Teilt die Sequenz nach einer internen Regel, falls m&ouml;glich.
     *     </p>
     * </div>
     *
     * @return Ein Tupel aus zwei Teilsequenzen
     *
     * @throws IllegalStateException falls eine Teilung nicht m&ouml;glich ist
     *
     * @since 1.0.0
     */
    @NonNull Value2<? extends Seq<A>, ? extends Seq<A>> split()
            throws IllegalStateException;

    /**
     * <div>
     *     <p>
     *         Filtert die Sequenz basierend auf einem Pr&auml;dikat.
     *     </p>
     * </div>
     *
     * @param pred Das Filterpr&auml;dikat
     * @return Eine neue Sequenz mit nur den passenden Elementen
     *
     * @since 1.0.0
     */
    @NonNull Seq<A> filter(final @NonNull Predicate<? super A> pred);

    /**
     * <div>
     *     <p>
     *         Wandelt die Sequenz in eine Collection um.
     *     </p>
     * </div>
     *
     * @param init Ein Lieferant f&uuml;r die zu bef&uuml;llende Collection
     * @return Eine Sammlung mit den Elementen dieser Sequenz
     *
     * @since 1.0.0
     */
    @NonNull Collection<A> toCollection(final @NonNull Supplier<Collection<A>> init);

}
