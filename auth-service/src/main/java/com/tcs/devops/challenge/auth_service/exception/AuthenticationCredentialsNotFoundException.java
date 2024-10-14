package com.tcs.devops.challenge.auth_service.exception;

public class AuthenticationCredentialsNotFoundException extends RuntimeException {

    public AuthenticationCredentialsNotFoundException(String message) {
        super(message);
    }
    
}
