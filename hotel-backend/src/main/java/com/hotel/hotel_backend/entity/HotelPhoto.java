package com.hotel.hotel_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
    public class HotelPhoto {
        @Column(name = "url", length = 500)
        private String url;

        @Column(name = "public_id", length = 255)
        private String publicId;
    }


