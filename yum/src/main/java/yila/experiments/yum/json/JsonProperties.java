package yila.experiments.yum.json;

import yila.experiments.yum.Jsonnable;

import java.util.List;
import static java.util.stream.Collectors.joining;

public class JsonProperties implements Jsonnable {

    private List<Pair> properties;

    public JsonProperties(List<Pair> properties) {
        this.properties = properties;
    }

    public String toJson() {
        if (properties == null) {
            return "";
        } else {
            String props = properties.stream().map(this::joinPair).collect(joining(", "));
            return "{" + props + "}";
        }
    }

    private String joinPair(Pair pair) {
        return "\"" + pair.getKey() + "\": " + asNumberOrString(pair.getValue().toString());
    }

    private String asNumberOrString(String value) {
        if (isNumber(value)) {
            return value;
        } else {
            return "\"" + value + "\"";
        }
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
