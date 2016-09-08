package Logic;

/**
 * Created by Maor Gershkovitch on 8/21/2016.
 * logic class used by MoveSet a point is much like class Square only it knows its position on the game board
 * and its current sign only
 */
public class Point {
    private Integer m_RowCord;
    private Integer m_ColCord;
    private Square.eSquareSign m_Sign;

    Point(Integer i_RowCord, Integer i_ColCord, Square.eSquareSign i_Sign){
        m_RowCord = i_RowCord;
        m_ColCord = i_ColCord;
        m_Sign = i_Sign;
    }

    public Square.eSquareSign getSign() {
        return m_Sign;
    }

    public Integer getColCord() {
        return m_ColCord;
    }

    public Integer getRowCord() {
        return m_RowCord;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", m_RowCord, m_ColCord);
    }
}
