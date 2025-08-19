package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     Creates a decision node that routes evaluation based on the given predicate:
 *     if the predicate evaluates to {@code true} for the provided fact, the {@code yesTree} is used,
 *     otherwise the {@code noTree}.
 *   </p>
 *   <p>
 *     Variance:
 *     <ul>
 *       <li>{@code Predicate<? super F>} allows predicates defined for supertypes of {@code F}.</li>
 *       <li>{@code DecisionTree<F, ? extends R>} allows subtrees that produce subtypes of {@code R}.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Error handling: any exception thrown by the predicate or by evaluating a subtree is propagated.
 *   </p>
 * </div>

 *
 * @param <F> the type of facts that this decision tree processes
 * @param <R> the type of results that this decision tree produces
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public sealed interface DecisionTree<F, R>
        permits DecisionTree.Node,
                DecisionTree.Leaf {

    /**
     * <div>
     *   <p>
     *     Creates a decision node that routes evaluation based on the given predicate:
     *     if the predicate evaluates to {@code true} for the provided fact, the {@code yesTree} is evaluated;
     *     otherwise the {@code noTree}.
     *   </p>
     *   <p>
     *     Variance:
     *     <ul>
     *       <li>{@code Predicate<? super F>} allows predicates defined for supertypes of {@code F}.</li>
     *       <li>{@code DecisionTree<F, ? extends R>} allows subtrees that produce subtypes of {@code R}.</li>
     *     </ul>
     *   </p>
     *   <p>
     *     Error handling:
     *     <ul>
     *       <li>Throws {@link NullPointerException} if any argument is {@code null}.</li>
     *       <li>Any exception thrown by {@code predicate} or during evaluation of a subtree is propagated.</li>
     *     </ul>
     *   </p>
     *   <p>
     *     Usage example:
     *   </p>
     *   <pre>{@code
     *   DecisionTree<Integer, String> tree =
     *       DecisionTree.decisionTree(
     *           i -> i < 0,
     *           DecisionTree.decisionTree(_$ -> "NEG"),
     *           DecisionTree.decisionTree(_$ -> "NON-NEG")
     *       );
     *   }</pre>
     * </div>

     *
     * @param predicate the condition to evaluate for branching
     * @param yesTree   the subtree to use when the predicate evaluates to true
     * @param noTree    the subtree to use when the predicate evaluates to false
     * @param <F>       the type of facts that this decision tree processes
     * @param <R>       the type of results that this decision tree produces
     * @return a new decision tree node with the specified predicate and subtrees
     * @throws NullPointerException if any parameter is null
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionTree(final @NonNull Predicate<? super F> predicate,
                                                  final @NonNull DecisionTree<F, ? extends R> yesTree,
                                                  final @NonNull DecisionTree<F, ? extends R> noTree) {
        Objects.requireNonNull(predicate, nullValue("predicate"));
        Objects.requireNonNull(yesTree, nullValue("yesTree"));
        Objects.requireNonNull(noTree, nullValue("noTree"));
        return new Node<>(predicate, yesTree, noTree);
    }

    /**
     * <div>
     *   <p>
     *     Creates a leaf node that computes the final result using the given transformer function.
     *     The transformer is applied to the fact when this leaf is reached during evaluation.
     *   </p>
     *   <p>
     *     Variance:
     *     <ul>
     *       <li>{@code Function<? super F, ? extends R>} allows consuming supertypes of {@code F}
     *           and producing subtypes of {@code R}.</li>
     *     </ul>
     *   </p>
     *   <p>
     *     Error handling:
     *     <ul>
     *       <li>Throws {@link NullPointerException} if {@code transformer} is {@code null}.</li>
     *       <li>Any exception thrown by {@code transformer} is propagated as-is.</li>
     *       <li>If {@code transformer} returns {@code null}, {@link #examine(Object)} will throw
     *           a {@link NullPointerException}.</li>
     *     </ul>
     *   </p>
     *   <p>
     *     Usage example:
     *   </p>
     *   <pre>{@code
     *   DecisionTree<String, Integer> leaf =
     *       DecisionTree.decisionTree(String::length); // returns the length of the input
     *
     *   int len = leaf.examine("abc"); // 3
     *   }</pre>
     * </div>

     *
     * @param transformer the function that transforms facts into results
     * @param <F>         the type of facts that this decision tree processes
     * @param <R>         the type of results that this decision tree produces
     * @return a new decision tree leaf with the specified transformer
     * @throws NullPointerException if the transformer is null
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionTree(final @NonNull Function<? super F, ? extends R> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        return new Leaf<>(transformer);
    }

    /**
     * <div>
     *   <p>
     *     Evaluates this decision tree with the provided fact. For decision nodes,
     *     the {@code predicate} determines the branch to follow; for leaves, the {@code transformer}
     *     computes the final result.
     *   </p>
     *   <p>
     *     Error handling:
     *     <ul>
     *       <li>Throws {@link NullPointerException} if {@code fact} is {@code null}.</li>
     *       <li>Exceptions raised by predicate/transformer or subtrees are propagated.</li>
     *       <li>If a leaf transformer returns {@code null}, a {@link NullPointerException} is thrown.</li>
     *     </ul>
     *   </p>
     *   <p>
     *     Example:
     *   </p>
     *   <pre>{@code
     *   DecisionTree<Integer, String> tree =
     *       DecisionTree.decisionTree(
     *           i -> i < 0,
     *           DecisionTree.decisionTree(_$ -> "NEG"),
     *           DecisionTree.decisionTree(
     *               i -> i > 0,
     *               DecisionTree.decisionTree(_$ -> "POS"),
     *               DecisionTree.decisionTree(_$ -> "ZERO")
     *           )
     *       );
     *
     *   tree.examine(-1); // "NEG"
     *   tree.examine( 1); // "POS"
     *   tree.examine( 0); // "ZERO"
     *   }</pre>
     * </div>

     *
     * @param fact the input fact to examine
     * @return the result produced by examining the fact
     * @throws NullPointerException if the fact is null or if a transformer returns null
     *
     * @since 1.0.0
     */
    default R examine(final @NonNull F fact) {
        Objects.requireNonNull(fact, nullValue("fact"));
        return switch (this) {
            case DecisionTree.Node<F, R> node -> node.predicate.test(fact)
                                                    ? node.yesTree.examine(fact)
                                                    : node.noTree.examine(fact);
            case DecisionTree.Leaf<F, R> leaf -> Objects.requireNonNull(leaf.transformer.apply(fact), nullResultFrom("transformer"));
        };
    }

    /**
     * <div>
     *     <p>
     *         A decision node in the tree that contains a predicate and two subtrees.
     *     </p>
     * </div>
     *
     * @param predicate the condition to evaluate for branching
     * @param yesTree   the subtree to use when the predicate evaluates to true
     * @param noTree    the subtree to use when the predicate evaluates to false
     * @param <F>       the type of facts that this node processes
     * @param <R>       the type of results that this node produces
     */
    record Node<F, R>(Predicate<? super F> predicate,
                      DecisionTree<F, ? extends R> yesTree,
                      DecisionTree<F, ? extends R> noTree) implements DecisionTree<F, R> {
    }

    /**
     * <div>
     *     <p>
     *         A leaf node in the tree that contains a transformer function.
     *     </p>
     * </div>
     *
     * @param transformer the function that transforms facts into results
     * @param <F>         the type of facts that this leaf processes
     * @param <R>         the type of results that this leaf produces
     */
    record Leaf<F, R>(Function<? super F, ? extends R> transformer)
            implements DecisionTree<F, R> { }

}
