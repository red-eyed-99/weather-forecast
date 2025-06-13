package utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.Properties;

@UtilityClass
public class TestPropertiesUtil {

    private static final String TEST_PROPERTIES_PATH = "/properties/application-test.properties";
    private static final String HIBERNATE_PROPERTIES_PATH = "/properties/hibernate-test.properties";

    private static final Properties TEST_PROPERTIES;
    private static final Properties HIBERNATE_PROPERTIES;

    static {
        TEST_PROPERTIES = initProperties(TEST_PROPERTIES_PATH);
        HIBERNATE_PROPERTIES = initProperties(HIBERNATE_PROPERTIES_PATH);
    }

    public static Properties getTestProperties() {
        return TEST_PROPERTIES;
    }

    public static Properties getHibernateProperties() {
        return HIBERNATE_PROPERTIES;
    }

    @SneakyThrows
    private static Properties initProperties(String path) {
        var properties = new Properties();
        properties.load(PropertiesUtil.class.getResourceAsStream(path));
        return properties;
    }
}
