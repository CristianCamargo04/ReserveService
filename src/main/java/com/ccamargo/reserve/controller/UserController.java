package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.auth.AuthResponse;
import com.ccamargo.reserve.model.user.UserDTO;
import com.ccamargo.reserve.model.user.UserDetailsRequest;
import com.ccamargo.reserve.security.service.AuthService;
import com.ccamargo.reserve.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con el correo electrónico proporcionado", content = @Content)
    })
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDetailsRequest userDetails) {
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDto);
        userService.createUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario y genera un token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    public ResponseEntity<?> login(@RequestBody @Valid UserDetailsRequest userDetails) {
        try {
            UserDetails user = userService.login(userDetails.getEmail(), userDetails.getPassword());
            String token = authService.generarToken(userService.findUserByEmail(userDetails.getEmail()));
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
