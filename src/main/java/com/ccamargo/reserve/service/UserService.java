package com.ccamargo.reserve.service;

import com.ccamargo.reserve.exception.EmailExistsException;
import com.ccamargo.reserve.exception.LoginFailedException;
import com.ccamargo.reserve.model.user.UserDTO;
import com.ccamargo.reserve.model.user.UserEntity;
import com.ccamargo.reserve.repository.RolRepository;
import com.ccamargo.reserve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void createUser(UserDTO user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EmailExistsException("Ya existe una cuenta con el correo electrónico proporcionado");

        if (user.getPassword().equals(user.getEmail()))
            throw new IllegalArgumentException("La contraseña no puede ser igual al correo electrónico");
        
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setRol(rolRepository.findByAuthority("ROLE_USUARIO"));

        userRepository.save(userEntity);
    }

    public UserDetails login(String email, String password) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        if (!bCryptPasswordEncoder.matches(password, userEntity.get().getEncryptedPassword())) {
            throw new LoginFailedException("Credenciales incorrectas");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (userEntity.get().getRol() != null) {
            authorities.add(new SimpleGrantedAuthority(userEntity.get().getRol().getAuthority()));
        } else {
            throw new UsernameNotFoundException("Error en el Login: usuario '" + email + "' no tiene roles asignados!");
        }
        return new User(userEntity.get().getEmail(), userEntity.get().getEncryptedPassword(), authorities);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
}
