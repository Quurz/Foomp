package org.quurz.foomp.base.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *   <p>
 *     A functional decision tree implementation for routing evaluation and producing results based on input facts.
 *     The tree consists of decision nodes (branching) and leaf nodes (final computation and optional side-effects).
 *   </p>
 *   <p>
 *     Variance:
 *     <ul>
 *       <li>{@code Predicate<? super F>} allows predicates defined for supertypes of {@code F}.</li>
 *       <li>{@code DecisionTree<F, ? extends R>} allows subtrees that produce subtypes of {@code R}.</li>
 *     </ul>
 *   </p>
 *   <p>
 *     Error handling: any exception thrown by a predicate, transformer, or consumer is propagated.
 *     If a transformer returns {@code null}, {@link #examine(Object)} will throw a {@link NullPointerException}.
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
                DecisionTree.TransformingLeaf,
                DecisionTree.TransformingAndConsumingLeaf,
                DecisionTree.ConsumingAndTransformingLeaf {

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
     *           DecisionTree.decisionLeaf(_$ -> "NEG"),
     *           DecisionTree.decisionLeaf(_$ -> "NON-NEG")
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
     *       DecisionTree.decisionLeaf(String::length); // returns the length of the input
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
    static <F, R> DecisionTree<F, R> decisionLeaf(final @NonNull Function<? super F, ? extends R> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        return new TransformingLeaf<>(transformer);
    }

    /**
     * <div>
     *   <p>
     *     Creates a leaf node that computes a result using the given transformer
     *     and then applies a side-effect to that result using the given consumer.
     *     <b>Execution order:</b> transformer -&gt; consumer -&gt; return result.
     *   </p>
     *   <p>
     *     This variant is particularly useful when the side-effect (e.g., logging or status updates)
     *     needs to access the computed result of the transformation.
     *   </p>
     *   <p>
     *     Error handling:
     *     <ul>
     *       <li>Throws {@link NullPointerException} if {@code transformer} or {@code consumer} is {@code null}.</li>
     *       <li>Any exception thrown by {@code transformer} or {@code consumer} is propagated as-is.</li>
     *       <li>If {@code transformer} returns {@code null}, {@link #examine(Object)} will throw
     *           a {@link NullPointerException}.</li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param transformer the function that transforms facts into results
     * @param consumer    the consumer that performs a side-effect on the result
     * @param <F>         the type of facts that this decision tree processes
     * @param <R>         the type of results that this decision tree produces
     * @return a new decision tree leaf with the specified transformer and consumer
     * @throws NullPointerException if any argument is null
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionLeaf(final @NonNull Function<? super F, ? extends R> transformer,
                                                  final @NonNull Consumer<? super R> consumer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return new TransformingAndConsumingLeaf<>(transformer, consumer);
    }

    /**
     * <div>
     *   <p>
     *     Creates a leaf node that performs a side-effect using the given consumer
     *     and then computes the final result using the given transformer.
     *     <b>Execution order:</b> consumer -&gt; transformer -&gt; return result.
     *   </p>
     *   <p>
     *     This variant is particularly useful when the side-effect (e.g., input validation logging or
     *     external system notification) needs to happen before the final transformation logic runs.
     *   </p>
     *   <p>
     *     Error handling:
     *     <ul>
     *       <li>Throws {@link NullPointerException} if {@code consumer} or {@code transformer} is {@code null}.</li>
     *       <li>Any exception thrown by {@code consumer} or {@code transformer} is propagated as-is.</li>
     *       <li>If {@code transformer} returns {@code null}, {@link #examine(Object)} will throw
     *           a {@link NullPointerException}.</li>
     *     </ul>
     *   </p>
     * </div>
     *
     * @param consumer    the consumer that performs a side-effect on the fact
     * @param transformer the function that transforms facts into results
     * @param <F>         the type of facts that this decision tree processes
     * @param <R>         the type of results that this decision tree produces
     * @return a new decision tree leaf with the specified consumer and transformer
     * @throws NullPointerException if any argument is null
     *
     * @since 1.0.0
     */
    static <F, R> DecisionTree<F, R> decisionLeaf(final @NonNull Consumer<? super F> consumer,
                                                  final @NonNull Function<? super F, ? extends R> transformer) {
        Objects.requireNonNull(transformer, nullValue("transformer"));
        Objects.requireNonNull(consumer, nullValue("consumer"));
        return new ConsumingAndTransformingLeaf<>(consumer, transformer);
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
     *           DecisionTree.decisionLeaf(_$ -> "NEG"),
     *           DecisionTree.decisionTree(
     *               i -> i > 0,
     *               DecisionTree.decisionLeaf(_$ -> "POS"),
     *               DecisionTree.decisionLeaf(_$ -> "ZERO")
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
            case DecisionTree.Node<F, R> node
                -> node.predicate.test(fact)
                    ? node.yesTree.examine(fact)
                    : node.noTree.examine(fact);
            case DecisionTree.TransformingLeaf<F, R> transformingLeaf
                -> Objects.requireNonNull(transformingLeaf.transformer.apply(fact), nullResultFrom("transformer"));
            case DecisionTree.TransformingAndConsumingLeaf<F, R> transformingAndConsumingLeaf
                -> {
                    final var result
                        = Objects.requireNonNull(transformingAndConsumingLeaf.transformer.apply(fact), nullResultFrom("transformer"));
                    transformingAndConsumingLeaf.consumer.accept(result);
                    yield result;
                }
            case DecisionTree.ConsumingAndTransformingLeaf<F, R> consumingAndTransformingLeaf
                -> {
                    consumingAndTransformingLeaf.consumer.accept(fact);
                    yield Objects.requireNonNull(consumingAndTransformingLeaf.transformer.apply(fact), nullResultFrom("transformer"));
                }
        };
    }

    /**
     * <div>
     *     <p>
     *         A decision node in the tree that contains a predicate and two subtrees.
     *     </p>
     * </div>
     *
     * @param <F> the type of facts that this node processes
     * @param <R> the type of results that this node produces
     */
    final class Node<F, R>
            implements DecisionTree<F, R> {

        private final Predicate<? super F> predicate;
        private final DecisionTree<F, ? extends R> yesTree;
        private final DecisionTree<F, ? extends R> noTree;

        private Node(final Predicate<? super F> predicate,
                     final DecisionTree<F, ? extends R> yesTree,
                     final DecisionTree<F, ? extends R> noTree) {
            this.predicate = predicate;
            this.yesTree = yesTree;
            this.noTree = noTree;
        }

    }

    /**
     * <div>
     *     <p>
     *         A leaf node in the tree that contains a transformer function.
     *     </p>
     * </div>
     *
     * @param <F> the type of facts that this leaf processes
     * @param <R> the type of results that this leaf produces
     */
    final class TransformingLeaf<F, R>
            implements DecisionTree<F, R> {

        private final Function<? super F, ? extends R> transformer;

        private TransformingLeaf(final Function<? super F, ? extends R> transformer) {
            this.transformer = transformer;
        }

    }

    /**
     * <div>
     *     <p>
     *         A leaf node in the tree that computes a result using a transformer
     *         and then applies a side-effect using a consumer.
     *     </p>
     * </div>
     *
     * @param <F> the type of facts that this leaf processes
     * @param <R> the type of results that this leaf produces
     */
    final class TransformingAndConsumingLeaf<F, R>
        implements DecisionTree<F, R> {

        private final Function<? super F, ? extends R> transformer;
        private final Consumer<? super R> consumer;

        private TransformingAndConsumingLeaf(final Function<? super F, ? extends R> transformer, final Consumer<? super R> consumer) {
            this.transformer
                = transformer;
            this.consumer
                = consumer;
        }

    }

    /**
     * <div>
     *     <p>
     *         A leaf node in the tree that applies a side-effect using a consumer
     *         and then computes a result using a transformer.
     *     </p>
     * </div>
     *
     * @param <F> the type of facts that this leaf processes
     * @param <R> the type of results that this leaf produces
     */
    final class ConsumingAndTransformingLeaf<F, R>
        implements DecisionTree<F, R> {

        private final Consumer<? super F> consumer;
        private final Function<? super F, ? extends R> transformer;

        private ConsumingAndTransformingLeaf(final Consumer<? super F> consumer, final Function<? super F, ? extends R> transformer) {
            this.consumer
                = consumer;
            this.transformer
                = transformer;
        }
    }

}
