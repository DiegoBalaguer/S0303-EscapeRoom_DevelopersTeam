package mvc.view;

import config.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;

import java.util.Optional;

@Slf4j
public class BaseView {

    private static BaseView baseViewInstance;
    private final boolean DEBUG = LoadConfigApp.getAppDebug();
    public final String LINE = System.lineSeparator();

    private BaseView() {
        displayDebugMessage("Creation Class: " + this.getClass().getName());
    }

    public static BaseView getInstance() {
        if (baseViewInstance == null) {
            synchronized (BaseView.class) {
                if (baseViewInstance == null) {
                    baseViewInstance = new BaseView();
                }
            }
        }
        return baseViewInstance;
    }

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
        log.error("ERROR: " + message);
    }

    public void displayDebugMessage(String message) {
        if (DEBUG) {
            System.out.print(LINE + "DEBUG: " + message + LINE);
        }
        log.error("DEBUG: " + message);
    }
}
