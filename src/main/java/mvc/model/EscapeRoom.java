package mvc.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mvc.model.*;
import java.util.ArrayList;
import java.util.List;
import interfaces.Observer;
import interfaces.Observable;
import dao.impl.h2.*;
import dao.interfaces.PlayerDAO;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EscapeRoom implements Observable {
    private int id;
    private static EscapeRoom escapeRoom;
    private List<Room> rooms;
    private List<Player> players;
    private final List<Observer> observers = new ArrayList<>();
    private PlayerDAO playerDAO;

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

    private void initialize() {
        rooms = new ArrayList<>();
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
            for (Player player : subscribedPlayers) {
                player.update(message);
            }
        } catch (Exception e) {
            System.err.println("Error notifying observers: " + e.getMessage());
        }
    }

    public void notifyRoomCreated(Room room) {
        notifyObservers("New room created: " + room.getName());
    }

    public void notifyItemAdded(String itemType, String roomName) {
        notifyObservers("New " + itemType + " added to room: " + roomName);
    }
}


/*public void notifyCertificateReceived(CertificateWin certificateWin) {
        notifyObservers("Congratulations! You received a certificate: " + certificateWin.getDescription());
    }
    public void notifyRewardReceived(RewardWin rewardWin) {
        notifyObservers("Congratulations! You received a reward: " + rewardWin.getDescription());
    }
}*/