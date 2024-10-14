package com.tcs.devops.challenge.auth_service.controller;

import org.springframework.http.ResponseEntity;

import com.tcs.devops.challenge.auth_service.dto.ResponseDTO;
import com.tcs.devops.challenge.auth_service.dto.TokenDTO;
import com.tcs.devops.challenge.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
     private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token")
    public ResponseEntity<TokenDTO> generateToken() {
        TokenDTO token = new TokenDTO(authService.generateToken()); 
        return ResponseEntity.ok(token);
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseDTO> validateToken(@RequestParam String token) {
        ResponseDTO response = authService.validateToken(token);
        if(response.isStatus()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

}
