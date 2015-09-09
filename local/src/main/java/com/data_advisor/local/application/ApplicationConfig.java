package com.data_advisor.local.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for the application.
 * Uses {@link ComponentScan} to scan for components.
 */
@Configuration
@ComponentScan("com.data_advisor.local")
public class ApplicationConfig {
}
