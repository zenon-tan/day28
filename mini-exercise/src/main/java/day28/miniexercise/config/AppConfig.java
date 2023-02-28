package day28.miniexercise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import static day28.miniexercise.config.Constants.*;

@Configuration
public class AppConfig {

    @Value("${spring.data.mongodb.url}")
    private String mongoUrl;
    
    @Primary
    @Bean
    public MongoTemplate createMongoTemplate() {

        MongoClient client = MongoClients.create(mongoUrl);

        return new MongoTemplate(client, "google");

    }

    
}
