package mvc.entities.clue;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;

import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.enumsMenu.OptionsMenuCLFUSDE;

import mvc.view.BaseView;

import java.util.Optional;

public class ClueController {

    private static ClueController instance;
    private final ClueService CLUE_SERVICE;
    private final ClueView CLUE_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Clue";

    private ClueController(ClueService clueService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.CLUE_SERVICE = clueService;
        CLUE_VIEW = ClueView.getInstance();
    }

    public static ClueController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (ClueController.class) {
                if (instance == null) {
                    ClueService service = ServiceManager.getInstance().getClueService();
                    instance = new ClueController(service);
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
                    case CREATE -> createClue();
                    case LIST_ALL -> listAllClues();
                    case FIND_BY_ID -> getClueById();
                    case UPDATE -> updateClue();
                    case SOFT_DELETE -> softDeleteClueById();
                    case DELETE -> deleteClueById();

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

    private void createClue() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");

        Inputs valuesInput = CLUE_VIEW.getClueDetailsCreate(CLUE_SERVICE.getAllRooms());

        CLUE_SERVICE.createClue(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    private void getClueById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> searchIdOpt = getClueIdWithList();
        Optional<Clue> optionalClue = CLUE_SERVICE.getClueById(searchIdOpt.get());
        CLUE_VIEW.displayRecordClue(optionalClue.get());
    }

    private void listAllClues() {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(CLUE_SERVICE.getAllCluesCompleteInfo().getMessage());
    }

    private void updateClue() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getClueIdWithList();
        Optional<Clue> existClueOpt = CLUE_SERVICE.getClueById(searchIdOpt.get());
        if (existClueOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        CLUE_VIEW.displayRecordClue(existClueOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for not changes.");
        Clue updatedClue = CLUE_VIEW.getUpdateClueDetails(existClueOpt.get(), CLUE_SERVICE.getAllRooms());

        CLUE_SERVICE.updateClue(updatedClue);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteClueById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getClueIdWithList();
        CLUE_SERVICE.deleteClue(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteClueById() throws IllegalArgumentException, NotFoundException, DAOException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getClueIdWithList();
        CLUE_SERVICE.softDeleteClue(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getClueIdWithList() throws DAOException, NotFoundException, IllegalArgumentException {
        Optional<Integer> searchIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Clues", "Input Clue ID: ", CLUE_SERVICE.getAllCluesCompleteInfo());
        if (searchIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return searchIdOpt;
    }
}