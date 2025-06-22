package dao.factory;

import dao.loadConfigDB.LoadConfigDB;
import dao.interfaces.EscapeRoomDAO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DAOFactory {
    private static final String DEFAULT_FACTORY_TYPE = LoadConfigDB.getDbType();

    @Getter
    @Setter
    private static String factoryType = DEFAULT_FACTORY_TYPE;

    private DAOFactory() {
    }

    public static EscapeRoomDAO getDAOFactory() {
        log.info("DB Type is: {}.", LoadConfigDB.getDbType());
        return switch (factoryType.toLowerCase()) {
            case "mysql", "h2" -> new JdbcEscapeRoomDAO();
            case "h2mem" -> InMemoryEscapeRoomDAO.getInstance();
            default -> {
                log.warn("Unsupported factory type: {}", factoryType);
                throw new IllegalArgumentException("Unknown factory type: " + factoryType);
            }
        };
    }
}
