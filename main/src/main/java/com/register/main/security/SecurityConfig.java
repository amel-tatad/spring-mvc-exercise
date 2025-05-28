package com.register.main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT admin_id, pass, active FROM admins WHERE admin_id=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT admin_id, roles FROM roles WHERE admin_id=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(configurer ->
                configurer
                    .requestMatchers("/list").hasAnyRole("ADMIN", "ASSTADMIN")
                    .requestMatchers("/update").hasAnyRole("ADMIN", "ASSTADMIN")
                    .requestMatchers("/delete").hasRole("ADMIN")
                    .requestMatchers("/register", "/submit", "/register-success", "/css/**").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticateTheUser")
                    .defaultSuccessUrl("/list", true)
                    .permitAll()
            )
            .logout(logout -> logout.permitAll())
            .exceptionHandling(configurer ->
                configurer.accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}