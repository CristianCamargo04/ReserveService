package com.ccamargo.reserve.security.service;

import com.ccamargo.reserve.model.user.UserEntity;
import com.ccamargo.reserve.repository.UserRepository;
import com.ccamargo.reserve.security.JwtUtils;
import com.ccamargo.reserve.security.SecurityConstants;
import com.ccamargo.reserve.security.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;

	@Override
	public String create(Authentication auth) throws IOException {

		String username = ((User) auth.getPrincipal()).getUsername();

		int idUser = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username))
				.getId();

		Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		claims.put("idUser", idUser);
		String token = Jwts.builder().setClaims(claims).setSubject(username)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET.getBytes()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE)).compact();

		return token;
	}

	@Override
	public boolean validate(String token) {
		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}

	}

	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET.getBytes())
				.parseClaimsJws(resolve(token)).getBody();
		return claims;
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public Integer getId(String token) {
		return (Integer) getClaims(token).get("idUser");
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}

	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			return token.replace(SecurityConstants.TOKEN_PREFIX, "");
		}
		return null;
	}

	@Override
	public String generarToken(UserEntity usuario) {
		Claims claims = Jwts.claims();
		claims.put("idUser", usuario.getId());
        return jwtUtils.generateToken(usuario.getEmail());
	}

}
