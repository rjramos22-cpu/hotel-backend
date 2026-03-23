package com.hotel.hotel_backend.repository;

import com.hotel.hotel_backend.entity.PagoEntity;
import com.hotel.hotel_backend.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Long> {
         Optional<PagoEntity> findByReserva(ReservaEntity reserva);
         Optional<PagoEntity> findByStripePaymentId(String stripePaymentId);
}
