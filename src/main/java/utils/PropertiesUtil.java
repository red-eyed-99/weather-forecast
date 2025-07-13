package utils;

import exceptions.EnvironmentVariableProcessException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.Properties;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;
import static org.springframework.util.SystemPropertyUtils.PLACEHOLDER_PREFIX;
import static org.springframework.util.SystemPropertyUtils.PLACEHOLDER_SUFFIX;

@UtilityClass
public class PropertiesUtil {

    private static final String HIBERNATE_PROPERTIES_PATH = "/properties/hibernate.properties";
    private static final String HIKARI_PROPERTIES_PATH = "/properties/hikari.properties";
    private static final String APPLICATION_PROPERTIES_PATH = "/properties/application.properties";

    private static final Properties HIBERNATE_PROPERTIES;
    private static final Properties HIKARI_PROPERTIES;
    private static final Properties APPLICATION_PROPERTIES;

    public static final String APPLICATION_PROPERTIES_CLASSPATH = CLASSPATH_URL_PREFIX + APPLICATION_PROPERTIES_PATH;

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

                if (propertyValue == null) {
                    throw new EnvironmentVariableProcessException(property.getValue().toString());
                }

                properties.put(property.getKey(), propertyValue);
            }
        }

        return properties;
    }

    private static boolean containsEnvironmentVariable(String value) {
        return value.startsWith(PLACEHOLDER_PREFIX) && value.endsWith(PLACEHOLDER_SUFFIX);
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