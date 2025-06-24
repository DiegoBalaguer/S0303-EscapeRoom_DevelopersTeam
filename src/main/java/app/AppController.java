package app;

import dao.loadConfigDB.LoadConfigDB;
import enums.OptionsMenuMain;

import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;


@Slf4j
public class AppController {

    private EscapeRoom escapeRoom;

    public void run() {
        log.info("run has started successfully.");

        LoadConfigApp.initialitze();
        LoadConfigDB.initialitze();
        System.out.println("Tipo bD: " + LoadConfigDB.getDbType());
        escapeRoom = EscapeRoom.getInstance();
        menu();
        ConsoleUtils.closeScanner();

    }

    private void menu() {
        do {
            OptionsMenuMain.viewMenu(LoadConfigApp.getAppName());
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            try {
                OptionsMenuMain idMenu = OptionsMenuMain.values()[answer - 1];
                if (idMenu == OptionsMenuMain.EXIT) {
                    return;
                } else menuOptionsWithoutRooms(idMenu);
            } catch (ArrayIndexOutOfBoundsException e) {
                log.error("Error: The value is wrong.");
            }
        } while (true);
    }

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
        }
    }

    private void nada() {
    }
}
