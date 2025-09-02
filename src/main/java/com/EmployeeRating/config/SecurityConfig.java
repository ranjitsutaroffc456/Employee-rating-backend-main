package com.EmployeeRating.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/register",
                        "api/health",
                        "/api/employees/export",
                        "/auth/**", "/excel/**",
                        "/api/v1/tasks/**")
                .permitAll()
                .anyRequest().permitAll() // Allow all requests for now
            .and()
            .httpBasic().disable(); // Disable basic auth
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
//      config.addAllowedOriginPattern("https://*.vercel.app");
//      config.addAllowedOriginPattern("http://localhost:*");
        config.setAllowedOrigins(List.of("https://employee-task-summary.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
