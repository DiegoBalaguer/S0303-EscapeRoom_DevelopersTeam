package mvc.controller;

import dao.exceptions.DAOException;
import mvc.enumsMenu.OptionsMenuMain;
import config.loadConfigApp.LoadConfigApp;
import mvc.view.BaseView;

public class AppController {

    private static AppController appControllerInstance;
    private BaseView baseView;

    private AppController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    public static AppController getInstance() {
        if (appControllerInstance == null) {
            synchronized (AppController.class) {
                if (appControllerInstance == null) {
                    appControllerInstance = new AppController();
                }
            }
        }
        return appControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuMain.viewMenu(LoadConfigApp.getAppName()));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            try {
                OptionsMenuMain idMenu = OptionsMenuMain.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        return;
                    }
                    case TICKET_MANAGEMENT -> SaleController.getInstance().mainMenu();
                    case ROOM_MANAGEMENT -> RoomController.getInstance().mainMenu();
                    case PLAYER_MANAGEMENT -> PlayerController.getInstance().mainMenu();
                    case ESCAPE_ROOM_MANAGEMENT -> EscapeRoomController.getInstance().mainMenu();
                    case FINANCIAL_MANAGEMENT -> InventoryController.getInstance().showInventoryMenu();

                    default -> baseView.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                baseView.displayErrorMessage("Error: The value in menu is wrong." + e.getMessage());
            } catch (DAOException e) {
                baseView.displayErrorMessage("Error: Database operation failed: " + e.getMessage());
            } catch (Exception e) {
                baseView.displayErrorMessage("Error: An unexpected error occurred: " + e.getMessage());
            }
     } while (true);
}

}