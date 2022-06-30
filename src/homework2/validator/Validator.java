package homework2.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

    public static int validateInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static boolean validateDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
