package com.encora.esteban.inventory.manager.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:8080")); // ✅ Allow frontend on port 8080
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // ✅ Allow necessary HTTP methods
        config.setAllowedHeaders(Arrays.asList("*")); // ✅ Allow all headers
        config.setAllowCredentials(true); // ✅ Allow credentials (if needed)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // ✅ Apply to all endpoints
        return new CorsFilter(source);
    }
}
