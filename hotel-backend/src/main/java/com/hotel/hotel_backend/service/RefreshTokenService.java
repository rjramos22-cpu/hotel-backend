package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.entity.RefreshTokenEntity;
import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity crearRefreshToken(UserEntity user) {
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setUser(user);

        return  refreshTokenRepository.save(refreshToken);

    }

    public RefreshTokenEntity validarRefreshToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh no encontrado"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token expiro");
        }
        return refreshToken;
    }

    public void eliminarRefreshToken(UserEntity user) {
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);
    }
}
