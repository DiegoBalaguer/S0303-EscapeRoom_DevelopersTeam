package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.*;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.dto.RewardWinDisplayDTO;
import mvc.enumsMenu.OptionsMenuPlayerAward;
import lombok.extern.slf4j.Slf4j;
import mvc.model.*;
import mvc.view.PlayerAwardsView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerAwardsController {

    private static PlayerAwardsController playerAwardsController;
    private final PlayerAwardsView playerAwardsView;
    private final PlayerDAO playerDAO;
    private final RewardDAO rewardDAO;
    private final CertificateDAO certificateDAO;
    private final RoomDAO roomDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;

    private PlayerAwardsController() {
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

    public static PlayerAwardsController getInstance() {
        if (playerAwardsController == null) {
            synchronized (PlayerAwardsController.class) {
                if (playerAwardsController == null) {
                    playerAwardsController = new PlayerAwardsController();
                }
            }
        }
        log.debug("Created PlayerAwardsWorkers Singleton");
        return playerAwardsController;
    }

    public void mainMenu() {
        do {
            playerAwardsView.displayPlayerMenu("PLAYER AWARDS MANAGEMENT");
            int answer = playerAwardsView.getInputRequiredInt("Choose an option: ");
            OptionsMenuPlayerAward selectedOption = OptionsMenuPlayerAward.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            playerAwardsView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case AWARD_REWARD_WIN -> awardRewardWinToPlayer();
                        case AWARD_CERTIFICATE_WIN -> awardCertificateWinToPlayer();
                        case REVOKE_REWARD_WIN ->  revokeRewardWinToPlayer();
                        case REVOKE_CERTIFICATE_WIN -> revokeCertificateWinToPlayer();
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

    private void awardRewardWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### AWARD REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardId = 0;
        String description = "";

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Rewards");
            rewardId = getRewardId();

            description = getDescription();

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        RewardWin newRewardWin = RewardWin.builder()
                .idPlayer(playerId)
                .idReward(rewardId)
                .dateDelivery(LocalDateTime.now())
                .description(description)
                .isActive(true)
                .build();

        if (newRewardWin != null) {
            RewardWin savedRewardWin = rewardWinDAO.create(newRewardWin);
            playerAwardsView.displayMessageln("Award created successfully with ID: " + savedRewardWin.getId());
        }
    }

    private void awardCertificateWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### AWARD CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int roomId = 0;
        int certificateId = 0;
        String description = "";

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Rooms");
            roomId = getRoomId();

            playerAwardsView.displayMessageln("List of Certificates");
            certificateId = getCertificateId();

            description = getDescription();

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        CertificateWin newCertificateWin = CertificateWin.builder()
                .idPlayer(playerId)
                .idCertificate(certificateId)
                .idRoom(roomId)
                .dateDelivery(LocalDateTime.now())
                .description(description)
                .isActive(true)
                .build();

        if (newCertificateWin != null) {
            CertificateWin savedCertificateWin = certificateWinDAO.create(newCertificateWin);
            playerAwardsView.displayMessageln("Certificate Win added successfully for Player ID " + playerId + " (Certificate Win ID: " + savedCertificateWin.getId() + ")");
        }
    }

    private void revokeRewardWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### REVOKE REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardWinId = 0;

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Rewards Wins");
            rewardWinId = getRewardWinId(playerId);

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<RewardWin> existingRewardWinOpt = rewardWinDAO.findById(rewardWinId);
        if (existingRewardWinOpt.isPresent()) {
            RewardWin existingRewardWin = existingRewardWinOpt.get();
            existingRewardWin.setActive(false);
            rewardWinDAO.update(existingRewardWin);
            playerAwardsView.displayMessageln("Reward Win Player unsubscribed successfully.");
        }
    }

    private void revokeCertificateWinToPlayer() throws DAOException {
        playerAwardsView.displayMessageln("#### REVOKE CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int certificateWinId = 0;

        try {
            playerAwardsView.displayMessageln("List of Players");
            playerId = getPlayerId();

            playerAwardsView.displayMessageln("List of Certificates Wins");
            certificateWinId = getCertificateWinId(playerId);

        } catch (IllegalArgumentException e) {
            playerAwardsView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<CertificateWin> existingCertificateWinOpt = certificateWinDAO.findById(certificateWinId);
        if (existingCertificateWinOpt.isPresent()) {
            CertificateWin existingCertificateWin = existingCertificateWinOpt.get();
            existingCertificateWin.setActive(false);
            certificateWinDAO.update(existingCertificateWin);
            playerAwardsView.displayMessageln("Certificate Win Player unsubscribed successfully.");
        }
    }

    private int getPlayerId() {
        listAllPayersIntern();
        int playerIdOpt = playerAwardsView.getInputRequiredInt("Enter player ID: ");
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
        int rewardIdOpt = playerAwardsView.getInputRequiredInt("Enter Reward ID: ");
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
        int roomIdOpt = playerAwardsView.getInputRequiredInt("Enter room ID: ");
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
        int certificateIdOpt = playerAwardsView.getInputRequiredInt("Enter Certificate ID: ");
        if (certificateDAO.findById(certificateIdOpt).isEmpty()) {
            String message = "Certificate ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return certificateIdOpt;
    }

    private int getRewardWinId(int playerId) {
        listAllRewardsWinsIntern(playerId);
        int rewardWinIdOpt = playerAwardsView.getInputRequiredInt("Enter Reward win ID: ");
        if (rewardWinDAO.findById(rewardWinIdOpt).isEmpty()) {
            String message = "Reward ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return rewardWinIdOpt;
    }

    private int getCertificateWinId(int playerId) {
        listAllCertificatesWinsIntern(playerId);
        int certificateIdOpt = playerAwardsView.getInputRequiredInt("Enter Certificate win ID: ");
        if (certificateWinDAO.findById(certificateIdOpt).isEmpty()) {
            String message = "Certificate ID required or not found.";
            playerAwardsView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return certificateIdOpt;
    }

    private String getDescription() {
        return playerAwardsView.getInputRequiredString("Enter description (30 chars): ");
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

    private void listAllRewardsWinsIntern(int playerId) throws DAOException {
        List<RewardWinDisplayDTO> rewardWinDisplayDTOS = rewardWinDAO.findByPlayerId(playerId);
        playerAwardsView.displayRewardWin(rewardWinDisplayDTOS);
    }

    private void listAllCertificatesWinsIntern(int playerId) throws DAOException {
        List<CertificateWinDisplayDTO> certificateWinDisplayDTOS = certificateWinDAO.findByPlayerId(playerId);
        playerAwardsView.displayCertificateWin(certificateWinDisplayDTOS);
    }
}