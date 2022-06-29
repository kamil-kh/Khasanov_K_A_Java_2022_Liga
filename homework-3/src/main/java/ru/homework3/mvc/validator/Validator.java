package ru.homework3.mvc.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

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

    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("[А-ЯA-Z][а-яa-z]+");
        return pattern.matcher(name).matches();
    }
}
