package app;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import enums.OptionsMenuEscapeRoom;
import enums.OptionsMenuMain;
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;
import java.util.List;
import java.util.Optional;

@Slf4j
public class EscapeRoomWorkers {

    private static EscapeRoomWorkers EscapeRoomWorkersInstance;

    public static EscapeRoomWorkers getInstance() {
            if (EscapeRoomWorkersInstance == null) {
                synchronized (EscapeRoomWorkers.class) {
                    if (EscapeRoomWorkersInstance == null) {
                        EscapeRoomWorkersInstance = new EscapeRoomWorkers();
                    }
                }
            }
            log.debug("Created EscapeRoomWorkers Singleton");
            return EscapeRoomWorkersInstance;
        }


    public void mainMenu() {
        do {
            OptionsMenuEscapeRoom.viewMenu(LoadConfigApp.getAppName());
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            try {
                OptionsMenuEscapeRoom idMenu = OptionsMenuEscapeRoom.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        return;
                    }
                        case CLUE_MANAGEMENT ->ClueWorkers.getInstance().mainMenu();
                        case DECORATION_MANAGEMENT -> DecorationWorkers.getInstance().mainMenu();

                    default -> log.warn("Error: The value {} is wrong.", idMenu);
                }
            } catch (NullPointerException e) {
                log.error("Error: The value is wrong in main menu.");
            }
        } while (true);
    }


}