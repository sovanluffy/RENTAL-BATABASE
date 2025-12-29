package com.rental_api.rental.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${SPRING_JWT_SECRET}")
    private String secretKey;

    @Value("${SPRING_JWT_EXPIRE}")
    private String jwtExpiration; // e.g., "7d"

    public String generateToken(String username) {
        long expireMillis = 604800000L; // default 7 days
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireMillis))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
