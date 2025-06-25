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

    private static final String DRIVER = LoadConfigDB.getH2Driver();
    private static final String URL = LoadConfigDB.getH2Url() + INPUT_FILE_WITH_PATH;
    private static final String USERNAME = LoadConfigDB.getH2User();
    private static final String PASSWORD = LoadConfigDB.getH2Password();


    private ConnectionDAOH2Impl() {
        initializeConnection();
    }

    private void initializeConnection() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("Database connection established successfully");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Error connecting to the database.", e.getMessage());
            throw new RuntimeException("Database connection error", e);
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

    public Connection getConnection() {
        getInstance();
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
            return connection;
        } catch (SQLException e) {
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
                log.info("Connection closed successfully");
            }
        } catch (SQLException e) {
            log.error("Error closing the connection.", e.getMessage());
        }
    }
}
