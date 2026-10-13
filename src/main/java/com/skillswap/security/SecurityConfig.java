
package com.skillswap.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(
        HttpSecurity http,
        JwtService jwt,
        CorsConfigurationSource corsSource) throws Exception {

        http
            .csrf(c -> c.disable())
            .cors(cors -> cors.configurationSource(corsSource))
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**", "/error").permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(e ->
                e.authenticationEntryPoint(
                    new HttpStatusEntryPoint(
                        org.springframework.http.HttpStatus.UNAUTHORIZED)))
            .addFilterBefore(
                new JwtFilter(jwt),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   
@Bean
CorsConfigurationSource corsSource() {
    CorsConfiguration c = new CorsConfiguration();

    c.setAllowedOriginPatterns(List.of(
        "http://localhost:5173",
        "http://127.0.0.1:5173"
    ));

    c.setAllowedMethods(List.of(
        "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));

    c.setAllowedHeaders(List.of("*"));
    c.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", c);

    return source;
}

}
