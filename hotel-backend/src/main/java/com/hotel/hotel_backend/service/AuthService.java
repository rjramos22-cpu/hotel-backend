package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.DTO.AuthResponseDTO;
import com.hotel.hotel_backend.DTO.LoginRequestDTO;
import com.hotel.hotel_backend.DTO.RegisterRequestDTO;
import com.hotel.hotel_backend.Exception.EmailAlreadyExistException;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.RefreshTokenEntity;
import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.RefreshTokenRepository;
import com.hotel.hotel_backend.repository.UserRepository;
import com.hotel.hotel_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final RefreshTokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDTO register (RegisterRequestDTO requestDTO){
        if (userRepository.existsByEmail(requestDTO.getEmail())){
            throw new EmailAlreadyExistException("Este correo ya existe");
        }

        UserEntity user = UserEntity.builder()
                .name(requestDTO.getName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .numberPhone(requestDTO.getNumberPhone())
                .rol(List.of("ROLE_USER"))
                .build();

        UserEntity userSave = userRepository.save(user);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenEntity.setUser(userSave);
        refreshTokenRepository.save(refreshTokenEntity);


        return new AuthResponseDTO(
                userSave.getName(),
                userSave.getLastName(),
                jwtService.generateToken(userSave.getEmail()),
                refreshTokenEntity.getToken(),
                userSave.getRol().getFirst()
        );
    }

    public AuthResponseDTO login (LoginRequestDTO requestDTO){
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                requestDTO.getEmail(),
                requestDTO.getPassword())
        );

        UserEntity user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return new AuthResponseDTO(
                user.getName(),
                user.getLastName(),
                jwtService.generateToken(user.getEmail()),
                tokenService.crearRefreshToken(user).getToken(),
                user.getRol().getFirst()
        );
    }

    public AuthResponseDTO refresh (String refreshToken){
        RefreshTokenEntity token = tokenService.validarRefreshToken(refreshToken);

        UserEntity user = token.getUser();

        return new AuthResponseDTO(
                user.getName(),
                user.getLastName(),
                jwtService.generateToken(user.getEmail()),
                refreshToken,
                user.getRol().getFirst()
        );
    }
}
