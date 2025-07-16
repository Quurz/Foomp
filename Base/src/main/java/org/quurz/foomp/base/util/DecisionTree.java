package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResult;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Ein Entscheidungsbaum, der bin&auml;re Entscheidungen trifft und dabei durch eine Reihe von Bedingungen
 *         verschiedene Zweige verfolgt. Jeder Knoten im Baum enth&auml;lt eine Bedingung und zwei Unterb&auml;ume.
 *         Erreicht der Baum ein Blatt, wird das finale Ergebnis basierend auf einer Berechnungsfunktion
 *         geliefert.
 *     </p>
 * </div>
 *
 * @param <F> Typ des zu pr&uuml;fenden Faktums
 * @param <R> Typ des Ergebnisses
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface DecisionTree<F, R>
        permits DecisionTree.DecisionTreeNode,
                DecisionTree.DecisionTreeLeaf{

    /**
     * <div>
     *     <p>
     *         Erzeugt einen Entscheidungsknoten, der basierend auf der angegebenen Bedingung entscheidet,
     *         welchen Zweig er weiterverfolgt.
     *     </p>
     * </div>
     *
     * @param predicate Die Bedingung zur Pr&uml;fung des Faktums
     * @param yesTree Der Zweig, der betreten wird, wenn die Bedingung <code>true</code> ergibt
     * @param noTree Der Zweig, der betreten wird, wenn die Bedingung <code>false</code> ergibt
     * @return Ein neuer <code>DecisionTree</code>-Knoten
     * @param <F> Typ des zu pr&uuml;fenden Faktums
     * @param <R> Typ des Ergebnisses
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionTree(final @NonNull Predicate<F> predicate,
                                                  final @NonNull DecisionTree<F, R> yesTree,
                                                  final @NonNull DecisionTree<F, R> noTree) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        Objects.requireNonNull(yesTree, nullValue("yesTree"));
        Objects.requireNonNull(noTree, nullValue("noTree"));
        return new DecisionTreeNode<>(predicate, yesTree, noTree);
    }

    /**
     * <div>
     *     <p>
     *         Erzeugt ein Blatt des Entscheidungsbaums, das eine Funktion enth&auml;lt, um ein finales Ergebnis zu berechnen.
     *     </p>
     * </div>
     *
     * @param transformer Funktion, die das Ergebnis der Pr&uuml;fung berechnet
     * @return Ein neues Blatt im <code>DecisionTree</code>
     * @param <F> Typ des zu prüfenden Faktums
     * @param <R> Typ des Ergebnisses
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionTree(final @NonNull Function<F, R> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        return new DecisionTreeLeaf<>(transformer);
    }

    /**
     * <div>
     *     <p>
     *         Startet die Pr&uuml;fung, indem der Entscheidungsbaum durchlaufen wird. Die Entscheidung wird
     *         basierend auf dem gegebenen Faktum und den Bedingungen in den Knoten getroffen.
     *     </p>
     * </div>
     *
     * @param fact Das zu pr&uuml;fende Faktum
     * @return Das Ergebnis der Pr&uuml;fung
     *
     * @since 1.0.0
     */
    default R examine(final @NonNull F fact) {
        Objects.requireNonNull(fact, nullValue("fact"));
        return switch (this) {
            case DecisionTreeNode<F, R> node -> node.predicate.test(fact)
                                                    ? node.yesTree.examine(fact)
                                                    : node.noTree.examine(fact);
            case DecisionTreeLeaf<F, R> leaf -> Objects.requireNonNull(leaf.mapper.apply(fact), nullResult());
        };
    }

    /**
     * <div>
     *     <p>
     *         Ein Knoten des <code>DecisionTree</code>s, der eine Bedingung und zwei Unterbäume enth&auml;lt:
     *         einen, wenn die Bedingung zu <code>true</code> auswertet, und einen, wenn sie zu <code>false</code> auswertet.
     *     </p>
     * </div>
     *
     * @param <F> Typ des Faktums
     * @param <R> Typ des Ergebnisses
     *
     * @since 1.0.0
     */
    final class DecisionTreeNode<F, R>
            implements DecisionTree<F, R> {

        private final Predicate<F> predicate;
        private final DecisionTree<F, R> yesTree;
        private final DecisionTree<F, R> noTree;

        private DecisionTreeNode(final Predicate<F> predicate,
                                 final DecisionTree<F, R> yesTree,
                                 final DecisionTree<F, R> noTree) {
            this.predicate
                = predicate;
            this.yesTree
                = yesTree;
            this.noTree
                = noTree;
        }

    }

    /**
     * <div>
     *     <p>
     *         Ein Blatt des <code>DecisionTree</code>s, das nur eine Mapping-Funktion von Faktum zu Ergebnis enth&auml;lt.
     *     </p>
     * </div>
     *
     * @param <F> Typ des Faktums
     * @param <R> Typ des Ergebnisses
     *
     * @since 1.0.0
     */
    final class DecisionTreeLeaf<F, R>
            implements DecisionTree<F, R> {

        private final Function<F, R> mapper;

        private DecisionTreeLeaf(final Function<F, R> mapper) {
            this.mapper
                = mapper;
        }

    }

}
