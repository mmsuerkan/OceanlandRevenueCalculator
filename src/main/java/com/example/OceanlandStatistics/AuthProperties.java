package com.example.OceanlandStatistics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
public class AuthProperties {
    private String chainid;
    private String email;
    private String password;
    private String url;

    // Getters and Setters
}
