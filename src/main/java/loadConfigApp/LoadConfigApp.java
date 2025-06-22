package loadConfigApp;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

@Slf4j
public class LoadConfigApp {
    private static final Path INPUT_FILE_WITH_PATH = Path.of(".local", "config", "config.app.properties").normalize();
    private static LoadConfigApp loadConfigApp;
    private static Properties properties;

    private LoadConfigApp() {}

    public static void initialitze() {
        if(loadConfigApp == null) {
            loadConfigApp = new LoadConfigApp();
        }
        properties = new Properties();
        try {
            properties.load(new FileInputStream(INPUT_FILE_WITH_PATH.toFile()));
            log.info("Config file load to: {}", INPUT_FILE_WITH_PATH);
        } catch (IOException e) {
            log.error("Error loading config file: {}", e.getMessage());
        }
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

    public static String getAppUser() {
        return properties.getProperty("app.user");
    }

    public static String getAppCreateSchema() {
        return properties.getProperty("app.createSchema");
    }

}