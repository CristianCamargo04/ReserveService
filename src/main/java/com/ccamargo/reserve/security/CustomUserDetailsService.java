package com.ccamargo.reserve.security;

import com.ccamargo.reserve.model.user.UserEntity;
import com.ccamargo.reserve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRol().getAuthority());
        return User
                .withUsername(user.getEmail())
                .password(user.getEncryptedPassword())
                .authorities(Collections.singletonList(authority))
                .build();
    }
}
