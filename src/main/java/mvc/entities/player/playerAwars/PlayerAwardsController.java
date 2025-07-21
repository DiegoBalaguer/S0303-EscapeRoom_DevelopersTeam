package mvc.entities.player.playerAwars;

import mvc.entities.certificateWin.CertificateWinController;
import mvc.entities.rewardWin.RewardWinController;

import dao.exceptions.NotFoundException;
import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;

import mvc.view.BaseView;

public class PlayerAwardsController {

    private static PlayerAwardsController instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Player Awards";

    private PlayerAwardsController() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    public static PlayerAwardsController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (PlayerAwardsController.class) {
                if (instance == null) {
                    instance = new PlayerAwardsController();
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuPlayerAward.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuPlayerAward selectedOption = OptionsMenuPlayerAward.getOptionByNumber(answer);
            try {
                switch (selectedOption) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case AWARD_REWARD_WIN -> RewardWinController.getInstance().createRewardWin();
                    case AWARD_CERTIFICATE_WIN -> CertificateWinController.getInstance().createCertificateWin();
                    case REVOKE_REWARD_WIN -> RewardWinController.getInstance().softDeleteRewardWinById();
                    case REVOKE_CERTIFICATE_WIN -> CertificateWinController.getInstance().softDeleteCertificateWinById();
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
}
