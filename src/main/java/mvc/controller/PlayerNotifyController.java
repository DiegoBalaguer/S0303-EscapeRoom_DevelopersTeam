package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import mvc.enumsMenu.OptionsMenuPlayerNotify;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Player;
import mvc.view.PlayerNotifyView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerNotifyController {

    private static PlayerNotifyController playerNotifyController;
    private final PlayerNotifyView playerNotifyView;
    private final PlayerDAO playerDAO;

    private PlayerNotifyController() {
        this.playerNotifyView = new PlayerNotifyView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerNotifyController getInstance() {
        if (playerNotifyController == null) {
            synchronized (PlayerNotifyController.class) {
                if (playerNotifyController == null) {
                    playerNotifyController = new PlayerNotifyController();
                }
            }
        }
        log.debug("Created PlayerNotifyWorkers Singleton");
        return playerNotifyController;
    }

    public void mainMenu() {
        do {
            playerNotifyView.displayPlayerMenu("PLAYER NOTIFY MANAGEMENT");
            int answer = playerNotifyView.getInputOptionMenu("Choose an option: ");
            OptionsMenuPlayerNotify selectedOption = OptionsMenuPlayerNotify.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerNotifyView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case SUBSCRIBE -> subscribePlayer();
                        case UNSUBSCRIBE -> unSubscribePlayer();
                    }
                } catch (DAOException e) {
                    playerNotifyView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    playerNotifyView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                playerNotifyView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void subscribePlayer() throws DAOException {
        playerNotifyView.displayMessageln("#### SUBSCRIBE PLAYER  #################");

        Optional<Player> existingPlayerOpt = playerDAO.findById(getPlayerId());
        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            existingPlayer.setSubscribed(true);
            playerDAO.update(existingPlayer);
            playerNotifyView.displayMessageln("Player subscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
        }
    }

    private void unSubscribePlayer() throws DAOException {
        playerNotifyView.displayMessageln("#### SUBSCRIBE PLAYER  #################");

        Optional<Player> existingPlayerOpt = playerDAO.findById(getPlayerId());
        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            existingPlayer.setSubscribed(false);
            playerDAO.update(existingPlayer);
            playerNotifyView.displayMessageln("Player unsubscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
        }
    }

    private void listAllPayersIntern() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerNotifyView.displayPlayers(players);
    }

    private int getPlayerId() throws DAOException {
        listAllPayersIntern();
        return playerNotifyView.getPlayerId();
    }
}