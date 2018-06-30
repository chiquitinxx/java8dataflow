package yila.experiments.yum.json;

import yila.experiments.yum.Jsonnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helpers {

    public static Jsonnable fromMap(Map<String, Object> map) {
        List<Pair> pairs = new ArrayList<>();
        map.forEach((key, value) -> pairs.add(Pair.of(key, value)));
        return new JsonProperties(pairs);
    }
}
