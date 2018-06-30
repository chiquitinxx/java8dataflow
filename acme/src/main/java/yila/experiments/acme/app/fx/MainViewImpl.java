package yila.experiments.acme.app.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import yila.experiments.acme.app.PurchaseEvent;
import yila.experiments.acme.app.MainView;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Consumer;

/**
 * JFL 20/6/18
 */
public class MainViewImpl implements MainView {

    private Scene scene;
    private Button makePurchaseButtonAs1;
    private Button makePurchaseButtonAs2;
    private Label purchasesLaunchedLabel;
    private Label successPurchasesLabel;
    private ListView<ErrorAdded> errors;

    public MainViewImpl() {

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));
        makePurchaseButtonAs1 = new Button();
        makePurchaseButtonAs2 = new Button();
        makePurchaseButtonAs1.setText("Make purchase for user: 1");
        makePurchaseButtonAs2.setText("Make purchase for user: 2");
        root.add(makePurchaseButtonAs1, 0, 0);
        root.add(makePurchaseButtonAs2, 0, 1);
        purchasesLaunchedLabel = new Label();
        root.add(purchasesLaunchedLabel, 0, 2);
        successPurchasesLabel = new Label();
        root.add(successPurchasesLabel, 0, 3);
        errors = new ListView<>();
        errors.setPrefHeight(200);
        errors.setPrefWidth(500);
        errors.setCellFactory((ListView<ErrorAdded> l) -> new ErrorCell());
        ObservableList<ErrorAdded> list = FXCollections.observableArrayList();
        errors.setItems(list);
        root.add(errors, 0, 4);

        this.scene = new Scene(root, 600, 450);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void onPurchaseButton(Consumer<PurchaseEvent> consumer) {
        makePurchaseButtonAs1.setOnAction(event -> consumer.accept(toPurchaseEvent(event)));
        makePurchaseButtonAs2.setOnAction(event -> consumer.accept(toPurchaseEvent(event)));
    }

    @Override
    public void addError(String message) {
        ObservableList<ErrorAdded> items = errors.getItems();
        items.forEach(errorAdded -> errorAdded.setLast(false));
        items.add(0, new ErrorAdded(message));
    }

    public void setPurchasesLaunched(int number) {
        if (number == 0) {
            purchasesLaunchedLabel.setText("No purchases launched.");
        } else {
            purchasesLaunchedLabel.setText("Launched " + number + " purchases.");
        }
    }

    public void setPurchasesSuccess(int number) {
        if (number == 0) {
            successPurchasesLabel.setText("No success purchases.");
        } else {
            successPurchasesLabel.setText(number + " purchases success.");
        }
    }

    private PurchaseEvent toPurchaseEvent(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String buttonText = button.getText();
        String personId = buttonText.substring(buttonText.indexOf(":") + 1).trim();
        return new PurchaseEvent(personId, new BigDecimal(new Random().nextInt(2000)));
    }
}
