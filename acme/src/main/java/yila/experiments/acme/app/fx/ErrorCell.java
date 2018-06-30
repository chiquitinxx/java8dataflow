package yila.experiments.acme.app.fx;

import javafx.scene.control.ListCell;

/**
 * JFL 30/6/18
 */
public class ErrorCell extends ListCell<ErrorAdded> {
    @Override
    public void updateItem(ErrorAdded item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText("⭐" + item.getErrorMessage());
            setUnderline(item.isLast());
        }
    }
}
