package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.EstadoReserva;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRerservaDTO {

    @NotNull (message = "El estado es obligatorio")
    private EstadoReserva estado;
}
