package config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class LoadConfigApp {
    private static final Path INPUT_FILE_WITH_PATH = Path.of("src/main/resources/config/config.app.properties").normalize();
    private static LoadConfigApp loadConfigApp;
    private static Properties properties;

    private LoadConfigApp() {}

    public static void initialitze(String configFileApp) {
        if(loadConfigApp == null) {
            loadConfigApp = new LoadConfigApp();
        }
        Path newFilePath = Path.of(configFileApp).normalize();
        properties = new Properties();
        try {
            properties.load(new FileInputStream(String.valueOf(configFileApp.isBlank() ? INPUT_FILE_WITH_PATH.toFile() : newFilePath)));
            log.info("Config file load to: {}", INPUT_FILE_WITH_PATH);
        } catch (IOException e) {
            log.error("Error loading config file: {}", e.getMessage());
        }
    }

    public static void setAppBusinessId(String value) {
        properties.setProperty("app.businessId", value);
    }

    public static void setAppBusinessName(String value) {
        properties.setProperty("app.businessName", value);
    }

    public static void setAppBusinessAddress(String value) {
        properties.setProperty("app.businessAddress", value);
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static String getAppBusinessId() {
        return properties.getProperty("app.businessId");
    }

    public static String getAppBusinessName() {
        return properties.getProperty("app.businessName");
    }

    public static String getAppBusinessAddress() {
        return properties.getProperty("app.businessAddress");
    }

    public static boolean getAppDebug() {
        return Boolean.parseBoolean(properties.getProperty("app.debug"));
    }


    public static String getAppFileDbConfig() {
        return properties.getProperty("app.fileDbConfig");
    }

    public static String getAppCreateSchema() {
        return properties.getProperty("app.createSchema");
    }

}