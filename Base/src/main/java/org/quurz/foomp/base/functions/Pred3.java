package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable3;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein dreistelliges Pr&auml;dikat <code>f:A1 &#x2715; A2 &#x2715; A3 &#x21A6; {true, false}</code>
 *     </p>
 * </div>
 *
 * @param <A1> Typ des ersten Arguments
 * @param <A2> Typ des zweiten Arguments
 * @param <A3> Typ des dritten Arguments
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred3<A1, A2, A3>
        extends Deferrable3<A1, A2, A3, Boolean> {

    /**
     * <div>
     *     <p>
     *         Negiert das gegebene <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param pred3 Das zu negierende <code>Pred3</code>
     * @param <A1> Typ des ersten Arguments
     * @param <A2> Typ des zweiten Arguments
     * @param <A3> Typ des dritten Arguments
     * @return Das negierte <code>Pred3</code>
     *
     * @since 1.0.0
     */
    static <A1, A2, A3> Pred3<A1, A2, A3> not(final @NonNull Pred3<? super A1, ? super A2, ? super A3> pred3) {
        Objects.requireNonNull(pred3);
        return (a1, a2, a3) -> !pred3.test(a1, a2, a3);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat f&uuml;r die gegebenen Argumente aus.
     *     </p>
     * </div>
     *
     * @param a1 Das erste Eingabe-Argument
     * @param a2 Das zweite Eingabe-Argument
     * @param a3 Das dritte Eingabe-Argument
     * @return <code>true</code> oder <code>false</code>
     *
     * @since 1.0.0
     */
    boolean test(final @NonNull A1 a1,
                 final @NonNull A2 a2,
                 final @NonNull A3 a3);

    /**
     * <div>
     *     <p>
     *         Negiert dieses <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @return Das negierte <code>Pred3</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> negate() {
        return not(this);
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem anderen <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred3</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) && other.test(a1, a2, a3));
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) && booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> and(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.and(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem anderen <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred3</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.and(other));
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return not(this.and(booleanSupplier));
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nand(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem anderen <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred3</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) || other.test(a1, a2, a3));
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2, a3) -> (this.test(a1, a2, a3) || booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> or(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.or(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem anderen <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred3</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.or(other));
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return not(this.or(booleanSupplier));
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> nor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem anderen <code>Pred3</code>.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred3</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(@NonNull final Pred3<A1, A2, A3> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (this.or(other)).and(not(this.and(other)));
    }


    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(final @NonNull BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, "booleanSupplier");
        return (this.or(booleanSupplier)).and(not(this.and(booleanSupplier)));
    }

    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred3<A1, A2, A3> xor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.xor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat partiell f&uuml;r das erste Argument aus.
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das erste Argument
     * @return Ein Pr&auml;dikat <code>f:A2 &#x2715; A3 &#x21A6; {true, false}</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A2, A3> partial1(@NonNull final Supplier<A1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a2, a3) -> this.test(supplier.get(), a2, a3);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat partiell f&uuml;r das zweite Argument aus.
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das zweite Argument
     * @return Ein Pr&auml;dikat <code>f:A1 &#x2715; A3 &#x21A6; {true, false}</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A3> partial2(@NonNull final Supplier<A2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a1, a3) -> this.test(a1, supplier.get(), a3);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat partiell f&uuml;r das dritte Argument aus.
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das dritte Argument
     * @return Ein Pr&auml;dikat <code>f:A1 &#x2715; A2 &#x21A6; {true, false}</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> partial3(@NonNull final Supplier<A3> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return (a1, a2) -> this.test(a1, a2, supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Verz&ouml;gert die Auswertung des Pr&auml;dikats, bis alle drei Argumente
     *         durch die angegebenen {@link Supplier} bereitgestellt werden.
     *     </p>
     * </div>
     *
     * @param supplier1 Lieferant f&uuml;r das erste Argument
     * @param supplier2 Lieferant f&uuml;r das zweite Argument
     * @param supplier3 Lieferant f&uuml;r das dritte Argument
     * @return Ein {@link Callable}, das das Pr&auml;dikat auswertet, sobald es aufgerufen wird.
     * @throws NullPointerException Wenn einer der {@link Supplier} oder deren bereitgestellter Wert <code>null</code> ist.
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Boolean> defer(final @NonNull Supplier<A1> supplier1,
                                    final @NonNull Supplier<A2> supplier2,
                                    final @NonNull Supplier<A3> supplier3) {
        Objects.requireNonNull(supplier1, nullValue("supplier1"));
        Objects.requireNonNull(supplier2, nullValue("supplier2"));
        Objects.requireNonNull(supplier3, nullValue("supplier3"));
        return () -> this.test(
                        Objects.requireNonNull(supplier1.get(), nullSupplied()),
                        Objects.requireNonNull(supplier2.get(), nullSupplied()),
                        Objects.requireNonNull(supplier3.get(), nullSupplied())
                     );
    }

}
