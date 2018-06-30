package yila.experiments.acme.tools;

import yila.experiments.acme.domain.purchase.Purchase;
import yila.experiments.acme.domain.purchase.PurchaseRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * JFL 22/6/18
 */
public class PurchaseRepositoryImpl implements PurchaseRepository {

    private List<Purchase> purchaseList = new ArrayList<>();

    @Override
    public Purchase save(Purchase purchase) {
        purchaseList.add(purchase);
        return purchase;
    }
}
