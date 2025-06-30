package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.PlayerDAO;
import mvc.enumsMenu.OptionsMenuPlayerNotify;
import mvc.model.Player;
import mvc.view.BaseView;
import mvc.view.PlayerNotifyView;

import java.util.Optional;

public class PlayerNotifyController {

    private static PlayerNotifyController playerNotifyController;
    private final BaseView baseView;
    private final PlayerNotifyView playerNotifyView;
    private final PlayerDAO playerDAO;
    private static final String NAME_OBJECT = "Player Notify";

    private PlayerNotifyController() {
        this.baseView = new BaseView();
        baseView.displayDebugMessage("Creation Class: " + this.getClass().getName());
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
        return playerNotifyController;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuPlayerNotify.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
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
                        // case NOTIFY -> showNotifications();
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

        Optional<Player> existPlayerOpt = playerDAO.findById(PlayerController.getInstance().getPlayerIdWithList());
            existPlayerOpt.get().setSubscribed(true);
            playerDAO.update(existPlayerOpt.get());
            baseView.displayMessageln("Player subscribed successfully: " + existPlayerOpt.get().getName() + " (ID: " + existPlayerOpt.get().getId() + ")");
        }


    private void unSubscribePlayer() throws DAOException {
        baseView.displayMessageln("#### UNSUBSCRIBE PLAYER  #################");

        Optional<Player> existPlayerOpt = playerDAO.findById(PlayerController.getInstance().getPlayerIdWithList());
        existPlayerOpt.get().setSubscribed(false);
        playerDAO.update(existPlayerOpt.get());
        baseView.displayMessageln("Player subscribed successfully: " + existPlayerOpt.get().getName() + " (ID: " + existPlayerOpt.get().getId() + ")");
    }
}