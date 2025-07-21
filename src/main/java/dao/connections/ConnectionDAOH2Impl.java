package dao.connections;

import dao.exceptions.DatabaseConnectionException;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.ConnectionDAOsql;
import config.LoadConfigDB;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionDAOH2Impl implements ConnectionDAO, ConnectionDAOsql {
    private static volatile ConnectionDAOH2Impl instance;
    private static Connection connection;

    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigDB.getH2FileWithPath()).normalize().toAbsolutePath();

    private static final String DRIVER = LoadConfigDB.getH2Driver();
    private static final String URL = LoadConfigDB.getH2Url() + INPUT_FILE_WITH_PATH;
    private static final String USERNAME = LoadConfigDB.getH2User();
    private static final String PASSWORD = LoadConfigDB.getH2Password();

    private ConnectionDAOH2Impl() throws DatabaseConnectionException {
        initializeConnection();
    }

    private void initializeConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Database connection to H2 established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection failed: {}", e.getMessage());
            throw new DatabaseConnectionException("Failed to connect to the database.", e);
        }
    }

    public static synchronized ConnectionDAOH2Impl getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new ConnectionDAOH2Impl();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
                log.info("Database connection re-established successfully.");
            }
            return connection;
        } catch (SQLException | DatabaseConnectionException e) {
            log.error("Error getting connection", e.getMessage());
            throw new RuntimeException("H2 DB Connection Error.", e);
        }
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
                log.info("Database connection closed.");
            }
        } catch (SQLException e) {
            log.error("Error closing database connection: {}", e.getMessage());
        }
    }
}
