package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by Maor Gershkovitch on 9/8/2016.
 */
public class ShowStatisticsController {
    @FXML
    private Label NumberOfMovesPlayedLabel;
    @FXML
    private Label NumberOfUndoPlayedLabel;
    @FXML
    private Label NumberOfRedoPlayedLabel;

    public Label getNumberOfMovesPlayedLabel() {
        return NumberOfMovesPlayedLabel;
    }

    public Label getNumberOfRedoPlayedLabel() {
    return NumberOfRedoPlayedLabel;
    }

    public Label getNumberOfUndoPlayedLabel() {
        return NumberOfUndoPlayedLabel;
    }
}
