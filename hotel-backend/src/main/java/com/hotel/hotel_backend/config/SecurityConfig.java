/*
  Esta es la clase de configuracion principal de Spring Security, aqui defineremos
  los endpoints que seran publicos y cuales requieren autenticacion
 */

package com.hotel.hotel_backend.config;

import com.hotel.hotel_backend.security.JwtAuthFilter;
import com.hotel.hotel_backend.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailsService; // se inyecta esta clase para poder buscar a los usuarios para poder autenticarlos
    private final JwtAuthFilter jwtAuthFilter; // se inyecta esta clase para verificar que usarios teiene permite que endponits

    //con @bean se le dice a spring que este metodo produce un objeto que el debe administrar e inyectar donde sea
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();//este es un algoritmo para encriptar las contrasenas
    }



    /*lo que hace "DaoAuthenticationProvider" es la implementacion de Spring que autentica
      usuarios desde la DB, le decimos como buscarlo pasando el "userDetailsService" y
      con "passwordEncoder" como verificar la contrasena
     */
    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) //aqui desactiva la proteccion CSRF ya que con JWT no se necesita
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/room/**").permitAll()
                        .requestMatchers("/api/room/create", "/api/room/update/**","/api/room/{id}/fotos").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//aqui le decimos a Spring que no aguarde sesiones en el servidor
                )
                .authenticationProvider(authenticationProvider())// aqui se registra el provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);//aqui se registra el JwtAuthFilter para que se ejecuten antes
        // del filtro de autentitcacion por defecto de Spring
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173",
                "https://hotel-frontend-6t15bc2yr-ricardos-projects-3b37b144.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
