package mvc.model;

import dao.interfaces.NotificationDAO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import interfaces.Observer;
import interfaces.Observable;
import dao.impl.h2.*;
import dao.interfaces.PlayerDAO;
import dao.impl.h2.PlayerDAOH2Impl;
import dao.impl.h2.ConnectionDAOH2Impl;
import dao.exceptions.DatabaseConnectionException;
import org.bson.types.ObjectId;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EscapeRoom implements Observable {
    private String id;
    private static EscapeRoom escapeRoom;
    private String name;
    private String address;
    private List<Room> rooms;
    private List<Player> players;
    private final List<Observer> observers = new ArrayList<>();
    private PlayerDAO playerDAO;

    private void initialize() {
        try {
            this.playerDAO = new PlayerDAOH2Impl(ConnectionDAOH2Impl.getInstance());
        } catch (DatabaseConnectionException e) {
            log.error("Error initializing PlayerDAO: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize EscapeRoom due to database connection issues.", e);
        }
        rooms = new ArrayList<>();
    }

    public static EscapeRoom getInstance() {
        if (escapeRoom == null) {
            escapeRoom = new EscapeRoom();
            escapeRoom.initialize();
        }
        return escapeRoom;
    }

    public boolean isEmptyRooms() {
        return rooms == null || rooms.isEmpty();
    }

    @Override
    public void addObserver(Observer observer) {
        throw new UnsupportedOperationException("Observers are managed dynamically using the database.");
    }


    @Override
    public void removeObserver(Observer observer) {
        throw new UnsupportedOperationException("Observers are managed dynamically using the database.");
    }

    @Override
    public void notifyObservers(String message) {
        try {
            List<Player> subscribedPlayers = playerDAO.findSubscribedPlayers();
            if (subscribedPlayers.isEmpty()) {
                log.info("No subscribed players found to notify.");
                return;
            }

            for (Player player : subscribedPlayers) {
                player.update(message);

                // Registrar la notificación en la base de datos.
                Notification notification = Notification.builder()
                        .idPlayer(player.getId())
                        .message(message)
                        .build();
                NotificationDAO notificationDAO = new NotificationDAOH2Impl(ConnectionDAOH2Impl.getInstance());
                notificationDAO.saveNotification(notification);
            }

            log.info("Notifications sent to all subscribed players.");

        } catch (Exception e) {
            log.error("Error notifying players: {}", e.getMessage());
        }
    }

    public void notifyRoomCreated(Room room) {
        String message = "New room created: " + room.getName();
        notifyObservers(message);
    }


    public void notifyItemAdded(String itemType, String roomName) {
        String message = "New " + itemType + " added to room: " + roomName;
        notifyObservers(message);
    }

}


/*public void notifyCertificateReceived(CertificateWin certificateWin) {
        notifyObservers("Congratulations! You received a certificate: " + certificateWin.getDescription());
    }
    public void notifyRewardReceived(RewardWin rewardWin) {
        notifyObservers("Congratulations! You received a reward: " + rewardWin.getDescription());
    }
}*/