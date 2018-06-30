package yila.experiments.acme.domain.purchase;

import yila.experiments.acme.domain.purchase.errors.PurchaseError;
import yila.experiments.acme.domain.purchase.event.PurchaseFinishedEvent;
import yila.experiments.acme.tools.ConsumerList;
import yila.experiments.acme.tools.Result;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * JFL 18/6/18
 */
public class PurchasesImpl implements Purchases {

    private final ConsumerList<PurchaseFinishedEvent> consumersPurchaseFinished;
    private final PurchaseRepository purchaseRepository;

    @Inject
    public PurchasesImpl(ConsumerList<PurchaseFinishedEvent> consumersPurchaseFinished,
                         PurchaseRepository purchaseRepository) {
        this.consumersPurchaseFinished = consumersPurchaseFinished;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public BigDecimal make(BigDecimal amount) {
        System.out.println("Making purchase of amount: " + amount);
        Result<String> result = amount.intValue() < 500 ?
                Result.fail(new PurchaseError()) : Result.success("COOL");
        System.out.println("Purchase of amount: " + amount + " Finished. Result: " + result);
        result.ifOk(transactionResult -> purchaseRepository.save(new Purchase(LocalDateTime.now(), amount)));
        consumersPurchaseFinished.launch(new PurchaseFinishedEvent(amount, result));
        return amount;
    }

    @Override
    public Consumer onPurchaseFinished(Consumer<PurchaseFinishedEvent> consumer) {
        consumersPurchaseFinished.add(consumer);
        return consumer;
    }
}
