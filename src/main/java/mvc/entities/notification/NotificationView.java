package mvc.entities.notification;

import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.util.Optional;

public class NotificationView {

    private static NotificationView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Notification";

    private NotificationView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static NotificationView getInstance() {
        if (instance == null) {
            synchronized (NotificationView.class) {
                if (instance == null) {
                    instance = new NotificationView();
                }
            }
        }
        return instance;
    }

    protected Inputs getNotificationDetailsCreate(MessageMinMax playersList) {
        Optional<Integer> playerId = getEntityId("List of Players", "Input Player ID: ", playersList);

        return new Inputs.Builder()
                .put("idPlayer", playerId.orElse(null))
                .put("message", getInputMessage().orElse(null))
                .build();
    }

    protected Optional<Integer> getEntityId(String title, String inputText, MessageMinMax messageMinMax) {
        return BASE_VIEW.getReadValueIntMinMax(title, inputText, messageMinMax);
    }

    private Optional<String> getInputMessage() {
        return BASE_VIEW.getReadValueString("Enter message (30 chars): ");
    }

    protected Notification getUpdateNotificationDetails(Notification notification, MessageMinMax playersList) {
        notification.setIdPlayer(getUpdateIdPlayer(playersList, notification.getIdPlayer()));
        notification.setMessage(getUpdateMessage(notification.getMessage()));
        notification.setActive(getUpdateIsActive(notification.isActive()));
        return notification;
    }

    private int getUpdateIdPlayer(MessageMinMax message, int oldValue) {
        Optional<Integer> playerId = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", message);
        return playerId.orElse(oldValue);
    }

    private String getUpdateMessage(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    protected void displayRecordNotification(Notification notification) {
        String message = "";
        if (notification != null) {
            message += BASE_VIEW.LINE + "--- " + NAME_OBJECT + " Details ---" + BASE_VIEW.LINE;
            message += "ID: " + notification.getId() + BASE_VIEW.LINE;
            message += "Player Id: " + notification.getIdPlayer() + BASE_VIEW.LINE;
            message += "Message: " + notification.getMessage() + BASE_VIEW.LINE;
            message += "Is Active: " + (notification.isActive() ? "Yes" : "No") + BASE_VIEW.LINE;
            message += "-------------------------" + BASE_VIEW.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        BASE_VIEW.displayMessageln(message);
    }


}
