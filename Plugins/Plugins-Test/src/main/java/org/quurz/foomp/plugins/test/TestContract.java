package org.quurz.foomp.plugins.test;

public interface TestContract {

    default String sayHello() {
        return "Hello World!";
    }

    String echo(final String message);

}
