package app;

import enums.OptionsMenuMain;
import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;

@Slf4j
public class AppWorkers {

    private static AppWorkers appWorkersInstance;

    public static AppWorkers getInstance() {
        if (appWorkersInstance == null) {
            synchronized (AppWorkers.class) {
                if (appWorkersInstance == null) {
                    appWorkersInstance = new AppWorkers();
                }
            }
        }
        log.info("Created AppWorkers Singleton");
        return appWorkersInstance;
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
                    case TICKET_MANAGEMENT -> nada(idMenu.getOPTION_NUMBER() + ". " +idMenu.getDESCRIPTION());
                    case ROOM_MANAGEMENT ->  nada(idMenu.getOPTION_NUMBER() + ". " +idMenu.getDESCRIPTION());
                    case PLAYER_MANAGEMENT -> PlayerWorkers.getInstance().mainMenu();
                    case ESCAPE_ROOM_MANAGEMENT -> nada(idMenu.getOPTION_NUMBER() + ". " +idMenu.getDESCRIPTION());
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
