package org.quurz.foomp.plugins.test;

public class TestImplementation2
        implements TestContract {

    private final String name;

    public TestImplementation2() {
        this("World");
    }

    public TestImplementation2(final String name) {
        this.name
            = name;
    }

    @Override
    public String sayHello() {
        return "Hello " + this.name + "!";
    }

    @Override
    public String echo(final String message) {
        return "[echo]: " + message;
    }

}
