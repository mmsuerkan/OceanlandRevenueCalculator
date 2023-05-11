package com.example.OceanlandStatistics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OceanlandStatisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceanlandStatisticsApplication.class, args);
	}

}
