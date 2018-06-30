package yila.experiments.acme.app;

import java.math.BigDecimal;

/**
 * JFL 22/6/18
 */
public class PurchaseEvent {

    private final BigDecimal amount;
    private final String personId;

    public PurchaseEvent(String personId, BigDecimal amount) {
        this.amount = amount;
        this.personId = personId;
    }

    BigDecimal getAmount() {
        return this.amount;
    }

    String getPersonId() {
        return this.personId;
    }
}
