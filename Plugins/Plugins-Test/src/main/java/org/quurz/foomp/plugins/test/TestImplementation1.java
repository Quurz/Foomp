package org.quurz.foomp.plugins.test;

public class TestImplementation1
        implements TestContract {

    @Override
    public String sayHello() {
        return "Hello World! It's me! TestImplementation1!";
    }

    @Override
    public String echo(final String message) {
        return message;
    }

}
