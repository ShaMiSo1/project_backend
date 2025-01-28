package com.example.bangyo.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;

    @PostConstruct
    public void init() {
        System.out.println("✅ JWT Secret Key Loaded: " + secretKey);
    }
}

