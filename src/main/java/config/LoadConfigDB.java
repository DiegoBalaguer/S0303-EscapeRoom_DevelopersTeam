package config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class LoadConfigDB {

    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigApp.getAppFileDbConfig()).normalize();
    private static Properties properties = new Properties();

    private LoadConfigDB() {
    }

    public static void initialitze() {

        properties = new Properties();
        try {
            properties.load(new FileInputStream(INPUT_FILE_WITH_PATH.toFile()));
            statusRevision();
            log.info("Config file load to: {}", INPUT_FILE_WITH_PATH);
        } catch (IOException e) {
            log.error("Error loading config file: {}", e.getMessage());
        }
    }

    private static void statusRevision() {
        if (Boolean.parseBoolean(properties.getProperty("mysql.sshEnable"))) {
            properties.setProperty("ssh.enable", "true");
        }
        if (Boolean.parseBoolean(properties.getProperty("mongodb.sshEnable"))) {
            properties.setProperty("ssh.enable", "true");
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    // Database general configuration
    public static String getDbType() {
        return properties.getProperty("db.type");
    }

    // Database H2 configuration
    public static String getH2Url() {
        return properties.getProperty("h2.url");
    }

    public static String getH2Driver() {
        return properties.getProperty("h2.driver");
    }

    public static String getH2FileWithPath() {
        return properties.getProperty("h2.fileWithPath");
    }

    public static int getH2Schema() {
        return Integer.parseInt(properties.getProperty("h2.schema"));
    }

    public static String getH2User() {
        return properties.getProperty("h2.user");
    }

    public static String getH2Password() {
        return properties.getProperty("h2.password");
    }

    // Database H2 configuration
    public static String getSqLiteUrl() {
        return properties.getProperty("sqlite.url");
    }

    public static String getSqLiteDriver() {
        return properties.getProperty("sqlite.driver");
    }

    public static String getSqLiteFileWithPath() {
        return properties.getProperty("sqlite.fileWithPath");
    }

    public static int getSqLiteSchema() {
        return Integer.parseInt(properties.getProperty("sqlite.schema"));
    }

    public static String getSqLiteUser() {
        return properties.getProperty("sqlite.user");
    }

    public static String getSqLitePassword() {
        return properties.getProperty("sqlite.password");
    }

    // Database MySql configuration
    public static boolean getMySqlSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("mysql.sshEnable"));
    }

    public static String getMySqlDriver() {
        return properties.getProperty("mysql.driver");
    }

    public static String getMySqlUrl() {
        return properties.getProperty("mysql.url");
    }

    public static String getMySqlHost() {
        return properties.getProperty("mysql.host");
    }

    public static int getMySqlLocalPort() {
        return Integer.parseInt(properties.getProperty("mysql.localPort"));
    }

    public static String getMySqlSchema() {
        return properties.getProperty("mysql.schema");
    }

    public static String getMySqlUser() {
        return properties.getProperty("mysql.user");
    }

    public static String getMySqlPassword() {
        return properties.getProperty("mysql.password");
    }

    public static int getMySqlRemotePort() {
        return Integer.parseInt(properties.getProperty("mysql.remotePort"));
    }

    // Database MongoDB configuration
    public static boolean getMongodbSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("mongodb.sshEnable"));
    }

    public static boolean getMongodbEnable() {
        return Boolean.parseBoolean(properties.getProperty("mongodb.enable"));
    }

    public static String getMongodbUrl() {
        return properties.getProperty("mongodb.url");
    }

    public static String getMongodbHost() {
        return properties.getProperty("mongodb.host");
    }

    public static int getMongodbLocalPort() {
        return Integer.parseInt(properties.getProperty("mongodb.localPort"));
    }

    public static String getMongodbSchema() {
        return properties.getProperty("mongodb.schema");
    }

    public static String getMongodbCollectionName() {
        return properties.getProperty("mongodb.collectionName");
    }

    public static String getMongodbUser() {
        return properties.getProperty("mongodb.user");
    }

    public static String getMongodbPassword() {
        return properties.getProperty("mongodb.password");
    }

    public static int getMongodbRemotePort() {
        return Integer.parseInt(properties.getProperty("mongodb.remotePort"));
    }

    // SSH optional
    public static boolean getSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("ssh.enable"));
    }

    public static String getSshHost() {
        return properties.getProperty("ssh.host");
    }

    public static int getSshPort() {
        return Integer.parseInt(properties.getProperty("ssh.hostPort"));
    }

    public static String getSshUser() {
        return properties.getProperty("ssh.user");
    }

    public static String getSshPassword() {
        return properties.getProperty("ssh.password");
    }
}