package mvc.entities.escapeRoom;

import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.util.Optional;

public class EscapeRoomView {

    private static EscapeRoomView instance;
    private static BaseView baseView = BaseView.getInstance();
    private static final String NAME_OBJECT = "EscapeRoom";

    private EscapeRoomView() {
        baseView = BaseView.getInstance();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static EscapeRoomView getInstance() {
        if (instance == null) {
            synchronized (EscapeRoomView.class) {
                if (instance == null) {
                    instance = new EscapeRoomView();
                }
            }
        }
        return instance;
    }

    protected void getUpdateBusinessDetails(EscapeRoom escapeRoom) {
        try {
            escapeRoom.setName(getUpdateName(escapeRoom.getName()));
            escapeRoom.setAddress(getUpdateAddress(escapeRoom.getAddress()));
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing " + NAME_OBJECT + ": " + e.getMessage());
            throw new IllegalArgumentException("Error editing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdateAddress(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    protected void displayRecordBusiness(EscapeRoom escapeRoom) {
        String message = "";
        if (escapeRoom != null) {
            message += baseView.LINE + "--- " + NAME_OBJECT + " Details ---" + baseView.LINE;
            message += "ID: " + escapeRoom.getId() + baseView.LINE;
            message += "Name: " + escapeRoom.getName() + baseView.LINE;
            message += "Address: " + escapeRoom.getAddress() + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        baseView.displayMessageln(message);
    }
}
