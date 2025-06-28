package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;
import enums.OptionsMenuPlayerAward;
import lombok.extern.slf4j.Slf4j;
import model.*;
import view.PlayerAwardsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class PlayerAwardsWorkers {

    private static PlayerAwardsWorkers playerAwardsWorkers;
    private final PlayerAwardsView playerAwardsView;
    private final PlayerDAO playerDAO;
    private final RewardDAO rewardDAO;
    private final CertificateDAO certificateDAO;
    private final RoomDAO roomDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;

    private PlayerAwardsWorkers() {
        this.playerAwardsView = new PlayerAwardsView();
        try {
            this.roomDAO = DAOFactory.getDAOFactory().getRoomDAO();
            this.playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
            this.rewardDAO = DAOFactory.getDAOFactory().getRewardDAO();
            this.certificateDAO = DAOFactory.getDAOFactory().getCertificateDAO();
            this.rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
            this.certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerAwardsWorkers getInstance() {
        if (playerAwardsWorkers == null) {
            synchronized (PlayerAwardsWorkers.class) {
                if (playerAwardsWorkers == null) {
                    playerAwardsWorkers = new PlayerAwardsWorkers();
                }
            }
        }
        log.debug("Created PlayerAwardsWorkers Singleton");
        return playerAwardsWorkers;
    }

    public void mainMenu() {
        do {
            playerAwardsView.displayPlayerMenu("PLAYER AWARDS MANAGEMENT");
            playerAwardsView.displayMessage("Choose an option: ");
            int answer = playerAwardsView.getInputOptionMenu();
            OptionsMenuPlayerAward selectedOption = OptionsMenuPlayerAward.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerAwardsView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case AWARD_REWARD_WIN -> addRewardWinToPlayer();
                        case AWARD_CERTIFICATE_WIN -> addCertificateWinToPlayer();
                    }
                } catch (DAOException e) {
                    playerAwardsView.displayErrorMessage("Database operation failed: " + e.getMessage());
                } catch (Exception e) {
                    playerAwardsView.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
                }
            } else {
                playerAwardsView.displayErrorMessage("Invalid option. Please choose a valid number from the menu.");
            }
        } while (true);
    }


    private void addRewardWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### ADD REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardId = 0;

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Rewards");
            rewardId = getRewardId();

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        RewardWin newRewardWin = RewardWin.builder()
                .idPlayer(playerId)
                .idReward(rewardId)
                .dateDelivery(LocalDateTime.from(LocalDate.now()))
                .isActive(true)
                .build();

        if (newRewardWin != null) {
            RewardWin savedRewardWin = rewardWinDAO.create(newRewardWin);
            playerAwardsView.displayMessageln("Reward created successfully with ID: " + savedRewardWin.getId());
        }
    }

    private void addCertificateWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### ADD CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int roomId = 0;
        int certificateId = 0;

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Rooms");
            roomId = getRoomId();

            playerAwardsView.displayMessageln("List of Certificates");
            certificateId = getCertificateId();

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        CertificateWin newCertificateWin = CertificateWin.builder()
                .idPlayer(playerId)
                .idCertificate(certificateId)
                .idRoom(roomId)
                .dateDelivery(LocalDateTime.from(LocalDate.now()))
                .isActive(true)
                .build();

        if (newCertificateWin != null) {
            CertificateWin savedCertificateWin = certificateWinDAO.create(newCertificateWin);
            playerAwardsView.displayMessageln("Certificate Win added successfully for Player ID " + playerId + " (Certificate Win ID: " + savedCertificateWin.getId() + ")");
        }
    }

    private int getPlayerId() {
        listAllPayersIntern();
        int playerIdOpt = playerAwardsView.getPlayerId();
        if (playerDAO.findById(playerIdOpt).isEmpty()) {
            String message = "Player with ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return playerIdOpt;
    }

    private int getRewardId() {
        listAllRewardsIntern();
        int rewardIdOpt = playerAwardsView.getRewardId();
        if (rewardDAO.findById(rewardIdOpt).isEmpty()) {
            String message = "Reward ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return rewardIdOpt;
    }

    private int getRoomId() {
        listAllRoomsIntern();
        int roomIdOpt = playerAwardsView.getRoomId();
        if (roomDAO.findById(roomIdOpt).isEmpty()) {
            String message = "Room ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return roomIdOpt;
    }

    private int getCertificateId() {
        listAllCertificatesIntern();
        int certificateIdOpt = playerAwardsView.getCertificateId();
        if (rewardDAO.findById(certificateIdOpt).isEmpty()) {
            String message = "Certificate ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return certificateIdOpt;
    }

    private void listAllPayersIntern() throws DAOException {
        List<Player> players = playerDAO.findAll();
        playerAwardsView.displayPlayers(players);
    }

    private void listAllRewardsIntern() throws DAOException {
        List<Reward> rewards = rewardDAO.findAll();
        playerAwardsView.displayRewards(rewards);
    }

    private void listAllCertificatesIntern() throws DAOException {
        List<Certificate> certificates = certificateDAO.findAll();
        playerAwardsView.displayCertificates(certificates);
    }

    private void listAllRoomsIntern() throws DAOException {
        List<Room> rooms = roomDAO.findAll();
        playerAwardsView.displayRooms(rooms);
    }
}