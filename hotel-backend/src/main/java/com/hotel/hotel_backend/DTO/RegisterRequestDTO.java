package com.hotel.hotel_backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @Email(message = "El correo no tiene un formato valido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-]).+$",
            message = "La contraseña debe tener al menos una mayuscula, una minuscula, un numero y un caracter especial"
    )
    private String password;


    @NotBlank (message = "El telofono es obligatorio")
    @Pattern(
            regexp = "^\\+52\\d{10}$",
            message = "El telefono debe de estar en formato internacional, por ejemplo +5266666666"
    )
    private String numberPhone;
}
