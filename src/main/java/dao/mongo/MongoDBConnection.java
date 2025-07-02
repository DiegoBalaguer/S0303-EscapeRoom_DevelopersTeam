package dao.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import dao.connection.SSHSessionManager;
import dao.exceptions.DatabaseConnectionException;
import config.LoadConfigDB;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBConnection {
    private static final Logger log = LoggerFactory.getLogger(MongoDBConnection.class);

    private static MongoDBConnection instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private SSHSessionManager sshSessionManager;

    private final boolean TUNNEL_SSH = LoadConfigDB.getMongodbSshEnable();
    private static final String URL = LoadConfigDB.getMongodbUrl();
    private static final String HOST = LoadConfigDB.getMongodbHost();
    private static final int PORT = LoadConfigDB.getMongodbLocalPort();
    private static final String USERNAME = LoadConfigDB.getMongodbUser();
    private static final String PASSWORD = LoadConfigDB.getMongodbPassword();
    private static final String SCHEMA = LoadConfigDB.getMongodbSchema();
    private static final String COLLECTION_NAME = LoadConfigDB.getMongodbCollectionName();

    private MongoDBConnection() throws DatabaseConnectionException {
        try {
            if (TUNNEL_SSH) {
                try {
                    sshSessionManager = SSHSessionManager.getInstance();
                    if (!sshSessionManager.isConnected()) {
                        throw new DatabaseConnectionException("SSH tunnel failed to connect for MongoDB.");
                    }
                    log.info("SSH Tunnel established for MongoDB connection.");
                } catch (Exception e) {
                    log.error("Failed to establish SSH tunnel for MongoDB: {}", e.getMessage());
                    throw new DatabaseConnectionException("Failed to establish SSH tunnel for MongoDB.", e);
                }
            }
            ConnectionString connectionString = new ConnectionString(URL + HOST + ":" + PORT);

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
        instance = null;
    }


    /**
     * Obtiene el ID del escape room con ID mas bajo
     *
     * @return ID del escape room o null si no se encuentra
     */
    public Optional<String> getEscapeRoomId() throws DatabaseConnectionException {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Bson projection = Projections.include("_id");
            Bson sort = Sorts.ascending("_id");

            Document doc = collection.find()
                    .projection(projection)
                    .sort(sort)
                    .first();
            return doc != null ? Optional.of(doc.getObjectId("_id").toString()) : Optional.empty();
        } catch (Exception e) {
            log.error("Connection error with mongoDB: {}", e.getMessage());
            throw new DatabaseConnectionException("Connection error with mongoDB", e);
        }
    }

    /**
     * Obtiene el ID de un escape room a partir de su nombre
     *
     * @param escapeRoomName Nombre del escape room a buscar
     * @return ID del escape room o null si no se encuentra
     */
    public String getEscapeRoomIdByName(String escapeRoomName) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document doc = collection.find(Filters.eq("name", escapeRoomName))
                    .projection(new Document("_id", 1))
                    .first();
            return doc != null ? doc.getObjectId("_id").toString() : null;
        } catch (Exception e) {
            log.error("Error al obtener el ID del escape room por nombre: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el nombre del escape room
     *
     * @param escapeRoomId ID del escape room
     * @return Nombre del escape room o null si no se encuentra
     */
    public String getEscapeRoomName(String escapeRoomId) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document doc = collection.find(Filters.eq("_id", new ObjectId(escapeRoomId)))
                    .projection(new Document("name", 1).append("_id", 0))
                    .first();
            return doc != null ? doc.getString("name") : null;
        } catch (Exception e) {
            log.error("Error al obtener el nombre del escape room: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene la dirección del escape room
     *
     * @param escapeRoomId ID del escape room
     * @return Dirección del escape room o null si no se encuentra
     */
    public String getEscapeRoomAddress(String escapeRoomId) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document doc = collection.find(Filters.eq("_id", new ObjectId(escapeRoomId)))
                    .projection(new Document("address", 1).append("_id", 0))
                    .first();
            return doc != null ? doc.getString("address") : null;
        } catch (Exception e) {
            log.error("Error al obtener la dirección del escape room: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza el nombre de un escape room
     *
     * @param escapeRoomId ID del escape room
     * @param newName      Nuevo nombre
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean setEscapeRoomName(String escapeRoomId, String newName) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Bson filter = Filters.eq("_id", escapeRoomId);
            Bson update = Updates.set("name", newName);
            UpdateResult result = collection.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            log.error("Error al actualizar el nombre del escape room: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la dirección de un escape room
     *
     * @param escapeRoomId ID del escape room
     * @param newAddress   Nueva dirección
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean setEscapeRoomAddress(String escapeRoomId, String newAddress) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Bson filter = Filters.eq("_id", escapeRoomId);
            Bson update = Updates.set("address", newAddress);
            UpdateResult result = collection.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            log.error("Error al actualizar la dirección del escape room: {}", e.getMessage());
            return false;
        }
    }
}