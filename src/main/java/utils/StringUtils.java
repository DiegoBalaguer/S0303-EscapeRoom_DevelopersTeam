package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * PROGRAM: StringUtils
 * AUTHOR:  Diego Balaguer
 * DATE:    19/02/2025
 */

public class StringUtils {

    public static String generateRandomString(int quantity) {

        StringBuilder passwd = new StringBuilder();
        char letter = 0;

        for (int i = 0; i < quantity; i++) {
            int type = NumberUtils.random(1, 3);
            switch (type) {
                case 1 -> letter = (char) NumberUtils.random('A', 'Z');
                case 2 -> letter = (char) NumberUtils.random('a', 'z');
                case 3 -> letter = (char) NumberUtils.random('0', '9');
            }
            passwd.append(letter);
        }
        return passwd.toString();
    }

    public static String generateRandomStringCapsNums(int quantity) {

        StringBuilder passwd = new StringBuilder();
        char letter = 0;

        for (int i = 0; i < quantity; i++) {
            int type = NumberUtils.random(1, 2);
            switch (type) {
                case 1 -> letter = (char) NumberUtils.random('A', 'Z');
                case 2 -> letter = (char) NumberUtils.random('0', '9');
            }
            passwd.append(letter);
        }
        return passwd.toString();
    }

    public static String formatToChars(String text, int lengthString) {

        text = (text == null || text.isEmpty()) ? "" : text;
        lengthString = (lengthString <= 0) ? text.length() : lengthString;

        if (text.length() > lengthString) {
            return text.substring(0, lengthString); // Truncate if it is too long.
        } else {
            return String.format("%-" + lengthString + "s", text); // Fill with spaces if it is short.
        }
    }
    public static String makeLineToList(Map<String, Integer> dataLine) {
        StringBuilder resultado = new StringBuilder();
        dataLine.forEach((k, v) -> {
            resultado.append(formatToChars(k, v));
        });
        return resultado.toString();
    }

    public static String getDateFormatUSA(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getDateFormatUSA(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

}
