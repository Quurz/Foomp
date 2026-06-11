package org.quurz.foomp.base.types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quurz.foomp.base.TestHelper;
import org.slf4j.Logger;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(MockitoExtension.class)
class SafeExecutableTest
        extends TestHelper {

    private static final Logger LOGGER
        = getLogger(SafeExecutableTest.class);

    @Test
    void testExecuteSafe() {
        LOGGER.info("Test safeExecutable.executeSafe");

        final var safeExecutable1
            = ((Executable<String>) () -> { throw new IllegalArgumentException(OUCH); }).safe();

        final var safeExecutable2
            = ((Executable<String>) () -> SOME_STRING_VALUE).safe();

        assertThat((XorValue<Exception, String>) safeExecutable1.executeSafe())
            .isInstanceOf(XorValue.class)
            .extracting(XorValue::isLeft)
            .isEqualTo(true);
        assertThatThrownBy(() -> safeExecutable1.executeSafe().getRight())
            .isInstanceOf(NoSuchElementException.class);
        assertThat((XorValue<Exception, String>) safeExecutable1.executeSafe())
            .extracting(XorValue::getLeft)
            .isInstanceOf(IllegalArgumentException.class)
            .extracting(Throwable::getMessage)
            .isEqualTo(OUCH);

        assertThat((XorValue<Exception, String>) safeExecutable2.executeSafe())
            .isInstanceOf(XorValue.class)
            .extracting(xorValue -> xorValue.isPresent() && xorValue.isRight())
            .isEqualTo(true);
        assertThatThrownBy(() -> safeExecutable2.executeSafe().getLeft())
            .isInstanceOf(NoSuchElementException.class);
        assertThat((XorValue<Exception, String>) safeExecutable2.executeSafe())
            .extracting(XorValue::getRight)
            .isInstanceOf(String.class)
            .isEqualTo(SOME_STRING_VALUE);
    }

}
