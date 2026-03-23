package com.hotel.hotel_backend.controller;


import com.hotel.hotel_backend.DTO.UpdateUserDTO;
import com.hotel.hotel_backend.DTO.UserResponseDTO;
import com.hotel.hotel_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/perfil")
    public ResponseEntity<UserResponseDTO> perfil(){
        return ResponseEntity.ok(userService.getPerfil());
    }

    @PutMapping("/perfil")
    public ResponseEntity<UserResponseDTO> updatePerfil(@RequestBody UpdateUserDTO userDTO){
        return ResponseEntity.ok(userService.actualizarPerfil(userDTO));
    }

    @PostMapping("/perfil/foto")
    public ResponseEntity<UserResponseDTO> updateFoto(@RequestParam ("foto") MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.subirFotoPerfil(file));
    }
}
