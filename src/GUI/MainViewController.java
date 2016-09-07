package GUI;

/**
 * Created by dan on 9/5/2016.
 */

import Logic.*;
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
import javafx.util.Pair;
import jaxb.GameDescriptor;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MainViewController implements Initializable{
    private Stage m_Stage;
    private ArrayList<GamePlayer> m_Players = new ArrayList<>();
    private GameBoard m_LoadedBoard;
    private GamePlayer m_CurrentPlayer;
    private int m_CurrentPlayerIndex;
    private HashMap<Pair<Integer,Integer>,Button> m_ButtonsSelected = new HashMap();
    private MoveSet m_CurrentMove;

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
    private MenuItem showStatisticsMenuItem;

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

        try {//// TODO: 9/6/2016 use task bar and threds
            GameDescriptor gameDescriptor = JaxBGridlerClassGenerator.FromXmlFileToObject(file.getAbsolutePath());
            m_LoadedBoard = gameLoader.loadBoard(gameDescriptor);
            m_Players = gameLoader.loadPlayer(gameDescriptor);
            startGameMenuItem.setDisable(false);
            m_CurrentPlayerIndex = 0;
            m_CurrentPlayer = null;
            buildBoard();
        } catch (JAXBException e) {//need to change!
            showErrorMsg("FIle loading error", "Illegal file");
        } catch (GameLoadException ex) {
            showErrorMsg("FIle loading error", ex.getMessage());
        }
        //todo show preview board and list of players
    }

    @FXML
    public void makeMoveOnClick() {
        Square.eSquareSign sign = Square.eSquareSign.UNDEFINED;

        if(blackRadioButton.isSelected()){
            sign = Square.eSquareSign.BLACKED;
        }
        else if(clearedRadioButton.isSelected()){
            sign = Square.eSquareSign.CLEARED;
        }

        for(Map.Entry<Pair<Integer,Integer>, Button> entry: m_ButtonsSelected.entrySet()){
            m_CurrentMove.AddNewPoint(entry.getKey().getKey(),entry.getKey().getValue(),sign);//// TODO: 9/6/2016 need to cheack first is row
            setBoardButtonStyle(entry.getValue(), sign);
        }
    }

    private void setBoardButtonStyle(Button value, Square.eSquareSign sign) {

    }

    @FXML
    public void startGameOnClick() {
        m_CurrentPlayer = m_Players.get(m_CurrentPlayerIndex);
        startGameMenuItem.setDisable(true);
    }

    private void buildBoard() {
       // BoardGridPane = new GridPane();
        int j;
        //Button bSquare;

        BoardGridPane.setAlignment(Pos.TOP_LEFT);
        for(int i = 0; i < m_LoadedBoard.getBoardHeight(); i++){
            for (j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
                final int column = j;
                final int row = i;
                final Button bSquare = new Button();
                bSquare.setDisable(true);
                bSquare.setMinWidth(25);
                bSquare.setMinHeight(25);
                bSquare.setOnAction((event)->buttonClicked(row, column, bSquare));
                bSquare.setAlignment(Pos.CENTER);
               // bSquare.setId();//to use css
               // bSquare.getStyleClass();//same
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

    private void buttonClicked(int i_ColumnIndex, int i_RowIndex, Button i_Button){
        Pair<Integer,Integer> pair = new Pair<>(i_ColumnIndex,i_RowIndex);

         if(!m_ButtonsSelected.containsKey(pair)){
             m_ButtonsSelected.put(pair,i_Button);
             //i_Button.getStyleClass().add();//// TODO: 9/6/2016 selected
         }
         else {
             m_ButtonsSelected.remove(pair);
            // i_Button.getStyleClass().remove();// // TODO: 9/6/2016 unselected
         }
    }

    private void addBlockLabel(GridPane i_GridPane, int i_ColumnIndex, int i_RowIndex, String i_blockSize) {
        Label lBlock = new Label();
        lBlock.setText(i_blockSize);
       // lBlock.setId();//// TODO: 9/6/2016
        //lBlock.getStyleClass();//// TODO: 9/6/2016
        i_GridPane.add(lBlock, i_ColumnIndex, i_RowIndex);
    }

    @FXML
    public void endGameOnClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // Quit game - end player's game
        } else {
            // do nothing...
        }
    }

    @FXML
    public void undoMoveOnClick() {
        //m_CurrentPlayer.Undo();
    }

    @FXML
    public void redoMoveOnClick() {
        //m_CurrentPlayer.redo();
    }

    @FXML
    public void instructionOnClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("Gridler 2.0");
        alert.setContentText("Please Read the ReadMe file :)");
        alert.showAndWait();
    }

    @FXML
    public void aboutOnClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Gridler JavaFX");
        alert.setHeaderText("Gridler 2.0");
        alert.setContentText("Have Fun!");
        alert.showAndWait();
    }

    @FXML
    public void showStatisticsOnClick(){

    }

    private void showErrorMsg(String i_MsgHeather, String i_ErrorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i_MsgHeather);
        alert.setHeaderText("ERROR!");
        alert.setContentText(i_ErrorMsg);
        alert.showAndWait();
    }
}
