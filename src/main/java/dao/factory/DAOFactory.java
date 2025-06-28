package dao.factory;

import dao.exceptions.DatabaseConnectionException;
import dao.loadConfigDB.LoadConfigDB;
import dao.interfaces.EscapeRoomDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFactory {
    private static volatile EscapeRoomDAO factoryInstance;

    private DAOFactory() {
    }

    public static EscapeRoomDAO getDAOFactory() throws DatabaseConnectionException {
        if (factoryInstance == null) {
            synchronized (DAOFactory.class) {
                if (factoryInstance == null) {
                    String factoryType = LoadConfigDB.getDbType().toLowerCase();
                    switch (factoryType) {
                        case "h2" -> factoryInstance = new EscapeRoomDAOH2();
                        // others...
                        default -> {
                            log.warn("Unsupported factory type: {}", factoryType);
                            throw new IllegalArgumentException("Unknown factory type: " + factoryType);
                        }
                    }
                    log.info("Created DAOFactory for {}", factoryType);
                }
            }
        }
        return factoryInstance;
    }
}
