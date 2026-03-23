package com.hotel.hotel_backend.DTO;

import com.hotel.hotel_backend.entity.TypeAmenity;
import com.hotel.hotel_backend.entity.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {

    private Long id;
    private String name;
    private String description;
    private TypeRoom typeRoom;
    private BigDecimal price;
    private Integer capacity;
    private boolean available;
    private List<String> photoUrls;
    private List<TypeAmenity> amenities;

}
