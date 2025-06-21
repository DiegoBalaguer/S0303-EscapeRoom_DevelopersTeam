package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String getDateFormatUSA(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
