package com.tcs.devops.challenge.auth_service.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;

import org.springframework.beans.factory.annotation.Value;

import com.tcs.devops.challenge.auth_service.exception.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.subject}")
    private String jwtSubject;

    
    private static final long EXPIRATION_TIME = 60000;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateToken() {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + EXPIRATION_TIME);

        SecretKey key = getSecretKey();

        return Jwts.builder()
                .subject(jwtSubject)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key)
                .compact();
      }

      public boolean validateToken(String token) {
        try {
            SecretKey key = getSecretKey();
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);            
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
        }
    }
}
