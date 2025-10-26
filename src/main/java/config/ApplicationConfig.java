package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static utils.PropertiesUtil.APPLICATION_PROPERTIES_CLASSPATH;

@ComponentScan({"services", "mappers", "repositories", "validation.validators"})
@PropertySource(APPLICATION_PROPERTIES_CLASSPATH)
@EnableScheduling
public class ApplicationConfig {

    @Value("${rest.connect.timeout}")
    private int connectTimeout;

    @Value("${rest.read.timeout}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        var clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(clientHttpRequestFactory);
    }
}