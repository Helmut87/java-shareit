package ru.practicum.shareit.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;

@Configuration
public class GatewayConfig {

    @Value("${shareit-server.url:http://localhost:9090}")
    private String serverUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public BaseClient baseClient(RestTemplate restTemplate) {
        return new BaseClient(serverUrl, restTemplate);
    }
}