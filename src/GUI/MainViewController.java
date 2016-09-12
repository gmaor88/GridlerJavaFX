package GUI;

/**
 * Created by dan on 9/5/2016.
 */

import Logic.*;
import Utils.GameLoadException;
import Utils.GameLoader;
import Utils.JaxBGridlerClassGenerator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import jaxb.GameDescriptor;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
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
    private ArrayList<ArrayList<Label>> m_HorizontalBlocksLabel = new ArrayList<>();
    private ArrayList<ArrayList<Label>> m_VerticalBlocksLabel = new ArrayList<>();
    private ArrayList<ArrayList<Button>> m_GameBoardButtons = new ArrayList<>();
    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intitPlayerDataLabel();
    }

    public void init(Stage i_Stage) {
        m_Stage = i_Stage;
        m_Stage.setOnCloseRequest((event)->StopTimer());//// TODO: 9/12/2016 maybe set to deamon by separting thread 
    }

    private void StopTimer() {
        timer.cancel();
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
    private TextArea commentTextArea;
    @FXML
    private Button makeMoveButton;
    @FXML
    private Button endTurnButton;
    @FXML
    private GridPane BoardGridPane;
    @FXML
    private MenuItem showStatisticsMenuItem;
    @FXML
    private BorderPane mainBoarderPane;
    @FXML
    private Menu PlayersBoardsMenu;
    @FXML
    private RadioMenuItem defaultSkinRadioMenuItem;
    @FXML
    private RadioMenuItem XSkinRadioMenuItem;
    @FXML
    private RadioMenuItem YSkinRadioMenuItem;
    @FXML
    private MenuItem ShowMovesListMenuItem;
    @FXML
    private MenuItem player1BoardMenuItem;

    @FXML
    private void ShowMovesListMenuItemOnClick(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowMoveList.fxml"));
            Parent root = fxmlLoader.load();
            ShowMoveListController controller = (ShowMoveListController) fxmlLoader.getController();
            ObservableList<String> items = FXCollections.observableList(m_CurrentPlayer.getMoveList());
            controller.getMoveListListView().setItems(items);
            Stage stage = new Stage();
            stage.setScene(new Scene(root,400,400));
            stage.show();
        }
        catch (IOException e){
            showErrorMsg("FXML loading error", "statistics fxml could not be loaded");
        }

    }
    @FXML
    private void PlayersBoardsMenuOnClick(){

    }
    @FXML
    private void defaultSkinRadioMenuItemOnClick(){

    }
    @FXML
    private void XSkinRadioMenuItemOnClick(){

    }
    @FXML
    private void YSkinRadioMenuItemOnClick(){

    }
    @FXML
    public void endTurnOnClick() {
        enableDisableControlButtons(true);
        setForNextTurnOrMove();
        timer.cancel();
        m_CurrentPlayer.endTurn();
        m_CurrentPlayerIndex++;
        if(m_CurrentPlayerIndex >= m_Players.size()){
            m_CurrentPlayerIndex = 0;
        }

        m_CurrentPlayer = m_Players.get(m_CurrentPlayerIndex);
        if(!m_CurrentPlayer.checkIfPlayerHasTurnLeft()){
            //// TODO: 9/11/2016 victoryTieHandler 
        }
        
        if(!m_CurrentPlayer.getIsHuman()){
            clearBoard();
            startTimer();
            m_CurrentPlayer.AiPlay();
            if(m_CurrentPlayer.getScore() == 100){
                //// TODO: 9/11/2016 victoryHandler
            }
            else {
                endTurnOnClick();
            }
        }
        else{
            updatePlayerDataLabels();
            showBoard(m_CurrentPlayer);
            enableDisableControlButtons(false);
            redoUndoMenuItemsAvailabilityModifier();
        }

    }

    @FXML
    public void loadGameOnClick() {
        FileChooser fileChooser = new FileChooser();
        GameLoader gameLoader = new GameLoader();
        fileChooser.setTitle("Open XML File");
        startGameMenuItem.setDisable(true);
        intitPlayerDataLabel();
        File file = fileChooser.showOpenDialog(m_Stage);
        if (file != null) {
            try {//// TODO: 9/6/2016 use task bar and threds
                GameDescriptor gameDescriptor = JaxBGridlerClassGenerator.FromXmlFileToObject(file.getAbsolutePath());
                m_LoadedBoard = gameLoader.loadBoard(gameDescriptor);
                m_Players = gameLoader.loadPlayer(gameDescriptor);
                startGameMenuItem.setDisable(false);
                m_CurrentPlayerIndex = 0;
                m_CurrentPlayer = null;
                clearArrayLists();
                buildBoard();
                createPlayersBoardMenu();
                enableDisableControlButtons(true);
            } catch (JAXBException e) {//need to change!
                showErrorMsg("FIle loading error", "Illegal file");
            } catch (GameLoadException ex) {
                showErrorMsg("FIle loading error", ex.getMessage());
            }
        }
    }

    private void createPlayersBoardMenu() {
        int i = 1;

        for (GamePlayer player : m_Players){
            if(i == 1){
                initPlayer1BoardMenuItem(player);
            }
            else{
                final MenuItem playerBoardMenuItem = new MenuItem();
                playerBoardMenuItem.setText(player.getName());
                player1BoardMenuItem.setId(player.getId());//Test
                playerBoardMenuItem.setOnAction((event)->playerBoardMenuItemClicked(player));
                PlayersBoardsMenu.getItems().add(playerBoardMenuItem);
                if(player.getIsHuman()){
                    playerBoardMenuItem.setDisable(true);
                }
            }

            i++;
        }

        PlayersBoardsMenu.setDisable(false);
    }

    private void initPlayer1BoardMenuItem(GamePlayer i_Player){
        player1BoardMenuItem.setText(i_Player.getName());
        player1BoardMenuItem.setId(i_Player.getId());//Test
        player1BoardMenuItem.setOnAction((event)->playerBoardMenuItemClicked(i_Player));
        if(i_Player.getIsHuman()){
            player1BoardMenuItem.setDisable(true);
        }
    }

    private void playerBoardMenuItemClicked(GamePlayer i_Player) {
        showBoard(i_Player);
        if (!i_Player.getIsHuman()) {
            for (MenuItem item : PlayersBoardsMenu.getItems()) {
                if (item.getText().equalsIgnoreCase(m_CurrentPlayer.getName())) {
                    item.setDisable(false);
                }
            }
        }
        else if (i_Player == m_CurrentPlayer) {
            for (MenuItem item : PlayersBoardsMenu.getItems()) {
                if (item.getText().equalsIgnoreCase(m_CurrentPlayer.getName())) {
                    item.setDisable(true);
                }
            }
        }
    }

    private void clearArrayLists() {
        m_HorizontalBlocksLabel.clear();
        m_VerticalBlocksLabel.clear();
        m_GameBoardButtons.clear();
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

        m_CurrentMove = new MoveSet(commentTextArea.getText());
        for(Map.Entry<Pair<Integer,Integer>, Button> entry: m_ButtonsSelected.entrySet()){
            m_CurrentMove.AddNewPoint(entry.getKey().getKey(),entry.getKey().getValue(),sign);//// TODO: 9/6/2016 need to cheack first is row
            setBoardButtonStyle(entry.getValue(), sign);
        }

        m_CurrentPlayer.preformPlayerMove(m_CurrentMove);
        redoUndoMenuItemsAvailabilityModifier();
        makeMoveButton.setDisable(!m_CurrentPlayer.checkIfPlayerHasMovesLeft());
        setForNextTurnOrMove();
    }

    private void setForNextTurnOrMove() {
        for(Map.Entry<Pair<Integer,Integer>, Button> entry: m_ButtonsSelected.entrySet()){
            entry.getValue().getStyleClass().remove("buttonSelected");
        }

        m_ButtonsSelected.clear();
    }

    private void setBoardButtonStyle(Button value, Square.eSquareSign sign) {
        String buttonId = "undefCell";

        if(sign == Square.eSquareSign.BLACKED){
            buttonId = "blackedCell";
        }
        else if(sign == Square.eSquareSign.CLEARED){
            buttonId = "clearedCell";
        }

        value.setId(buttonId);
    }

    private void redoUndoMenuItemsAvailabilityModifier(){
        UndoMenuItem.setDisable(!m_CurrentPlayer.isUndoAvailable());
        RedoMenuItem.setDisable(!m_CurrentPlayer.isRedoAvailable() || !m_CurrentPlayer.checkIfPlayerHasMovesLeft());
    }

    @FXML
    public void startGameOnClick() {
        setBoardsOnPlayers();
        m_CurrentPlayer = m_Players.get(m_CurrentPlayerIndex);
        startGameMenuItem.setDisable(true);
        if(m_CurrentPlayer.getIsHuman()) {
            showBoard(m_CurrentPlayer);
            updatePlayerDataLabels();
            RedoMenuItem.setDisable(true);
            UndoMenuItem.setDisable(true);
        }
        else {
            m_CurrentPlayer.AiPlay();
            if(m_CurrentPlayer.getScore() == 100){
                //// TODO: 9/11/2016 victoryHandler
            }
            else {
                endTurnOnClick();
            }
        }
    }

    private void setBoardsOnPlayers() {
        for(GamePlayer player:m_Players){
            player.setGameBoard(m_LoadedBoard);
        }
    }

    private void buildBoard() {
       BoardGridPane = new GridPane();
       int j;
       //Button bSquare;
       mainBoarderPane.setCenter(BoardGridPane);
       BoardGridPane.setAlignment(Pos.CENTER);
       for(int i = 0; i < m_LoadedBoard.getBoardHeight(); i++){
           m_GameBoardButtons.add(new ArrayList<Button>());
           m_HorizontalBlocksLabel.add(new ArrayList<>());
           for (j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
               final int column = j;
               final int row = i;
               final Button bSquare = new Button();
               bSquare.setDisable(true);
               //bSquare.setMinWidth(25);
               //bSquare.setMinHeight(25);
               bSquare.setOnAction((event)->buttonClicked(row, column, bSquare));
               bSquare.setAlignment(Pos.CENTER);
               bSquare.setId("undefCell");
               bSquare.getStyleClass().add("boardButton");
               m_GameBoardButtons.get(i).add(bSquare);
               BoardGridPane.add(bSquare, j, i);
               BoardGridPane.setMargin(bSquare, new Insets(0,0,2,1));
           }

           for(Block block:m_LoadedBoard.getHorizontalSlice(i)){//was i -1
               addBlockLabel(BoardGridPane, j, i, block.toString(), m_HorizontalBlocksLabel.get(i));
               j++;
           }
       }

       for(int i = 0; i < m_LoadedBoard.getBoardWidth(); i++){
           j = m_LoadedBoard.getBoardHeight();
           m_VerticalBlocksLabel.add(new ArrayList<>());
           for(Block block:m_LoadedBoard.getVerticalSlice(i)) { //was i - 1
               addBlockLabel(BoardGridPane, i , j, block.toString(), m_VerticalBlocksLabel.get(i));
               j++;
           }
       }
    }

    private void buttonClicked(int i_ColumnIndex, int i_RowIndex, Button i_Button){
        Pair<Integer,Integer> pair = new Pair<>(i_ColumnIndex,i_RowIndex);

         if(!m_ButtonsSelected.containsKey(pair)){
             m_ButtonsSelected.put(pair,i_Button);
             i_Button.getStyleClass().add("buttonSelected");
         }
         else {
             m_ButtonsSelected.remove(pair);
             i_Button.getStyleClass().remove("buttonSelected");
         }
    }

    private void addBlockLabel(GridPane i_GridPane, int i_ColumnIndex, int i_RowIndex, String i_blockSize, ArrayList<Label> i_SliceLabels) {
        Label lBlock = new Label();
        lBlock.setText(i_blockSize);
        lBlock.setId("incompleteBlock");
        lBlock.getStyleClass().add("boardBlockLabel");
        i_SliceLabels.add(lBlock);
        i_GridPane.add(lBlock, i_ColumnIndex, i_RowIndex);
        i_GridPane.setMargin(lBlock, new Insets(0,0,0,5));
    }

    @FXML
    public void endGameOnClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // Quit game - end player's game
            timer.cancel();
        } else {
            // do nothing...
        }
    }


    @FXML
    public void undoMoveOnClick() {
        m_CurrentPlayer.undo();
        showBoard(m_CurrentPlayer);
        redoUndoMenuItemsAvailabilityModifier();
        makeMoveButton.setDisable(!m_CurrentPlayer.checkIfPlayerHasMovesLeft());
    }

    @FXML
    public void redoMoveOnClick() {
        m_CurrentPlayer.redo();
        showBoard(m_CurrentPlayer);
        redoUndoMenuItemsAvailabilityModifier();
        //RedoMenuItem.setDisable(!m_CurrentPlayer.isRedoAvailable() && m_CurrentPlayer.checkIfPlayerHasMovesLeft());
        makeMoveButton.setDisable(!m_CurrentPlayer.checkIfPlayerHasMovesLeft());
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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShowStatistics.fxml"));
            Parent root = fxmlLoader.load();
            ShowStatisticsController controller = (ShowStatisticsController) fxmlLoader.getController();
            controller.getNumberOfMovesPlayedLable().setText(m_CurrentPlayer.getNumOfMovesMade().toString());
            controller.getNumberOfRedoPlayedLable().setText(m_CurrentPlayer.getNumOfRedoMade().toString());
            controller.getNumberOfUndoPlayedLable().setText(m_CurrentPlayer.getNumOfUndoMade().toString());
            Stage stage = new Stage();
            stage.setScene(new Scene(root,400,400));
            stage.show();
        }
        catch (IOException e){
            showErrorMsg("FXML loading error", "statistics fxml could not be loaded");
        }
    }

    private void showErrorMsg(String i_MsgHeather, String i_ErrorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(i_MsgHeather);
        alert.setHeaderText("ERROR!");
        alert.setContentText(i_ErrorMsg);
        alert.showAndWait();
    }

    //// TODO: 9/12/2016 when game ends, open all players board for viewing.
    private void showBoard(GamePlayer i_Player){
        if(i_Player != m_CurrentPlayer && i_Player.getIsHuman()){
            //// TODO: 9/11/2016 error msg
            return;
        }

        clearBoard();
        i_Player.updateBlocks();//// TODO: 9/11/2016 use in progarss thread
        for(int i = 0; i < m_LoadedBoard.getBoardHeight(); i++){
            updateBlocks(m_HorizontalBlocksLabel.get(i), i_Player.getHorizontalSlice(i));
            for (int j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
                setBoardButtonStyle(m_GameBoardButtons.get(i).get(j), i_Player.getGameBoardSquareSign(i,j));
                m_GameBoardButtons.get(i).get(j).setDisable(!i_Player.getId().equalsIgnoreCase(m_CurrentPlayer.getId()));
            }
        }

        for (int j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
            updateBlocks(m_VerticalBlocksLabel.get(j), i_Player.getVerticalSlice(j));
        }

        enableDisableControlButtons(!i_Player.getId().equalsIgnoreCase(m_CurrentPlayer.getId()));
    }

    private void enableDisableControlButtons(boolean i_Disable) {// if true the buttons disabled
        makeMoveButton.setDisable(i_Disable);
        endTurnButton.setDisable(i_Disable);
        RedoMenuItem.setDisable(i_Disable);
        UndoMenuItem.setDisable(i_Disable);
    }

    private void clearBoard() {
        for(int i = 0; i < m_LoadedBoard.getBoardHeight(); i++){
            clearSlice(m_HorizontalBlocksLabel.get(i));
            for (int j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
                m_GameBoardButtons.get(i).get(j).setId("undefCell");
            }
        }

        for (int j = 0; j < m_LoadedBoard.getBoardWidth(); j++){
            clearSlice(m_VerticalBlocksLabel.get(j));
        }
    }

    private void clearSlice(ArrayList<Label> i_Labels) {
        for (Label label: i_Labels){
            label.setId("incompleteBlock");
            //label.getStyleClass().clear();
        }
    }

    private void updateBlocks(ArrayList<Label> i_Labels, ArrayList<Block> i_Blocks) {
        int i = 0;

        for(Label label: i_Labels){
            if(i_Blocks.get(i).isMarked()){
                label.setId("perfectBlock");
            }

            i++;
        }
    }

    private void updatePlayerDataLabels(){
        playersNameLabel.setText(m_CurrentPlayer.getName());
        scoreLabel.setText(((Integer)((int)m_CurrentPlayer.getScore())).toString());
        IDLabel.setText(m_CurrentPlayer.getId());
        turnsLeftInGameLabel.setText(m_CurrentPlayer.getTurnNumber().toString());
        movesLeftInTurnLabel.setText(((Integer)(2 - m_CurrentPlayer.getNumOfMovesMade())).toString());
        startTimer();
    }

    private void intitPlayerDataLabel(){
        playersNameLabel.setText("");
        scoreLabel.setText("");
        IDLabel.setText("");
        turnsLeftInGameLabel.setText("");
        movesLeftInTurnLabel.setText("");
        timerLabel.setText("");
    }

    private void startTimer(){//// TODO: 9/12/2016 use timer.clear in override onClose
        timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        m_CurrentPlayer.incrementTime();
                        timerLabel.setText(((Long)m_CurrentPlayer.getTimer()).toString());
                    }
                });
            }
        }, 1000, 1000);//delay, period
    }
}
