package config;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"services", "mappers", "repositories"})
public class ApplicationConfig {
}