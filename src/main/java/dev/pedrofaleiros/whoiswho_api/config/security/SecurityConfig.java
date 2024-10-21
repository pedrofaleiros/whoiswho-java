package dev.pedrofaleiros.whoiswho_api.config.security;

import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import dev.pedrofaleiros.whoiswho_api.config.security.filters.ExceptionHandlerFilter;
import dev.pedrofaleiros.whoiswho_api.config.security.filters.SecurityFilter;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    SecurityFilter securityFilter;
    ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .headers(h -> h.frameOptions(f -> f.disable()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/auth/guest").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/h2/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();

        config.setAllowCredentials(true);
        //TODO: cors 
        config.setAllowedOriginPatterns(Arrays.asList(
            "*"
        ));
        // config.addAllowedOrigin("http://localhost:4200");

        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION, 
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT));

        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(), 
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(), 
                HttpMethod.DELETE.name()));
                
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);

        var bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(-102);
        return bean;
    }
}
