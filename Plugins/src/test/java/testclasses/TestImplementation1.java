package testclasses;

public class TestImplementation1 implements TestContract {

    private final String value;

    public TestImplementation1(final String value) {
        this.value
            = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
