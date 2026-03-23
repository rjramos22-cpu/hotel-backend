package com.hotel.hotel_backend.DTO;


import com.hotel.hotel_backend.entity.EstadoReserva;
import com.hotel.hotel_backend.entity.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDTO {

    //datos de la reserva
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalNoche;
    private BigDecimal precioTotal;
    private LocalDateTime createdAt;
    private EstadoReserva estado;

    //datos de la habitacion
    private Long idRoom;
    private String nameRoom;
    private TypeRoom typeRoom;
    private BigDecimal priceRoom;


    //datos del usuario
    private String username;
    private String userEmail;
}
