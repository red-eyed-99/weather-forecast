package annotations;

import config.ApplicationConfig;
import config.TestDataSourceConfig;
import config.TestFlywayConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {ApplicationConfig.class, TestDataSourceConfig.class, TestFlywayConfig.class})
@TestPropertySource("classpath:/properties/application-test.properties")
@Transactional
public @interface IntegrationTest {
}
