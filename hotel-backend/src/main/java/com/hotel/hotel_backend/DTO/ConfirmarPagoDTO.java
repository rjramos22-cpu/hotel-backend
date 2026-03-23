package com.hotel.hotel_backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmarPagoDTO {
    private String paymentIntentId;
}
