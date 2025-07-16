package org.quurz.foomp.base.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.functions.Fun;
import org.slf4j.Logger;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.functions.Pred.alwaysTrue;
import static org.quurz.foomp.base.util.DecisionTree.decisionTree;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
class DecisionTreeTest {

    private static final Logger LOGGER
        = getLogger(DecisionTreeTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testDecisionTreeNode() {
        LOGGER.info("Test DecisionTree.decisionTree (Node)");

        assertThatThrownBy(() -> decisionTree(null, decisionTree(Fun.identity()), decisionTree(Fun.identity())))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> decisionTree(alwaysTrue(), null, decisionTree(Fun.identity())))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> decisionTree(alwaysTrue(), decisionTree(Fun.identity()), null))
            .isInstanceOf(NullPointerException.class);
        assertThatNoException()
            .isThrownBy(() -> decisionTree(alwaysTrue(), decisionTree(Fun.identity()), decisionTree(Fun.identity())));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testDecisionTreeLeaf() {
        LOGGER.info("Test DecisionTree.decisionTree (Leaf)");

        assertThatThrownBy(() -> decisionTree((Function<?, ?>) null))
            .isInstanceOf(NullPointerException.class);

        assertThatNoException()
            .isThrownBy(() -> decisionTree(Fun.identity()));
    }

    @SuppressWarnings({"DataFlowIssue", "unused"})
    @Test
    void testExamine() {
        LOGGER.info("Test DecisionTree.examine");

        final DecisionTree<Integer, String> decisionTree
            = decisionTree(
                i -> i < 0,
                decisionTree(_$ -> "LEAF-1"),
                decisionTree(
                    i -> i > 0,
                    decisionTree(_$ -> "LEAF-2"),
                    decisionTree(_$ -> "LEAF-3")
                )
            );

        assertThatThrownBy(() -> decisionTree.examine(null))
            .isInstanceOf(NullPointerException.class);
        assertThat(decisionTree.examine(-1))
            .isEqualTo("LEAF-1");
        assertThat(decisionTree.examine(1))
            .isEqualTo("LEAF-2");
        assertThat(decisionTree.examine(0))
            .isEqualTo("LEAF-3");
    }

}
