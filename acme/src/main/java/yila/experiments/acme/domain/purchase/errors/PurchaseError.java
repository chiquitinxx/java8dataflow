package yila.experiments.acme.domain.purchase.errors;

import yila.experiments.acme.tools.Error;

/**
 * JFL 29/6/18
 */
public class PurchaseError implements Error {
    @Override
    public int getCode() {
        return 255;
    }
}
