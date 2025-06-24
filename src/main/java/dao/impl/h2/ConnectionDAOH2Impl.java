package dao.impl.h2;

import dao.interfaces.ConnectionDAO;
import dao.interfaces.ConnectionDAOsql;
import dao.loadConfigDB.LoadConfigDB;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionDAOH2Impl implements ConnectionDAO, ConnectionDAOsql {
    private static volatile ConnectionDAOH2Impl instance;
    private static Connection connection;

    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigDB.getH2File()).normalize().toAbsolutePath();

    private static final String URL = LoadConfigDB.getH2Url() + INPUT_FILE_WITH_PATH;
    private static final String USERNAME = LoadConfigDB.getH2User();
    private static final String PASSWORD = LoadConfigDB.getH2Password();

    private ConnectionDAOH2Impl() {

        try {
            Class.forName(LoadConfigDB.getH2Driver());
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Conexión a la base de datos establecida exitosamente");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Error al conectar con la base de datos: {}", e.getMessage());
            throw new RuntimeException("Error de conexión a la base de datos", e);
        }
    }

    public static ConnectionDAOH2Impl getInstance() {
        if (instance == null) {
            synchronized (ConnectionDAOH2Impl.class) {
                if (instance == null) {
                    instance = new ConnectionDAOH2Impl();
                }
            }
        }
        return instance;
    }

    public static Connection getConnection() {
        getInstance();
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            log.error("Error al obtener la conexión: {}", e.getMessage());
            throw new RuntimeException("Error Connection BD H2.", e);
        }
        return connection;
    }

    @Override
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("Conexión cerrada exitosamente");
            }
        } catch (SQLException e) {
            log.error("Error al cerrar la conexión: {}", e.getMessage());
        }
    }
}
