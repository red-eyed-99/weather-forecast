package utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private final String HIBERNATE_PROPERTIES_PATH = "/properties/hibernate.properties";
    private final String HIKARI_PROPERTIES_PATH = "/properties/hikari.properties";

    public Properties getHibernateProperties() {
        return getProperties(HIBERNATE_PROPERTIES_PATH);
    }

    public Properties getHikariProperties() {
        return getProperties(HIKARI_PROPERTIES_PATH);
    }

    @SneakyThrows
    private Properties getProperties(String path) {
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

    private boolean containsEnvironmentVariable(String value) {
        return value.startsWith("${") && value.endsWith("}");
    }

    private String trimPlaceholders(String value) {
        var startIndex = 2;
        var endIndex = value.length() - 1;

        return value.substring(startIndex, endIndex);
    }
}