package mvc.entities.notification;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;

import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.view.BaseView;

import java.util.List;
import java.util.Optional;

public class NotificationController {

    private static NotificationController instance;
    private final NotificationService NOTIFICATION_SERVICE;
    private final NotificationView NOTIFICATION_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Notification";

    private NotificationController(NotificationService notificationService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.NOTIFICATION_SERVICE = notificationService;
        NOTIFICATION_VIEW = NotificationView.getInstance();
    }

    public static NotificationController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (NotificationController.class) {
                if (instance == null) {
                    NotificationService service = ServiceManager.getInstance().getNotificationService();
                    instance = new NotificationController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createNotification();
                    case LIST_ALL -> listAllNotifications();
                    case FIND_BY_ID -> getNotificationById();
                    case UPDATE -> updateNotification();
                    case SOFT_DELETE -> softDeleteNotificationById();
                    case DELETE -> deleteNotificationById();

                    default -> BASE_VIEW.displayErrorMessage("The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void createNotification() {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");

        Inputs inputsValue = NOTIFICATION_VIEW.getNotificationDetailsCreate(NOTIFICATION_SERVICE.getAllPlayers());
        Optional<Integer> playerId = inputsValue.getFieldAs("playerId", Integer.class);
        Optional<String> message = inputsValue.getFieldAs("message", String.class);

        NOTIFICATION_SERVICE.createNotification(playerId, message);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " Player unsubscribed successfully.");
    }

    private void getNotificationById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        int notificationId = getNotificationIdWithList();
        Optional<Notification> optionalNotification = NOTIFICATION_SERVICE.getNotificationById(notificationId);
        NOTIFICATION_VIEW.displayRecordNotification(optionalNotification.get());
    }

    private void listAllNotifications() {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessage2ln(NOTIFICATION_SERVICE.getAllNotificationsCompleteInfoDto().getMessage());
    }

    private void listNotificationsByPlayer() throws DatabaseConnectionException, NotFoundException {
        BASE_VIEW.displayMessage2ln("####  LIST " + NAME_OBJECT.toUpperCase() + "S BY PLAYER  #################");
        int playerId = getPlayerId().get();
        List<NotificationDisplayDTO> notifications = NOTIFICATION_SERVICE.getNotificationsByPlayerId(playerId);
        BASE_VIEW.displayMessage2ln(NOTIFICATION_SERVICE.displayNotificationListDto(notifications));
    }

    private void updateNotification() throws NotFoundException, DatabaseConnectionException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");

        int notificationId = getNotificationIdWithList();
        Optional<Notification> existNotificationOpt = NOTIFICATION_SERVICE.getNotificationById(notificationId);
        if (existNotificationOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        NOTIFICATION_VIEW.displayRecordNotification(existNotificationOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for not changes.");
        Notification updatedNotification = NOTIFICATION_VIEW.getUpdateNotificationDetails(existNotificationOpt.get(), NOTIFICATION_SERVICE.getAllPlayers());

        NOTIFICATION_SERVICE.updateNotification(updatedNotification);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteNotificationById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        NOTIFICATION_SERVICE.deleteNotification(getNotificationIdWithList());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteNotificationById() throws IllegalArgumentException, NotFoundException, DAOException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");

        NOTIFICATION_SERVICE.softDeleteNotification(getNotificationIdWithList());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private int getNotificationIdWithList() throws DAOException, NotFoundException, IllegalArgumentException {
        Optional<Integer> searchID = BASE_VIEW.getReadValueIntMinMax("List of Notifications", "Input Notification ID: ", NOTIFICATION_SERVICE.getAllNotificationsCompleteInfoDto());
        if (searchID.isEmpty()) {
            String message = NAME_OBJECT + " ID required.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        if (NOTIFICATION_SERVICE.getNotificationById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID " + searchID.get() + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return searchID.get();
    }

    private Optional<Integer> getPlayerId() throws NotFoundException {
        Optional<Integer> playerIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", NOTIFICATION_SERVICE.getAllPlayers());
        if (playerIdOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return playerIdOpt;
    }
}