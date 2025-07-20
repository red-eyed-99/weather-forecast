package annotations;

import config.TestApplicationConfig;
import config.TestDataSourceConfig;
import config.TestFlywayConfig;
import config.WebConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IntegrationTest
@ContextConfiguration(classes = {
        TestApplicationConfig.class, WebConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class
})
@WebAppConfiguration
public @interface WebIntegrationTest {
}
