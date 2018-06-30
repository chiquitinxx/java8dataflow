package yila.experiments.frp.stream;

import yila.experiments.frp.property.Property;

import java.util.Timer;
import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * Created by jorgefrancoleza on 17/10/15.
 */
public class FactoryPublisher {

    public static <T> Publisher<T> newInstance() {
        return new BasicPublisher<>();
    }

    public static Publisher<LocalDateTime> timerInstance(long delay, long interval) {
        Publisher<LocalDateTime> publisher = new BasicPublisher<>();
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                publisher.publish(LocalDateTime.now());
            }
        };
        timer.scheduleAtFixedRate(timerTask, delay, interval);
        return publisher;
    }

    public static <T> Publisher<T> propertyInstance(Property<T> property) {
        Publisher<T> publisher = new BasicPublisher<>();
        property.onChangeAsync((oldValue, newValue) -> publisher.publish(newValue));
        return publisher;
    }
}
