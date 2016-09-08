package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by Maor Gershkovitch on 9/8/2016.
 */
public class ShowStatisticsController {
    @FXML
    private Label NumberOfMovesPlayedLable;
    @FXML
    private Label NumberOfUndoPlayedLable;
    @FXML
    private Label NumberOfRedoPlayedLable;

    public Label getNumberOfMovesPlayedLable() {
        return NumberOfMovesPlayedLable;
    }

    public Label getNumberOfRedoPlayedLable() {
        return NumberOfRedoPlayedLable;
    }

    public Label getNumberOfUndoPlayedLable() {
        return NumberOfUndoPlayedLable;
    }
}
