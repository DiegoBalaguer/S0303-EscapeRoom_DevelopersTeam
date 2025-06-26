package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import enums.OptionsMenuPlayer;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import utils.ConsoleUtils;
import view.PlayerView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerWorkers {

    private static PlayerWorkers appWorkersInstance;
    private final PlayerView playerView;
    private final PlayerDAO playerDAO;
    private final String LINE = System.lineSeparator();

    private PlayerWorkers() {
        this.playerView = new PlayerView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerWorkers getInstance() {
        if (appWorkersInstance == null) {
            synchronized (PlayerWorkers.class) {
                if (appWorkersInstance == null) {
                    appWorkersInstance = new PlayerWorkers();
                }
            }
        }
        log.debug("Created PlayerWorkers Singleton");
        return appWorkersInstance;
    }

    public void mainMenu() {
        do {
            playerView.displayPlayerMenu("PLAYER MANAGEMENT");
            playerView.displayMessage("Choose an option: ");
            int answer = ConsoleUtils.readRequiredInt("");
            OptionsMenuPlayer selectedOption = OptionsMenuPlayer.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case CREATE -> createPlayer();
                        case LIST_ALL -> listAllPlayers();
                        case READ -> findPlayerById();
                        case UPDATE -> updatePlayer();
                        case SOFT_DELETE -> softDeletePlayer();
                        case DELETE -> deletePlayer();
                        case SUBSCRIBE -> subscribePlayer();
                        case UNSUBSCRIBE -> unSubscribePlayer();
                    }
                } catch (DAOException e) {
                    playerView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    playerView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                playerView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createPlayer() throws DAOException {
        Player newPlayer = new Player();
        newPlayer = playerView.getPlayerDetailsCreate(new Player());
        if (newPlayer != null) {
            Player savedPlayer = playerDAO.create(newPlayer);
            playerView.displayMessageln("Player created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        }
    }

    private void listAllPlayers() throws DAOException {
        playerView.displayMessageln("#### LIST ALL PLAYERS  #################");
        listAllPayersIntern();
    }

    private void listAllPayersIntern()  throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerView.displayPlayers(players);
    }

    private void findPlayerById() throws DAOException {
        playerView.displayMessageln("#### FIND PLAYER BY ID  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> player = playerDAO.findById(idOpt.get());
            playerView.displayPlayer(player.orElse(null));
        }
    }

    private void updatePlayer() throws DAOException {
        playerView.displayMessageln("#### UPDATE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                playerView.displayMessageln("Current Player Details:");
                playerView.displayPlayer(existingPlayer);

                playerView.displayMessageln("Enter new details:");
                Player updatedDetails = playerView.getUpdatePlayerDetails(existingPlayer);

                Player savedPlayer = playerDAO.update(updatedDetails);
                playerView.displayMessageln("Player updated successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot update.");
        }
    }

    private void deletePlayer() throws DAOException {
        playerView.displayMessageln("#### DELETE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            playerDAO.deleteById(idOpt.get());
            playerView.displayMessageln("Player with ID " + idOpt.get() + " deleted successfully (if existed).");
        }
    }

    private void softDeletePlayer() throws DAOException {
        playerView.displayMessageln("#### SOFT DELETE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setActive(false);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player soft deleted successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot soft deleted.");
        }
    }

    private void subscribePlayer() throws DAOException {
        playerView.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setSubscribed(true);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player subscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot subscribed.");
        }
    }

    private void unSubscribePlayer() throws DAOException {
        playerView.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setSubscribed(false);
                playerDAO.update(existingPlayer);
                playerView.displayMessageln("Player unsubscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            playerView.displayMessageln("Player with ID " + idOpt.get() + " not found. Cannot unsubscribed.");
        }
    }
}
