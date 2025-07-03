package dao.impl.mysql;

import config.LoadConfigDB;
import dao.connection.SSHSessionManager;
import dao.exceptions.DatabaseConnectionException;
import dao.interfaces.ConnectionDAO;
import dao.interfaces.ConnectionDAOsql;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionDAOMySqlImpl implements ConnectionDAO, ConnectionDAOsql {
    private static volatile ConnectionDAOMySqlImpl instance;
    private static Connection connection;
    private SSHSessionManager sshSessionManager;

    private static final boolean TUNNEL_SSH = LoadConfigDB.getMySqlSshEnable();
    private static final String DRIVER = LoadConfigDB.getMySqlDriver();
    private static final String URL = LoadConfigDB.getMySqlUrl();
    private static final String HOST =  LoadConfigDB.getMySqlHost();
    private static final int LOCAL_PORT = LoadConfigDB.getMySqlLocalPort();
    private static final String SCHEMA = LoadConfigDB.getMySqlSchema();
    private static final String USERNAME = LoadConfigDB.getMySqlUser();
    private static final String PASSWORD = LoadConfigDB.getMySqlPassword();

    private ConnectionDAOMySqlImpl() throws DatabaseConnectionException {
        initializeConnection();
    }

    private void initializeConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER);

            if (TUNNEL_SSH) {
                try {
                    sshSessionManager = SSHSessionManager.getInstance();
                    if (!sshSessionManager.isConnected()) {
                        throw new DatabaseConnectionException("SSH tunnel failed to connect for MongoDB.");
                    }
                    log.info("SSH Tunnel established for MySql connection.");
                } catch (Exception e) {
                    log.error("Failed to establish SSH tunnel for MongoDB: {}", e.getMessage());
                    throw new DatabaseConnectionException("Failed to establish SSH tunnel for MongoDB.", e);
                }
            }
            String urlFin = URL + "//" + HOST + ":" + LOCAL_PORT + "/" + SCHEMA;
            this.connection = DriverManager.getConnection(urlFin, USERNAME, PASSWORD);
            log.info("Database connection to MySql established successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Database connection failed: {}", e.getMessage());
            throw new DatabaseConnectionException("Failed to connect to the database.", e);
        } catch (DatabaseConnectionException e) {
            throw e;
        }
    }

    public static synchronized ConnectionDAOMySqlImpl getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new ConnectionDAOMySqlImpl();
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
            throw new RuntimeException("MySql DB Connection Error.", e);
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
