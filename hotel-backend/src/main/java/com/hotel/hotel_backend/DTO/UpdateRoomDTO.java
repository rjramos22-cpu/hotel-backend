package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.TypeAmenity;
import com.hotel.hotel_backend.entity.TypeRoom;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomDTO {
    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
    private String description;

    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @Min(value = 1, message = "La capacidad minima es 1 persona")
    @Max(value = 10, message = "La capacidad maxima es 10 personas")
    private Integer capacity;

    private TypeRoom typeRoom;
    private Boolean available;
    private List<TypeAmenity> amenities;
}
