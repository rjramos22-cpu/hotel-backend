package com.hotel.hotel_backend.controller;

import com.hotel.hotel_backend.DTO.ConfirmarPagoDTO;
import com.hotel.hotel_backend.DTO.PagoResponseDTO;
import com.hotel.hotel_backend.service.PagoService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pago")
@RequiredArgsConstructor
public class PagoController {
    private final PagoService pagoService;

    @PostMapping("/crear/{reservaId}")
    public ResponseEntity<String> crear (@PathVariable Long reservaId) throws StripeException {
        return ResponseEntity.ok(pagoService.crearPaymentIntent(reservaId));
    }

    @PostMapping("/confirmar")
    public ResponseEntity<PagoResponseDTO> confirmar(@RequestBody ConfirmarPagoDTO dto){
        return ResponseEntity.ok(pagoService.confirmarPago(dto.getPaymentIntentId()));
    }

    @PostMapping("/reembolso/{reservaId}")
    public ResponseEntity<PagoResponseDTO> reembolso(@PathVariable Long reservaId) throws StripeException {
        return ResponseEntity.ok(pagoService.reembolso(reservaId));
    }
}
