package utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private static final String HIBERNATE_PROPERTIES_PATH = "/properties/hibernate.properties";
    private static final String HIKARI_PROPERTIES_PATH = "/properties/hikari.properties";

    public static Properties getHibernateProperties() {
        return getProperties(HIBERNATE_PROPERTIES_PATH);
    }

    public static Properties getHikariProperties() {
        return getProperties(HIKARI_PROPERTIES_PATH);
    }

    @SneakyThrows
    private static Properties getProperties(String path) {
        var properties = new Properties();

        properties.load(PropertiesUtil.class.getResourceAsStream(path));

        for (var property : properties.entrySet()) {
            var propertyValue = property.getValue().toString();

            if (containsEnvironmentVariable(propertyValue)) {
                propertyValue = System.getenv(trimPlaceholders(propertyValue));
                properties.put(property.getKey(), propertyValue);
            }
        }

        return properties;
    }

    private static boolean containsEnvironmentVariable(String value) {
        return value.startsWith("${") && value.endsWith("}");
    }

    private static String trimPlaceholders(String value) {
        var startIndex = 2;
        var endIndex = value.length() - 1;

        return value.substring(startIndex, endIndex);
    }
}