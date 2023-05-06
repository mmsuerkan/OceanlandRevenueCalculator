package com.example.OceanlandStatistics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "material")
@Getter
@Setter
public class PriceProperties {

    private double oland;
    private double water;
    private double food;
    private double wood;
    private double metal;

    // Getters and Setters
}
