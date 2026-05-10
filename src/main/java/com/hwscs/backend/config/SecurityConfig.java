package com.hwscs.backend.config;

import com.hwscs.backend.security.JwtAuthFilter;
import com.hwscs.backend.security.JwtAuthenticationEntryPoint;
import com.hwscs.backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Nurse endpoints
                        .requestMatchers("/api/nurses/me/**").hasRole("NURSE")
                        .requestMatchers(HttpMethod.POST, "/api/shift-requests/create").hasRole("NURSE")
                        .requestMatchers("/api/shift-requests/peer-response").hasRole("NURSE")
                        .requestMatchers(HttpMethod.PUT, "/api/shift-requests/*/cancel").hasRole("NURSE")

                        // Nursing Incharge endpoints
                        .requestMatchers("/api/shift-requests/incharge-review").hasRole("NURSING_INCHARGE")
                        .requestMatchers("/api/incharge/**").hasRole("NURSING_INCHARGE")

                        // Duty Officer endpoints
                        .requestMatchers("/api/duty-officer/**").hasRole("DUTY_OFFICER")

                        // Incharge and Duty Officer can both assign shifts and view nurses
                        .requestMatchers("/api/nurses/**").hasAnyRole("NURSING_INCHARGE", "DUTY_OFFICER")
                        .requestMatchers("/api/shifts/**").hasAnyRole("NURSING_INCHARGE", "DUTY_OFFICER")

                        // Shift request listing — multiple roles can view
                        .requestMatchers("/api/shift-requests/**")
                        .hasAnyRole("NURSE", "NURSING_INCHARGE", "DUTY_OFFICER")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
