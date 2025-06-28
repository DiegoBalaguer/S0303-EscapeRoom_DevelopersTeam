package mvc.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import java.util.ArrayList;
import java.util.List;
import interfaces.Observer;
import interfaces.Observable;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EscapeRoom implements Observable {
    private int id;
    private static EscapeRoom escapeRoom;
    private List<Room> rooms;
    private List<Player> players;

    public static EscapeRoom getInstance() {
        if (escapeRoom == null) {
            escapeRoom = new EscapeRoom();
            escapeRoom.initialize();
        }
        return escapeRoom;
    }

    public boolean isEmptyRooms() {
        return rooms.isEmpty();
    }

    private void initialize() {
        rooms = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer instanceof Player) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            if (observer instanceof Player player && player.isSubscribed()) {
                player.update(message);
            }
        }
    }

    public void notifyRoomCreated(Room room) {
        notifyObservers("New room created: " + room.getName());
    }

    public void notifyItemAdded(String itemType, String roomName) {
        notifyObservers("New " + itemType + " added to room: " + roomName);
    }

    public void notifyCertificateReceived(CertificateWin certificateWin) {
        notifyObservers("Congratulations! You received a certificate: " + certificateWin.getDescription());
    }
    public void notifyRewardReceived(RewardWin rewardWin) {
        notifyObservers("Congratulations! You received a reward: " + rewardWin.getDescription());
    }
}