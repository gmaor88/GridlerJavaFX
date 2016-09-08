package Logic;

import Utils.BadMoveInputException;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private Long m_GameDurationTimer;
    private LinkedList<MoveSet> m_UndoList = new LinkedList<>();
    private LinkedList<MoveSet> m_RedoList = new LinkedList<>();
    private LinkedList<String> m_MoveList = new LinkedList();
    private Integer m_NumOfMovesMade = 0;
    private Integer m_NumOfUndoMade = 0;
    private Integer m_NumOfRedoMade = 0;

    public GamePlayer(Boolean i_isHuman, String i_Name, String i_Id){
        f_IsHuman = i_isHuman;
        f_Name = i_Name;
        f_Id = i_Id;
    }

    public void setGameBoarrd(GameBoard i_GameBoard){
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

    public void insertMoveToMoveList(int i_StartRow, int i_StartColumn, int i_EndRow, int i_EndColumn,
                                      Square.eSquareSign i_Sign, String i_Comment){
        m_MoveList.addFirst(i_StartRow + "," + i_StartColumn + " " + i_EndRow + "," +
                i_EndColumn + " " + i_Sign + " " + hasComment(i_Comment));
        m_NumOfMovesMade++;
    }

    private void insertMoveToMoveList(MoveSet i_move){
        m_MoveList.addFirst(i_move.toString());
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
        return m_GameBoard.getSquare(i_RowIndex,i_ColumnIndex).getCurrentSquareSign();//need to catch??
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
        m_NumOfMovesMade--;
        m_NumOfUndoMade++;
        m_MoveList.removeFirst();
    }

    public void incrementNumOfRedos(){
        m_NumOfMovesMade++;
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
    }

    public void undo() {
        m_RedoList.addFirst(undoRedoHandler(m_UndoList));
        incrementNumOfUndos();
    }

    public void redo() {
        m_UndoList.addFirst(undoRedoHandler(m_RedoList));
        incrementNumOfRedos();
    }

    private MoveSet undoRedoHandler(LinkedList<MoveSet> i_MoveSetList){
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
}