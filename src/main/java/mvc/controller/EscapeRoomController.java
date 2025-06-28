package mvc.controller;

import mvc.enumsMenu.OptionsMenuEscapeRoom;
import config.loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;

@Slf4j
public class EscapeRoomController {

    private static EscapeRoomController escapeRoomControllerInstance;

    public static EscapeRoomController getInstance() {
            if (escapeRoomControllerInstance == null) {
                synchronized (EscapeRoomController.class) {
                    if (escapeRoomControllerInstance == null) {
                        escapeRoomControllerInstance = new EscapeRoomController();
                    }
                }
            }
            log.debug("Created EscapeRoomWorkers Singleton");
            return escapeRoomControllerInstance;
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
                        case CLUE_MANAGEMENT -> ClueController.getInstance().mainMenu();
                        case DECORATION_MANAGEMENT -> DecorationController.getInstance().mainMenu();

                    default -> log.warn("Error: The value {} is wrong.", idMenu);
                }
            } catch (NullPointerException e) {
                log.error("Error: The value is wrong in main menu.");
            }
        } while (true);
    }


}