package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.TypeRoom;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservaDTO {

    @NotNull(message = "La fecha de check-in es obligatoria")
    @Future (message = "La fecha del check-in debe ser una futura")
    private LocalDate checkIn;

    @NotNull(message = "La fecha de check-out es obligatoria")
    @Future (message = "La fecha del check-out debe ser una futura")
    private LocalDate checkOut;

    @NotNull(message = "La habitacion es obligatoria")
    private Long roomId;
}
