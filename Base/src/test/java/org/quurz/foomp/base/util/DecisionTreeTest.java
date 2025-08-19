package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.util.DecisionTree.decisionTree;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("DecisionTree")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DecisionTreeTest {

    private static final Logger LOGGER
            = getLogger(DecisionTreeTest.class);

    @Nested
    class Factory {

        @Nested
        class Node {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_throw_NPE_when_predicate_is_null() {
                LOGGER.info("DecisionTree.decisionTree(predicate, yes, no) should throw NPE when predicate is null");

                assertThatThrownBy(
                    () -> decisionTree(
                        null,
                        decisionTree(x -> x),
                        decisionTree(x -> x)
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("predicate");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_throw_NPE_when_yesTree_is_null() {
                LOGGER.info("DecisionTree.decisionTree(predicate, yes, no) should throw NPE when yesTree is null");

                assertThatThrownBy(
                    () -> decisionTree(
                        i -> i > 0,
                        null,
                        decisionTree((Function<Integer, Integer>) x -> x)
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("yesTree");
            }

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_throw_NPE_when_noTree_is_null() {
                LOGGER.info("DecisionTree.decisionTree(predicate, yes, no) should throw NPE when noTree is null");

                assertThatThrownBy(
                    () -> decisionTree(
                        i -> i > 0,
                        decisionTree((Function<Integer, Integer>) x -> x),
                        null
                    )
                )
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("noTree");
            }

            @Test
            void should_create_node_when_arguments_are_valid() {
                LOGGER.info("DecisionTree.decisionTree(predicate, yes, no) should create node on valid args");

                assertThatNoException()
                    .isThrownBy(
                        () -> decisionTree(
                                (Integer i) -> i % 2 == 0,
                                decisionTree(_$ -> "even"),
                                decisionTree(_$ -> "odd")
                        )
                    );
            }

        }

        @Nested
        class Leaf {

            @SuppressWarnings("DataFlowIssue")
            @Test
            void should_throw_NPE_when_transformer_is_null() {
                LOGGER.info("DecisionTree.decisionTree(transformer) should throw NPE when transformer is null");

                assertThatThrownBy(() -> decisionTree((Function<?, ?>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transformer");
            }

            @Test
            void should_create_leaf_when_transformer_is_valid() {
                LOGGER.info("DecisionTree.decisionTree(transformer) should create leaf on valid transformer");

                assertThatNoException()
                    .isThrownBy(() -> decisionTree(String::length));
            }

        }

    }

    @Nested
    class Behaviour {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void examine_should_throw_NPE_when_fact_is_null() {
            LOGGER.info("DecisionTree.examine(null) should throw NullPointerException");

            final var tree
                = decisionTree(
                    x -> true,
                    decisionTree(x -> x),
                    decisionTree(x -> x)
                );

            assertThatThrownBy(() -> tree.examine(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("fact");
        }

        @Test
        void node_should_route_yes_and_no_correctly() {
            LOGGER.info("DecisionTree Node should route to yes/no correctly");

            final var yes
                = decisionTree((Function<Integer, String>) _$ -> "YES");
            final var no
                = decisionTree((Function<Integer, String>) _$ -> "NO");
            final var node
                = decisionTree(i -> i % 2 == 0, yes, no);

            assertThat(node.examine(2))
                .isEqualTo("YES");
            assertThat(node.examine(3))
                .isEqualTo("NO");
        }

        @Test
        void leaf_should_apply_transformer_to_fact() {
            LOGGER.info("DecisionTree Leaf should apply transformer to fact");

            final var leaf
                = decisionTree(String::length);

            assertThat(leaf.examine("abcd")).isEqualTo(4);
        }

        @Test
        void leaf_transformer_returning_null_should_cause_NPE_in_examine() {
            LOGGER.info("DecisionTree Leaf transformer returning null should cause NPE in examine");

            final var leaf
                = decisionTree((Function<String, String>) _s -> null);

            assertThatThrownBy(() -> leaf.examine("x"))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void nested_tree_NEG_POS_ZERO_should_traverse_correctly() {
            LOGGER.info("DecisionTree nested traversal NEG/POS/ZERO should yield expected outputs");

            final DecisionTree<Integer, String> tree
                = decisionTree(
                    i -> i < 0,
                    decisionTree(_$ -> "NEG"),
                    decisionTree(
                            i -> i > 0,
                            decisionTree(_$ -> "POS"),
                            decisionTree(_$ -> "ZERO")
                    )
                );

            assertThat(tree.examine(-1))
                .isEqualTo("NEG");
            assertThat(tree.examine(1))
                .isEqualTo("POS");
            assertThat(tree.examine(0))
                .isEqualTo("ZERO");
        }

        @Test
        void variance_should_be_supported_for_predicate_and_transformer() {
            LOGGER.info("DecisionTree should support variance (Predicate<? super F>, Function<? super F, ? extends R>)");

            final Predicate<Object> pred
                = o -> o != null && o.toString().length() > 3;
            final Function<CharSequence, String> toUpper
                = cs -> cs.toString().toUpperCase();

            final DecisionTree<String, CharSequence> tree
                = decisionTree(
                    pred,
                    decisionTree((Function<? super String, ? extends CharSequence>) toUpper),
                    decisionTree((Function<String, CharSequence>) s -> s)
                );

            assertThat(tree.examine("test"))
                .isEqualTo("TEST");
            assertThat(tree.examine("a"))
                .isEqualTo("a");
        }

        @Test
        void lazy_evaluation_should_only_evaluate_chosen_branch_yes_path() {
            LOGGER.info("DecisionTree should not evaluate NO-branch when YES-branch is taken");

            final var dangerousNo
                = decisionTree(
                    (Function<Integer, String>) _i -> { throw new AssertionError("NO-branch must not be evaluated"); }
                );
            final var safeYes
                    = decisionTree((Function<Integer, String>) _i -> "SAFE-YES");
            final var node
                    = decisionTree(i -> i % 2 == 0, safeYes, dangerousNo);

            assertThat(node.examine(2)).isEqualTo("SAFE-YES");
        }

        @Test
        void lazy_evaluation_should_only_evaluate_chosen_branch_no_path() {
            LOGGER.info("DecisionTree should not evaluate YES-branch when NO-branch is taken");

            final var dangerousYes
                    = decisionTree((Function<Integer, String>) _i -> {
                throw new AssertionError("YES-branch must not be evaluated");
            });
            final var safeNo
                    = decisionTree((Function<Integer, String>) _i -> "SAFE-NO");
            final var node
                    = decisionTree(i -> i < 0, dangerousYes, safeNo);

            assertThat(node.examine(0)).isEqualTo("SAFE-NO");
        }

        @Test
        void exceptions_from_predicate_should_propagate_as_is() {
            LOGGER.info("DecisionTree should propagate exceptions thrown by the predicate unchanged");

            final var ex = new IllegalStateException("boom from predicate");
            final var yes = decisionTree((Function<Integer, String>) _i -> "YES");
            final var no = decisionTree((Function<Integer, String>) _i -> "NO");
            final var node = decisionTree(_i -> {
                throw ex;
            }, yes, no);

            assertThatThrownBy(() -> node.examine(1))
                .isSameAs(ex);
        }

        @Test
        void exceptions_from_transformer_should_propagate_as_is() {
            LOGGER.info("DecisionTree should propagate exceptions from leaf transformer unchanged");

            final var ex = new UnsupportedOperationException("boom from transformer");
            final var throwingLeaf
                = decisionTree((Function<Integer, String>) _i -> {
                    throw ex;
                });
            final var okLeaf
                = decisionTree((Function<Integer, String>) _i -> "OK");

            // Force the throwing leaf to be taken
            final var node = decisionTree(i -> i > 0, throwingLeaf, okLeaf);

            assertThatThrownBy(() -> node.examine(42))
                .isSameAs(ex);
        }

        @Test
        void deeply_nested_tree_should_traverse_correctly() {
            LOGGER.info("DecisionTree deep nesting traversal should yield expected results");

            // Build a tree with ~8 Ebenen; YES wenn i > d, sonst spezifische NO_d
            DecisionTree<Integer, String> tree = decisionTree(_i -> "LEAF");
            for (int d = 7; d >= 0; d--) {
                final int depth = d;
                final var noLeaf = decisionTree((Function<Integer, String>) _i -> "NO" + depth);
                tree = decisionTree(i -> i > depth, tree, noLeaf);
            }

            // Großes i geht immer YES bis zum finalen Leaf
            assertThat(tree.examine(999)).isEqualTo("LEAF");
            // Grenzfälle: entscheiden an der ersten Stelle, an der i <= depth
            assertThat(tree.examine(-1)).isEqualTo("NO0");
            assertThat(tree.examine(0)).isEqualTo("NO0");
            assertThat(tree.examine(1)).isEqualTo("NO1");
            assertThat(tree.examine(2)).isEqualTo("NO2");
            assertThat(tree.examine(7)).isEqualTo("NO7");
            assertThat(tree.examine(8)).isEqualTo("LEAF");
        }

        @Test
        void leaf_length_property_like_should_hold_for_random_strings() {
            LOGGER.info("DecisionTree leaf(String::length) should return s.length() for random inputs");

            final var leaf = decisionTree(String::length);

            final var rnd = new java.util.Random(123456789L);
            for (int t = 0; t < 100; t++) {
                final int len = rnd.nextInt(0, 32);
                final var sb = new StringBuilder(len);
                for (int i = 0; i < len; i++) {
                    // zufällige sichtbare ASCII-Zeichen [32..126]
                    int ch = rnd.nextInt(32, 127);
                    sb.append((char) ch);
                }
                final String s = sb.toString();
                assertThat(leaf.examine(s)).isEqualTo(s.length());
            }

            // Edge Cases: leerer String und Unicode
            assertThat(leaf.examine("")).isEqualTo(0);
            assertThat(leaf.examine("äöüß")).isEqualTo("äöüß".length());
            assertThat(leaf.examine("𝄞🎉")).isEqualTo("𝄞🎉".length());
        }

    }

}