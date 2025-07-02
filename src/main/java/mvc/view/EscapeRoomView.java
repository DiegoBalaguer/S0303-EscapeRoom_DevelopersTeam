package mvc.view;

import mvc.model.EscapeRoom;
import utils.ConsoleUtils;

import java.util.Optional;

public class EscapeRoomView {

    private static BaseView baseView = new BaseView();
    private static final String NAME_OBJECT = "EscapeRoom";


    public void getUpdateBusinessDetails(EscapeRoom escapeRoom) {
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

    public void displayRecordBusiness(EscapeRoom escapeRoom) {
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

