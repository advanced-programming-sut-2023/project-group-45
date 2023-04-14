package operator;

public class Result<T> {
    private final T data;

    Result(T t) {
        data = t;
    }

    T get() {
        return data;
    }
}
