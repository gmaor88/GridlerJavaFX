package Utils;

/**
 * Created by Maor Gershkovitch on 8/24/2016.
 * Exception class for Bad Move Input.
 */
public class BadMoveInputException extends Throwable {
    String m_ErrorMsg = "Wrong input form.";

    public BadMoveInputException(String i_ErrorMsg){
        m_ErrorMsg = i_ErrorMsg;
    }

    public BadMoveInputException(){}

    public String getErorMsg() {
        return m_ErrorMsg;
    }

}
