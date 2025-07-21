package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import mvc.entities.escapeRoom.EscapeRoomController;
import inventory.InventoryController;
import mvc.entities.notification.NotificationController;
import mvc.enumsMenu.OptionsMenuMain;
import mvc.entities.escapeRoom.EscapeRoom;
import mvc.view.BaseView;
import mvc.entities.player.PlayerController;
import mvc.entities.room.RoomController;
import mvc.entities.sale.SaleController;

public class AppController {

    private static AppController appControllerInstance;
    private final BaseView BASE_VIEW;
    private static EscapeRoom escapeRoom;

    private AppController() throws DatabaseConnectionException {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        escapeRoom = EscapeRoom.getInstance();
    }

    public static AppController getInstance(EscapeRoom escapeRoom) throws DatabaseConnectionException {
        if (appControllerInstance == null) {
            synchronized (AppController.class) {
                if (appControllerInstance == null) {
                    appControllerInstance = new AppController();
                }
            }
        }
        return appControllerInstance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuMain.viewMenu(escapeRoom.getName()));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            try {
                OptionsMenuMain idMenu = OptionsMenuMain.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        return;
                    }
                    case TICKET_MANAGEMENT -> SaleController.getInstance().mainMenu();
                    case ROOM_MANAGEMENT -> RoomController.getInstance().mainMenu();
                    case PLAYER_MANAGEMENT -> PlayerController.getInstance().mainMenu();
                    case ESCAPE_ROOM_MANAGEMENT -> EscapeRoomController.getInstance(escapeRoom).mainMenu();
                    case FINANCIAL_MANAGEMENT -> InventoryController.getInstance().showInventoryMenu();
                    case NOTIFICATION_MANAGEMENT ->  NotificationController.getInstance().mainMenu();

                    default -> BASE_VIEW.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            }
         } while (true);
    }
}