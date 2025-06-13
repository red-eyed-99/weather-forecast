package annotations;

import config.ApplicationConfig;
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
@ContextConfiguration(classes = {ApplicationConfig.class, WebConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@WebAppConfiguration
public @interface WebIntegrationTest {
}
