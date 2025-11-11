package com.example.faceauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FaceAuthSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceAuthSystemApplication.class, args);
    }

    // YE LINE ADD KARO
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}