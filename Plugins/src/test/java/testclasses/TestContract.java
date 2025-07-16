package testclasses;

public interface TestContract {

    String getValue();

    default String echo(String message) {
        return message;
    }

}
