package utils;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.function.Function;

/**
 * PROGRAM: ConsoleUtils
 * AUTHOR:  Diego Balaguer
 * DATE:    19/02/2025
 */

public class ConsoleUtils {

    private static final Scanner sc = new Scanner(System.in);

    public static void closeScanner() {
        sc.close();
    }

    public static String readRequiredString(String message) {
        do {
            try {
                return readLineString(message);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e);
            }
        } while (true);
    }

    private static String readLineString(String message) {
        String input = readValueString(message);
        if (input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        } else {
            return input;
        }
    }

    public static char readRequiredChar(String message) {

        do {
            String input = readRequiredString(message);
            try {
                lengthChar(input.length());
                return input.charAt(0);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e);
            }
        } while (true);
    }

    private static void lengthChar(int length) throws IllegalArgumentException {

        if (length != 1) {
            throw new IllegalArgumentException("Only one character allowed.");
        }
    }

    private static <T> T readValue(String message, Function<String, T> parser, Class<? extends RuntimeException> expectedException) {
        do {
            try {
                String input = readRequiredString(message);
                return parser.apply(input);
            } catch (RuntimeException e) {
                if (expectedException.isInstance(e)) {
                    System.err.println("Error: " + e.getMessage());
                } else {
                    throw e;
                }
            }
        } while (true);
    }

    public static int readRequiredInt(String message) {
        return readValue(message, Integer::parseInt, NumberFormatException.class);
    }

    public static long readRequiredLong(String message) {
        return readValue(message, Long::parseLong, NumberFormatException.class);
    }

    public static double readRequiredDouble(String message) {
        return readValue(message, Double::parseDouble, NumberFormatException.class);
    }

    public static float readRequiredFloat(String message) {
        return readValue(message, Float::parseFloat, NumberFormatException.class);
    }

    public static byte readRequiredByte(String message) {
        return readValue(message, Byte::parseByte, NumberFormatException.class);
    }

    public static short readShort(String message) {
        return readValue(message, Short::parseShort, NumberFormatException.class);
    }

    public static boolean readRequiredBoolean(String message) {

        do {
            char input = Character.toUpperCase(readRequiredChar(message));
            try {
                return calculateValueBoolean(input);
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e);
            }
        } while (true);
    }

    private static boolean calculateValueBoolean(char valueBoolean) {

        return switch (valueBoolean) {
            case 'Y', 'T', 'S' -> true;
            case 'F', 'N' -> false;
            default -> throw new IllegalArgumentException("Only 'S', 'N', 'Y', 'N', 'T' or 'F' allowed.");
        };
    }

    public static String readValueString(String message) {

        System.out.print(message);
        return sc.nextLine();
    }

    public static <T> T readValueWithDefault(String message, T defaultValue, Function<String, T> parser) {
        do {
            String input = readValueString(message);
            input = input.isEmpty() ? defaultValue.toString() : input;

            try {
                return parser.apply(input);
            } catch (Exception e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static int readValueInt(String message) {
        return readValueWithDefault(message, 0, Integer::parseInt);
    }

    public static long readValueLong(String message) {
        return readValueWithDefault(message, 0L, Long::parseLong);
    }

    public static double readValueDouble(String message) {
        return readValueWithDefault(message, 0.0, Double::parseDouble);
    }

    public static float readValueFloat(String message) {
        return readValueWithDefault(message, 0.0f, Float::parseFloat);
    }

    public static byte readValueByte(String message) {
        return readValueWithDefault(message, (byte) 0, Byte::parseByte);
    }

    public static int valueIntWithoutException(String message) {
        do {
            String input = readRequiredString(message);
            if (NumberUtils.isNumber(input)) {
                return Integer.parseInt(input);
            }
        } while (true);
    }
    public static BigDecimal readRequiredBigDecimal(String message) {
        String input = readValueString(message);
        while (true) {
            try {
                System.out.print(input);
                input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("This field is required. Please enter a valid number.");
                    continue;
                }
                // Intenta convertir el valor a BigDecimal
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid decimal value.");
            }
        }
    }

}
