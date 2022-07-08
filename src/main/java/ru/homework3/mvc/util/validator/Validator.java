package ru.homework3.mvc.util.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Validator {

    public static boolean validateDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean validateName(String name) {
        if (name == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[А-ЯA-Z][а-яa-z]+");
        return pattern.matcher(name).matches();
    }

    public static boolean isEmpty(String str) {
        try {
            return str.compareTo("") == 0;
        } catch (NullPointerException ex) {
            return true;
        }
    }

    public static boolean validateStatus(String status) {
        try {
            return status.toLowerCase().compareTo("новая") == 0 ||
                    status.toLowerCase().compareTo("в работе") == 0 ||
                    status.toLowerCase().compareTo("готово") == 0;
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
