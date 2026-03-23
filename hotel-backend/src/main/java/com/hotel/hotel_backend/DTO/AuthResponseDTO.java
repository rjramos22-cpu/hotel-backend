package com.hotel.hotel_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String name;
    private String lastName;
    private String token;
    private String refreshToken;
    private String rol;
}
