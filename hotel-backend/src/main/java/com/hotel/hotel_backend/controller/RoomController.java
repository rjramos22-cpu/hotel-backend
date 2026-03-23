package com.hotel.hotel_backend.controller;

import com.hotel.hotel_backend.DTO.CreateRoomDTO;
import com.hotel.hotel_backend.DTO.RoomResponseDTO;
import com.hotel.hotel_backend.DTO.UpdateRoomDTO;
import com.hotel.hotel_backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<RoomResponseDTO> crear(@RequestBody @Valid CreateRoomDTO roomDTO){
        return ResponseEntity.ok(roomService.crearRoom(roomDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RoomResponseDTO> actualizar( @PathVariable Long id,
            @RequestBody @Valid  UpdateRoomDTO roomDTO){
        return ResponseEntity.ok(roomService.updateRoom(id,roomDTO));
    }

    @PostMapping("/{id}/fotos")
    public ResponseEntity<RoomResponseDTO> subirFoto(
            @PathVariable Long id,
            @RequestParam ("foto")MultipartFile file
            ) throws IOException {
        return ResponseEntity.ok(roomService.subirFoto(id, file));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomResponseDTO>> getAll() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

}
