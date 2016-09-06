package GUI;

/**
 * Created by dan on 9/5/2016.
 */

        import Logic.Block;
        import Logic.GameBoard;
        import Logic.GamePlayer;
        import Utils.GameLoadException;
        import Utils.GameLoader;
        import Utils.JaxBGridlerClassGenerator;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.geometry.Pos;
        import javafx.scene.control.*;
        import javafx.scene.layout.GridPane;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import jaxb.GameDescriptor;

        import javax.xml.bind.JAXBException;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.ResourceBundle;

public class MainViewController implements Initializable{
    private Stage m_Stage;
    private ArrayList<GamePlayer> m_Players = new ArrayList<>();
    private GameBoard m_LoadedBoard;
    private GamePlayer m_CurrentPlayer;
    private int m_CurrentPlayerIndex;

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
    private GridPane BoardGridPane;

    @FXML
    public void endTurnOnClick() {

    }

    @FXML
    public void loadGameOnClick() {
        FileChooser fileChooser = new FileChooser();
        GameLoader gameLoader = new GameLoader();
        fileChooser.setTitle("Open XML File");
        startGameMenuItem.setDisable(true);
        File file = fileChooser.showOpenDialog(m_Stage);

        try {
            GameDescriptor gameDescriptor = JaxBGridlerClassGenerator.FromXmlFileToObject(file.getAbsolutePath());
            m_LoadedBoard = gameLoader.loadBoard(gameDescriptor);
            m_Players = gameLoader.loadPlayer(gameDescriptor);
            startGameMenuItem.setDisable(false);
            m_CurrentPlayerIndex = 0;
            m_CurrentPlayer = null;
        } catch (JAXBException e) {//need to change!
            System.out.println("Illegal file.");
        } catch (GameLoadException ex) {
            System.out.println(ex.getMessage());
        }
        //todo show preview board and list of players
    }

    @FXML
    public void makeMoveOnClick() {

    }

    @FXML
    public void startGameOnClick() {
        m_CurrentPlayer = m_Players.get(m_CurrentPlayerIndex);
        startGameMenuItem.setDisable(true);
    }

    private void buildBoard() {
       // BoardGridPane = new GridPane();
        int j;
        Button bSquare;

        BoardGridPane.setAlignment(Pos.CENTER);
        for(int i = 0; i < m_LoadedBoard.getBoardHeight(); i++){
            for (j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
                bSquare = new Button();
                bSquare.setMinWidth(25);
                bSquare.setMinHeight(25);
                bSquare.setOnAction(()->todo);
                bSquare.setAlignment(Pos.CENTER);
                bSquare.setId();//to use css
                bSquare.getStyleClass();//same
                BoardGridPane.add(bSquare, j, i);
            }

            for(Block block:m_LoadedBoard.getHorizontalSlice(i)){//was i -1
                addBlockLabel(BoardGridPane, j, i, block.toString());
                j++;
            }
        }

        for(int i = 0; i < m_LoadedBoard.getBoardWidth(); i++){
            j = m_LoadedBoard.getBoardHeight();
            for(Block block:m_LoadedBoard.getVerticalSlice(i)) { //was i - 1
                addBlockLabel(BoardGridPane, i , j, block.toString());
                j++;
            }
        }
    }

    private void addBlockLabel(GridPane i_GridPane, int i_ColumnIndex, int i_RowIndex, String i_blockSize) {
        Label lBlock = new Label();
        lBlock.setText(i_blockSize);
        lBlock.setId();//// TODO: 9/6/2016
        lBlock.getStyleClass();//// TODO: 9/6/2016
        i_GridPane.add(lBlock, i_ColumnIndex, i_RowIndex);
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
