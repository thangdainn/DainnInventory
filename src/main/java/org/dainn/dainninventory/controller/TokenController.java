package org.dainn.dainninventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.service.ITokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final ITokenService tokenService;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(bearer) || !bearer.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is required");
        }
        String refreshToken = bearer.substring(7);
        return ResponseEntity.ok(tokenService.handleRefreshToken(refreshToken, response));
    }


}
