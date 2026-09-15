package org.quurz.foomp.base.types;

import org.junit.jupiter.api.Test;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

class XorValueTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(XorValueTest.class);

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testNullValidation() {
        LOGGER.info("Test XorValue null checks for left and right factories");

        assertThatThrownBy(() -> XorValue.left(null))
            .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> XorValue.right(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testLeft() {
        LOGGER.info("Test XorValue.left");

        final XorValue<String, Integer> left
            = XorValue.left("error");

        checkIsLeftWithValue(left, "error");
        assertThat(left.isLeft()).isTrue();
        assertThat(left.isRight()).isFalse();
        assertThat(left.isPresent()).isFalse();
        assertThat(left.getLeft()).isEqualTo("error");

        assertThatThrownBy(left::getRight)
            .isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(left::get)
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testRight() {
        LOGGER.info("Test XorValue.right");

        final XorValue<String, Integer> right
            = XorValue.right(42);

        checkIsRightWithValue(right, 42);
        assertThat(right.isLeft()).isFalse();
        assertThat(right.isRight()).isTrue();
        assertThat(right.isPresent()).isTrue();
        assertThat(right.getRight()).isEqualTo(42);
        assertThat(right.get()).isEqualTo(42);

        assertThatThrownBy(right::getLeft)
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testEqualsAndHashCode() {
        LOGGER.info("Test XorValue equals and hashCode");

        final XorValue<String, Integer> left1 = XorValue.left("fail");
        final XorValue<String, Integer> left2 = XorValue.left("fail");
        final XorValue<String, Integer> left3 = XorValue.left("other");

        final XorValue<String, Integer> right1 = XorValue.right(100);
        final XorValue<String, Integer> right2 = XorValue.right(100);
        final XorValue<String, Integer> right3 = XorValue.right(200);

        assertThat(left1)
            .isEqualTo(left1)
            .isEqualTo(left2)
            .hasSameHashCodeAs(left2)
            .isNotEqualTo(left3)
            .isNotEqualTo(right1)
            .isNotEqualTo(null)
            .isNotEqualTo("fail");

        assertThat(right1)
            .isEqualTo(right1)
            .isEqualTo(right2)
            .hasSameHashCodeAs(right2)
            .isNotEqualTo(right3)
            .isNotEqualTo(left1)
            .isNotEqualTo(null)
            .isNotEqualTo(100);
    }

    @Test
    void testToString() {
        LOGGER.info("Test XorValue toString");

        final XorValue<String, Integer> left = XorValue.left("err");
        final XorValue<String, Integer> right = XorValue.right(42);

        assertThat(left.toString()).contains("Left", "err");
        assertThat(right.toString()).contains("Right", "42");
    }

}
