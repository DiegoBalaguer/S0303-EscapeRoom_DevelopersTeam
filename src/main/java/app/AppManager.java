package app;

import config.ImportData;

import config.LoadConfigDB;

import config.LoadConfigApp;
import dao.connection.SSHSessionManager;
import dao.interfaces.EscapeRoomDAO;
import lombok.extern.slf4j.Slf4j;
import mvc.controller.AppController;
import mvc.model.EscapeRoom;
import utils.ConsoleUtils;


@Slf4j
public class AppManager {

    private static AppManager appManagerInstance;
    private static EscapeRoom escapeRoom;
    private static EscapeRoomDAO escapeRoomDAO;
    private static SSHSessionManager sshSessionManager;

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

    public void startApp(String configFileApp) {

        LoadConfigApp.initialitze(configFileApp);
        LoadConfigDB.initialitze();
        escapeRoom = EscapeRoom.getInstance();
        ImportData.configDataEscapeRoom(escapeRoom);
    try {
        if (LoadConfigDB.getSshEnable()) {
            sshSessionManager = SSHSessionManager.getInstance();
        }
    } catch (Exception e) {
        log.error("Error connecting SSH instance {}", e.getMessage());
    }
        log.debug("Database Type: {}", LoadConfigDB.getDbType());
        log.debug("run has started successfully.");

        AppController.getInstance(escapeRoom).mainMenu();
        ConsoleUtils.closeScanner();
        try {
            if (escapeRoomDAO != null) {
                escapeRoomDAO.closeConnection();
                log.info("Database connection closed via DAOFactory.");
            }
            if (sshSessionManager != null) {
                sshSessionManager.close();
            }
        } catch (Exception e) {
            log.error("Error closing database connection via DAOFactory: {}", e.getMessage());
        }
    }
}
