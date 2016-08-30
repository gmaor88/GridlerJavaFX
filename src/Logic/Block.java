package Logic;

/**
 * Created by dan on 8/23/2016.
 * logic class which defines a block - number of black squares in a row
 */
public class Block {
    private final Integer f_Size;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private Boolean m_Marked = false;

    Block(Integer i_Size){
        f_Size = i_Size;
    }

    void setMarked(Boolean m_Marked) {
        this.m_Marked = m_Marked;
    }

    public Boolean isMarked() {
        return m_Marked;
    }

    Integer getSize() {
        return f_Size;
    }

    @Override
    public String toString() {
        String str;

        //if(m_Marked){
          //  str = ANSI_RED + f_Size.toString() + ANSI_RESET;
        //}
        //else{
            str = f_Size.toString();
        //}

        return str;
    }
}
