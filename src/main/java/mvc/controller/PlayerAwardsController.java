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
import mvc.view.BaseView;
import mvc.view.PlayerAwardsView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerAwardsController {

    private static PlayerAwardsController playerAwardsController;
    private final BaseView baseView;
    private final PlayerAwardsView playerAwardsView;
    private final PlayerDAO playerDAO;
    private final RewardDAO rewardDAO;
    private final CertificateDAO certificateDAO;
    private final RoomDAO roomDAO;
    private final RewardWinDAO rewardWinDAO;
    private final CertificateWinDAO certificateWinDAO;

    private PlayerAwardsController() {
        this.baseView = new BaseView();
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
            int answer = baseView.getInputRequiredInt("Choose an option: ");
            OptionsMenuPlayerAward selectedOption = OptionsMenuPlayerAward.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessage2ln("Returning to Main Menu...");
                            return;
                        }
                        case AWARD_REWARD_WIN -> awardRewardWinToPlayer();
                        case AWARD_CERTIFICATE_WIN -> awardCertificateWinToPlayer();
                        case REVOKE_REWARD_WIN ->  revokeRewardWinToPlayer();
                        case REVOKE_CERTIFICATE_WIN -> revokeCertificateWinToPlayer();
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

    private void awardRewardWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### AWARD REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardId = 0;
        String description = "";

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = getPlayerId();

            baseView.displayMessage2ln("List of Rewards");
            rewardId = getRewardId();

            description = getDescription();

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
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
            baseView.displayMessage2ln("Award created successfully with ID: " + savedRewardWin.getId());
        }
    }

    private void awardCertificateWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### AWARD CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int roomId = 0;
        int certificateId = 0;
        String description = "";

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = getPlayerId();

            baseView.displayMessage2ln("List of Rooms");
            roomId = RoomController.getInstance().getRoomId();

            baseView.displayMessage2ln("List of Certificates");
            certificateId = getCertificateId();

            description = getDescription();

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
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
            baseView.displayMessage2ln("Certificate Win added successfully for Player ID " + playerId + " (Certificate Win ID: " + savedCertificateWin.getId() + ")");
        }



    }

    private void revokeRewardWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### REVOKE REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardWinId = 0;

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = getPlayerId();

            baseView.displayMessage2ln("List of Rewards Wins");
            rewardWinId = getRewardWinId(playerId);

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<RewardWin> existingRewardWinOpt = rewardWinDAO.findById(rewardWinId);
        if (existingRewardWinOpt.isPresent()) {
            RewardWin existingRewardWin = existingRewardWinOpt.get();
            existingRewardWin.setActive(false);
            rewardWinDAO.update(existingRewardWin);
            baseView.displayMessage2ln("Reward Win Player unsubscribed successfully.");
        }
    }

    private void revokeCertificateWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### REVOKE CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int certificateWinId = 0;

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = getPlayerId();

            baseView.displayMessage2ln("List of Certificates Wins");
            certificateWinId = getCertificateWinId(playerId);

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<CertificateWin> existingCertificateWinOpt = certificateWinDAO.findById(certificateWinId);
        if (existingCertificateWinOpt.isPresent()) {
            CertificateWin existingCertificateWin = existingCertificateWinOpt.get();
            existingCertificateWin.setActive(false);
            certificateWinDAO.update(existingCertificateWin);
            baseView.displayMessage2ln("Certificate Win Player unsubscribed successfully.");
        }
    }

    private int getPlayerId() {
        listAllPayersIntern();
        int playerIdOpt = baseView.getInputRequiredInt("Enter player ID: ");
        if (playerDAO.findById(playerIdOpt).isEmpty()) {
            String message = "Player with ID required or not found.";
            baseView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return playerIdOpt;
    }

    private int getRewardId() {
        listAllRewardsIntern();
        int rewardIdOpt = baseView.getInputRequiredInt("Enter Reward ID: ");
        if (rewardDAO.findById(rewardIdOpt).isEmpty()) {
            String message = "Reward ID required or not found.";
            baseView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return rewardIdOpt;
    }



    private int getCertificateId() {
        listAllCertificatesIntern();
        int certificateIdOpt = baseView.getInputRequiredInt("Enter Certificate ID: ");
        if (certificateDAO.findById(certificateIdOpt).isEmpty()) {
            String message = "Certificate ID required or not found.";
            baseView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return certificateIdOpt;
    }

    private int getRewardWinId(int playerId) {
        listAllRewardsWinsIntern(playerId);
        int rewardWinIdOpt = baseView.getInputRequiredInt("Enter Reward win ID: ");
        if (rewardWinDAO.findById(rewardWinIdOpt).isEmpty()) {
            String message = "Reward ID required or not found.";
            baseView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return rewardWinIdOpt;
    }

    private int getCertificateWinId(int playerId) {
        listAllCertificatesWinsIntern(playerId);
        int certificateIdOpt = baseView.getInputRequiredInt("Enter Certificate win ID: ");
        if (certificateWinDAO.findById(certificateIdOpt).isEmpty()) {
            String message = "Certificate ID required or not found.";
            baseView.displayErrorMessage(message);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return certificateIdOpt;
    }

    private String getDescription() {
        return baseView.getInputRequiredString("Enter description (30 chars): ");
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

    private void listAllRewardsWinsIntern(int playerId) throws DAOException {
        List<RewardWinDisplayDTO> rewardWinDisplayDTOS = rewardWinDAO.findByPlayerId(playerId);
        playerAwardsView.displayRewardWin(rewardWinDisplayDTOS);
    }

    private void listAllCertificatesWinsIntern(int playerId) throws DAOException {
        List<CertificateWinDisplayDTO> certificateWinDisplayDTOS = certificateWinDAO.findByPlayerId(playerId);
        playerAwardsView.displayCertificateWin(certificateWinDisplayDTOS);
    }
}