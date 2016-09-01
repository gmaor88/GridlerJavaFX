package GridlerJavaFXUI;

import Logic.Square;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
    @FXML
    private MenuItem loadGameMenuItem;
    @FXML
    private MenuItem startGameMenuItem;
    @FXML
    private MenuItem endGameMenuItem;
    @FXML
    private MenuItem UndoMenuItem;
    @FXML
    private MenuItem RedoMenuItem;
    @FXML
    private MenuItem instructionMenuItem;
    @FXML
    private MenuItem aboutlMenuItem;
    @FXML
    private Label playersNameLabel;
    @FXML
    private Label IDLabel;
    @FXML
    private Label movesLeftInTurnLabel;
    @FXML
    private Label turnsLeftInGameLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private RadioButton blackRadioButton;
    @FXML
    private RadioButton clearedRadioButton;
    @FXML
    private RadioButton undefinedRadioButton;
    @FXML
    private TextField commentTextField;
    @FXML
    private Button makeMoveButton;
    @FXML
    private Button endTurnButton;

    @FXML
    public void endTurnOnClick() {
        Square.eSquareSign sign = Square.eSquareSign.UNDEFINED;

        if(blackRadioButton.isSelected()){
            sign = Square.eSquareSign.BLACKED;
        }
        else if(clearedRadioButton.isSelected()){
            sign = Square.eSquareSign.CLEARED
        }

        playerMoveHandler.Invoke(playersNameLabel.getText(), sign, commentTextField.getText());
    }

    @FXML
    public void makeMoveOnClick() {

    }

    @FXML
    public void startGameOnClick() {

    }

    @FXML
    public void endGameOnClick() {

    }

    @FXML
    public void undoMoveOnClick() {

    }

    @FXML
    public void redoMoveOnClick() {

    }

    @FXML
    public void instructionOnClick() {

    }

    @FXML
    public void aboutOnClick() {

    }
}
