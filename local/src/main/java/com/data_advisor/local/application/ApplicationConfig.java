package com.data_advisor.local.application;

import com.data_advisor.infrastructure.InfrastructureConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration class for the application.
 * Uses {@link ComponentScan} to scan for components.
 */
@Configuration
@ComponentScan("com.data_advisor.local")
@Import({InfrastructureConfig.class})
public class ApplicationConfig {
}
