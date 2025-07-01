package com.exam.tomatoback.infrastructure.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    private final Long accessExpireMs;
    private final Long refreshExpireMs;

    public JwtUtil(
            @Value("${spring.security.jwt}")String secret,
            @Value("${spring.security.access}") Long accessHour,
            @Value("${spring.security.refresh}")Long refreshWeek
    ) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpireMs = Duration.ofHours(accessHour).toMillis();
        refreshExpireMs = Duration.ofDays(refreshWeek).toMillis();
    }

    private Claims getPayload(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String getUsername(String token) {
        return getPayload(token).getSubject();
    }

    public String getRole(String token) {
        return getPayload(token).get("roles", String.class);
    }

    public String getCategory(String token) {
        return getPayload(token).get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return getPayload(token).getExpiration().before(new Date());
    }

    public Boolean validate(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    public String getAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessExpireMs, "access");
    }

    public String getRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshExpireMs, "refresh");
    }

    private String generateToken(UserDetails userDetails, Long expireMs, String category) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        claims.put("category", category);
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(secretKey)
                .compact();
    }
}
