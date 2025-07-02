package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.RewardDAO;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.model.Reward;
import mvc.view.BaseView;
import mvc.view.RewardView;

import java.util.List;
import java.util.Optional;

public class RewardController {

    private static RewardController rewardControllerInstance;
    private final RewardDAO REWARD_DAO;
    private BaseView baseView;
    private RewardView rewardView;
    private static final String NAME_OBJECT = "Reward";

    public RewardController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        try {
            REWARD_DAO = DAOFactory.getDAOFactory().getRewardDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        rewardView = new RewardView();
    }

    public static RewardController getInstance() {
        if (rewardControllerInstance == null) {
            synchronized (RewardController.class) {
                if (rewardControllerInstance == null) {
                    rewardControllerInstance = new RewardController();
                }
            }
        }
        return rewardControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createReward();
                    case LIST_ALL -> listAllRewards();
                    case FIND_BY_ID -> getRewardById();
                    case UPDATE -> updateReward();
                    case SOFT_DELETE -> softDeleteRewardById();
                    case DELETE -> deleteRewardById();

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


    private void createReward() {
        baseView.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
        try {
            Reward newReward = rewardView.getRewardDetailsCreate();
            Reward savedReward = REWARD_DAO.create(newReward);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedReward.getName() + " (ID: " + savedReward.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void getRewardById() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
             Optional<Reward> optionalReward = REWARD_DAO.findById(getRewardIdWithList());

            rewardView.displayRecordReward(optionalReward.get());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error show " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllRewards() throws DAOException {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllRewardDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error list all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateReward() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Reward> existRewardOpt = REWARD_DAO.findById(getRewardIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            rewardView.displayRecordReward(existRewardOpt.get());

            baseView.displayMessage2ln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for not changes.");
            Reward updatedReward = rewardView.getUpdateRewardDetails(existRewardOpt.get());

            Reward savedReward = REWARD_DAO.update(updatedReward);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedReward.getName() + " (ID: " + savedReward.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deleteRewardById() {
        baseView.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            REWARD_DAO.deleteById(getRewardIdWithList());
            baseView.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void softDeleteRewardById() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Reward> existRewardOpt = REWARD_DAO.findById(getRewardIdWithList());
            existRewardOpt.get().setActive(false);

            REWARD_DAO.update(existRewardOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existRewardOpt.get().getName() + " (ID: " + existRewardOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    // Queries for other Classes

    public int getRewardIdWithList() {
        listAllRewardDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || REWARD_DAO.findById(searchID.get()).isEmpty()) {
            String message =  NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllRewardDetail() throws DAOException {
        List<Reward> rewards = REWARD_DAO.findAll();
        rewardView.displayListRewards(rewards);
    }
}