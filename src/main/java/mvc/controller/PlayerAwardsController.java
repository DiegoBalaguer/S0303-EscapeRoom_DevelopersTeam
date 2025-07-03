package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;

import dao.impl.h2.ConnectionDAOH2Impl;
import dao.impl.h2.NotificationDAOH2Impl;
import dao.interfaces.CertificateWinDAO;
import dao.interfaces.NotificationDAO;
import dao.interfaces.RewardWinDAO;
import mvc.enumsMenu.OptionsMenuPlayerAward;
import mvc.model.CertificateWin;
import mvc.model.Notification;
import mvc.model.RewardWin;
import mvc.view.BaseView;

import java.time.LocalDateTime;
import java.util.Optional;

public class PlayerAwardsController {

    private BaseView baseView;
    private RewardWinDAO rewardWinDAO;
    private CertificateWinDAO certificateWinDAO;
    private CertificateController certificateController;
    private RewardController rewardController;
    private RewardWinController rewardWinController;
    private CertificateWinController certificateWinController;

    private static final String NAME_OBJECT = "Player Awards";

    public PlayerAwardsController() {
        baseView = BaseView.getInstance();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
        try {
            this.rewardWinDAO = DAOFactory.getDAOFactory().getRewardWinDAO();
            this.certificateWinDAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        rewardController = new RewardController();
        certificateController =  new CertificateController();
        certificateWinController = new CertificateWinController();
        rewardWinController = new RewardWinController();
}

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuPlayerAward.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuPlayerAward selectedOption = OptionsMenuPlayerAward.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessage2ln("Returning to Main Menu...");
                            return;
                        }
                        case AWARD_REWARD_WIN -> addRewardWinToPlayer();
                        case AWARD_CERTIFICATE_WIN -> addCertificateWinToPlayer();
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

    private void addCertificateWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### AWARD CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int roomId = 0;
        int certificateId = 0;
        String description = "";

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = PlayerController.getInstance().getPlayerIdWithList();

            baseView.displayMessage2ln("List of Rooms");
            roomId = RoomController.getInstance().getRoomIdWithList();

            baseView.displayMessage2ln("List of Certificates");
            certificateId = certificateController.getCertificateIdWithList();

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
            try {
                NotificationDAO notificationDAO = new NotificationDAOH2Impl(ConnectionDAOH2Impl.getInstance());

                String certificateName = certificateController.getCertificateNameById(certificateId);

                Notification notification = Notification.builder()
                        .idPlayer(playerId)
                        .message("Congratulations! You have won the reward: " + certificateName)
                        .dateTimeSent(LocalDateTime.now())
                        .isActive(true)
                        .build();

                notificationDAO.saveNotification(notification);

                baseView.displayMessage2ln("Notification sent to Player ID: " + playerId
                        + " for winning reward: " + certificateName);

            } catch (DAOException e) {
                baseView.displayErrorMessage("Error while sending notification: " + e.getMessage());
            } catch (DatabaseConnectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addRewardWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### AWARD REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardId = 0;
        String description = "";

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = PlayerController.getInstance().getPlayerIdWithList();

            baseView.displayMessage2ln("List of Rewards");
            rewardId = rewardController.getRewardIdWithList();

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
            try {
                NotificationDAO notificationDAO = new NotificationDAOH2Impl(ConnectionDAOH2Impl.getInstance());

                String rewardName = rewardController.getRewardNameById(rewardId);

                Notification notification = Notification.builder()
                        .idPlayer(playerId)
                        .message("Congratulations! You have won the reward: " + rewardName)
                        .dateTimeSent(LocalDateTime.now())
                        .isActive(true)
                        .build();

                notificationDAO.saveNotification(notification);

                baseView.displayMessage2ln("Notification sent to Player ID: " + playerId
                        + " for winning reward: " + rewardName);

            } catch (DAOException e) {
                baseView.displayErrorMessage("Error while sending notification: " + e.getMessage());
            } catch (DatabaseConnectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void revokeCertificateWinToPlayer() throws DAOException {
        baseView.displayMessage2ln("#### REVOKE CERTIFICATE WIN TO PLAYER #################");

        int playerId = 0;
        int certificateWinId = 0;

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = PlayerController.getInstance().getPlayerIdWithList();

            baseView.displayMessage2ln("List of Certificates Wins");
            certificateWinId = certificateWinController.getCertificateWinForPlayerWithList(playerId);

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<CertificateWin> existCertificateWinOpt = certificateWinDAO.findById(certificateWinId);
        if (existCertificateWinOpt.isPresent()) {
            existCertificateWinOpt.get().setActive(false);
            certificateWinDAO.update(existCertificateWinOpt.get());
            baseView.displayMessage2ln("Certificate Win Player unsubscribed successfully.");
        }
    }

    private void revokeRewardWinToPlayer() throws IllegalArgumentException, DAOException {
        baseView.displayMessage2ln("#### REVOKE REWARD WIN TO PLAYER #################");

        int playerId = 0;
        int rewardWinId = 0;

        try {
            baseView.displayMessage2ln("List of Players");
            playerId = PlayerController.getInstance().getPlayerIdWithList();

            baseView.displayMessage2ln("List of Rewards Wins");
            rewardWinId = rewardWinController.getRewardWinForPlayerWithList(playerId);

        } catch (IllegalArgumentException e) {
            baseView.displayErrorMessage(e.getMessage());
            return;
        }

        Optional<RewardWin> existRewardWinOpt = rewardWinDAO.findById(rewardWinId);
        if (existRewardWinOpt.isPresent()) {
            existRewardWinOpt.get().setActive(false);
            rewardWinDAO.update(existRewardWinOpt.get());
            baseView.displayMessage2ln("Reward Win Player unsubscribed successfully.");
        }
    }

    private String getDescription() {
        return baseView.getReadRequiredString("Enter description (30 chars): ");
    }
}