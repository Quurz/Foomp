package org.quurz.foomp.base;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class TestException
        extends RuntimeException {

    private final Map<String, Object> context;

    public TestException(final String message) {
        super(message);
        this.context
            = new HashMap<>();
    }

    public TestException(final String message,
                         final Throwable cause) {
        super(message, cause);
        this.context
            = new HashMap<>();
    }

    public TestException(final String message,
                         final Map<String, Object> context) {
        super(message);
        this.context
            = new HashMap<>(context);
    }

    public Map<String, Object> context() {
        return this.context;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "TestException()[", "]")
                .add("context = " + this.context)
                .toString();
    }
}
