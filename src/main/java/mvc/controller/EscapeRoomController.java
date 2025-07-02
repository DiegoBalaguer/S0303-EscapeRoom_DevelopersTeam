package mvc.controller;

import config.LoadConfigApp;
import config.LoadConfigDB;
import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.mongo.MongoDBConnection;
import mvc.enumsMenu.*;
import mvc.model.Clue;
import mvc.model.EscapeRoom;
import mvc.view.BaseView;
import mvc.view.EscapeRoomView;

import java.util.Optional;

public class EscapeRoomController {

    private static EscapeRoomController escapeRoomControllerInstance;
    private BaseView baseView;
    private EscapeRoomView escapeRoomView;
    private final String NAME_OBJECT = "Escape Room";
    private static EscapeRoom escapeRoom;

    private EscapeRoomController(EscapeRoom escapeRoom) {
        baseView = new BaseView();
        escapeRoomView = new EscapeRoomView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.escapeRoom = escapeRoom;
    }

    public static EscapeRoomController getInstance(EscapeRoom escapeRoom) {
        if (escapeRoomControllerInstance == null) {
            synchronized (EscapeRoomController.class) {
                if (escapeRoomControllerInstance == null) {
                    escapeRoomControllerInstance = new EscapeRoomController(escapeRoom);
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
                    case REWARD_MANAGEMENT -> RewardController.getInstance().mainMenu();
                    case CERTIFICATE_MANAGEMENT -> CertificateController.getInstance().mainMenu();
                    case SHOW_BUSINESS_DATA -> showBusinessData();
                    case UPDATE_DATA_BUSINESS -> updateDataBusiness();
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

    private void showBusinessData() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + "  #################");
        escapeRoomView.displayRecordBusiness(escapeRoom);
    }

    private void updateDataBusiness() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            escapeRoomView.displayRecordBusiness(escapeRoom);

            baseView.displayMessage2ln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for not changes.");
            escapeRoomView.getUpdateBusinessDetails(escapeRoom);
            updateDataMongodb();
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + escapeRoom.getName() + " (ID: " + escapeRoom.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateDataMongodb() {
        if (LoadConfigDB.getMongodbEnable()) {
            try {
                MongoDBConnection connection = MongoDBConnection.getInstance();
                connection.setEscapeRoomName(escapeRoom.getId(), escapeRoom.getName());
                connection.setEscapeRoomAddress(escapeRoom.getId(), escapeRoom.getAddress());
            } catch (DatabaseConnectionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
