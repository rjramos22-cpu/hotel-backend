package com.hotel.hotel_backend.repository;

import com.hotel.hotel_backend.entity.EstadoReserva;
import com.hotel.hotel_backend.entity.ReservaEntity;
import com.hotel.hotel_backend.entity.RoomEntity;
import com.hotel.hotel_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long>{
    List<ReservaEntity> findByUser (UserEntity user);

    List<ReservaEntity> findByRoom(RoomEntity room);

    List<ReservaEntity> findByEstado(EstadoReserva estado);

    @Query("SELECT r FROM ReservaEntity r WHERE r.room.id = :roomId " +
     "AND r.estado != 'CANCELADA' " +
     "AND r.checkIn < :checkOut AND r.checkOut > :checkIn")
    List<ReservaEntity> findReservasEnFechas(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
