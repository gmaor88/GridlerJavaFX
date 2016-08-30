package UI;

import Logic.Square;

/**
 * Created by Maor Gershkovitch on 8/24/2016.
 * This class is for holding the current player move request.
 */
class UserMoveData {

    private Integer m_StartSquareRowNum = 0;
    private Integer m_StartSquareColNum = 0;
    private Integer m_EndSquareRowNum = 0;
    private Integer m_EndSquareColNum = 0;
    private Square.eSquareSign m_Sign = Square.eSquareSign.UNDEFINED;
    private String m_Comment = null;

    Square.eSquareSign getSign() {
        return m_Sign;
    }

    String getComment() {
        return m_Comment;
    }

    Integer getStartSquareRowNum() {
        return m_StartSquareRowNum;
    }

    Integer getStartSquareColNum() {
        return m_StartSquareColNum;
    }

    Integer getEndSquareRowNum() {
        return m_EndSquareRowNum;
    }

    Integer getEndSquareColNum() {
        return m_EndSquareColNum;
    }

    void setSign(Square.eSquareSign i_Sign) {
        m_Sign = i_Sign;
    }

    void setComment(String i_Comment) {
        m_Comment = i_Comment;
    }

    void setStartSquareRowNum(Integer i_StartSquareRowNum) {
        m_StartSquareRowNum = i_StartSquareRowNum;
    }

    void setStartSquareColNum(Integer i_StartSquareColNum) {
        m_StartSquareColNum = i_StartSquareColNum;
    }

    void setEndSquareRowNum(Integer i_EndSquareRowNum) {
        m_EndSquareRowNum = i_EndSquareRowNum;
    }

    void setEndSquareColNum(Integer i_EndSquareColNum) {
        m_EndSquareColNum = i_EndSquareColNum;
    }
}
