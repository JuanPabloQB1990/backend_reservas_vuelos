package com.proyecto.reservaVuelos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Auth
                        .requestMatchers("/api/clientes/auth/**").permitAll()

                        // Búsqueda de vuelos pública
                        .requestMatchers(HttpMethod.GET, "/api/vuelos/buscar/**").permitAll()

                        // Seguridad vuelos
                        .requestMatchers(HttpMethod.PATCH, "/api/reservaciones/reservacion")
                        .hasAnyAuthority("EMPLEADO","ADMIN")

                        .requestMatchers(HttpMethod.PATCH,"/api/vuelos/vuelo/**", "/api/aerolineas", "/api/tipo_vuelos", "/api/admin/cliente/**")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/reservaciones/reservacion")
                        .hasAnyAuthority("EMPLEADO","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/reservaciones/reservacion")
                        .hasAnyAuthority("EMPLEADO","ADMIN", "CLIENTE")

                        .requestMatchers(HttpMethod.DELETE,"/api/vuelos/vuelo/**", "/api/aerolineas", "/api/tipo_vuelos")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/vuelos/vuelo/**", "/api/aerolineas", "/api/tipo_vuelos", "/api/reservaciones/reservacion")
                        .hasAnyAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/admin/cliente/**").hasAnyAuthority("ADMIN","EMPLEADO")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
