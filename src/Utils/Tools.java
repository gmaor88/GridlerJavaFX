package Utils;

/**
 * Created by dan on 8/22/2016.
 * for general utils.
 */
public class Tools {
   public static Boolean tryParseInt(String i_Value) {
        try {
            Integer.parseInt(i_Value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
