package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.DTO.CreateReservaDTO;
import com.hotel.hotel_backend.DTO.ReservaResponseDTO;
import com.hotel.hotel_backend.DTO.UpdateRerservaDTO;
import com.hotel.hotel_backend.Exception.HabitacionNoDisponibleException;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.EstadoReserva;
import com.hotel.hotel_backend.entity.ReservaEntity;
import com.hotel.hotel_backend.entity.RoomEntity;
import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.HabitacionRepository;
import com.hotel.hotel_backend.repository.ReservaRepository;
import com.hotel.hotel_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UserRepository userRepository;
    private final HabitacionRepository habitacionRepository;


    public ReservaResponseDTO crear (CreateReservaDTO reservaDTO){
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("usuario no encontrado"));

        RoomEntity habitacion = habitacionRepository.findById(reservaDTO.getRoomId())
                 .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada con el id: " + reservaDTO.getRoomId()));

        if (!habitacion.isAvailable()){
            throw new HabitacionNoDisponibleException("Esta habitacion no esta disponible");
        }
        List<ReservaEntity> reservaEntities = reservaRepository.findReservasEnFechas(
                reservaDTO.getRoomId(),reservaDTO.getCheckIn(),reservaDTO.getCheckOut()
        );

        if (!reservaEntities.isEmpty()){
            throw new HabitacionNoDisponibleException("La habitacion no esta disponible en esas fechas");
        }

        long totalNoche = ChronoUnit.DAYS.between(reservaDTO.getCheckIn(), reservaDTO.getCheckOut());


        ReservaEntity reserva = ReservaEntity.builder()
                .checkIn(reservaDTO.getCheckIn())
                .checkOut(reservaDTO.getCheckOut())
                .totalNoche(BigDecimal.valueOf(totalNoche))
                .precioTotal(habitacion.getPrice().multiply(BigDecimal.valueOf(totalNoche)))
                .estado(EstadoReserva.PENDIENTE)
                .room(habitacion)
                .user(user)
                .build();
        ReservaEntity reservaSave = reservaRepository.save(reserva);

        return convertirAResponseDTO(reservaSave);

    }

    //este metodo solo lo usara el admin para cambiar el estado de la reserva
    public ReservaResponseDTO updateReserva (Long id, UpdateRerservaDTO updateRerservaDTO){
            ReservaEntity reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

            reserva.setEstado(updateRerservaDTO.getEstado());


            ReservaEntity updateReserva = reservaRepository.save(reserva);

            return convertirAResponseDTO(updateReserva);
    }

    public List<ReservaResponseDTO> misReservas() {
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return reservaRepository.findByUser(user)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    public ReservaResponseDTO cancelar (Long id){
        UserEntity user = userRepository.findByEmail(getUser())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservar no encontrada"));

        if (!reserva.getUser().getId().equals(user.getId())){
            throw new ResourceNotFoundException("No tienes perimos para cancelar esta reserva");
        }
        reserva.setEstado(EstadoReserva.CANCELADA);

        ReservaEntity reservaCancelada = reservaRepository.save(reserva);

        return convertirAResponseDTO(reservaCancelada);

    }

    public List<ReservaResponseDTO> allReservas(){
        return reservaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }



    public ReservaResponseDTO convertirAResponseDTO (ReservaEntity entity){
            return new ReservaResponseDTO(
                   entity.getId(),
                   entity.getCheckIn(),
                   entity.getCheckOut(),
                   entity.getTotalNoche(),
                   entity.getPrecioTotal(),
                   entity.getCreatedAt(),
                   entity.getEstado(),
                   entity.getRoom().getId(),
                   entity.getRoom().getName(),
                   entity.getRoom().getTypeRoom(),
                   entity.getRoom().getPrice(),
                   entity.getUser().getName(),
                   entity.getUser().getEmail()
            );
    }
    public String getUser(){
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }

}
