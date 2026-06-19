package org.quurz.foomp.base.concurrent;

public class ConcurrentExecutionException extends RuntimeException {

    public ConcurrentExecutionException(String message) {
        super(message);
    }

    public ConcurrentExecutionException(final Throwable cause) {
        super(cause);
    }

}
