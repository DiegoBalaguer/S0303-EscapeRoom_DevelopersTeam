package mvc.view;

import utils.ConsoleUtils;

public class BaseView {

    public final String LINE = System.lineSeparator();



    public int getInputRequiredInt(String message) {
        return ConsoleUtils.readRequiredInt(message);
    }

    public String getInputRequiredString(String message) {
        return ConsoleUtils.readRequiredString(message);
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


}
