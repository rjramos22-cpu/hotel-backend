package com.hotel.hotel_backend.service;

import com.hotel.hotel_backend.DTO.CreateRoomDTO;
import com.hotel.hotel_backend.DTO.RoomResponseDTO;
import com.hotel.hotel_backend.DTO.UpdateRoomDTO;
import com.hotel.hotel_backend.Exception.ResourceNotFoundException;
import com.hotel.hotel_backend.entity.HotelPhoto;
import com.hotel.hotel_backend.entity.RoomEntity;
import com.hotel.hotel_backend.entity.TypeAmenity;
import com.hotel.hotel_backend.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final HabitacionRepository habitacionRepository;
    private final CloudinaryService cloudinaryService;


    public RoomResponseDTO crearRoom (CreateRoomDTO roomDTO){
        RoomEntity room = RoomEntity.builder()
                .name(roomDTO.getRoomName())
                .description(roomDTO.getDescription())
                .typeRoom(roomDTO.getTypeRoom())
                .price(roomDTO.getRoomPrice())
                .capacity(roomDTO.getCapacity())
                .available(true)
                .amenities(List.of(roomDTO.getAmenities().toArray(new TypeAmenity[0])))
                .build();

        RoomEntity roomSave = habitacionRepository.save(room);

        return convertirAResponseDTO(roomSave);
    }

    public RoomResponseDTO updateRoom (Long id ,UpdateRoomDTO roomDTO){

        RoomEntity entity = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));

        if (!roomDTO.getDescription().isEmpty()){
            entity.setDescription(roomDTO.getDescription());
        }
        if (roomDTO.getPrice() != null){
            entity.setPrice(roomDTO.getPrice());
        }
        if (roomDTO.getCapacity() != null){
            entity.setCapacity(roomDTO.getCapacity());
        }
        if (roomDTO.getTypeRoom() != null){
            entity.setTypeRoom(roomDTO.getTypeRoom());
        }
        if (roomDTO.getAvailable() != null){
            entity.setAvailable(roomDTO.getAvailable());
        }
        if (!roomDTO.getAmenities().isEmpty()){
            entity.setAmenities(roomDTO.getAmenities());
        }

        RoomEntity roomSave = habitacionRepository.save(entity);
        return convertirAResponseDTO(roomSave);
    }

    public RoomResponseDTO subirFoto (Long id, MultipartFile file) throws IOException {
        RoomEntity room = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));

        //subir la foto a cloudinary

        Map resultado = cloudinaryService.subirImagen(file, "hotel/rooms");

        HotelPhoto foto = new HotelPhoto(
                (String) resultado.get("secure_url"),
                (String) resultado.get("public_id")
        );

        room.getPhoto().add(foto);

        RoomEntity roomSave = habitacionRepository.save(room);

        return convertirAResponseDTO(roomSave);
    }

    public List<RoomResponseDTO> getAllRooms() {
        return habitacionRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    public RoomResponseDTO getRoomById(Long id) {
        RoomEntity room = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada"));
        return convertirAResponseDTO(room);
    }

    private RoomResponseDTO convertirAResponseDTO(RoomEntity room){

        List<String> photoUrls  = room.getPhoto().stream()
                .map(HotelPhoto::getUrl)
                .toList();
        return new RoomResponseDTO(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.getTypeRoom(),
                room.getPrice(),
                room.getCapacity(),
                room.isAvailable(),
                photoUrls,
                room.getAmenities()
        );
    }
}
