package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.entity.EstadoReserva;
import com.hotel.hotel_backend.entity.ReservaEntity;
import com.hotel.hotel_backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final ReservaRepository reservaRepository;

    @Scheduled(fixedRate = 900000)
    public void cancelarReserva (){
        List<ReservaEntity> reserva = reservaRepository.findByEstado(EstadoReserva.PENDIENTE);

        for(ReservaEntity entity : reserva){
            if (entity.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(15))){
                entity.setEstado(EstadoReserva.CANCELADA);
                reservaRepository.save(entity);
            }
        }
    }
}
