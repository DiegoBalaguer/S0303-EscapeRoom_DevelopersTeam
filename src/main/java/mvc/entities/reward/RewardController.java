package mvc.entities.reward;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.view.BaseView;

import java.util.Optional;

public class RewardController {

    private static RewardController instance;
    private final RewardService REWARD_SERVICE;
    private final RewardView REWARD_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Reward";

    private RewardController(RewardService rewardService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        REWARD_VIEW = RewardView.getInstance();
        this.REWARD_SERVICE = rewardService;
    }

    public static RewardController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (RewardController.class) {
                if (instance == null) {
                    RewardService service = ServiceManager.getInstance().getRewardService();
                    instance = new RewardController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() {
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
                    case CREATE -> createReward();
                    case LIST_ALL -> listAllRewards();
                    case FIND_BY_ID -> getRewardById();
                    case UPDATE -> updateReward();
                    case SOFT_DELETE -> softDeleteRewardById();
                    case DELETE -> deleteRewardById();

                    default -> BASE_VIEW.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
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

    private void createReward() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");

        Inputs valuesInput = REWARD_VIEW.getRewardDetailsCreate();
        REWARD_SERVICE.createReward(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    private void getRewardById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> searchIdOpt = getRewardIdWithList();
        Optional<Reward> optionalReward = REWARD_SERVICE.getRewardById(searchIdOpt.get());
        REWARD_VIEW.displayRecordReward(optionalReward.get());
    }

    private void listAllRewards() throws DAOException {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(REWARD_SERVICE.getAllRewards().getMessage());
    }

    private void updateReward() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getRewardIdWithList();
        Optional<Reward> existRewardOpt = REWARD_SERVICE.getRewardById(searchIdOpt.get());
        if (existRewardOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        REWARD_VIEW.displayRecordReward(existRewardOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for no changes.");
        Reward updatedReward = REWARD_VIEW.getUpdateRewardDetails(existRewardOpt.get());

        REWARD_SERVICE.updateReward(updatedReward);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteRewardById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getRewardIdWithList();
        REWARD_SERVICE.deleteReward(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteRewardById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getRewardIdWithList();
        REWARD_SERVICE.softDeleteReward(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getRewardIdWithList() throws NotFoundException {
        Optional<Integer> searchIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Rewards Win", "Input Reward Win ID: ", REWARD_SERVICE.getAllRewards());
        if (searchIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return  searchIdOpt;
    }
}