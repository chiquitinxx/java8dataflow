package yila.experiments.acme.domain.purchase.event;

import yila.experiments.acme.tools.Result;

import java.math.BigDecimal;

/**
 * JFL 18/6/18
 */
public class PurchaseFinishedEvent {

    private final BigDecimal amount;
    private final Result<String> result;

    public PurchaseFinishedEvent(BigDecimal amount, Result<String> result) {
        this.amount = amount;
        this.result = result;
    }

    public Result<String> getResult() {
        return this.result;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }
}
