package com.ccamargo.reserve.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SecurityConstants {

    @Value("${tokenSecret}")
    private String tokenSecret;

    public static String TOKEN_SECRET;
    public static final long EXPIRATION_DATE = 86400000; // 1 día en milisegundos
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostConstruct
    public void init() {
        TOKEN_SECRET = tokenSecret;
    }

}
