package com.ccamargo.reserve.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ccamargo.reserve.exception.EmailExistsException;
import com.ccamargo.reserve.exception.LoginFailedException;
import com.ccamargo.reserve.model.rol.RolEntity;
import com.ccamargo.reserve.model.user.UserDTO;
import com.ccamargo.reserve.model.user.UserEntity;
import com.ccamargo.reserve.repository.RolRepository;
import com.ccamargo.reserve.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private UserEntity userEntity;
    private RolEntity roleEntity;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("securePassword");

        userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setEncryptedPassword("encryptedPassword");

        roleEntity = new RolEntity();
        roleEntity.setAuthority("ROLE_USUARIO");
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(userEntity));

        Exception exception = assertThrows(EmailExistsException.class, () -> userService.createUser(userDTO));

        assertEquals("Ya existe una cuenta con el correo electrónico proporcionado", exception.getMessage());
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordEqualsEmail() {
        userDTO.setPassword(userDTO.getEmail());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));

        assertEquals("La contraseña no puede ser igual al correo electrónico", exception.getMessage());
    }

    @Test
    void createUser_ShouldSaveUser_WhenValid() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("hashedPassword");
        when(rolRepository.findByAuthority("ROLE_USUARIO")).thenReturn(roleEntity);

        userService.createUser(userDTO);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(userDTO.getEmail(), userDTO.getPassword()));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.matches(userDTO.getPassword(), userEntity.getEncryptedPassword())).thenReturn(false);

        assertThrows(LoginFailedException.class, () -> userService.login(userDTO.getEmail(), userDTO.getPassword()));
    }

    @Test
    void login_ShouldReturnUserDetails_WhenValidCredentials() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.matches(userDTO.getPassword(), userEntity.getEncryptedPassword())).thenReturn(true);
        userEntity.setRol(roleEntity);

        User userDetails = (User) userService.login(userDTO.getEmail(), userDTO.getPassword());

        assertNotNull(userDetails);
        assertEquals(userDTO.getEmail(), userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USUARIO")));
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(userEntity));

        UserEntity foundUser = userService.findUserByEmail(userDTO.getEmail());

        assertNotNull(foundUser);
        assertEquals(userDTO.getEmail(), foundUser.getEmail());
    }

    @Test
    void findUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByEmail(userDTO.getEmail()));
    }
}
