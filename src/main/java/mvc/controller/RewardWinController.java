package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.RewardWinDAO;
import mvc.dto.RewardWinDisplayDTO;
import mvc.model.RewardWin;
import mvc.view.BaseView;
import mvc.view.RewardWinView;

import java.util.List;
import java.util.Optional;

public class RewardWinController {

    private final RewardWinDAO REWARDWIN_DAO;
    private BaseView baseView;
    private RewardWinView rewardWinView;
    private static final String NAME_OBJECT = "Reward Win";

    public RewardWinController() {

        try {
            REWARDWIN_DAO = DAOFactory.getDAOFactory().getRewardWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        baseView = BaseView.getInstance();
        rewardWinView = new RewardWinView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    public int getRewardWinForPlayerWithList(int playerId) {
        boolean status = listAllCertificatesWinForPlayerDetail(playerId);
        if (!status) {
            String message = "No " + NAME_OBJECT + "s found.";
            throw new IllegalArgumentException(message);
        }
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || getRewardFindForId(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    public boolean listAllCertificatesWinForPlayerDetail(int playerId) throws DAOException {
        List<RewardWinDisplayDTO> rewardWinDisplayDTOS = getRewardWinForPlayer(playerId);
        if (rewardWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return false;
        }
        rewardWinView.displayListRewardWinDTO(rewardWinDisplayDTOS);
        return true;
    }

    public List<RewardWinDisplayDTO> getRewardWinForPlayer(int playerId) {
        return REWARDWIN_DAO.findByPlayerId(playerId);
    }

    public Optional<RewardWin> getRewardFindForId(int id) {
        return REWARDWIN_DAO.findById(id);
    }
}