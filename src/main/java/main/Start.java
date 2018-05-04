package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"service"})//, "service.controller", "service.Serializing", "service.security", "repository.validation"})
@EnableJpaRepositories({"repository"})
@PropertySource("classpath:application.properties")
//@EntityScan({"repository.dao"})
@Configuration
public class Start {
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }
}

