package dao.factory;

import dao.loadConfigDB.LoadConfigDB;
import dao.interfaces.EscapeRoomDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFactory {
    private static volatile EscapeRoomDAO factoryInstance;

    private DAOFactory() {
    }

    public static EscapeRoomDAO getDAOFactory() {
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

/*
@Slf4j
public class DAOFactory {
    @Getter
    @Setter
    private static String factoryType = LoadConfigDB.getDbType();

    private DAOFactory() {
    }

    public static EscapeRoomDAO getDAOFactory() {
        log.info("DB Type is: {}.", LoadConfigDB.getDbType());
        return switch (factoryType.toLowerCase()) {
            case "h2" -> new EscapeRoomDAOH2();

            default -> {
                log.warn("Unsupported factory type: {}", factoryType);
                throw new IllegalArgumentException("Unknown factory type: " + factoryType);
            }
        };
    }
}
 */