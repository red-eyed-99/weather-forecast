package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static utils.PropertiesUtil.APPLICATION_PROPERTIES_CLASSPATH;

@ComponentScan({"services", "mappers", "repositories", "validation.validators"})
@PropertySource(APPLICATION_PROPERTIES_CLASSPATH)
@EnableScheduling
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}