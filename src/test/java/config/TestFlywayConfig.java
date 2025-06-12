package config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestFlywayConfig {

    private final ApplicationContext applicationContext;

    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(applicationContext.getBean(DataSource.class))
                .locations("classpath:db/migration_test")
                .load();
    }
}