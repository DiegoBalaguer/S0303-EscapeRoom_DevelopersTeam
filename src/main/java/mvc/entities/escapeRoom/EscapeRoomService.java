package mvc.entities.escapeRoom;

import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.ServiceManager;
import mvc.entities.notification.NotificationController;
import mvc.entities.notification.NotificationService;
import mvc.entities.player.Player;
import mvc.view.BaseView;

import java.util.List;
import java.util.Optional;

public class EscapeRoomService {

    //private final EscapeRoomDAO ESCAPE_ROOM_DAO;
    private final NotificationService NOTIFICATION_SERVICE;
    private static final String NAME_OBJECT = "Escape Room";

    //public DecorationService(EscapeRoomDAO escapeRoomDAO, NotificationService notificationService) {
    public EscapeRoomService(NotificationService notificationService) {
        //this.ESCAPE_ROOM_DAO = escapeRoomDAO;
        this.NOTIFICATION_SERVICE = notificationService;
    }

    public void notifyObservers(String message) throws DatabaseConnectionException, NotFoundException {
        List<Player> subscribedPlayers =
                ServiceManager.getInstance().getPlayerService().getAllSubscribedPlayers();
        if (subscribedPlayers.isEmpty()) {
            throw new NotFoundException("No subscribed players found to notify.");
        }

        Optional<String> messageOpt = message.describeConstable();
        for (Player player : subscribedPlayers) {
            Optional<Integer> playerId = Optional.of(player.getId());
            ServiceManager.getInstance().getNotificationService().createNotification(playerId, messageOpt);
        }
    }


}
