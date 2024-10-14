package com.tcs.devops.challenge.auth_service.service;

import org.springframework.stereotype.Service;

import com.tcs.devops.challenge.auth_service.dto.ResponseDTO;
import com.tcs.devops.challenge.auth_service.security.JwtProvider;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String generateToken() {
        return jwtProvider.generateToken();
    }

    public ResponseDTO validateToken(String token) {
        try {
            jwtProvider.validateToken(token);
            return ResponseDTO.builder().message("Token is valid").status(true).build();
        } catch (Exception e) {
            return ResponseDTO.builder().message(e.getMessage()).status(false).build();
        }
    }    
}
