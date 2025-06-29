package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.impl.h2.ConnectionDAOH2Impl;
import dao.impl.h2.NotificationDAOH2Impl;
import dao.interfaces.NotificationDAO;
import dao.interfaces.PlayerDAO;
import mvc.enumsMenu.OptionsMenuPlayerNotify;
import lombok.extern.slf4j.Slf4j;
import mvc.model.Notification;
import mvc.model.Player;
import mvc.view.BaseView;
import mvc.view.PlayerNotifyView;

import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayerNotifyController {

    private static PlayerNotifyController playerNotifyController;
    private final BaseView baseView;
    private final PlayerNotifyView playerNotifyView;
    private final PlayerDAO playerDAO;

    private PlayerNotifyController() {
        this.baseView = new BaseView();
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
            int answer = baseView.getInputRequiredInt("Choose an option: ");
            OptionsMenuPlayerNotify selectedOption = OptionsMenuPlayerNotify.getOptionByNumber(answer);

            if (selectedOption != null) {
                try {
                    switch (selectedOption) {
                        case EXIT -> {
                            baseView.displayMessageln("Returning to Main Menu...");
                            return;
                        }
                        case SUBSCRIBE -> subscribePlayer();
                        case UNSUBSCRIBE -> unSubscribePlayer();
                        case NOTIFY -> showAllNotifications();
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

    private void subscribePlayer() throws DAOException {
        baseView.displayMessageln("#### SUBSCRIBE PLAYER  #################");

        Optional<Player> existingPlayerOpt = playerDAO.findById(getPlayerId());
        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            existingPlayer.setSubscribed(true);
            playerDAO.update(existingPlayer);
            baseView.displayMessageln("Player subscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
        }
    }

    private void unSubscribePlayer() throws DAOException {
        baseView.displayMessageln("#### SUBSCRIBE PLAYER  #################");

        Optional<Player> existingPlayerOpt = playerDAO.findById(getPlayerId());
        if (existingPlayerOpt.isPresent()) {
            Player existingPlayer = existingPlayerOpt.get();
            existingPlayer.setSubscribed(false);
            playerDAO.update(existingPlayer);
            baseView.displayMessageln("Player unsubscribed successfully: " + existingPlayer.getName() + " (ID: " + existingPlayer.getId() + ")");
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
    private void showAllNotifications() throws DAOException, DatabaseConnectionException {
        NotificationDAO notificationDAO = new NotificationDAOH2Impl(ConnectionDAOH2Impl.getInstance());
        List<Notification> notifications = notificationDAO.findAllNotifications();

        baseView.displayMessageln("#### NOTIFICATIONS SENT #################");
        if (notifications.isEmpty()) {
            baseView.displayMessageln("No notifications found.");
            return;
        }

        for (Notification notification : notifications) {
            baseView.displayMessageln(
                    "Player ID: " + notification.getIdPlayer() +
                            " | Message: " + notification.getMessage() +
                            " | Date: " + notification.getDateTimeSent()
            );
        }
    }
}