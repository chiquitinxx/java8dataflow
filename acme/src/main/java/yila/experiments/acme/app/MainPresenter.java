package yila.experiments.acme.app;

import yila.experiments.acme.domain.purchase.Purchases;
import yila.experiments.acme.tools.Error;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * JFL 18/6/18
 */
public class MainPresenter {

    private final Purchases purchases;
    private final MainView mainView;

    private int numberPurchases;
    private int successPurchases;

    @Inject
    public MainPresenter(MainView mainView, Purchases purchases) {
        this.mainView = mainView;
        this.purchases = purchases;
        numberPurchases = 0;
        successPurchases = 0;
        mainView.setPurchasesLaunched(numberPurchases);
        mainView.setPurchasesSuccess(successPurchases);
        purchases.onPurchaseFinished(event ->
                event.getResult()
                        .ifOk(result -> incSuccessPurchases())
                        .ifFailed(error -> showPurchaseError(error, event.getAmount())));
        mainView.onPurchaseButton(event -> {
            incPurchasesLaunched();
            purchases.make(event.getAmount());
        });
    }

    private void incPurchasesLaunched() {
        numberPurchases++;
        mainView.setPurchasesLaunched(numberPurchases);
    }

    private void incSuccessPurchases() {
        successPurchases++;
        mainView.setPurchasesSuccess(successPurchases);
    }

    private void showPurchaseError(Error error, BigDecimal amount) {
        mainView.addError("Error: " + error.getCode() + " purchasing an amount of: " + amount);
    }
}
