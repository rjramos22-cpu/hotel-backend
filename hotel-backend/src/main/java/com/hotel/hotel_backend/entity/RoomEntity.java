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
@Table (name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String description;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private TypeRoom typeRoom;


    @Column (nullable = false)
    private BigDecimal price;

    @Column ( nullable = false)
    private Integer capacity;

    @Column (nullable = false)
    private boolean available = true;

    @Builder.Default
    @ElementCollection
    @CollectionTable (name = "roomPhotos", joinColumns = @JoinColumn(name = "room_id"))
    private List<HotelPhoto> photo = new ArrayList<>();


    @Builder.Default
    @ElementCollection
    @Enumerated (EnumType.STRING)
    @CollectionTable (name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    @Column (name = "amenity", nullable = false)
    private List<TypeAmenity> amenities = new ArrayList<>();


    private LocalDateTime createdAt;


    @PrePersist
    public void prePersistRoom() {
        this.createdAt = LocalDateTime.now();
    }

}
