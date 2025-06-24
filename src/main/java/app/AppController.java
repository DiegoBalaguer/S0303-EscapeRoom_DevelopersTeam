package app;

import enums.OptionsMenuMain;
import model.Element;
import model.EscapeRoom;
import model.Player;
import model.Room;
import utils.ConsoleUtils;

import java.util.List;

public class AppController {
    private EscapeRoom escapeRoom;

    public void run() {
        escapeRoom = EscapeRoom.getInstance();
        menu();
        ConsoleUtils.closeScanner();
    }

    private void menu() {
        do {
            OptionsMenuMain.viewMenu("Main Menu");
            int answer = ConsoleUtils.readRequiredInt("");
            try {
                OptionsMenuMain idMenu = OptionsMenuMain.values()[answer - 1];
                if (idMenu == OptionsMenuMain.EXIT) {
                    return;
                } else menuOptionsWithRooms(idMenu);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: The value is wrong.");
            }
        } while (true);
    }

    private void menuOptionsWithRooms(OptionsMenuMain idMenu) {
        switch (idMenu) {
            case USER_MANAGEMENT -> manageModule("Player Management", "players"); // Submenú para Usuarios
            case ESCAPE_ROOM_MANAGEMENT -> manageModule("Escape Room Management", "escapeRooms"); // Submenú para Salas
            case FINANCIAL_MANAGEMENT -> System.out.println("Financial Management not implemented yet.");
            case ELEMENT_MANAGEMENT -> manageModule("Element Management", "elements"); // Submenú para Elementos
            default -> System.out.println("Error: The value is wrong.");
        }
    }

    private void manageModule(String title, String type) {
        switch (type) {
            case "elements" -> {
                // Submenú para elementos (Element y subclases)
                List<Element> elements = escapeRoom.getElements();
                // Solución: usar una clase concreta en lugar de Element::new
                AppWorker<Element> appWorker = new AppWorker<>(title, ConcreteElement::new, elements);
                appWorker.display();
            }
            case "players" -> {
                // Submenú para usuarios (Player)
                List<Player> players = escapeRoom.getPlayers();
                AppWorker<Player> appWorker = new AppWorker<>(title, Player::new, players); // Sin CalculateCommand
                appWorker.display();
            }
            case "escapeRooms" -> {
                // Submenú para Escape Rooms (Room)
                List<Room> rooms = escapeRoom.getRooms();
                AppWorker<Room> appWorker = new AppWorker<>(title, Room::new, rooms); // Sin CalculateCommand
                appWorker.display();
            }
            default -> System.out.println("No matching module found.");
        }
    }
}