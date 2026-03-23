package com.hotel.hotel_backend.repository;


import com.hotel.hotel_backend.entity.RoomEntity;
import com.hotel.hotel_backend.entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findById(Long id);
    List<RoomEntity> findByAvailableTrue ();
    List<RoomEntity> findByTypeRoom (TypeRoom typeRoom);
    List<RoomEntity> findByCapacityGreaterThanEqual (Integer capacity);
}
