package app;

import enums.OptionsMenuMain;
import lombok.extern.slf4j.Slf4j;
import model.EscapeRoom;
import utils.ConsoleUtils;

@Slf4j
public class AppController {

    private EscapeRoom escapeRoom;

    public void run() {
        log.info("run has started successfully.");

        escapeRoom = EscapeRoom.getInstance();
        menu();
        ConsoleUtils.closeScanner();
    }

    private void menu() {
        do {
            OptionsMenuMain.viewMenu("Escape room management");
            int answer = ConsoleUtils.readRequiredInt("");
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
                    log.warn("Option {} incorrect, no rooms registered into the system.",  idMenu);
                } else menuOptionsWithRooms(idMenu);
            }
        }
    }

    private void menuOptionsWithRooms(OptionsMenuMain idMenu) {
        switch (idMenu) {
            case PLAYER_MANAGEMENT -> nada();
            case ESCAPE_ROOM_MANAGEMENT -> nada();
            case FINANCIAL_MANAGEMENT -> nada();

            default -> log.warn("Error: The value {} is wrong.",  idMenu);
        }
    }

    private void nada() {
    }
}
