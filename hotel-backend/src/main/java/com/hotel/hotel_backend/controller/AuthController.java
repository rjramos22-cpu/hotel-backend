package com.hotel.hotel_backend.controller;

import com.hotel.hotel_backend.DTO.AuthResponseDTO;
import com.hotel.hotel_backend.DTO.LoginRequestDTO;
import com.hotel.hotel_backend.DTO.RefreshTokenRequestDTO;
import com.hotel.hotel_backend.DTO.RegisterRequestDTO;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.UserRepository;
import com.hotel.hotel_backend.service.AuthService;
import com.hotel.hotel_backend.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO>login (@RequestBody @Valid LoginRequestDTO RequestDTO){
        return ResponseEntity.ok(authService.login(RequestDTO));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        refreshTokenService.eliminarRefreshToken(user);
        return ResponseEntity.ok("Sesion cerrada correctamente");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO>register(@RequestBody @Valid RegisterRequestDTO requestDTO){
        return  ResponseEntity.ok(authService.register(requestDTO));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO>refresg(@RequestBody RefreshTokenRequestDTO tokenRequestDTO){
        return ResponseEntity.ok(authService.refresh(tokenRequestDTO.getRefreshToken()));
    }

}
