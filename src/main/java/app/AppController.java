package app;

import dao.interfaces.EscapeRoomDAO;
import dao.loadConfigDB.LoadConfigDB;

import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import utils.ConsoleUtils;


@Slf4j
public class AppController {

    private static AppController appControllerInstance;
    private EscapeRoomDAO escapeRoomDAO;

    public static AppController getInstance() {
        if (appControllerInstance == null) {
            synchronized (AppController.class) {
                if (appControllerInstance == null) {
                    appControllerInstance = new AppController();
                }
            }
        }
        log.debug("Created AppController Singleton");
        return appControllerInstance;
    }

    public void startApp() {
        LoadConfigApp.initialitze();
        LoadConfigDB.initialitze();
        log.debug("Database Type: {}", LoadConfigDB.getDbType());
        log.debug("run has started successfully.");
        AppWorkers.getInstance().mainMenu();
        ConsoleUtils.closeScanner();
        if (escapeRoomDAO != null) {
            try {
                escapeRoomDAO.closeConnection();
                log.info("Database connection closed via DAOFactory.");
            } catch (Exception e) {
                log.error("Error closing database connection via DAOFactory: {}", e.getMessage());
            }
        }
    }
}
