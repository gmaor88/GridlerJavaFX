package UI;

import Logic.*;
import Utils.*;
import javafx.scene.paint.Color;
import jaxb.GameDescriptor;
import javax.xml.bind.JAXBException;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Maor Gershkovitch on 8/8/2016.
 * This class is in charge of the game engine, and Ui.
 */
public class GameManager {
    private GameBoard m_GameBoard;
    private GamePlayer m_Player;
    private LinkedList<MoveSet> m_UndoList = new LinkedList<>();
    private LinkedList<MoveSet> m_RedoList = new LinkedList<>();
    private Boolean m_PlayerWantsToPlay = true;
    private Boolean m_InGame = false;
    private Boolean m_GameReady = false;
    private Boolean m_StatisticsReady = false;
    private Long m_GameDurationTimer;

    public GameManager() {
    }

    public void Run() {
        while (m_PlayerWantsToPlay) {
            findAndPresentVictory();
            presentMainMenu();
            eGameOptions choice = getPlayersChoiceForMenu();
            switch (choice) {
                case LOAD_GAME:
                    loadNewGame();
                    break;
                case START_GAME:
                    startNewGame();
                    break;
                case DISPLAY_BORD:
                    printBoard();
                    break;
                case PERFORM_MOVE:
                    preformPlayerMove();
                    break;
                case DISPLAY_MOVES_LIST:
                    printPlayersMovesList();
                    break;
                case UNDO:
                    preformUndo();
                    break;
                case REDO:
                    preformRedo();
                    break;
                case STATISTICS:
                    printStatistics();
                    break;
                case QUIT:
                    if (!m_InGame) {
                        m_PlayerWantsToPlay = false;
                    } else {
                        m_InGame = false;
                    }
                    break;
            }
        }
    }

    private void findAndPresentVictory() {
        if(m_InGame) {
            if (m_GameBoard.getBoardCompletionPercentage() == 100) {
                System.out.print("Congratulations " + m_Player.getName() + ", ");
                System.out.print("have won the game!!." + System.lineSeparator());
                m_InGame = false;
                m_GameReady = false;
            }
        }
    }

    private void presentMainMenu() {
        int i = 1;

        System.out.print(System.getProperty("line.separator"));
        for (eGameOptions option : eGameOptions.values()) {
            if (m_InGame) {
                if (option != eGameOptions.LOAD_GAME && option != eGameOptions.START_GAME) {
                    System.out.print((i+2) + "." + option.toString() + System.lineSeparator());
                    i++;
                }
            }
            else {
                    System.out.print(i + "." + option.toString() + System.lineSeparator());
                    i++;
            }
        }

        System.out.println("Select one of the above: ");
    }

    private void loadNewGame() {
        String path = getFilePathFromUser();

        try {
            GameDescriptor gameDescriptor = JaxBGridlerClassGenerator.FromXmlFileToObject(path);
            validateGameDescriptorInfo(gameDescriptor);
            loadPlayerData(gameDescriptor);
            m_GameReady = true;
            m_StatisticsReady = true;
        } catch (JAXBException e) {
            System.out.println("Illegal file.");
        } catch (GameLoadException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void loadPlayerData(GameDescriptor i_GameDescriptor) throws GameLoadException {
        String userChoice;
        boolean validInput;

        do {
            System.out.println("Would you like to load player data from file? y/n");
            userChoice = InputScanner.scanner.nextLine();
            validInput = userChoice.equalsIgnoreCase("y") || userChoice.equalsIgnoreCase("n");
        }while (!validInput);

        if(userChoice.equalsIgnoreCase("y")){
            GameLoader gameLoader = new GameLoader();
            m_Player = gameLoader.loadPlayer(i_GameDescriptor);
        }
        else{
            getPlayerDataFromUser();
        }
    }

    private void getPlayerDataFromUser() {
       String playerName, playerId, humanPLayer, userChoice;
        Boolean validInput;

        System.out.println("Please enter player name: ");
        playerName = InputScanner.scanner.nextLine();
        do {
            System.out.println("Please enter player ID");
            playerId = InputScanner.scanner.nextLine();
        }while (!Tools.tryParseInt(playerId));

        do {
            System.out.println("Human player? y/n");
            userChoice = InputScanner.scanner.nextLine();
            validInput = userChoice.equalsIgnoreCase("y") || userChoice.equalsIgnoreCase("n");
        }while (!validInput);

        humanPLayer = userChoice;
        m_Player = new GamePlayer(humanPLayer.equalsIgnoreCase("y"), playerName, playerId);
        if(!humanPLayer.equalsIgnoreCase("y")){
            do {
                System.out.println("Please Enter Pc Move Limit");
                userChoice = InputScanner.scanner.nextLine();
            }while (!Tools.tryParseInt(userChoice));

            m_Player.setMoveLimit(Integer.parseInt(userChoice));
        }
    }

    private void validateGameDescriptorInfo(GameDescriptor i_GameDescriptor) throws GameLoadException {
        GameLoader gameloader = new GameLoader();

        m_GameBoard = gameloader.loadBoard(i_GameDescriptor);
    }

    private String getFilePathFromUser() {
        String requestedPath;

        System.out.println("Enter file path:");
        requestedPath = InputScanner.scanner.nextLine();

        return requestedPath;
    }

    private void startNewGame(){
        if(!m_GameReady){
            System.out.println("Please load game first.");
            return;
        }

        m_InGame = true;
        m_GameDurationTimer = System.currentTimeMillis();
        if(!m_Player.getIsHuman()){
            AiPlay();
        }

        printBoard();
    }

    private Boolean printBoard() {
        Boolean printed = true;

        if (!m_InGame){
            System.out.println("Board unavailable, Load Game first.");
            return !printed;
        }

        m_GameBoard.updateBlocks();
        printBoardAndHorizontalSlice();
        printVerticalSlices();
        System.out.print(System.getProperty("line.separator"));

        return printed;
    }

    private void printBoardAndHorizontalSlice(){
        for (int i = 1; i <= m_GameBoard.getBoardHeight(); i++) {
            for (int j = 1; j <= m_GameBoard.getBoardWidth(); j++) {
                try {
                    printSign(m_GameBoard.getSquare(i, j).getCurrentSquareSign());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.print(e.getMessage());
                    return;
                }
            }

            System.out.print("|");
            for(Block block:m_GameBoard.getHorizontalSlice(i - 1)){
                if(block.isMarked()){
                    System.out.print("*" + block.toString() + " ");//changed from color sign to this because of compatibility
                }
                else {
                    System.out.print(block.toString() + " ");
                }
            }

            System.out.print(System.getProperty("line.separator"));
            PrintSeparator();
        }
    }

    private void printVerticalSlices(){
        for (int i = 0; i < m_GameBoard.getMaxVerticalSlicesLength(); i++) {
            for (int j = 0; j < m_GameBoard.getBoardWidth(); j++) {
                if(i < m_GameBoard.getVerticalSlice(j).size()){
                    if(m_GameBoard.getVerticalSlice(j).get(i).isMarked()){
                        System.out.print("/");
                    }
                    else{
                        System.out.print("|");
                    }

                        System.out.print(m_GameBoard.getVerticalSlice(j).get(i).toString());
                }
                else {
                    System.out.print("| ");
                }
            }

            System.out.print("| ");
            System.out.print(System.getProperty("line.separator"));
        }
    }

    private void PrintSeparator() {
        for (int i = 0; i < m_GameBoard.getBoardWidth() + m_GameBoard.getMaxHorizontalSlicesLength(); i++){
            System.out.print("--");
        }

        System.out.print(System.getProperty("line.separator"));
    }

    private void printSign(Square.eSquareSign i_Sign) {
        if (i_Sign == Square.eSquareSign.BLACKED) {
            System.out.print("|X");
        } else if (i_Sign == Square.eSquareSign.CLEARED) {
            System.out.print("|O");
        } else {
            System.out.print("| ");
        }
    }

    private void preformPlayerMove() {
        if(!m_Player.getIsHuman()){
            System.out.print("Player not human");
            return;
        }

        if(!printBoard()){
            return;
        }

        try {
            parseAndMakeAMove();
            printBoard();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        catch (BadMoveInputException ex){
            System.out.println();
        }
    }

    private void AiPlay(){
        Random rand = new Random();
        double percentage = 0;
        int startRow,startCol, endRow, endCol;
        Square.eSquareSign sign;

        /*
        *runs until one of the conditions have been meet
        * selects randomly which blocks should be changed and to what sign
        * if there have been no progress from the selection it will choose to undo its move
         */
        while (m_Player.checkIfPlayerHasMovesLeft() && m_GameBoard.getBoardCompletionPercentage() != 100){
            sign = randSign(rand);
            startRow = rand.nextInt(m_GameBoard.getBoardHeight()) + 1;
            endRow = getRandomEndRowOrCol(startRow,m_GameBoard.getBoardHeight(),rand);
            startCol = rand.nextInt(m_GameBoard.getBoardWidth()) + 1;
            endCol = getRandomEndRowOrCol(startCol,m_GameBoard.getBoardWidth(),rand);
            try {
                m_UndoList.addFirst(m_GameBoard.insert(startRow,startCol,endRow,endCol,sign,"Pc"));
                m_RedoList.clear();
                m_Player.insertMoveToMoveList(startRow,startCol,endRow,endCol,sign,"Pc");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return;
            }

            if(percentage < m_GameBoard.getBoardCompletionPercentage()){
                printBoard();
                percentage = m_GameBoard.getBoardCompletionPercentage();
            }
            else{
                preformUndo();
            }
        }
    }

    private int getRandomEndRowOrCol(int i_Start, int i_Limit, Random i_Rand) {
        int end = i_Rand.nextInt(i_Limit) + 1;

        if(i_Start > end){
            end = i_Start;
        }

        return end;
    }

    private Square.eSquareSign randSign(Random i_Rand) {
        Square.eSquareSign sign;
        int numSign = i_Rand.nextInt(3) + 1;

        if(numSign == 1){
            sign = Square.eSquareSign.BLACKED;
        }
        else if(numSign == 2){
            sign = Square.eSquareSign.CLEARED;
        }
        else {
            sign = Square.eSquareSign.UNDEFINED;
        }

        return sign;
    }

    private void parseAndMakeAMove() throws BadMoveInputException {
        UserMoveData userData = new UserMoveData();
        String requestedMove = InputScanner.scanner.nextLine();

        if(!requestedMove.contains(",") || !requestedMove.contains(".") ){
            throw new BadMoveInputException("not enough . or , detected");
        }

        String userDataString[] = requestedMove.split(Pattern.quote("."));

        if(userDataString.length > 3){
            throw new BadMoveInputException("Too many . detected");
        }

        if (!parseToSquares(userData, userDataString[0])) {
            throw new BadMoveInputException("Bad square choice input");
        }

        if (!validateChangeTo(userData, userDataString[1])) {
            throw new BadMoveInputException("Bad coloring choice input.");
        }

        if(userDataString.length == 3) //user entered a comment
        getComment(userData, userDataString[2]);

        m_UndoList.addFirst(m_GameBoard.insert(userData.getStartSquareRowNum(),userData.getStartSquareColNum(),
                userData.getEndSquareRowNum(),userData.getEndSquareColNum(),userData.getSign(),
                userData.getComment()));
        m_Player.insertMoveToMoveList(userData.getStartSquareRowNum(),userData.getStartSquareColNum(),
                userData.getEndSquareRowNum(),userData.getEndSquareColNum(),userData.getSign(),
                userData.getComment());
        m_RedoList.clear();
    }

    private Boolean parseToSquares(UserMoveData io_userData, String i_requestedMove)
            throws BadMoveInputException{
        Integer intermediate[] = new Integer[4];
        Boolean validInput = true;
        int i = 0;

        for(String str : i_requestedMove.split(",")){
            if(!Tools.tryParseInt(str) || i > 3) {
                throw new IllegalArgumentException("Bad square choice input");
            }
            intermediate[i] = Integer.parseInt(str);
            i++;
        }

        io_userData.setStartSquareRowNum(intermediate[0]);
        io_userData.setStartSquareColNum(intermediate[1]);
        io_userData.setEndSquareRowNum(intermediate[2]);
        io_userData.setEndSquareColNum(intermediate[3]);
        if (!checkIfFirstSquareIsSmallerAndValid(io_userData)) {
            return !validInput;
        }

        if (io_userData.getStartSquareRowNum() != io_userData.getEndSquareRowNum() &&
               io_userData.getStartSquareColNum() != io_userData.getEndSquareColNum() ){
            return !validInput;
        }

        return validInput;
    }

    private boolean checkIfFirstSquareIsSmallerAndValid(UserMoveData io_userData) {
        Boolean startSquareIsSmallerAndValid = true;

        if (io_userData.getStartSquareRowNum() > io_userData.getEndSquareRowNum() ||
                io_userData.getStartSquareColNum() > io_userData.getEndSquareColNum() ||
                (io_userData.getStartSquareRowNum() < 1) || io_userData.getEndSquareColNum() < 1) {
            startSquareIsSmallerAndValid= false;
        }

        return startSquareIsSmallerAndValid;
    }

    private Boolean validateChangeTo(UserMoveData io_userData, String i_requestedMove) {
        Boolean validInput = true;

        if (i_requestedMove.equalsIgnoreCase("b")){
            io_userData.setSign(Square.eSquareSign.BLACKED);
        }
        else if(i_requestedMove.equalsIgnoreCase("c")) {
            io_userData.setSign(Square.eSquareSign.CLEARED);
        }
            else if(i_requestedMove.equalsIgnoreCase("u")){
            io_userData.setSign(Square.eSquareSign.UNDEFINED);
        }
        else {
            validInput = false;
        }

        return validInput;
    }

    private void getComment(UserMoveData io_userData, String i_requestedMove) {
        if (i_requestedMove != null) {
            io_userData.setComment(i_requestedMove);
        }
    }

    private void printPlayersMovesList() {
        if(!m_StatisticsReady || m_Player.getMoveList().size() == 0) {
            System.out.println("Move list is unavailable");
            return;
        }

        for(String move : m_Player.getMoveList()){
            System.out.println(move);
        }
    }

    private void preformUndo() {
        if(m_UndoList.isEmpty() || m_InGame == false){
            System.out.println("Undo unavailable");
            return;
        }

        try {
            m_RedoList.addFirst(undoRedoHandler(m_UndoList));
            m_Player.incrementNumOfUndos();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        printBoard();
    }

    private void preformRedo() {
        if(m_RedoList.isEmpty() || m_InGame == false){
            System.out.println("Redo unavailable");
            return;
        }

        try {
            m_UndoList.addFirst(undoRedoHandler(m_RedoList));
            m_Player.incrementNumOfRedos();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        printBoard();
    }

    private MoveSet undoRedoHandler(LinkedList<MoveSet> i_MoveSetList) throws Exception{
        MoveSet moveSet = new MoveSet(i_MoveSetList.getFirst().getComment());
        Square.eSquareSign sign;

        for(Point point: i_MoveSetList.getFirst().getPointsList()){
            sign = m_GameBoard.getSquare(point.getRowCord(), point.getColCord()).getCurrentSquareSign();
            moveSet.AddNewPoint(point.getRowCord(),point.getColCord(),sign);
            m_GameBoard.getSquare(point.getRowCord(), point.getColCord()).setCurrentSquareSign(point.getSign());
        }

        i_MoveSetList.removeFirst();

        return moveSet;
    }

    private void printStatistics() {
        if (!m_StatisticsReady){
            System.out.println("Statistics unavailable");
            return;
        }
        Long currentTime = System.currentTimeMillis();

        System.out.println(String.format("Number of Moves played: %d", m_Player.getNumOfMovesMade()));
        System.out.println(String.format("Number of Undo's made: %d", m_Player.getNumOfUndoMade()));
        System.out.println(String.format("Number of Redo's made: %d", m_Player.getNumOfRedoMade()));
        System.out.println(String.format("Play time: %d seconds", ((currentTime - m_GameDurationTimer) / 1000)));
        System.out.println(String.format("Score: %f", m_GameBoard.getBoardCompletionPercentage()));
    }

    private eGameOptions getPlayersChoiceForMenu() {
        eGameOptions playersChoice = eGameOptions.START_GAME;
        String input;
        Integer inputAsNum = 0, minVal = eGameOptions.LOAD_GAME.getOrdinalPosition(),
                maxVal = eGameOptions.QUIT.getOrdinalPosition();

        if (m_InGame) {
            minVal = eGameOptions.DISPLAY_BORD.getOrdinalPosition();
        }

        while (inputAsNum < minVal || inputAsNum > maxVal) {
            input = InputScanner.scanner.nextLine();
            if (Tools.tryParseInt(input)) {
                inputAsNum = Integer.parseInt(input);
            } else {
                System.out.println("Please select once more.");
            }
        }
        for (eGameOptions options : eGameOptions.values()) {
            if (options.getOrdinalPosition() == inputAsNum)
                playersChoice = options;
        }

        return playersChoice;
    }

    private enum eGameOptions {

        LOAD_GAME("Load new game", 1),
        START_GAME("Start new game", 2),
        DISPLAY_BORD("Display board", 3),
        PERFORM_MOVE("Preform a new move", 4),
        DISPLAY_MOVES_LIST("Display all made move", 5),
        UNDO("Undo move", 6),
        REDO("Redo move", 7),
        STATISTICS("Display statistics", 8),
        QUIT("Quit", 9);

        private String m_Name;
        private int m_OrdinalPosition;

        private eGameOptions(String i_Name, int i_OrdinalPosition) {
            m_Name = i_Name;
            m_OrdinalPosition = i_OrdinalPosition;
        }

        @Override
        public String toString() {
            return (m_Name);
        }

        public int getOrdinalPosition() {
            return m_OrdinalPosition;
        }
    }
}
