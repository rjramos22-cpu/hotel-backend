package com.hotel.hotel_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stripe_payment_id", nullable = false, unique = true)
    private String stripePaymentId;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPagos = EstadoPago.PENDIENTE;

    @OneToOne
    @JoinColumn (name = "reserva_id", nullable = false)
    private ReservaEntity reserva;

    @PrePersist
    public void prePersistPago (){
        this.createdAt = LocalDateTime.now();
    }

}
