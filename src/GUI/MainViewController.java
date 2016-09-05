package GUI;

/**
 * Created by dan on 9/5/2016.
 */

        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.*;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.net.URL;
        import java.util.ResourceBundle;

public class MainViewController implements Initializable{
    private Stage m_Stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(Stage i_Stage) {
        m_Stage = i_Stage;
    }

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
    private MenuItem aboutMenuItem;
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

    }

    @FXML
    public void loadGameOnClick() throws FileNotFoundException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        File file = fileChooser.showOpenDialog(m_Stage);

        if(file == null){
            throw new FileNotFoundException("Unable to open file");
        }
        String url = file.getAbsolutePath();


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

    public Button getEndTurnButton() {
        return endTurnButton;
    }

    public MenuItem getEndGameMenuItem() {
        return endGameMenuItem;
    }

    public MenuItem getLoadGameMenuItem() {
        return loadGameMenuItem;
    }

    public Label getIDLabel() {
        return IDLabel;
    }

    public Label getMovesLeftInTurnLabel() {
        return movesLeftInTurnLabel;
    }

    public Button getMakeMoveButton() {
        return makeMoveButton;
    }

    public Label getPlayersNameLabel() {
        return playersNameLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getTurnsLeftInGameLabel() {
        return turnsLeftInGameLabel;
    }

    public MenuItem getRedoMenuItem() {
        return RedoMenuItem;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    public MenuItem getStartGameMenuItem() {
        return startGameMenuItem;
    }

    public MenuItem getUndoMenuItem() {
        return UndoMenuItem;
    }

    public RadioButton getBlackRadioButton() {
        return blackRadioButton;
    }

    public RadioButton getClearedRadioButton() {
        return clearedRadioButton;
    }

    public RadioButton getUndefinedRadioButton() {
        return undefinedRadioButton;
    }

    public TextField getCommentTextField() {
        return commentTextField;
    }

    public MenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public MenuItem getInstructionMenuItem() {
        return instructionMenuItem;
    }
}
