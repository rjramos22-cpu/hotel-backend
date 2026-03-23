package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.TypeAmenity;
import com.hotel.hotel_backend.entity.TypeRoom;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDTO {

    @NotBlank (message = "El nombre es obligatorio")
    private String roomName;

    @NotBlank (message = "La descripcion es obligatoria")
    @Size (max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String description;

    @NotNull(message = "El tipo de habitacion es obligatoria")
    private TypeRoom typeRoom;

    @NotNull (message = "El precio de la habitacion es obligatoria")
    @Positive (message = "El precio debe ser mayor a 0")
    private BigDecimal roomPrice;

    @NotNull (message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad minima es 1 persona")
    @Max (value = 10, message = "la capacidad maxima es de 10 personas")
    private Integer capacity;


    private List<TypeAmenity> amenities = new ArrayList<>();
}
