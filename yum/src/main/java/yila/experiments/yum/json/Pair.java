package yila.experiments.yum.json;

public class Pair {

    private final String key;
    private final Object value;

    public static Pair of(String key, Object value) {
        return new Pair(key, value);
    }

    public Pair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
