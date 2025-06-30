package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.RewardDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Reward;
import mvc.view.BaseView;
import mvc.view.RewardView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RewardController {

    private final RewardDAO REWARD_DAO;
    private BaseView baseView;
    private RewardView rewardView;
    private static final String NAME_OBJECT = "Reward";

    public RewardController() {

        try {
            REWARD_DAO = DAOFactory.getDAOFactory().getRewardDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        
        baseView = new BaseView();
        rewardView = new RewardView();

         log.debug("Created Class: {}", this.getClass().getName());
    }

    // Queries for other Classes

    public int getRewardIdWithList() {
        listAllRewardsDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || REWARD_DAO.findById(searchID.get()).isEmpty()) {
            String message =  NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllRewardsDetail() throws DAOException {
        List<Reward> rewards = REWARD_DAO.findAll();
        rewardView.displayListRewards(rewards);
    }
}