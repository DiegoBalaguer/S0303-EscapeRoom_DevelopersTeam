package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.enumsMenu.OptionsMenuPlayer;
import lombok.extern.slf4j.Slf4j;

import mvc.model.Player;
import mvc.dto.RewardWinDisplayDTO;

import mvc.view.BaseView;
import mvc.view.PlayerView;

import java.util.*;

@Slf4j
public class PlayerController {

    private static PlayerController playerControllerInstance;
    private final BaseView baseView;
    private final PlayerView playerView;
    private final PlayerDAO playerDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;

    private PlayerController() {
        this.baseView = new BaseView();
        this.playerView = new PlayerView();
        try {
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
            this.rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
            this.certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerController getInstance() {
        if (playerControllerInstance == null) {
            synchronized (PlayerController.class) {
                if (playerControllerInstance == null) {
                    playerControllerInstance = new PlayerController();
                }
            }
        }
        log.debug("Created PlayerWorkers Singleton");
        return playerControllerInstance;
    }

    public void mainMenu() {
        do {
            playerView.displayPlayerMenu("PLAYER MANAGEMENT");
            int answer = baseView.getInputRequiredInt("Choose an option: ");
            OptionsMenuPlayer selectedOption = OptionsMenuPlayer.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessage2ln("Returning to Main Menu...");
                            return;
                        }
                        case CREATE -> createPlayer();
                        case LIST_ALL -> listAllPlayers();
                        case READ -> findPlayerById();
                        case UPDATE -> updatePlayer();
                        case DELETE -> deletePlayer();
                        case SOFT_DELETE -> softDeletePlayer();
                        case AWARDS_MANAGEMENT -> PlayerAwardsController.getInstance().mainMenu();
                        case NOTIFY_MANAGEMENT -> PlayerNotifyController.getInstance().mainMenu();
                    }
                } catch (DAOException e) {
                    baseView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                baseView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createPlayer() throws DAOException {
       Player newPlayer = playerView.getPlayerDetailsCreate();
        if (newPlayer != null) {
            Player savedPlayer = playerDAO.create(newPlayer);
            baseView.displayMessage2ln("Player created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        }
    }

    private void listAllPlayers() throws DAOException {
        baseView.displayMessage2ln("#### LIST ALL PLAYERS  #################");
        listAllPayersDetail();
    }

    private void findPlayerById() throws DAOException {
        baseView.displayMessage2ln("#### FIND PLAYER BY ID  #################");
        listAllPayersDetail();
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
        baseView.displayMessage2ln("#### UPDATE PLAYER  #################");
        listAllPayersDetail();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                baseView.displayMessage2ln("Current Player Details:");
                playerView.displayPlayer(existingPlayer);

                baseView.displayMessage2ln("Enter new details:");
                Player updatedDetails = playerView.getUpdatePlayerDetails(existingPlayer);

                Player savedPlayer = playerDAO.update(updatedDetails);
                baseView.displayMessage2ln("Player updated successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
            }
        } else {
            baseView.displayMessage2ln("Player with ID " + idOpt.get() + " not found. Cannot update.");
        }
    }

    private void deletePlayer() throws DAOException {
        baseView.displayMessage2ln("#### DELETE PLAYER  #################");
        listAllPayersDetail();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            playerDAO.deleteById(idOpt.get());
            baseView.displayMessage2ln("Player with ID " + idOpt.get() + " deleted successfully (if existed).");
        }
    }

    private void softDeletePlayer() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE PLAYER  #################");
        listAllPayersDetail();
        Optional<Integer> idOpt = playerView.getPlayerId();
        if (idOpt.isPresent()) {
            Optional<Player> existingPlayerOpt = playerDAO.findById(idOpt.get());
            if (existingPlayerOpt.isPresent()) {
                Player existingPlayer = existingPlayerOpt.get();
                existingPlayer.setActive(false);
                playerDAO.update(existingPlayer);
                baseView.displayMessage2ln("Player soft deleted successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
            }
        } else {
            baseView.displayMessage2ln("Player with ID " + idOpt.get() + " not found. Cannot soft deleted.");
        }
    }

    private void listAllPayersDetail() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerView.displayPlayers(players);
    }
}