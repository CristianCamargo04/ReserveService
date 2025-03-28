package com.ccamargo.reserve.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsRequest {

    @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com")
    @NotEmpty(message = "Email es obligatorio")
    @Email(message = "Email debe ser válido")
    private String email;

    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres, máximo 30)", example = "Password123*")
    @NotEmpty(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 30, message = "La contraseña debe tener entre 8 y 30 caracteres")
    private String password;
}
