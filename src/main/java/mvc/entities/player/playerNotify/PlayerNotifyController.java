package mvc.entities.player.playerNotify;

import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;
import mvc.entities.player.Player;
import mvc.view.BaseView;
import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;

import java.util.Optional;

public class PlayerNotifyController {

    private static PlayerNotifyController instance;
    private final PlayerNotifyService PLAYER_NOTIFY_SERVICE;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Player Notify";

    private PlayerNotifyController(PlayerNotifyService playerNotifyService) throws DatabaseConnectionException {
        this.BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Creation Class: " + this.getClass().getName());
        this.PLAYER_NOTIFY_SERVICE = playerNotifyService;
    }

    public static PlayerNotifyController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (PlayerNotifyController.class) {
                if (instance == null) {
                    PlayerNotifyService service = ServiceManager.getInstance().getPlayerNotifyService();
                    instance = new PlayerNotifyController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() throws DatabaseConnectionException {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuPlayerNotify.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuPlayerNotify selectedOption = OptionsMenuPlayerNotify.getOptionByNumber(answer);
            try {
                switch (selectedOption) {
                    case EXIT -> {
                        BASE_VIEW.displayMessageln("Returning to Main Menu...");
                        return;
                    }
                    case SUBSCRIBE -> subscribePlayer();
                    case UNSUBSCRIBE -> unSubscribePlayer();
                    case NOTIFICATIONS -> showAllNotifications();
                    default -> BASE_VIEW.displayErrorMessage("Unknown option selected.");
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void subscribePlayer() throws DAOException, NotFoundException {
        BASE_VIEW.displayMessageln("#### SUBSCRIBE PLAYER  #################");
        Optional<Player> existPlayerOpt = PLAYER_NOTIFY_SERVICE.getPlayerById(getPlayerId());
        PLAYER_NOTIFY_SERVICE.subscribePlayerById(existPlayerOpt);
        BASE_VIEW.displayMessageln("Player subscribed successfully.");
    }

    private void unSubscribePlayer() throws DAOException, NotFoundException {
        BASE_VIEW.displayMessageln("#### UNSUBSCRIBE PLAYER  #################");
        Optional<Player> existPlayerOpt = PLAYER_NOTIFY_SERVICE.getPlayerById(getPlayerId());
        PLAYER_NOTIFY_SERVICE.unSubscribePlayerById(existPlayerOpt);
        BASE_VIEW.displayMessageln("Player subscribed successfully: " + existPlayerOpt.get().getName() + " (ID: " + existPlayerOpt.get().getId() + ")");
    }

    private void showAllNotifications() throws DAOException {
        BASE_VIEW.displayMessageln("#### NOTIFICATIONS SENT #################");
        BASE_VIEW.displayMessageln(PLAYER_NOTIFY_SERVICE.getAllNotificationsCompleteInfoDto());
    }

    private Optional<Integer> getPlayerId() throws NotFoundException {
        Optional<Integer> playerIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", PLAYER_NOTIFY_SERVICE.getAllPlayers());
        if (playerIdOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return playerIdOpt;
    }
}