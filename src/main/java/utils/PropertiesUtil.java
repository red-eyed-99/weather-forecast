package utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private static final String HIBERNATE_PROPERTIES_PATH = "/properties/hibernate.properties";
    private static final String HIKARI_PROPERTIES_PATH = "/properties/hikari.properties";
    private static final String APPLICATION_PROPERTIES_PATH = "/properties/application.properties";

    private static final Properties HIBERNATE_PROPERTIES;
    private static final Properties HIKARI_PROPERTIES;
    private static final Properties APPLICATION_PROPERTIES;

    static {
        HIBERNATE_PROPERTIES = initProperties(HIBERNATE_PROPERTIES_PATH);
        HIKARI_PROPERTIES = initProperties(HIKARI_PROPERTIES_PATH);
        APPLICATION_PROPERTIES = initProperties(APPLICATION_PROPERTIES_PATH);
    }

    public static Properties getHibernateProperties() {
        return getPropertiesCopy(HIBERNATE_PROPERTIES);
    }

    public static Properties getHikariProperties() {
        return getPropertiesCopy(HIKARI_PROPERTIES);
    }

    @SneakyThrows
    private static Properties initProperties(String path) {
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

    private static Properties getPropertiesCopy(Properties properties) {
        var copy = new Properties();
        copy.putAll(properties);
        return copy;
    }
}