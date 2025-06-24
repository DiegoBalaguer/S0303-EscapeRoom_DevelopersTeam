package app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Player;
import model.Room;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EscapeRoom {
<<<<<<< HEAD:src/main/java/model/EscapeRoom.java
    private static EscapeRoom escapeRoom = null; // Instancia estática inicializada hasta necesario

=======
    private int id;
    private static EscapeRoom escapeRoom;
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c:src/main/java/app/EscapeRoom.java
    private List<Room> rooms;
    private List<Player> players;
    private List<Element> elements;

<<<<<<< HEAD:src/main/java/model/EscapeRoom.java
    private EscapeRoom() {
        initialize();
    }

=======
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c:src/main/java/app/EscapeRoom.java
    public static EscapeRoom getInstance() {
        if (escapeRoom == null) {
            escapeRoom = new EscapeRoom();
        }
        return escapeRoom;
    }

    private void initialize() {
<<<<<<< HEAD:src/main/java/model/EscapeRoom.java
        this.rooms = new ArrayList<>();
        this.players = new ArrayList<>();
        this.elements = new ArrayList<>();
=======
        rooms = new ArrayList<>();
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c:src/main/java/app/EscapeRoom.java
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addElement(Element element) {
        this.elements.add(element);
    }
}