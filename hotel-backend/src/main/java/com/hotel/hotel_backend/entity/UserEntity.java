package com.hotel.hotel_backend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String lastName;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false, unique = true)
    private String numberPhone;

    @Column (nullable = false)
    private String password;

    @Column (length = 500)
    private String profilePictureUrl;

    @Column (length = 255)
    private  String profilePicturePublicId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable (name = "usersRol", joinColumns = @JoinColumn (name = "user_id"))
    @Column (name = "rol")
    private List<String> rol = new ArrayList<>();


    @Column (name = "createdAt", nullable = false)
    private LocalDateTime createdAt;


    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReservaEntity> reservas = new ArrayList<>();


    @PrePersist
    public void prePersistUser() {
        this.createdAt = LocalDateTime.now();
    }

}
