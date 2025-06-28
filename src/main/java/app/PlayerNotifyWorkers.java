package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import enums.OptionsMenuPlayerNotify;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import view.PlayerNotifyView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerNotifyWorkers {

    private static PlayerNotifyWorkers playerNotifyWorkers;
    private final PlayerNotifyView playerNotifyView;
    private final PlayerDAO playerDAO;

    private PlayerNotifyWorkers() {
        this.playerNotifyView = new PlayerNotifyView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerNotifyWorkers getInstance() {
        if (playerNotifyWorkers == null) {
            synchronized (PlayerNotifyWorkers.class) {
                if (playerNotifyWorkers == null) {
                    playerNotifyWorkers = new PlayerNotifyWorkers();
                }
            }
        }
        log.debug("Created PlayerNotifyWorkers Singleton");
        return playerNotifyWorkers;
    }

    public void mainMenu() {
        do {
            playerNotifyView.displayPlayerMenu("PLAYER NOTIFY MANAGEMENT");
            playerNotifyView.displayMessage("Choose an option: ");
            int answer = playerNotifyView.getInputOptionMenu();
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
        listAllPayersIntern();
        int idOpt = playerNotifyView.getPlayerId();

        Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt);
        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            existingPlayer.setSubscribed(true);
            playerDAO.update(existingPlayer);
            playerNotifyView.displayMessageln("Player subscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
        }
    }

    private void unSubscribePlayer() throws DAOException {
        playerNotifyView.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        listAllPayersIntern();
        int idOpt = playerNotifyView.getPlayerId();

        Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt);
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
}