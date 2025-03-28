package com.ccamargo.reserve.security.service;

import com.ccamargo.reserve.model.user.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

public interface AuthService {

	String create(Authentication auth) throws IOException;
	boolean validate(String token);
	Claims getClaims(String token);
	String getUsername(String token);
	Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
	String resolve(String token);
	Integer getId(String token);
	String generarToken(UserEntity usuario);
}
