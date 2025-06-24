package app;

import dao.loadConfigDB.LoadConfigDB;
import enums.OptionsMenuMain;
<<<<<<< HEAD
import model.Element;
import model.EscapeRoom;
import model.Player;
import model.Room;
import utils.ConsoleUtils;

import java.util.List;
=======

import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;


@Slf4j
public class AppController {
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c

public class AppController {
    private EscapeRoom escapeRoom;

    public void run() {
<<<<<<< HEAD
=======
        log.info("run has started successfully.");

        LoadConfigApp.initialitze();
        LoadConfigDB.initialitze();
        System.out.println("Tipo bD: " + LoadConfigDB.getDbType());
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c
        escapeRoom = EscapeRoom.getInstance();
        menu();
        ConsoleUtils.closeScanner();

    }

    private void menu() {
        do {
<<<<<<< HEAD
            OptionsMenuMain.viewMenu("Main Menu");
            int answer = ConsoleUtils.readRequiredInt("");
=======
            OptionsMenuMain.viewMenu(LoadConfigApp.getAppName());
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c
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

<<<<<<< HEAD
    private void menuOptionsWithRooms(OptionsMenuMain idMenu) {
        switch (idMenu) {
            case USER_MANAGEMENT -> manageModule("Player Management", "players"); // Submenú para Usuarios
            case ESCAPE_ROOM_MANAGEMENT -> manageModule("Escape Room Management", "escapeRooms"); // Submenú para Salas
            case FINANCIAL_MANAGEMENT -> System.out.println("Financial Management not implemented yet.");
            case ELEMENT_MANAGEMENT -> manageModule("Element Management", "elements"); // Submenú para Elementos
            default -> System.out.println("Error: The value is wrong.");
=======
    private void menuOptionsWithoutRooms(OptionsMenuMain idMenu) {
        switch (idMenu) {
            case ESCAPE_ROOM_MANAGEMENT -> nada();
            default -> {
                if (escapeRoom.isEmptyRooms()) {
                    log.warn("Option {} incorrect, no rooms registered into the system.", idMenu);
                } else menuOptionsWithRooms(idMenu);
            }
        }
    }

    private void menuOptionsWithRooms(OptionsMenuMain idMenu) {
        switch (idMenu) {
            case PLAYER_MANAGEMENT -> nada();
            case ESCAPE_ROOM_MANAGEMENT -> nada();
            case FINANCIAL_MANAGEMENT -> nada();

            default -> log.warn("Error: The value {} is wrong.", idMenu);
>>>>>>> 04232f6687ecd18771ec8f7ee29a19169caeb16c
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