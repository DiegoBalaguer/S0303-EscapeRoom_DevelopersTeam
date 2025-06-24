package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EscapeRoom {
    private static EscapeRoom escapeRoom = null; // Instancia estática inicializada hasta necesario

    private List<Room> rooms;
    private List<Player> players;
    private List<Element> elements;

    private EscapeRoom() {
        initialize();
    }

    public static EscapeRoom getInstance() {
        if (escapeRoom == null) {
            escapeRoom = new EscapeRoom();
        }
        return escapeRoom;
    }

    private void initialize() {
        this.rooms = new ArrayList<>();
        this.players = new ArrayList<>();
        this.elements = new ArrayList<>();
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