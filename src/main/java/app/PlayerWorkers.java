package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;
import dto.CertificateWinDisplayDTO;
import enums.OptionsMenuPlayer;
import lombok.extern.slf4j.Slf4j;

import model.Player;
import dto.RewardWinDisplayDTO;

import view.PlayerView;

import java.util.*;

@Slf4j
public class PlayerWorkers {

    private static PlayerWorkers playerWorkersInstance;
    private final PlayerView playerView;
    private final PlayerDAO playerDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;
    private final String LINE = System.lineSeparator();

    private PlayerWorkers() {
        this.playerView = new PlayerView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
            this.rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
            this.certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerWorkers getInstance() {
        if (playerWorkersInstance == null) {
            synchronized (PlayerWorkers.class) {
                if (playerWorkersInstance == null) {
                    playerWorkersInstance = new PlayerWorkers();
                }
            }
        }
        log.debug("Created PlayerWorkers Singleton");
        return playerWorkersInstance;
    }

    public void mainMenu() {
        do {
            playerView.displayPlayerMenu("PLAYER MANAGEMENT");
            int answer = playerView.getInputOptionMenu("Choose an option: ");
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
                        case DELETE -> deletePlayer();
                        case SOFT_DELETE -> softDeletePlayer();
                        case AWARDS_MANAGEMENT -> PlayerAwardsWorkers.getInstance().mainMenu();
                        case NOTIFY_MANAGEMENT -> PlayerNotifyWorkers.getInstance().mainMenu();
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
       Player newPlayer = playerView.getPlayerDetailsCreate();
        if (newPlayer != null) {
            Player savedPlayer = playerDAO.create(newPlayer);
            playerView.displayMessageln("Player created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        }
    }

    private void listAllPlayers() throws DAOException {
        playerView.displayMessageln("#### LIST ALL PLAYERS  #################");
        listAllPayersIntern();
    }

    private void findPlayerById() throws DAOException {
        playerView.displayMessageln("#### FIND PLAYER BY ID  #################");
        listAllPayersIntern();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Integer playerId = idOpt.get();
            Optional<Player> player = playerDAO.findById(playerId);
            playerView.displayPlayer(player.orElse(null));

            List<RewardWinDisplayDTO> rewardWinDisplayDTOs = rewardWinDAO.findByPlayerId(playerId);
            playerView.displayRewardWinDTOs(rewardWinDisplayDTOs);

            List<CertificateWinDisplayDTO> certificateWinDisplayDTOs = certificateWinDAO.findByPlayerId(playerId);
            playerView.displayCertificateWinDTOs(certificateWinDisplayDTOs);
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

    private void listAllPayersIntern() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerView.displayPlayers(players);
    }
}