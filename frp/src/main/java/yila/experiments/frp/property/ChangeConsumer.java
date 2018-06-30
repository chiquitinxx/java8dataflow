package yila.experiments.frp.property;

/**
 * Created by jorgefrancoleza on 16/10/15.
 */
@FunctionalInterface
public interface ChangeConsumer<T> {

    void onChange(T beforeValue, T afterValue);
}
