package Logic;

import java.util.LinkedList;

/**
 * Created by Maor Gershkovitch on 8/19/2016.
 * logic class which saves and defines the player moves - used for undo and redo
 */
public class MoveSet {
    private LinkedList<Point> m_PointsList = new LinkedList<>();
    private String m_Comment = null;

    public MoveSet(String i_Comment){
        m_Comment = i_Comment;
    }

    public void AddNewPoint(Integer i_RowCord, Integer i_ColCord, Square.eSquareSign i_Sign){
        Point pointToAdd = new Point(i_RowCord, i_ColCord, i_Sign);
        m_PointsList.addFirst(pointToAdd);
    }

    public void AddNewPoint(Point i_Point){
        m_PointsList.addFirst(i_Point);
    }

    public LinkedList<Point> getPointsList(){
        return  m_PointsList;
    }

    public String getComment (){
        return m_Comment;
    }

    @Override
    public String toString(){
        String Msg;

        Msg = m_Comment + ": ";
        for (Point point:m_PointsList){
            Msg += point.toString();
        }

        Msg += ". " + m_PointsList.getFirst().getSign().toString();

        return Msg;
    }
}
