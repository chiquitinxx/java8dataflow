package yila.experiments.acme.conf;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import yila.experiments.acme.app.MainPresenter;
import yila.experiments.acme.app.MainView;
import yila.experiments.acme.app.fx.MainViewImpl;
import yila.experiments.acme.domain.purchase.PurchaseRepository;
import yila.experiments.acme.domain.purchase.Purchases;
import yila.experiments.acme.domain.purchase.PurchasesImpl;
import yila.experiments.acme.domain.purchase.event.PurchaseFinishedEvent;
import yila.experiments.acme.tools.ConsumerList;
import yila.experiments.acme.tools.ConsumerListImpl;
import yila.experiments.acme.tools.PurchaseRepositoryImpl;

/**
 * JFL 30/6/18
 */
public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MainView.class).to(MainViewImpl.class).asEagerSingleton();
        bind(MainPresenter.class).asEagerSingleton();
        bind(new TypeLiteral<ConsumerList<PurchaseFinishedEvent>>() {})
                .toInstance(new ConsumerListImpl<>());
        bind(Purchases.class).to(PurchasesImpl.class).asEagerSingleton();
        bind(PurchaseRepository.class).to(PurchaseRepositoryImpl.class).asEagerSingleton();
    }
}
