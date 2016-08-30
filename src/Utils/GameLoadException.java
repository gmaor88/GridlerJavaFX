package Utils;

/**
 * Created by Maor Gershkovitch on 8/17/2016.
 * Exception class for Game load errors (thrown from GameLoader).
 */
public class GameLoadException extends Exception {
    String m_ErrorMsg;

    public GameLoadException(String i_ErrorMsg){
        m_ErrorMsg = i_ErrorMsg;
    }

    public String getErorMsg() {
        return m_ErrorMsg;
    }

}
