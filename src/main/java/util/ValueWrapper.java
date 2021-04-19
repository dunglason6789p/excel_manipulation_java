package util;

public class ValueWrapper<T> {
    private T value;
    public T get() {
        return value;
    }
    public void set(T value) {
        this.value = value;
    }
    public static <T> ValueWrapper<T> from(T value) {
        ValueWrapper<T> wrapper = new ValueWrapper<>();
        wrapper.set(value);
        return wrapper;
    }
}
