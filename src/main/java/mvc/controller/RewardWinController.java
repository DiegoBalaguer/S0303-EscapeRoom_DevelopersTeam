package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.RewardWinDAO;
import mvc.dto.RewardWinDisplayDTO;
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
        baseView = new BaseView();
        rewardWinView = new RewardWinView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }


    public int getRewardWinForPlayerWithList(int playerId) {
        listAllCertificatesWinForPlayerDetail(playerId);
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || REWARDWIN_DAO.findByPlayerId(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    public void listAllCertificatesWinForPlayerDetail(int playerId) throws DAOException {
        List<RewardWinDisplayDTO> rewardWinDisplayDTOS = REWARDWIN_DAO.findByPlayerId(playerId);
        rewardWinView.displayListRewardWinDTO(rewardWinDisplayDTOS);
    }


}