package com.rental_api.rental.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Getter
@Setter
public class JwtUtils {

    @Value("${SPRING_JWT_SECRET}")
    private String secretKey;

    @Value("${SPRING_JWT_EXPIRE}")
    private long jwtExpirationMs; // e.g., 604800000 = 7 days

    // ================= GENERATE TOKEN =================
    public String generateToken(
            Long userId,
            String username,
            String email,
            List<String> roles,
            List<Long> roleIds
    ) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("roles", roles);       // include roles
        claims.put("roleIds", roleIds);   // include role IDs

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // ================= EXTRACT CLAIMS =================
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Object userIdObj = getClaims(token).get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        return null;
    }

    public String extractEmail(String token) {
        Object emailObj = getClaims(token).get("email");
        if (emailObj instanceof String) {
            return (String) emailObj;
        }
        return null;
    }

    public List<String> extractRoles(String token) {
        Object rolesObj = getClaims(token).get("roles");
        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .filter(o -> o instanceof String)
                    .map(o -> (String) o)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public List<Long> extractRoleIds(String token) {
        Object roleIdsObj = getClaims(token).get("roleIds");
        if (roleIdsObj instanceof List<?>) {
            return ((List<?>) roleIdsObj).stream()
                    .filter(o -> o instanceof Number)
                    .map(o -> ((Number) o).longValue())
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // ================= VALIDATE TOKEN =================
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
