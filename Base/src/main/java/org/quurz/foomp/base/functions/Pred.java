package org.quurz.foomp.base.functions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.types.Deferrable;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.quurz.foomp.base.localisation.BaseMessages.nullSupplied;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Eine funktionale Schnittstelle, die eine Bedingung f&uuml;r den Typ {@code A} beschreibt.
 *     </p>
 *     <p>
 *         Diese Schnittstelle erweitert die Funktionalit&auml;t der Standard {@code Predicate<A>}
 *         Schnittstelle in Java und bietet zus&auml;tzliche logische Operationen wie
 *         NAND, NOR, und XOR. Sie unterst&uuml;tzt auch eine Zusammensetzung von
 *         Bedingungen durch Methoden wie {@code and}, {@code or}, und {@code not}.
 *     </p>
 * </div>
 *
 * @param <A> der Typ der Eingabewerte, die gepr&uuml;ft werden.
 *
 * @see Predicate
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@FunctionalInterface
public interface Pred<A>
        extends Deferrable<A, Boolean>,
                Predicate<A> {

    /**
     * <div>
     *     <p>
     *         Erzeugt ein Pr&auml;dikat, das immer {@code true} zur&uuml;ckgibt, unabh&auml;ngig vom Eingabewert.
     *     </p>
     *     <p>
     *         Das Pr&auml;dikat &uuml;berpr&uuml;ft zun&auml;chst, ob der &uuml;bergebene Wert {@code null} ist, und l&ouml;st
     *         in diesem Fall eine {@link NullPointerException} aus.
     *     </p>
     * </div>
     *
     * Erzeugt ein Pr&auml;dikat, das immer {@code true} zur&uuml;ckgibt, unabh&auml;ngig vom Eingabewert.
     * <p>
     *
     * in diesem Fall eine {@link NullPointerException} aus.
     *
     * @param <A> der Typ des Eingabewerts, den das Pr&auml;dikat akzeptiert
     * @return ein Pr&auml;dikat, das immer {@code true} zur&uuml;ckgibt
     * @throws NullPointerException wenn der Eingabewert {@code null} ist
     *
     * @since 1.0.0
     */
    static <A> Pred<A> alwaysTrue() {
        return object -> {
            Objects.requireNonNull(object, nullValue("object"));
            return true;
        };
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein Pr&auml;dikat, das immer {@code false} zur&uuml;ckgibt, unabh&auml;ngig vom Eingabewert.
     *     </p>
     *     <p>
     *         Das Pr&auml;dikat &uuml;berpr&uuml;ft zun&auml;chst, ob der &uuml;bergebene Wert {@code null} ist, und l&ouml;st
     *         in diesem Fall eine {@link NullPointerException} aus.
     *     </p>
     * </div>
     *
     * @param <A> der Typ des Eingabewerts, den das Pr&auml;dikat akzeptiert
     * @return ein Pr&auml;dikat, das immer {@code false} zur&uuml;ckgibt
     * @throws NullPointerException wenn der Eingabewert {@code null} ist
     *
     * @since 1.0.0
     */
    static <A>Pred<A> alwaysFalse() {
        return object -> {
            Objects.requireNonNull(object, nullValue("object"));
            return false;
        };
    }

    /**
     * <div>
     *     <p>
     *         Verpackt das gegebene <code>{@link Predicate}</code> in ein <code>Pred</code>.
     *     </p>
     * </div>
     *
     * @param predicate Das einzupackende <code>Predicate</code>
     * @param <A> Typ des Arguments
     * @return Das neue <code>Pred</code>
     *
     * @throws NullPointerException Wenn <code>predicate &#61;&#61; null</code>
     *
     * @since 1.0.0
     */
    static <A> Pred<A> pred(@NonNull final Predicate<A> predicate) {
        Objects.requireNonNull(predicate);
        return predicate::test;
    }

    /**
     * <div>
     *     <p>
     *         Negiert das gegebene <code>Pred</code>.
     *     </p>
     * </div>
     *
     * @param predicate Das zu negierende <code>Pred</code>
     * @param <A> Typ des Arguments
     * @return Das negierte <code>Pred</code>
     *
     * @since 1.0.0
     */
    static <A> Pred<A> not(@NonNull final Predicate<A> predicate) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        return a -> !predicate.test(a);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein <code>Pred</code> aus der UND-Verkn&uuml;pfung der gegebenen Pr&auml;dikate.
     *     </p>
     * </div>
     *
     * @param first Das erste Pr&auml;dikat
     * @param second Das zweite Pr&auml;dikat
     * @param others Weitere Pr&auml;dikate
     * @return Das Ergebnis der UND-Verkn&uuml;pfung
     * @param <A> Typ des Pr&auml;dikats
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> and(final @NonNull Pred<A> first,
                           final @NonNull Pred<A> second,
                           final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.and(second);
        for (final var pred : others) {
            result
                = result.and(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein <code>Pred</code> aus der ODER-Verkn&uuml;pfung der gegebenen Pr&auml;dikate.
     *     </p>
     * </div>
     *
     * @param first Das erste Pr&auml;dikat
     * @param second Das zweite Pr&auml;dikat
     * @param others Weitere Pr&auml;dikate
     * @return Das Ergebnis der ODER-Verkn&uuml;pfung
     * @param <A> Typ des Pr&auml;dikats
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> or(final @NonNull Pred<A> first,
                          final @NonNull Pred<A> second,
                          final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("first"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.or(second);
        for (final var pred : others) {
            result
                = result.or(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein <code>Pred</code> aus der exklusiven ODER-Verkn&uuml;pfung der gegebenen Pr&auml;dikate.
     *     </p>
     * </div>
     *
     * @param first Das erste Pr&auml;dikat
     * @param second Das zweite Pr&auml;dikat
     * @param others Weitere Pr&auml;dikate
     * @return Das Ergebnis der exklusiven ODER-Verkn&uuml;pfung
     * @param <A> Typ des Pr&auml;dikats
     *
     * @since 1.0.0
     */
    @SafeVarargs
    static <A> Pred<A> xor(final @NonNull Pred<A> first,
                           final @NonNull Pred<A> second,
                           final @NonNull Pred<A>... others) {
        Objects.requireNonNull(first, nullValue("value1"));
        Objects.requireNonNull(first, nullValue("second"));
        Objects.requireNonNull(others, nullValue("others"));
        var result
            = first.xor(second);
        for (final var pred : others) {
            result
                = result.xor(pred);
        }
        return result;
    }

    /**
     * <div>
     *     <p>
     *         Wertet das Pr&auml;dikat f&uuml;r das gegebene Argument aus.
     *     </p>
     * </div>
     *
     * @param a Das Eingabe-Argument
     * @return <code>true</code> oder <code>false</code>
     *
     * @since 1.0.0
     */
    @Override
    boolean test(final @NonNull A a);

    /**
     * <div>
     *     <p>
     *         Negiert dieses <code>Pred</code>.
     *     </p>
     * </div>
     *
     * @return Das negierte <code>Pred</code>
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Pred<A> negate() {
        return not(this);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine UND-Verkn&uuml;pfung mit einem anderen <code>Pred</code> durch.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred</code>
     * @return Die UND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> and(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return a -> (this.test(a) && other.test(a));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine UND-Verkn&uuml;pfung mit einem <code>BoolSupplier</code> durch.
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BoolSupplier</code>
     * @return Die UND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> and(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return a -> (this.test(a) && boolSupplier.getAsBoolean());
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine UND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code> durch.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die UND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> and(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.and(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine NAND-Verkn&uuml;pfung mit einem anderen <code>Pred</code> durch.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nand(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.and(other));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine NAND-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code> durch.
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die NAND-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nand(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.and(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine ODER-Verkn&uuml;pfung mit einem anderen <code>Pred</code> durch.
     *     </p>
     * </div>
     *
     * @param supplier Das zu verkn&uuml;pfende <code>Pred</code>
     * @return Die ODER-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nand(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nand(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine ODER-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code> durch.
     *     </p>
     * </div>
     *
     * @param other Der <code>BooleanSupplier</code>
     * @return Die ODER-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> or(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return a -> (this.test(a) || other.test(a));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine ODER-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code> durch.
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die ODER-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> or(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return a -> (this.test(a) || boolSupplier.getAsBoolean());
    }

    @NonNull
    default Pred<A> or(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
            = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.or(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine NOR-Verkn&uuml;pfung mit einem anderen <code>Pred</code> durch.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return not(this.or(other));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine NOR-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code> durch.
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BooleanSupplier</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return not(this.or(boolSupplier));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine NOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code> durch.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die NOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> nor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        final BooleanSupplier boolSupplier
                = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.nor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine XOR-Verkn&uuml;pfung mit einem anderen <code>Pred</code> durch.
     *     </p>
     * </div>
     *
     * @param other Das zu verkn&uuml;pfende <code>Pred</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> xor(final @NonNull Pred<A> other) {
        Objects.requireNonNull(other, nullValue("other"));
        return (this.or(other)).and(not(this.and(other)));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine XOR-Verkn&uuml;pfung mit einem <code>BooleanSupplier</code> durch.
     *     </p>
     * </div>
     *
     * @param boolSupplier Der <code>BooleanSupplier</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> xor(final @NonNull BooleanSupplier boolSupplier) {
        Objects.requireNonNull(boolSupplier, nullValue("boolSupplier"));
        return (this.or(boolSupplier)).and(not(this.and(boolSupplier)));
    }

    /**
     * <div>
     *     <p>
     *         F&uuml;hrt eine XOR-Verkn&uuml;pfung mit einem <code>Supplier&lt;Boolean&gt;</code> durch.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;Boolean&gt;</code>
     * @return Die XOR-Verkn&uuml;pfung
     *
     * @since 1.0.0
     */
    @NonNull
    default Pred<A> xor(final @NonNull Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier);
        final BooleanSupplier boolSupplier
            = () -> Objects.requireNonNull(supplier.get(), nullSupplied());
        return this.xor(boolSupplier);
    }

    /**
     * <div>
     *     <p>
     *         Verz&ouml;gert die Auswertung des Pr&auml;dikats bis zur Ausf&uuml;hrung des <code>BooleanSupplier</code>.
     *     </p>
     * </div>
     *
     * @param supplier Der <code>Supplier&lt;A&gt;</code>
     * @return Ein <code>Callable</code>, der das Pr&auml;dikat auswertet
     *
     * @since 1.0.0
     */
    @Override
    @NonNull
    default Callable<Boolean> defer(final @NonNull Supplier<A> supplier) {
        Objects.requireNonNull(supplier, nullValue("supplier"));
        return () -> this.test(Objects.requireNonNull(supplier.get(), nullSupplied()));
    }

}
