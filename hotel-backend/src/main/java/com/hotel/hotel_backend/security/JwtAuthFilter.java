package com.hotel.hotel_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        String autherHeader = request.getHeader("Authorization"); // extraemos las credenciales del usuario

        //verificamos si el token del usuario esta verificado con el rango correcto
        if (autherHeader == null || !autherHeader.startsWith("Bearer ")){
            //primero preguntamos si la peticion trajo el header "Authorization
            //despues pregunata si el header no empieza por Baerer
             filterChain.doFilter(request,response); //si se cumple las condiciones quiere decir que es un usuario no verificado
            // y lo mandamos al siguiente capa de seguridad

            return;

        }

        String token = autherHeader.substring(7);
        String email = jwtService.extracEmail(token);

        if (jwtService.isTokenValid(token,email)){
            UserDetails userDetails = userDetailService.loadUserByUsername(email); // aqui obtenemos los datos del usuario desde la DB

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );// aqui creamos un objeto de autenticacion, esto almacena datos del usuario y permisos

            SecurityContextHolder.getContext().setAuthentication(authToken);// aqui guardamos los datos para despues usar getUser(), para poder obtner el usuario
        }
        filterChain.doFilter(request, response);

    }
}
