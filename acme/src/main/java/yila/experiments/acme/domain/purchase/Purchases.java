package yila.experiments.acme.domain.purchase;

import yila.experiments.acme.domain.purchase.event.PurchaseFinishedEvent;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * JFL 18/6/18
 */
public interface Purchases {

    BigDecimal make(BigDecimal amount);

    Consumer<PurchaseFinishedEvent> onPurchaseFinished(Consumer<PurchaseFinishedEvent> consumer);
}
