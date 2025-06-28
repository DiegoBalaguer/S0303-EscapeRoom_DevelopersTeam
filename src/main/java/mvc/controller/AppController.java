package mvc.controller;

import mvc.enumsMenu.OptionsMenuMain;
import config.loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;

@Slf4j
public class AppController {

    private static AppController appControllerInstance;

    public static AppController getInstance() {
        if (appControllerInstance == null) {
            synchronized (AppController.class) {
                if (appControllerInstance == null) {
                    appControllerInstance = new AppController();
                }
            }
        }
        log.info("Created AppWorkers Singleton");
        return appControllerInstance;
    }

    public void mainMenu() {
        do {
            OptionsMenuMain.viewMenu(LoadConfigApp.getAppName());
            int answer = ConsoleUtils.readRequiredInt("Choose an option: ");
            try {
                OptionsMenuMain idMenu = OptionsMenuMain.getOptionByNumber(answer);
                switch (idMenu) {
                    case EXIT -> {
                        return;
                    }
                    case TICKET_MANAGEMENT -> SaleController.getInstance().mainMenu();
                    case ROOM_MANAGEMENT ->   RoomController.getInstance().mainMenu();
                    case PLAYER_MANAGEMENT -> PlayerController.getInstance().mainMenu();
                    case ESCAPE_ROOM_MANAGEMENT -> EscapeRoomController.getInstance().mainMenu();
                    case FINANCIAL_MANAGEMENT -> nada(idMenu.getOPTION_NUMBER() + ". " +idMenu.getDESCRIPTION());

                    default -> log.warn("Error: The value {} is wrong.", idMenu);
                }
            } catch (NullPointerException e) {
                log.error("Error: The value is wrong in main menu.");
            }
        } while (true);
    }

    private void nada(String message) {
        System.err.println(System.lineSeparator() + message + System.lineSeparator());
    }
}
