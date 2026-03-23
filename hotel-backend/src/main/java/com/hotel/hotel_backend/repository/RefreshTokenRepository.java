package com.hotel.hotel_backend.repository;

import com.hotel.hotel_backend.entity.RefreshTokenEntity;
import com.hotel.hotel_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUser(UserEntity user);

    Optional<RefreshTokenEntity> findByUser(UserEntity user);
}
