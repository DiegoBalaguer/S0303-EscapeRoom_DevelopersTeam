package mvc.entities.rewardWin;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.view.BaseView;

import java.util.Optional;

public class RewardWinController {

    private static RewardWinController instance;
    private final RewardWinService REWARD_WIN_SERVICE;
    private final RewardWinView REWARD_WIN_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Reward Win";


    private RewardWinController(RewardWinService rewardWinService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        this.REWARD_WIN_SERVICE = rewardWinService;
        REWARD_WIN_VIEW = RewardWinView.getInstance();
    }

    public static RewardWinController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (RewardWinController.class) {
                if (instance == null) {
                    RewardWinService service = ServiceManager.getInstance().getRewardWinService();
                    instance = new RewardWinController(service);
                }
            }
        }
        return instance;
    }

    public void createRewardWin() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### AWARD  " + NAME_OBJECT.toUpperCase() + " TO PLAYER  #################");

        Inputs valuesInput = REWARD_WIN_VIEW.getRewardWinDetailsCreate(
                REWARD_WIN_SERVICE.getAllPlayers(),
                REWARD_WIN_SERVICE.getAllRewards());
        REWARD_WIN_SERVICE.createRewardWin(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    public void softDeleteRewardWinById() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### REVOKE  " + NAME_OBJECT.toUpperCase() + " TO PLAYER  #################");

        Optional<Integer> playerIdOpt = getPlayerId();

        Optional<Integer> rewardWinId = getRewardWinForPlayerWithList(playerIdOpt.get());

        RewardWin softDeletedRewardWin = REWARD_WIN_SERVICE.softDeleteRewardWin(rewardWinId.get());

        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " Player unsubscribed successfully: " + softDeletedRewardWin.getId());
    }

    private Optional<Integer> getPlayerId() throws NotFoundException {
        Optional<Integer> playerIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", REWARD_WIN_SERVICE.getAllPlayers());
        if (playerIdOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return playerIdOpt;
    }

    public Optional<Integer> getRewardWinForPlayerWithList(int playerId) throws DAOException, NotFoundException, IllegalArgumentException {
        Optional<Integer> rewardWinIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Rewards Wins for Player", "Input Reward Win ID: ", REWARD_WIN_SERVICE.getAllRewardsWinForPlayer(playerId));

        if (rewardWinIdOpt.isEmpty()) {
            String message = "No " + NAME_OBJECT + "s found for player ID: " + playerId;
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return rewardWinIdOpt;
    }
}
