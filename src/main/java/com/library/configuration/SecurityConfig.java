package com.library.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.okta.spring.boot.oauth.Okta;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        http.cors(cors-> Customizer.withDefaults());
        http.csrf(csrf->csrf.disable());
        http.authorizeHttpRequests(config->config.requestMatchers("/api/books/secure/**").authenticated());
        http.oauth2ResourceServer();
        http.jwt();
        http.setSharedObject(ContentNegotiationStrategy.class,new HeaderContentNegotiationStrategy());
        Okta.configureResourceServer401ResponseBody(http);    

        return http.build();
    }
}
