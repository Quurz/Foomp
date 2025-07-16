package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable2;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein zweistelliges Pr&auml;dikat <code>f:A1 &#x2715; A2 &#x21A6; {true, false}</code>
 *     </p>
 * </div>
 *
 * @see BiPredicate
 *
 * @param <A1> Typ des ersten Arguments
 * @param <A2> Typ des zweiten Arguments
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred2<A1, A2>
        extends Deferrable2<A1, A2, Boolean>,
                BiPredicate<A1, A2> {

    /**
     * <div>
     *     <p>
     *         Verpackt das gegebene <code>{@link BiPredicate}</code> in ein <code>Pred</code>
     *     </p>
     * </div>
     *
     * @param biPredicate Das einzupackende <code>BiPredicate</code>
     * @param <A1> Typ des ersten Arguments
     * @param <A2> Typ des zweiten Arguments
     * @return Das neue <code>Pred2</code>
     *
     * @throws NullPointerException Falls <code>function &#61;&#61; null</code>
     *
     * @since 1.0.0
     */
    static <A1, A2> Pred2<A1, A2> pred2(@NonNull final BiPredicate<? super A1, ? super A2> biPredicate) {
        Objects.requireNonNull(biPredicate, nullValue("biPredicate"));
        return biPredicate::test;
    }

    /**
     * <div>
     *     <P>
     *         Negiert das gegebene <code>Pred2</code>
     *     </P>
     * </div>
     *
     * @param pred2 Das zu negierende <code>BiPredicate</code>
     * @param <A1> Typ des ersten Arguments
     * @param <A2> Typ des zweiten Arguments
     * @return Das negierte <code>Pred</code>
     *
     * @since 1.0.0
     */
    static <A1, A2> Pred2<A1, A2> not(@NonNull final Pred2<? super A1, ? super A2> pred2) {
        Objects.requireNonNull(pred2, nullValue("pred2"));
        return (a1, a2) -> !pred2.test(a1, a2);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat f&uuml;r die gegebenen Argumente aus
     *     </p>
     * </div>
     *
     * @param a1 Das erste Eingabe-Argument
     * @param a2 Das zweite Eingabe-Argument
     * @return <code>true</code> oder <code>false</code>
     *
     * @since 1.0.0
     */
    @Override
    boolean test(final @NonNull A1 a1,
                 final @NonNull A2 a2);

    /**
     * <div>
     *     <p>
     *         Negiert dieses <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @return Das negierte <code>Pred2</code>
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Pred2<A1, A2> negate() {
        return not(this);
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem anderen <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred2</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2) -> (this.test(a1, a2) && other.test(a1, a2));
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code>
     *     </p>
     * </div>
     *
     * @param booleanSupplier Der <code>BooleanSupplier</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final BooleanSupplier booleanSupplier) {
        Objects.requireNonNull(booleanSupplier, nullValue("booleanSupplier"));
        return (a1, a2) -> (this.test(a1, a2) && booleanSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         AND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolen&gt;</code>
     *     </p>
     * </div>
     *
     * @param supplier <code>Supplier&lt;Boolen&gt;</code>
     * @return Die AND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> and(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.and(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem anderen <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred2</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.and(other));
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem <code>BoolSupplier</code>
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BoolSupplier</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.and(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         NAND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolen&gt;</code>
     *     </p>
     * </div>
     *
     * @param supplier <code>Supplier&lt;Boolen&gt;</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nand(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem anderen <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred2</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (a1, a2) -> (this.test(a1, a2) || other.test(a1, a2));
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem <code>BoolSupplier</code>
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BoolSupplier</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (a1, a2) -> (this.test(a1, a2) || boolSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         OR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolen&gt;</code>
     *     </p>
     * </div>
     *
     * @param supplier <code>Supplier&lt;Boolen&gt;</code>
     * @return Die OR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> or(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.or(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem anderen <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred2</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.or(other));
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem <code>BoolSupplier</code>
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BoolSupplier</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.or(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         NOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolen&gt;</code>
     *     </p>
     * </div>
     *
     * @param supplier <code>Supplier&lt;Boolen&gt;</code>
     * @return Die NOR-Verkn&uuml;pfung
     */
    @NonNull
    default Pred2<A1, A2> nor(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem anderen <code>Pred2</code>
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred2</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final Pred2<A1, A2> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (this.or(other)).and(not(this.and(other)));
    }

    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem <code>BoolSupplier</code>
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BoolSupplier</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (this.or(boolSupplier)).and(not(this.and(boolSupplier)));
    }

    /**
     * <div>
     *     <p>
     *         XOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolen&gt;</code>
     *     </p>
     * </div>
     *
     * @param supplier <code>Supplier&lt;Boolen&gt;</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred2<A1, A2> xor(@NonNull final Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.xor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat partiell f&uuml;r das erste Argument aus
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das erste Argument
     * @return Ein Pr&auml;dikat <code>f:A2 &#x21A6; {true, false}</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A2> partial1(@NonNull final Supplier<A1> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return a2 -> this.test(supplier.get(), a2);
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat partiell f&uuml;r das zweite Argument aus
     *     </p>
     * </div>
     *
     * @param supplier Lieferant f&uuml;r das zweite Argument
     * @return Eine Funktion <code>f:A1 &#x21A6; {true, false}</code>
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A1> partial2(@NonNull final Supplier<A2> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return a1 -> this.test(a1, supplier.get());
    }

    /**
     * <div>
     *     <p>
     *         Verz&ouml;gert die Auswertung des Pr&auml;dikats, bis beide Argumente
     *         durch die angegebenen {@link Supplier} bereitgestellt werden.
     *     </p>
     * </div>
     *
     * @param supplier1 Lieferant f&uuml;r das erste Argument
     * @param supplier2 Lieferant f&uuml;r das zweite Argument
     * @return Ein {@link Callable}, das das Pr&auml;dikat auswertet, sobald es aufgerufen wird.
     * @throws NullPointerException Wenn einer der {@link Supplier} oder deren bereitgestellter Wert <code>null</code> ist.
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Boolean> defer(final @NonNull Supplier<A1> supplier1,
                                    final @NonNull Supplier<A2> supplier2) {
        Objects.requireNonNull(supplier1, nullValue("supplier1"));
        Objects.requireNonNull(supplier2, nullValue("supplier2"));
        return () -> this.test(
                        Objects.requireNonNull(supplier1.get(), nullSupplied()),
                        Objects.requireNonNull(supplier2.get(), nullSupplied())
                     );
    }

}
