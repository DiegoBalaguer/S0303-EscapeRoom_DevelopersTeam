package dao.loadConfigDB;

import loadConfigApp.LoadConfigApp;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class LoadConfigDB {

    private static final Path INPUT_FILE_WITH_PATH = Path.of(LoadConfigApp.getAppFileDbConfig()).normalize();
    private static Properties properties = new Properties();

    private LoadConfigDB() {}

    public static void initialitze() {

        properties = new Properties();
        try {
            properties.load(new FileInputStream(INPUT_FILE_WITH_PATH.toFile()));
            log.info("Config file load to: {}", INPUT_FILE_WITH_PATH);
        } catch (IOException e) {
            log.error("Error loading config file: {}", e.getMessage());
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

    // Database MySql configuration
    public static boolean getMysqlSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("mysql.sshEnable"));
    }

    public static String getMysqlUrl() {
        return properties.getProperty("mysql.url");
    }

    public static String getMysqlHost() {
        return properties.getProperty("mysql.host");
    }

    public static int getMysqlPort() {
        return Integer.parseInt(properties.getProperty("mysql.port"));
    }

    public static String getMysqlSchema() {
        return properties.getProperty("mysql.schema");
    }

    public static String getMysqlUser() {
        return properties.getProperty("mysql.user");
    }

    public static String getMysqlPassword() {
        return properties.getProperty("mysql.password");
    }

    // Database H2 configuration
    public static String getH2Url() {
        return properties.getProperty("h2.url");
    }

    public static String getH2Driver() {
        return properties.getProperty("h2.driver");
    }

    public static String getH2File() {
        return properties.getProperty("h2.fileWithPath");
    }

    public static int getH2Schema() {
        return Integer.parseInt(properties.getProperty("h2.Schema"));
    }

    public static String getH2User() {
        return properties.getProperty("h2.user");
    }

    public static String getH2Password() {
        return properties.getProperty("h2.password");
    }

    // Database MongoDB configuration
    public static boolean getMongoSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("mongo.ssh"));
    }

    public static String getMongoUrl() {
        return properties.getProperty("mongo.url");
    }

    public static String getMongoHost() {
        return properties.getProperty("mongo.host");
    }

    public static int getMongoPort() {
        return Integer.parseInt(properties.getProperty("mongo.port"));
    }

    public static String getMongoSchema() {
        return properties.getProperty("mongo.schema");
    }

    public static String getMongoUser() {
        return properties.getProperty("mongo.user");
    }

    public static String getMongoPassword() {
        return properties.getProperty("mongo.password");
    }

    // SSH optional

    public static String getSshHost() {
        return properties.getProperty("ssh.host");
    }

    public static int getSshPort() {
        return Integer.parseInt(properties.getProperty("ssh.port"));
    }

    public static String getSshUser() {
        return properties.getProperty("ssh.user");
    }

    public static String getSshPassword() {
        return properties.getProperty("ssh.password");
    }

    public static int getSshLocalPort() {
        return Integer.parseInt(properties.getProperty("ssh.localPort"));
    }

    public static String getSshRemoteHost() {
        return properties.getProperty("ssh.remoteHost");
    }

    public static int getSshRemotePort() {
        return Integer.parseInt(properties.getProperty("ssh.remotePort"));
    }
}