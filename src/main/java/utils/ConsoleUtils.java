package utils;

import java.util.Optional;
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
            case 'T', 'Y', 'S' -> true;
            case 'F', 'N' -> false;
            default -> throw new IllegalArgumentException("Only 'T', 'F', 'S', 'Y' or 'N' allowed.");
        };
    }


    public static Optional<Boolean> readBooleanWithDefault(String message, Optional<Boolean> defaultValue) {
        do {
            String input = readValueString(message);

            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                char value = input.toUpperCase().charAt(0);
                return Optional.of(calculateValueBoolean(value));
            } catch (Exception e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static String readValueString(String message) {

        System.out.print(message);
        return sc.nextLine();
    }

    public static Optional<String> readStringWithDefault(String message, Optional<String> defaultValue) {
        do {
            String input = readValueString(message);

            if (input.isEmpty()) {
                return defaultValue;
            }
            try {
                return Optional.of(input);
            } catch (Exception e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static <T> Optional<T> readValueWithDefault(String message, Optional<T> defaultValue, Function<String, T> parser) {
        do {
            String input = readValueString(message);

            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(parser.apply(input));
            } catch (Exception e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static Optional<Integer> readValueInt(String message) {
        do {
            String input = readValueString(message);
            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static Optional<Long> readValueLong(String message) {
        do {
            String input = readValueString(message);
            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Long.parseLong(input));
            } catch (NumberFormatException e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static Optional<Double> readValueDouble(String message) {
        do {
            String input = readValueString(message);
            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static Optional<Float> readValueFloat(String message) {
        do {
            String input = readValueString(message);
            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Float.parseFloat(input));
            } catch (NumberFormatException e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
    }

    public static Optional<Byte> readValueByte(String message) {
        do {
            String input = readValueString(message);
            if (input.isEmpty()) {
                return Optional.empty();
            }
            try {
                return Optional.of(Byte.parseByte(input));
            } catch (NumberFormatException e) {
                System.err.println("Error: input not valid (" + e.getMessage() + ").");
            }
        } while (true);
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
        while (true) {
            try {
                System.out.print(message);
                String input = sc.nextLine().trim(); // Solo solicita entrada una vez
                if (input.isEmpty()) {
                    System.out.println("This field is required. Please enter a valid number.");
                    continue; // Solicita nuevamente si el campo está vacío
                }
                return new BigDecimal(input); // Devuelve el valor si es válido
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid decimal value."); // Maneja errores
            }
        }
    }
    public static Optional<String> readOptionalString(String message) {
        System.out.print(message); // Mostrar el mensaje
        String input = sc.nextLine().trim(); // Leer entrada del usuario (eliminando espacios al inicio o final)

        if (input.isEmpty()) {
            return Optional.empty(); // Devuelve vacío si no hay entrada
        }
        return Optional.of(input); // Devuelve el valor opcional si hay entrada
    }


}
