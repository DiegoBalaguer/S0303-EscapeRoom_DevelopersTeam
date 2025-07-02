package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import mvc.enumsMenu.OptionsMenuPlayer;

import mvc.model.Player;

import mvc.view.BaseView;
import mvc.view.PlayerView;

import java.util.*;

public class PlayerController {

    private static PlayerController instancePlayerController;
    private final PlayerDAO PLAYER_DAO;
    private BaseView baseView;
    private PlayerView playerView;

    private PlayerAwardsController playerAwardsController;
    private CertificateWinController certificateWinController;
    private RewardWinController rewardWinController;

    private static final String NAME_OBJECT = "Player";

    private PlayerController() {
        baseView = new BaseView();
        playerView = new PlayerView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
        playerAwardsController = new PlayerAwardsController();
        rewardWinController = new RewardWinController();
        certificateWinController = new CertificateWinController();
        try {
            this.PLAYER_DAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerController getInstance() {
        if (instancePlayerController == null) {
            synchronized (PlayerController.class) {
                if (instancePlayerController == null) {
                    instancePlayerController = new PlayerController();
                }
            }
        }
        return instancePlayerController;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuPlayer.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
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
                        case DELETE -> deletePlayerById();
                        case SOFT_DELETE -> softDeletePlayer();
                        case AWARDS_MANAGEMENT -> playerAwardsController.mainMenu();
                        case NOTIFY_MANAGEMENT -> PlayerNotifyController.getInstance().mainMenu();
                        default -> baseView.displayErrorMessage("Unknown option selected.");
                    }
                } catch (DAOException e) {
                    baseView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    baseView.displayErrorMessage(e.getMessage());
                } catch (Exception e) {
                    baseView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                baseView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }

    private void createPlayer() throws DAOException {
        baseView.displayMessage2ln("####  CREATE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Player newPlayer = playerView.getPlayerDetailsCreate();
            Player savedPlayer = PLAYER_DAO.create(newPlayer);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllPlayers() {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllPayersDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error list all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void findPlayerById() {
        baseView.displayMessage2ln("####  FIND " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
            Optional<Player> existPlayerOpt = PLAYER_DAO.findById(getPlayerIdWithList());

            playerView.displayRecordPlayer(existPlayerOpt.get());

            baseView.displayMessage2ln("List of Rewards Wins");
            rewardWinController.listAllCertificatesWinForPlayerDetail(existPlayerOpt.get().getId());

            baseView.displayMessage2ln("List of Certificate Wins");
            certificateWinController.listAllCertificatesWinForPlayerDetail(existPlayerOpt.get().getId());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error retrieving " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updatePlayer() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Player> existPlayerOpt = PLAYER_DAO.findById(getPlayerIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            playerView.displayRecordPlayer(existPlayerOpt.get());

            baseView.displayMessage2ln("Enter new details:");
            Player updatedPlayer = playerView.getUpdatePlayerDetails(existPlayerOpt.get());

            Player savedPlayer = PLAYER_DAO.update(updatedPlayer);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedPlayer.getName() + " (ID: " + savedPlayer.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deletePlayerById() {
        baseView.displayMessage2ln("####  DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            int searchPlayerId = getPlayerIdWithList();
            PLAYER_DAO.deleteById(searchPlayerId);
            baseView.displayMessage2ln(NAME_OBJECT + " with ID " + searchPlayerId + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void softDeletePlayer() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Player> existPlayerOpt = PLAYER_DAO.findById(getPlayerIdWithList());
            existPlayerOpt.get().setActive(false);

            PLAYER_DAO.update(existPlayerOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existPlayerOpt.get().getName() + " (ID: " + existPlayerOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }


    public int getPlayerIdWithList() {
        listAllPayersDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || PLAYER_DAO.findById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllPayersDetail() throws DAOException {
        List<Player> players = PLAYER_DAO.findAll();
        playerView.displayListPlayers(players);
    }
}