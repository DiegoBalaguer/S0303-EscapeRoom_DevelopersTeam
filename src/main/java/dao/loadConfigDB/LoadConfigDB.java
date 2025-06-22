package dao.loadConfigDB;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class LoadConfigDB {

    private static final Path INPUT_FILE_WITH_PATH = Path.of(".local", "config", "config.db.secret.properties").normalize();
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

    // SSH optional
    public static boolean getSshEnable() {
        return Boolean.parseBoolean(properties.getProperty("ssh.enabled"));
    }

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