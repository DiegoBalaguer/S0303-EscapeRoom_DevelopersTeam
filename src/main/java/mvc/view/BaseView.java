package mvc.view;

import utils.ConsoleUtils;

import java.util.Optional;

public class BaseView {

    public final String LINE = System.lineSeparator();

    public int getReadRequiredInt(String message) {
        return ConsoleUtils.readRequiredInt(message);
    }

    public String getReadRequiredString(String message) {
        return ConsoleUtils.readRequiredString(message);
    }

    public Optional<Integer> getReadValueInt(String message) {
        return ConsoleUtils.readValueInt(message);
    }

    public void displayMessage2ln(String message) {
        System.out.println(LINE + message + LINE);
    }

    public void displayMessageln(String message) {
        System.out.println(message);
    }

    public void displayMessage(String message) {
        System.out.print(message);
    }

    public void displayErrorMessage(String message) {
        System.err.println(LINE + "ERROR: " + message + LINE);
    }

    public void displayDebugMessage(String message) {
        System.out.print(LINE + "DEBUG: " + message + LINE);
    }
}
