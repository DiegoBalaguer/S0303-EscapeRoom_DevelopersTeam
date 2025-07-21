package mvc.entities.player.playerNotify;

import dao.exceptions.NotFoundException;
import mvc.entities.MessageMinMax;
import mvc.entities.notification.NotificationService;
import mvc.entities.player.Player;
import mvc.entities.player.PlayerDAO;
import mvc.entities.player.PlayerService;
import mvc.view.BaseView;

import java.util.Optional;

public class PlayerNotifyService {

    private final PlayerService PLAYER_SERVICE;
    private final NotificationService NOTIFICATION_SERVICE;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Player Notify";

    public PlayerNotifyService(PlayerService playerService, NotificationService notificationService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        this.PLAYER_SERVICE = playerService;
        this.NOTIFICATION_SERVICE = notificationService;
    }

    public Optional<Player> getPlayerById(Optional<Integer> playerIdOpt) throws NotFoundException {
        if (playerIdOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return PLAYER_SERVICE.getPlayerById(playerIdOpt.get());
    }

    public void subscribePlayerById(Optional<Player> playerOpt) throws NotFoundException {
        if (playerOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        playerOpt.get().setSubscribed(true);
        PLAYER_SERVICE.updatePlayer(playerOpt.get());
    }

    public void unSubscribePlayerById(Optional<Player> playerOpt) throws NotFoundException {
        if (playerOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        playerOpt.get().setSubscribed(false);
        PLAYER_SERVICE.updatePlayer(playerOpt.get());
    }

    public String getAllNotificationsCompleteInfoDto() {
        return NOTIFICATION_SERVICE.getAllNotificationsCompleteInfoDto().getMessage();
    }

    protected MessageMinMax getAllPlayers() {
        return PLAYER_SERVICE.getAllPlayers();
    }

}