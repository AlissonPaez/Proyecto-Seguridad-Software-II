package edu.uptc.software.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Sigue deshabilitado
            .cors(cors -> cors.disable()) // Añadimos esto para evitar bloqueos de navegador
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 🔓 CAMBIO CLAVE: Permite TODO por ahora para la prueba
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}