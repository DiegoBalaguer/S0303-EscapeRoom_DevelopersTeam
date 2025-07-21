package mvc.entities.escapeRoom;

import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;
import mvc.entities.certificate.CertificateController;
import mvc.entities.clue.ClueController;
import config.LoadConfigDB;
import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import mongo.MongoDBConnection;
import mvc.entities.decoration.DecorationController;
import mvc.view.BaseView;
import mvc.entities.reward.RewardController;

public class EscapeRoomController {

    private static EscapeRoomController instance;
    private final EscapeRoomService ESCAPE_ROOM_SERVICE;
    private final BaseView BASE_VIEW;
    private final EscapeRoomView ESCAPE_ROOM_VIEW;
    private final String NAME_OBJECT = "Escape Room";
    private static EscapeRoom escapeRoom;

    private EscapeRoomController(EscapeRoom escapeRoom, EscapeRoomService escapeRoomService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.ESCAPE_ROOM_SERVICE = escapeRoomService;
        ESCAPE_ROOM_VIEW = EscapeRoomView.getInstance();
        this.escapeRoom = escapeRoom;
    }

    public static EscapeRoomController getInstance(EscapeRoom escapeRoom) throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (EscapeRoomController.class) {
                if (instance == null) {
                    EscapeRoomService service = ServiceManager.getInstance().getEscapeRoomService();
                    instance = new EscapeRoomController(escapeRoom, service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuEscapeRoom.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            try {
                OptionsMenuEscapeRoom idMenu = OptionsMenuEscapeRoom.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CLUE_MANAGEMENT -> ClueController.getInstance().mainMenu();
                    case DECORATION_MANAGEMENT -> DecorationController.getInstance().mainMenu();
                    case REWARD_MANAGEMENT -> RewardController.getInstance().mainMenu();
                    case CERTIFICATE_MANAGEMENT -> CertificateController.getInstance().mainMenu();
                    case SHOW_BUSINESS_DATA -> showBusinessData();
                    case UPDATE_DATA_BUSINESS -> updateDataBusiness();
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

    private void showBusinessData() {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + "  #################");
        ESCAPE_ROOM_VIEW.displayRecordBusiness(escapeRoom);
    }

    private void updateDataBusiness() throws DatabaseConnectionException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        ESCAPE_ROOM_VIEW.displayRecordBusiness(escapeRoom);

        BASE_VIEW.displayMessage2ln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for not changes.");
        ESCAPE_ROOM_VIEW.getUpdateBusinessDetails(escapeRoom);
        updateDataMongodb();
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully: " + escapeRoom.getName() + " (ID: " + escapeRoom.getId() + ")");
    }

    private void updateDataMongodb() throws DatabaseConnectionException {
        if (LoadConfigDB.getMongodbEnable()) {
            MongoDBConnection connection = MongoDBConnection.getInstance();
            connection.setEscapeRoomName(escapeRoom.getId(), escapeRoom.getName());
            connection.setEscapeRoomAddress(escapeRoom.getId(), escapeRoom.getAddress());
        }
    }
}
