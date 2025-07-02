package dao.impl.sqLite;

import dao.connection.SSHSessionManager;
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
public class ConnectionDAOSqLiteImpl implements ConnectionDAO, ConnectionDAOsql {
    private static volatile ConnectionDAOSqLiteImpl instance;
    private static Connection connection;

    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigDB.getSqLiteFileWithPath()).normalize().toAbsolutePath();

    private static final String DRIVER = LoadConfigDB.getSqLiteDriver();
    private static final String URL = LoadConfigDB.getSqLiteUrl() + INPUT_FILE_WITH_PATH;
    private static final String USERNAME = LoadConfigDB.getSqLiteUser();
    private static final String PASSWORD = LoadConfigDB.getSqLitePassword();


    private ConnectionDAOSqLiteImpl() throws DatabaseConnectionException {
        initializeConnection();
    }

    private void initializeConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER);

            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Database connection to SqLite established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection failed: {}", e.getMessage());
            throw new DatabaseConnectionException("Failed to connect to the database.", e);
        }
    }

    public static synchronized ConnectionDAOSqLiteImpl getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new ConnectionDAOSqLiteImpl();
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
