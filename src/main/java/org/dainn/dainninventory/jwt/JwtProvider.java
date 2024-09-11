package org.dainn.dainninventory.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.dainn.dainninventory.dto.UserDTO;
import org.dainn.dainninventory.service.IUserService;
import org.dainn.dainninventory.utils.constant.JwtConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
//@RequiredArgsConstructor
public class JwtProvider {
    @Autowired
    private IUserService userService;
    public final Key secretKey = new SecretKeySpec(JwtConstant.JWT_SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public String generateToken(String email, Provider provider) {
        UserDTO userDTO = userService.findByEmailAndProvider(email, provider);
        Map<String, Object> claims = Map.of(
                "id", userDTO.getId(),
                "email", userDTO.getEmail(),
                "name", userDTO.getName(),
                "provider", userDTO.getProvider().name(),
                "role", userDTO.getRolesName()
        );
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.JWT_EXPIRATION))
                .signWith(secretKey)
                .compact();

    }
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token);
        return true;
    }

    public String getEmailFromJwt(String token) {
        if (isTokenExpired(token)) {
            throw new ExpiredJwtException(null, null, "Token is expired");
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody()
                .get("email", String.class);
    }

    public String getProviderFromJwt(String token) {
        if (isTokenExpired(token)) {
            throw new ExpiredJwtException(null, null, "Token is expired");
        }
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody()
                .get("provider", String.class);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody()
                .getExpiration()
                .before(new Date());
    }
}
