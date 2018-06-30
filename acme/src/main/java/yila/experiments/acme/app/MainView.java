package yila.experiments.acme.app;

import java.util.function.Consumer;

/**
 * JFL 21/6/18
 */
public interface MainView {

    void setPurchasesLaunched(int number);
    void setPurchasesSuccess(int number);

    void onPurchaseButton(Consumer<PurchaseEvent> consumer);
    void addError(String message);
}
