package org.tasker.usermanagementservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.tasker.usermanagementservice.security.utils.UserSecurityUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@Component
public class JwtProvider {

    private SecretKey secret;

    @Value("${taskmanager.security.jwt.expiration}")
    private long expiration;

    @Value("${taskmanager.security.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    public JwtProvider(@Value("${taskmanager.security.jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(UserDetails user) {
        return generateToken(generateClaims(user), user);
    }

    private Map<String, Object> generateClaims(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities());
        return claims;
    }

    public String generateToken(Map<String, Object> claims, UserDetails user) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    public String generateRefreshToken() {
        return UserSecurityUtils.generateRandomString(64);
    }

    public Optional<Claims> extractClaimsFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getUsernameFromToken(String token) {
        Optional<Claims> claims = extractClaimsFromToken(token);
        if (claims.isPresent()) {
            return claims.get().getSubject();
        }
        return "empty";
    }

    public boolean validateToken(String token) {
        return extractClaimsFromToken(token).isPresent();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length()).trim();
        }
        return null;
    }

    public Instant getRefreshTokenExpirationDate() {
        return Instant.now().plusMillis(refreshTokenExpiration);
    }

}
