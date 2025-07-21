package mvc.entities.decoration;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;

import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.enumsMenu.OptionsMenuCLFUSDE;

import mvc.view.BaseView;

import java.util.Optional;

public class DecorationController {

    private static DecorationController instance;
    private final DecorationService DECORATION_SERVICE;
    private final DecorationView DECORATION_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Decoration";

    private DecorationController(DecorationService decorationService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());

        DECORATION_VIEW = DecorationView.getInstance();
        this.DECORATION_SERVICE = decorationService;
    }

    public static DecorationController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (DecorationController.class) {
                if (instance == null) {
                    DecorationService service = ServiceManager.getInstance().getDecorationService();
                    instance = new DecorationController(service);
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
                    case CREATE -> createDecoration();
                    case LIST_ALL -> listAllDecorations();
                    case FIND_BY_ID -> getDecorationById();
                    case UPDATE -> updateDecoration();
                    case SOFT_DELETE -> softDeleteDecorationById();
                    case DELETE -> deleteDecorationById();

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

    private void createDecoration() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");

        Inputs valuesInput = DECORATION_VIEW.getDecorationDetailsCreate(DECORATION_SERVICE.getAllRooms());

        DECORATION_SERVICE.createDecoration(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    private void getDecorationById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> searchIdOpt = getDecorationIdWithList();
        Optional<Decoration> optionalDecoration = DECORATION_SERVICE.getDecorationById(searchIdOpt.get());
        DECORATION_VIEW.displayRecordDecoration(optionalDecoration.get());
    }

    private void listAllDecorations() {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(DECORATION_SERVICE.getAllDecorationsCompleteInfo().getMessage());
    }

    private void updateDecoration() throws NotFoundException, DatabaseConnectionException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getDecorationIdWithList();
        Optional<Decoration> existDecorationOpt = DECORATION_SERVICE.getDecorationById(searchIdOpt.get());
        if (existDecorationOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        DECORATION_VIEW.displayRecordDecoration(existDecorationOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for not changes.");
        Decoration updatedDecoration = DECORATION_VIEW.getUpdateDecorationDetails(existDecorationOpt.get(), DECORATION_SERVICE.getAllRooms());

        DECORATION_SERVICE.updateDecoration(updatedDecoration);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteDecorationById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getDecorationIdWithList();
        DECORATION_SERVICE.deleteDecoration(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteDecorationById() throws IllegalArgumentException, NotFoundException, DAOException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getDecorationIdWithList();
        DECORATION_SERVICE.softDeleteDecoration(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getDecorationIdWithList() throws DAOException, NotFoundException, IllegalArgumentException {
        Optional<Integer> searchIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Decorations", "Input Decoration ID: ", DECORATION_SERVICE.getAllDecorationsCompleteInfo());
        if (searchIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return searchIdOpt;
    }
}