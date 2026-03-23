package com.hotel.hotel_backend.controller;


import com.hotel.hotel_backend.DTO.CreateReservaDTO;
import com.hotel.hotel_backend.DTO.ReservaResponseDTO;
import com.hotel.hotel_backend.DTO.UpdateRerservaDTO;
import com.hotel.hotel_backend.repository.ReservaRepository;
import com.hotel.hotel_backend.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;


    @PostMapping("/create")
    public ResponseEntity<ReservaResponseDTO> crear(@RequestBody @Valid CreateReservaDTO reservaDTO){
        return ResponseEntity.ok(reservaService.crear(reservaDTO));
    }
     @PutMapping("/update/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar (@PathVariable Long id,
                                                          @RequestBody @Valid UpdateRerservaDTO reservaDTO){
        return ResponseEntity.ok(reservaService.updateReserva(id, reservaDTO));
    }

    @GetMapping("/mis-reservas")
    public List<ReservaResponseDTO> misReservas(){
        return reservaService.misReservas();
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<ReservaResponseDTO> cancelar(@PathVariable Long id){
        return ResponseEntity.ok(reservaService.cancelar(id));
    }

    @GetMapping("/all")
    public List<ReservaResponseDTO> allReservas(){
        return reservaService.allReservas();
    }

}
