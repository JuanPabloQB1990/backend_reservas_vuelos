package com.proyecto.reservaVuelos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest -> authRequest
                        .requestMatchers("/api/clientes/auth/**", "api/vuelos/busqueda/criterio").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/vuelos/vuelo/**", "/api/admin/cliente/**").hasAnyAuthority("EMPLEADO","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/vuelos/vuelo").hasAnyAuthority("EMPLEADO","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/vuelos/vuelo/**", "/api/admin/cliente/**").hasAnyAuthority("EMPLEADO","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/cliente/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/admin/cliente/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/vuelos/vuelo/**").hasAnyAuthority("EMPLEADO","ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reservaciones/reservacion/**", "/api/reservaciones/cliente/**", "/api/vuelos/busqueda/**", "api/aerolineas", "api/tipo_vuelos").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.POST,"/api/reservaciones/reservacion").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE,"/api/reservaciones/reservacion/**").hasAuthority("CLIENTE")
                        .anyRequest().authenticated())
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("api-docs/**", "/swagger-ui/**", "/swagger-resources/**");
    }

    private RequestMatcher publicEndpoints(){
        return new OrRequestMatcher(new AntPathRequestMatcher("/api/clientes/auth/**"),
                new AntPathRequestMatcher("/api/vuelos/**"));
    }

}
