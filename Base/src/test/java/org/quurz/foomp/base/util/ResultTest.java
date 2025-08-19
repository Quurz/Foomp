package org.quurz.foomp.base.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;

@DisplayName("Result")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResultTest {

    @Nested
    class Factory {

        @Nested
        class Success {

        }

        @Nested
        class Failure {

        }

    }

    @Nested
    class Accessor {

    }

    @Nested
    class Behaviour {

    }

}
