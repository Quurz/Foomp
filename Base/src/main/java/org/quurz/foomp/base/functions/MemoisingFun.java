    package org.quurz.foomp.base.functions;

    import org.checkerframework.checker.nullness.qual.NonNull;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;
    import java.util.function.Function;

    import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
    import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

    /**
     * <div>
     *     <p>
     *         Eine erweiterte funktionale Schnittstelle, die Funktionsaufrufe mit Memoisierung unterst&uuml;tzt,
     *         um wiederholte Berechnungen zu vermeiden und die Leistung zu verbessern.
     *     </p>
     * </div>
     *
     * @param <X> der Eingabetyp der Funktion
     * @param <Y> der Rückgabetyp der Funktion
     *
     * @since 1.0.0
     *
     * @author Alexander Schell
     */
    public interface MemoisingFun<X, Y>
            extends Fun<X, Y> {

        /**
         * <div>
         *     <p>
         *         Erzeugt eine neue {@code MemoisingFun}-Instanz aus einer gegebenen {@link Function}, die
         *         die Ergebnisse vorheriger Aufrufe für identische Eingaben speichert und so wiederholte
         *         Berechnungen vermeidet.
         *     </p>
         * </div>
         *
         * @param <X> der Eingabetyp der Funktion
         * @param <Y> der Rückgabetyp der Funktion
         * @param function die Funktion, die in die memoisierten Funktion umgewandelt wird; darf nicht {@code null} sein
         * @return eine memoisierte Version der angegebenen Funktion
         * @throws NullPointerException falls {@code function} oder deren Resultat {@code null} ist
         *
         * @since 1.0.0
         */
        static <X, Y> MemoisingFun<X, Y> memoisingFun(final @NonNull Function<X, Y> function) {
            Objects.requireNonNull(function, nullValue("function"));

            return new MemoisingFun<>() {
                private final Map<X, Y> memo
                    = new HashMap<>();

                @Override
                public @NonNull Y apply(final @NonNull X x) {
                    Objects.requireNonNull(x, nullValue("x"));
                    return this.memo.computeIfAbsent(x, _x -> Objects.requireNonNull(function.apply(_x), nullResult()));
                }

                @Override
                public MemoisingFun<X, Y> clear() {
                    this.memo.clear();
                    return this;
                }
            };
        }

        /**
         * <div>
         *     <p>
         *         L&ouml;scht alle gespeicherten Ergebnisse der Memoisierung, sodass zuk&uuml;nftige Aufrufe der Funktion
         *         wieder berechnet und nicht aus dem Speicher zur&uuml;ckgegeben werden.
         *     </p>
         * </div>
         *
         * @return die aktuelle {@code MemoisingFun}-Instanz für method chaining
         *
         * @since 1.0.0
         */
        MemoisingFun<X, Y> clear();

    }
