package app;

import dao.interfaces.EscapeRoomDAO;
import config.loadConfigDB.LoadConfigDB;

import config.loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;
import mvc.controller.AppController;
import utils.ConsoleUtils;


@Slf4j
public class AppManager {

    private static AppManager appManagerInstance;
    private EscapeRoomDAO escapeRoomDAO;

    public static AppManager getInstance() {
        if (appManagerInstance == null) {
            synchronized (AppManager.class) {
                if (appManagerInstance == null) {
                    appManagerInstance = new AppManager();
                }
            }
        }
        log.debug("Created AppController Singleton");
        return appManagerInstance;
    }

    public void startApp() {
        LoadConfigApp.initialitze();
        LoadConfigDB.initialitze();
        log.debug("Database Type: {}", LoadConfigDB.getDbType());
        log.debug("run has started successfully.");
        AppController.getInstance().mainMenu();
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
