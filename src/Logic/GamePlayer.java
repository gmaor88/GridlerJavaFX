package Logic;

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
    private Integer m_MovesLimit = 0;
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

    public void setMoveLimit(Integer i_MoveLimit){
        m_MovesLimit = i_MoveLimit;
    }

    public LinkedList<String> getMoveList(){
        return m_MoveList;
    }

    public  void insertMoveToMoveList(int i_StartRow, int i_StartColumn, int i_EndRow, int i_EndColumn,
                                      Square.eSquareSign i_Sign, String i_Comment){
        m_MoveList.addFirst(i_StartRow + "," + i_StartColumn + " " + i_EndRow + "," +
                i_EndColumn + " " + i_Sign + " " + hasComment(i_Comment));
        m_NumOfMovesMade++;
    }

    private String hasComment(String i_Comment){
        if(i_Comment != null){
            return i_Comment;
        }
        return "";
    }

    public Boolean getIsHuman() {
        return f_IsHuman;
    }

    public String getName() {
        return f_Name;
    }

    public Integer getMovesLimit() {
        return m_MovesLimit;
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
        Boolean playerHasMoveLeft = true;

        if(m_NumOfMovesMade >= m_MovesLimit) {
            playerHasMoveLeft = false;
        }

        return playerHasMoveLeft;
    }
}