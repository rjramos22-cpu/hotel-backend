package com.hotel.hotel_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private  LocalDate checkOut;

    @Column(nullable = false)
    private BigDecimal totalNoche;

    @Column(nullable = false)
    private BigDecimal precioTotal;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @ManyToOne
    @JoinColumn (name = "room_id", nullable = false)
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @PrePersist
    public void prePersistReserva (){
        this.createdAt = LocalDateTime.now();
    }

}
