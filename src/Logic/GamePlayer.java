package Logic;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Maor Gershkovitch on 8/8/2016.
 * logic class which contains all relevant player data
 */
public class GamePlayer {
    private final String f_Name;
    private  final String f_Id;
    private final Boolean f_IsHuman;
    private GameBoard m_GameBoard;
    private Integer m_TurnLimit = 0;
    private Integer m_TurnNumber = 0;
    private LinkedList<MoveSet> m_UndoList = new LinkedList<>();
    private LinkedList<MoveSet> m_RedoList = new LinkedList<>();
    private LinkedList<String> m_MoveList = new LinkedList();
    private Integer m_NumOfMovesMade = 0;
    private Integer m_NumOfUndoMade = 0;
    private Integer m_NumOfRedoMade = 0;
    private double m_Score = 0;
    private long m_Timer = 0;

    public GamePlayer(Boolean i_isHuman, String i_Name, String i_Id){
        f_IsHuman = i_isHuman;
        f_Name = i_Name;
        f_Id = i_Id;
    }

    public void incrementTime(){
        m_Timer++;
    }

    public long getTimer() {
        return m_Timer;
    }

    public void incrementNumberOfMoves(){m_NumOfMovesMade++;}

    public void setGameBoard(GameBoard i_GameBoard){
        m_GameBoard = new GameBoard(i_GameBoard);
    }

    public  String getId(){return  f_Id;}

    public boolean isUndoAvailable(){
        return !m_UndoList.isEmpty();
    }

    public boolean isRedoAvailable(){
        return !m_RedoList.isEmpty();
    }

    public void setMoveLimit(Integer i_TurnLimit){
        m_TurnLimit = i_TurnLimit;
    }

    public LinkedList<String> getMoveList(){
        return m_MoveList;
    }

    public double getScore() {
        return m_Score;
    }

    public void insertMoveToMoveList(int i_StartRow, int i_StartColumn, int i_EndRow, int i_EndColumn,
                                     Square.eSquareSign i_Sign, String i_Comment){
        m_MoveList.addFirst(i_StartRow + "," + i_StartColumn + " " + i_EndRow + "," +
                i_EndColumn + " " + i_Sign + " " + hasComment(i_Comment));
        m_NumOfMovesMade++;
    }

    private void insertMoveToMoveList(MoveSet i_Move){
        if(i_Move.getPointsList().size() > 0) {
            m_MoveList.addFirst(i_Move.toString());
        }

        m_NumOfMovesMade++;
    }

    private String hasComment(String i_Comment){
        if(i_Comment != null){
            return i_Comment;
        }
        return "";
    }

    public Integer getTurnNumber() {
        return m_TurnNumber;
    }

    public void endTurn(){
        m_TurnNumber++;
        m_NumOfMovesMade = 0;
    }

    public Square.eSquareSign getGameBoardSquareSign(int i_RowIndex, int i_ColumnIndex){
        return m_GameBoard.getSquare(i_RowIndex, i_ColumnIndex).getCurrentSquareSign();//need to catch?? //was with + 1
    }

    public ArrayList getHorizontalSlice(int i_RowIndex){
        return m_GameBoard.getHorizontalSlice(i_RowIndex);
    }

    public ArrayList getVerticalSlice(int i_ColumnIndex){
        return m_GameBoard.getVerticalSlice(i_ColumnIndex);
    }

    public boolean isGameBoardHorizontalBlockPerfect(int i_RowIndex, int i_ColumnIndex){
        return m_GameBoard.getHorizontalSlice(i_RowIndex).get(i_ColumnIndex).isMarked();
    }

    public boolean isGameBoardVerticalBlockPerfect(int i_RowIndex, int i_ColumnIndex){
        return m_GameBoard.getVerticalSlice(i_ColumnIndex).get(i_RowIndex).isMarked();
    }

    public Boolean getIsHuman() {
        return f_IsHuman;
    }

    public String getName() {
        return f_Name;
    }

    public Integer getTurnLimit() {
        return m_TurnLimit;
    }

    public Integer getNumOfMovesMade() {
        return m_NumOfMovesMade;
    }

    public Integer getNumOfUndoMade() {
        return m_NumOfUndoMade;
    }

    public Integer getNumOfRedoMade() {
        return m_NumOfRedoMade;
    }

    public void incrementNumOfUndos(){
        if(m_NumOfMovesMade > 0){
            m_NumOfMovesMade--;
        }

        m_NumOfUndoMade++;
        m_MoveList.removeFirst();
    }

    public void incrementNumOfRedos(MoveSet i_Move){
        insertMoveToMoveList(i_Move);
        //m_NumOfMovesMade++;
        m_NumOfRedoMade++;
    }

    public Boolean checkIfPlayerHasMovesLeft(){
        return m_NumOfMovesMade < 2;
    }

    public Boolean checkIfPlayerHasTurnLeft(){
        return m_TurnNumber < m_TurnLimit;
    }

    public void preformPlayerMove(MoveSet i_Move) {
        m_UndoList.addFirst(m_GameBoard.insert(i_Move));
        insertMoveToMoveList(i_Move);
        m_RedoList.clear();
        m_Score = m_GameBoard.getBoardCompletionPercentage();
    }

    public void undo() {
        m_RedoList.addFirst(undoRedoHandler(m_UndoList));
        incrementNumOfUndos();
        m_Score = m_GameBoard.getBoardCompletionPercentage();
    }

    public void redo() {
        incrementNumOfRedos(m_RedoList.getFirst());//maybe peek?
        m_UndoList.addFirst(undoRedoHandler(m_RedoList));
        m_Score = m_GameBoard.getBoardCompletionPercentage();
        //incrementNumOfRedos();
    }

    private MoveSet undoRedoHandler(LinkedList<MoveSet> i_MoveSetList){
        MoveSet moveSet = new MoveSet(i_MoveSetList.getFirst().getComment());
        Square.eSquareSign sign;

        for(Point point: i_MoveSetList.getFirst().getPointsList()){
            sign = getGameBoardSquareSign(point.getRowCord(),point.getColCord());
            //sign = m_GameBoard.getSquare(point.getRowCord() + 1, point.getColCord() + 1).getCurrentSquareSign();
            moveSet.AddNewPoint(point.getRowCord(),point.getColCord(),sign);
            m_GameBoard.getSquare(point.getRowCord(), point.getColCord()).setCurrentSquareSign(point.getSign());//was with + 1
        }

        i_MoveSetList.removeFirst();

        return moveSet;
    }

    public void AiPlay(){
        Random rand = new Random();
        int startRow,startCol, endRow, endCol;
        Square.eSquareSign sign;

        /*
        *runs until one of the conditions have been meet
        * selects randomly which blocks should be changed and to what sign
        * if there have been no progress from the selection it will choose to undo its move
         */
        while (checkIfPlayerHasMovesLeft() && m_GameBoard.getBoardCompletionPercentage() != 100){
            sign = randSign(rand);
            startRow = rand.nextInt(m_GameBoard.getBoardHeight());
            endRow = getRandomEndRowOrCol(startRow,m_GameBoard.getBoardHeight(),rand);
            startCol = rand.nextInt(m_GameBoard.getBoardWidth());
            endCol = getRandomEndRowOrCol(startCol,m_GameBoard.getBoardWidth(),rand);
            try {
                m_UndoList.addFirst(m_GameBoard.insert(startRow,startCol,endRow,endCol,sign,"Pc"));
                m_RedoList.clear();
                insertMoveToMoveList(startRow,startCol,endRow,endCol,sign,"Pc");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return;
            }

            if(m_Score < m_GameBoard.getBoardCompletionPercentage()){
                m_Score = m_GameBoard.getBoardCompletionPercentage();
            }
            else{
                undo();
            }
        }
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

    private int getRandomEndRowOrCol(int i_Start, int i_Limit, Random i_Rand) {
        int end = i_Rand.nextInt(i_Limit);

        if(i_Start > end){
            end = i_Start;
        }

        return end;
    }

    public void updateBlocks(){
        m_GameBoard.updateBlocks();
    }
}