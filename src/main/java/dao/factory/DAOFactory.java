package dao.factory;

import dao.loadConfigDB.LoadConfigDB;
import dao.interfaces.EscapeRoomDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
