package com.rental_api.rental.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class JwtUtils {

    @Value("${SPRING_JWT_SECRET}")
    private String secretKey;

    @Value("${SPRING_JWT_EXPIRE}")
    private long jwtExpirationMs;

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
        claims.put("roles", roles);
        claims.put("roleIds", roleIds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // ================= PARSE TOKEN =================
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
        Object obj = getClaims(token).get("userId");
        return obj instanceof Number ? ((Number) obj).longValue() : null;
    }

    public String extractEmail(String token) {
        Object obj = getClaims(token).get("email");
        return obj instanceof String ? (String) obj : null;
    }

    public List<String> extractRoles(String token) {
        Object obj = getClaims(token).get("roles");
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public List<Long> extractRoleIds(String token) {
        Object obj = getClaims(token).get("roleIds");
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .filter(Number.class::isInstance)
                    .map(o -> ((Number) o).longValue())
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // ================= VALIDATE =================
    public boolean validateToken(String token) {
        return !getClaims(token).getExpiration().before(new Date());
    }
}
