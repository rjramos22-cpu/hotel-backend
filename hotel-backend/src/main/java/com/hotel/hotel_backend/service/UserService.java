package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.DTO.UpdateUserDTO;
import com.hotel.hotel_backend.DTO.UserResponseDTO;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.HotelPhoto;
import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;


    public UserResponseDTO getPerfil(){
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return convertirAResponseDTO(user);
    }

    public UserResponseDTO actualizarPerfil (UpdateUserDTO userDTO){
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setNumberPhone(userDTO.getNumberPhone());

        UserEntity userSave = userRepository.save(user);
        return convertirAResponseDTO(userSave);
    }

    public UserResponseDTO subirFotoPerfil(MultipartFile file) throws IOException {
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getProfilePicturePublicId() != null){
            cloudinaryService.eliminarImagen(user.getProfilePicturePublicId());
        }

        Map resultado = cloudinaryService.subirImagen(file, "hotel/fotoUsuario");

        user.setProfilePictureUrl((String) resultado.get("secure_url"));
        user.setProfilePicturePublicId((String) resultado.get("public_id"));

        UserEntity userSave = userRepository.save(user);
        return convertirAResponseDTO(userSave);
    }



    private UserResponseDTO convertirAResponseDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getNumberPhone(),
                user.getProfilePictureUrl()
        );
    }
    private String getUser(){
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }
}
