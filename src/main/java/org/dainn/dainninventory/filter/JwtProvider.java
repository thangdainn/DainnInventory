package org.dainn.dainninventory.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.dainn.dainninventory.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtProvider {
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserDTO user) {
        Map<String, Object> claims = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "avatar", user.getAvatar(),
                "role", user.getRoleName(),
                "provider", user.getProvider(),
                "uuid", UUID.randomUUID().toString()
        );
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token);
        return true;
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Integer extractId(String token) {
        if (isTokenExpired(token)) {
            throw new ExpiredJwtException(null, null, "TOKEN EXPIRED");
        }
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }

    public String extractUUID(String token) {
        return extractClaim(token, claims -> claims.get("uuid", String.class));
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
