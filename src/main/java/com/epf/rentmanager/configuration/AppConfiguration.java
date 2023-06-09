package com.epf.rentmanager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan({ "com.epf.rentmanager.service", "com.epf.rentmanager.dao",
        "com.epf.rentmanager.persistence" }) // packages dans lesquels chercher les beans

public class AppConfiguration {
}

