package yila.experiments.acme;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import yila.experiments.acme.app.MainView;
import yila.experiments.acme.app.fx.MainViewImpl;
import yila.experiments.acme.conf.GuiceModule;

/**
 * JFL 18/6/18
 */
public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) {
        Injector injector = initGuice();
        primaryStage.setTitle("Purchase system!");
        MainViewImpl mainView = (MainViewImpl)injector.getInstance(MainView.class);
        primaryStage.setScene(mainView.getScene());
        primaryStage.show();
    }

    private Injector initGuice() {
        return Guice.createInjector(new GuiceModule());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
