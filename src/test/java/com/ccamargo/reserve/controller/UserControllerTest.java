package com.ccamargo.reserve.controller;

import com.ccamargo.reserve.model.rol.RolEntity;
import com.ccamargo.reserve.model.user.UserDTO;
import com.ccamargo.reserve.model.user.UserDetailsRequest;
import com.ccamargo.reserve.model.user.UserEntity;
import com.ccamargo.reserve.security.service.AuthService;
import com.ccamargo.reserve.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Test
    void createUser_ShouldReturnNoContent() {
        UserDetailsRequest userDetails = new UserDetailsRequest();
        userDetails.setEmail("test@example.com");
        userDetails.setPassword("password123");
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDto);

        doNothing().when(userService).createUser(any(UserDTO.class));

        ResponseEntity<Void> response = userController.createUser(userDetails);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        UserDetailsRequest userDetails = new UserDetailsRequest();
        userDetails.setEmail("test@example.com");
        userDetails.setPassword("password123");
        UserDetails mockUser = new User("test@example.com", "password123", new ArrayList<>());
        String mockToken = "jwt-token";
        RolEntity rol = new RolEntity(2, "ROLE_USUARIO");
        UserEntity userEntity = new UserEntity(1, "usuario@example.com", "password123", rol);

        when(userService.login(userDetails.getEmail(), userDetails.getPassword())).thenReturn(mockUser);
        when(userService.findUserByEmail(userDetails.getEmail())).thenReturn(userEntity);
        when(authService.generarToken(userEntity)).thenReturn(mockToken);

        ResponseEntity<?> response = userController.login(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        UserDetailsRequest userDetails = new UserDetailsRequest();
        userDetails.setEmail("invalid@example.com");
        userDetails.setPassword("wrongpassword");

        when(userService.login(userDetails.getEmail(), userDetails.getPassword()))
                .thenThrow(new UsernameNotFoundException("Credenciales inválidas"));

        ResponseEntity<?> response = userController.login(userDetails);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody());
    }
}
