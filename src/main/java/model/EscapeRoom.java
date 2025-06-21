package model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EscapeRoom {
    private static EscapeRoom escapeRoom;
    private List<Room> rooms;
    private List<Player> players;

    private EscapeRoom() {
    }

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
}
