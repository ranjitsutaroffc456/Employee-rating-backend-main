package com.EmployeeRating.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/register","api/health", "/api/employees/export", "/auth/**", "/excel/**", "/api/v1/tasks/**").permitAll()
                .anyRequest().permitAll() // Allow all requests for now
            .and()
            .httpBasic().disable(); // Disable basic auth
        return http.build();
    }
}
