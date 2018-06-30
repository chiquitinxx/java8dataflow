package yila.experiments.acme.domain.purchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JFL 22/6/18
 */
public class Purchase {

    private final LocalDateTime created;
    private final BigDecimal amount;

    public Purchase(LocalDateTime created, BigDecimal amount) {
        this.amount = amount;
        this.created = created;
    }
}
