package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponseDTO {

    private Long idPago;
    private BigDecimal monto;
    private String moneda;
    private LocalDateTime createdAt;
    private EstadoPago estado;

}
