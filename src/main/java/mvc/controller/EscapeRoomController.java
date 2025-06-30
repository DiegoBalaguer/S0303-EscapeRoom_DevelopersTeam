package mvc.controller;

import dao.exceptions.DAOException;
import mvc.enumsMenu.*;
import mvc.view.BaseView;

public class EscapeRoomController {

    private static EscapeRoomController escapeRoomControllerInstance;
    private static BaseView baseView;
    private static final String NAME_OBJECT = "Escape Room";

    private EscapeRoomController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
    }

    public static EscapeRoomController getInstance() {
        if (escapeRoomControllerInstance == null) {
            synchronized (EscapeRoomController.class) {
                if (escapeRoomControllerInstance == null) {
                    escapeRoomControllerInstance = new EscapeRoomController();
                }
            }
        }
        return escapeRoomControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuEscapeRoom.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            try {
                OptionsMenuEscapeRoom idMenu = OptionsMenuEscapeRoom.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CLUE_MANAGEMENT -> ClueController.getInstance().mainMenu();
                    case DECORATION_MANAGEMENT -> DecorationController.getInstance().mainMenu();
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