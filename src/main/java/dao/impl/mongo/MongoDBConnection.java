package dao.impl.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dao.connection.SSHSessionManager; // Importa SSHSessionManager
import dao.exceptions.DatabaseConnectionException;
import config.loadConfigDB.LoadConfigDB;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MongoDBConnection {

    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private SSHSessionManager sshSessionManager;

    private final boolean TUNNEL_SSH = LoadConfigDB.getMongoSshEnable();


    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigDB.getH2File()).normalize().toAbsolutePath();

    //private static final String DRIVER = LoadConfigDB.getMongoDriver();
    private static final String URL = LoadConfigDB.getMongoUrl() + INPUT_FILE_WITH_PATH;
    private static final String USERNAME = LoadConfigDB.getMongoUser();
    private static final String PASSWORD = LoadConfigDB.getMongoPassword();
    private static final String SCHEMA = LoadConfigDB.getMongoSchema();

    private MongoDBConnection() throws DatabaseConnectionException {
        try {
            // Verificar si se debe usar el túnel SSH
            if (TUNNEL_SSH) {
                try {
                    // Inicializar y conectar el túnel SSH
                    sshSessionManager = new SSHSessionManager();
                    if (!sshSessionManager.isConnected()) {
                        throw new DatabaseConnectionException("SSH tunnel failed to connect for MongoDB.");
                    }
                    log.info("SSH Tunnel established for MongoDB connection.");
                } catch (Exception e) {
                    log.error("Failed to establish SSH tunnel for MongoDB: {}", e.getMessage());
                    throw new DatabaseConnectionException("Failed to establish SSH tunnel for MongoDB.", e);
                }
            }

            // La URL de conexión a MongoDB debe apuntar al host local (127.0.0.1)
            // y al puerto local del túnel SSH, NO al host remoto de MongoDB.
            ConnectionString connectionString = new ConnectionString(LoadConfigDB.getMongoUrl());

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToClusterSettings(builder ->
                            builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS))
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(SCHEMA);
            log.info("Successfully connected to MongoDB database: {}", SCHEMA);

        } catch (Exception e) {
            log.error("Failed to connect to MongoDB (possibly due to SSH or DB issues): {}", e.getMessage(), e);
            // Asegúrate de cerrar el túnel SSH si la conexión a Mongo falla después de establecerlo
            closeSshTunnel();
            throw new DatabaseConnectionException("Failed to connect to MongoDB.", e);
        }
    }

    public static synchronized MongoDBConnection getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    public MongoDatabase getDatabase() throws DatabaseConnectionException {
        if (database == null || mongoClient == null) {
            log.warn("MongoDB client or database object is null. Attempting to re-establish connection.");
            MongoDBConnection.instance = null;
            return getInstance().getDatabase();
        }
        return database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            log.info("MongoDB connection closed.");
        }
        closeSshTunnel(); // Cierra el túnel SSH al cerrar la conexión de MongoDB
        instance = null; // Restablece la instancia para futuras conexiones
    }

    private void closeSshTunnel() {
        if (sshSessionManager != null) {
            try {
                sshSessionManager.close();
                log.info("SSH Tunnel for MongoDB closed.");
            } catch (Exception e) {
                log.error("Error closing SSH session for MongoDB: {}", e.getMessage());
            }
        }
    }
}