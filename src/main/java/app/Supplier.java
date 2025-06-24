package app;

import model.Player;
import model.Room;
import model.Element;

import java.util.List;
import java.util.function.Supplier;

import static model.EscapeRoom.escapeRoom;

public class SupplierModule {
    private void manageModule(String title, String type) {
        if (escapeRoom == null) {
            throw new IllegalStateException("EscapeRoom instance is not initialized.");
        }

        switch (type) {
            case "elements" -> {
                List<Element> elements = escapeRoom.getElements();
                if (elements == null || elements.isEmpty()) {
                    System.out.println("No elements are available.");
                    return;
                }

                AppWorker<Element> appWorker = new AppWorker<>(title, this::createDefaultElement, elements);
                appWorker.display();
            }
            case "players" -> {
                List<Player> players = escapeRoom.getPlayers();
                if (players == null || players.isEmpty()) {
                    System.out.println("No players are available.");
                    return;
                }

                AppWorker<Player> appWorker = new AppWorker<>(title, () -> new Player(0, "default", "default@example.com"), players);
                appWorker.display();
            }
            case "escapeRooms" -> {
                List<Room> rooms = escapeRoom.getRooms();
                if (rooms == null || rooms.isEmpty()) {
                    System.out.println("No escape rooms are available.");
                    return;
                }

                AppWorker<Room> appWorker = new AppWorker<>(title, () -> new Room("Default Room"), rooms);
                appWorker.display();
            }
            default -> System.out.println("No matching module found. Valid types are: elements, players, escapeRooms.");
        }
    }

    private Element createDefaultElement() {
        return new ExampleElement(); // Clase concreta de ejemplo que extienda Element
    }
}